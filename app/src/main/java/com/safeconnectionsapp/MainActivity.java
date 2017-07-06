package com.safeconnectionsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;


import com.safeconnectionsapp.app.MyApplication;
import com.safeconnectionsapp.helper.AllKeys;
import com.safeconnectionsapp.helper.CustomRequest;
import com.safeconnectionsapp.session.SessionManager;

import org.json.JSONObject;

import java.security.Security;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    private Context context = this;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private String TAG = MainActivity.class.getSimpleName();
    private SpotsDialog pDialog;
    private LinearLayout llQuotation;
    private LinearLayout llComplain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        setTitle("Dashboard");

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();

        pDialog =new SpotsDialog(context);
        pDialog.setCancelable(false);



        if(userDetails.get(SessionManager.KEY_USER_MOBILE).length() ==10 && userDetails.get(SessionManager.KEY_VERSTATUS).equals("0"))
        {
            Intent intent = new Intent(context , VerificationActivity.class);
            startActivity(intent);
            finish();

        }
        else if(userDetails.get(SessionManager.KEY_USER_ID).equals("0"))
        {

            Intent intent = new Intent(context , LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {

            UpdateFcmTokenDetailsToServer();
            //Commit statement
            //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();

            /*Intent intent = new Intent(context , InviteCodeActivity.class);
            startActivity(intent);
            finish();*/


        }


        try {
            Log.d(TAG , "DEVICE ID : " +  Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }


        llQuotation = (LinearLayout)findViewById(R.id.llQuotation);
        llComplain = (LinearLayout)findViewById(R.id.llComplain);

        llQuotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* sessionManager.setRequestType("quotation");
                Intent intent = new Intent(context , MenuActivity.class);
                startActivity(intent);
                finish();*/
                Toast.makeText(context, "Quotation...", Toast.LENGTH_SHORT).show();
            }
        });

        llComplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.setRequestType("complaint");

                Intent intent = new Intent(context , MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });




    }
    //onCreate completed

    private void UpdateFcmTokenDetailsToServer()
    {
        showDialog();



        String url = AllKeys.WEBSITE + "InsertFCMToken";

        Log.d(TAG, "URL  InsertFCMToken : " + url);


        CustomRequest request = new CustomRequest(Request.Method.POST, url, null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "Response InsertFCMToken : " + response.toString());
                hideDialog();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof ServerError || error instanceof NetworkError) {
                    hideDialog();
                } else {
                    UpdateFcmTokenDetailsToServer();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();

                String fcm_tokenid = "";
                try {
                    MyFirebaseInstanceIDService mid = new MyFirebaseInstanceIDService();
                    fcm_tokenid = mid.onTokenRefreshNew(context);

                } catch (Exception e) {
                    fcm_tokenid = "";
                    e.printStackTrace();
                }


                params.put("type", "fcmtoken");
                params.put("userid", userDetails.get(SessionManager.KEY_USER_ID));
                params.put("fcmtoken", fcm_tokenid);

                Log.d(TAG, "Update FCM Params :" + params.toString());


                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(request);


    }

    private void hideDialog() {
        if(pDialog.isShowing())
        {
            pDialog.cancel();
            pDialog.dismiss();

        }
    }

    private void showDialog() {

        if(!pDialog.isShowing())
        {
            pDialog.show();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main_activity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_logout)
        {
            sessionManager.logoutUser();


        }
        return super.onOptionsItemSelected(item);
    }
}
