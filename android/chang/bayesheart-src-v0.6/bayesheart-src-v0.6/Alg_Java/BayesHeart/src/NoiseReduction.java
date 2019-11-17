
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import ICA_Updated.ICA;
import PCA_Updated.PCA;

import com.mathworks.toolbox.javabuilder.MWArray;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

public class NoiseReduction {

	/**
	 * This class first applies interpolations on the raw signals, 
	 * then it detects covering actions and concatenates covering sequences,  
	 * then applies different noise reduction techniques (PCA/ICA/using R directly/using Y directly)
	 * and outputs signals after the noise reduction step. 
	 * 
	 * Set "path" to the absolute path to the folder that has Y/R/G/B_raw.csv files before running.
	 * 
	 * Input:	Y_raw.csv
	 * 			R_raw.csv
	 * 			G_raw.csv
	 * 			B_raw.csv
	 * Output:	Y_ready.csv
	 * 			R_ready.csv
	 * 			ICA_ready.csv
	 * 			PCA_ready.csv
	 */
	
	static String path = "C:/.../BayesHeart/Data/1.1/";//path to the Y/R/G/B_raw.csv files, e.g., "C:/.../BayesHeart/Data/1.1/"
	
	static int dataSize=0;
	static int valid_upperBound=-50000;//to detect covering action
	static int valid_lowerBound=-100000;
	static int samplingRate = 20; //Hz
	static long minimum_time_for_valid_covering = 2000;// minimum time threshold for valid covering 
	static BufferedReader br,brR,brG,brB;
	static BufferedWriter wr,wrRGB, wrR, wrG, wrB, wrICA, wrPCA;
	static ArrayList<RGBDataItem> NewData;
	
	
public static void interpolation() throws IOException{
		
		br=new BufferedReader(new FileReader(path + "Y_raw.csv"));
		brR=new BufferedReader(new FileReader(path + "R_raw.csv"));
		brG=new BufferedReader(new FileReader(path + "G_raw.csv"));
		brB=new BufferedReader(new FileReader(path + "B_raw.csv"));
		
		ArrayList<RGBDataItem> RawData=new ArrayList<RGBDataItem>();
		NewData=new ArrayList<RGBDataItem>();
		
		String sCurrentLine="";
		long t=0;
		double r=0;
		double g=0;
		double b=0;
		double y=0;
		int count=0;
		RGBDataItem lastDataItem=new RGBDataItem(t,r,g,b,y);
		long lastTime=0;
		if((sCurrentLine = br.readLine()) != null)//read the first line
		{
			
			StringTokenizer st = new StringTokenizer(sCurrentLine, ",");
			if(st.hasMoreTokens()){
				t = Long.parseLong(st.nextToken());
				
				if(st.hasMoreTokens()){
					y = Double.parseDouble(st.nextToken());
					sCurrentLine = brR.readLine();
					st = new StringTokenizer(sCurrentLine, ",");
					if(st.hasMoreTokens()){
						t=Long.parseLong(st.nextToken());
						if(st.hasMoreTokens()){
							r=Double.parseDouble(st.nextToken());
							
						}
					}
					sCurrentLine = brG.readLine();
					st = new StringTokenizer(sCurrentLine, ",");
					if(st.hasMoreTokens()){
						t=Long.parseLong(st.nextToken());
						if(st.hasMoreTokens()){
							g=Double.parseDouble(st.nextToken());
							
						}
					}
					sCurrentLine = brB.readLine();
					st = new StringTokenizer(sCurrentLine, ",");
					if(st.hasMoreTokens()){
						t=Long.parseLong(st.nextToken());
						if(st.hasMoreTokens()){
							b=Double.parseDouble(st.nextToken());
							
						}
					}	
				}
			}
			RGBDataItem item=new RGBDataItem(t,r,g,b,y);
			RawData.add(item);
			NewData.add(item);		
			lastDataItem=item;
			lastTime=item.gettime();
		}
		while ((sCurrentLine = br.readLine()) != null)
		{
			StringTokenizer st = new StringTokenizer(sCurrentLine, ",");
			if(st.hasMoreTokens()){
				t = Long.parseLong(st.nextToken());
				if(st.hasMoreTokens()){
					y = Double.parseDouble(st.nextToken());
					
					sCurrentLine = brR.readLine();
					st = new StringTokenizer(sCurrentLine, ",");
					if(st.hasMoreTokens()){
						t=Long.parseLong(st.nextToken());
						if(st.hasMoreTokens()){
							r=Double.parseDouble(st.nextToken());
							
						}
					}
					sCurrentLine = brG.readLine();
					st = new StringTokenizer(sCurrentLine, ",");
					if(st.hasMoreTokens()){
						t=Long.parseLong(st.nextToken());
						if(st.hasMoreTokens()){
							g=Double.parseDouble(st.nextToken());
							
						}
					}
					sCurrentLine = brB.readLine();
					st = new StringTokenizer(sCurrentLine, ",");
					if(st.hasMoreTokens()){
						t=Long.parseLong(st.nextToken());
						if(st.hasMoreTokens()){
							b=Double.parseDouble(st.nextToken());
							
						}
					}	
				}
				
				
			}
			RGBDataItem item=new RGBDataItem(t,r,g,b,y);
			
			int sampleInterval=1000/samplingRate;
			for(;lastTime+sampleInterval<=item.gettime();lastTime=lastTime+sampleInterval)
			{
				double lumin=(item.getY()-lastDataItem.getY())/(item.gettime()-lastDataItem.gettime())*(lastTime+sampleInterval-lastDataItem.gettime())+lastDataItem.getY();
				double red=(item.getR()-lastDataItem.getR())/(item.gettime()-lastDataItem.gettime())*(lastTime+sampleInterval-lastDataItem.gettime())+lastDataItem.getR();
				double green=(item.getG()-lastDataItem.getG())/(item.gettime()-lastDataItem.gettime())*(lastTime+sampleInterval-lastDataItem.gettime())+lastDataItem.getG();
				double blue=(item.getB()-lastDataItem.getB())/(item.gettime()-lastDataItem.gettime())*(lastTime+sampleInterval-lastDataItem.gettime())+lastDataItem.getB();
				if((item.gettime()-lastDataItem.gettime())<500){
					NewData.add(new RGBDataItem(lastTime+sampleInterval,red,green,blue,lumin));
					
				}
				
			}
			
			lastDataItem=item;
		}
		dataSize=NewData.size();
		br.close();
		brR.close();
		brG.close();
		brB.close();	
	}
	
