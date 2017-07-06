package com.safeconnectionsapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.safeconnectionsapp.Model.UNotificationResponse;
import com.safeconnectionsapp.app.MyApplication;
import com.safeconnectionsapp.fragments.FragmentComplaints;
import com.safeconnectionsapp.fragments.FragmentProfile;
import com.safeconnectionsapp.fragments.FragmentReferalList;
import com.safeconnectionsapp.fragments.HomeFragment;
import com.safeconnectionsapp.fragments.InviteFriendsFragment;
import com.safeconnectionsapp.helper.AllKeys;
import com.safeconnectionsapp.helper.CustomRequest;
import com.safeconnectionsapp.rest.APIClient;
import com.safeconnectionsapp.rest.ApiInterface;
import com.safeconnectionsapp.session.SessionManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;

public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = DashBoardActivity.class.getSimpleName();
    private Context context = this;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private SpotsDialog pDialog;
    private TextView tvUserName;
    private TextView tvEmail;
    private ImageView imgProfilePic;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        setTitle("Dashboard");

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();

        pDialog = new SpotsDialog(context);
        pDialog.setCancelable(false);


        if (userDetails.get(SessionManager.KEY_USER_MOBILE).length() == 10 && userDetails.get(SessionManager.KEY_VERSTATUS).equals("0")) {
            Intent intent = new Intent(context, VerificationActivity.class);
            startActivity(intent);
            finish();

        } else if (userDetails.get(SessionManager.KEY_USER_ID).equals("0")) {

            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {

            //  UpdateFcmTokenDetailsToServer();


            ApiInterface apiService =
                    APIClient.getClient().create(ApiInterface.class);


            showDialog();

            MyFirebaseInstanceIDService mid = new MyFirebaseInstanceIDService();

            Call<UNotificationResponse> call = apiService.getTopRatedMovies("fcmtoken", mid.onTokenRefreshNew(context), userDetails.get(SessionManager.KEY_USER_ID));
            call.enqueue(new Callback<UNotificationResponse>() {
                @Override
                public void onResponse(Call<UNotificationResponse> call, retrofit2.Response<UNotificationResponse> response) {

                    try {
                        int statusCode = response.code();
                        // Toast.makeText(DashBoardActivity.this, "Status Code : "+statusCode, Toast.LENGTH_SHORT).show();

                        Log.d(TAG, "Resposne  FCM : " + response.body().getMESSAGE().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        hideDialog();
                    }
                    hideDialog();


                }

                @Override
                public void onFailure(Call<UNotificationResponse> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(TAG, "FCM Failed : " + t.toString());
                    hideDialog();
                }
            });


            //Commit statement
            //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();

            /*Intent intent = new Intent(context , InviteCodeActivity.class);
            startActivity(intent);
            finish();*/


        }


        try {
            Log.d(TAG, "DEVICE ID : " + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setVisibility(View.VISIBLE);


        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_dash_board);

        tvUserName = (TextView) headerLayout.findViewById(R.id.tvName);
        tvEmail = (TextView) headerLayout.findViewById(R.id.tvEmail);
        imgProfilePic = (ImageView) headerLayout.findViewById(R.id.imgProfilePic);


        tvUserName.setText(userDetails.get(SessionManager.KEY_USER_NAME));
        tvEmail.setText(userDetails.get(SessionManager.KEY_USER_EMAIL));

        //SetUserProfilePictireFromBase64EnodedString();

        imgProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    /*Fragment fragment = new FragmentProfile();

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.commit();


                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);*/


                getMenuInflater().inflate(R.menu.activity_dash_board_drawer, menu);
                MenuItem mProfileFrag = menu.findItem(R.id.nav_profile);

                onNavigationItemSelected(mProfileFrag);


                    /*MenuItem mDefaultFrag = (MenuItem) navigationView.findViewById(R.id.nav_profile);
                    onNavigationItemSelected(mDefaultFrag);*/


            }
        });



        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/

        setupFragment(null, getString(R.string.app_name));
    }

    public void setupFragment(Fragment fragment, String title) {
        setTitle(title);

        if (fragment != null) {


            FragmentManager fragmentManager = getSupportFragmentManager();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();


        } else {

            FragmentManager fragmentManager = getSupportFragmentManager();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragment = new HomeFragment();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
        }


    }

    private void SetUserProfilePictireFromBase64EnodedString() {
        showDialog();
        try {
            userDetails = sessionManager.getSessionDetails();
            String myBase64Image = userDetails.get(SessionManager.KEY_ENODEDED_STRING);
            if (!myBase64Image.equals("")) {

                Bitmap myBitmapAgain = AllKeys.decodeBase64(myBase64Image);

                imgProfilePic.setImageBitmap(myBitmapAgain);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Decode Img Exception : ", e.getMessage());
            hideDialog();
        }
        hideDialog();
    }


    private void UpdateFcmTokenDetailsToServer() {
        showDialog();


        String url = AllKeys.WEBSITE + "InsertFCMToken";

        Log.d(TAG, "URL  InsertFCMToken : " + url);


        CustomRequest request = new CustomRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;


        // Inflate the menu; this adds items to the action bar if it is present.
        /*getMenuInflater().inflate(R.menu.dash_board, menu);

        MenuItem menuitem = (MenuItem)menu.findItem(R.id.action_settings);
        menuitem.setVisible(false);
*/


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        SetUserProfilePictireFromBase64EnodedString();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            setupFragment(new HomeFragment(), getString(R.string.app_name));

        } else if (id == R.id.nav_products) {


            Intent intent = new Intent(context, CategoryAcitivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(context, "Category", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_complaints) {

            setupFragment(new FragmentComplaints(), "Complaints");

        } else if (id == R.id.nav_quotation) {

        } else if (id == R.id.nav_profile) {
            setupFragment(new FragmentProfile(), "Profile");

        } else if (id == R.id.nav_refer_a_friend) {
            setupFragment(new InviteFriendsFragment(), "Refer a friend");

        } else if (id == R.id.nav_referal_list) {
            setupFragment(new FragmentReferalList(), "Referral List");

        } else if (id == R.id.nav_logout) {

            sessionManager.logoutUser();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
