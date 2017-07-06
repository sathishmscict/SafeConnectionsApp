package com.safeconnectionsapp.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.safeconnectionsapp.LoginActivity;

import java.util.HashMap;

/**
 * Created by SATHISH on 28-Jun-17.
 */

public class SessionManager {

    //Shared preferences
    SharedPreferences pref;

    //Editor for shared preferences
    SharedPreferences.Editor editor;

    //Context
    Context _context;


    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "SafeConncetionPref";


    public static final String KEY_CODE = "code", KEY_SMSURL = "smsurl",
            KEY_RECEIVECODE = "reccode",
            KEY_VERSTATUS = "verification_status";

    public static final String KEY_SHIPPED_DAYS = "ShippedDaya", KEY_CHECKOUT_TYPE = "CheckoutType";
    public static final String KEY_PRODUCT_ID = "ProductId";

    public static final String KEY_WISHLIST_STATUS = "WishListStatus";
    public static final String KEY_PRODUCT_DESCR = "ProductDescr";
    public static final String KEY_PRODUCT_IMAGE_URL = "ProductImageURL", KEY_PRODUCT_RATING = "ProductRating";

    public static final String KEY_PRICE_TOTAL = "PriceTotal", KEY_GRAND_TOTAL = "GrandTotal", KEY_DELIVERY_CHARGES_TOTAL = "TotalDeliveryCharges", KEY_TOTAL_ITEMS = "totalItems", KEY_MLM_DISCOUNT = "totalMLMDiscount";



    public static final String KEY_CARTITEMS_ID = "CartedProductIds";


    public static final String KEY_USER_ID = "UserId", KEY_USER_NAME = "UserName", KEY_USER_EMAIL = "UserEmail", KEY_USER_MOBILE = "UserMobile", KEY_USER_ADDRESS = "userAddress";

    public static final String KEY_REQUEST_TYPE = "RequestType", KEY_IS_NEWUSER = "IsNewUser", KEY_REFERAL_CODE = "ReferralCode", KEY_GENDER = "Gender";

    public static final String KEY_ENODEDED_STRING = "Encoded_string";


    public static final String KEY_DEFAULL_POINTS = "default_points";
    public static final String KEY_REFERAL_POINTS = "referal_points";
    public static final String KEY_REFERAL_CAPPING = "referal_capping";
    public static final String KEY_REFERAL_LIMIT = "referal_limit";
    public static final String KEY_PAYABLE_AMOUNT = "payamt";
    public static final String KEY_ENCRYPTION = "key";
    public static final String KEY_VISITING_CHARGE = "VisitiCharge";

    public static final String KEY_WHATSAPP_MSG = "whatsappmsg";

    public static final String KEY_FACEBOOK_MSG = "facebookmsg";

    public static final String KEY_TWITTER_MSG = "twittwermsg";

    public static final String KEY_GPLUS_MSG = "gplusmag";

    public static final String KEY_GMAIL_MSG = "gmailmsg";

    public static final String KEY_CATEGORY_ID = "CategoryID", KEY_CATEGORY_TYPE = "CategoryType";
    public static final String KEY_CATEGORY_NAME = "CategoryName";

    public static final String KEY_NEWACTIVITY_NAME = "newActivityName";

    public static final String KEY_ACTIVITY_NAME = "ActivityName";


