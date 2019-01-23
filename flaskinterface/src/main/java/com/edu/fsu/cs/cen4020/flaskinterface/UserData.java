package com.edu.fsu.cs.cen4020.flaskinterface;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserData extends AsyncTask<String, Void, Boolean> {

    private URL mURL;
    private HttpURLConnection serverConnection;
    private BufferedReader in;
    String line;


    @Override
    protected Boolean doInBackground(String... params) {

        /* Attempts to connect to server */
        try {
            mURL = new URL(params[0]);
        } catch (Exception m) {
            m.printStackTrace();
            Log.e("PostData", "URL not valid");
        }

        try {
            serverConnection = (HttpURLConnection) mURL.openConnection();
            serverConnection.setDoOutput(true);
            serverConnection.setDoInput(true);
            serverConnection.setRequestMethod("POST");
            serverConnection.setRequestProperty("Accept-Charset", "UTF-8");
            serverConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        } catch (Exception x) {
            x.printStackTrace();
            Log.e("PostData", "Failed to connect to PizzaPi");
            return false;
        }

        if (serverConnection != null) {
        /* Parses through data and stores them if server connection was successful */
            String accountParameter = "username=" + params[1] + "&password=" + params[2];

            try {
                OutputStreamWriter out = new OutputStreamWriter(serverConnection.getOutputStream());
                out.write(accountParameter);
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
            return false;
        }
        return false;
    }
}
