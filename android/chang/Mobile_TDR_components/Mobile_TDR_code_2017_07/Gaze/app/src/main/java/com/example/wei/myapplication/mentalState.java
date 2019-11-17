package com.example.wei.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.qualcomm.snapdragon.sdk.face.FaceData;
import com.qualcomm.snapdragon.sdk.face.FacialProcessing;
import com.qualcomm.snapdragon.sdk.sample.CameraSurfacePreview;

import java.util.EnumSet;
import java.util.Timer;
import java.util.TimerTask;

public class mentalState extends ActionBarActivity implements Camera.PreviewCallback{
    private static Context context;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    int chooseSpeed;
    Bundle extras;
    TextView textView1;

    String userid;
    int duration;
    Timer timer;
    TimerTask timerTask;
    CountDownTimer mCountDownTimer;
    int i;
    Handler handler;
    SurfaceView mPreview;
    FrameLayout preview;
    FacialProcessing faceProc;
    boolean fpFeatureSupported = false;
    Camera cameraObj;
    int surfaceWidth = 0, surfaceHeight = 0;
    int leftEyeBlink = 0, rightEyeBlink = 0, faceRollValue = 0, pitch = 0, yaw = 0, horizontalGaze = 0, verticalGaze = 0, displayAngle;
    Point leftEyeBrowsPointTop=null, leftEyeBrowsPointBot=null, leftEyeBrowsPointLeft=null,leftEyeBrowsPointRight=null, rightEyeBrowsPointTop=null;
    Point rightEyeBrowsPointBot=null,rightEyeBrowsPointLeft=null,rightEyeBrowsPointRight=null,leftEarPointTop;
    Point leftEarPointBottom,rightEarPointTop,rightEarPointBottom,leftEyeBot,leftEyeTop,leftEyeCenter,leftEyeLeft;
    Point leftEyeRight,rightEyeBot,rightEyeTop,rightEyeCenter,rightEyeLeft,rightEyeRight;
    Point mouthULipBot,mouthULipTop,mouthLLipBot,mouthLLipTop,mouthLeft,mouthRight,noseBridgePoint;
    Point noseCenterPoint,noseLLeft,noseLRight,noseMLeft,noseMRight,noseTipPoint,noseULeft,noseURight;
    PointF gazePointValue = null;
    Point leftEyeCoord=null, rightEyeCoord=null;
    Rect faceRect = null;
    FaceData[] faceArray = null;
    boolean landScapeMode = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mental_state);
        context = getApplicationContext();
        preview = (FrameLayout) findViewById(R.id.preview_reread);
        fpFeatureSupported = FacialProcessing.isFeatureSupported(FacialProcessing.FEATURE_LIST.FEATURE_FACIAL_PROCESSING);

        //textView1 = (TextView)findViewById(R.id.prep1_textView);
        if (fpFeatureSupported && faceProc == null) {
            faceProc = FacialProcessing.getInstance();
            faceProc.setProcessingMode(FacialProcessing.FP_MODES.FP_MODE_VIDEO);
        } else {
            return;
        }
        try {
            cameraObj = Camera.open(1);
            cameraObj.setDisplayOrientation(90);
        } catch (Exception e) {
        }
        mPreview = new CameraSurfacePreview(mentalState.this, cameraObj, faceProc);
        preview.removeAllViews();
        preview = (FrameLayout) findViewById(R.id.preview_reread);
        preview.addView(mPreview, 0);

        leftEyeBrowsPointTop=null;
        leftEyeBrowsPointBot=null;
        leftEyeBrowsPointLeft=null;
        leftEyeBrowsPointRight=null;
        rightEyeBrowsPointTop=null;
        rightEyeBrowsPointBot=null;
        rightEyeBrowsPointLeft=null;
        rightEyeBrowsPointRight=null;
        leftEarPointTop=null;
        leftEarPointBottom=null;
        rightEarPointTop=null;
        rightEarPointBottom=null;
        leftEyeBot=null;
        leftEyeTop=null;
        leftEyeCenter=null;
        leftEyeLeft=null;
        leftEyeRight=null;
        rightEyeBot=null;
        rightEyeTop=null;
        rightEyeCenter=null;
        rightEyeLeft=null;
        rightEyeRight=null;
        mouthULipBot=null;
        mouthULipTop=null;
        mouthLLipBot=null;
        mouthLLipTop=null;
        mouthLeft=null;
        mouthRight=null;
        noseBridgePoint=null;
        noseCenterPoint=null;
        noseLLeft=null;
        noseLRight=null;
        noseMLeft=null;
        noseMRight=null;
        noseTipPoint=null;
        noseULeft=null;
        noseURight=null;

        extras = getIntent().getExtras();
        if (extras != null) {
            duration = extras.getInt("duration");
        }

        setTimers(duration * 1000);



    }

    //Gaze Tracking
    @Override
    protected void onPause() {
        super.onPause();
        stopCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cameraObj != null) {
            stopCamera();
        }
        startCamera(1);
    }

    //    public void setUI(PointF gazePointValue) {
