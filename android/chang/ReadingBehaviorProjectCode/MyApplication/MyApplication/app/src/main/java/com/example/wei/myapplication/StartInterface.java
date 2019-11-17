package com.example.wei.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class StartInterface extends ActionBarActivity{
    private static Context context;
    EditText mEdit;
    ArrayList<int []> readingOrdering;
    ArrayList<int []> readingOrdering2;
    ArrayList<int []> conditionOrdering;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_interface);
        context = getApplicationContext();
        mEdit = (EditText)findViewById(R.id.user_id);
        readingOrdering = new ArrayList<int []>();
        readingOrdering.add(new int [] {2,1,3,4});//1
        readingOrdering.add(new int [] {1,3,2,4});//2
        readingOrdering.add(new int [] {3,4,1,2});//3
        readingOrdering.add(new int [] {3,2,4,1});//4
        readingOrdering.add(new int [] {4,2,3,1});//5
        readingOrdering.add(new int [] {2,3,4,1});//6
        readingOrdering.add(new int [] {4,3,1,2});//7
        readingOrdering.add(new int [] {1,4,2,3});//8
        readingOrdering.add(new int [] {2,4,1,3});//9
        readingOrdering.add(new int [] {1,2,4,3});//10
        readingOrdering.add(new int[] {3,1,2,4});//11
        readingOrdering.add(new int[] {2,3,4,1});//12
        readingOrdering.add(new int[] {4,2,3,1});//13
        readingOrdering.add(new int[] {1,3,2,4});//14
        readingOrdering.add(new int[] {2,1,3,4});//15
        readingOrdering.add(new int[] {4,1,3,2});//16
        readingOrdering.add(new int [] {2,1,3,4});//17
        readingOrdering.add(new int [] {1,3,2,4});//18
        readingOrdering.add(new int [] {3,4,1,2});//19
        readingOrdering.add(new int [] {3,2,4,1});//20
        readingOrdering.add(new int [] {4,2,3,1});//21
        readingOrdering.add(new int [] {2,3,4,1});//22
        readingOrdering.add(new int [] {4,3,1,2});//23
        readingOrdering.add(new int [] {1,4,2,3});//24
        readingOrdering.add(new int [] {2,4,1,3});//25
        readingOrdering.add(new int [] {1,2,4,3});//26
        readingOrdering.add(new int[] {3,1,2,4});//27
        readingOrdering.add(new int[] {2,3,4,1});//28
        readingOrdering.add(new int[] {4,2,3,1});//29
        readingOrdering.add(new int[] {1,3,2,4});//30
        readingOrdering.add(new int[] {2,1,3,4});//31
        readingOrdering.add(new int[] {4,1,3,2});//32





        readingOrdering2 = new ArrayList<int []>();
        readingOrdering2.add(new int [] {2,1,3});//1
        readingOrdering2.add(new int [] {1,3,2});//2
        readingOrdering2.add(new int [] {3,1,2});//3
        readingOrdering2.add(new int [] {3,2,1});//4
        readingOrdering2.add(new int [] {2,3,1});//5
        readingOrdering2.add(new int [] {2,3,1});//6
        readingOrdering2.add(new int [] {3,1,2});//7
        readingOrdering2.add(new int [] {1,2,3});//8
        readingOrdering2.add(new int [] {2,1,3});//9
        readingOrdering2.add(new int [] {1,2,3});//10
        readingOrdering2.add(new int [] {3,1,2});//11
        readingOrdering2.add(new int [] {2,3,1});//12
        readingOrdering2.add(new int [] {2,3,1});//13
        readingOrdering2.add(new int [] {1,3,2});//14
        readingOrdering2.add(new int [] {2,1,3});//15
        readingOrdering2.add(new int [] {1,3,2});//16
        readingOrdering2.add(new int [] {2,1,3});//17
        readingOrdering2.add(new int [] {1,3,2});//18
        readingOrdering2.add(new int [] {3,1,2});//19
        readingOrdering2.add(new int [] {3,2,1});//20
        readingOrdering2.add(new int [] {2,3,1});//21
        readingOrdering2.add(new int [] {2,3,1});//22
        readingOrdering2.add(new int [] {3,1,2});//23
        readingOrdering2.add(new int [] {1,2,3});//24
        readingOrdering2.add(new int [] {2,1,3});//25
        readingOrdering2.add(new int [] {1,2,3});//26
        readingOrdering2.add(new int [] {3,1,2});//27
        readingOrdering2.add(new int [] {2,3,1});//28
        readingOrdering2.add(new int [] {2,3,1});//29
        readingOrdering2.add(new int [] {1,3,2});//30
        readingOrdering2.add(new int [] {2,1,3});//31
        readingOrdering2.add(new int [] {1,3,2});//32


        conditionOrdering = new ArrayList<int []>();
        conditionOrdering.add(new int[] {1,4,3,2});//1
        conditionOrdering.add(new int[] {2,4,3,1});//2
        conditionOrdering.add(new int[] {4,3,1,2});//3
        conditionOrdering.add(new int[] {4,1,2,3});//4
        conditionOrdering.add(new int[] {3,4,2,1});//5
        conditionOrdering.add(new int[] {1,3,4,2});//6
        conditionOrdering.add(new int[] {1,3,2,4});//7
        conditionOrdering.add(new int[] {4,3,1,2});//8
        conditionOrdering.add(new int[] {3,1,4,2});//9
        conditionOrdering.add(new int[] {3,4,1,2});//10
        conditionOrdering.add(new int[] {4,2,1,3});//11
        conditionOrdering.add(new int[] {2,3,1,4});//12
        conditionOrdering.add(new int[] {2,1,3,4});//13
        conditionOrdering.add(new int[] {1,3,2,4});//14
        conditionOrdering.add(new int[] {3,4,1,2});//15
        conditionOrdering.add(new int[] {3,2,4,1});//16
        conditionOrdering.add(new int[] {1,4,3,2});//17
        conditionOrdering.add(new int[] {2,4,3,1});//18
        conditionOrdering.add(new int[] {4,3,1,2});//19
        conditionOrdering.add(new int[] {4,1,2,3});//20
        conditionOrdering.add(new int[] {3,4,2,1});//21
        conditionOrdering.add(new int[] {1,3,4,2});//22
        conditionOrdering.add(new int[] {1,3,2,4});//23
        conditionOrdering.add(new int[] {4,3,1,2});//24
        conditionOrdering.add(new int[] {3,1,4,2});//25
        conditionOrdering.add(new int[] {3,4,1,2});//26
        conditionOrdering.add(new int[] {4,2,1,3});//27
        conditionOrdering.add(new int[] {2,3,1,4});//28
        conditionOrdering.add(new int[] {2,1,3,4});//29
        conditionOrdering.add(new int[] {1,3,2,4});//30
        conditionOrdering.add(new int[] {3,4,1,2});//31
        conditionOrdering.add(new int[] {3,2,4,1});//32
    }

    public void OnClickWarmUp(View view){
        System.out.println("Warm Up Session");
        Intent i = new Intent(getApplicationContext(), warmUpPage.class);
        i.putExtra("userID", mEdit.getText().toString());
        i.putExtra("readingOrder", new int[]{1,2,3,4});
        i.putExtra("conditionOrder",new int[]{1,2,3,4});
        i.putExtra("progressNum", 0);
        startActivity(i);
    }

    public void OnClickExperiment1(View view) {
        System.out.println("Click choose userid_passing: " + mEdit.getText().toString());
        Intent i = new Intent(getApplicationContext(), Preparing1.class);
        i.putExtra("userID", mEdit.getText().toString());
        i.putExtra("readingOrder", readingOrdering.get(Integer.parseInt(mEdit.getText().toString())-1));
        i.putExtra("conditionOrder",conditionOrdering.get(Integer.parseInt(mEdit.getText().toString())-1));
        i.putExtra("progressNum",0);
        startActivity(i);
    }

    public void OnClickExperiment2(View view){
        System.out.println("Click choose phase 2");
        Intent i = new Intent(getApplicationContext(), phase2Page1.class);
        i.putExtra("userID", mEdit.getText().toString());
        i.putExtra("readingOrder", readingOrdering2.get(Integer.parseInt(mEdit.getText().toString())-1));
        i.putExtra("progressNum",0);
        startActivity(i);
    }
}