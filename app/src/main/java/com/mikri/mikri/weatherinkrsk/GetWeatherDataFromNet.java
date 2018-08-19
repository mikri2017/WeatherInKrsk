package com.mikri.mikri.weatherinkrsk;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetWeatherDataFromNet extends AsyncTask<String, Void, ArrayList<String>> {
    private InputStream inputStream = null;
    private String net_result = "";
    private ArrayList<String> arrlstStrRes = null;

    protected void onPreExecute() {
        arrlstStrRes = new ArrayList<String>();
    }

    @Override
    protected ArrayList<String> doInBackground(String... weather_url) {
        arrlstStrRes.clear();

        try {
            URL url = new URL(weather_url[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                net_result = readStream(in);
            } catch (Exception e) {
                Log.e("Exception", e.toString());
                e.printStackTrace();
                arrlstStrRes.clear();
                arrlstStrRes.add("Error");
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("Exception", e.toString());
            e.printStackTrace();
            arrlstStrRes.clear();
            arrlstStrRes.add("Error");
        }

        if(arrlstStrRes.isEmpty()) {
            //parse JSON data
            try {
                JSONObject jObject = new JSONObject(net_result);
                arrlstStrRes.add(jObject.getString("t"));
                arrlstStrRes.add(jObject.getString("p"));
                arrlstStrRes.add(jObject.getString("h"));
            } catch (Exception e) {
                Log.e("Exception", "Error: " + e.toString());
                arrlstStrRes.clear();
                arrlstStrRes.add("Error");
            } // catch (JSONException e)
        }

        return arrlstStrRes;
    } // protected Void doInBackground(String... params)

    protected void onPostExecute(ArrayList<String> result) {

    } // protected void onPostExecute(Void v)

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while (i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }
}
