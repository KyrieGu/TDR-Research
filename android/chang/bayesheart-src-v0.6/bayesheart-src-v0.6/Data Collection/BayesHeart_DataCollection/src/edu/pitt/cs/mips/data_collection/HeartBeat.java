package edu.pitt.cs.mips.data_collection;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class HeartBeat
    implements android.hardware.Camera.PreviewCallback, PulseObserver
{

    public HeartBeat(SurfaceView surfaceview, Context context1)
    {
        observer = null;
        Flashlight flashlight = new Flashlight();
        flashLight = flashlight;
        lightIntensity = 0;
        redIndex = 0;
        skipSamples = 25;	// pick a pixel every skipSamples pixels
        running = false;
        startRequest = false;
        previewSurfaceValid = false;
        c = null;
        useCameraLed = true;
        cameraOpen = false;
        lastTime = 0;
        frameRate = 30;
        lastFrameRateBoundTimestamp = 0;
        useFrameRateTiming = true;
        cntSamplesThisSession = 0;
        preinit();
        context = context1;
        previewSurface = surfaceview;
        previewSurfaceHolder = surfaceview.getHolder();
        previewSurfaceHolder.addCallback(getPreviewSurfaceCallback());
        count = 0;
        ROI_pw = 30;
        ROI_ph = 30;

    }

    private Camera getDefault()
    {
        return Camera.open();
    }

    private void preinit()
    {
    	
    }

    private void resetSampleAquisitor()
    {
        lastFrameRateBoundTimestamp = 0;
        useFrameRateTiming = true;
        cntSamplesThisSession = 0;
    }

    private void setFlash(boolean flag)
    {
        try
        {
            android.hardware.Camera.Parameters parameters = c.getParameters();
            if(flag)
                parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
            else
                parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
            c.setParameters(parameters);
        } catch(Exception exception) {
    			c.release();
    			c = null;
    	}
    }

    private void setPreviewCallback()
    {
        c.setPreviewCallback(this);

    }

    public void addSample(byte buffer[], long timestamp)
    {
        int ychannel_size = (buffer.length * 2) / 3;

        int sum = 0;
        
        for ( int pos = 0; pos < ychannel_size; pos +=  skipSamples)
        {
            sum += buffer[pos] & 0xff;
        }
        
        final int frameSize = pw * ph;
        int[] arrayY = new int[frameSize];
        int[] arrayV = new int[frameSize];
        int[] arrayU = new int[frameSize];
		int[] arrayR = new int[frameSize];
		int[] arrayG = new int[frameSize];
		int[] arrayB = new int[frameSize];
		int sumY = 0;
		int sumV = 0;
		int sumU = 0;
		int sumR = 0;
		int sumG = 0;
		int sumB = 0;
		
		int cursor = 0;
		
	    for (int i = 0, ci = 0; i < ph; ++i, ci += 1) {
	        for (int j = 0, cj = 0; j < pw; ++j, cj += 1) {
	        	/* The format of NV21 is all Y samples are found first in memory as an array of unsigned char with an even number of lines, 
	        	 * followed immediately by an array of unsigned char containing interleaved Cr and Cb samples with the same total stride as the Y samples.
	        	 * This is the preferred 4:2:0 pixel format
	        	 */
	            int Y = (0xff & ((int) buffer[ci * pw + cj]));
	            int Cr = (0xff & ((int) buffer[frameSize + (ci >> 1) * pw + (cj & ~1) + 0]));
	            int Cb = (0xff & ((int) buffer[frameSize + (ci >> 1) * pw + (cj & ~1) + 1]));
	            Y = Y < 16 ? 16 : Y;
	            
	            // formula to convert from YCrCb to RGB based on intel IPP.
	            int R = (int) (1.164f * (Y - 16) + 1.596f * (Cr - 128));
	            int G = (int) (1.164f * (Y - 16) - 0.813f * (Cr - 128) - 0.391f * (Cb - 128));
	            int B = (int) (1.164f * (Y - 16) + 2.018f * (Cb - 128));
	            
	            
		        
	            R = R < 0 ? 0 : (R > 255 ? 255 : R);
	            G = G < 0 ? 0 : (G > 255 ? 255 : G);
	            B = B < 0 ? 0 : (B > 255 ? 255 : B);
	            
	            int pos = ci * pw + cj;
	            if (ci >= ph/2 - ROI_ph/2 && ci < ph/2 + ROI_ph/2 && cj >= pw/2 - ROI_pw/2 && cj < pw/2 + ROI_pw/2 ) {
	            	sumY = sumY + Y;
	            	sumU = sumU + Cb;
	            	sumV = sumV + Cr;
	                sumR = sumR + R;
	                sumG = sumG + G;
	                sumB = sumB + B;
	                cursor++;
	            }
	        }
	    }
	    Log.i("Info", ""+cursor);
	    

        
        DataStorage.AddSample(timestamp, -sum);
        DataStorage.AddYSample(timestamp, -sumY);
        DataStorage.AddUSample(timestamp, -sumU);
        DataStorage.AddVSample(timestamp, -sumV);
        DataStorage.AddRSample(timestamp, -sumR);
        DataStorage.AddGSample(timestamp, -sumG);
        DataStorage.AddBSample(timestamp, -sumB);
        
        Log.i("TimeStamp",""+timestamp);
        
    }

    void allocatePreviewBuffers()
    {
       android.hardware.Camera.Size size = c.getParameters().getPreviewSize();

        int frame_size = size.height * size.width * 2;
        int buffer_size = 4000000; // approximately 4MB
        int max_frame = 8;
        int frames = 0;

        if(frame_size != 0)
        {
            frames = Math.max(Math.min(buffer_size / frame_size, max_frame), 1);
        }
        
        for (int i =0; i < frames; i++)
        {
            try
            {
                c.addCallbackBuffer(new byte[frame_size]);
            }
            catch(Exception exception) { }
        }
    }

    public int analyze(byte frame[], int sample_span)
    {
        int ychannel_size = (frame.length * 2) / 3;  

        int sum = 0;
        
        for ( int pos = 0; pos < ychannel_size; pos +=  sample_span)
        {
            sum += frame[pos] & 0xff;
        }
        
        return sum;
    }

    public int getFramerate()
    {
        return frameRate;
    }

    android.view.SurfaceHolder.Callback getPreviewSurfaceCallback()
    {
        return new android.view.SurfaceHolder.Callback()
            {

				public void surfaceChanged(SurfaceHolder holder, int format,
						int width, int height) {
					// TODO Auto-generated method stub
					
				}

				public void surfaceCreated(SurfaceHolder holder) {
					// TODO Auto-generated method stub
                    previewSurfaceValid = true;

                    if(startRequest)
                    {
                        start();
                    }
                    
                    startRequest = false;
				}

				public void surfaceDestroyed(SurfaceHolder holder) {
					// TODO Auto-generated method stub
                    previewSurfaceValid = false;
                    stopCamera();
				}
            };
    }

    public boolean isRunning()
    {
        if(c != null && running)
        {
            return true;
        }
        
        return false;
    }

    public void loadSettings(SharedPreferences sharedpreferences)
    {
    }

    public void onHRUpdate(int heartrate, int duration )
    {
        if(observer != null)
        {
            observer.onBeat(heartrate, duration / 1000);
        }
    }

    public void onPreviewFrame(byte frame[], Camera camera)
    {
    	count++;
    	
        lastTime = System.nanoTime();

        if(running)
        {
            addSample(frame, lastTime / 1000000);
        }
        
        returnBuffer(frame);
    }

    public void onPreviewFrameOptimized(byte frame[], Camera camera)
    {
        cntSamplesThisSession = cntSamplesThisSession + 1;
        lastTime = System.nanoTime();
        double last = (double)lastTime / 1000000;
        
        if(useFrameRateTiming)
        {
            if(cntSamplesThisSession > frameRate * 8)
            {
                lastFrameRateBoundTimestamp = lastFrameRateBoundTimestamp + 1000 / frameRate;

                if( Math.abs(lastFrameRateBoundTimestamp - last) >6000D / frameRate)
                {
                    useFrameRateTiming = false;
                } else
                {
                    last = lastFrameRateBoundTimestamp;
                }
            }
        }
        if(running)
        {
            addSample(frame, (long)last);
        }
        returnBuffer(frame);
    }

    public void onValidRR(long timestamp, int value)
    {
        if(observer != null)
        {
            observer.onValidRR(timestamp, value);
        }
    }

    public void onValidatedRR(long timestamp, int value)
    {
        if(observer != null)
        {
            observer.onValidatedRR(timestamp, value);
        }
    }

    void returnBuffer(byte buffer[])
    {
        try
        {
        	c.addCallbackBuffer(buffer);
        }
        catch(Exception exception) { }
    }

    public void saveSettings(SharedPreferences sharedpreferences)
    {
    }

    public void setBPMObserver(BeatObserver beatobserver)
    {
        observer = beatobserver;
    }

    public boolean start()
    {
        try
        {
            resetSampleAquisitor();
            startRequest = true;
            
            if ( startCamera() )
            {
                setFlash(true);
                c.startPreview();
                running = true;
                
                if(observer != null)
                {
                    observer.onHBStart();
                }
            } else
            {
            	if(observer != null)
            	{
            		Log.e("Preview Surface seems to be invalid!", null);
            	}
            }
    		
    		
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            
            if(observer != null)
            {
                BeatObserver beatlistener = observer;
                android.hardware.Camera.Parameters parameters;
                if(c != null)
                {
                    parameters = c.getParameters();
                } else 
                {
                    parameters = null;
                }
                beatlistener.onCameraError(exception, parameters);
            }
        }
        
        return true;
    }

    boolean startCamera()
    {
        boolean flag;
        if(!previewSurfaceValid)
        {
        	Log.e("ERROR", "Preview surface invalid");
            flag = false;
        } else
        {
            Log.e("SUCCESS", "Preview surface valid");
                flag = true;
        }

        c = getDefault();
        Camera.Parameters parameters = c.getParameters();
        
        try
        {
	        c.setPreviewDisplay(previewSurfaceHolder);
	
	        pw = 176;// 260;
	        ph = 144; //220;
	        
	        Log.i("INFO", "Setting preview size: " + pw  + " | " + ph);
	
	        skipSamples = (pw * ph) / 1000;
	        parameters.setPreviewSize(pw, ph);
	        c.setParameters(parameters);
	        
	        if(useCameraLed)
	        {
	            parameters.set("flash-mode", "torch");
	            c.setParameters(parameters);
	        }
	
	        parameters.set("focus-mode", "infinity");

	        c.setParameters(parameters);
	        


	        parameters.setExposureCompensation(0);
	        parameters.setWhiteBalance(Parameters.WHITE_BALANCE_FLUORESCENT);
	        
	        c.setParameters(parameters);
	
	        frameRate = parameters.getPreviewFrameRate();
	
	        c.setParameters(parameters);
	        setPreviewCallback();
	        allocatePreviewBuffers();
	        c.startPreview();

        } catch (IOException e)
        {
        	c.release();
        	c = null;
        	e.printStackTrace();
        	
        	return false;
        }
        cameraOpen = true;
        running = true;
        flag = true;
        
        return flag;
    }

    public void stop()
    {
        startRequest = false;
        setFlash(false);
        running = false;
        stopCamera();
        
        if(observer != null)
        {
            observer.onHBStop();
        }
    }

    public void stopCamera()
    {
        running = false;
        if(c != null)
        {

        	
            c.setOneShotPreviewCallback(null);
            c.stopPreview();
            c.release();
            c = null;
        }
        cameraOpen = false;
        c = null;
    }

	private static final String TAG = "HeartBeat"; 
	
    Camera c;
    boolean cameraOpen;
    int cntSamplesThisSession;
    Context context;
    Flashlight flashLight;
    int frameRate;
    double lastFrameRateBoundTimestamp;
    long lastTime;
    int lightIntensity;
    BeatObserver observer;
    SurfaceView previewSurface;
    private SurfaceHolder previewSurfaceHolder;
    boolean previewSurfaceValid;
    int redIndex;
    boolean running;
    private int skipSamples;
    boolean startRequest;
    boolean useCameraLed;
    boolean useFrameRateTiming;
    int pw;
    int ph;
    int count;
    int ROI_pw;
    int ROI_ph;

}
