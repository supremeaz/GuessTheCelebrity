package com.arthur.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Arthur on 25/01/2016.
 */
public class BitmapDownloader extends AsyncTask<String,Void,Bitmap> {
    @Override
    protected Bitmap doInBackground(String... url) {
        try{
            URL imgURL=new URL(url[0]);
            HttpURLConnection urlC=(HttpURLConnection)imgURL.openConnection();
            urlC.connect();
            InputStream in=urlC.getInputStream();
            Bitmap theImg= BitmapFactory.decodeStream(in);
            return theImg;

           // BitmapDownloader imgDLer=imgURL.get
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
