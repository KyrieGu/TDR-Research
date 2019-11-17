package com.example.wei.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class StartInterface extends ActionBarActivity{
    private static Context context;
    EditText mEdit;
    ArrayList<int []> readingOrdering;
    ArrayList<int []> readingOrdering2;
    ArrayList<int []> conditionOrdering;
    double resultFuzzy;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_interface);
        context = getApplicationContext();
        mEdit = (EditText)findViewById(R.id.user_id);
        tv = (TextView)findViewById(R.id.textViewResult);
    }

    public void onClick10sec(View view){
        /**
        Intent resultIntent = new Intent(getApplicationContext(), mental_state.class);
// TODO Add extras or a data URI to this intent as appropriate.
        resultIntent.putExtra("userID", mEdit.getText().toString());
        resultIntent.putExtra("duration", 10);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    */

        Intent i = new Intent(getApplicationContext(), mentalState.class);
        i.putExtra("userID", mEdit.getText().toString());
        i.putExtra("duration", 10);
        //startActivity(i);
        startActivityForResult(i, 331);

    }

    public void onClick20sec(View view) {
        Intent i = new Intent(getApplicationContext(), mentalState.class);
        i.putExtra("userID", mEdit.getText().toString());
        i.putExtra("duration", 20);
        //startActivity(i);
        startActivityForResult (i, 331);
    }

    public void onClick30sec(View view){
        Intent i = new Intent(getApplicationContext(), mentalState.class);
        i.putExtra("userID", mEdit.getText().toString());
        i.putExtra("duration", 30);
        //startActivity(i);
        startActivityForResult (i, 331);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 331 && resultCode == Activity.RESULT_OK && data != null) {
            resultFuzzy = data.getDoubleExtra("fuzzy",0);
            tv.setText(""+resultFuzzy);
        }
    }

}