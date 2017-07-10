package com.safeconnectionsapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import com.safeconnectionsapp.adapter.MenuRecyclerViewAdapter2;
import com.safeconnectionsapp.app.MyApplication;
import com.safeconnectionsapp.helper.AllKeys;
import com.safeconnectionsapp.pojo.Menudata;
import com.safeconnectionsapp.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

import static com.safeconnectionsapp.R.id.fab;

public class MenuActivity extends AppCompatActivity {

    private Context context = this;
    private SpotsDialog pDialog;
    private TextView txtnodata;
    private RecyclerView rv_menu;
    private String TAG = MenuActivity.class.getSimpleName();

    ArrayList<Menudata> list_menu = new ArrayList<Menudata>();

    private SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private MenuRecyclerViewAdapter2 adapter;
    private int SERVICE_POSITION;
    private Spinner spnComplaints;

    ArrayList<String> list_compalintid = new ArrayList<String>();
    ArrayList<String> list_compalint = new ArrayList<String>();
    ArrayList<String> list_compalint_rate = new ArrayList<String>();
    ArrayList<String> list_compalint_type = new ArrayList<String>();


    ArrayList<String> list_quotationid = new ArrayList<String>();
    ArrayList<String> list_quotation = new ArrayList<String>();

    ArrayList<String> list_quotation_type = new ArrayList<String>();


    //getComplaintMaster Realted Keys
    public static final String TAG_COMPLAINT_ID = "complainid";
    public static final String TAG_COMPLAINT_TYPE = "complaintype";
    public static final String TAG_COMPLAINT_DESCR = "complaindescription";
    public static final String TAG_RATE = "rate";

    //Quotation Realted Keys
    public static final String TAG_QUOTATION_ID = "quotationid";
    public static final String TAG_QUOTATION_TYPE = "quotationtype";
    public static final String TAG_QUOTATION_DESCR = "quotationdescription";

