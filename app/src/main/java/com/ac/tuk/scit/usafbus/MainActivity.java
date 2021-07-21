package com.ac.tuk.scit.usafbus;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView recyclerView;
    private BusesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Make call to AsyncTask
        BusFetchTask busFetchTask = new BusFetchTask();
        //busFetchTask.execute((Void) null);
        busFetchTask.execute();

    }

    private class BusFetchTask extends AsyncTask<Void, Void, String> {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(Void... params) {
            try {

                // Enter URL address where your json file resides
                url = new URL("http://192.168.137.1//Android_booking_app/fetch_data.php");


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we receive data from json file
                conn.setDoOutput(true);

            }catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }
            try {
                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                    //creating StringBuilder
                    StringBuilder sb = new StringBuilder();

                    String line;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (sb.toString());

                }else {

                    return ("unsuccessful");
                }

            }catch (IOException e2) {
                e2.printStackTrace();
                return e2.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread
            pdLoading.dismiss();
            List<Company> Buses = new ArrayList<>();

            pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);
                String[] Bus = new String[jArray.length()];
                // Extract data from json and store into ArrayList as class objects
                for(int i=0; i<jArray.length(); i++){
                    JSONObject json_data = jArray.getJSONObject(i);

                    Bus[i] = json_data.getString("name");
                    Bus[i] = json_data.getString("image");
/**
                    Company Bus = new Company(json_data.getString("image"), json_data.getString("name")

                            /**
                            , json_data.getString("PickStation"),
                            json_data.getString("Destination"),
                            json_data.getDouble("Price")
                             */
                           // );
                  //  Bus.Image= json_data.getString("image");
                  //  Bus.Name= json_data.getString("name");
                    /**
                    Bus.PickStation= json_data.getString("PickStation");
                    Bus.Destination= json_data.getString("Destination");
                    Bus.RoutPrice= json_data.getDouble("Price");
                    */
//*/
                   // Buses.add(Bus);
                }

                // Setup and Handover data to recyclerview
                recyclerView = (RecyclerView)findViewById(R.id.busCompanyView);
                adapter = new BusesAdapter(MainActivity.this, Buses);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

            } catch (JSONException e3) {
                //Showing the "JSONException e" error message
                Toast.makeText(MainActivity.this, "Something is wrong with JSON conversion", Toast.LENGTH_LONG).show();
            }

        }

    }
}

