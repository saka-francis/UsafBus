package com.ac.tuk.scit.usafbus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    RecyclerView recyclerView;
    BusesAdapter adapter;
    List<Company> companyList;

    private static final String HTTP_JSON_URL = "http://192.168.137.1/android_booking_app/fetch_data.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setClipToPadding(false);

        companyList = new ArrayList<>();

        getCompany();
    }

    private void getCompany() {
        /**
         * Showing a ProgressDialog as the buses load
         */
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

            StringRequest Srequest = new StringRequest(Request.Method.GET,
                    HTTP_JSON_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            /**
                             * hiding the progressDialog once loading is done
                             */
                            progressDialog.dismiss();

                            try {
                                JSONArray array = new JSONArray(response);

                                for (int i = 0; i < array.length(); i++) {
                                    ;
                                    JSONObject companyObjects = array.getJSONObject(i);

                                    Company company = new Company(
                                            companyObjects.getString("image"),
                                            companyObjects.getString("name")
                                    );

                                    companyList.add(company);
                                }
                                adapter = new BusesAdapter(companyList, getApplicationContext());
                                recyclerView.setAdapter(adapter);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            /**
                             * hiding the progressDialog once loading is done
                             */
                            progressDialog.dismiss();

                            Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(Srequest);

        }


    }