

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.StringTokenizer;


public class LivePulse {
	/**
	 * This class takes the signal after the noise reduction step as input 
	 * and outputs heart rate estimations by using LivePulse algorithm. 
	 * 
	 * The LivePulse algorithm is a heuristic based outlier removal and 
	 * local peaks/valleys counting algorithm. 
	 * It can be treated as an optimized, temporal domain adaptive thresholding algorithm.
	 * 
	 * Set "path" to the absolute path to the folder that has Y/R/G/B_ready.csv files
	 * Set "filename" to "Y"/"R"/"ICA"/"PCA" to select signals with different noise reduction techniques
	 * 
	 * Input:	Y/R/ICA/PCA_ready.csv
	 *
	 * Output:	Y/R/ICA/PCA_LivePulse_hr.csv
	 * 			
	 */
	
	static String path = "C:/.../BayesHeart/Data/1.1/";//path to the Y/R/G/B_ready.csv files, e.g., "C:/.../BayesHeart/Data/1.1/"
	static String filename="Y";//to choose signals with different noise reduction techniques (i.e. "R"/"Y"/"ICA"/"PCA")
	
	
	static BufferedReader br1 = null;
	static String sCurrentLine = "";

	static final int MAX_PEAKS = 12500;
	static final int MAX_SAMPLES = 50000;
	static final int TEMP_SIZE = 100;
	
	static final int MAX_HEART_RATE = 200;
	static final int MIN_HEART_RATE = 40;	

	static final int MOVE_DOWN = -1;
	static final int MOVE_UNDEFINED = 0;
	static final int MOVE_UP = 1;

	static int badBeatRR;


	static int calculatedHr;
	static int calculatedHrQuality;
	static public int currentFps;
	static int ppg_direction;
	static int errorBeatAllowedAfterNoBeats;
	static int errorCounter;
	static int firstGoodRRIdx;
	static int fpsCounter;
	static long fpsLastTimeStamp;
	static int goodRRCnt;
	static int goodRRTime;
	static int goodUninteruptedRRCnt;
	static public int heartRateLimit;
	final static int hrMax = 30;
	final static int hrMin = 4;
	static int lastBadBeatIdx;
	static int lastProcessedCrossSample;
	static int maxAveragingTime;
	static int maxBadBeats;	
	static long maxTime;
	static int maxTimeVariability;

	static int shanonFactor;
	static int thD;
	static int totalMeasuringTime;

	static int valueDifferenceTriggerFactor;

	static int zcDiscriminatorTime;
	static int zcMaxShootDifference;
	static int[] zcRR;
	static double[] zcShot;
	
	static ArrayList<RGBDataItem> BeatRecords;
	static int chunkNo=6;//post-processing
	static double thres=15;
	
    static private ArrayList<DataSample> samples;
	
