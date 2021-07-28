package com.ac.tuk.scit.usafbus;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

  //  public ProgressDialog progressDialog;

    ArrayList<Company> companyArrayList;
    RecyclerView recyclerView;
    BusesAdapter adapter;

    private static final String TAG="tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Choose your desired bus");


        companyArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.busCompanyView);

        //Make call to AsyncTask
       // System.out.println("we are about to call do in background");
        BusFetchTask busFetchTask = new BusFetchTask();
        busFetchTask.execute("String", "String", "String");

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

   public class BusFetchTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            companyArrayList.clear();
            System.out.println("WE ARE IN DO IN BACKGROUND");
            String feedback =  null;
            try {

                // Enter URL address where your json file resides
                URL url = new URL("http://192.168.221.133/Android_booking_app/fetch_data.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                System.out.println("CONNECTED TO THE URL");

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String temp;

                    while ((temp = reader.readLine()) != null) {
                        stringBuilder.append(temp);
                    }
                    feedback = stringBuilder.toString();

                    System.out.println("The JSON feedback is " + feedback);
                } else {
                    feedback = "error";
                }

            }

            catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            return feedback;
        }


        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            if (result!=null) {

                try {
                    JSONObject json_data = new JSONObject(result);
                    JSONArray jArray = json_data.getJSONArray("bus");

                    // Extract data from json and store into ArrayList as class objects
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jsonObject = jArray.getJSONObject(i);
                        Company company = new Company();
                        company.setName(jsonObject.getString("name"));
                        company.setImage(jsonObject.getString("image"));

                        companyArrayList.add(company);
                    }
                    adapter = new BusesAdapter(MainActivity.this, companyArrayList);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}

