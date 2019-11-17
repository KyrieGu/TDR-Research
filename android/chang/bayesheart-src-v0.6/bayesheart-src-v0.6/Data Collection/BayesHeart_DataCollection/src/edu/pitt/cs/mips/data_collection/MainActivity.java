package edu.pitt.cs.mips.data_collection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import edu.pitt.cs.mips.BayesHeart_DataCollection.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements BeatObserver {
	private static Context context;
	private SurfaceView mPreview;
	
    Camera mCamera;
    int numberOfCameras;
    int cameraCurrentlyLocked;
    HeartBeat heartrate;
    DataStorage storage;
    
    Button start;
    Button save;

    // The first rear facing camera
    int defaultCameraId;
	
    public static Context getAppContext() {
        return MainActivity.context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        
        storage = DataStorage.getInstance(context);
       
        mPreview = (SurfaceView) findViewById(R.id.preview);
       
        mPreview.setMinimumWidth(176);
        mPreview.setMinimumHeight(144);
        
        start = (Button) findViewById(R.id.btnStart);
        save = (Button) findViewById(R.id.btnSave);

        heartrate = new HeartBeat(mPreview, this);
        
        heartrate.setBPMObserver(this);
    }

    public void OnClickSave(View view) {
    	Button btn = (Button)view;
    	start.setEnabled(true);
    	save.setEnabled(false);
    	
    	storage.save();
    }  
    
    public void OnClickStart(View view) {
    	Button btn = (Button)view;
    	start.setEnabled(false);
    	save.setEnabled(true);
    	
    	storage.clearData();
        heartrate.stop();    	
        heartrate.start();
    }     
    
    @Override
    protected void onResume() {
        super.onResume();

        heartrate.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        
        heartrate.stop();
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
    	
    	TextView tv = (TextView) findViewById(R.id.lblHeartRate);
    	
    	tv.setText(hr);
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

	public void onSample(long timestamp, float value) {
		// TODO Auto-generated method stub
		
	}

	public void onValidRR(long timestamp, int value) {
		// TODO Auto-generated method stub
		
	}

	public void onValidatedRR(long timestamp, int value) {
		// TODO Auto-generated method stub
		
	}
	
}