//        if(gazePointValue!=null){
//            gazeDataString.append(gazePointValue.x+","+gazePointValue.y+","+System.currentTimeMillis()+"\n");
//        }
//        else{
//            gazeDataString.append("null,null" + "\n");
//        }
//    }


    public void stopCamera() {
        if (cameraObj != null) {
            cameraObj.stopPreview();
            cameraObj.setPreviewCallback(null);
            preview.removeAllViews();
            preview.addView(mPreview, 0);
            cameraObj.release();
            faceProc.release();
            faceProc = null;
        }
        cameraObj = null;
    }

    /*
     * This is a function to start the camera preview. Call the appropriate constructors and objects.
     * @param-cameraIndex: Will specify which camera (front/back) to start.
     */
    public void startCamera(int cameraIndex) {
        Log.i("INFO", "Setting preview size: 176, 144");
        if (fpFeatureSupported && faceProc == null) {
            faceProc = FacialProcessing.getInstance();// Calling the Facial Processing Constructor.
        }

        try {
            cameraObj = Camera.open(cameraIndex);// attempt to get a Camera instance
            cameraObj.setDisplayOrientation(90);
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }

        mPreview = new CameraSurfacePreview(mentalState.this, cameraObj, faceProc);
        preview.removeAllViews();
        preview = (FrameLayout) findViewById(R.id.preview_reread);
        preview.addView(mPreview,0);
        cameraObj.setPreviewCallback(mentalState.this);
    }

    /*
     * Detecting the face according to the new Snapdragon SDK. Face detection will now take place in this function.
     * 1) Set the Frame
     * 2) Detect the Number of faces.
     * 3) If(numFaces > 0) then do the necessary processing.
     */
    @Override
    public void onPreviewFrame(byte[] data, Camera arg1) {

        FacialProcessing.PREVIEW_ROTATION_ANGLE angleEnum = FacialProcessing.PREVIEW_ROTATION_ANGLE.ROT_0;
        displayAngle = 90;
        angleEnum = FacialProcessing.PREVIEW_ROTATION_ANGLE.ROT_90;
        if (faceProc == null) {
            faceProc = FacialProcessing.getInstance();
        }
        Camera.Parameters params = cameraObj.getParameters();
        Camera.Size previewSize = params.getPreviewSize();
        surfaceWidth = mPreview.getWidth();
        surfaceHeight = mPreview.getHeight();
//        // Landscape mode - front camera
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            faceProc.setFrame(data, previewSize.width, previewSize.height, true, angleEnum);
            cameraObj.setDisplayOrientation(displayAngle);
            landScapeMode = true;
        }
        // Portrait mode - front camera
        else{
            if(data==null){
                return;
            }
            faceProc.setFrame(data, previewSize.width, previewSize.height, true, angleEnum);
            cameraObj.setDisplayOrientation(displayAngle);
            landScapeMode = false;
        }
        int numFaces = faceProc.getNumFaces();
        if (numFaces == 0) {
//            setUI(null);
            System.out.print("@@@@@@@@@@@@@@@@Face==NULL");
        }

        else {
            faceArray = faceProc.getFaceData(EnumSet.of(FacialProcessing.FP_DATA.FACE_RECT,
                    FacialProcessing.FP_DATA.FACE_COORDINATES, FacialProcessing.FP_DATA.FACE_CONTOUR, FacialProcessing.FP_DATA.FACE_ORIENTATION,
                    FacialProcessing.FP_DATA.FACE_BLINK, FacialProcessing.FP_DATA.FACE_GAZE, FacialProcessing.FP_DATA.FACE_CONTOUR));
            if (faceArray == null) {

            } else {

                faceProc.normalizeCoordinates(surfaceWidth, surfaceHeight);
                for (int j = 0; j < numFaces; j++) {
                    leftEyeBlink = faceArray[j].getLeftEyeBlink();
                    rightEyeBlink = faceArray[j].getRightEyeBlink();
                    faceRollValue = faceArray[j].getRoll();
                    gazePointValue = faceArray[j].getEyeGazePoint();
                    pitch = faceArray[j].getPitch();
                    yaw = faceArray[j].getYaw();
                    horizontalGaze = faceArray[j].getEyeHorizontalGazeAngle();
                    verticalGaze = faceArray[j].getEyeVerticalGazeAngle();
                    faceRect = faceArray[j].rect;
                    leftEyeCoord = faceArray[j].leftEye;
                    rightEyeCoord = faceArray[j].rightEye;
                    leftEyeBrowsPointTop=faceArray[j].leftEyebrow.top;
                    leftEyeBrowsPointBot=faceArray[j].leftEyebrow.bottom;
                    leftEyeBrowsPointLeft=faceArray[j].leftEyebrow.left;
                    leftEyeBrowsPointRight=faceArray[j].leftEyebrow.right;
                    rightEyeBrowsPointTop=faceArray[j].rightEyebrow.top;
                    rightEyeBrowsPointBot=faceArray[j].rightEyebrow.bottom;
                    rightEyeBrowsPointLeft=faceArray[j].rightEyebrow.left;
                    rightEyeBrowsPointRight=faceArray[j].rightEyebrow.right;
                    leftEarPointTop=faceArray[j].leftEar.top;
                    leftEarPointBottom=faceArray[j].leftEar.bottom;
                    rightEarPointTop=faceArray[j].rightEar.top;
                    rightEarPointBottom=faceArray[j].rightEar.bottom;

                    leftEyeBot=faceArray[j].leftEyeObj.bottom;
                    leftEyeTop=faceArray[j].leftEyeObj.top;
                    leftEyeCenter=faceArray[j].leftEyeObj.centerPupil;
                    leftEyeLeft=faceArray[j].leftEyeObj.left;
                    leftEyeRight=faceArray[j].leftEyeObj.right;

                    rightEyeBot=faceArray[j].rightEyeObj.bottom;
                    rightEyeTop=faceArray[j].rightEyeObj.top;
                    rightEyeCenter=faceArray[j].rightEyeObj.centerPupil;
                    rightEyeLeft=faceArray[j].rightEyeObj.left;
                    rightEyeRight=faceArray[j].rightEyeObj.right;

                    mouthULipBot=faceArray[j].mouthObj.upperLipBottom;
                    mouthULipTop=faceArray[j].mouthObj.upperLipTop;
                    mouthLLipBot=faceArray[j].mouthObj.lowerLipBottom;
                    mouthLLipTop=faceArray[j].mouthObj.lowerLipTop;
                    mouthLeft=faceArray[j].mouthObj.left;
                    mouthRight=faceArray[j].mouthObj.right;

                    noseBridgePoint=faceArray[j].nose.noseBridge;
                    noseCenterPoint=faceArray[j].nose.noseCenter;
                    noseLLeft=faceArray[j].nose.noseLowerLeft;
                    noseLRight=faceArray[j].nose.noseLowerRight;
                    noseMLeft=faceArray[j].nose.noseMiddleLeft;
                    noseMRight=faceArray[j].nose.noseMiddleRight;
                    noseTipPoint=faceArray[j].nose.noseTip;
                    noseULeft=faceArray[j].nose.noseUpperLeft;
                    noseURight=faceArray[j].nose.noseUpperRight;

                }
//                setUI(gazePointValue);
                System.out.print("@@@@@@@@@@@@@@@@Face==1");
            }
        }
    }



    public void setTimers(long intervals){

        System.out.println("Intervals assigned: "+intervals);
        handler = new Handler();
        timer = new Timer();
        timerTask = new TimerTask(){
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        try {

                        }
                        catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };

        i=0;
        mCountDownTimer = new CountDownTimer(intervals,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Log_tag", "Tick of Progress" + i + millisUntilFinished);
                i++;
            }

            @Override
            public void onFinish() {
                i++;
                Intent resultIntent = new Intent(getApplicationContext(), mentalState.class);
// TODO Add extras or a data URI to this intent as appropriate.
                resultIntent.putExtra("fuzzy", 0.75);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        };

        mCountDownTimer.start();
        timer.schedule(timerTask, intervals);

    }
}


