package com.example.wei.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import java.util.ArrayList;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Field;

public class Preparing1 extends ActionBarActivity{
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
            conditionOrder = extras.getIntArray("conditionOrder");
            progressNum=extras.getInt("progressNum");
        }
        if(progressNum<4){
            if(conditionOrder[progressNum]==1){
                textView1.setText("Please read the following article with 3 pages under NORMAL SEQUENCIAL CONDTION. " +
                        "Under this condition, you need to read each line from left to right, line by line from top to the end. " +
                        "\nPlease do not reread any word or line, do not skip any word or line. " +
                        "When you finish one page, please click the bottom 'NEXT' button to continue read next page, " +
                        "or click the bottom 'FINISH' button to confirm your finishing reading. " +
                        "\nWhen you are ready, click 'Start' button to begin.");
            }
            if(conditionOrder[progressNum]==2){
                textView1.setText("Please read the following article with 3 pages under REREAD ONLY CONDITION. " +
                        "Under this condition, you need to read each line from left to right, line by line from top to the end of" +
                        "the hightlighted area, and then start reading from the beginning of highlighted area " +
                        "to the end of the page. \n" +
                        "Please do not reread any word or line other than instructed, and do not skip any word or line. " +
                        "When you finish one page, please click the bottom 'NEXT' button to continue read next page, " +
                        "or click the bottom 'FINISH' button to confirm your finishing reading. " +
                        "\nWhen you are ready, click 'Start' button to begin.");
            }
            if(conditionOrder[progressNum]==3){
                textView1.setText("Please read the following article with 3 pages under SKIP ONLY CONDTION. " +
                        "Under this condition, you need to read each line from left to right, " +
                        "line by line from top to the beginning of light gray paragraph, and then start" +
                        "from the end of light gray paragraph to the end of the page. \n" +
                        "Please do not reread any word or line, do not skip any word or line other than instructed. " +
                        "When you finish one page, please click the bottom 'NEXT' button to continue read next page, " +
                        "or click the bottom 'FINISH' button to confirm your finishing reading. " +
                        "\nWhen you are ready, click 'Start' button to begin.");
            }
            if(conditionOrder[progressNum]==4){
                textView1.setText("Please read the following article with 3 pages under REREAD+SKIP CONDITION. " +
                                "Under this condition, you will see either a highlighted area following with a 'RED' button, or a " +
                                "light gray area following with a 'RED' button. \n If you see a highlighted area following with" +
                                " a 'RED' button, please read from the beginning to the end of highlighted area," +
                                " and click the 'RED' button as soon as you reach it. After clicking, a sequence of " +
                                "paragraphs will turn to gray color. You need to go back to the beginning of the " +
                                "highlighted area, and start reading to the bottom of the page. You need to skip the gray area" +
                                " during reading. \n" +
                                "If you see a gray area following with a 'RED' button, please read from the beginning of " +
                                "the page to the beginning of the gray area. You need to skip the gray area, and directly click " +
                                "the 'RED' button. After clicking, a sequence of paragraphs will be highlighted. If there is any " +
                                "overlap between highlights and grays, please start from the beginning of highlights, read though it " +
                                "to the end of highlights, and then read from the end of gray area to the end of the page. If there is no" +
                                " overlap, please read from the end of the gray area, to the end of highlights, and then from the beginning" +
                                " of highlights to the end of the page. \n" +
                                "Please do not reread any word or line other than instructed, " +
                                "do not skip any word or line other than instructed. " +
                                "When you finish one page, please click the bottom 'NEXT' button to continue read next page, " +
                                "or click the bottom 'FINISH' button to confirm your finishing reading. " +
                                "\nWhen you are ready, click 'Start' button to begin."


                );
            }
        }
        else{
            textView1.setText("Experiment Phase 1 Finished. Please press start button to go back and start Phase 2.");
        }





    }

    public void StartingReading1(View view) {
        Intent i;
        if(progressNum<4){
            if(conditionOrder[progressNum]==1){
                i=new Intent(getApplicationContext(), NormalCondition.class);
            }
            else if(conditionOrder[progressNum]==2){
                i=new Intent(getApplicationContext(), RereadCondition.class);
            }
            else if(conditionOrder[progressNum]==3){
                i=new Intent(getApplicationContext(), SkipCondition.class);
            }
            else{
                System.out.println("??????????");
                i=new Intent(getApplicationContext(), SkipRereadCondition.class);
            }

            i.putExtra("userID", userid);
            i.putExtra("readingOrder",readingOrder);
            i.putExtra("conditionOrder",conditionOrder);
            i.putExtra("progressNum",progressNum);
            i.putExtra("warmup",0);
            startActivity(i);
        }
        else{
            System.out.println("Finish");
            i=new Intent(getApplicationContext(), StartInterface.class);
            startActivity(i);
        }



    }
}


