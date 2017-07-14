package com.safeconnectionsapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.safeconnectionsapp.MenuActivity;
import com.safeconnectionsapp.R;
import com.safeconnectionsapp.app.MyApplication;
import com.safeconnectionsapp.helper.AllKeys;
import com.safeconnectionsapp.session.SessionManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;


public class HomeFragment extends Fragment{


    private Context context = getActivity();
    private HashMap<String, String> userDetails = new HashMap<String, String>();


    private SQLiteDatabase sd;

    private int count = 1;
    private TextView nodata;


    private String TAG = HomeFragment.class.getSimpleName();
    private SpotsDialog pDialog;


    private SessionManager sessionManager;

    HashMap<String, String> url_maps = new HashMap<String, String>();

    private ArrayList<Integer> list_SliderId = new ArrayList<Integer>();
    private ArrayList<String> list_SliderImages = new ArrayList<String>();

    private ArrayList<String> list_SliderCategoryURL = new ArrayList<String>();

    private ImageView imgPickup;
    private TextView txtwait;
    private LinearLayout llQuotation;
    private LinearLayout llComplain;
    private SliderLayout mDemoSlider;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        sessionManager = new SessionManager(getActivity());
        userDetails = sessionManager.getSessionDetails();



        pDialog =  new SpotsDialog(getActivity());
        pDialog.setCancelable(false);

        mDemoSlider = (SliderLayout) rootView.findViewById(R.id.final_slider1);

        llQuotation = (LinearLayout) rootView.findViewById(R.id.llQuotation);
        llComplain = (LinearLayout) rootView.findViewById(R.id.llComplain);

        llQuotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sessionManager.setRequestType("quotation");

                Intent intent = new Intent(getActivity(), MenuActivity.class);
                startActivity(intent);
                getActivity().finish();
              //  Toast.makeText(getActivity(), "Quotation...", Toast.LENGTH_SHORT).show();
            }
        });

        llComplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.setRequestType("complaint");

                Intent intent = new Intent(getActivity(), MenuActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        getSlidersByTypeDetailsFromServer();

        return rootView;

    }

    private void getSlidersByTypeDetailsFromServer() {
        showDialog();
        final String url_getSldierByType = AllKeys.WEBSITE + "ViewBanner?type=banner";
        Log.d(TAG, "url ViewBanner : " + url_getSldierByType);
        StringRequest str_getSlidersByType = new StringRequest(Request.Method.GET, url_getSldierByType, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Resposne ViewBanner : " + response);


                if (response.contains("image"))
                {



                        try {
                            //response = AllKeys.convertToJsonFormat(response);
                            JSONObject obj = new JSONObject(response);
                            JSONArray arr = obj.getJSONArray(AllKeys.ARRAY_DATA);
                            url_maps.clear();
                            list_SliderId.clear();
                            list_SliderImages.clear();

                            list_SliderCategoryURL.clear();
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject c = arr.getJSONObject(i);


                                String url_image_url = c.getString(AllKeys.TAG_BANNER_IMAGE_URL);
                                //  url_image_url = "http://www.yelona.com" + url_image_url;
                                //url_maps.put(c.getString(AllKeys.TAG_BANNER_IMAGE), url_image_url);
                                url_maps.put(String.valueOf(++i), url_image_url);

                              //  list_SliderCategoryURL.add(c.getString(AllKeys.TAG_BANNER_CATEGORYLINK));

                                list_SliderId.add(i);
                                list_SliderImages.add(url_image_url);


                            }

                            FillDashBoardSliders();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            hideDialog();
                        }





                }


                hideDialog();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(TAG, "Error getSlidersByType : " + error.getMessage());
                if (error instanceof ServerError) {
                    hideDialog();
                } else {
                    getSlidersByTypeDetailsFromServer();

                }

            }

        });
    /*    str_getSlidersByType.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/

        MyApplication.getInstance().addToRequestQueue(str_getSlidersByType);

    }

    private void FillDashBoardSliders() {
        showDialog();

        //HashMap<String, String> url_maps = new HashMap<String, String>();

       /* url_maps.clear();
        url_maps.put("Hannibal", "http://www.jepsylifestyle.com/Photos/Banner/banner_02.jpg");
        url_maps.put("Big Bang Theory", "http://www.jepsylifestyle.com/images/thumb/t-3.jpg");
        url_maps.put("House of Cards", "https://s-media-cache-ak0.pinimg.com/originals/d2/3f/a0/d23fa03aefa1f661405548750500c01a.jpg");
        url_maps.put("Game of Thrones", "http://media.indipepper.com/2014/05/1394024215326-BeSummerReady_980x459_980_459_mini-770x459.jpg");
*/
       /* url_maps.clear();
        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal", R.drawable.slider_1);
        file_maps.put("Big Bang Theory", R.drawable.bigbang);
        file_maps.put("House of Cards", R.drawable.slider_1);
        file_maps.put("Game of Thrones", R.drawable.game_of_thrones);*/

      /*  try {
            url_maps.put("sda", list_otherSliders.get(3));
            url_maps.put("sda", list_otherSliders.get(4));
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    /*.image(url_maps.get(name))*/
                    .setScaleType(BaseSliderView.ScaleType.Fit);;
                    //.setOnSliderClickListener(getActivity());

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);


            String sd2 = list_SliderImages.get(list_SliderImages.indexOf(url_maps.get(name)));


            int sd = list_SliderId.indexOf(list_SliderImages.indexOf(url_maps.get(name)));

            try {
                mDemoSlider.addSlider(textSliderView);
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*if ((list_SliderId.get(list_SliderImages.indexOf(url_maps.get(name))) % 2) != 0) {
                mDemoSlider.addSlider(textSliderView);
            } else {
                mDemoSlider2.addSlider(textSliderView);

            }
*/

        }


      /*  for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
                 *//*   .setOnSliderClickListener(Newthis);*//*

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
            mDemoSlider2.addSlider(textSliderView);
        }*/

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.DepthPage);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(5000);




//        mDemoSlider.addOnPageChangeListener(ge);
        hideDialog();

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

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            pDialog.cancel();
            pDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Produc Review Related Pojo Class
     */


}
