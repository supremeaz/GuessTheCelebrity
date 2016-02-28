package com.arthur.guessthecelebrity;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Arthur on 25/01/2016.
 */
public class ContentDownloader extends AsyncTask<String,Void,String> {
    @Override
    protected String doInBackground(String... url) {
        String content="";
        try{
            URL siteURL=new URL(url[0]);
            HttpURLConnection urlC=(HttpURLConnection)siteURL.openConnection();
            InputStream in=urlC.getInputStream();
            InputStreamReader inReader=new InputStreamReader(in);

            int current=inReader.read();
            while(current!=-1){     //as long as there is still stuff to read
                content+=(char)current;
                current=inReader.read();
            }
            return content;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "Failed";
    }
}