    private TextView tvVisitCharge;
    private Spinner spnQuotation;
    private Button btnConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      /*  final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (userDetails.get(SessionManager.KEY_REQUEST_TYPE).toLowerCase().equals("complaint")) {

                    Intent intent = new Intent(context, AddComplaintActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                    Intent intent = new Intent(context, AddComplaintActivity.class);
                    startActivity(intent);
                    finish();

                }


            }
        });

        fab.setVisibility(View.GONE);*/


        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();

        }


        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();



        pDialog = new SpotsDialog(context);
        pDialog.setCancelable(false);


        btnConfirm = (Button)findViewById(R.id.btnConfirm);
        txtnodata = (TextView) findViewById(R.id.txtnodata);
        rv_menu = (RecyclerView) findViewById(R.id.rv_menu);



        if (userDetails.get(SessionManager.KEY_REQUEST_TYPE).toLowerCase().equals("complaint")) {
            setTitle(getString(R.string.str_complaints));
            btnConfirm.setText("Select Complaint Template");
        } else {
            btnConfirm.setText("Select Quotation Template");
            setTitle(getString(R.string.str_quotation));

        }


        btnConfirm.setVisibility(View.GONE);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (userDetails.get(SessionManager.KEY_REQUEST_TYPE).toLowerCase().equals("complaint")) {

                    Intent intent = new Intent(context, AddComplaintActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                    Intent intent = new Intent(context, AddQuotationActivity.class);
                    startActivity(intent);
                    finish();

                }

            }

        });


        LinearLayoutManager lManger = new LinearLayoutManager(context);
        lManger.setOrientation(LinearLayoutManager.VERTICAL);
        rv_menu.setLayoutManager(lManger);

        rv_menu.addOnItemTouchListener(new AllKeys.RecyclerTouchListener(context, rv_menu, new AllKeys.ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                try {

                    for (int i = 0; i < list_menu.size(); i++) {
                        list_menu.get(i).setSelectionstatus(false);
                    }
                    list_menu.get(position).setSelectionstatus(true);
                    btnConfirm.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();

                   // Toast.makeText(context, "Name : " + list_menu.get(position).getMenuname() + " Id : " + list_menu.get(position).getMenuid(), Toast.LENGTH_SHORT).show();

                    SERVICE_POSITION = position;
                    sessionManager.setCategoryTypeAndIdDetails( list_menu.get(position).getMenuid(), list_menu.get(position).getMenuname(),"multiple");


                   /* if (userDetails.get(SessionManager.KEY_REQUEST_TYPE).toLowerCase().equals("complaint")) {


                        final Dialog dialog = new Dialog(MenuActivity.this);
                        dialog.setTitle(list_menu.get(position).getMenuname() + " Complaint");
                        dialog.setContentView(R.layout.dialog_complaint);


                        spnComplaints = (Spinner) dialog.findViewById(R.id.spnComplaints);

                        getComplaintMasterDetailsFromServer(list_menu.get(position).getMenuid());


                        TextView tvClose = (TextView) dialog.findViewById(R.id.tvClose);
                        final TextView tvError = (TextView) dialog.findViewById(R.id.tvError);
                        tvError.setVisibility(View.GONE);
                        final TextInputLayout edtComplaintWrapper = (TextInputLayout) dialog.findViewById(R.id.edtComplaintWrapper);
                        final EditText edtComplaint = (EditText) dialog.findViewById(R.id.edtComplaint);
                        final EditText edtEmail = (EditText) dialog.findViewById(R.id.edtEmail);

                        final TextInputLayout edtEmailWrapper = (TextInputLayout) dialog.findViewById(R.id.edtEmailWrapper);

                        edtEmail.setText(userDetails.get(SessionManager.KEY_USER_EMAIL));

                        Button btnSendComplaint = (Button) dialog.findViewById(R.id.btnSendComplaint);

                        final TextView tvComplaintcharge = (TextView) dialog.findViewById(R.id.tvComplaintcharge);
                        tvVisitCharge = (TextView) dialog.findViewById(R.id.tvVistingCharge);


                        tvClose.setText(list_menu.get(position).getMenuname() + " Complaint");

                        spnComplaints.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                try {
                                    edtComplaint.setText(list_compalint.get(position));
                                    tvComplaintcharge.setText("Complaint Charge :" + list_compalint_rate.get(position));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    tvComplaintcharge.setText("Complaint Charge :");
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        tvClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.cancel();
                                dialog.dismiss();

                            }
                        });

                        btnSendComplaint.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (edtComplaint.getText().toString().equals("")) {
                                    edtComplaintWrapper.setErrorEnabled(true);
                                    edtComplaintWrapper.setError("Enter Complaint");

                                } else if (edtEmail.getText().toString().equals("")) {
                                    edtEmailWrapper.setErrorEnabled(true);
                                    edtEmailWrapper.setError("Enter Email");
                                    sessionManager.setUserDetails(userDetails.get(SessionManager.KEY_USER_ID), userDetails.get(SessionManager.KEY_USER_NAME), edtEmail.getText().toString(), userDetails.get(SessionManager.KEY_USER_MOBILE), userDetails.get(SessionManager.KEY_USER_ADDRESS), userDetails.get(SessionManager.KEY_IS_NEWUSER), userDetails.get(SessionManager.KEY_REFERAL_CODE), userDetails.get(SessionManager.KEY_GENDER));
                                    userDetails = sessionManager.getSessionDetails();

                                } else {
                                    edtComplaintWrapper.setErrorEnabled(false);
                                    edtEmailWrapper.setErrorEnabled(false);
                                    //showDialog();


                                    //String url = AllKeys.WEBSITE + "InsertServiceDetail?type=detail&userid=" + userDetails.get(SessionManager.KEY_USER_ID) + "&serviceid=" + list_menu.get(position).getMenuid() + "&date=" + AllKeys.convertEncodedString(AllKeys.getDateTime()) + "&description=" + AllKeys.convertEncodedString(edtComplaint.getText().toString()) + "&emailid=" + AllKeys.convertEncodedString(edtEmail.getText().toString()) + "";
                                    String url = AllKeys.WEBSITE + "InsertServiceDetail?type=detail&userid=" + userDetails.get(SessionManager.KEY_USER_ID) + "&serviceid=" + list_menu.get(position).getMenuid() + "&date=" + AllKeys.convertEncodedString(AllKeys.getDateTime()) + "&description=" + AllKeys.convertEncodedString(edtComplaint.getText().toString() + "&status=0");
                                    Log.d(TAG, "send Complaint : " + url);
                                    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.d(TAG, " Resposne InsertServiceDetail:  " + response.toString());
                                            showDialog();
                                            try {
                                                String str_error = response.getString(AllKeys.TAG_MESSAGE);
                                                String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                                                boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                                                boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);

                                                //  list_menu.clear();
                                                if (error_status == false) {
                                                    tvError.setVisibility(View.GONE);


                                                    if (record_status == true) {

                                                        dialog.cancel();

                                                        dialog.dismiss();

                                                        Toast.makeText(context, "Complaint has been submitted", Toast.LENGTH_SHORT).show();

                                                        AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                                                        builder.setTitle("Complaint Info");
                                                        builder.setMessage(list_menu.get(position).getMenuname() + " Complaint has been submited.we will contact to you with in a 24 hours.\nThank you ,for using  Safe Conncetions services.");
                                                        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                                dialog.cancel();
                                                                dialog.dismiss();

                                                                Intent intent = new Intent(context, DashBoardActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        });
                                                        builder.show();
                                                        hideDialog();

                                                    }
                                                } else {
                                                    tvError.setVisibility(View.VISIBLE);
                                                    tvError.setText(str_error);

                                                    hideDialog();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                            if (error instanceof ServerError || error instanceof NetworkError) {
                                                hideDialog();
                                            } else {
                                                hideDialog();
                                                Toast.makeText(context, "Try again..." + error.getMessage(), Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                    MyApplication.getInstance().addToRequestQueue(request);


                                }


                            }
                        });


                        dialog.getWindow().setLayout(RecyclerView.LayoutParams.FILL_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
                        dialog.show();


                    } else
                    {


                        final Dialog dialog = new Dialog(MenuActivity.this);
                        dialog.setTitle(list_menu.get(position).getMenuname() + " Quotation");
                        dialog.setContentView(R.layout.dialog_quotation);


                        spnQuotation = (Spinner) dialog.findViewById(R.id.spnQuotation);

                        // getComplaintMasterDetailsFromServer(list_menu.get(position).getMenuid());
                        getQuotationMasterDetailsFromServer(list_menu.get(position).getMenuid());


                        TextView tvClose = (TextView) dialog.findViewById(R.id.tvClose);
                        final TextView tvError = (TextView) dialog.findViewById(R.id.tvError);
                        tvError.setVisibility(View.GONE);
                        final TextInputLayout edtQuotationWrapper = (TextInputLayout) dialog.findViewById(R.id.edtQuotationWrapper);
                        final EditText edtQuotation = (EditText) dialog.findViewById(R.id.edtQuotation);
                        final EditText edtEmail = (EditText) dialog.findViewById(R.id.edtEmail);

                        final TextInputLayout edtEmailWrapper = (TextInputLayout) dialog.findViewById(R.id.edtEmailWrapper);

                        edtEmail.setText(userDetails.get(SessionManager.KEY_USER_EMAIL));

                        Button btnSendQuoatationRequest = (Button) dialog.findViewById(R.id.btnSendRequest);

                        final TextView tvComplaintcharge = (TextView) dialog.findViewById(R.id.tvComplaintcharge);
                        tvVisitCharge = (TextView) dialog.findViewById(R.id.tvVistingCharge);


                        tvClose.setText(list_menu.get(position).getMenuname() + " Quotation");

                        spnQuotation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                try {
                                    edtQuotation.setText(list_quotation.get(position));

                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        tvClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.cancel();
                                dialog.dismiss();

                            }
                        });

                        btnSendQuoatationRequest.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (edtQuotation.getText().toString().equals("")) {
                                    edtQuotationWrapper.setErrorEnabled(true);
                                    edtQuotationWrapper.setError("Enter quotation details");

                                } else if (edtEmail.getText().toString().equals("")) {
                                    edtEmailWrapper.setErrorEnabled(true);
                                    edtEmailWrapper.setError("Enter Email");
                                    sessionManager.setUserDetails(userDetails.get(SessionManager.KEY_USER_ID), userDetails.get(SessionManager.KEY_USER_NAME), edtEmail.getText().toString(), userDetails.get(SessionManager.KEY_USER_MOBILE), userDetails.get(SessionManager.KEY_USER_ADDRESS), userDetails.get(SessionManager.KEY_IS_NEWUSER), userDetails.get(SessionManager.KEY_REFERAL_CODE), userDetails.get(SessionManager.KEY_GENDER));
                                    userDetails = sessionManager.getSessionDetails();

                                } else {
                                    edtQuotationWrapper.setErrorEnabled(false);
                                    edtEmailWrapper.setErrorEnabled(false);
                                    //showDialog();


                                    //String url = AllKeys.WEBSITE + "InsertServiceDetail?type=detail&userid=" + userDetails.get(SessionManager.KEY_USER_ID) + "&serviceid=" + list_menu.get(position).getMenuid() + "&date=" + AllKeys.convertEncodedString(AllKeys.getDateTime()) + "&description=" + AllKeys.convertEncodedString(edtComplaint.getText().toString()) + "&emailid=" + AllKeys.convertEncodedString(edtEmail.getText().toString()) + "";
                                    String url = AllKeys.WEBSITE + "InsertQuotationDetail?type=quotation&userid=" + userDetails.get(SessionManager.KEY_USER_ID) + "&serviceid=" + list_menu.get(position).getMenuid() + "&date=" + AllKeys.convertEncodedString(AllKeys.getDateTime()) + "&description=" + AllKeys.convertEncodedString(edtQuotation.getText().toString()) + "&status=0";
                                    Log.d(TAG, "send Quotation InsertQuotationDetail : " + url);
                                    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.d(TAG, " Resposne InsertQuotationDetail:  " + response.toString());
                                            showDialog();
                                            try {
                                                String str_error = response.getString(AllKeys.TAG_MESSAGE);
                                                String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                                                boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                                                boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);

                                                //  list_menu.clear();
                                                if (error_status == false) {
                                                    tvError.setVisibility(View.GONE);


                                                    if (record_status == true) {

                                                        dialog.cancel();

                                                        dialog.dismiss();

                                                        Toast.makeText(context, "Request has been submitted", Toast.LENGTH_SHORT).show();

                                                        AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                                                        builder.setTitle("Quoatation Request Info");
                                                        builder.setMessage(list_menu.get(position).getMenuname() + " Quotation request has been submited.we will contact to you with in a 24 hours.\nThank you ,for using  Safe Conncetions services.");
                                                        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                                dialog.cancel();
                                                                dialog.dismiss();

                                                                Intent intent = new Intent(context, DashBoardActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        });
                                                        builder.show();
                                                        hideDialog();

                                                    }
                                                } else {
                                                    tvError.setVisibility(View.VISIBLE);
                                                    tvError.setText(str_error);

                                                    hideDialog();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                            if (error instanceof ServerError || error instanceof NetworkError) {
                                                hideDialog();
                                            } else {
                                                hideDialog();
                                                Toast.makeText(context, "Try again..." + error.getMessage(), Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                    MyApplication.getInstance().addToRequestQueue(request);


                                }


                            }
                        });


                        dialog.getWindow().setLayout(RecyclerView.LayoutParams.FILL_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
                        dialog.show();


                    }*/







                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        getAllMenuDetailsFromServer();
    }


    private void getQuotationMasterDetailsFromServer(final String serviceid)
    {
        String url = AllKeys.WEBSITE + "ViewQuotation?type=quotation&serviceid=" + serviceid + "";
        Log.d(TAG, "URL ViewQuotation : " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, " ViewQuotation Response  : " + response.toString());

                try {
                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);


                    list_quotation.clear();
                    list_quotation_type.clear();
                    list_quotationid.clear();


                    list_quotationid.add("0");
                    list_quotation.add("");
                    list_quotation_type.add("Select Quotaion Descr");


                    if (error_status == false) {

                        if (record_status == true) {


                            JSONArray arr = response.getJSONArray(AllKeys.ARRAY_DATA);
                            for (int i = 0; i < arr.length(); i++) {

                                JSONObject c = arr.getJSONObject(i);

                                //Menudata(String menuid, String menuname, String image) {


                                list_quotationid.add(c.getString(TAG_QUOTATION_ID));
                                list_quotation.add(TAG_QUOTATION_DESCR);
                                list_quotation_type.add(TAG_QUOTATION_TYPE);


                            }


                            list_quotationid.add("1000");
                            list_quotation.add("");
                            list_quotation_type.add("Other");


                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list_quotation_type);
                            spnQuotation.setAdapter(adapter);

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
                    hideDialog();
                } else {
                    getQuotationMasterDetailsFromServer(serviceid);
                }

            }
        });
        MyApplication.getInstance().addToRequestQueue(request);


    }


    private void getComplaintMasterDetailsFromServer(final String serviceid)
    {
        String url = AllKeys.WEBSITE + "ViewComplain?type=complain&serviceid=" + serviceid + "";
        Log.d(TAG, "URL ViewComplain : " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, " ViewComplain Response  : " + response.toString());

                try {
                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);


                    list_compalintid.clear();
                    list_compalint.clear();
                    list_compalint_rate.clear();
                    list_compalint_type.clear();

                    list_compalintid.add("0");
                    list_compalint.add("");
                    list_compalint_rate.add("");
                    list_compalint_type.add("Select Complaint");


                    if (error_status == false) {

                        if (record_status == true) {


                            JSONArray arr = response.getJSONArray(AllKeys.ARRAY_DATA);
                            for (int i = 0; i < arr.length(); i++) {

                                JSONObject c = arr.getJSONObject(i);

                                //Menudata(String menuid, String menuname, String image) {


                                sessionManager.setVisitingCharge(response.getString(AllKeys.TAG_RR_VISIT_CHARGE));
                                userDetails = sessionManager.getSessionDetails();

                                tvVisitCharge.setText("Visiting Charge         : " + response.getString(AllKeys.TAG_RR_VISIT_CHARGE));

                                list_compalintid.add(c.getString(TAG_COMPLAINT_ID));
                                list_compalint.add(c.getString(TAG_COMPLAINT_DESCR));
                                list_compalint_type.add(c.getString(TAG_COMPLAINT_TYPE));
                                list_compalint_rate.add(c.getString(TAG_RATE));


                            }

                            list_compalintid.add("1000");
                            list_compalint.add("");
                            list_compalint_rate.add("");
                            list_compalint_type.add("Other");


                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list_compalint_type);
                            spnComplaints.setAdapter(adapter);

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
                    hideDialog();
                } else {
                    getComplaintMasterDetailsFromServer(serviceid);
                }

            }
        });
        MyApplication.getInstance().addToRequestQueue(request);


    }


    //onCreate completed

    private void getAllMenuDetailsFromServer() {
        String url = AllKeys.WEBSITE + "ViewService?type=menu";
        Log.d(TAG, "URL ViewMenus " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, " ViewMenus Response  : " + response.toString());

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
                    hideDialog();
                } else {
                    getAllMenuDetailsFromServer();
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


    public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<MenuRecyclerViewAdapter.MyViewHolder> {


        private final Context _context;
        private ArrayList<Menudata> list_menudata;
        private String TAG = MenuRecyclerViewAdapter.class.getSimpleName();

        public MenuRecyclerViewAdapter(Context context, ArrayList<Menudata> lmenu) {
            this._context = context;
            this.list_menudata = lmenu;


        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private final CircleImageView ivMenuImg;
            private final TextView tvMenuTitle;
            private final ImageView ivSelected;

            public MyViewHolder(View itemView) {
                super(itemView);

                ivMenuImg = (CircleImageView) itemView.findViewById(R.id.ivMenu);
                tvMenuTitle = (TextView) itemView.findViewById(R.id.tvMenuTitle);
                ivSelected = (ImageView) itemView.findViewById(R.id.ivSelected);


            }
        }

        @Override
        public MenuRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(_context).inflate(R.layout.row_single_menu, parent, false);
            MyViewHolder holder = new MyViewHolder(view);


            return holder;
        }

        @Override
        public void onBindViewHolder(MenuRecyclerViewAdapter.MyViewHolder holder, int position) {

            Menudata md = list_menudata.get(position);

            holder.tvMenuTitle.setText(md.getMenuname());
            Log.d(TAG, "IMAGE URL : " + md.getImage());
            Glide.with(_context).load(md.getImage()).crossFade().into(holder.ivMenuImg);

            if (md.isSelectionstatus() == true) {
                holder.ivSelected.setVisibility(View.VISIBLE);
            } else {
                holder.ivSelected.setVisibility(View.INVISIBLE);

            }


            //Glide.with(_context).load(md.getImage()).crossFade().into(holder.ivMenuImg);

        }

        @Override
        public int getItemCount() {
            return list_menudata.size();
        }
    }


}
