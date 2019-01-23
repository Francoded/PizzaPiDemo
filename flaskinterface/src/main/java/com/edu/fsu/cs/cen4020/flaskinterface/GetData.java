package com.edu.fsu.cs.cen4020.flaskinterface;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class GetData extends AsyncTask<String, Void, ArrayList<String>> {


    private URL mURL;
    private HttpURLConnection serverConnection;
    private BufferedReader in;

    @Override
    protected ArrayList<String> doInBackground(String... params) {

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


        /* Parses through data and stores them if server connection was successful */
        ArrayList<String> recipes = new ArrayList<>();
        String temp;

        try {
            if (serverConnection != null) {
                if (!(params[0].contains("recipepage") || params[0].contains("rotd"))) {
                    in = new BufferedReader(new InputStreamReader(serverConnection.getInputStream(), "UTF-8"));
                    if (in != null) {
                        temp = in.readLine();
                        while (temp != null) {
                            recipes.add(temp);
                            temp = in.readLine();
                        }
                        return recipes;
                    }
                } else {
                    String result = "";
                    in = new BufferedReader(new InputStreamReader(serverConnection.getInputStream(), "UTF-8"));
                    StringBuilder sBuilder = new StringBuilder();

                    String line = null;
                    while ((line = in.readLine()) != null) {
                        sBuilder.append(line + "\n");
                    }

                    result = sBuilder.toString();
                    JSONObject jObject = new JSONObject(result);

                    String recipeName =jObject.getString("Name");
                    String description = jObject.getString("Description");
                    String measurements = jObject.getString("Measurements");
                    String missingMeasurements = jObject.getString("MissingMeasurements");
                    String instructions = jObject.getString("Instructions");
                    ArrayList<String> recipeInformation = new ArrayList<>();
                    Log.d("GetData", "This is the name: " + recipeName);
                    Log.d("GetData", "This is the description: " + description);
                    Log.d("GetData", "This is the measurements: " + measurements);
                    Log.d("GetData", "This is the missing measurements: " + missingMeasurements);
                    Log.d("GetData", "This is the instructions: " + instructions);
                    recipeInformation.add(recipeName);
                    recipeInformation.add(description);
                    recipeInformation.add(measurements);
                    recipeInformation.add(missingMeasurements);
                    recipeInformation.add(instructions);
                    return recipeInformation;
                }
            } else
                Log.e("GetData", "Connection Failed");

        } catch (Exception x) {
            x.printStackTrace();
            Log.e("GetData", "Failed to get data");
        }

        return null;
    }
}
