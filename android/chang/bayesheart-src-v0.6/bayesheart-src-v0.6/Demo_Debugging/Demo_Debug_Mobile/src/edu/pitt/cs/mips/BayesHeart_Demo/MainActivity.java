package edu.pitt.cs.mips.BayesHeart_Demo;

import java.io.IOException;

import edu.pitt.cs.mips.BayesHeart_Demo.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements BeatObserver {
	private static Context context;
	private SurfaceView mPreview;
	
    Camera mCamera;
    int numberOfCameras;
    int cameraCurrentlyLocked;
    HeartBeat heartrate;
    
    Button start;
    Button stop;
    Button time;
    TextView hrIndicator;
    TextView statusText;
    ImageView statusImage;

    // The first rear facing camera
    int defaultCameraId;
	
    public static Context getAppContext() {
        return MainActivity.context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        
        Log.i("HeartBeatAlgorithm", "AppCreated");
        
        mPreview = (SurfaceView) findViewById(R.id.preview);
        mPreview.setMinimumWidth(176);
        mPreview.setMinimumHeight(144);
        
        start = (Button) findViewById(R.id.btnStart);
        stop = (Button) findViewById(R.id.btnStop);
        hrIndicator = (TextView) findViewById(R.id.lblHeartRate);
        statusText = (TextView) findViewById(R.id.statusText);
        statusImage = (ImageView) findViewById(R.id.statusImage);
        statusImage.setVisibility(View.INVISIBLE);

        heartrate = new HeartBeat(mPreview, this);
        heartrate.setBPMObserver(this);
    }

    public void OnClickStop(View view) {
    	if (heartrate.isRunning()) {
        	heartrate.stop();
        } 
    	mPreview.setBackgroundColor(Color.BLACK);
    	reset();
    	Log.i("HeartBeatAlgorithm", "AppStopped");
    }  
    
    public void OnClickStart(View view) {
    	mPreview.setBackgroundColor(Color.TRANSPARENT);
    	start.setEnabled(false);
    	stop.setEnabled(true);
    	
        heartrate.start();
        onUncovered();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        reset();
        
    	Log.i("HeartBeatAlgorithm", "AppResumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        
        Log.i("HeartBeatAlgorithm", "AppPaused");
        if (heartrate.isRunning()) {
        	heartrate.stop();
        }
    }
    
    protected void onDestroy() {
    	super.onDestroy();
    	
    	Log.i("HeartBeatAlgorithm", "AppClosed");
    }
    
    private void reset() {
        start.setEnabled(true);
    	stop.setEnabled(false);
    	hrIndicator.setText("--");
    	statusText.setText("Please press the start button to measure your heart rate");
    	statusImage.setVisibility(View.INVISIBLE);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        // Handle item selection
        switch (item.getItemId()) 
        {
        case R.id.switch_cam:
            // check for availability of multiple cameras
            if (numberOfCameras == 1) 
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(this.getString(R.string.camera_alert))
                       .setNeutralButton("Close", null);
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }


            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

	public void onBeat(int heartrate, int duration) {
		// TODO Auto-generated method stub
		
    	String hr = "" + heartrate;
    	
    	hrIndicator.setText(hr);
    	
    	if (heartrate > 0) {
    		statusImage.setImageResource(R.drawable.green);
    	}
        statusText.setText("Lens Covered");
        
        Log.i("HeartBeatAlgorithm", "onBeat");
	}

	public void onCameraError(Exception exception, Parameters parameters) {
		// TODO Auto-generated method stub
		
	}

	public void onHBError() {
		// TODO Auto-generated method stub
		
	}

	public void onHBStart() {
		// TODO Auto-generated method stub
		
	}

	public void onHBStop() {
		// TODO Auto-generated method stub
		
	}

	public void onValidRR(long timestamp, int value) {
		// TODO Auto-generated method stub
		
	}

	public void onValidatedRR(long timestamp, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSample(long timestamp, double value) {
		// TODO Auto-generated method stub
		
	}
	
	public void clearLog(){
		try {
		    Runtime.getRuntime().exec("logcat -c");
		} catch (Exception e1) {
		    e1.printStackTrace();
		} 
	}
	
    @Override
    public void onCovered() {
    	
    	statusText.setText("Lens Covered");
    	statusImage.setImageResource(R.drawable.yellow);
        
		Log.i("HeartBeatAlgorithm", "onCovered");
    }

    @Override
    public void onUncovered() {
    	
    	String hr = "--";
    	hrIndicator.setText(hr);
    	statusText.setText("Lens Uncovered");
    	statusImage.setVisibility(View.VISIBLE);
        statusImage.setImageResource(R.drawable.red);
        
		Log.i("HeartBeatAlgorithm", "onUncovered");
    }
}