    public static void main (String[] args) {
		heartRateLimit = MAX_HEART_RATE;
		currentFps = 30;
		thD = 4;
		maxTime = 1500;
		zcDiscriminatorTime = 1000;
		zcMaxShootDifference = 50;

		samplesTimestamp = new long[50000];
		samplesBVP = new double[50000];
		sample_count = 0;

		peakvalleyTimestamp = new long[12500]; 
		peakvalleyBVP = new double[12500];
		peakvalley_count = 0;

		validPeaksTimestamp = new long[20000]; 
		validPeaksBVP = new double[20000];
		valid_peak_count = 0;

		zcTimestamp = new long[20000];
		zcBVP = new double[20000];
		zcShot =  new double[20000];
		zcRR = new int[20000];
		zc_count = 0;

		bpmTimestamp = new long[20000];
		bpmBVP = new int[20000];
		bpm_count = 0;
		lastProcessedCrossSample = 0;

		ppg_direction = MOVE_UNDEFINED;
		fpsLastTimeStamp = 0L;
		fpsCounter = 0;
		shanonFactor = 6;
		maxTimeVariability = 200;
		maxAveragingTime = 25000;
		errorBeatAllowedAfterNoBeats = 5;
		maxBadBeats = 1;
		valueDifferenceTriggerFactor = 5;
		firstGoodRRIdx = -1;
		goodRRCnt = 0;
		errorCounter = 0;
		goodRRTime = 0;
		goodUninteruptedRRCnt = 0;
		badBeatRR = 0;
		totalMeasuringTime = 0;
		lastBadBeatIdx = 0;
		
		calculatedHr = 0;
		calculatedHrQuality = 0;
		
		zcRRB = new boolean[12500];
		BeatRecords=new ArrayList<RGBDataItem>();
		
		samples = new ArrayList<DataSample>(10000);
		
		
		try {
			br1 = new BufferedReader(new FileReader(path + filename+"_ready.csv"));
			
    		while ((sCurrentLine = br1.readLine()) != null) {
    			StringTokenizer st = new StringTokenizer(sCurrentLine, ",");
    			
    			if(st.hasMoreTokens()){
    				long timestamp = Long.parseLong(st.nextToken());
    				double pulse_value = Double.parseDouble(st.nextToken());
    				
    				//System.out.println("addSample : " + timestamp + "," + pulse_value);
    				
    				samplesTimestamp[sample_count] = timestamp;
    				samplesBVP[sample_count] = pulse_value;

    				if (timestamp - fpsLastTimeStamp > 1000)
    				{
    					currentFps = fpsCounter;
    					fpsCounter = 0;
    					fpsLastTimeStamp = timestamp;

    					heartRateLimit = Math.min(MAX_HEART_RATE, currentFps * 60 / shanonFactor);

    					//System.out.println("currentFps = " + currentFps + " Heart rate Limit = " + heartRateLimit);
    				}

    				fpsCounter += 1;
    				sample_count += 1;

    				if (sample_count < 2) 
    				{
    					continue;
    				}

    				processOneSample();
    			}
    		}
		} catch (IOException e){
    		e.printStackTrace();
    	} finally {
			try {
				if (br1 != null)br1.close();
				save();
				
				float counter = 0;
	    		
	    		for(int i = 1; i < zc_count; i++){
	    			if(zcRRB[i] == false){
						long start_time = zcTimestamp[i-1];
						long end_time = zcTimestamp[i];
						
						for( int j = 0; j < sample_count; j++ ) {
							if( samplesTimestamp[j] > start_time && samplesTimestamp[j] <= end_time ) {
								counter++;
							}
							
							if(samplesTimestamp[j] > end_time)
								break;
						}
	    			}
	    		}
	    		
	    		//System.out.println(""+(1-counter/sample_count));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
    }

	private static void processOneSample() {
		switch ( ppg_direction )
		{
		case MOVE_UP:
			if (samplesBVP[sample_count - 1] >= samplesBVP[sample_count - 2])
				return;

			addPeakValley(samplesTimestamp[sample_count - 2], samplesBVP[sample_count - 2]);

			ppg_direction = MOVE_DOWN;
			break;
		case MOVE_DOWN:
			if (samplesBVP[sample_count - 1] <= samplesBVP[sample_count - 2])
				return;

			addPeakValley(samplesTimestamp[sample_count - 2], samplesBVP[sample_count - 2]);

			ppg_direction = MOVE_UP;
			break;
		default:
			if (samplesBVP[sample_count - 1] < samplesBVP[sample_count - 2])
			{
				ppg_direction = MOVE_DOWN;
				break;
			}
			ppg_direction = MOVE_UP;
		}
	}
	
	public static void addPeakValley(long timestamp, double pulse_value)
	{
		//System.out.println("addPeak : " + timestamp + "," + pulse_value);

		peakvalleyTimestamp[peakvalley_count] = timestamp;
		peakvalleyBVP[peakvalley_count] = pulse_value;

		validPeaksTimestamp[valid_peak_count] = timestamp;
		validPeaksBVP[valid_peak_count] = pulse_value;

		peakvalley_count += 1;
		valid_peak_count += 1;

		if (valid_peak_count < 4)
		{
			//System.out.println("addPeak : Step 1");
			
			return; // we need at least 4 peaks for the follow up calculations
		}

		double amp1 = validPeaksBVP[(valid_peak_count - 4)] - validPeaksBVP[(valid_peak_count - 3)];
		double amp2 = validPeaksBVP[(valid_peak_count - 3)] - validPeaksBVP[(valid_peak_count - 2)];
		double amp3 = validPeaksBVP[(valid_peak_count - 2)] - validPeaksBVP[(valid_peak_count - 1)];

		long t1 = validPeaksTimestamp[(valid_peak_count - 3)] - validPeaksTimestamp[(valid_peak_count - 4)];
		long t2 = validPeaksTimestamp[(valid_peak_count - 2)] - validPeaksTimestamp[(valid_peak_count - 3)];

		long last_span = t1 + t2;

		if (Math.abs(amp2) < Math.abs(amp1 / thD)) // amp2 is very very small, most likely it's a noise
		{
			if (last_span < maxTime)
			{
				if (Math.abs(amp3) > Math.abs(amp2))
				{
					//System.out.println("deletePeak : " + validPeaksT[(valid_peak_count - 3)] + "," + validPeaksV[(valid_peak_count - 3)]);
					
					validPeaksBVP[(valid_peak_count - 3)] = validPeaksBVP[(valid_peak_count - 1)];
					validPeaksTimestamp[(valid_peak_count - 3)] = validPeaksTimestamp[(valid_peak_count - 1)];
					
					//System.out.println("deletePeak : " + validPeaksT[(valid_peak_count - 2)] + "," + validPeaksV[(valid_peak_count - 2)]);
				} else {
					//System.out.println("deletePeak : " + validPeaksT[(valid_peak_count - 1)] + "," + validPeaksV[(valid_peak_count - 1)]);
					//System.out.println("deletePeak : " + validPeaksT[(valid_peak_count - 2)] + "," + validPeaksV[(valid_peak_count - 2)]);
				}
				
				valid_peak_count -= 2;

				return;
			}
		}
		if (amp1 >= 0)
		{
			//System.out.println("addPeak : Step 2");
			return;
		}

		if (samplesTimestamp[lastProcessedCrossSample] >= validPeaksTimestamp[(valid_peak_count - 4)])
		{
			//System.out.println("addPeak : Step 3");
			return;
		}

		double zc_amp = (validPeaksBVP[(valid_peak_count - 4)] + validPeaksBVP[(valid_peak_count - 3)]) / 2;

		while ((lastProcessedCrossSample < sample_count) && (samplesTimestamp[lastProcessedCrossSample] < validPeaksTimestamp[(valid_peak_count - 4)]))
		{
			lastProcessedCrossSample += 1;
		}
		//System.out.println("addPeak : Step 4");

		while ((lastProcessedCrossSample < sample_count) && (samplesBVP[lastProcessedCrossSample] < zc_amp))
		{
			lastProcessedCrossSample += 1;
		}

		double change = samplesBVP[lastProcessedCrossSample] - samplesBVP[(lastProcessedCrossSample - 1)];

		if (change == 0) 
		{
			change = 1;
		}
		
		double ratio = (zc_amp - samplesBVP[(lastProcessedCrossSample - 1)]) * 1000 / change;

		long whole_step = samplesTimestamp[lastProcessedCrossSample] - samplesTimestamp[(lastProcessedCrossSample - 1)];

		// getting the timestamp information of the zero crossing point by interprotaion
		
		long time = (long)(samplesTimestamp[(lastProcessedCrossSample - 1)] + ratio * whole_step / 1000L);

		addZeroCross(time, zc_amp, validPeaksBVP[(valid_peak_count - 3)] - validPeaksBVP[(valid_peak_count - 4)]);
		
		//System.out.println("addPeak : Step 5");
	}
	
	public static void addZeroCross(long timestamp, double pulse_value, double shot_value)
	{
		//System.out.println("addZeroCross : " + timestamp + "," + pulse_value + "," + shot_value);

		zcTimestamp[zc_count] = timestamp;
		zcBVP[zc_count] = pulse_value;

		zcShot[zc_count] = shot_value;
		zc_count += 1;
		
		if (zc_count == 1)
		{
			return;
		}
		
		if (zcShot[(zc_count - 2)] * zcMaxShootDifference / 100 > shot_value )
		{
			if (timestamp - zcTimestamp[(zc_count - 2)] < zcDiscriminatorTime)
			{
				//System.out.println("deleteZeroCross," + zcT[zc_count-1] + "," + zcV[zc_count-1] + "," + zcShot[zc_count-1]);
				zc_count -= 1;
				
				return;
			}
		}

		zcRR[(zc_count - 1)] = (int)(timestamp - zcTimestamp[(zc_count - 2)]);
		
		int rawBpm = 60000/zcRR[(zc_count - 1)];
		//System.out.println("addRawBpm," + timestamp + "," + rawBpm);
		
		if(rawBpm>40 && rawBpm<200){
			RGBDataItem newRecord=new RGBDataItem(timestamp,rawBpm);
			BeatRecords.add(newRecord);		
		}
		addBpm(timestamp,rawBpm);
		
	}
	
	
	private static void addBpm(long timestamp, int heart_rate)
	{
		//System.out.println("addBpm," + timestamp + "," + heart_rate);
		DataSample sample = new DataSample(timestamp, heart_rate);
		samples.add(sample);
		
		bpmTimestamp[bpm_count] = timestamp;
		bpmBVP[bpm_count] = heart_rate;
		
		bpm_count++;
	}
	
    public static void save() throws IOException
    {
    	BeatRecords=postProcessing(BeatRecords);
    	BufferedWriter wr=new BufferedWriter(new FileWriter(path+filename+"_LivePulse_hr.csv"));
    	for(int i=0;i<BeatRecords.size();i++)
		{
			RGBDataItem rec=BeatRecords.get(i);
			wr.write(String.valueOf(rec.gettime())+","+String.valueOf((int)(rec.getBeat())));
			System.out.println(String.valueOf(rec.gettime())+","+String.valueOf((int)(rec.getBeat())));
			wr.newLine();
		}
		wr.close();
    	
        return;
    }
    public static ArrayList<RGBDataItem> postProcessing(ArrayList<RGBDataItem> BeatRecords){
		ArrayList<RGBDataItem> BeatRecords_new=new ArrayList<RGBDataItem>();
		double[] chunk=new double[chunkNo];
		for(int i=chunkNo;i<BeatRecords.size();i++)
		{
			for(int j=0;j<chunkNo;j++)
			{
				chunk[j]=BeatRecords.get(i-chunkNo+j).getBeat();
			}
			Arrays.sort(chunk);
			double ave=0;
			double total=0;
			for(int k=1;k<chunkNo-1;k++)
			{
				total=total+chunk[k];
			}
			ave=total/(chunkNo-2);
			int useful_ends=0;
			if(((double)Math.abs(chunk[0]-ave))<=thres )
			{
				total=total+chunk[0];
				useful_ends++;
			}
			if(((double)Math.abs(chunk[chunkNo-1]-ave))<=thres )
			{
				total=total+chunk[chunkNo-1];
				useful_ends++;
			}
			ave=total/(chunkNo-2+useful_ends);
			if(((double)Math.abs(BeatRecords.get(i).getBeat()-ave))<=thres)//the new bpm is good
			{
				double newbpm=(chunk[chunk.length/2-1]+chunk[chunk.length/2]+BeatRecords.get(i).getBeat())/3;
				RGBDataItem newrecord=new RGBDataItem(BeatRecords.get(i).gettime(),newbpm);
				BeatRecords_new.add(newrecord);						
			}
		}
		
		return BeatRecords_new;
	}
    
	static int peakvalley_count;
	static long[] peakvalleyTimestamp;
	static double[] peakvalleyBVP;
	
	static int bpm_count;	
	static long[] bpmTimestamp;
	static int[] bpmBVP;

	static int sample_count;	
	static long[] samplesTimestamp;
	static double[] samplesBVP;  
	
	static int valid_peak_count;
	static long[] validPeaksTimestamp;
	static double[] validPeaksBVP;	
	
	static int zc_count;	
	static long[] zcTimestamp;
	static double[] zcBVP;
	static boolean[] zcRRB;	
}

	class DataSample
	{
		public long timestamp;
		public int value;

    public DataSample(long l, int i)
    {
        timestamp = l;
        value = i;
    }

    public static String toCSV(ArrayList<DataSample> arraylist)
    {
        StringBuilder stringbuilder = new StringBuilder();

        for (Iterator<DataSample> iterator = arraylist.iterator();iterator.hasNext();  )
        {
            DataSample sample = iterator.next();
            
            stringbuilder.append( "" + sample.timestamp + "," + sample.value + "\r\n");
            
            
        }
        
        return stringbuilder.toString();
    }
    
    public long getTimeStamp(){
    	return timestamp;
    }
    
    public int getValue(){
    	return value;
    }
    
    public boolean equals(Object o){
    	DataSample newData = (DataSample)o;
    	
    	if(this.timestamp == newData.getTimeStamp() && this.value == newData.getValue()){
    		return true;
    	} else {
    		return false;
    	}
    }
    
   
}
	

/*
Copyright (c) 2015 Regents of the University of Pittsburgh.
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

1. Redistributions of source code must retain the above copyright
	notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright
	notice, this list of conditions and the following disclaimer in the
	documentation and/or other materials provided with the distribution.

3. All advertising materials mentioning features or use of this software
	must display the following acknowledgement:

		This product includes software developed by the Group for User 
		Interface Research at the University of Pittsburgh.

4. The name of the University may not be used to endorse or promote products 
	derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
*/

