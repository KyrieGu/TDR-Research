package edu.pitt.cs.mips.data_collection;

import android.content.Context;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import android.util.Log;

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

public class DataStorage
{

    private DataStorage(Context c)
    {
        context = c;
        samples =  new ArrayList<DataSample>(10000);
        samples2 = new ArrayList<DataSample>(10000);
        samples3 = new ArrayList<DataSample>(10000);
        samples4 = new ArrayList<DataSample>(10000);
        samples5 = new ArrayList<DataSample>(10000);
        samples6 = new ArrayList<DataSample>(10000);
        samples7 = new ArrayList<DataSample>(10000);
    }

    public static boolean AddSample(long l, int i)
    {
        if(instance != null)
        {
            instance.add(l, i);
            
            return true;
        } 
        
        return false;
    }
    
    public static boolean AddYSample(long l, int i){
    	if(instance != null)
        {
            instance.add2(l, i);
            
            return true;
        } 
        
        return false;
    }
    
    public static boolean AddUSample(long l, int i){
    	if(instance != null)
        {
            instance.add6(l, i);
            
            return true;
        } 
        
        return false;
    }
    
    public static boolean AddVSample(long l, int i){
    	if(instance != null)
        {
            instance.add7(l, i);
            
            return true;
        } 
        
        return false;
    }
    
    public static boolean AddRSample(long l, int i){
    	if(instance != null)
        {
            instance.add3(l, i);
            
            return true;
        } 
        
        return false;
    }
    
    public static boolean AddGSample(long l, int i){
    	if(instance != null)
        {
            instance.add4(l, i);
            
            return true;
        } 
        
        return false;
    }
    
    public static boolean AddBSample(long l, int i){
    	if(instance != null)
        {
            instance.add5(l, i);
            
            return true;
        } 
        
        return false;
    }

    public static DataStorage getInstance(Context c)
    {
        if(instance == null)
        {
            instance = new DataStorage(c);
        }
        
        return instance;
    }

    public void add(long l, int i)
    {
        if(samples != null)
        {
            DataSample sample = new DataSample(l, i);
            samples.add(sample);
        }
    }
    
    public void add2(long l, int i)
    {
    	DataSample sample = new DataSample(l, i);
    	
        if(samples2 != null)
        {
            samples2.add(sample);
        }
    }
    
    public void add3(long l, int i)
    {
    	DataSample sample = new DataSample(l, i);
    	
        if(samples3 != null)
        {
            samples3.add(sample);
        }
    }
    
    public void add4(long l, int i)
    {
        if(samples4 != null)
        {
            DataSample sample = new DataSample(l, i);
            samples4.add(sample);
        }
    }
    
    public void add5(long l, int i)
    {
        if(samples5 != null)
        {
            DataSample sample = new DataSample(l, i);
            samples5.add(sample);
        }
    }
    
    public void add6(long l, int i)
    {
        if(samples6 != null)
        {
            DataSample sample = new DataSample(l, i);
            samples6.add(sample);
        }
    }
    
    public void add7(long l, int i)
    {
        if(samples7 != null)
        {
            DataSample sample = new DataSample(l, i);
            samples7.add(sample);
        }
    }
    
    public void clearData()
    {
    	if(samples != null){
    		samples.clear();
    	}
    	
    	if(samples2 != null){
    		samples2.clear();
    	}
    	
    	if(samples3 != null){
    		samples3.clear();
    	}
    	
    	if(samples4 != null){
    		samples4.clear();
    	}
    	
    	if(samples5 != null){
    		samples5.clear();
    	}
    	
    	if(samples6 != null){
    		samples6.clear();
    	}
    	
    	if(samples7 != null){
    		samples7.clear();
    	}
    }

    public String save()
    {
        return save( null);
    }


