package com.arthur.guessthecelebrity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.Touch;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    Button[] answerChoices;
    String webContent;
    List<String>celebNames;
    List<String>celebImgURLs;
    Bitmap currentImg;
    ImageView celebDisplay;
    String correctName;
    private void setUp(){
        answerChoices=new Button[4];
        celebNames=new ArrayList<String>();
        celebImgURLs=new ArrayList<String>();
        celebDisplay=(ImageView)findViewById(R.id.celebrityDisplay);
        answerChoices[0]=(Button)findViewById(R.id.op1);
        answerChoices[1]=(Button)findViewById(R.id.op2);
        answerChoices[2]=(Button)findViewById(R.id.op3);
        answerChoices[3]=(Button)findViewById(R.id.op4);
    }
    private void downloadContents(){
        ContentDownloader cD=new ContentDownloader();
        try {
            webContent = cD.execute("http://www.posh24.com/celebrities").get();
           // Log.i("Completed", webContent);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    private void readCelebNames(String content){
        String[] contentDivision=webContent.split("<div class=\"col-xs-12 col-sm-6 col-md-4\">");
        webContent=contentDivision[0];
        Pattern p=Pattern.compile("<img src=\"(.*?)\"");
        Matcher m=p.matcher(webContent);
        while(m.find()){
            String temp=m.group(1);
            celebImgURLs.add(temp);
          //  Log.i("url",temp);
        }
        p=Pattern.compile("alt=\"(.*?)\"");
        m=p.matcher(webContent);
        while(m.find()){
            String temp=m.group(1);
            celebNames.add(temp);
           // Log.i("names",temp);
        }

    }
    private void generateQuestion(){
        Random randomGenerator= new Random();
        int index=randomGenerator.nextInt(100);
        correctName=celebNames.get(index);
        //First have to download the Image and put into ImageView and assign 1 random button the name of the celebrity
        try {
            BitmapDownloader bitDL=new BitmapDownloader();
            currentImg=bitDL.execute(celebImgURLs.get(index)).get();
            celebDisplay.setImageBitmap(currentImg);
            int correctOption=randomGenerator.nextInt(4);
            answerChoices[correctOption].setText(celebNames.get(index));    //this sets the correct Button text

            //Then have to randomly assign to the buttons Names of other 3 random celebrities
            int otherIndex;
            for(int i=0;i<4;i++){
                if(i!=correctOption){   //for all other option choices
                    do{
                        otherIndex=randomGenerator.nextInt(100);
                    }while(otherIndex==index);
                    answerChoices[i].setText(celebNames.get(otherIndex));
                }
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }



    }

    public void chooseOption(View optionButton){
        //first check correctness by getting the String...
        Button clickedButton=(Button)optionButton;
        String chosenOption=clickedButton.getText().toString();
        if(chosenOption.equals(correctName)){
            Toast.makeText(getApplicationContext(),"Correct!",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Wrong!",Toast.LENGTH_SHORT).show();
        }

        //then generate next Question
        generateQuestion();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUp();
        //here need to do other things such as Download pics, etc...
        downloadContents();
        readCelebNames(webContent);
        generateQuestion();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
