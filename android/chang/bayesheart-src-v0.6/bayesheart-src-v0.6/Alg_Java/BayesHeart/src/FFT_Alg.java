
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import FFT.FastFourTran;
import com.mathworks.toolbox.javabuilder.MWArray;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

public class FFT_Alg {
	/**
	 * This class takes the signal after the noise reduction step as input 
	 * and outputs heart rate estimations by using FFT algorithm. 
	 * 
	 * Set "path" to the absolute path to the folder that has Y/R/G/B_ready.csv files
	 * Set "file" to "Y"/"R"/"ICA"/"PCA" to select signals with different noise reduction techniques
	 * 
	 * Input:	Y/R/ICA/PCA_ready.csv
	 *
	 * Output:	Y/R/ICA/PCA_FFT_hr.csv
	 * 			
	 */
	
	static String path = "C:/.../BayesHeart/Data/1.1/";//path to the folder containing Y/R/G/B_ready.csv files, e.g., "C:/.../BayesHeart/Data/1.1/"
	static String file = "Y";//choose from: "R"/"Y"/"ICA"/"PCA
	
	static int Fs= 20;
	static int windowSize=7;
	static int totalwindow=Fs*windowSize;
	static int overlap=2; 
	static int chunkNo=6;
	static double thres=15;
	static BufferedReader br;
	static BufferedWriter wr;
	static long[] time;
	static double beatRate;
	static double[] matrix;
	static double[] result_fft;
	
	
	public static void normalize() throws IOException, MWException {
		
		int initialsize=20000;
		double[] rawYValues=new double[initialsize];
		time=new long[initialsize];
		int sampleLength=0;
		br=new BufferedReader(new FileReader(path+file+"_ready.csv"));
		String sCurrentLine="";
		long t=0;
		double y=0;
		sCurrentLine = br.readLine();
		while(sCurrentLine!=null){
			sampleLength++;
			StringTokenizer st = new StringTokenizer(sCurrentLine, ",");
			if(st.hasMoreTokens()){			
				t = Long.parseLong(st.nextToken());
				time[sampleLength-1]=t;
				if(st.hasMoreTokens()){
					y = Double.parseDouble(st.nextToken());
					rawYValues[sampleLength-1]=y;
					//System.out.println(y);
				}
			}
			sCurrentLine=br.readLine();
		}
		br.close();
		double[] newYValues=new double[sampleLength];
		
		double mean=0;
		double SD=0;


		for(int i=0; i<sampleLength; i+=overlap*Fs){
			//index begin with i; end with i+totalwindow or sampleLength
			if((i+totalwindow)>(sampleLength-1)){
				mean=getMean(rawYValues,sampleLength-1-totalwindow , sampleLength-1);
				SD=getSD(rawYValues,sampleLength-1-totalwindow , sampleLength-1);
			}
			else{
				mean=getMean(rawYValues,i , i+totalwindow);
				SD=getSD(rawYValues,i , i+totalwindow);
			}
	
			for(int j=i;j<Math.min(sampleLength-1, i+overlap*Fs); j++){
				newYValues[j]=rawYValues[j]-mean;
			}
		}
		
		matrix=new double[newYValues.length-1];
		for(int i=0;i<newYValues.length-1;i++){
			matrix[i]=newYValues[i];
		}
	}
	
	public static double getMean(double[] rawValues, int beginIndex, int endIndex ){
		
		double total=0;
		for(int i=beginIndex; i<=endIndex; i++){
			total+=rawValues[i];
		}
		double mean=total/(endIndex-beginIndex+1);
		return mean;
		
	}
	public static double getSD(double[] rawValues, int beginIndex, int endIndex){
		
		double mean=getMean(rawValues, beginIndex, endIndex);
		double totaldeviation=0;
		for(int i=beginIndex;i<=endIndex;i++){
			totaldeviation+=Math.pow(rawValues[i]-mean, 2);
		}
		double SD=Math.sqrt(totaldeviation/(endIndex-beginIndex+1));
		return SD;
	}
	
	public static void fft_call(double[][] channel,int begin,int end) throws MWException{

		FastFourTran fftTool=new FastFourTran();
		double[][] com=new double[end-begin+1][1];
		if(end<=channel.length-1)
		{
		for(int i=0;i<=end-begin;i++){
			com[i][0]=channel[begin+i][0];
		}
		Object[] ob= fftTool.HeartRateFFT(1, com);
		MWNumericArray n_fft=(MWNumericArray)ob[0];
		result_fft=new double[n_fft.numberOfElements()];
		 for(int k=1;k<=n_fft.numberOfElements();k++)
         {
        	 result_fft[k-1]=n_fft.getFloat(k);	
         }
		 beatRate=result_fft[0];
		 MWArray.disposeArray(ob);
         MWArray.disposeArray(n_fft);
         fftTool.dispose();
		}
	}
	
	public static void fft() throws MWException, IOException{
		System.out.println("FFT started");
		ArrayList<RGBDataItem> BeatRecords=new ArrayList<RGBDataItem>();
		
		double[][] channel=new double[matrix.length][1];
		for(int j=0;j<matrix.length;j++)
		{
			channel[j][0]=matrix[j];
		}
			
		fft_call(channel,0,matrix.length-1);
		for(int i=0;i<result_fft.length;i++){
			RGBDataItem newRecord=new RGBDataItem(time[Fs*windowSize+(i-1)*overlap*Fs],result_fft[i]);
			if(newRecord.getBeat()>40 && newRecord.getBeat()<200){
			BeatRecords.add(newRecord);	
			}
		}
		
		wr=new BufferedWriter(new FileWriter(path+file+"_FFT_hr.csv"));
		BeatRecords=postProcessing(BeatRecords);
		for(int i=0;i<BeatRecords.size();i++)
		{
			RGBDataItem rec=BeatRecords.get(i);
			wr.write(String.valueOf(rec.gettime())+","+String.valueOf((int)(rec.getBeat())));
			System.out.println(String.valueOf(rec.gettime())+","+String.valueOf((int)(rec.getBeat())));
			wr.newLine();
		}
		wr.close();
		System.out.println("FFT finished");
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
			if(((double)Math.abs(BeatRecords.get(i).getBeat()-ave))<=thres)
			{
				double newbpm=(chunk[chunk.length/2-1]+chunk[chunk.length/2]+BeatRecords.get(i).getBeat())/3;
				RGBDataItem newrecord=new RGBDataItem(BeatRecords.get(i).gettime(),newbpm);
				BeatRecords_new.add(newrecord);
			}
		}
		return BeatRecords_new;
	}
	
	public static void main(String[] args) throws IOException, MWException {
		
		normalize();
		fft();
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

