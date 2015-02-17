package com.example.mathias.snow;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by romjallet on 17/02/15.
 */
public class GetWeatherTask extends AsyncTask<String, String, String> {
        private Station station;

        public GetWeatherTask(Station station){
            this.station = station;
        }

        @Override
        protected void onPreExecute(){
            station.progress = new ProgressDialog(station);
            station.progress.setMessage("Chargement...");
            station.progress.show();
        }


        @Override
        protected String doInBackground(String... uri) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                if(station.progress.isShowing()){
                    station.progress.dismiss();
                }
                Toast toast = Toast.makeText(station.getApplicationContext(),"Erreur réseau",Toast.LENGTH_SHORT);
                toast.show();

            } catch (IOException e) {

            }
            //Station.updateWidgets(responseString);
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result != null) {
                station.updateWidgets(result);
            }
            if(station.progress.isShowing()){
                station.progress.dismiss();

            }
            Vibrator vib=(Vibrator)this.station.getSystemService(Context.VIBRATOR_SERVICE);
            vib.vibrate(10000);
        }
    }
