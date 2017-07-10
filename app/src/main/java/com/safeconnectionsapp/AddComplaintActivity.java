package com.safeconnectionsapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.karumi.dexter.listener.single.PermissionListener;
import com.kosalgeek.android.imagebase64encoder.ImageBase64;
import com.safeconnectionsapp.app.MyApplication;
import com.safeconnectionsapp.helper.AllKeys;
import com.safeconnectionsapp.helper.CustomRequest;
import com.safeconnectionsapp.helper.ImageUtils;
import com.safeconnectionsapp.helper.NetConnectivity;
import com.safeconnectionsapp.pojo.ComplaintAndQuotaionMaster;
import com.safeconnectionsapp.session.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class AddComplaintActivity extends AppCompatActivity {

    private RecyclerView rv_complaint_mst;
    private Context context = this;
    private String TAG = AddComplaintActivity.class.getSimpleName();
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();


    //getComplaintMaster Realted Keys
    public static final String TAG_COMPLAINT_ID = "complainid";
    public static final String TAG_COMPLAINT_TYPE = "complaintype";
    public static final String TAG_COMPLAINT_DESCR = "complaindescription";
    public static final String TAG_COMPLAINT_IMAGE = "complainimage";
    public static final String TAG_RATE = "rate";

    private ArrayList<ComplaintAndQuotaionMaster> list_complaint_master = new ArrayList<ComplaintAndQuotaionMaster>();
    private ComplaintsMasterAdapter adapter;
    private SpotsDialog pDialog;

    private int ITEM_POSITION = 0;
    private String BASE64STRING = "";
    private String userChoosenTask;
    private int SELECT_FILE = 100;
    private int REQUEST_CAMERA = 101;
    private Bitmap bitmap;
    private Button btnConfirm;
    private EditText edtComplaint;
    private TextInputLayout edtComplaintWrapper;
    private String IMAGE_URL = "";
    private int SELECTED_POSITION = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_complaint);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        } catch (Exception e) {
            e.printStackTrace();
        }


        setTitle("Complaint Info");
        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();

        pDialog = new SpotsDialog(context);
        pDialog.setCancelable(false);

        rv_complaint_mst = (RecyclerView) findViewById(R.id.rv_complaint_mst);
        LinearLayoutManager lManager = new LinearLayoutManager(context);
        rv_complaint_mst.setLayoutManager(lManager);

        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        edtComplaint = (EditText) findViewById(R.id.edtComplaint);
        edtComplaintWrapper = (TextInputLayout) findViewById(R.id.edtComplaintWrapper);


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sednComplaintDetailsToServer();


            }
        });

       /* rv_complaint_mst.addOnItemTouchListener(new AllKeys.RecyclerTouchListener(context, rv_complaint_mst, new AllKeys.ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                try {

                    for (int i = 0; i < list_complaint_master.size(); i++) {
                        list_complaint_master.get(i).setComplaintStatus(false);
                    }
                    list_complaint_master.get(position).setComplaintStatus(true);

                    adapter.notifyDataSetChanged();

                    Toast.makeText(context, "Name : " + list_complaint_master.get(position).getComplaintdescr() + " Id : " + list_complaint_master.get(position).getComplaintId(), Toast.LENGTH_SHORT).show();

                    ITEM_POSITION = position;
                    //sessionManager.setCategoryTypeAndIdDetails( list_complaint_master.get(position).getComplaintId(), list_menu.get(position).getMenuname(),"multiple");

                    edtComplaint.setText(list_complaint_master.get(position).getComplaintdescr());



                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));*/


        getComplaintMasterDetailsFromServer(userDetails.get(SessionManager.KEY_CATEGORY_ID));


        //ComplaintAndQuotaionMaster(String complaintId,String complaintdescr ,String imageurl)

    }

    private void sednComplaintDetailsToServer()
    {
        if (edtComplaint.getText().toString().equals("")) {
            edtComplaintWrapper.setErrorEnabled(true);
            edtComplaintWrapper.setError("Enter Complaint");

        } /*else if (edtEmail.getText().toString().equals("")) {
            edtEmailWrapper.setErrorEnabled(true);
            edtEmailWrapper.setError("Enter Email");
            sessionManager.setUserDetails(userDetails.get(SessionManager.KEY_USER_ID), userDetails.get(SessionManager.KEY_USER_NAME), edtEmail.getText().toString(), userDetails.get(SessionManager.KEY_USER_MOBILE), userDetails.get(SessionManager.KEY_USER_ADDRESS), userDetails.get(SessionManager.KEY_IS_NEWUSER), userDetails.get(SessionManager.KEY_REFERAL_CODE), userDetails.get(SessionManager.KEY_GENDER));
            userDetails = sessionManager.getSessionDetails();

        }*/ else {
            edtComplaintWrapper.setErrorEnabled(false);
            //edtEmailWrapper.setErrorEnabled(false);
            //showDialog();


            //String url = AllKeys.WEBSITE + "InsertServiceDetail?type=detail&userid=" + userDetails.get(SessionManager.KEY_USER_ID) + "&serviceid=" + list_menu.get(position).getMenuid() + "&date=" + AllKeys.convertEncodedString(AllKeys.getDateTime()) + "&description=" + AllKeys.convertEncodedString(edtComplaint.getText().toString()) + "&emailid=" + AllKeys.convertEncodedString(edtEmail.getText().toString()) + "";
            String url = AllKeys.WEBSITE + "InsertServiceDetail?type=detail&userid=" + userDetails.get(SessionManager.KEY_USER_ID) + "&serviceid=" + list_complaint_master.get(SELECTED_POSITION).getComplaintId() + "&date=" + AllKeys.convertEncodedString(AllKeys.getDateTime()) + "&description=" + AllKeys.convertEncodedString(edtComplaint.getText().toString()) + "&status=0&image=" + IMAGE_URL + "";
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

                            //tvError.setVisibility(View.GONE);


                            if (record_status == true) {

                            /*    dialog.cancel();

                                dialog.dismiss();*/

                                Toast.makeText(context, "Complaint has been submitted", Toast.LENGTH_SHORT).show();

                                AlertDialog.Builder builder = new AlertDialog.Builder(AddComplaintActivity.this);
                                builder.setTitle("Complaint Info");
                                builder.setMessage(userDetails.get(SessionManager.KEY_CATEGORY_NAME) + " Complaint has been submited.we will contact to you with in a 24 hours.\nThank you ,for using  Safe Conncetions services.");
                                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        Intent intent = new Intent(context, DashBoardActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                builder.show();
                                hideDialog();

                            }
                        } else {
                    /*        tvError.setVisibility(View.VISIBLE);
                            tvError.setText(str_error);
*/

                            Toast.makeText(context, "Sorry try again...", Toast.LENGTH_SHORT).show();
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


                    list_complaint_master.clear();

                    if (error_status == false) {

                        if (record_status == true) {


                            JSONArray arr = response.getJSONArray(AllKeys.ARRAY_DATA);
                            for (int i = 0; i < arr.length(); i++) {

                                JSONObject c = arr.getJSONObject(i);

                                //Menudata(String menuid, String menuname, String image) {


                                sessionManager.setVisitingCharge(response.getString(AllKeys.TAG_RR_VISIT_CHARGE));
                                userDetails = sessionManager.getSessionDetails();


                                ComplaintAndQuotaionMaster ca = new ComplaintAndQuotaionMaster(c.getString(TAG_COMPLAINT_ID), c.getString(TAG_COMPLAINT_DESCR), c.getString(TAG_COMPLAINT_IMAGE), false, c.getString(TAG_COMPLAINT_TYPE));
                                list_complaint_master.add(ca);


                            }

                            ComplaintAndQuotaionMaster ca = new ComplaintAndQuotaionMaster("0", "", "", false, "Other");
                            list_complaint_master.add(ca);

                            adapter = new ComplaintsMasterAdapter(context, list_complaint_master);
                            rv_complaint_mst.setAdapter(adapter);


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

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Select Profile Picture!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals("Take Photo")) {


                    Dexter.withActivity(AddComplaintActivity.this)
                            .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse response) {


                                    userChoosenTask = "Take Photo";

                                    cameraIntent();

                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse response) {

                                    Toast.makeText(context, "Please grant access camera permission", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                            }).check();


                } else if (items[item].equals("Choose from Library")) {


                    Dexter.withActivity(AddComplaintActivity.this)
                            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse response) {

                                    userChoosenTask = "Choose from Library";

                                    galleryIntent();

                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse response) {

                                    Toast.makeText(context, "Please grant access camera permission", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                            }).check();


                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);

        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String dd = cursor.getString(column_index);
            return cursor.getString(column_index);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return "sa";

    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void onCaptureImageResult(Intent data) {

        bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);


        int bitmapByteCount = BitmapCompat.getAllocationByteCount(bitmap);
        Log.d("C:Before Bitmap Size : ", "" + bitmapByteCount);


        //String realPath=getRealPathFromURI(data.getData());

        Uri tempUri = getImageUri(bitmap);


        String realPath = null;
        try {
            Log.d("C: Realpath URI : ", "" + tempUri.toString());
            realPath = getRealPathFromURI(tempUri);
            Log.d("C: Realpath : ", realPath);
            showDialog();
            BASE64STRING = ImageBase64
                    .with(getApplicationContext())
                    .requestSize(512, 512)
                    .encodeFile(realPath);
            hideDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }


        bitmap = ImageUtils.getInstant().getCompressedBitmap(realPath);
        //imageView.setImageBitmap(bitmap);

        bitmapByteCount = BitmapCompat.getAllocationByteCount(bitmap);
        Log.d("C:After Bitmap Size : ", "" + bitmapByteCount);


        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (NetConnectivity.isOnline(context)) {

            // new SendUserProfilePictureToServer().execute();

            SendImageToServer();
        } else {

            //   checkInternet();
            Toast.makeText(context, "Please enable internet", Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {


        if (data != null) {
            try {


                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());

                int bitmapByteCount = BitmapCompat.getAllocationByteCount(bitmap);
                Log.d("Before Bitmap Size : ", "" + bitmapByteCount);


                Uri tempUri = getImageUri(bitmap);


                String realPath = null;
                try {
                    Log.d("CC: Realpath URI : ", "" + tempUri.toString());
                    realPath = getRealPathFromURI(tempUri);
                    BASE64STRING = ImageBase64
                            .with(getApplicationContext())
                            .requestSize(512, 512)
                            .encodeFile(realPath);
                    Log.d("CC: Realpath : ", realPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                /*Uri uri = data.getData();

                realPath=""+getRealPathFromURI(data.getData());
                Log.d("RealPath : " , ""+realPath);
                realPath = uri.getEncodedPath();
                Log.d("RealPath URI : " , ""+realPath);
                realPath=""+getRealPathFromURI_NEW(data.getData());
                Log.d("RealPath New : " , ""+realPath);*/


                bitmap = ImageUtils.getInstant().getCompressedBitmap(realPath);
                //imageView.setImageBitmap(bitmap);

                bitmapByteCount = BitmapCompat.getAllocationByteCount(bitmap);
                Log.d("After Bitmap Size : ", "" + bitmapByteCount);


                // getStringImage(bm);


            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        try {
            if (NetConnectivity.isOnline(context)) {

                //new SendUserProfilePictureToServer().execute();

                SendImageToServer();


            } else {
//                checkInternet();
                Toast.makeText(context, "Please enable internet", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void SendImageToServer() {
        showDialog();
        String url = AllKeys.WEBSITE + "UpdateProfileImage";
        Log.d(TAG, "URL " + url);
        CustomRequest request = new CustomRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                Log.d(TAG, "Response   UploadImage : " + response.toString());

                try {
                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);

                    if (error_status == false) {
                        if (record_status == true) {


                            JSONArray arr = response.getJSONArray(AllKeys.ARRAY_DATA);
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject c = arr.getJSONObject(i);


                                String strImageName = c.getString("image");
                                IMAGE_URL = c.getString("image");
                                IMAGE_URL = IMAGE_URL.replace("http://safeconnexions.com", "");

                                list_complaint_master.get(ITEM_POSITION).setImageurl(c.getString("image"));
                                //list_ClothesData.get(ITEM_POSITION).setImageName(strImageName);
                                adapter.notifyDataSetChanged();

                            }
                            //Set Images Details To Adapter and close dialog


                            hideDialog();
                        } else {
                            hideDialog();
                            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        hideDialog();
                        Toast.makeText(context, "" + str_error, Toast.LENGTH_SHORT).show();
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
                    SendImageToServer();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("type", "complainimg");
                params.put("userid", userDetails.get(SessionManager.KEY_USER_ID));

                params.put("imagecode", BASE64STRING);


                Log.d(TAG, "Complaint Image Sending PArams : " + params.toString());
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(request);
    }


    /**
     * Cusotm Adapter
     */


    public class ComplaintsMasterAdapter extends RecyclerView.Adapter<ComplaintsMasterAdapter.MyViewHolder> {

        private final Context _context;
        private final ArrayList<ComplaintAndQuotaionMaster> list_complatintandQuotation;


        public ComplaintsMasterAdapter(Context context, ArrayList<ComplaintAndQuotaionMaster> lst) {

            this._context = context;
            this.list_complatintandQuotation = lst;


        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private final TextView tvDescr;
            private final ImageView ivImage;
            private final ImageView ivCapture;
            private final ImageView ivSelected;

            public MyViewHolder(View itemView) {
                super(itemView);

                tvDescr = (TextView) itemView.findViewById(R.id.tvDescr);
                ivImage = (ImageView) itemView.findViewById(R.id.iv);
                ivCapture = (ImageView) itemView.findViewById(R.id.ivCapture);
                ivSelected = (ImageView) itemView.findViewById(R.id.ivSelected);


            }
        }

        @Override
        public ComplaintsMasterAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(_context).inflate(R.layout.row_single_complaint_quotation, parent, false);
            ComplaintsMasterAdapter.MyViewHolder v = new ComplaintsMasterAdapter.MyViewHolder(view);
            return v;
        }

        @Override
        public void onBindViewHolder(ComplaintsMasterAdapter.MyViewHolder holder, final int position) {


            holder.tvDescr.setText(list_complatintandQuotation.get(position).getComplainttype());

            try {
                //Glide.with(_context).load(list_categoty.get(position).getImageurl()).placeholder(R.drawable.app_logo).error(R.drawable.app_logo).crossFade(R.anim.fadein, 2000).override(500, 500).into(holder.ivCategory);

                Picasso.with(_context)
                        .load(list_complatintandQuotation.get(position).getImageurl())
                        .placeholder(R.drawable.app_logo)
                        .error(R.drawable.app_logo)
                        .into(holder.ivImage);


            } catch (Exception e) {
                e.printStackTrace();
            }


            if (list_complaint_master.get(position).isComplaintStatus() == true) {

                holder.ivSelected.setImageDrawable(getResources().getDrawable(R.drawable.icon_selected_enable));

            } else {
                holder.ivSelected.setImageDrawable(getResources().getDrawable(R.drawable.icon_selected_disable));
            }


            holder.ivSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (int i = 0; i < list_complaint_master.size(); i++) {
                        list_complaint_master.get(i).setComplaintStatus(false);
                    }
                    list_complaint_master.get(position).setComplaintStatus(true);

                    SELECTED_POSITION = position;
                    adapter.notifyDataSetChanged();
                    //adapter.notifyItemChanged(position);


                    edtComplaint.setText(list_complatintandQuotation.get(position).getComplaintdescr());
                }
            });

            holder.ivCapture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(context, "Clicked on capture image", Toast.LENGTH_SHORT).show();

                    ITEM_POSITION = position;
                    selectImage();


                }
            });


        }

        @Override
        public int getItemCount() {
            return list_complatintandQuotation.size();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {

            try {
           Intent     i = new Intent(context, MenuActivity.class);
                //i.putExtra("ActivityName",ACTIVITYNAME);
                startActivity(i);
                finish();
            } catch (Exception e) {

                Log.d(TAG, "Error in Converting Class : " + e.getMessage());
                e.printStackTrace();
            }


        return  false;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        Intent i = null;
        try {
            i = new Intent(context, MenuActivity.class);
            //i.putExtra("ActivityName",ACTIVITYNAME);
            startActivity(i);
            finish();
        } catch (Exception e) {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Error in Converting Class : " + e.getMessage());
            e.printStackTrace();
        }
        super.onBackPressed();

    }


}
