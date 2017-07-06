package com.safeconnectionsapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
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
import com.safeconnectionsapp.adapter.CategoryAdapterRecyclerView;
import com.safeconnectionsapp.app.MyApplication;

import com.safeconnectionsapp.helper.AllKeys;
import com.safeconnectionsapp.pojo.Category;
import com.safeconnectionsapp.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;

public class CategoryAcitivity extends AppCompatActivity {

    private TextView tvnodata;
    private Context context = this;
    private SpotsDialog spotsDialog;
    private RecyclerView rv_category;
    private String TAG = CategoryAcitivity.class.getSimpleName();
    private ArrayList<Category> list_cateogry = new ArrayList<Category>();
    private SessionManager sessionmanager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        /*getTheme().applyStyle(R.style.OverlayPrimaryColorNH, true);*/



        setContentView(R.layout.activity_category_acitivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setHomeAsUpIndicator(back_button);
        } catch (Exception e) {
            e.printStackTrace();
        }


        sessionmanager = new SessionManager(getApplicationContext());
        userDetails = sessionmanager.getSessionDetails();

        spotsDialog = new SpotsDialog(context);
        spotsDialog.setCancelable(false);


        setTitle("Categories");
        tvnodata = (TextView) findViewById(R.id.tvnodata);
        rv_category = (RecyclerView) findViewById(R.id.rv_category);


        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        //rv_category.addItemDecoration(new dbhandler.GridSpacingItemDecoration(2, dpToPx(10), true));
        rv_category.setLayoutManager(layoutManager);
        rv_category.setItemAnimator(new DefaultItemAnimator());


        rv_category.addOnItemTouchListener(new AllKeys.RecyclerTouchListener(getApplicationContext(), rv_category, new AllKeys.ClickListener() {
            @Override
            public void onClick(View view, int position) {

/*

                sessionmanager.setCategoryTypeAndIdDetails(String.valueOf(list_cateogry.get(position).getCategoryid()) , list_cateogry.get(position).getCategoryname());


                Intent intent = new Intent(context, ItemDisplayActivity.class);
                startActivity(intent);
                finish();
*/


                sessionmanager.setCategoryTypeAndIdDetails(String.valueOf(list_cateogry.get(position).getCategoryid()), list_cateogry.get(position).getCategoryname(), "category");



                Intent intent = new Intent(context, ItemDisplayActivity.class);
                intent.putExtra(AllKeys.ACTIVITYNAME, "Category");
                startActivity(intent);
                finish();




                //   Toast.makeText(context, "Clicked on : " + list_cateogry.get(position).getCategoryname(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        getCategoryMasterDetailsFromServer();


    }

    public  int dpToPx(int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void getCategoryMasterDetailsFromServer() {
        showDialog();
        String url = AllKeys.WEBSITE + "ViewCategory?type=category";
        Log.d(TAG, "URL ViewCategory : " + url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "Response ViewCategoryData : " + response);
                try {

                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);

                    list_cateogry.clear();
                    if (error_status == false) {
                        if (record_status == true) {


                            JSONArray arr = response.getJSONArray(AllKeys.ARRAY_DATA);
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject c = arr.getJSONObject(i);


                                Integer category_id = c.getInt(AllKeys.TAG_CATEGORYID);
                                String category_name = c.getString(AllKeys.TAG_CATEGORY_NAME);
                                String category_image = c.getString(AllKeys.TAG_CATEGORY_IMAGE);
                                //String parentid = c.getString(AllKeys.TAG_PARENTID);


                                //String totalProducts = c.getString(AllKeys.TAG_TOTAL_RECORDS);
                                //String totalSubcategpries = c.getString(AllKeys.TAG_TOTAL_SUB_CATEGORIES);


                                /*if (parentid.equals("0")) {*/

                                    Category car = new Category(category_id, category_name, category_image);

                                    list_cateogry.add(car);
                                /*}*/


                            }


                            tvnodata.setVisibility(View.GONE);
                            rv_category.setVisibility(View.VISIBLE);


                            CategoryAdapterRecyclerView adapter = new CategoryAdapterRecyclerView(context, list_cateogry);
                            rv_category.setAdapter(adapter);


                            hideDialog();


                        } else {

                            tvnodata.setVisibility(View.VISIBLE);
                            rv_category.setVisibility(View.GONE);
                            hideDialog();
                            Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), "" + str_error, Toast.LENGTH_SHORT).show();
                        //   Snackbar.make(coordinatorLayout, str_error, Snackbar.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    hideDialog();

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Log.d(TAG, "ViewCategoryData Error  : " + error.getMessage());
                if (error instanceof ServerError || error instanceof NetworkError) {

                    hideDialog();
                } else {
                    getCategoryMasterDetailsFromServer();
                }
            }
        }
        );
        MyApplication.getInstance().addToRequestQueue(request);

    }


    public void showDialog() {

        try {
            if (!spotsDialog.isShowing()) {

                spotsDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void hideDialog() {
        try {
            if (spotsDialog.isShowing()) {
                spotsDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            Intent intent = new Intent(context, DashBoardActivity.class);
            startActivity(intent);
            finish();


        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, DashBoardActivity.class);
        startActivity(intent);
        finish();

    }


}
