package com.safeconnectionsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.safeconnectionsapp.adapter.MenuRecyclerViewAdapter2;
import com.safeconnectionsapp.app.MyApplication;
import com.safeconnectionsapp.helper.AllKeys;
import com.safeconnectionsapp.pojo.Menudata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class RequestQuotationActivity extends AppCompatActivity {

    private Context context = this;
    private String TAG = RequestQuotationActivity.class.getSimpleName();
    private SpotsDialog pDialog;
    ArrayList<Menudata> list_menu = new ArrayList<Menudata>();
    private TextView txtnodata;
    private RecyclerView rv_menu;
    private MenuRecyclerViewAdapter2 adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_quotation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        setTitle(getString(R.string.str_quotation));

        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();

        }

        pDialog = new SpotsDialog(context);
        pDialog.setCancelable(false);

        txtnodata = (TextView) findViewById(R.id.txtnodata);
        rv_menu = (RecyclerView) findViewById(R.id.rv_menu);


        LinearLayoutManager lManger = new LinearLayoutManager(context);
        lManger.setOrientation(LinearLayoutManager.VERTICAL);
        rv_menu.setLayoutManager(lManger);

        rv_menu.addOnItemTouchListener(new AllKeys.RecyclerTouchListener(context, rv_menu, new AllKeys.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                try {
                    Toast.makeText(context, "Name : " + list_menu.get(position).getMenuname() + " Id : " + list_menu.get(position).getMenuid(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context,
                            RequestQuotationActivity.class);
                    intent.putExtra("Id", list_menu.get(position).getMenuid());
                    intent.putExtra("ActivityName", TAG);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        getAllQuotationDetailsFromServer();
    }

    private void getAllQuotationDetailsFromServer() {
        String url = AllKeys.WEBSITE + "ViewMenus?type=menu";
        Log.d(TAG, "URL ViewMenus " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "Response  : " + response.toString());

                try {
                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);

                    list_menu.clear();
                    if (error_status == false) {

                        if (record_status == true) {


                            JSONArray arr = response.getJSONArray(AllKeys.ARRAY_DATA);
                            for (int i = 0; i < arr.length(); i++) {

                                JSONObject c = arr.getJSONObject(i);

                                //Menudata(String menuid, String menuname, String image) {
                                Menudata md = new Menudata(c.getString(AllKeys.TAG_MENUID), c.getString(AllKeys.TAG_MENU_TITLE), c.getString(AllKeys.TAG_IMAGE_URL), false);
                                list_menu.add(md);

                            }

                            if (list_menu.size() == 0) {
                                txtnodata.setVisibility(View.VISIBLE);
                                rv_menu.setVisibility(View.GONE);
                            } else {
                                txtnodata.setVisibility(View.GONE);
                                rv_menu.setVisibility(View.VISIBLE);


                            }

                            adapter = new MenuRecyclerViewAdapter2(context, list_menu);
                            rv_menu.setAdapter(adapter);


                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ServerError || error instanceof NetworkError) {

                } else {

                }

            }
        });
        MyApplication.getInstance().addToRequestQueue(request);


    }

    private void hideDialog() {
        if (pDialog.isShowing()) {
            pDialog.cancel();
            pDialog.dismiss();

        }
    }

    private void showDialog() {

        if (!pDialog.isShowing()) {
            pDialog.show();

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
