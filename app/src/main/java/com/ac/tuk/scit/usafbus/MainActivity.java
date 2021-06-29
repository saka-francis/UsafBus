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

    private static final String HTTP_JSON_URL = "http://192.168.208.126/android_booking_app/fetch_data.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /**
         * Showing a ProgressDialog as the buses load
         */
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setClipToPadding(false);

        getCompany();

        adapter = new BusesAdapter(companyList, this);
        recyclerView.setAdapter(adapter);
    }

    private void getCompany() {


            StringRequest request = new StringRequest(Request.Method.GET, HTTP_JSON_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONArray companies = new JSONArray(response);
                                int i;
                                for (i = 0; i < companies.length(); i++) ;
                                JSONObject companyObjects = companies.getJSONObject(i);

                                String image = companyObjects.getString("image");
                                String name = companyObjects.getString("name");

                                Company company = new Company(image, name);
                                companyList.add(company);
                                /**
                                 * hiding the progressDialog once loading is done
                                 */
                                progressDialog.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            /**
                             * hiding the progressDialog
                             */
                            progressDialog.dismiss();
                        }
                    });
            Volley.newRequestQueue(this).add(request);

        }


    }