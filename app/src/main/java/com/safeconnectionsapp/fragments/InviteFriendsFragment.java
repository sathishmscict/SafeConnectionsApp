package com.safeconnectionsapp.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.safeconnectionsapp.DashBoardActivity;
import com.safeconnectionsapp.R;
import com.safeconnectionsapp.app.MyApplication;
import com.safeconnectionsapp.helper.AllKeys;
import com.safeconnectionsapp.helper.NetConnectivity;

import com.safeconnectionsapp.session.SessionManager;
import com.google.android.gms.plus.PlusShare;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class InviteFriendsFragment extends Fragment {

    private TextView txtupto;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails;
    private ImageButton btnwhatsapp;
    private ImageButton btnsms;
    private ImageButton btnfacebook;
    private ImageButton btntwitter;
    private ImageButton btnmail;
    private ImageButton btngmail;
    //private TextView txtitle;
    private Context context = getActivity();

    private String MESSAGE = "";

    private int STATUS = 0;
    private int ERRORCODE = 0;
    private TextView txtrefcpde;

    private static final String TAG = InviteFriendsFragment.class
            .getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;
    String contactNumber = null;// contacts unique ID
    String contactName = null;
    private String MOBILENO = "";
    private String cNumber;
    private String name;
    private String recharge = "";

    private String referalcode;

    private String FBMSG;
    private String WHATSAPPMSG;
    private String GOOGLEPLUSMSG;
    private String GMAILMSG;
    private String TWITTERMSG;


    public static final String SHAREIMAGE = "index.jpeg";
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private SpotsDialog pDialog;


    public InviteFriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_invitefriends, container,
                false);


        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getActivity(), Manifest.permission.READ_CONTACTS)) {

                    android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Read Contacts permisson is needed to send invite message");
                    alertBuilder.setPositiveButton(android.R.string.yes, new OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                        }
                    });
                    android.support.v7.app.AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                }
                //return false;
            } else {
                //return true;
            }
        }


        pDialog = new SpotsDialog(getActivity());
        pDialog.setCancelable(false);




		/* Control Declaration */
        btnwhatsapp = (ImageButton) rootView.findViewById(R.id.imgwhatsapp);
        btnsms = (ImageButton) rootView.findViewById(R.id.imgsms);
        btnfacebook = (ImageButton) rootView.findViewById(R.id.imgfb);
        btngmail = (ImageButton) rootView.findViewById(R.id.imggmail);
        btnmail = (ImageButton) rootView.findViewById(R.id.immail);
        btntwitter = (ImageButton) rootView.findViewById(R.id.imgtwitter);
        //txtitle = (TextView)rootView.findViewById(R.id.textView3);
        txtrefcpde = (TextView) rootView.findViewById(R.id.txtrefcode);


        txtupto = (TextView) rootView.findViewById(R.id.txtupto);


        btnsms.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub


                Dexter.withActivity(getActivity()).withPermission(Manifest.permission.READ_CONTACTS).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        //onClickSelectContact(v);

                        try {
                            STATUS = 2;
                            Intent intent = new Intent(Intent.ACTION_PICK,
                                    ContactsContract.Contacts.CONTENT_URI);
                            startActivityForResult(intent, 0);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                        Toast.makeText(getActivity(),    "Please grant read contact permission", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();




            }
        });


        sessionManager = new SessionManager(getActivity());

        userDetails = new HashMap<String, String>();

        userDetails = sessionManager.getSessionDetails();

        referalcode = userDetails.get(SessionManager.KEY_REFERAL_CODE);

        final String UID = userDetails.get(SessionManager.KEY_USER_ID);

        final String referalcode = userDetails
                .get(SessionManager.KEY_REFERAL_CODE);

        txtrefcpde.setText("Your Invite Code : " + referalcode);


        // MESSAGE =
        // "You are invited to join PayMyReview. Join today and get rewards! Use this referral code - "
        // + referalcode;

        Intent ii = getActivity().getIntent();

        //final String actname = userDetails.get(SessionManager.KEY_NEWUSER);

        // getSupportActionBar().hide();

        String bal = "<b>abcd</b>";

        // txtupto.setText("recharge upto \u20B9 <b>" +
        // userDetails.get(SessionManager.KEY_REFERAL_LIMIT)+" ");


        txtupto.setText(Html.fromHtml("You will earn <b>Rs "
                + userDetails.get(SessionManager.KEY_REFERAL_POINTS)
                + "</b> with every sucessful referral. You can redeem your reward money through mobile recharge. Maximum Limit is <b>Rs " + userDetails.get(SessionManager.KEY_REFERAL_LIMIT) + "</b>."));

        SpannableString content = new SpannableString("SKIP");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);


		/* Button Click Operations */

        btnwhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                STATUS = 1;

                new SendInviteToFriends().execute();

            }
        });

        btnfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                STATUS = 3;
                if (NetConnectivity.isOnline(getActivity())) {
                    new SendInviteToFriends().execute();
                } else {
                    Toast.makeText(getActivity(),
                            "Please enable Data Connection", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });

        btngmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                STATUS = 4;
                if (NetConnectivity.isOnline(getActivity())) {
                    new SendInviteToFriends().execute();
                } else {
                    Toast.makeText(getActivity(),
                            "Please enable Data Connection", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });

        btnmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                STATUS = 5;
                if (NetConnectivity.isOnline(getActivity())) {
                    new SendInviteToFriends().execute();
                } else {
                    Toast.makeText(getActivity(),
                            "Please enable Data Connection", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        btntwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                STATUS = 6;
                if (NetConnectivity.isOnline(getActivity())) {
                    new SendInviteToFriends().execute();
                } else {
                    Toast.makeText(getActivity(),
                            "Please enable Data Connection", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        getAllInviteMessagesFromServer();
        getReferalRewardsSettingsFromServer();

        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /* Retrive Contact Mobile No : */
    public void onClickSelectContact(View btnSelectContact) {

        // using native contacts selection
        // Intent.ACTION_PICK = Pick an item from the data, returning what was
        // selected.
        try {
            STATUS = 2;
            Intent intent = new Intent(Intent.ACTION_PICK,
                    ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        try {
            if (STATUS == 2) {
                if (0 == reqCode) {
                    if (resultCode == Activity.RESULT_OK) {
                        System.out.println("in on ActivityResult");
                        Uri contactData = data.getData();
                        Cursor c = getActivity().managedQuery(contactData, null, null, null,
                                null);
                        if (c.moveToFirst()) {
                            String id = c
                                    .getString(c
                                            .getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                            String hasPhone = c
                                    .getString(c
                                            .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                            if (hasPhone.equalsIgnoreCase("1")) {
                                Cursor phones = getActivity().getContentResolver()
                                        .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                                null,
                                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                        + " = " + id, null,
                                                null);
                                phones.moveToFirst();
                                cNumber = phones.getString(phones
                                        .getColumnIndex("data1"));
                                name = phones
                                        .getString(phones
                                                .getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                                // here you can find out all the thing.
                                System.out.println("NAME:" + name);

                                MOBILENO = cNumber;
                                //Toast.makeText(context, "Length  : "+MOBILENO.length(),1000).show();

                                if (NetConnectivity
                                        .isOnline(getActivity())) {
                                    //new SendInviteToFriends().execute();

                                    SendInviteSMSToOther();
                                } else {
                                    Toast.makeText(getActivity(),
                                            "Please enable Data Connection",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                }

            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }
    private void getReferalRewardsSettingsFromServer()
    {
        showDialog();
        String url = AllKeys.WEBSITE + "GetRewardsData?ACTION=rewards";
        Log.d(TAG, "URL GetRewardsData  : " + url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                Log.d(TAG, " GetRewardsData :" + response);

                try {

                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);

                    if (error_status == false) {
                        if (record_status == true) {


                            JSONArray arr = response.getJSONArray(AllKeys.ARRAY_REWARDS);
                            for (int i = 0; i < arr.length(); i++)
                            {

                                JSONObject c = arr.getJSONObject(i);


                                String defaultPoints = c.getString(AllKeys.TAG_RR_DEFAULT_POINTS);
                                String referralpoints = c.getString(AllKeys.TAG_RR_REFERAAL_POINTS);
                                String referralcapping = c.getString(AllKeys.TAG_RR_REFERRAL_CAPPING);
                                String payableAmount = c.getString(AllKeys.TAG_RR_PAYABLE_AMOUNT);
                                String referralLimit = c.getString(AllKeys.TAG_RR_REFERRAL_LIMIT);
                              String key = c.getString(AllKeys.TAG_RR_KEY);







                                sessionManager.createPayments(defaultPoints, referralpoints,
                                        referralcapping, referralLimit, payableAmount, key);

                            }
                            hideDialog();
                            userDetails = sessionManager.getSessionDetails();

                            txtupto.setText(Html.fromHtml("You will earn <b>Rs "
                                    + userDetails.get(SessionManager.KEY_REFERAL_POINTS)
                                    + "</b> with every sucessful referral. You can redeem your reward money through mobile recharge. Maximum Limit is <b>Rs " + userDetails.get(SessionManager.KEY_REFERAL_LIMIT) + "</b>."));



                        } else {
                            hideDialog();
                            Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        hideDialog();
                        Toast.makeText(getActivity(), "" + str_error, Toast.LENGTH_SHORT).show();
                        //   Snackbar.make(coordinatorLayout, str_error, Snackbar.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    hideDialog();

                    e.printStackTrace();
                }

                hideDialog();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ServerError || error instanceof NetworkError) {

                    hideDialog();
                } else {
                    getReferalRewardsSettingsFromServer();
                }

            }
        });
        MyApplication.getInstance().addToRequestQueue(request);

    }
    private void SendInviteSMSToOther() {
        showDialog();
        String url = AllKeys.WEBSITE+"GetSendSMS?ACTION=sendsms&CUSTOMER_ID="+ userDetails.get(SessionManager.KEY_USER_ID) +"&MOBILE="+ MOBILENO +"";
        Log.d(TAG, "URL GetSendSMS : "+url);

        JsonObjectRequest request =new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);


                    if (error_status == false) {


                            AlertDialog.Builder alert = new AlertDialog.Builder(
                                    getActivity());
                            alert.setTitle(getString(R.string.app_name));
                            alert.setIcon(R.mipmap.ic_launcher);
                            alert.setMessage("Invalid Mobile No");

                            alert.setNeutralButton("OK",
                                    new OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {

                                            // TODO Auto-generated method stub


                                            dialog.cancel();
                                            dialog.dismiss();
                                            //getActivity().finish();


                                        }

                                    });
                            alert.show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    hideDialog();
                }

                hideDialog();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(error instanceof ServerError || error instanceof NetworkError)
                {

                    hideDialog();
                }
                else
                {
                    SendInviteSMSToOther();
                }
            }
        });
        MyApplication.getInstance().addToRequestQueue(request);


    }

	/* Complete Retrive Mobile No */
    /*
     * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.friends, menu); return true; }
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { // Handle
	 * action bar item clicks here. The action bar will // automatically handle
	 * clicks on the Home/Up button, so long // as you specify a parent activity
	 * in AndroidManifest.xml. int id = item.getItemId();
	 * 
	 * //noinspection SimplifiableIfStatement if (id == R.id.action_settings) {
	 * return true; }
	 * 
	 * return super.onOptionsItemSelected(item); }
	 */

    private class SendInviteToFriends extends AsyncTask<Void, Void, Void> {

        private String mm = "";
        Uri theUri;
        String response;
        private ProgressDialog pDialog2;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog2 = new ProgressDialog(getActivity());
            pDialog2.setMessage("Please wait...");
            pDialog2.setCancelable(false);
            pDialog2.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // /Write here statements
            //ServiceHandler sh = new ServiceHandler();
            theUri = Uri.parse("content://" + getActivity().getPackageName() + "/" + SHAREIMAGE + "");

            if (STATUS == 1) {

                PackageManager pm = getActivity().getPackageManager();
                try {

                    Intent waIntent = new Intent(Intent.ACTION_SEND);

                    PackageInfo info = pm.getPackageInfo("com.whatsapp",
                            PackageManager.GET_META_DATA);
                    // Check if package exists or not. If not then code
                    // in catch block will be called
                    //	waIntent.setType("image/*");
                    //waIntent.putExtra(Intent.EXTRA_STREAM, theUri);

                    //WHATSAPPMSG="Download Cleanzone App and get free 10 Rs. instant balance...";
                    waIntent.putExtra(Intent.EXTRA_TEXT, WHATSAPPMSG);
                    waIntent.setType("text/plain");
                    waIntent.setPackage("com.whatsapp");

                    // startActivity(Intent.createChooser(waIntent,
                    // "Share with"));
                    startActivity(waIntent);
                } catch (PackageManager.NameNotFoundException e) {
                    ERRORCODE = 1;
                }

            } else if (STATUS == 2) {

                try {

                    if (MOBILENO.length() >= 10) {
                        MOBILENO = MOBILENO.replace(" ", "");
                        MOBILENO = MOBILENO.replace("+", "");
                        if (MOBILENO.length() != 10) {
                            MOBILENO = MOBILENO.substring(
                                    MOBILENO.length() - 10, MOBILENO.length());
                            String data = "" + MOBILENO;
                            System.out.print("Mobile No : " + data);
                        }



                        //CreateCustomMethodForSendingSMSToNEwUSER
                        //String SURL = AllKeys.TAG_JSONDATA+ "?action=sendsms";
                        String SURL = AllKeys.WEBSITE + "GetSendSMS?ACTION=sendsms&CUSTOMER_ID="
                                + userDetails.get(SessionManager.KEY_USER_ID)
                                + "&MOBILE=" + MOBILENO + "";

                        Log.d(TAG, "URL GetSendSMS : " + SURL);


//{"MESSAGE":"SMS sent successfully","ORIGINAL_ERROR":"63460841","ERROR_STATUS":false,"RECORDS":false}


                        //response = sh.makeServiceCall(SURL, ServiceHandler.GET);

                        JSONObject obj = new JSONObject(response);

                        mm = obj.getString(AllKeys.TAG_MESSAGE);

                        //messageCounter = "";

					/*	if (response != null) {

							JSONObject jsonObj = new JSONObject(response);
							JSONArray jj = jsonObj.getJSONArray("smsmessage");
							// looping through All Contacts
							for (int i = 0; i < jj.length(); i++) {
								JSONObject c = jj.getJSONObject(i);

								mm = c.getString("message");

							}

						}*/

                    } else {
                        ERRORCODE = 2;
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }

            } else if (STATUS == 3) {

                try {
                    String urlToShare = "http://safeconnexions.com";
                    // + "//getjson.php?action=invitemsg&userid=21";
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    // intent.putExtra(Intent.EXTRA_SUBJECT, "Foo bar"); // NB:
                    // has no effect!
                    intent.putExtra(Intent.EXTRA_TEXT, FBMSG);

                    // See if official Facebook app is found
                    boolean facebookAppFound = false;
					/*List<ResolveInfo> matches = getPackageManager()
							.queryIntentActivities(intent, 0);
					for (ResolveInfo info : matches) {
						if (info.activityInfo.packageName.toLowerCase()
								.startsWith("com.facebook.katana")) {
							intent.setPackage(info.activityInfo.packageName);
							//facebookAppFound = true;
							startActivity(intent);
							break;
						}
					}*/

                    // As fallback, launch sharer.php in a browser
                    if (facebookAppFound == false) {


                        //http://paymyreview.in/getjson.aspx?action=facebookmsg&userid=23


                        //String uu = AllKeys.TAG_FACEBOOK_MESSAGE_WEBSITE+"?action=facebookmsg&userid="+userDetails.get(SessionManager.KEY_UID);
                        String uu = AllKeys.WEBSITE + "/GetFacebookMessage?action=facebookmsg&userid=" + userDetails.get(SessionManager.KEY_USER_ID);
                        String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u="
                                + urlToShare;

                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));


                        startActivity(intent);


                        // ERRORCODE = 3;
                    } else {
                        // startActivity(intent);
                    }

                } catch (Exception e) {
                    ERRORCODE = 3;
                    e.printStackTrace();
                }

            } else if (STATUS == 4) {

                // Launch the Google+ share dialog with attribution to your app.
                try {
                    // shareIntent.setType("image/*");
                    // shareIntent.putExtra(Intent.EXTRA_STREAM,theUri);
                    //File image = new File(theUri.toString());
                    Intent shareIntent = new PlusShare.Builder(
                            getActivity()).setType("text/plain")
                            .setText("" + GOOGLEPLUSMSG)

                            .getIntent();

                    startActivityForResult(shareIntent, 0);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else if (STATUS == 5) {

                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);

                    // File image = new File(theUri.toString());

                    File image = new File(Uri.parse(
                            "android.resource://" + getActivity().getPackageName()
                                    + "/drawable/" + R.drawable.app_logo)
                            .toString());
                    // sharingIntent.putExtra(Intent.EXTRA_STREAM,
                    // Uri.fromFile(image));

                    shareIntent.setType("image/*");

                    shareIntent.putExtra(Intent.EXTRA_SUBJECT,
                            "Join " + getString(R.string.app_name) + " App & Earn upto Rs 500/month");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "" + GMAILMSG);
                    shareIntent
                            .putExtra(
                                    Intent.EXTRA_STREAM,
                                    Uri.parse(
                                            "android.resource://"
                                                    + getActivity().getPackageName()
                                                    + "/drawable/"
                                                    + R.drawable.app_logo)
                                            .toString());

                    // shareIntent.putExtra(Intent.EXTRA_STREAM,
                    // Uri.parse("content://"+getPackageName()+"/"+SHAREIMAGE+""));
                    // shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

                    PackageManager pm = getActivity().getPackageManager();
                    List<ResolveInfo> activityList = pm.queryIntentActivities(
                            shareIntent, 0);

                    for (final ResolveInfo app : activityList) {
                        if ((app.activityInfo.name).contains("android.gm")) {
                            final ActivityInfo activity = app.activityInfo;
                            final ComponentName name = new ComponentName(
                                    activity.applicationInfo.packageName,
                                    activity.name);
                            shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                            shareIntent
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                            | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                            shareIntent.setComponent(name);
                            startActivity(shareIntent);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();// Uri.parse("file://"+fileName));
                }

            } else if (STATUS == 6) {

                Intent tweetIntent = new Intent(Intent.ACTION_SEND);
                tweetIntent.putExtra(Intent.EXTRA_TEXT, "" + TWITTERMSG);
                tweetIntent.setType("image/*");
                //tweetIntent.putExtra(Intent.EXTRA_STREAM, theUri);

                PackageManager packManager = getActivity().getPackageManager();
                List<ResolveInfo> resolvedInfoList = packManager
                        .queryIntentActivities(tweetIntent,
                                PackageManager.MATCH_DEFAULT_ONLY);

                boolean resolved = false;
                for (ResolveInfo resolveInfo : resolvedInfoList) {
                    if (resolveInfo.activityInfo.packageName
                            .startsWith("com.twitter.android")) {
                        tweetIntent.setClassName(
                                resolveInfo.activityInfo.packageName,
                                resolveInfo.activityInfo.name);
                        resolved = true;
                        break;
                    }
                }
                if (resolved) {
                    startActivity(tweetIntent);


                } else {
                    Intent i = new Intent();
                    i.putExtra(Intent.EXTRA_TEXT, "" + TWITTERMSG);
                    i.setType("image/*");
                    //	i.putExtra(Intent.EXTRA_STREAM, theUri);
                    i.setAction(Intent.ACTION_VIEW);
                    i.setData(Uri
                            .parse("https://twitter.com/intent/tweet?text="
                                    + TWITTERMSG + "&via=profileName"));
                    startActivity(i);

                }

            } else {

            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            try {
                //getFragmentManager().executePendingTransactions();
				
			/*	if(STATUS != 2)
				{*/

                if (pDialog2.isShowing()) {

                    pDialog2.dismiss();
                    pDialog2.cancel();
                }

			/*	}
*/


                // Write statement after background process execution

                if (STATUS == 2 && ERRORCODE != 2) {
                    ERRORCODE = 0;

                    //AlertDialogManager2.showAlertDialog(getActivity(), "PayMyReview", ""+ mm, false);
                    AlertDialog.Builder alert = new AlertDialog.Builder(
                            getActivity());
                    alert.setTitle(getString(R.string.app_name));
                    alert.setIcon(R.mipmap.ic_launcher);
                    alert.setMessage("" + mm);

                    alert.setNeutralButton("OK",
                            new OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    dialog.cancel();
                                    dialog.dismiss();
                                    // TODO Auto-generated method stub

                                    //Intent ii = new Intent(getActivity(),DashBoardActivity.class);

                                    //startActivity(ii);
                                    //getActivity().finish();


                                }

                            });
                    alert.show();

                }
                if (ERRORCODE == 2) {
                    ERRORCODE = 0;

                    //AlertDialogManager2.showAlertDialog(getActivity(), "PayMyReview", "Invalid Mobile No", false);
                    AlertDialog.Builder alert = new AlertDialog.Builder(
                            getActivity());
                    alert.setTitle("PayMyReview");
                    alert.setIcon(R.mipmap.ic_launcher);
                    alert.setMessage("Invalid Mobile No");

                    alert.setNeutralButton("OK",
                            new OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    // TODO Auto-generated method stub

                                    Intent ii = new Intent(getActivity(),
                                            DashBoardActivity.class);
                                    startActivity(ii);
                                    //getActivity().finish();


                                }

                            });
                    alert.show();


                }
                if (ERRORCODE == 1) {
                    ERRORCODE = 0;
                    //AlertDialogManager2.showAlertDialog(getActivity(), "PayMyReview", "Whats App Not Installed", false);
                    Toast.makeText(getActivity(), "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                } else if (ERRORCODE == 6) {
                    ERRORCODE = 0;
                    Toast.makeText(getActivity(), "Twitter app isn't found", Toast.LENGTH_LONG).show();
                } else if (ERRORCODE == 3) {
                    ERRORCODE = 0;
                    Toast.makeText(getActivity(), "Facebook app isn't found", Toast.LENGTH_LONG).show();
                }


                if (!recharge.equals("recharge")) {
                    //txtitle.setText("INVITE MORE FRIENDS");
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                String ee = e.getMessage();
                e.printStackTrace();


            }


        }


    }

    public void shareIt(View view) {
        try {
            // sharing implementation
            List<Intent> targetedShareIntents = new ArrayList<Intent>();
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            // String shareBody = "string of text " + txt_var +
            // " more text! Get the app at http://someapp.com";

            PackageManager pm = view.getContext().getPackageManager();
            List<ResolveInfo> activityList = pm.queryIntentActivities(
                    sharingIntent, 0);
            for (final ResolveInfo app : activityList) {

                String packageName = app.activityInfo.packageName;
                Intent targetedShareIntent = new Intent(Intent.ACTION_SEND);
                targetedShareIntent.setType("text/plain");
                targetedShareIntent.putExtra(Intent.EXTRA_SUBJECT, "" + FBMSG);
                if (TextUtils.equals(packageName, "com.facebook.katana")) {
                    targetedShareIntent.putExtra(Intent.EXTRA_TEXT, "" + FBMSG);
                } else {
                    targetedShareIntent.putExtra(Intent.EXTRA_TEXT, "" + FBMSG);
                }

                targetedShareIntent.setPackage(packageName);
                targetedShareIntents.add(targetedShareIntent);

            }

            Intent chooserIntent = Intent.createChooser(
                    targetedShareIntents.remove(0), "Share Idea");

            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    targetedShareIntents.toArray(new Parcelable[]{}));
            startActivity(chooserIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }

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


	/* Get User Invite Message */

    private void getAllInviteMessagesFromServer() {

        showDialog();

        String url = AllKeys.WEBSITE + "GetMessages?ACTION=msg";
        Log.d(TAG, "URL GetAllMessages : " + url);
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "Response GetAllMessages "+response);



                //Here SetAllMessageIntoSession



                    try {
                        String str_error = response.getString(AllKeys.TAG_MESSAGE);
                        String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                        boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                        boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);


                        if (error_status == false)
                        {
                            if (record_status == true)
                            {

                                JSONArray arr_messages = response.getJSONArray(AllKeys.ARRAY_DATA);
                                for (int i = 0; i < arr_messages.length(); i++)
                                {
                                    JSONObject c = arr_messages.getJSONObject(i);


                                    FBMSG = "" + c.getString(AllKeys.TAG_FACEBOOK_MSG);
                                    System.out.print("FBMSG : " + FBMSG);
                                    if (FBMSG.contains("#Username#")
                                            || FBMSG.contains("#REFCODE#")) {
                                        FBMSG = FBMSG
                                                .replace(
                                                        "#Username#",
                                                        ""
                                                                + userDetails
                                                                .get(SessionManager.KEY_USER_NAME));
                                        FBMSG = FBMSG
                                                .replace(
                                                        "#REFCODE#",
                                                        ""
                                                                + userDetails
                                                                .get(SessionManager.KEY_REFERAL_CODE));

                                        String asd = FBMSG;
                                        System.out.print("FBMSG : " + FBMSG);

                                    }

                                    WHATSAPPMSG = ""
                                            + c.getString(AllKeys.TAG_WHATSAPP_MSG);
                                    if (WHATSAPPMSG.contains("#Username#")
                                            || WHATSAPPMSG.contains("#REFCODE#")) {
                                        WHATSAPPMSG = WHATSAPPMSG
                                                .replace(
                                                        "#Username#",
                                                        ""
                                                                + userDetails
                                                                .get(SessionManager.KEY_USER_NAME));
                                        WHATSAPPMSG = WHATSAPPMSG
                                                .replace(
                                                        "#REFCODE#",
                                                        ""
                                                                + userDetails
                                                                .get(SessionManager.KEY_REFERAL_CODE));

                                    }

                                    GOOGLEPLUSMSG = ""
                                            + c.getString(AllKeys.TAG_GOOGLE_PLUS_MSG);
                                    if (GOOGLEPLUSMSG.contains("#Username#")
                                            || GOOGLEPLUSMSG.contains("#REFCODE#")) {
                                        GOOGLEPLUSMSG = GOOGLEPLUSMSG
                                                .replace(
                                                        "#Username#",
                                                        ""
                                                                + userDetails
                                                                .get(SessionManager.KEY_USER_NAME));
                                        GOOGLEPLUSMSG = GOOGLEPLUSMSG
                                                .replace(
                                                        "#REFCODE#",
                                                        ""
                                                                + userDetails
                                                                .get(SessionManager.KEY_REFERAL_CODE));

                                    }

                                    GMAILMSG = "" + c.getString(AllKeys.TAG_GMAIL_MSG);
                                    if (GMAILMSG.contains("#Username#")
                                            || GMAILMSG.contains("#REFCODE#")) {
                                        GMAILMSG = GMAILMSG
                                                .replace(
                                                        "#Username#",
                                                        ""
                                                                + userDetails
                                                                .get(SessionManager.KEY_USER_NAME));
                                        GMAILMSG = GMAILMSG
                                                .replace(
                                                        "#REFCODE#",
                                                        ""
                                                                + userDetails
                                                                .get(SessionManager.KEY_REFERAL_CODE));

                                    }

                                    TWITTERMSG = ""
                                            + c.getString(AllKeys.TAG_TWITTER_MSG);

                                    if (TWITTERMSG.contains("#Username#")
                                            || TWITTERMSG.contains("#REFCODE#")) {
                                        TWITTERMSG = TWITTERMSG
                                                .replace(
                                                        "#Username#",
                                                        ""
                                                                + userDetails
                                                                .get(SessionManager.KEY_USER_NAME));
                                        TWITTERMSG = TWITTERMSG
                                                .replace(
                                                        "#REFCODE#",
                                                        ""
                                                                + userDetails
                                                                .get(SessionManager.KEY_REFERAL_CODE));

                                    }

                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                sessionManager.createSocialMessage(WHATSAPPMSG, FBMSG, TWITTERMSG,
                        GOOGLEPLUSMSG, GMAILMSG);

                hideDialog();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof ServerError || error instanceof NetworkError) {

                    hideDialog();
                } else {
                    getAllInviteMessagesFromServer();

                }
            }
        });
        MyApplication.getInstance().addToRequestQueue(request);


    }


    }

