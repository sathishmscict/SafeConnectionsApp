package com.safeconnectionsapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
/*import com.mingle.entity.MenuEntity;
import com.mingle.sweetpick.BlurEffect;
import com.mingle.sweetpick.CustomDelegate;
import com.mingle.sweetpick.RecyclerViewDelegate;
import com.mingle.sweetpick.SweetSheet;*/
import com.safeconnectionsapp.adapter.NewProductsRecyclerViewAdapter;
import com.safeconnectionsapp.app.MyApplication;

import com.safeconnectionsapp.helper.AllKeys;
import com.safeconnectionsapp.helper.CustomFonts;
import com.safeconnectionsapp.helper.NetConnectivity;
import com.safeconnectionsapp.helper.Utils;
import com.safeconnectionsapp.pojo.ProductData;
import com.safeconnectionsapp.session.SessionManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import dmax.dialog.SpotsDialog;


public class ItemDisplayActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerview_products;
    private Context context = this;

    private SpotsDialog pDialog;
    private SessionManager sessionmanager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private String TAG = ItemDisplayActivity.class.getSimpleName();
    private ArrayList<ProductData> list_Products = new ArrayList<ProductData>();
    private FloatingActionButton fab;
    private TextView txtDisplayData;
    private TextView txtSort;
    private TextView txtFilter;
    private boolean IsGridData = true;
    NewProductsRecyclerViewAdapter adapter_NewProduct;

    private ArrayList<String> list_wishListProducts = new ArrayList<String>();

    private TextView txtnodata;
    private MenuItem cart;
    private ArrayList<String> list_cartItemsId = new ArrayList<String>();
    private CardView cvSorting;

    /*Expandable ListView*/
    private static final int MAX_LEVELS = 3;

    private static final int LEVEL_1 = 1;
    private static final int LEVEL_2 = 2;
    private static final int LEVEL_3 = 3;

    private ArrayList<String> firstLevelCategoryName = new ArrayList<String>();
    private ArrayList<Integer> firstLEvelCategoryId = new ArrayList<Integer>();

    private ArrayList<String> secondLevelCategoryName = new ArrayList<String>();
    private ArrayList<Integer> secondLEvelCategoryId = new ArrayList<Integer>();

    private ArrayList<String> thirdtLevelCategoryName = new ArrayList<String>();
    private ArrayList<Integer> thirdLEvelCategoryId = new ArrayList<Integer>();

    // private SweetSheet mSweetSheet;
    private RelativeLayout rl;
    private ArrayList<String> sortType = new ArrayList<String>();
    private String SORTING_TYPE = "Default";
    //  private SweetSheet mSweetSheet3;
    //private TextView size_ss, size_m, size_l, size_xl, size_xxl;
    //  private RangeBar price_rangebar;
    private boolean Is_Price_Range_Enalbe = false;
    private Button btnPriceMinValue;
    private Button btnPriceMaximumValue;
    private Integer MINIMUM_VALUE_IN_RS = 0, MAXIMUM_VALUE_IN_RS = 0, SELECTED_MAXIMUM_VALUE = 0;
    private ArrayList<String> list_productSize = new ArrayList<String>();
    private ArrayList<String> list_selected_productSize = new ArrayList<String>();
    private CardView crdSize;
    private RecyclerView recyclerview_size;

    // Store a member variable for the listener
    // private EndlessRecyclerViewScrollListener scrollListener;
    //private Integer OFFSET = 10, PAGENO = 0, TOTAL_PAGES = 0;
    //private Integer TOTAL_RECORDS = 0;
    private LinearLayout ll_no_internet;
    private LinearLayout ll_mainUI;
    private Button btnNoNetwork;

    private SwipeRefreshLayout swipeRefreshLayout;
    //private dbhandler db;
    private MenuItem menu_displayview;
    private Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerview_products
                        .getLayoutManager();
                layoutManager.scrollToPositionWithOffset(0, 0);
            }
        });


        if (Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        } catch (Exception e) {
            e.printStackTrace();
        }


        //db = new dbhandler(context);


        pDialog = new SpotsDialog(context);
        pDialog.setCancelable(false);

        sessionmanager = new SessionManager(context);
        userDetails = sessionmanager.getSessionDetails();


        setTitle(userDetails.get(SessionManager.KEY_CATEGORY_NAME));


        try {
//Track View Details
            Answers.getInstance().logContentView(new ContentViewEvent());
            Answers.getInstance().logContentView(new ContentViewEvent()
                    .putContentName("Items Display")
                    .putContentType("Items Display Screen")
                    .putCustomAttribute("CategoryID", userDetails.get(SessionManager.KEY_CATEGORY_ID))
                    .putCustomAttribute("CategoryName", userDetails.get(SessionManager.KEY_CATEGORY_NAME))
                    .putContentId("ItemsDispaly-100"));
        } catch (Exception e) {
            e.printStackTrace();
        }


        ll_no_internet = (LinearLayout) findViewById(R.id.ll_no_internet);
        btnNoNetwork = (Button) findViewById(R.id.btnNoNetwork);

        btnNoNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoadDataFromServer();
            }
        });

        ll_mainUI = (LinearLayout) findViewById(R.id.llmainUI);
        cvSorting = (CardView) findViewById(R.id.cvSorting);
        txtDisplayData = (TextView) findViewById(R.id.txtDisplayData);
        txtSort = (TextView) findViewById(R.id.txtSort);
        txtFilter = (TextView) findViewById(R.id.txtFilter);


        hideNoInternetUI();
        showInternetUI();


        rl = (RelativeLayout) findViewById(R.id.rl);
        /**
         * Handle Grid and ListView
         */
        txtDisplayData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (IsGridData == true) {

                    IsGridData = false;
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                    recyclerview_products.setLayoutManager(layoutManager);
                    txtDisplayData.setBackgroundResource(R.drawable.icon_grid);
                    //adapter_NewProduct = new NewProductsRecyclerViewAdapter(context, list_Products, "list", list_wishListProducts);
                    adapter_NewProduct = new NewProductsRecyclerViewAdapter(context, list_Products, "list", list_wishListProducts);
                    recyclerview_products.setAdapter(adapter_NewProduct);


                } else {
                    adapter_NewProduct = new NewProductsRecyclerViewAdapter(context, list_Products, "grid", list_wishListProducts);
                    recyclerview_products.setAdapter(adapter_NewProduct);

                    txtDisplayData.setBackgroundResource(R.drawable.icon_list);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ItemDisplayActivity.this, 2);
                    recyclerview_products.setLayoutManager(layoutManager);
                    recyclerview_products.addItemDecoration(new AllKeys.GridSpacingItemDecoration(2, AllKeys.dpToPx(2, context), true));

                    IsGridData = true;
                }


            }
        });


        // mSweetSheet.toggle();
        //txtSort.setTypeface(Typeface.DEFAULT);
        // txtFilter.setTypeface(Typeface.DEFAULT);
        txtSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               /* if (mSweetSheet3.isShow()) {
                    mSweetSheet3.dismiss();
                }
                mSweetSheet.toggle();*/


                fab.setVisibility(View.GONE);

                //Toast.makeText(context, "Sort", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Clicked on Sort");
            }
        });
        txtFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "Filter", Toast.LENGTH_SHORT).show();
              /*  try {
                    Log.d(TAG, "Clicked on Filter");
                    if (mSweetSheet.isShow()) {
                        mSweetSheet.dismiss();
                    }

                    mSweetSheet3.toggle();
                    //   Toast.makeText(context, "Filter", Toast.LENGTH_SHORT).show();

                    fab.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/


            }
        });


        txtnodata = (TextView) findViewById(R.id.txtnodata);
        txtnodata.setVisibility(View.GONE);
        txtnodata.setTypeface(CustomFonts.typefaceCondensed(context));


        recyclerview_products = (RecyclerView) findViewById(R.id.recyclervieW_products);


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);


        //   swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this);


        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {


                                        if (NetConnectivity.isOnline(context)) {
                                            //new AsyncData1().execute();
                                            // swipeRefreshLayout.setRefreshing(true);
                                           /* if (swipeRefreshLayout.isRefreshing()) {
                                                swipeRefreshLayout.setRefreshing(false);
                                            }*/
                                            swipeRefreshLayout.setRefreshing(false);
                                            LoadDataFromServer();
                                            //  new SendAllDetialToServer().execute();
                                            Log.d("oncreate", "from run method");


                                        } else {
                                            // swipeRefreshLayout.setRefreshing(false);
                                            //checkInternet();
                                            Toast.makeText(context, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
        );




        /*RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);


        recyclerview_products.setLayoutManager(layoutManager);
        recyclerview_products.addItemDecoration(new dbhandler.GridSpacingItemDecoration(2, db.dpToPx(2), true));*/

        LinearLayoutManager layoutManager2 = new GridLayoutManager(ItemDisplayActivity.this, 2);
        recyclerview_products.setLayoutManager(layoutManager2);
        recyclerview_products.addItemDecoration(new AllKeys.GridSpacingItemDecoration(2, AllKeys.dpToPx(2, context), true));


      /*  LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerview_products.setLayoutManager(linearLayoutManager);
        recyclerview_products.setItemAnimator(new DefaultItemAnimator());*/

        // final List<Contact> allContacts = Contact.createContactsList(10, 0);
        // final ContactsAdapter adapter = new ContactsAdapter(allContacts);
        //  recyclerview_products.setAdapter(adapter);


        // Retain an instance so that you can call `resetState()` for fresh searches
      /*  scrollListener = new EndlessRecyclerViewScrollListener(layoutManager2) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list


                //Toast.makeText(context, "Page No : "+page, Toast.LENGTH_SHORT).show();
                if (TOTAL_PAGES > PAGENO) {
                    //loadNextDataFromApi(page);
                    ++PAGENO;


                    if (PAGENO * OFFSET < TOTAL_RECORDS) {
                        //getAllProductDetailsFromServer();
                       // Toast.makeText(context, "Total " + PAGENO * OFFSET + " of " + TOTAL_RECORDS + " Products", Toast.LENGTH_SHORT).show();
                    }


                }


            }
        };*/
        // Adds the scroll listener to RecyclerView
        // recyclerview_products.addOnScrollListener(scrollListener);
        recyclerview_products.setHasFixedSize(false);
        recyclerview_products.getLayoutManager().setAutoMeasureEnabled(true);
        // recyclerview_products.setNestedScrollingEnabled(false);


        recyclerview_products.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fab.isShown()) {
                    fab.hide();
                    //  cvSorting.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                    //cvSorting.setVisibility(View.VISIBLE);
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });


        recyclerview_products.addOnItemTouchListener(new AllKeys.RecyclerTouchListener(context, recyclerview_products, new AllKeys.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                try {
                    //Toast.makeText(context, "Name : " + list_Products.get(position).getProductname() + " Id : " + list_Products.get(position).getProductid(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context,
                            SingleItemActivity.class);
                    sessionmanager.setProductDetails(list_Products.get(position).getProductid(), userDetails.get(SessionManager.KEY_WISHLIST_STATUS), list_Products.get(position).getDescription(), list_Products.get(position).getImage_url(), "0");


                    intent.putExtra("ProductId", list_Products.get(position).getProductid());
                    intent.putExtra("ActivityName", TAG);

                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        //   LoadDataFromServer();


    }


    private void LoadDataFromServer() {


        list_Products.clear();
        // PAGENO = 0;
        // TOTAL_PAGES = 1;
        // TOTAL_RECORDS = 0;

        //getAllProductDetailsFromServer();

        if (!userDetails.get(SessionManager.KEY_USER_ID).equals("0")) {

            // getAllWishListDetailsFromServer();
            getAllProductDetailsFromServer();
              getAllCartItemDetailsFromServer();

            // getProductSizeMasterDataFromAPI();
        } else {


            // getAllWishListDetailsFromServer();
            getAllProductDetailsFromServer();
            getAllCartItemDetailsFromServer();

            //getProductSizeMasterDataFromAPI();
        }
    }


    private void getAllCartItemDetailsFromServer()
    {

        hideNoInternetUI();
        showDialog();

        String url_addtowishlist = AllKeys.WEBSITE + "ViewCartData?type=cartdata&userid=" + userDetails.get(SessionManager.KEY_USER_ID) + "";
        Log.d(TAG, "URL ViewCartData : " + url_addtowishlist);
        JsonObjectRequest str_manageCart = new JsonObjectRequest(Request.Method.GET, url_addtowishlist, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "Response str_manageCart : " + response);

                list_cartItemsId.clear();
                try {
                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);

                    if (error_status == false) {
                        if (record_status == true) {


                            JSONArray arr = response.getJSONArray(AllKeys.ARRAY_CART_DATA);

                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject c = arr.getJSONObject(i);

                                list_cartItemsId.add(c.getString(AllKeys.TAG_PRODUCT_ID));


                            }
                            setAddToCartBadget();
                        }

                    }
                } catch (JSONException e) {
                    setAddToCartBadget();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof ServerError || error instanceof NetworkError) {
                    hideNoInternetUI();
                    showInternetUI();
                } else {
                    getAllCartItemDetailsFromServer();
                }

                setAddToCartBadget();

                Log.d(TAG, "Error in ManageWishList : " + error.getMessage());

            }
        });
        MyApplication.getInstance().addToRequestQueue(str_manageCart);


    }


    private void showInternetUI() {

        ll_mainUI.setVisibility(View.GONE);
        ll_no_internet.setVisibility(View.VISIBLE);
    }

    private void hideNoInternetUI() {


        ll_mainUI.setVisibility(View.VISIBLE);
        ll_no_internet.setVisibility(View.GONE);

    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`

    }


    @Override
    public void onRefresh() {
        if (NetConnectivity.isOnline(context)) {
            //new AsyncData1().execute();
            //  swipeRefreshLayout.setColorSchemeColors(R.color.color1, R.color.color2, R.color.color3);
            //    swipeRefreshLayout.setRefreshing(true);
            //swipeRefreshLayout.setColorSchemeColors(R.color.color1, R.color.color2, R.color.color3);


            Log.d("swipe from ", "onrefresh method");

            LoadDataFromServer();


        } else {
            Toast.makeText(context, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
        }
    }






    /*Complete Setup Expandable ListView*//*
    private void getAllCartItemDetailsFromServer()
    {

        hideNoInternetUI();
        showDialog();

        String url = AllKeys.WEBSITE + "getCartDataByUserid/" + userDetails.get(SessionManager.KEY_USER_ID) + "";
        url = AllKeys.WEBSITE + "ViewCartData?type=cartdata&userid=" + userDetails.get(SessionManager.KEY_USER_ID) + "";
        Log.d(TAG, "URL ViewCartData : " + url);
        JsonObjectRequest str_manageCart = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {

                Log.d(TAG, "Response ViewCartData : " + response);


                try {
                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);
                    list_cartItemsId.clear();

                    if (error_status == false)
                    {

                        if(record_status  ==true)
                        {
                            try
                            {

                                JSONArray arr = response.getJSONArray(AllKeys.ARRAY_DATA);


                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject c = arr.getJSONObject(i);

                                    list_cartItemsId.add(c.getString(AllKeys.TAG_CART_PRODUCTID));


                                }

                                if (list_cartItemsId.size() == 0) {


                                    sessionmanager.setCartItemsIdDetails("0");
                                } else {
                                    String data = list_cartItemsId.toString();

                                    data = data.substring(1, data.length() - 1);
                                    sessionmanager.setCartItemsIdDetails(data);
                                }

                                setAddToCartBadget();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }










                getAllProductDetailsFromServer();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof ServerError || error instanceof NetworkError) {
                    hideNoInternetUI();
                    showInternetUI();
                } else {
                    getAllCartItemDetailsFromServer();
                }

                setAddToCartBadget();

                Log.d(TAG, "Error in ViewCartData : " + error.getMessage());

            }
        });
        MyApplication.getInstance().addToRequestQueue(str_manageCart);


    }*/


    /**
     * get All Wishlist Details from server
     */
    private void getAllWishListDetailsFromServer() {
        hideNoInternetUI();
        String url_getProducts = AllKeys.WEBSITE + "getWishlistByUserid/" + userDetails.get(SessionManager.KEY_CATEGORY_ID) + "";


        Log.d(TAG, "URL get WishList: " + url_getProducts);
        StringRequest str_get_products = new StringRequest(Request.Method.GET, url_getProducts, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Response getAll Hot Deals Products : " + response);
                list_wishListProducts.clear();
                list_wishListProducts.add("items");

                if (response.contains("product_id")) {

                    try {

                        response = AllKeys.convertToJsonFormat(response);
                        JSONObject obj = new JSONObject(response);
                        JSONArray arr = obj.getJSONArray("data");

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject c = arr.getJSONObject(i);
                            list_wishListProducts.add(c.getString("satish"));

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        hideDialog();
                    }


                }

                hideDialog();
                getAllProductDetailsFromServer();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof ServerError || error instanceof NetworkError) {
                    hideDialog();
                    showInternetUI();
                } else {
                    getAllWishListDetailsFromServer();
                }


                Log.d(TAG, "Error in get WishList : " + error.getMessage());
                hideDialog();

            }
        });
        MyApplication.getInstance().addToRequestQueue(str_get_products);
    }

    /**
     * get All Product Details From Server
     */
    // Send an API request to retrieve appropriate paginated data
    //  --> Send the request including an offset value (i.e `page`) as a query parameter.
    //  --> Deserialize and construct new model objects from the API response
    //  --> Append the new data objects to the existing set of items inside the array of items
    //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
    private void getAllProductDetailsFromServer() {


        showDialog();
        hideNoInternetUI();


        String url_getProducts = "";
        String sizes = list_selected_productSize.toString();
        sizes = sizes.replace(", ", ",");
        Log.d(TAG, "Sizes Before : " + sizes);
        sizes = sizes.substring(1, sizes.length() - 1);
        Log.d(TAG, "Sizes After : " + sizes);
        sizes = AllKeys.convertEncodedString(sizes);


        url_getProducts = AllKeys.WEBSITE + "ViewProduct?type=product&categoryid=" + userDetails.get(SessionManager.KEY_CATEGORY_ID) + "";


        Log.d(TAG, "URL GetJSONForAllProductData : " + url_getProducts);
        JsonObjectRequest str_get_products = new JsonObjectRequest(Request.Method.GET, url_getProducts, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "Response GetJSONForAllProductData   : " + response);


                try {
                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);


                    if (error_status == false) {


                        if (record_status == true) {



                           /* if (PAGENO == 0) {

                                list_Products.clear();
                            }
*/



                           /* try
                            {


                                try {
                                    TOTAL_RECORDS = response.getInt(AllKeys.TAG_TOTAL_RECORDS);
                                } catch (NumberFormatException e) {

                                    e.printStackTrace();
                                }

                                TOTAL_PAGES = TOTAL_RECORDS / OFFSET;
                                if ((TOTAL_PAGES % OFFSET) != 0) {
                                    ++TOTAL_PAGES;
                                }


                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }*/


                            try {

                                JSONArray arr = response.getJSONArray(AllKeys.ARRAY_DATA);

                                ArrayList<ProductData> list_current_Products = new ArrayList<ProductData>();

                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject c = arr.getJSONObject(i);
                                    String image_url;


                                    image_url = c.getString(AllKeys.TAG_PRODUCT_IMAGE);
                                    Log.d(TAG, "Image Path : " + image_url);
                                    //http://www.yelona.com/uploads/original/97e55a825f7206dad3faea31fb3533fe.jpg


                                    //  public ProductData(String shoppingcost, String productid, String productname, String description, String category_id, String image_url, String seller_id, String price, String offer_value, String offer_tag, String color, String weight, String height, String length, String width, String size, String mrp, String ship_date, String brand, String inventory, String committed_qty) {
                                    Double mrp_rs = c.getDouble(AllKeys.TAG_PRODUCT_MRP);
                                    Double price_rs = c.getDouble(AllKeys.TAG_PRODUCT_PRICE);


                                    if (MAXIMUM_VALUE_IN_RS < price_rs.intValue()) {
                                        MAXIMUM_VALUE_IN_RS = price_rs.intValue();
                                    }


                                    //String productid, String category_id, String image_url, String price,  String mrp,  String productname, String description, String quantity, boolean selectionStatus) {
                                    ProductData prod = new ProductData(c.getString(AllKeys.TAG_PRODUCT_ID), userDetails.get(SessionManager.KEY_CATEGORY_ID), image_url, c.getString(AllKeys.TAG_PRODUCT_PRICE), c.getString(AllKeys.TAG_PRODUCT_MRP), c.getString(AllKeys.TAG_PRODUCT_NAME), c.getString(AllKeys.TAG_PRODUCT_DESCR), "0", false);
                                    list_current_Products.add(prod);


                                }

                        /*        if (PAGENO != 0) {*/

                                    /*final int curSize = adapter_NewProduct.getItemCount();
                                    list_Products.addAll(list_current_Products);

                                    adapter_NewProduct.notifyItemRangeInserted(curSize, list_Products.size() - 1);*/


                                /*} else {*/
                                list_Products.addAll(list_current_Products);
                                adapter_NewProduct = new NewProductsRecyclerViewAdapter(context, list_Products, "grid", list_wishListProducts);
                                recyclerview_products.setAdapter(adapter_NewProduct);

                                /*}*/


                              /*  if (MAXIMUM_VALUE_IN_RS >= 5000) {
                                    price_rangebar.setTickEnd(MAXIMUM_VALUE_IN_RS);
                                } else {
                                    price_rangebar.setTickEnd(5000);
                                }*/


                            } catch (JSONException e) {
                                e.printStackTrace();
                                hideDialog();
                            }

                            txtnodata.setVisibility(View.GONE);
                            recyclerview_products.setVisibility(View.VISIBLE);


                        } else {

                            txtnodata.setVisibility(View.VISIBLE);
                            recyclerview_products.setVisibility(View.GONE);
                            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(context, str_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                hideDialog();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                try {
                    throw new RuntimeException(error.getMessage());
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }


                Log.d(TAG, "Error in get  ItemsData : " + error.getMessage());

                if (error instanceof ServerError || error instanceof NetworkError) {
                    showInternetUI();
                    hideDialog();
                } else {

                    getAllProductDetailsFromServer();
                }


            }
        });
        MyApplication.getInstance().addToRequestQueue(str_get_products);

    }


/*

    private void getSelectedProductDetailsFromServer()
    {


        showDialog();
        hideNoInternetUI();


        String url_getProducts = "";
        String sizes = list_selected_productSize.toString();
        sizes = sizes.replace(", ", ",");
        Log.d(TAG, "Sizes Before : " + sizes);
        sizes = sizes.substring(1, sizes.length() - 1);
        Log.d(TAG, "Sizes After : " + sizes);
        sizes = AllKeys.convertEncodedString(sizes);


        if (Is_Price_Range_Enalbe == true)
        {


            url_getProducts = AllKeys.WEBSITE + "GetJSONForAllProductData/" + userDetails.get(SessionManager.KEY_CATEGORY_ID) + "/" + MINIMUM_VALUE_IN_RS + "/" + SELECTED_MAXIMUM_VALUE + "/" + OFFSET + "/" + PAGENO + "/nosorting/" + Is_Price_Range_Enalbe + "/" + sizes + "";

        } else
        {



                url_getProducts = AllKeys.WEBSITE + "GetJSONForAllProductData?type=AllProductData&catid=" + userDetails.get(SessionManager.KEY_CATEGORY_ID) + "";



        }


        Log.d(TAG, "URL GetJSONForAllProductData : " + url_getProducts);
        JsonObjectRequest str_get_products = new JsonObjectRequest(Request.Method.GET, url_getProducts,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "Response GetJSONForAllProductData   : " + response);




                try
                {
                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);


                    if (error_status == false)
                    {


                        if(record_status == true)
                        {



                            if (PAGENO == 0) {

                                list_Products.clear();
                            }




                            try
                            {


                                try {
                                    TOTAL_RECORDS = response.getInt(AllKeys.TAG_TOTAL_RECORDS);
                                } catch (NumberFormatException e) {

                                    e.printStackTrace();
                                }

                                TOTAL_PAGES = TOTAL_RECORDS / OFFSET;
                                if ((TOTAL_PAGES % OFFSET) != 0) {
                                    ++TOTAL_PAGES;
                                }


                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }


                            try
                            {

                                JSONArray arr = response.getJSONArray(AllKeys.ARRAY_DATA);

                                ArrayList<ProductData> list_current_Products = new ArrayList<ProductData>();

                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject c = arr.getJSONObject(i);
                                    String image_url;


                                    image_url = c.getString(AllKeys.TAG_PRODUCT_IMAGE);
                                    Log.d(TAG, "Image Path : " + image_url);
                                    //http://www.yelona.com/uploads/original/97e55a825f7206dad3faea31fb3533fe.jpg


                                    //  public ProductData(String shoppingcost, String productid, String productname, String description, String category_id, String image_url, String seller_id, String price, String offer_value, String offer_tag, String color, String weight, String height, String length, String width, String size, String mrp, String ship_date, String brand, String inventory, String committed_qty) {
                                    Double mrp_rs = c.getDouble(AllKeys.TAG_PRODUCT_MRP);
                                    Double price_rs = c.getDouble(AllKeys.TAG_PRODUCT_PRICE);


                                    if (MAXIMUM_VALUE_IN_RS < price_rs.intValue()) {
                                        MAXIMUM_VALUE_IN_RS = price_rs.intValue();
                                    }





                                    ProductData prod = new ProductData(c.getString(AllKeys.TAG_PRODUCT_ID), c.getString(AllKeys.TAG_CATEOGRYNAME), image_url, c.getString(AllKeys.TAG_REAL_PRICE), c.getString(AllKeys.TAG_OFFER_PRICE), c.getString(AllKeys.TAG_REAL_PRICE), c.getString(AllKeys.TAG_UNIT), c.getString(AllKeys.TAG_UNIT), c.getString(AllKeys.TAG_PRODUCT_NAME), c.getString(AllKeys.TAG_LONGDESCR), "0", false);
                                    list_current_Products.add(prod);


                                }

                                if (PAGENO != 0) {

                                    final int curSize = adapter_NewProduct.getItemCount();
                                    list_Products.addAll(list_current_Products);

                                    adapter_NewProduct.notifyItemRangeInserted(curSize, list_Products.size() - 1);


                                } else {
                                    list_Products.addAll(list_current_Products);
                                    adapter_NewProduct = new NewProductsRecyclerViewAdapter(context, list_Products, "grid", list_wishListProducts);
                                    recyclerview_products.setAdapter(adapter_NewProduct);

                                }


                                if (MAXIMUM_VALUE_IN_RS >= 5000) {
                                    price_rangebar.setTickEnd(MAXIMUM_VALUE_IN_RS);
                                } else {
                                    price_rangebar.setTickEnd(5000);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                hideDialog();
                            }

                            txtnodata.setVisibility(View.GONE);
                            recyclerview_products.setVisibility(View.VISIBLE);




                        }
                        else
                        {

                            txtnodata.setVisibility(View.VISIBLE);
                            recyclerview_products.setVisibility(View.GONE);
                            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(context, str_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }





                hideDialog();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                try {
                    throw new RuntimeException(error.getMessage());
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }


                Log.d(TAG, "Error in get  ItemsData : " + error.getMessage());

                if (error instanceof ServerError || error instanceof NetworkError) {
                    showInternetUI();
                    hideDialog();
                } else {

                    getSelectedProductDetailsFromServer();
                }


            }
        });
        MyApplication.getInstance().addToRequestQueue(str_get_products);

    }
*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        try {

            this.menu = menu;
            getMenuInflater().inflate(R.menu.item_display, menu);
            cart = (MenuItem) menu.findItem(R.id.menu_addtocart);

            LayerDrawable icon = (LayerDrawable) cart.getIcon();

            LinkedHashMap<String, String> lhm = new LinkedHashMap<String, String>();
            menu_displayview = (MenuItem) menu.findItem(R.id.menu_displayview);


//int orderid=db.getMaxOrderId();


            //  String query = "select * from " + db.TABLE_ORDER + " where " + db.ORDER_ORDERID + "=" + userDetails.get(SessionManager.KEY_ORDERID) + "";


            //  Cursor cc = sd.rawQuery(query, null);
            // numorder = cc.getCount();
            //sessionmanager.StoreOrderDetails(""+numorder);
            // System.out.println("" + numorder);

            Utils.setBadgeCount(ItemDisplayActivity.this, icon, list_cartItemsId.size());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        //getMenuInflater().inflate(R.menu.item_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {


            if (userDetails.get(SessionManager.KEY_ACTIVITY_NAME).equals("SearchActivity"))

            {


                sessionmanager.setActivityName("");
                Intent launchNextActivity = new Intent(context, DashBoardActivity.class);
                /*launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);*/
                startActivity(launchNextActivity);
                finish();
            } else {

                //if(getIntent().getStringExtra(AllKeys.ACTIVITYNAME).equals("Category"))
                if (userDetails.get(SessionManager.KEY_CATEGORY_TYPE).equals("category")) {


                    Intent launchNextActivity = new Intent(context, CategoryAcitivity.class);
                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(launchNextActivity);
                    finish();

                }


            }


        } else if (item.getItemId() == R.id.menu_search) {
            sessionmanager.setActivityName(TAG);
            Intent intent = new Intent(context, SearchActivity.class);
            intent.putExtra("ActivityName", TAG);
            startActivity(intent);
            finish();

            //Toast.makeText(context, "Search Activity", Toast.LENGTH_SHORT).show();

        } else if (item.getItemId() == R.id.menu_whishlist) {
            /*Intent intent = new Intent(context, WishListActivity.class);
            intent.putExtra("ActivityName", TAG);
            startActivity(intent);
            finish();*/

            Toast.makeText(context, "Wishlist Activity", Toast.LENGTH_SHORT).show();

        } else if (item.getItemId() == R.id.menu_addtocart) {


            if(list_cartItemsId.size() != 0)
            {

                Intent intent = new Intent(context, CheckoutActivity.class);
                startActivity(intent);
                finish();

            }
            else
            {
                Toast.makeText(context, "Cart is empty", Toast.LENGTH_SHORT).show();
            }
          //  Toast.makeText(context, "Addtocart Activity", Toast.LENGTH_SHORT).show();

        } else if (item.getItemId() == R.id.menu_displayview) {

            if (IsGridData == true) {

                menu.getItem(3).setIcon(getResources().getDrawable(R.drawable.icon_grid));


                IsGridData = false;
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                recyclerview_products.setLayoutManager(layoutManager);
                txtDisplayData.setBackgroundResource(R.drawable.icon_grid);
                //adapter_NewProduct = new NewProductsRecyclerViewAdapter(context, list_Products, "list", list_wishListProducts);
                adapter_NewProduct = new NewProductsRecyclerViewAdapter(context, list_Products, "list", list_wishListProducts);
                recyclerview_products.setAdapter(adapter_NewProduct);


            } else {
                menu.getItem(3).setIcon(getResources().getDrawable(R.drawable.icon_list));
                adapter_NewProduct = new NewProductsRecyclerViewAdapter(context, list_Products, "grid", list_wishListProducts);
                recyclerview_products.setAdapter(adapter_NewProduct);

                txtDisplayData.setBackgroundResource(R.drawable.icon_list);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ItemDisplayActivity.this, 2);
                recyclerview_products.setLayoutManager(layoutManager);
                recyclerview_products.addItemDecoration(new AllKeys.GridSpacingItemDecoration(2, AllKeys.dpToPx(2, context), true));

                IsGridData = true;
            }


        }
        return super.onOptionsItemSelected(item);
    }

    private void setAddToCartBadget() {

        try {
            LayerDrawable icon = (LayerDrawable) cart.getIcon();

            Utils.setBadgeCount(ItemDisplayActivity.this, icon, list_cartItemsId.size());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onBackPressed() {


    /*    if (mSweetSheet.isShow())
        {
            if (mSweetSheet.isShow()) {
                mSweetSheet.dismiss();
            }

        } else
            {*/
        //  super.onBackPressed();


        // Toast.makeText(context, "Actname : "+getIntent().getStringExtra(AllKeys.ACTIVITYNAME), Toast.LENGTH_SHORT).show();

        if (userDetails.get(SessionManager.KEY_ACTIVITY_NAME).equals("SearchActivity"))

        {


            sessionmanager.setActivityName("");
            Intent launchNextActivity = new Intent(context, DashBoardActivity.class);
                /*launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);*/
            startActivity(launchNextActivity);
            finish();
        } else {


            Intent launchNextActivity = new Intent(context, CategoryAcitivity.class);
                /*launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);*/
            startActivity(launchNextActivity);
            finish();


        }



       /* }*/


    }


    public void showDialog() {


        try {
            if (!pDialog.isShowing()) {

                pDialog.show();
            }
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
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
           /* if(swipeRefreshLayout.isRefreshing())
            {
                swipeRefreshLayout.setRefreshing(false);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    /*Complete DIsplay Product Size UI*/

}
