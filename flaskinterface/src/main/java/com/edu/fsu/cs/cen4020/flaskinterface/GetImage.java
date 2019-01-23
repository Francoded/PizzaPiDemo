package com.edu.fsu.cs.cen4020.flaskinterface;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ronal on 11/28/2016.
 */

public class GetImage extends AsyncTask<String, Void, Drawable> {

    private URL mURL;
    private HttpURLConnection serverConnection;
    private BufferedReader in;

    @Override
    protected Drawable doInBackground(String... params) {

        /* Attempts to connect to server */
        try {
            params[0] = params[0].replace(" ", "%20");
            Log.d("GetData", "This is params[0]: " + params[0]);
            mURL = new URL(params[0]);
            Log.d("GetData", "This is URL: " + mURL.toString());
        } catch (MalformedURLException m) {
            m.printStackTrace();
            Log.e("GetData", "URL not valid");
        }
        try {
            serverConnection = (HttpURLConnection) mURL.openConnection();
        } catch (IOException x) {
            x.printStackTrace();
            Log.e("GetData", "Failed to connect to PizzaPi");
        }
        try {
            if (serverConnection != null) {
                try {
                    InputStream is = (InputStream) serverConnection.getContent();
                    Drawable d = Drawable.createFromStream(is, "src name");
                    return d;
                } catch (Exception e) {
                    return null;
                }
            } else
                Log.e("GetImage", "Connection Failed");

        } catch (Exception x) {
            x.printStackTrace();
            Log.e("GetImage", "Failed to get image");
        }

        return null;
    }
}
