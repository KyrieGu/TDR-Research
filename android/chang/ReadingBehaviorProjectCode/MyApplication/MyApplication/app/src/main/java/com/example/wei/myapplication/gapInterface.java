package com.example.wei.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class gapInterface extends ActionBarActivity{
    private static Context context;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    int chooseSpeed;
    Bundle extras;
    TextView textView1;

    String userid;
    int [] readingOrder;
    int [] conditionOrder;
    int progressNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prepare_1);
        context = getApplicationContext();
        textView1 = (TextView)findViewById(R.id.prep1_textView);
        extras = getIntent().getExtras();
        if (extras != null) {
            userid = extras.getString("userID");
            readingOrder = extras.getIntArray("readingOrder");
            progressNum=extras.getInt("progressNum");
        }

            textView1.setText("Answer the questions please.");






    }

    public void StartingReading1(View view) {
        Intent i;
        if(progressNum<=2){
            System.out.println("####@1111111111111111111111:"+progressNum);
            i=new Intent(getApplicationContext(), phase2Page1.class);


            i.putExtra("userID", userid);
            i.putExtra("readingOrder",readingOrder);
            i.putExtra("progressNum", progressNum);
            startActivity(i);

        }
        else{
            System.out.println("####@2222222222222222"+progressNum);
            System.out.println("Finish");
            i=new Intent(getApplicationContext(), StartInterface.class);
            startActivity(i);
        }



    }
}


