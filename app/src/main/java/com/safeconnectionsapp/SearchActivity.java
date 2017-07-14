package com.safeconnectionsapp;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.safeconnectionsapp.app.MyApplication;
import com.safeconnectionsapp.databinding.ContentSearchBinding;
import com.safeconnectionsapp.databinding.RowItemBinding;
import com.safeconnectionsapp.helper.AllKeys;
import com.safeconnectionsapp.pojo.SearchData;
import com.safeconnectionsapp.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class SearchActivity extends AppCompatActivity {


    //Searching Related Keys
/*    public static final String TAG_SEARCH_ARRAY = "data";
    public static final String TAG_SEARCH_FLAG = "flag";
    public static final String TAG_SEARCH_MESSAGE = "message";
    public static final String TAG_SEARCH_ID = "id";
    public static final String TAG_SEARCH_PRODUCTID = "product_id";
    public static final String TAG_SEARCH_NAME = "name";
    public static final String TAG_SEARCH_CATEGORYID = "category_id";
    public static final String TAG_SEARCH_CATEGORYNAME = "category_name";
    public static final String TAG_SEARCH_CATEGORY_TYPE = "category_type";
    public static final String TAG_SEARCH_CATEGORY_TYPE_NAME = "category_type_name";
    public static final String TAG_SEARCH_IS_ACTIVE_SELLER = "is_active_seller";
    public static final String TAG_SEARCH_IS_ADMIN_ACTIVE = "is_active_admin";
    public static final String TAG_SEARCH_SKU = "sku";
    public static final String TAG_SEARCH_PRICE = "price";
    public static final String TAG_SEARCH_IS_PARENT = "is_parent";*/


    ContentSearchBinding activityMainBinding;
    ListAdapter adapter;

    List<String> arrayList = new ArrayList<>();
    private String TAG = SearchActivity.class.getSimpleName();
    private ArrayList<SearchData> list_SearchData = new ArrayList<SearchData>();
    private Context context = this;
    private SpotsDialog pDialog;
    private SessionManager sessionmanager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();

    private ImageView ivBack;
    private String categortype;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.content_search);

        activityMainBinding.search.setActivated(true);
        activityMainBinding.search.setQueryHint(getResources().getString(R.string.search_hint));
        activityMainBinding.search.onActionViewExpanded();
        activityMainBinding.search.setIconified(false);
        activityMainBinding.search.clearFocus();


        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
        activityMainBinding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                try {
                    getProductDataFromServer(query);

                    if(adapter!=null)
                    {

                        adapter.getFilter().filter(query);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                try {
                    getProductDataFromServer(newText);

                    if(adapter!=null)
                    {

                    adapter.getFilter().filter(newText);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }
        });


        pDialog = new SpotsDialog(context);
        pDialog.setCancelable(false);

        sessionmanager = new SessionManager(context);
        userDetails = sessionmanager.getSessionDetails();





        ivBack = (ImageView)findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intet = new Intent(context , DashBoardActivity.class);
                startActivity(intet);
                finish();
            }
        });



       // getProductDataFromServer("");


    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        try {
            getMenuInflater().inflate(R.menu.menu_search, menu);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }*/


    public static String convertToJsonFormat(String json_data) {

        String response = "{\"Maindata\":" + json_data + "]}";
        return response;

    }



    public void showDialog() {


        try {
            if (!pDialog.isShowing()) {

                pDialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void hideDialog() {
        try {
            if (pDialog.isShowing()) {
                pDialog.dismiss();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getProductDataFromServer(final String str) {


        showDialog();

        String url_getData = AllKeys.WEBSITE+"SearchProductData?type=search&search="+ str;
        Log.d(TAG, "SearchProductData  URL : "+url_getData);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url_getData, null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG ,"Response SearchProductData : "+response);


                try {
                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);


                    list_SearchData.clear();
                    arrayList.clear();

                    if (error_status == false) {

                        if (record_status == true) {

                            JSONArray arr = response.getJSONArray(AllKeys.ARRAY_DATA);

                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject cc = arr.getJSONObject(i);
                                arrayList.add(cc.getString(AllKeys.TAG_PRODUCT_NAME));

                                //SearchData(String search_flag, String search_id, String search_product_id, String search_name, String search_category_id, String search_category_name, String search_category_type, String search_category_type_name, String search_is_active_seller, String search_is_active_admin, String search_sku, String search_price) {


                                categortype="category";




                                SearchData sc = new SearchData("true", "1", String.valueOf(cc.getInt(AllKeys.TAG_PRODUCT_ID)), cc.getString(AllKeys.TAG_PRODUCT_NAME), String.valueOf(cc.getInt(AllKeys.TAG_CATEGORYID)), cc.getString(AllKeys.TAG_PRODUCT_IMAGE), categortype, "","1", "1", "", String.valueOf(cc.getInt(AllKeys.TAG_PRODUCT_PRICE)));


                                list_SearchData.add(sc);


                            }
                            adapter = new ListAdapter(context, arrayList);
                            activityMainBinding.listView.setAdapter(adapter);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    hideDialog();
                }



                hideDialog();




                //Toast.makeText(SearchActivity.this, "Search : " + response, Toast.LENGTH_SHORT).show();


                //  adapter = new ListAdapter(context,arrayList);
                // activityMainBinding.listView.setAdapter(adapter);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

             //   Toast.makeText(SearchActivity.this, "Errro :" + error, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Errro :" + error.getMessage());

                hideDialog();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<String, String>();

                params.put("q", str);
                params.put("limit", "30");

                Log.d(TAG, "Params : " + params.toString());

                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(request);


    }


    public class ListAdapter extends BaseAdapter implements Filterable {

        List<String> mData;
        List<String> mStringFilterList;
        ListAdapter.ValueFilter valueFilter;
        private LayoutInflater inflater;
        private Context context;


        public ListAdapter(Context context, List<String> cancel_type) {
            mData = cancel_type;
            mStringFilterList = cancel_type;
            this.context = context;
        }


        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {

            if (inflater == null) {
                inflater = (LayoutInflater) parent.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            RowItemBinding rowItemBinding = DataBindingUtil.inflate(inflater, R.layout.row_item, parent, false);
            rowItemBinding.stringName.setText(mData.get(position));

            rowItemBinding.stringName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  Toast.makeText(context, "Selecte : " + list_SearchData.get(position).getSearch_product_id(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Data : " + mData.get(position) + list_SearchData.get(position).getSearch_product_id());

              /*      Intent intent = new Intent(context , SingleItemActivity.class);
                    startActivity(intent);
                    finish();*/

                    try {
                        //Toast.makeText(_context, "Name : " + pd.getProductname() + " Id : " + pd.getProductid(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Clicked On Name : " + list_SearchData.get(position).getSearch_name() + " Id : " + list_SearchData.get(position).getSearch_product_id());
                        Intent intent = new Intent(context,
                                SingleItemActivity.class);


                        sessionmanager.setProductDetails(list_SearchData.get(position).getSearch_product_id(), "-1", "", "", "0");

                        sessionmanager.setCategoryTypeAndIdDetails(list_SearchData.get(position).getSearch_category_id(),"",list_SearchData.get(position).getSearch_category_type());




                      //  Toast.makeText(context, "Company id : "+list_SearchData.get(position).getCompnayid(), Toast.LENGTH_SHORT).show();
                        //sessionmanager.setCompnayTypeAndIdDetails(list_SearchData.get(position).getCompnayid() , dbhandler.getCompanyNameByCompnayId(Integer.parseInt(list_SearchData.get(position).getCompnayid())));


                        //sessionmanager.setCategoryTypeAndIdDetails(categoryType, categoryId, categoryName);





                        //intent.putExtra("ProductId", pd.getProductid());
                        intent.putExtra("ActivityName", TAG);

                        sessionmanager.setActivityName(TAG);

                        //sessionmanager.setActivityName("");
                        context.startActivity(intent);
                        //finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });


            return rowItemBinding.getRoot();
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {
                valueFilter = new ValueFilter();
            }
            return valueFilter;
        }

        private class ValueFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint != null && constraint.length() > 0) {
                    List<String> filterList = new ArrayList<>();
                    for (int i = 0; i < mStringFilterList.size(); i++) {
                        if ((mStringFilterList.get(i).toUpperCase()).contains(constraint.toString().toUpperCase())) {
                            filterList.add(mStringFilterList.get(i));
                        }
                    }
                    results.count = filterList.size();
                    results.values = filterList;
                } else {
                    results.count = mStringFilterList.size();
                    results.values = mStringFilterList;
                }
                return results;

            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                mData = (List<String>) results.values;
                notifyDataSetChanged();
            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            Intent i = new Intent(context, ItemDisplayActivity.class);
            startActivity(i);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(context, ItemDisplayActivity.class);
        startActivity(i);
        finish();
    }
}
