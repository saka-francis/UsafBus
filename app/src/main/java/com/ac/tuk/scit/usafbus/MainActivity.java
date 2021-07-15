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

    /**
     * CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
     */
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    RecyclerView recyclerView;
    BusesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Make call to AsyncTask
         */
        new BusesTask();

    }

    public class BusesTask extends AsyncTask<Void, Void, String> {
        BusesTask() {

        }

        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        HttpURLConnection conn;
        URL url = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            progressDialog.setMessage("\tLoading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }


        @Override
        protected String doInBackground(Void... params) {
            try {

                /**
                 * URL address where the json file resides
                 */

                url = new URL("http://192.168.208.126/Android_booking_app/fetch_data.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                /**
                 * HttpURLConnection class Setup to send and receive data from php and mysql
                 */
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                /**
                 * DoOutput set to true as we receive data from json file
                 * */
                conn.setDoOutput(true);

            } catch (IOException e1) {
                /**
                 * TODO Auto-generated catch block
                 */
                e1.printStackTrace();
                return e1.toString();
            }
            try {

                int response_code = conn.getResponseCode();
                /**
                 *  Check if successful connection made
                 */
                if (response_code == HttpURLConnection.HTTP_OK) {
                    /**
                     * Read data sent from server
                     */
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    /**
                     * Pass data to onPostExecute method
                     */
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            /**
             * this method will be running on UI thread
             */

            progressDialog.dismiss();
            List<Company> bus = new ArrayList<>();

            progressDialog.dismiss();

            try {

                JSONArray jArray = new JSONArray(result);

                /**
                 * Extract data from json and store into ArrayList as class objects
                 */
                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject json_data = jArray.getJSONObject(i);
                    Company buses = new Company();

                    buses.image = json_data.getString("image");
                    buses.name = json_data.getString("name");

                    /*
                    buses.pickStation= json_data.getString("pickStation");
                    buses.destination= json_data.getString("destination");
                    buses.price= json_data.getDouble("price");
                    */
                    bus.add(buses);
                }
                /**
                 * Setup and Handover data to recyclerview
                 */
                recyclerView.findViewById(R.id.recyclerView);
                adapter = new BusesAdapter(MainActivity.this, bus);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

            } catch (JSONException e) {

                Toast.makeText(getApplicationContext(), "Something went wrong. Please try again!!!", Toast.LENGTH_LONG).show();
            }

        }
    }
}