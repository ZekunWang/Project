package com.zwang_000.whatstheweather;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class MainActivity extends Activity {

    EditText cityName;
    TextView weatherTextView;
    DownloadTask task;

    public void randomClick(View view){
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(), 0);  // hide keyboard
    }

    public void findWeather(View view){
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(), 0);  // hide keyboard
        try {
            String encodedCityName = URLEncoder.encode(cityName.getText().toString(), "UTF-8");
            task = new DownloadTask();
            task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=1c3d873bd8c891918bf4d32e7f98aa0d");
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(getApplicationContext(), "Could not find weather0", Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName = (EditText) findViewById(R.id.cityName);
        weatherTextView = (TextView) findViewById(R.id.weatherTextView);
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            URL url;
            HttpURLConnection connection = null;
            try {
                url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data != -1){
                    result += (char) data;
                    data = reader.read();
                }
                return result;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Could not find weather1", Toast.LENGTH_LONG);
            }
            return null;
        }
/*
*   {"coord":{"lon":-89.58,"lat":40.67},
*   "weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"01d"}],
*   "base":"stations",
*   "main":{"temp":278.09,"pressure":1019,"humidity":52,"temp_min":277.15,"temp_max":279.15},
*   "visibility":16093,
*   "wind":{"speed":2.1,"deg":180},
*   "clouds":{"all":1},
*   "dt":1457134500,
*   "sys":{"type":1,"id":981,"message":0.1058,"country":"US","sunrise":1457180709,"sunset":1457222085},
*   "id":4890549,
*   "name":"East Peoria",
*   "cod":200}
* */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String message = "";
                String name = "";
                name = jsonObject.getString("name");
                if(name != ""){
                    message += name + '\n';
                }
                JSONArray arr = new JSONArray(jsonObject.getString("weather"));
                for(int i = 0; i < arr.length(); i++){
                    JSONObject jsonPart = arr.getJSONObject(i);
                    String main = "";
                    String description = "";
                    main = jsonPart.getString("main");
                    description = jsonPart.getString("description");
                    if(main != "" && description != ""){
                        message += main + ": " + description + "\r\n";
                    }
                }
                JSONObject mainPart = new JSONObject(jsonObject.getString("main"));
                String temp = "";
                String humidity = "";
                double calTemp = 0;
                temp = mainPart.getString("temp");
                if(temp != ""){
                    calTemp = ((int)((Double.parseDouble(temp) - 273.15) * 100)) / 100.0;
                }
                humidity = mainPart.getString("humidity");
                if(temp != "" && humidity != ""){
                        message += "Temperature: " + calTemp + " °C\n" + "Humidity: " + humidity + "\n";
                }
                if(message != ""){
                    weatherTextView.setText(message);
                }else{
                    Toast.makeText(getApplicationContext(), "Could not find weather2", Toast.LENGTH_LONG);
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Could not find weather3", Toast.LENGTH_LONG);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}