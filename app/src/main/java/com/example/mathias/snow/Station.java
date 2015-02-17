package com.example.mathias.snow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


public class Station extends ActionBarActivity {
    ImageView ImageTemps;
    TextView texteStation;
    TextView etatStation;
    TextView temperatureMatin;
    TextView temperatureAprem;
    TextView valeurVent;
    TextView valeurNeige;
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);

        //Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        ImageTemps = (ImageView) findViewById(R.id.tempsImage);
        texteStation = (TextView) findViewById(R.id.txtStation);
        texteStation.setText(message);
        etatStation = (TextView) findViewById(R.id.etatStation);
        temperatureMatin = (TextView) findViewById(R.id.TempMatin);
        temperatureAprem = (TextView) findViewById(R.id.TempAprem);
        valeurNeige = (TextView) findViewById(R.id.valeurNeige);
        valeurVent = (TextView) findViewById(R.id.valeurVent);

        String url="http://snowlabri.appspot.com/snow?station="+message;
        new GetWeatherTask(this).execute(url);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_station, menu);
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
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendStation(View view){

    }

    public void updateWidgets(String result) {
        try {

            JSONObject json= (JSONObject) new JSONTokener(result).nextValue();
            String erreur = "Pas d'info";
            if (!json.isNull("ouverte"))
                etatStation.setText((String) json.get("ouverte"));
            else
                etatStation.setText("erreur");

            if (!json.isNull("temps"))
                switch((String) json.get("temps")){
                    case "beau":
                        ImageTemps.setImageResource(R.drawable.soleil);
                        break;
                    case "couvert":
                        ImageTemps.setImageResource(R.drawable.nuage);
                        break;
                    case "neige":
                        ImageTemps.setImageResource(R.drawable.neige);
                        break;
                    default:
                        ImageTemps.setImageResource(R.drawable.error);

                }

            if (!json.isNull("temperatureMatin"))
                temperatureMatin.setText((String) json.get("temperatureMatin"));
            else
                temperatureMatin.setText(erreur);

            if (!json.isNull("temperatureMidi"))
                temperatureAprem.setText ((String) json.get("temperatureMidi"));
            else
                temperatureAprem.setText(erreur);

            if (!json.isNull("vent"))
                valeurVent.setText((String) json.get("vent"));
            else
                valeurVent.setText(erreur);

            if (!json.isNull("neige"))
                valeurNeige.setText((String) json.get("neige"));
            else
                valeurNeige.setText(erreur);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}