    public void setActivityName(String activityName) {
        editor.putString(KEY_ACTIVITY_NAME, activityName);
        editor.commit();

    }


    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }

    public void CheckSMSVerificationActivity(String reccode, String actstatus) {

        editor.putString(KEY_RECEIVECODE, reccode);
        editor.putString(KEY_VERSTATUS, actstatus);
        editor.commit();

    }
    public void setCheckoutType(String checkoutType, String productId) {

        editor.putString(KEY_CHECKOUT_TYPE, checkoutType);
        editor.putString(KEY_PRODUCT_ID, productId);
        editor.commit();
    }


    public void createUserSendSmsUrl(String code, String websiteurl) {

        editor.putString(KEY_CODE, code);
        editor.putString(KEY_SMSURL, websiteurl);// http://radiant.dnsitexperts.com/JSON_Data.aspx?type=otp&mobile=9825681802&code=7692
        editor.commit();

    }


    public void setVisitingCharge(String visitingcharge)
     {
        editor.putString(KEY_VISITING_CHARGE, visitingcharge);
         editor.commit();


    }

    public void createPayments(String defaultpoints, String referalpoints, String referalcaping, String referallimit, String payableamt, String encr) {
        // Storing login value as TRUE
        //  editor.putBoolean(IS_LOGIN, true);


        // Storing name in pref
        editor.putString(KEY_DEFAULL_POINTS, defaultpoints);
        editor.putString(KEY_REFERAL_POINTS, referalpoints);
        editor.putString(KEY_REFERAL_CAPPING, referalcaping);
        editor.putString(KEY_REFERAL_LIMIT, referallimit);
        editor.putString(KEY_PAYABLE_AMOUNT, payableamt);


        editor.putString(KEY_ENCRYPTION, encr);


        // commit changes
        editor.commit();
    }


    public void setCategoryTypeAndIdDetails(String categoryid, String categotyname, String categorytype) {

        editor.putString(KEY_CATEGORY_ID, categoryid);
        editor.putString(KEY_CATEGORY_NAME, categotyname);
        editor.putString(KEY_CATEGORY_TYPE, categorytype);


        editor.commit();

    }

    public void createSocialMessage(String whatsappmsg, String facebookmsg, String twittwermsg, String gplusmag, String gmailmsg) {
        // Storing login value as TRUE


        // Storing name in pref
        editor.putString(KEY_WHATSAPP_MSG, whatsappmsg);

        editor.putString(KEY_FACEBOOK_MSG, facebookmsg);

        editor.putString(KEY_TWITTER_MSG, twittwermsg);

        editor.putString(KEY_GPLUS_MSG, gplusmag);

        editor.putString(KEY_GMAIL_MSG, gmailmsg);


        // commit changes
        editor.commit();
    }


    public void setEncodedImage(String encodeo_image) {


        editor.putString(KEY_ENODEDED_STRING, encodeo_image);

        editor.commit();
    }

    public void setUserDetails(String userid, String name, String email, String mobile, String userAddres, String isNewUser, String referralCode, String gender) {


        editor.putString(KEY_USER_ID, userid);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_MOBILE, mobile);

        editor.putString(KEY_USER_ADDRESS, userAddres);
        editor.putString(KEY_IS_NEWUSER, isNewUser);
        editor.putString(KEY_REFERAL_CODE, referralCode);
        editor.putString(KEY_GENDER, gender);


        editor.commit();

    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    public void setRequestType(String requestType) {

        editor.putString(KEY_REQUEST_TYPE, requestType);
        editor.commit();
    }

    public void setNewUserSession(String newActivityName) {

        editor.putString(KEY_NEWACTIVITY_NAME, newActivityName);
        editor.commit();
    }



    public void setCartItemsIdDetails(String cartItemIds) {

        editor.putString(KEY_CARTITEMS_ID, cartItemIds);
        editor.commit();

    }

    public void setProductDetails(String productId, String wishListStatus, String descr, String productImageURL, String productRating) {



        editor.putString(KEY_PRODUCT_ID, productId);
        editor.putString(KEY_WISHLIST_STATUS, wishListStatus);
        editor.putString(KEY_PRODUCT_DESCR, descr);
        editor.putString(KEY_PRODUCT_IMAGE_URL, productImageURL);

        editor.putString(KEY_PRODUCT_RATING, productRating);
        editor.commit();

    }




    public void setOrderTotalDetails(String total_price, String grand_total, String total_delivery_charges, String total_items, String totalMLMdicsount) {



        editor.putString(KEY_PRICE_TOTAL, total_price);
        editor.putString(KEY_GRAND_TOTAL, grand_total);
        editor.putString(KEY_DELIVERY_CHARGES_TOTAL, total_delivery_charges);
        editor.putString(KEY_TOTAL_ITEMS, total_items);
        editor.putString(KEY_MLM_DISCOUNT, totalMLMdicsount);
        editor.commit();




    }


    public HashMap<String, String> getSessionDetails() {



        //KEY_FULLNAME="uerfullname",KEY_USERNAME="username",KEY_EMAIL="email",KEY_MOBILE="mobileno",KEY_PHONENO="phoneno",KEY_PASSWORD="password";
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_CARTITEMS_ID, pref.getString(KEY_CARTITEMS_ID, ""));

        user.put(KEY_CHECKOUT_TYPE, pref.getString(KEY_CHECKOUT_TYPE, ""));


        user.put(KEY_PRICE_TOTAL, pref.getString(KEY_PRICE_TOTAL, "0"));
        user.put(KEY_GRAND_TOTAL, pref.getString(KEY_GRAND_TOTAL, "0"));
        user.put(KEY_DELIVERY_CHARGES_TOTAL, pref.getString(KEY_DELIVERY_CHARGES_TOTAL, "0"));

        user.put(KEY_TOTAL_ITEMS, pref.getString(KEY_TOTAL_ITEMS, "0"));
        user.put(KEY_MLM_DISCOUNT, pref.getString(KEY_MLM_DISCOUNT, "0"));


        user.put(KEY_PRODUCT_ID, pref.getString(KEY_PRODUCT_ID, ""));

        user.put(KEY_PRODUCT_RATING, pref.getString(KEY_PRODUCT_RATING, ""));

        user.put(KEY_PRODUCT_DESCR, pref.getString(KEY_PRODUCT_DESCR, ""));
        user.put(KEY_PRODUCT_IMAGE_URL, pref.getString(KEY_PRODUCT_IMAGE_URL, ""));
        user.put(KEY_WISHLIST_STATUS, pref.getString(KEY_WISHLIST_STATUS, "-1"));

        user.put(KEY_SHIPPED_DAYS, pref.getString(KEY_SHIPPED_DAYS, "7"));

        user.put(KEY_VISITING_CHARGE, pref.getString(KEY_VISITING_CHARGE, "0"));
        user.put(KEY_ACTIVITY_NAME, pref.getString(KEY_ACTIVITY_NAME, ""));

        user.put(KEY_NEWACTIVITY_NAME, pref.getString(KEY_NEWACTIVITY_NAME, ""));

        user.put(KEY_CATEGORY_ID, pref.getString(KEY_CATEGORY_ID, "0"));
        user.put(KEY_CATEGORY_NAME, pref.getString(KEY_CATEGORY_NAME, ""));
        user.put(KEY_CATEGORY_TYPE, pref.getString(KEY_CATEGORY_TYPE, ""));


        user.put(KEY_ENODEDED_STRING, pref.getString(KEY_ENODEDED_STRING, ""));

        user.put(KEY_GENDER, pref.getString(KEY_GENDER, "1"));

        user.put(KEY_WHATSAPP_MSG, pref.getString(KEY_WHATSAPP_MSG, ""));

        user.put(KEY_FACEBOOK_MSG, pref.getString(KEY_FACEBOOK_MSG, ""));

        user.put(KEY_TWITTER_MSG, pref.getString(KEY_TWITTER_MSG, ""));

        user.put(KEY_GPLUS_MSG, pref.getString(KEY_GPLUS_MSG, ""));

        user.put(KEY_GMAIL_MSG, pref.getString(KEY_GMAIL_MSG, ""));


        user.put(KEY_DEFAULL_POINTS, pref.getString(KEY_DEFAULL_POINTS, "0"));
        user.put(KEY_REFERAL_CAPPING, pref.getString(KEY_REFERAL_CAPPING, "0"));
        user.put(KEY_REFERAL_LIMIT, pref.getString(KEY_REFERAL_LIMIT, "0"));
        user.put(KEY_ENCRYPTION, pref.getString(KEY_ENCRYPTION, "0"));
        user.put(KEY_REFERAL_POINTS, pref.getString(KEY_REFERAL_POINTS, "0"));
        user.put(KEY_PAYABLE_AMOUNT, pref.getString(KEY_PAYABLE_AMOUNT, "0"));


        user.put(KEY_USER_ADDRESS, pref.getString(KEY_USER_ADDRESS, ""));

        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, "0"));
        user.put(KEY_USER_NAME, pref.getString(KEY_USER_NAME, ""));
        user.put(KEY_USER_EMAIL, pref.getString(KEY_USER_EMAIL, ""));
        user.put(KEY_USER_MOBILE, pref.getString(KEY_USER_MOBILE, ""));


        user.put(KEY_RECEIVECODE, pref.getString(KEY_RECEIVECODE, "0"));
        user.put(KEY_CODE, pref.getString(KEY_CODE, "0"));

        user.put(KEY_SMSURL, pref.getString(KEY_SMSURL, ""));
        user.put(KEY_VERSTATUS, pref.getString(KEY_VERSTATUS, "0"));

        user.put(KEY_REQUEST_TYPE, pref.getString(KEY_REQUEST_TYPE, ""));

        user.put(KEY_IS_NEWUSER, pref.getString(KEY_IS_NEWUSER, ""));

        user.put(KEY_REFERAL_CODE, pref.getString(KEY_REFERAL_CODE, ""));


        return user;
    }


}