    public String save(String surfix)
    {
        if(samples == null || samples.size() == 0) 
        {	
        	return "";
        }

        Log.i("DataStorage", "end");
        if(surfix == null) 
        {
        	surfix = "";
        }
        
        if(!surfix.startsWith("_"))
        {
        	surfix = "_" + surfix;
        }
        
        File dir = new File("" + context.getExternalFilesDir(null));
        
        String time1 = String.valueOf(System.nanoTime()/1000000);
        String time2 = String.valueOf(System.currentTimeMillis());
        String filename = time1 + "_" + time2 + surfix +  "_samples.csv";
        String filename2 = time1 + "_" + time2 + surfix + "_Y_samples.csv";
        String filename3 = time1 + "_" + time2 + surfix + "_R_samples.csv";
        String filename4 = time1 + "_" + time2 + surfix + "_G_samples.csv";
        String filename5 = time1 + "_" + time2 + surfix + "_B_samples.csv";
        String filename6 = time1 + "_" + time2 + surfix + "_U_samples.csv";
        String filename7 = time1 + "_" + time2 + surfix + "_V_samples.csv";
        
        File file = new File(dir, filename);
        
        if(!dir.exists())
            dir.mkdir();

        try {
	        OutputStreamWriter outputstreamwriter = new OutputStreamWriter( new FileOutputStream(file, true));
	
	        outputstreamwriter.write( DataSample.toCSV(samples) );
	        outputstreamwriter.close();
        
        } catch (IOException e)
        {
            e.printStackTrace();
            Log.i("DataStorage", e.toString());
        }
        
        if(samples2 != null && samples2.size() != 0)
        {
            File file2 = new File(dir, filename2);
            
        	try {
        		OutputStreamWriter outputstreamwriter = new OutputStreamWriter( new FileOutputStream(file2, true));
        		
        		outputstreamwriter.write( DataSample.toCSV(samples2) );
        		outputstreamwriter.close();
        	} 
        	catch (IOException e)
        	{
        		e.printStackTrace();
        		Log.i("DataStorage", e.toString());
        	}
        }
        
        if(samples3 != null && samples3.size() != 0)
        {
        	File file3 = new File(dir, filename3);
        
        	try {
        		OutputStreamWriter outputstreamwriter = new OutputStreamWriter( new FileOutputStream(file3, true));
	
        		outputstreamwriter.write( DataSample.toCSV(samples3) );
        		outputstreamwriter.close(); 
        	} 
        	catch (IOException e)
        	{
        		e.printStackTrace();
        		Log.i("DataStorage", e.toString());
        	}
        }
        
        if(samples4 != null && samples4.size() != 0)
        {
        	File file4 = new File(dir, filename4);
        
        	try {
        		OutputStreamWriter outputstreamwriter = new OutputStreamWriter( new FileOutputStream(file4, true));
        		
        		outputstreamwriter.write( DataSample.toCSV(samples4) );
        		outputstreamwriter.close();
        	} 
        	catch (IOException e)
        	{
        		e.printStackTrace();
        		Log.i("DataStorage", e.toString());
        	}
        }
        
        if(samples5 != null && samples5.size() != 0)
        {
        	File file5 = new File(dir, filename5);
        
        	try {
        		OutputStreamWriter outputstreamwriter = new OutputStreamWriter( new FileOutputStream(file5, true));
        		
        		outputstreamwriter.write( DataSample.toCSV(samples5) );
        		outputstreamwriter.close();
        	} 
        	catch (IOException e)
        	{
        		e.printStackTrace();
        		Log.i("DataStorage", e.toString());
        	}
        }
        
        if(samples6 != null && samples6.size() != 0)
        {
        	File file6 = new File(dir, filename6);
        
        	try {
        		OutputStreamWriter outputstreamwriter = new OutputStreamWriter( new FileOutputStream(file6, true));
        		
        		outputstreamwriter.write( DataSample.toCSV(samples6) );
        		outputstreamwriter.close();
        	} 
        	catch (IOException e)
        	{
        		e.printStackTrace();
        		Log.i("DataStorage", e.toString());
        	}
        }
        
        if(samples7 != null && samples7.size() != 0)
        {
        	File file7 = new File(dir, filename7);
        
        	try {
        		OutputStreamWriter outputstreamwriter = new OutputStreamWriter( new FileOutputStream(file7, true));
        		
        		outputstreamwriter.write( DataSample.toCSV(samples7) );
        		outputstreamwriter.close();
        	} 
        	catch (IOException e)
        	{
        		e.printStackTrace();
        		Log.i("DataStorage", e.toString());
        	}
        }
        
        return surfix;
    }

    private static Context context;
    private static DataStorage instance;
    private ArrayList<DataSample> samples;
    private ArrayList<DataSample> samples2;
    private ArrayList<DataSample> samples3;
    private ArrayList<DataSample> samples4;
    private ArrayList<DataSample> samples5;
    private ArrayList<DataSample> samples6;
    private ArrayList<DataSample> samples7;
}
