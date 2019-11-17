package com.example.wei.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.qualcomm.snapdragon.sdk.face.FaceData;
import com.qualcomm.snapdragon.sdk.face.FacialProcessing;
import com.qualcomm.snapdragon.sdk.face.FacialProcessing.FP_MODES;
import com.qualcomm.snapdragon.sdk.sample.CameraSurfacePreview;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.EnumSet;

//import android.support.v4.content.ContextCompat;


public class NormalCondition extends ActionBarActivity implements Camera.PreviewCallback {


    private static Context context;
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
    private WebView mWebView;
    private Button btn_next;
    private Button btn_finish;
    StringBuilder gazeDataString;
    StringBuilder layoutResult;
    StringBuilder faceFeatures;
    final ValueCallback<String> valueCallBackTemp=new ValueCallback<String>() {
        @Override
        public void onReceiveValue(String s) {
            Log.d("LogName", s); // Returns the value from the function
        }
    };
    final ValueCallback<String> valueCallBackTemp2=new ValueCallback<String>() {
        @Override
        public void onReceiveValue(String s) {
            Log.d("LogName2", s); // Returns the value from the function
            layoutResult.append(s);
        }
    };
    int count;

    int warmup;



    Bundle extras;
    String userid;
    int [] readingOrder;
    int [] conditionOrder;
    int progressNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normal_condition);
        context = getApplicationContext();
        preview = (FrameLayout) findViewById(R.id.preview_normal);
        fpFeatureSupported = FacialProcessing.isFeatureSupported(FacialProcessing.FEATURE_LIST.FEATURE_FACIAL_PROCESSING);
        btn_next=(Button)findViewById(R.id.next_btn_normal_condition);
        btn_finish=(Button)findViewById(R.id.finish_btn_normal_condition);
        if (fpFeatureSupported && faceProc == null) {
            faceProc = FacialProcessing.getInstance();
            faceProc.setProcessingMode(FP_MODES.FP_MODE_VIDEO);
        } else {
            return;
        }
        try {
            cameraObj = Camera.open(1);
            cameraObj.setDisplayOrientation(90);
        } catch (Exception e) {
        }
        mPreview = new CameraSurfacePreview(NormalCondition.this, cameraObj, faceProc);
        preview.removeAllViews();
        preview = (FrameLayout) findViewById(R.id.preview_normal);
        preview.addView(mPreview, 0);
        extras = getIntent().getExtras();
        if (extras != null) {
            userid = extras.getString("userID");
            readingOrder = extras.getIntArray("readingOrder");
            conditionOrder = extras.getIntArray("conditionOrder");
            progressNum=extras.getInt("progressNum");
            warmup = extras.getInt("warmup");
        }
        count=1;


        mWebView = (WebView) findViewById(R.id.webview_normal);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        if(warmup==1){
            mWebView.loadUrl("http://pitt.edu/~weg21/data_collection/warm_up" + readingOrder[progressNum]+"_"+count+".html");
        }
        else{
            mWebView.loadUrl("http://pitt.edu/~weg21/data_collection/article" + readingOrder[progressNum]+"_"+count+".html");
        }
        //"http://cs.pitt.edu/~weg21/data_collection_experiment/article"+readingOrder[progressNum]+"_1"+".html");
        mWebView.setWebViewClient(new MyAppWebViewClient());
        mWebView.addJavascriptInterface(new MyJavaScriptInterface(), "768999000");
        gazeDataString=new StringBuilder();
        layoutResult = new StringBuilder();
        faceFeatures = new StringBuilder();

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

        String strJavascript2 = "getLayout();";
        mWebView.evaluateJavascript(strJavascript2, valueCallBackTemp2);
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

    public void setUI(int leftEyeBlink, int rightEyeBlink, int faceRollValue, PointF gazePointValue,
                      int pitch, int yaw, int horizontalGaze,
                      int verticalGaze, Point leftEyeCoord, Point rightEyeCoord,
                      Point leftEyeBrowsPointTop, Point leftEyeBrowsPointBot, Point leftEyeBrowsPointLeft,
                      Point leftEyeBrowsPointRight,
                      Point rightEyeBrowsPointTop, Point rightEyeBrowsPointBot, Point rightEyeBrowsPointLeft,
                      Point rightEyeBrowsPointRight,
                      Point leftEarPointTop, Point leftEarPointBottom, Point rightEarPointTop,
                      Point rightEarPointBottom,
                      Point leftEyeBot, Point leftEyeTop, Point leftEyeCenter, Point leftEyeLeft,
                      Point leftEyeRight,
                      Point rightEyeBot, Point rightEyeTop, Point rightEyeCenter, Point rightEyeLeft,
                      Point rightEyeRight,
                      Point mouthULipBot, Point mouthULipTop, Point mouthLLipBot, Point mouthLLipTop,
                      Point mouthLeft, Point mouthRight,
                      Point noseBridgePoint, Point noseCenterPoint, Point noseLLeft, Point noseLRight,
                      Point noseMLeft, Point noseMRight, Point noseTipPoint, Point noseULeft, Point noseURight){
        if(gazePointValue!=null){
            gazeDataString.append(gazePointValue.x+","+gazePointValue.y+","+System.currentTimeMillis()+"\n");
        }
        else{
            gazeDataString.append("null,null" + "\n");
        }
        if(gazePointValue!=null && leftEyeCoord!=null && rightEyeCoord!=null && leftEyeBrowsPointTop!=null &&
        leftEyeBrowsPointBot!=null && leftEyeBrowsPointLeft!=null && leftEyeBrowsPointRight!=null &&
        rightEyeBrowsPointTop!=null && rightEyeBrowsPointBot!=null && rightEyeBrowsPointLeft!=null &&
        rightEyeBrowsPointRight!=null && leftEarPointTop!=null && leftEarPointBottom!=null && rightEarPointTop!=null &&
        rightEarPointBottom!=null && leftEyeBot!=null && leftEyeTop!=null &&
        leftEyeCenter!=null && leftEyeLeft!=null && leftEyeRight!=null && rightEyeBot!=null &&
        rightEyeTop!=null && rightEyeCenter!=null && rightEyeLeft!=null
        && rightEyeRight!=null && mouthULipBot!=null && mouthULipTop!=null && mouthLLipBot!=null && mouthLLipTop!=null
        && mouthLeft!=null && mouthRight!=null && noseBridgePoint!=null && noseCenterPoint!=null && noseLLeft!=null
        && noseLRight!=null && noseMLeft!=null && noseMRight!=null && noseTipPoint!=null && noseULeft!=null && noseURight!=null){
            faceFeatures.append(leftEyeBlink+","+rightEyeBlink+","+faceRollValue+","+gazePointValue.x+","+gazePointValue.y+
                    ","+pitch+","+yaw+","+horizontalGaze+","+verticalGaze+","+","+leftEyeCoord.x+","+leftEyeCoord.y
                    +","+rightEyeCoord.x+","+rightEyeCoord.y+","+leftEyeBrowsPointTop.x+","+leftEyeBrowsPointTop.y
                    +","+leftEyeBrowsPointBot.x+","+leftEyeBrowsPointBot.y+","+leftEyeBrowsPointLeft.x+","+leftEyeBrowsPointLeft.y
                    +","+leftEyeBrowsPointRight.x+","+leftEyeBrowsPointRight.y+","+rightEyeBrowsPointTop.x+","+rightEyeBrowsPointTop.y
                    +","+rightEyeBrowsPointBot.x+","+rightEyeBrowsPointBot.y+","+rightEyeBrowsPointLeft.x+","+rightEyeBrowsPointLeft.y
                    +","+rightEyeBrowsPointRight.x+","+rightEyeBrowsPointRight.y+","+leftEarPointTop.x+","+leftEarPointTop.y
                    +","+leftEarPointBottom.x+","+leftEarPointBottom.y+","+rightEarPointTop.x+","+rightEarPointTop.y
                    +","+rightEarPointBottom.x+","+rightEarPointBottom.y+","+leftEyeBot.x+","+leftEyeBot.y
                    +","+leftEyeTop.x+","+leftEyeTop.y +","+leftEyeCenter.x+","+leftEyeCenter.y +","+leftEyeLeft.x+","+leftEyeLeft.y
                    +","+leftEyeRight.x+","+leftEyeRight.y +","+rightEyeBot.x+","+rightEyeBot.y +","+rightEyeTop.x+","+rightEyeTop.y
                    +","+rightEyeCenter.x+","+rightEyeCenter.y +","+rightEyeLeft.x+","+rightEyeLeft.y +","+rightEyeRight.x+","+rightEyeRight.y
                    +","+mouthULipBot.x+","+mouthULipBot.y +","+mouthULipTop.x+","+mouthULipTop.y +","+mouthLLipBot.x+","+mouthLLipBot.y
                    +","+mouthLLipTop.x+","+mouthLLipTop.y +","+mouthLeft.x+","+mouthLeft.y +","+mouthRight.x+","+mouthRight.y
                    +","+noseBridgePoint.x+","+noseBridgePoint.y +","+noseCenterPoint.x+","+noseCenterPoint.y +","+noseLLeft.x+","+noseLLeft.y
                    +","+noseLRight.x+","+noseLRight.y +","+noseMLeft.x+","+noseMLeft.y +","+noseMRight.x+","+noseMRight.y
                    +","+noseTipPoint.x+","+noseTipPoint.y +","+noseULeft.x+","+noseULeft.y +","+noseURight.x+","+noseURight.y+System.currentTimeMillis()+"\n");


          /*  faceFeatures.append(leftEyeBlink+","+rightEyeBlink+","+faceRollValue+","+gazePointValue+
                    ","+pitch+","+yaw+","+horizontalGaze+","+verticalGaze+","+","+leftEyeCoord
                    +","+rightEyeCoord+","+leftEyeBrowsPointTop
                    +","+leftEyeBrowsPointBot+","+leftEyeBrowsPointLeft
                    +","+leftEyeBrowsPointRight+","+rightEyeBrowsPointTop
                    +","+rightEyeBrowsPointBot+","+rightEyeBrowsPointLeft
                    +","+rightEyeBrowsPointRight+","+leftEarPointTop
                    +","+leftEarPointBottom+","+rightEarPointTop
                    +","+rightEarPointBottom+","+leftEyeBot
                    +","+leftEyeTop +","+leftEyeCenter +","+leftEyeLeft
                    +","+leftEyeRight +","+rightEyeBot +","+rightEyeTop
                    +","+rightEyeCenter +","+rightEyeLeft +","+rightEyeRight
                    +","+mouthULipBot +","+mouthULipTop +","+mouthLLipBot
                    +","+mouthLLipTop +","+mouthLeft +","+mouthRight
                    +","+noseBridgePoint +","+noseCenterPoint +","+noseLLeft
                    +","+noseLRight +","+noseMLeft +","+noseMRight
                    +","+noseTipPoint +","+noseULeft +","+noseURight+","+System.currentTimeMillis()+"\n");*/
        }
        else{
            faceFeatures.append("0, 0, 0, null, 0, 0, 0, 0, null, null, null,null,null,null, null,null,null,null," +
                    "null,null,null,null, null,null,null,null,null, null,null,null,null,null," +
                    "null,null,null,null,null,null, null,null,null,null,null,null,null,null,null\n");
        }


    }

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

        mPreview = new CameraSurfacePreview(NormalCondition.this, cameraObj, faceProc);
        preview.removeAllViews();
        preview = (FrameLayout) findViewById(R.id.preview_normal);
        preview.addView(mPreview,0);
        cameraObj.setPreviewCallback(NormalCondition.this);
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
            //setUI(null);
            setUI(0, 0, 0, null, 0, 0, 0, 0, null, null, null,null,null,null, null,null,null,null,
                    null,null,null,null, null,null,null,null,null, null,null,null,null,null,
                    null,null,null,null,null,null, null,null,null,null,null,null,null,null,null);
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
//leftEyeBlink, rightEyeBlink, faceRollValue, gazePointValue, pitch, yaw, horizontalGaze, verticalGaze, leftEyeCoord, rightEyeCoord
//leftEyeBrowsPointTop,leftEyeBrowsPointBot,leftEyeBrowsPointLeft,leftEyeBrowsPointRight
//rightEyeBrowsPointTop,rightEyeBrowsPointBot,rightEyeBrowsPointLeft,rightEyeBrowsPointRight
//leftEarPointTop,leftEarPointBottom,rightEarPointTop,rightEarPointBottom
//leftEyeBot,leftEyeTop,leftEyeCenter,leftEyeLeft,leftEyeRight
//rightEyeBot,rightEyeTop,rightEyeCenter,rightEyeLeft,rightEyeRight
//mouthULipBot,mouthULipTop,mouthLLipBot,mouthLLipTop,mouthLeft,mouthRight
//noseBridgePoint,noseCenterPoint,noseLLeft,noseLRight,noseMLeft,noseMRight,noseTipPoint,noseULeft,noseURight
                //setUI(gazePointValue);
                setUI(leftEyeBlink, rightEyeBlink, faceRollValue, gazePointValue, pitch, yaw, horizontalGaze,
                        verticalGaze, leftEyeCoord, rightEyeCoord,
leftEyeBrowsPointTop,leftEyeBrowsPointBot,leftEyeBrowsPointLeft,leftEyeBrowsPointRight,
rightEyeBrowsPointTop,rightEyeBrowsPointBot,rightEyeBrowsPointLeft,rightEyeBrowsPointRight,
leftEarPointTop,leftEarPointBottom,rightEarPointTop,rightEarPointBottom,
leftEyeBot,leftEyeTop,leftEyeCenter,leftEyeLeft,leftEyeRight,
rightEyeBot,rightEyeTop,rightEyeCenter,rightEyeLeft,rightEyeRight,
mouthULipBot,mouthULipTop,mouthLLipBot,mouthLLipTop,mouthLeft,mouthRight,
noseBridgePoint,noseCenterPoint,noseLLeft,noseLRight,noseMLeft,noseMRight,noseTipPoint,noseULeft,noseURight);
                Log.i("ReadingBehaviorData",gazePointValue.x+","+gazePointValue.y+","+System.currentTimeMillis());
            }
        }
    }

    public class MyAppWebViewClient extends WebViewClient {
        public MyAppWebViewClient(){
        }
        public void onPageFinished(WebView view, String url){
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    class MyJavaScriptInterface
    {
        @JavascriptInterface
        public void receiveString(String value)
        {
            System.out.println(value);
        }
    }

    public void normalNext(View view) {
        String directory0;
        if(warmup==1){
            directory0 = context.getExternalFilesDir(null) + "/DataCollectionExperiment/WarmUp/USERID_"+userid+"/";
        }
        else{
            directory0 = context.getExternalFilesDir(null) + "/DataCollectionExperiment/USERID_"+userid+"/";
        }

        //-------------Face Feature Saving----------------
        String filename0 = "FaceFeature-article"+readingOrder[progressNum]+"_"+count+"_NormalCondition"+System.currentTimeMillis()+".txt";
        //"GazeReadingData.txt";
        try {
            File dir=null;
            OutputStreamWriter outputstreamwriter;
            dir = new File(directory0);
            if (!dir.exists())
                dir.mkdirs();
            File file = new File(dir, filename0);
            outputstreamwriter = new OutputStreamWriter(new FileOutputStream(file, true));
            outputstreamwriter.write(faceFeatures.toString());
            faceFeatures.setLength(0);
            outputstreamwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Gaze Data Saving-------------------------
        String filename = "Gaze-article"+readingOrder[progressNum]+"_"+count+"_NormalCondition"+System.currentTimeMillis()+".txt";
        //"GazeReadingData.txt";
        try {
            File dir=null;
            OutputStreamWriter outputstreamwriter;
            dir = new File(directory0);
            if (!dir.exists())
                dir.mkdirs();
            File file = new File(dir, filename);
            outputstreamwriter = new OutputStreamWriter(new FileOutputStream(file, true));
            outputstreamwriter.write(gazeDataString.toString());
            gazeDataString.setLength(0);
            outputstreamwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //--------------------------------PageLayoutSaving-----------------------------------------

        String filename2 = "Layout-article"+readingOrder[progressNum]+"_"+count+"_NormalCondition"+System.currentTimeMillis()+".txt";;
        try {
            File dir2=null;
            OutputStreamWriter outputstreamwriter2;
            dir2 = new File(directory0);
            if (!dir2.exists())
                dir2.mkdirs();
            File file2 = new File(dir2, filename2);
            outputstreamwriter2 = new OutputStreamWriter(new FileOutputStream(file2, true));
            String strJavascript2 = "getLayout();";
            mWebView.evaluateJavascript(strJavascript2, valueCallBackTemp2);
            outputstreamwriter2.write(layoutResult.toString());
            outputstreamwriter2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        count++;
        if(warmup==1){
            mWebView.loadUrl("http://pitt.edu/~weg21/data_collection/warm_up" + readingOrder[progressNum]+"_"+count+".html");

        }
        else {
            mWebView.loadUrl("http://pitt.edu/~weg21/data_collection/article" + readingOrder[progressNum] + "_" + count + ".html");
        }
        if (count == 3)
        {
            btn_next.setVisibility(View.INVISIBLE);
            btn_finish.setVisibility(View.VISIBLE);
        }
    }


    public void normalFinish(View view) {
        //String directory0 = context.getExternalFilesDir(null) + "/DataCollectionExperiment/USERID_"+userid+"/";
        String directory0;
        if(warmup==1){
            directory0 = context.getExternalFilesDir(null) + "/DataCollectionExperiment/WarmUp/USERID_"+userid+"/";
        }
        else{
            directory0 = context.getExternalFilesDir(null) + "/DataCollectionExperiment/USERID_"+userid+"/";
        }
        //-------------Face Feature Saving----------------
        String filename0 = "FaceFeature-article"+readingOrder[progressNum]+"_"+count+"_NormalCondition"+System.currentTimeMillis()+".txt";
        //"GazeReadingData.txt";
        try {
            File dir=null;
            OutputStreamWriter outputstreamwriter;
            dir = new File(directory0);
            if (!dir.exists())
                dir.mkdirs();
            File file = new File(dir, filename0);
            outputstreamwriter = new OutputStreamWriter(new FileOutputStream(file, true));
            outputstreamwriter.write(faceFeatures.toString());
            faceFeatures.setLength(0);
            outputstreamwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Gaze Data Saving-------------------------
        String filename = "Gaze-article"+readingOrder[progressNum]+"_"+count+"_NormalCondition"+System.currentTimeMillis()+".txt";
        //"GazeReadingData.txt";
        try {
            File dir=null;
            OutputStreamWriter outputstreamwriter;
            dir = new File(directory0);
            if (!dir.exists())
                dir.mkdirs();
            File file = new File(dir, filename);
            outputstreamwriter = new OutputStreamWriter(new FileOutputStream(file, true));
            outputstreamwriter.write(gazeDataString.toString());
            gazeDataString.setLength(0);
            outputstreamwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //--------------------------------PageLayoutSaving-----------------------------------------

        String filename2 = "Layout-article"+readingOrder[progressNum]+"_"+count+"_NormalCondition"+System.currentTimeMillis()+".txt";;
        try {
            File dir2=null;
            OutputStreamWriter outputstreamwriter2;
            dir2 = new File(directory0);
            if (!dir2.exists())
                dir2.mkdirs();
            File file2 = new File(dir2, filename2);
            outputstreamwriter2 = new OutputStreamWriter(new FileOutputStream(file2, true));
            String strJavascript2 = "getLayout();";
            mWebView.evaluateJavascript(strJavascript2, valueCallBackTemp2);
            outputstreamwriter2.write(layoutResult.toString());
            outputstreamwriter2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent i;
        if(warmup==1){
            i=new Intent(getApplicationContext(), warmUpPage.class);
        }
        else{
            i=new Intent(getApplicationContext(), Preparing1.class);
        }


        progressNum++;
        i.putExtra("userID", userid);
        i.putExtra("readingOrder",readingOrder);
        i.putExtra("conditionOrder",conditionOrder);
        i.putExtra("progressNum",progressNum);
        startActivity(i);



    }


}

