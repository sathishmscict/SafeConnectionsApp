package com.safeconnectionsapp.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.safeconnectionsapp.R;
import com.safeconnectionsapp.adapter.ReferralListRecyclerView;
import com.safeconnectionsapp.app.MyApplication;
import com.safeconnectionsapp.helper.AllKeys;
import com.safeconnectionsapp.helper.NetConnectivity;
import com.safeconnectionsapp.pojo.ReferralList;
import com.safeconnectionsapp.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;

public class FragmentReferalList extends Fragment {

    private SessionManager session;
    private Context context = getActivity();
    private HashMap<String, String> userDetails;
    private LinearLayout bglayout;
    private int id = 0;

    //private SQLiteDatabase sd;

    Typeface fontFamily;

    List<String> SURVEYID;
    private Boolean LOADSURVEY = false;
    public String DATETIME;
    public String POINTS;
    private String FIRSTNAME;
    private String MOBILENUMBER, MEMBERSTATUS = "";


    private static final Pattern TRIM_SQL_PATTERN = Pattern
            .compile("\\\\\\\'.");


    ArrayList<HashMap<String, String>> ReferalDetails;
    private TextView txterror;
    private String TAG = FragmentReferalList.class.getSimpleName();
    private ListView list;
    private ArrayList<ReferralList> list_ReferralList = new ArrayList<ReferralList>();

    private SpotsDialog pDialog;
    private RecyclerView rv_ReferralList;

    public ReferralListRecyclerView adpater;


    public FragmentReferalList() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_referal_list, container,
                false);


        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //wa3002g4@aarti

