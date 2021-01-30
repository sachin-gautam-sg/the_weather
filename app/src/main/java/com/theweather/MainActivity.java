package com.theweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText getCity;
    private TextView showTemprature, showStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        fullView();
        setContentView(R.layout.activity_main);
        init();
    }

    //to initialize the views and widgets
    private void init(){
        getCity= findViewById(R.id.getCity);
        showTemprature= findViewById(R.id.showTemprature);
        showStatus= findViewById(R.id.showStatus);
    }

    //for full screen all over the app
    private void fullView(){
       try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_main);
    }

    //button event
    public void searchCity(View view){
        // button event
        String cityName= getCity.getText().toString();
        String url= "https://api.openweathermap.org/data/2.5/weather?q="+cityName+"&appid=4dc0d62b08e7c49ebe23b4f39e33f9d5";
        if(TextUtils.isEmpty(cityName)){
            Toast.makeText(MainActivity.this, "Enter any city name.", Toast.LENGTH_SHORT).show();
        }
        else{
            checkTemprature(url);
        }

    }

    //function to get weather of the user desired city
    private void checkTemprature(String url){
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject object= response.getJSONObject("main");
                    Double inCelcius= Double.parseDouble(object.getString("temp"))-273;
                    Double inCelciusMax= Double.parseDouble(object.getString("temp_max"))-273;
                    Double inCelciusMin= Double.parseDouble(object.getString("temp_min"))-273;
                    showTemprature.setText(inCelcius.toString().substring(0,4));
                    showStatus.setText("max: "+ inCelciusMax.toString().substring(0,4)+"\nmin: "+ inCelciusMin.toString().substring(0,4));
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }
}