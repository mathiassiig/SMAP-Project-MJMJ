package mathiassiig.assignment2;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class weatherService extends Service {

    public static final long LOOP_TIME = 5000;
    private boolean started = false;

    public weatherService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String weatherURL = intent.getStringExtra("weather");
        started = true;
        runInBackground(weatherURL);
        return super.onStartCommand(intent, flags, startId);
    }

    private void runInBackground(final String weatherURL) {

        AsyncTask<Object, Object, String> task = new AsyncTask<Object, Object, String>() {
            @Override
            protected String doInBackground(Object[] params) {
                String weatherData;
                String s = "Background job";
                try {
                    Thread.sleep(LOOP_TIME);
                    weatherData = callURL(weatherURL);
                } catch (Exception e) {
                    s+= " did not finish due to error";
                    return s;
                }

             //   Toast.makeText(weatherService.this, "Weather:" +weatherData.toString(), Toast.LENGTH_SHORT).show();

                return weatherData;
            }


            @Override
            protected void onPostExecute(String stringResult) {
                super.onPostExecute(stringResult);
                broadcastTaskResult(stringResult);

                if(started){
                    runInBackground(weatherURL);
                }
            }
        };

        task.execute();

    }

    private String callURL(String callUrl) {

        InputStream is = null;

        try {
            //create URL
            URL url = new URL(callUrl);

            //configure HttpURLConnetion object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);


            // Starts the request
            conn.connect();
            int response = conn.getResponseCode();

            //probably check check on response code here!

            //give user feedback in case of error


            is = conn.getInputStream();

            // Convert the InputStream into a string

            String contentAsString = convertStreamToStringBuffered(is);
            return contentAsString;


        } catch (ProtocolException pe) {

        } catch (UnsupportedEncodingException uee) {

        } catch (IOException ioe) {

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ioe) {

                }
            }
        }
        return null;
    }

    private String convertStreamToStringBuffered(InputStream is) {
        String s = "";
        String line = "";

        BufferedReader rd = new BufferedReader(new InputStreamReader(is));


        try {
            while ((line = rd.readLine()) != null) { s += line; }
        } catch (IOException ex) {
           ;
        }

        // Return full string
        return s;
    }

    private void broadcastTaskResult(String result){
        Intent broadcastIntent = new Intent("weatherInfo");
        broadcastIntent.putExtra("Result", result);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        started = false;
        super.onDestroy();
    }
}
