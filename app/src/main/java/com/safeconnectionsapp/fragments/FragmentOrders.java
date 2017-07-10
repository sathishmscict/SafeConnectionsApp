package com.safeconnectionsapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.safeconnectionsapp.R;
import com.safeconnectionsapp.adapter.ComplaintsDisplayRecyclerViewAdapter;
import com.safeconnectionsapp.adapter.OrderDisplayRecyclerViewAdapter;
import com.safeconnectionsapp.app.MyApplication;
import com.safeconnectionsapp.helper.AllKeys;
import com.safeconnectionsapp.pojo.ComplaintAndQuotaionMaster;
import com.safeconnectionsapp.pojo.OrderMaster;
import com.safeconnectionsapp.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;


public class FragmentOrders extends android.support.v4.app.Fragment {


    private Context context = getActivity();


    private SessionManager sessionmanager;

    private HashMap<String, String> userDetails = new HashMap<String, String>();


    private String TAG = FragmentOrders.class.getSimpleName();
    private SpotsDialog pDialog;

    private RecyclerView recyclerview_orders;
    private OrderDisplayRecyclerViewAdapter adapter;
    private ArrayList<OrderMaster> list_OrderData = new ArrayList<OrderMaster>();


    public FragmentOrders() {
        // Required empty public constructor
    }

	/*
     * @Override public void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState); setHasOptionsMenu(true);
	 * 
	 * 
	 * }
	 */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_complaints, container,
                false);

        if (Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        pDialog = new SpotsDialog(getActivity());
        pDialog.setCancelable(false);




        sessionmanager = new SessionManager(getActivity());
        userDetails = sessionmanager.getSessionDetails();


        recyclerview_orders = (RecyclerView) rootView.findViewById(R.id.recyclerview_orders);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);

      /*  LinearLayoutManager layoutManagerrr = (LinearLayoutManager) recyclerview_products
                .getLayoutManager();
        layoutManagerrr.scrollToPositionWithOffset(0, 0);*/
        recyclerview_orders.setLayoutManager(layoutManager);
        recyclerview_orders.addItemDecoration(new AllKeys.GridSpacingItemDecoration(2, AllKeys.dpToPx(2, getActivity()), true));


        getAllOrderDetailsFromServer();

        // Inflate the layout for this fragment
        return rootView;
    }
    //onCreateView Completed


    private void getAllOrderDetailsFromServer() {
        String url_getOrders = AllKeys.WEBSITE + "ViewOrderHistory?type=OrderHistory&clientid=" + userDetails.get(SessionManager.KEY_USER_ID) + "";
        Log.d(TAG, "URL ViewServiceDetail : " + url_getOrders);
        JsonObjectRequest str_getOrders = new JsonObjectRequest(Request.Method.GET, url_getOrders, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "Response ViewServiceDetail  :" + response);

                list_OrderData.clear();


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






                                                                    //OrderMaster(String orderdate, String totalamount, String orderid, String productname, String product_qty, String price, String image) {
                                OrderMaster om= new OrderMaster(c.getString(AllKeys.TAG_DATE) , c.getString(AllKeys.TAG_TOTAL_AMT),c.getString(AllKeys.TAG_ORDER_ID),c.getString(AllKeys.TAG_PRODUCT_NAME),c.getString(AllKeys.TAG_PRODUCT_QUANTITY),c.getString(AllKeys.TAG_PRODUCT_PRICE),c.getString(AllKeys.TAG_PRODUCT_IMAGE));



                                list_OrderData.add(om);

                            }
                            adapter = new OrderDisplayRecyclerViewAdapter(getActivity(), list_OrderData);
                            recyclerview_orders.setAdapter(adapter);
                        } else {
                            Toast.makeText(getActivity(), "No Orders Found", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ServerError || error instanceof NetworkError) {
                    hideDialog();

                } else {
                    getAllOrderDetailsFromServer();
                }

            }
        });
        MyApplication.getInstance().addToRequestQueue(str_getOrders);

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // pDialog.dismiss();
    }

    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        super.onResume();
    }


}