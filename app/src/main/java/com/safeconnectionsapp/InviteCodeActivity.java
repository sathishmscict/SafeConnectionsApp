package com.safeconnectionsapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kosalgeek.android.imagebase64encoder.*;
import com.safeconnectionsapp.app.MyApplication;
import com.safeconnectionsapp.helper.AllKeys;

import com.safeconnectionsapp.session.SessionManager;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import dmax.dialog.SpotsDialog;

public class InviteCodeActivity extends AppCompatActivity {

    private EditText txtreferalcode;
    private TextView lblsubmit;
    private TextView txtnext;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails;
    private String referalcode = "";
    private String responseresult = "";
    private Context context = this;
    private String TAG = InviteCodeActivity.class.getSimpleName();
    private SpotsDialog spotsDialog;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitecode);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Invite Code");

        sessionManager = new SessionManager(getApplicationContext());

        userDetails = new HashMap<String, String>();

        userDetails = sessionManager.getSessionDetails();

        spotsDialog = new SpotsDialog(context);
        spotsDialog.setCancelable(false);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        txtreferalcode = (EditText) findViewById(R.id.editText);
        lblsubmit = (TextView) findViewById(R.id.submit);
        txtnext = (TextView) findViewById(R.id.txtnext);

        final SpannableString content = new SpannableString("SKIP");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        txtnext.setText(content);

        txtnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),
                        DashBoardActivity.class);
                startActivity(intent);
                finish();
                // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

            }
        });

        lblsubmit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if (txtreferalcode.getText().toString().equals("")) {
                    Animation shake = AnimationUtils.loadAnimation(InviteCodeActivity.this, R.anim.shake);
                    findViewById(R.id.editText).startAnimation(shake);
                    Toast.makeText(getApplicationContext(),
                            "Please Enter Referal Code", Toast.LENGTH_SHORT)
                            .show();

                } else if (txtreferalcode.getText().toString().length() != 8) {
                    Animation shake = AnimationUtils.loadAnimation(InviteCodeActivity.this, R.anim.shake);
                    findViewById(R.id.editText).startAnimation(shake);
                    Toast.makeText(getApplicationContext(),
                            "Invalid Referal code,try again", Toast.LENGTH_LONG)
                            .show();
                } else {
                    referalcode = txtreferalcode.getText().toString();

                 //   new InviteCodeVerification().execute();
                    sendInviteCodeVerificationDetailsToServer();

                }

                // Toast.makeText(getApplicationContext() ,
                // "Please wait..",Toast.LENGTH_LONG).show();

            }
        });

    }

    private void sendInviteCodeVerificationDetailsToServer()
    {
        showDialog();


        String url = AllKeys.WEBSITE + "GetCheckRefcode?ACTION=checkrefcode&USERID=" + userDetails.get(SessionManager.KEY_USER_ID) + "&REFCODE=" + referalcode + "&USERREFCODE=" + userDetails.get(SessionManager.KEY_REFERAL_CODE) + "";

        Log.d("URL Invite code", url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "  GetCheckRefcode Response : " + response);


                try
                {

                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);

                    if (error_status == false)
                    {


                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setIcon(R.mipmap.ic_launcher);
                            alert.setCancelable(false);
                            alert.setTitle(getString(R.string.app_name));
                            alert.setMessage(
                                    str_error);

                            alert.setNeutralButton("OK", new OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    Intent intent = new Intent(context, DashBoardActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                    // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                }
                            });

                            alert.show();







                    } else {
                        hideDialog();
                        Toast.makeText(context, "" + str_error, Toast.LENGTH_SHORT).show();

                       // Snackbar.make(coordinatorLayout, str_error, Snackbar.LENGTH_SHORT).show();

                        Animation shake = AnimationUtils.loadAnimation(InviteCodeActivity.this, R.anim.shake);
                        findViewById(R.id.editText).startAnimation(shake);



                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setIcon(R.mipmap.ic_launcher);
                        alert.setCancelable(false);
                        alert.setTitle(getString(R.string.app_name));
                        alert.setMessage(
                                str_error);

                        alert.setNeutralButton("OK", new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub

                                dialog.cancel();
                                dialog.dismiss();
                                // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                            }
                        });

                        alert.show();



                    }


                } catch (JSONException e) {
                    hideDialog();
                    hideDialog();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof ServerError || error instanceof NetworkError) {

                    hideDialog();
                } else {
                    sendInviteCodeVerificationDetailsToServer();

                }
            }
        });
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
                spotsDialog.hide();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_referalcode, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



}
