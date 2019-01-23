package com.edu.fsu.cs.cen4020.flaskinterface;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PostData extends AsyncTask<String, Void, Boolean> {


    private URL mURL;
    private HttpURLConnection serverConnection;
    private BufferedReader in;
    String line;

    @Override
    protected Boolean doInBackground(String... params) {

        /* Attempts to connect to server */
        try {
            mURL = new URL(params[0]);
        } catch (MalformedURLException m) {
            m.printStackTrace();
            Log.e("PostData", "URL not valid");
        }

        try {
            serverConnection = (HttpURLConnection) mURL.openConnection();
            serverConnection.setDoOutput(true);
            serverConnection.setDoInput(true);
            serverConnection.setRequestMethod("POST");
            serverConnection.setRequestProperty("Accept-Charset", "UTF-8");
            serverConnection.setRequestProperty("Content-Type", "application/json");
        } catch (IOException x) {
            x.printStackTrace();
            Log.e("PostData", "Failed to connect to PizzaPi");
        }

        if (serverConnection != null) {
        /* Parses through data and stores them if server connection was successful */
            try {
                OutputStreamWriter out = new OutputStreamWriter(serverConnection.getOutputStream());
                JSONObject parameters = new JSONObject();
                parameters.put("username", params[1]);

                parameters.put(params[2], params[3]);

                out.write(parameters.toString());
                out.flush();
                out.close();
            } catch (Exception x) {
                x.printStackTrace();
                Log.e("PostData", "Failed to write to server");
            }
            try {
                if (serverConnection.getResponseCode() / 100 == 2) {
                    in = new BufferedReader(new InputStreamReader(serverConnection.getInputStream()));
                    line = in.readLine();
                    in.close();
                    serverConnection.disconnect();
                    return line.equals("true");
                } else {
                    InputStream errorStream = serverConnection.getErrorStream();
                    line = errorStream.toString();
                    Log.e("PostData", "Error Line: " + line);
                }
            } catch (Exception x) {
                x.printStackTrace();
                Log.e("PostData", "Error getting output!");
            }
            serverConnection.disconnect();
        }
        return false;
    }
}
