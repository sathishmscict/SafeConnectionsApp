package com.safeconnectionsapp.helper;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class AllKeys {


    public static final String URL_CITYNAME_BY_PINCODE_CHECK = "https://www.whizapi.com/api/v2/util/ui/in/indian-city-by-postal-code?project-app-key=p75dteec5dvdymhkis18c5lk";


    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern
            .compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
                    + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
                    + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");
    public static final String ACTIVITYNAME = "ActivityName";
    public static final String ARRAY_PRODUCT_DATA ="Productdata" ;
    private static String TAG = AllKeys.class.getSimpleName();
    public static String ARRAY_CART_DATA="Cartdata";


    public static final boolean checkEmail(String email) {
        System.out.println("Email Validation:==>" + email);
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }


    public static final String WEBSITE = "http://safeconnexions.com/Service.asmx/";
    //public static final String WEBSITE = "http://arham.dnsitexperts.com/yelona/index.php/welcome/";//http://demo1.dnsitexperts.com/
    //public static final String WEBSITE = "http://19designs.org/yelona/index.php/welcome/";//http://demo1.dnsitexperts.com/


    public static final String RESOURSES = "https://www.shahagenciesindia.com/";
    public static final Integer MY_SOCKET_TIMEOUT_MS = 60000;


    //  //UploadDocument Realted KEYS
    public static final String TAG_UPLOAD_IMAGES_ARRAY = "Data";
    public static final String TAG_UPLOAD_IMAGE_NAME = "IMAGE_NAME";
    public static final String TAG_UPLOAD_IMAGEURL = "visitingcardfronturl";


    //Common Keys
    public static final String TAG_MESSAGE = "MESSAGE";
    public static final String TAG_ERROR_ORIGINAL = "ORIGINAL_ERROR";
    public static final String TAG_ERROR_STATUS = "ERROR_STATUS";
    public static final String TAG_IS_RECORDS = "RECORDS";

    //GetJSONForCategoryData Related Keys
  //  public static final String ARRAY_CATEGORY = "categorydata";
    public static final String TAG_CATEGORYID="categoryid";
    public static final String TAG_CATEGORY_NAME = "categoryname";
    public static final String TAG_CATEGORY_IMAGE = "categoryimage";
    //public static final String TAG_PARENTID="p_id";

    public static final String TAG_TOTAL_SUB_CATEGORIES = "TotalSubCategory";


    //Product Realted data
    public static final String TAG_PRODUCT_ID="productid";
    public static final String TAG_PRODUCT_NAME="productname";
    public static final String TAG_PRODUCT_DESCR="productdescription";
    public static final String TAG_PRODUCT_MRP="mrp";
    public static final String TAG_PRODUCT_PRICE="price";
    public static final String TAG_PRODUCT_IMAGE="productimage";


    //Cart Related keys


    public static final String TAG_PRODUCT_QUANTITY= "product_qty";
    //public static final String TAG_DATE= "date";









    //LoginData Related keys
    public static final String ARRAY_DATA = "Data";
    public static final String TAG_USERID = "userid";
    public static final String TAG_NAME = "name";
    public static final String TAG_MOBILENO = "mobile";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_ADDRESS = "address";
    public static final String TAG_IS_NEW_USER = "isfirsttime";
    public static final String TAG_REFERRAL_CODE = "referralcode";

    public static final String TAG_BANNER_IMAGE_URL = "image";

    //ViewMenus related keys
    public static final String TAG_MENUID = "serviceid";
    public static final String TAG_IMAGE_URL = "symbol";
    public static final String TAG_MENU_TITLE = "title";

	/* Social Invite Default Messaged */

    public static final String TAG_FACEBOOK_MSG = "fbmsg";
    public static final String TAG_TWITTER_MSG = "twmsg";
    public static final String TAG_GOOGLE_PLUS_MSG = "gmsg";
    public static final String TAG_GMAIL_MSG = "mailmsg";
    public static final String TAG_WHATSAPP_MSG = "wtsmsg";

    //Referral list dertails from server

    public static final String REFERRAL_NAME = "name";
    public static final String REFERRAL_MOBILE = "mobile";
    public static final String REFERRAL_DATE = "date";
    public static final String REFERRAL_TIME = "time";

    public static final String REFERRAL_POINTS = "Points";


    //Complaint Related Data
    public static final String TAG_COMPLAINT_ID = "servicedetailid";
    public static final String TAG_DATE = "date";
    public static final String TAG_TIME = "time";
    public static final String TAG_DESCRIPTION = "description";
    public static final String TAG_COMPLAINT_CATEGORYNAME = "service";


    //Quotation related data
    public static final String TAG_QUOTATION_ID="quotationdetailid";
    public static final String TAG_QUOTATION_SERVICE="service";
    /*public static final String TAG_="date";
    public static final String TAG_="time";
    public static final String TAG_="description";*/



    //Referal Rewards Related DAta
    public static final String ARRAY_REWARDS="Rewards";
    public static final String TAG_RR_DEFAULT_POINTS = "DEFAULT_POINTS";
    public static final String TAG_RR_REFERAAL_POINTS = "REFERRAL_POINTS";
    public static final String TAG_RR_REFERRAL_CAPPING = "REFERRAL_CAPPING";
    public static final String TAG_RR_PAYABLE_AMOUNT = "PAYABLE_AMOUNT";
    public static final String TAG_RR_REFERRAL_LIMIT = "REFERRAL_LIMIT";
    public static final String TAG_RR_KEY = "KEY";
    public static final String TAG_RR_VISIT_CHARGE = "VISIT_CHARGE";

    //Order Realted keys
    public static final String TAG_ORDER_ID="OrderId";
    public static final String TAG_TOTAL_AMT="totalAmt";













    /**
     * Converting dp to pixel
     */
    public static int dpToPx(int dp, Context context) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


    public static final String convertToJsonDateFormat(String cur_date) {

        Log.d("Passed Date : ", cur_date);
        SimpleDateFormat dateFormat = null;
        Date date = null;
        try {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd",
                    Locale.getDefault());

//String string = "January 2, 2010";
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            date = format.parse(cur_date);
            System.out.println(date);
        } catch (Exception e) {
            Log.d("Convert DataFormat :: ", e.getMessage());
        }


        //Date date = new Date();

        return dateFormat.format(date);


    }


    public static final String convertToAppDateFormat(String cur_date) {

        Log.d("Passed Date : ", cur_date);
        SimpleDateFormat dateFormat = null;
        Date date = null;
        try {
            dateFormat = new SimpleDateFormat("dd-MM-yyyy",
                    Locale.getDefault());

//String string = "January 2, 2010";
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            date = format.parse(cur_date);
            System.out.println(date);
        } catch (Exception e) {
            Log.d("Convert DataFormat :", e.getMessage());
        }


        //Date date = new Date();

        return dateFormat.format(date);


    }

    //Methods
    public static String convertToJsonFormat(String json_data) {

        String response = "{\"data\":" + json_data + "}";
        return response;

    }

    public static final String convertEncodedString(String str) {
        String enoded_string = null;
        try {
            enoded_string = URLEncoder.encode(str, "utf-8").replace(".", "%2E");
            enoded_string = URLEncoder.encode(str, "utf-8").replace("+", "%20");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return enoded_string;
    }

    public static String getMonth(int month) {
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }


    public static final String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());


        Date date = new Date();

        Log.d(TAG, "Current Date and Time :" + dateFormat.format(date).toString());

        return dateFormat.format(date);
    }


    public static String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.d("Encoded String : ", encodedImage);
        return encodedImage;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        Log.d("Decoded String   : ", "" + BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length));
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }


}