/*		db = new dbhandler(getActivity());
        sd = db.getReadableDatabase();
		sd = db.getWritableDatabase();*/
        session = new SessionManager(getActivity());
        userDetails = new HashMap<String, String>();

        userDetails = session.getSessionDetails();


        pDialog = new SpotsDialog(getActivity());
        pDialog.setCancelable(false);

        ReferalDetails = new ArrayList<HashMap<String, String>>();


        bglayout = (LinearLayout) rootView.findViewById(R.id.ll1);

        txterror = (TextView) rootView.findViewById(R.id.textView1);


        list = (ListView) rootView.findViewById(R.id.list);

        rv_ReferralList = (RecyclerView) rootView.findViewById(R.id.rvReferralList);
        RecyclerView.LayoutManager lManager = new LinearLayoutManager(getActivity());
        rv_ReferralList.setLayoutManager(lManager);


        id = Integer.parseInt(userDetails.get(SessionManager.KEY_USER_ID));

        // id = 3;


        GetAllReferalListDetailsFromServer();


        // Inflate the layout for this fragment
        return rootView;
    }

    private void GetAllReferalListDetailsFromServer() {
        showDialog();
        String url = AllKeys.WEBSITE + "ViewReferral?type=referral&userid=" + userDetails.get(SessionManager.KEY_USER_ID) + "";
        // url = AllKeys.WEBSITE + "ViewReferral?type=referral&userid=" + 10 + "";
        Log.d(TAG, "URL ViewReferral : " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {


                try {
                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);

                    if (error_status == false) {
                        if (record_status == true)
                        {


                            JSONArray arr = response.getJSONArray(AllKeys.ARRAY_DATA);
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject c = arr.getJSONObject(i);

                                //ReferralList(String name, String price, String mobile, String datetime, String status) {
                                ReferralList rf = new ReferralList(c.getString(AllKeys.REFERRAL_NAME), "0", c.getString(AllKeys.REFERRAL_MOBILE), c.getString(AllKeys.REFERRAL_DATE) + " " + c.getString(AllKeys.REFERRAL_TIME), "1");

                                list_ReferralList.add(rf);


                            }

                            adpater = new ReferralListRecyclerView(getActivity(), list_ReferralList);
                            rv_ReferralList.setAdapter(adpater);


                            txterror.setVisibility(View.GONE);
                            rv_ReferralList.setVisibility(View.VISIBLE);

                            hideDialog();
                        }
                        else
                        {
                            txterror.setVisibility(View.VISIBLE);
                            rv_ReferralList.setVisibility(View.GONE);
                        }

                    } else {
                        hideDialog();
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
                    GetAllReferalListDetailsFromServer();

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




/*	private void FillDataonListView()
    {

		try {
			String query = "select * from "+ dbhandler.TABLE_REFERRAL_LIST +"";

			

			Cursor c = sd.rawQuery(query, null);



			list_referralList.clear();
			if (c.getCount() > 0) {
				
				txterror.setVisibility(View.GONE);



				while (c.moveToNext())
				{



					//ReferralList(String name, String price, String mobile, String datetime, String status) {
					ReferralList rf= new ReferralList("Reward for referring " + c.getString(c.getColumnIndex(dbhandler.REFERRAL_LIST_NAME))," \u20B9 "+ c.getString(c.getColumnIndex(dbhandler.REFERRAL_LIST_POINTS)),c.getString(c.getColumnIndex(dbhandler.REFERRAL_LIST_MOBILENO)),c.getString(c.getColumnIndex(dbhandler.REFERRAL_LIST_DATETIME)),c.getString(c.getColumnIndex(dbhandler.REFERRAL_LIST_MEMBER_STATUS)));

					list_referralList.add(rf);






				}

				if (!list_referralList.isEmpty()) {
					// adapter = null;
					try {

					*//*	ListAdapter	adapter = new SimpleAdapter(getActivity(), ReferalDetails,
								R.layout.row_referal_list, new String[] {
										 "NAME", "RUPEES", "MOBILE","DATETIME" }, new int[] { R.id.txtname,
										R.id.txtrs, R.id.txtmobile,
										R.id.txtdate});
						
						setListAdapter(adapter);
						list.setAdapter(adapter);*//*

						ReferralListRecyclerView new_adapter =new ReferralListRecyclerView(getActivity() , list_referralList);

						rvReferralList.setAdapter(new_adapter);


					} catch (Exception e) {
						e.printStackTrace();
					}
					
					//bglayout.setBackgroundColor(Color.parseColor("#FFFFFF"));

					//lst.setVisibility(View.VISIBLE);
				}
				else
				{
					//lst.setVisibility(View.GONE);
				}
			

				//backupDB();

			}
			else
			{
				//bglayout.setBackgroundResource(R.drawable.noresult);
				//bglayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
				txterror.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.print("Error  :"+e.getMessage());
			e.printStackTrace();
		}
	}*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    // /////////////////////////Getting All Survey Related
    // Details////////////////


    // ///Complete Getting Survey Details From The WebStite//////////////////

    private static String trimSqlForDisplay(String sql) {
        return TRIM_SQL_PATTERN.matcher(sql).replaceAll(" ");
    }


    // Code For Backup Current Database
/*	public void backupDB()
	{

		File sd = Environment.getExternalStorageDirectory();
		File data = Environment.getDataDirectory();
		FileChannel source = null;
		FileChannel destination = null;
		String currentDBPath = "/data/" + context.getPackageName() + "/databases/"
				+ db.databasename;
		String backupDBPath = db.databasename;
		File currentDB = new File(data, currentDBPath);
		File backupDB = new File(sd, backupDBPath);
		try {
			source = new FileInputStream(currentDB).getChannel();
			destination = new FileOutputStream(backupDB).getChannel();
			destination.transferFrom(source, 0, source.size());
			source.close();
			destination.close();
			Toast.makeText(getActivity(), "DataBase Exported!",
					Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/

    // Complete Method Backup database

    // /////////////////////////Getting All Referal Reward Related
    // Details////////////////

    /**
     * Async task class to get json by making HTTP call
     */
/*	private class GetAllReferalListDetails extends
			AsyncTask<Void, Void, Void>
	{

		private ProgressDialog pDialog;

		private JSONArray allreferalslist;

		 //http://paymyreview.com/getjson.aspx?action=refereallist&userid=1234
			 
		//private String referal_url = AllKeys.TAG_WEBSITE


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			 String referal_url = AllKeys.WEBSITE
					+ "GetReferralList?action=referrallist&userid="+ userDetails.get(SessionManager.KEY_USERID)+"&clientid="+ AllKeys.CLIENT_ID +"";
			Log.d(TAG, "URL GetReferralList : "+referal_url);




			// Making a request to url and getting response
			String review_str = sh.makeServiceCall(referal_url,
					ServiceHandler.GET);
			
			//review_str = "{\""+ AllKeys.REFERAL_LIST_ARRAY +"\":"+review_str+"}";

			Log.d("Response: ", ">"+ review_str);

			sd.delete(dbhandler.TABLE_REFERRAL_LIST, null, null);
			
			if (!review_str.equals("")) {
				try {
 					JSONObject jsonObj = null;
					try {
						jsonObj = new JSONObject(review_str);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					try {
						// Getting JSON Array node
						allreferalslist = jsonObj.getJSONArray(AllKeys.REFERAL_LIST_ARRAY);
						// Create Object of dbhandler
						System.out.println("Success");
					
						
					} catch (Exception e) {
						// System.out.print("Country Table Errror : "+e.getMessage());
						e.getMessage();
					}
					try {
						// looping through All Contacts
						for (int i = 0; i < allreferalslist.length(); i++) {
							JSONObject c = allreferalslist.getJSONObject(i);

							FIRSTNAME = "" + c.getString(AllKeys.REFERRAL_NAME);
							MOBILENUMBER = "" + c.getString(AllKeys.REFERRAL_MOBILE);
							DATETIME = "" + c.getString(AllKeys.REFERRAL_DATE);
							POINTS = "" + c.getString(AllKeys.REFERRAL_POINTS);
							MEMBERSTATUS = c.getString(AllKeys.REFERRAL_MEMBER_STATUS);


						*//*	ContentValues cv = new ContentValues();
							cv.put(dbhandler.REFERRAL_LIST_NAME,FIRSTNAME);
							cv.put(dbhandler.REFERRAL_LIST_MOBILENO,MOBILENUMBER);
							cv.put(dbhandler.REFERRAL_LIST_DATETIME,DATETIME);
							cv.put(dbhandler.REFERRAL_LIST_POINTS,POINTS);
							cv.put(dbhandler.REFERRAL_LIST_MEMBER_STATUS,MEMBERSTATUS);

							sd.insert(dbhandler.TABLE_REFERRAL_LIST ,null,cv);*//*


							//sd.execSQL("insert into "+ dbhandler.TABLE_REFERRAL_LIST +" values(null,'"+ FIRSTNAME +"','"+ MOBILENUMBER +"','"+ DATETIME +"','"+ POINTS +"')");
							
						}
					} catch (SQLException e) {
						
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {

				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();

			try {


				
				FillDataonListView();
				
			} catch (NumberFormatException e) {

				e.printStackTrace();
				System.out.print("Error : " + e.getMessage());

			}

		}
	}*/

    // ///Complete Getting Masters Details From The WebStite//////////////////


}