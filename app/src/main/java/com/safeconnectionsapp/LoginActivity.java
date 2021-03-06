package com.safeconnectionsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;
import com.safeconnectionsapp.app.MyApplication;
import com.safeconnectionsapp.helper.AllKeys;
import com.safeconnectionsapp.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import dmax.dialog.SpotsDialog;


public class LoginActivity extends AppCompatActivity  {

    //private TextView custom_signup_button;
    private EditText edtMobile;

    private TextInputLayout edtMobileWrapper;

    private Button btnsignin;

    private String TAG = LoginActivity.class.getSimpleName();
    private Context context = this;
    private SpotsDialog pDialog;
    private SessionManager sessionmanager;

    private TextView errorTextView;



    private static final int SIGN_IN_CODE = 0;
    private static final int PROFILE_PIC_SIZE = 120;

    private boolean is_intent_inprogress;
    private boolean is_signInBtn_clicked;
    private int request_code;

    private String facebook = "";

    /*ProgressDialog progress_dialog;*/

    SpotsDialog progress_dialog;
    public static final int MY_PERMISSIONS_REQUEST_GET_ACCOUNTS=100;

    public String LOGIN_TYPE="normal",USER_EMAIL="";
    private String PROVIDER_USERID="0";

    private HashMap<String, String> userDetails= new HashMap<String, String>();
    private CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);


        if (Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setTitle(getString(R.string.app_name)+" Login");

        pDialog = new SpotsDialog(context);
        pDialog.setCancelable(false);

        coordinatorLayout  =(CoordinatorLayout)findViewById(R.id.coordinateLayout);


        sessionmanager = new SessionManager(context);
        userDetails = sessionmanager.getSessionDetails();



        progress_dialog = new SpotsDialog(context);
        progress_dialog.setCancelable(true);










        edtMobileWrapper = (TextInputLayout) findViewById(R.id.edtMobileWrapper);

        edtMobile = (EditText) findViewById(R.id.edtMobile);



              //  userNameEditText.setText("zealtech9teen@gmail.com");
       // passwordEditText.setText("zeal1234");



        btnsignin = (Button) findViewById(R.id.btnsignin);



        errorTextView = (TextView) findViewById(R.id.errorTextView);
        errorTextView.setVisibility(View.GONE);









    /*    custom_signup_button = (TextView) findViewById(R.id.custom_signup_button);

        custom_signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();

            }
        });*/


        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LOGIN_TYPE="normal";

                errorTextView.setVisibility(View.GONE);

                boolean IsError = false;

                if (edtMobile.getText().toString().equals(""))
                {
                    IsError = true;
                    edtMobileWrapper.setErrorEnabled(true);
                    edtMobileWrapper.setError("Enter Mobile");
                } else {

                    if(edtMobile.getText().toString().length() == 10)
                    {
                        IsError = false;

                    }
                    else
                    {
                        IsError = true;
                        edtMobileWrapper.setErrorEnabled(true);
                        edtMobileWrapper.setError("Invalid Mobile");

                    }




                }


               /* if (edtEmail.getText().toString().equals("")) {
                    IsError = true;
                    usernameWrapper.setErrorEnabled(true);
                    usernameWrapper.setError("Enter Usermobile Or Email");
                } else {
                    usernameWrapper.setErrorEnabled(false);

                }*/

                /*if (edtPassword.getText().toString().equals("")) {
                    IsError = true;
                    passwordWrapper.setErrorEnabled(true);
                    passwordWrapper.setError("Enter Password");
                } else {
                    passwordWrapper.setErrorEnabled(false);

                }*/
                if (IsError == false) {


                    getLoginDetailsFromServer();

                }

            }
        });





     //   gPlusSignOut();
      //  gPlusRevokeAccess();

    }


    private void getLoginDetailsFromServer() {


        showDialog();


        String url_sendSignUpDetails = AllKeys.WEBSITE + "login?type=login&mobile="+ edtMobile.getText().toString() +"&deviceid="+ Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID) +"&devicetype=and";

        Log.d(TAG, "URL userLogin : " + url_sendSignUpDetails);



        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url_sendSignUpDetails, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "  CustomerLogin Response : " + response);


                try
                {

                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);

                    if (error_status == false) {
                        if (record_status == true) {
                            JSONArray arr = response.getJSONArray(AllKeys.ARRAY_DATA);
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject c = arr.getJSONObject(i);


                                String Userid = c.getString(AllKeys.TAG_USERID);
                                String Name = c.getString(AllKeys.TAG_NAME);
                                String Email = c.getString(AllKeys.TAG_EMAIL);
                                String Mobile = c.getString(AllKeys.TAG_MOBILENO);
                                String Address = c.getString(AllKeys.TAG_ADDRESS);
                                String isNewUSer = c.getString(AllKeys.TAG_IS_NEW_USER);
                                String ReferralCode=  c.getString(AllKeys.TAG_REFERRAL_CODE);







                                //setUserDetails(String emp_id, String emp_name, String emp_email, String emp_mobile, String emp_type) {
                                sessionmanager.setUserDetails(Userid,Name,Email,Mobile,Address,isNewUSer,ReferralCode,"1");









                                Answers.getInstance().logLogin(new LoginEvent()
                                        .putMethod("User Login")
                                        .putCustomAttribute("UserName : " , Name)
                                        .putCustomAttribute("UserMobile : " , Mobile)
                                        .putCustomAttribute("UserId : " , Userid)
                                        .putCustomAttribute("UserEmail : " , Email)

                                        .putSuccess(true));




                                hideDialog();



                                Intent ii = new Intent(context, VerificationActivity.class);
                                startActivity(ii);
                                finish();



                            }


                        } else {
                            hideDialog();
                            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        hideDialog();
                        Toast.makeText(context, "" + str_error, Toast.LENGTH_SHORT).show();
                        Snackbar.make(coordinatorLayout, str_error, Snackbar.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    hideDialog();
                    Snackbar.make(coordinatorLayout,"Sorry, try again.. ",Snackbar.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof ServerError || error instanceof NetworkError) {

                    hideDialog();
                } else {
                    getLoginDetailsFromServer();
                }

            }
        });
        MyApplication.getInstance().addToRequestQueue(request);

    }



    public void showDialog() {

        if (!pDialog.isShowing()) {

            pDialog.show();
        }


    }

    public void hideDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();

        }
    }









    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = null;
        try {
            i = new Intent(context, SplashScreen.class);
            //i.putExtra("ActivityName",ACTIVITYNAME);
            startActivity(i);
            finish();
        } catch (Exception e) {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Error in Converting Class : " + e.getMessage());
            e.printStackTrace();
        }


    }

}
