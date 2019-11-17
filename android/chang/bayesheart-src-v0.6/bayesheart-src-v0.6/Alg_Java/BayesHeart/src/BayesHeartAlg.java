
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import BayesHeart_general.HMM;
import com.mathworks.toolbox.javabuilder.MWArray;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;


public class BayesHeartAlg {
	/**
	 * This class takes the signal after the noise reduction step as input 
	 * and outputs heart rate estimations by using BayesHeart algorithm. 
	 * 
	 * 
	 * Set "path" to the absolute path to the folder that has Y/R/G/B_ready.csv files
	 * Set "sourceFile" to "Y"/"R"/"ICA"/"PCA" to select signals with different noise reduction techniques
	 * 
	 * Input:	Y/R/ICA/PCA_ready.csv
	 *
	 * Output:	Y/R/ICA/PCA_BayesHeart_hr.csv
	 * 			
	 */
	static String path = "C:/.../BayesHeart/Data/1.1/";//path to the folder containing Y/R/G/B_ready.csv files, e.g., "C:/.../BayesHeart/Data/1.1/"
	public String sourceFile="Y";//choose from: "R"/"Y"/"ICA"/"PCA

	private ArrayList<ArrayList<DataItem>> validData;
	private ArrayList<BpmRecord> bpm_hmm_Records;
	BufferedReader br = null;
	String sCurrentLine = "";
	int LEAST_PIECE_SIZE=20;
	BufferedWriter bw1=null;
	long interval_lb=300;// hr= 60000/interval
	long interval_ub=1500;	
	static int chunkNo=6;
	static double thres=10;
	
	
	public BayesHeartAlg() throws IOException
	{
		validData=new ArrayList<ArrayList<DataItem>>();
		bpm_hmm_Records=new ArrayList<BpmRecord>();
		
		br = new BufferedReader(new FileReader(path+sourceFile+"_ready.csv"));
		long t=0;
		double v=0;
		ArrayList<DataItem> lastPiece=new ArrayList<DataItem>();
		
		if((sCurrentLine = br.readLine()) != null)
		{
		
			StringTokenizer st = new StringTokenizer(sCurrentLine, ",");
			if(st.hasMoreTokens()){
				t = Long.parseLong(st.nextToken());
				if(st.hasMoreTokens()){
					v = Double.parseDouble(st.nextToken());
				}
			}
			DataItem item=new DataItem(t,v);
			lastPiece.add(item);
		}
		
		while ((sCurrentLine = br.readLine()) != null) 
		{
		
			StringTokenizer st = new StringTokenizer(sCurrentLine, ",");
			if(st.hasMoreTokens()){
				t = Long.parseLong(st.nextToken());
				if(st.hasMoreTokens()){
					v = Double.parseDouble(st.nextToken());
				}
			}
			DataItem item=new DataItem(t,v);
			lastPiece.add(item);
		}
		if(lastPiece.size()>LEAST_PIECE_SIZE)
		{
			validData.add(lastPiece);
		}
		br.close();
	}
	
	
	public void calculate_observation()
	{
		
		for(int i=0;i<validData.size();i++)
		{
			ArrayList<DataItem> currentPiece=validData.get(i);
			
			for(int j=1;j<currentPiece.size()-1;j++)
			{
				if((currentPiece.get(j).getvalue()>=currentPiece.get(j-1).getvalue())&&currentPiece.get(j+1).getvalue()>=currentPiece.get(j).getvalue())
				{
					//increasing
					currentPiece.get(j).setobs(1);				  
				}
				else if((currentPiece.get(j).getvalue()<=currentPiece.get(j-1).getvalue())&&currentPiece.get(j+1).getvalue()<=currentPiece.get(j).getvalue())
				{
					//decreasing
					 currentPiece.get(j).setobs(2);
				}
				else if((currentPiece.get(j).getvalue()>=currentPiece.get(j-1).getvalue())&&currentPiece.get(j+1).getvalue()<=currentPiece.get(j).getvalue())
				{
					//local maximum
					currentPiece.get(j).setobs(3); 
				}
				else
				{
					//local minimum
					currentPiece.get(j).setobs(4);
				}
			}
			
		}
	}
	
	
	public void calculate_states() throws MWException
	{
		for(int i=0;i<validData.size();i++)
		{
			ArrayList<DataItem> currentPiece=validData.get(i);
			int[] seq=new int[currentPiece.size()-2];
			for(int j=0;j<seq.length;j++)
			{
				seq[j]=currentPiece.get(j+1).getobs();
			}
			
			HMM hmmtool=new HMM();
			Object[] r = hmmtool.GetHiddenStates_general(1, seq);
			MWNumericArray n=(MWNumericArray)r[0];
			double[] result=new double[n.numberOfElements()];
	         for(int k=1;k<=n.numberOfElements();k++)
	         {
	        	 result[k-1]=n.getFloat(k);
	        	 currentPiece.get(k).setstate(result[k-1]);
	         }
	       
	         MWArray.disposeArray(r);
	         MWArray.disposeArray(n);
	         hmmtool.dispose();
	        	         
		}
	}
	
	
	public void calculate_heartrate() throws IOException, MWException
	{
		for(int i=0;i<validData.size();i++)
		{
			ArrayList<DataItem> currentPiece=validData.get(i);
			int beginingIndex=0;
			int flag=0;//0:searching for next beginning; 1: not searching for next beginning 
			
			for(int j=1;j<currentPiece.size()-1;j++)
			{
				if( currentPiece.get(j).getstate()==2.0)
				{
					beginingIndex=j;
					flag=1;
					break;
				}
			}
			double vallay_beginning=0;
			double peak=0;
			double vallay_ending=0;
			double state1_low=0;
			double state1_high=0;
			double state3_low=0;
			double state3_high=0;
			boolean searching3=true;
			int count_state1=0;
			int count_otherstates=0;
			int previousBpm=0;
			
			for(int j=beginingIndex+1;j<currentPiece.size()-1;j++)
			{
				if(currentPiece.get(j).getstate()==3.0){
					count_otherstates++;
				}
				if(currentPiece.get(j).getstate()==4.0){
					count_otherstates++;
					if(searching3){
						state3_low=currentPiece.get(j).getvalue();
						searching3=false;
					}
				}
				if(currentPiece.get(j).getstate()==5.0){
					count_otherstates++;
					if(!searching3){
						searching3=true;
						state3_high=currentPiece.get(j-1).getvalue();
					}
				}
				
				if(flag==0 && currentPiece.get(j).getstate()==2.0)
				{
					//new interval: from beginingIndex to j
					long interval=currentPiece.get(j).gettime()-currentPiece.get(beginingIndex).gettime();
					vallay_ending=currentPiece.get(j-1).getvalue();

					if(interval>interval_lb && interval<interval_ub 
							&&(state1_high-state1_low)>(state3_high-state3_low)
							 &&count_otherstates>count_state1
						&&(peak-vallay_beginning)<(peak-vallay_ending)*3 && (peak-vallay_beginning)>(peak-vallay_ending)/3) 
					{

						int bpm=(int) (60000/interval);
						int currentbpm=bpm;
						if(previousBpm!=0){
							bpm=(bpm+previousBpm)/2;
							
						}
						previousBpm=currentbpm;
						BpmRecord newRecord=new BpmRecord(currentPiece.get(j).gettime(),bpm);
						//System.out.println(currentPiece.get(j).gettime()+" : "+bpm);
						bpm_hmm_Records.add(newRecord);
				
					}
					else{
						previousBpm=0;
					}

					beginingIndex=j;
					flag=1;
					state1_low=0;
					state1_high=0;
					state3_low=0;
					state3_high=0;
					count_state1=0;
					count_otherstates=0;
					
					vallay_beginning=currentPiece.get(j-1).getvalue();
					state1_low=currentPiece.get(j).getvalue();
					count_state1++;
					
				}
				if(flag==1 && currentPiece.get(j).getstate()==2.0){
					 count_state1++;
				}
				else if(flag==1 && currentPiece.get(j).getstate()!=2.0)
				{
					flag=0;
					state1_high=currentPiece.get(j-1).getvalue();
					peak=currentPiece.get(j-1).getvalue();	
				}	
			}
		}
	
		bpm_hmm_Records=postProcessing(bpm_hmm_Records);
		
		bw1 = new BufferedWriter(new FileWriter(path+ sourceFile+ "_BayesHeart_hr" + ".csv"));
		for(int i=0;i<bpm_hmm_Records.size();i++)
		{
			bw1.write(bpm_hmm_Records.get(i).getTime()+","+bpm_hmm_Records.get(i).getBpm());
			System.out.println(String.valueOf(bpm_hmm_Records.get(i).getTime())+","+String.valueOf((int)(bpm_hmm_Records.get(i).getBpm())));
			bw1.newLine();
		}
		bw1.close();
		
	}
	
	public static ArrayList<BpmRecord> postProcessing(ArrayList<BpmRecord> BeatRecords){
		ArrayList<BpmRecord> BeatRecords_new=new ArrayList<BpmRecord>();
		double[] chunk=new double[chunkNo];
		for(int i=chunkNo;i<BeatRecords.size();i++)
		{
			for(int j=0;j<chunkNo;j++)
			{
				chunk[j]=BeatRecords.get(i-chunkNo+j).getBpm();
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
			if(((double)Math.abs(BeatRecords.get(i).getBpm()-ave))<=thres)//the new bpm is good
			{		
				double newbpm=(chunk[chunk.length/2-1]+chunk[chunk.length/2]+BeatRecords.get(i).getBpm())/3;
				BpmRecord newrecord=new BpmRecord(BeatRecords.get(i).getTime(),(int)newbpm);
				BeatRecords_new.add(newrecord);		
			}
		}
		
		return BeatRecords_new;
	}
	

	public static void main(String[] arg0) throws MWException, IOException
	{
		
		BayesHeartAlg alg=new BayesHeartAlg();
		alg.calculate_observation();
		alg.calculate_states();	
		alg.calculate_heartrate();		
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