	public static void noiseReduction() throws IOException, MWException{
		
		wr=new BufferedWriter(new FileWriter(path + "Y_ready.csv"));
		wrR=new BufferedWriter(new FileWriter(path + "R_ready.csv"));
		wrICA=new BufferedWriter(new FileWriter(path + "ICA_ready.csv"));
		wrPCA=new BufferedWriter(new FileWriter(path + "PCA_ready.csv"));
		
		
		long[] time_sequence = new long[dataSize];
		double[] y_sequence = new double[dataSize];
		double[] r_sequence = new double[dataSize];
		double[] g_sequence = new double[dataSize];
		double[] b_sequence = new double[dataSize];
		
		for(int i=0;i<dataSize;i++){
			time_sequence[i]=NewData.get(i).gettime();
			y_sequence[i]=NewData.get(i).getY();
			r_sequence[i]=NewData.get(i).getR();
			g_sequence[i]=NewData.get(i).getG();
			b_sequence[i]=NewData.get(i).getB();
			
		}
		
		//To detect the covering action
		ArrayList<Integer> breakingIndex = new ArrayList<Integer>();
		int first_valid_index = -1;
		int last_valid_index = -1;
		
		for(int j=0;j<dataSize;j++){
			if(first_valid_index==-1){//looking for first valid point
				if(y_sequence[j]>valid_lowerBound && y_sequence[j]<valid_upperBound){
					first_valid_index = j;
					last_valid_index = j;
					breakingIndex.add(first_valid_index);
				}
			}
			else{//looking for last valid point
				if(y_sequence[j]>valid_lowerBound && y_sequence[j]<valid_upperBound){
					last_valid_index = j;
					if(j==dataSize-1){
						breakingIndex.add(last_valid_index);
					}
				}
				else{
					breakingIndex.add(last_valid_index);
					first_valid_index = -1;
					last_valid_index = -1;
				}
			}
		}
		if(first_valid_index!=-1 && last_valid_index==-1){
			breakingIndex.add(dataSize-1);
		}
		
		//Now the breakingIndex stores the beginning points and ending points of the valid sequences
		
		//To delete the sequence that less than 2 second 
		ArrayList<Integer> validCoveringIndex = new ArrayList<Integer>();
		for(int k=0; k<breakingIndex.size();k=k+2){
			
			if((time_sequence[breakingIndex.get(k+1)]-time_sequence[breakingIndex.get(k)])>minimum_time_for_valid_covering){//valid covering
				validCoveringIndex.add(breakingIndex.get(k));
				validCoveringIndex.add(breakingIndex.get(k+1));
				
			}
		}
		
		//Now the validCoveringIndex stores the beginning points and ending points of the >2s covering sequence
		
		//To remove the begining 1 second of the data
		for(int l=0;l<validCoveringIndex.size();l=l+2){
			int new_starting_index = validCoveringIndex.get(l)+samplingRate;
			validCoveringIndex.remove(l);
			validCoveringIndex.add(l, new_starting_index);
		}

		
		ArrayList<Integer> validCoveringIndex_linking = new ArrayList<Integer>();
		//To get the maximum points of the beginning/ending of each sequence and then link the sequence
		for (int m=0; m<validCoveringIndex.size();m=m+2){
			int starting_index = validCoveringIndex.get(m);
			int ending_index = validCoveringIndex.get(m+1);
			int new_starting = findMaximum(y_sequence, starting_index, starting_index+samplingRate);
			int new_ending = findMaximum(y_sequence, ending_index-samplingRate, ending_index);
			if((time_sequence[new_ending]-time_sequence[new_starting])>minimum_time_for_valid_covering){
				validCoveringIndex_linking.add(new_starting);
				
				validCoveringIndex_linking.add(new_ending);
			}
		}
		
		
		int validDataSize = 0;
		for(int n=0; n<validCoveringIndex_linking.size()-1;n=n+2){
			int start = validCoveringIndex_linking.get(n);
			int end = validCoveringIndex_linking.get(n+1);
			for(int z=start; z<=end; z++){
				validDataSize++;
				
				wr.write(String.valueOf(time_sequence[z])+","+String.valueOf(y_sequence[z]));
				wr.newLine();
				wrR.write(String.valueOf(time_sequence[z])+","+String.valueOf(r_sequence[z]));
				wrR.newLine();
			}
		}
		
		wr.close();
		wrR.close();
		
		double[][] matrix=new double[3][validDataSize];
		long[] time=new long[validDataSize];
		int index =0;
		
		for(int n=0; n<validCoveringIndex_linking.size()-1;n=n+2){
			int start = validCoveringIndex_linking.get(n);
			int end = validCoveringIndex_linking.get(n+1);
			for(int z=start; z<=end; z++){
				
				matrix[0][index]=r_sequence[z];
				matrix[1][index]=g_sequence[z];
				matrix[2][index]=b_sequence[z];
				time[index]=time_sequence[z];
				index++;				

			}
		}
		// Apply ICA on rgb signals
		ICA icaTool=new ICA();
		Object[] obj = icaTool.ICA_updated2(1, matrix);
		MWNumericArray n=(MWNumericArray)obj[0];
		double[] result=new double[n.numberOfElements()];
		for(int k=1;k<=n.numberOfElements();k++)
        {
        	result[k-1]=n.getFloat(k);
        	
        }
       
        MWArray.disposeArray(obj);
        MWArray.disposeArray(n);
        icaTool.dispose();
        for (int m=0; m<result.length;m++){
        	 wrICA.write(String.valueOf(time[m])+","+String.valueOf(result[m]));
			 wrICA.newLine();
         }
         wrICA.close();
        
        // Apply PCA on rgb signals
        PCA pcaTool=new PCA();
 		Object[] o = pcaTool.PCA_updated_2(1, matrix);
 		MWNumericArray nn=(MWNumericArray)o[0];
 		double[] result_PCA=new double[nn.numberOfElements()];
 		for(int k=1;k<=nn.numberOfElements();k++)
        {
         	result_PCA[k-1]=nn.getFloat(k);
         	
        }
        MWArray.disposeArray(o);
        MWArray.disposeArray(nn);
        pcaTool.dispose();
         
        for (int m=0; m<result_PCA.length;m++){
        	wrPCA.write(String.valueOf(time[m])+","+String.valueOf(result_PCA[m]));
 			wrPCA.newLine();
        }
        wrPCA.close();	
	}
	
	static int findMaximum(double[] array, int start, int end){
		double max = array[start];
		int max_index = start;
		for(int i = start; i<=end; i++){
			if(array[i]>max){
				max=array[i];
				max_index=i;
			}
		}
		return max_index;
	}
	
	public static void main(String[] args) throws IOException, MWException {
		
		interpolation();
		noiseReduction();
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

