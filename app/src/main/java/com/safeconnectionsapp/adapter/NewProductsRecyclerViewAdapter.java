package com.safeconnectionsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.safeconnectionsapp.LoginActivity;
import com.safeconnectionsapp.R;



import com.safeconnectionsapp.app.MyApplication;

import com.safeconnectionsapp.helper.AllKeys;
import com.safeconnectionsapp.helper.CustomFonts;
import com.safeconnectionsapp.pojo.ProductData;
import com.safeconnectionsapp.session.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sathish Gadde on 30-Jan-17.
 */

public class NewProductsRecyclerViewAdapter extends RecyclerView.Adapter<NewProductsRecyclerViewAdapter.MyViewHolder> {


    private final Context _context;
    private final ArrayList<ProductData> list_NewProcuts;
    private final LayoutInflater inflater;
    private final SessionManager sessionmanager;
    private final String layoutType;
    private final ArrayList<String> list_wishList;
   // private final String category_type;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private String TAG = NewProductsRecyclerViewAdapter.class.getSimpleName();
    private ImageLoader mImageLoader;


    //,String categortytype
    public NewProductsRecyclerViewAdapter(Context context, ArrayList<ProductData> listNewProduct, String layoutType, ArrayList<String> list_wishList) {
        this._context = context;
        this.list_NewProcuts = listNewProduct;
        inflater = LayoutInflater.from(context);
        this.list_wishList = list_wishList;

        sessionmanager = new SessionManager(context);
        userDetails = sessionmanager.getSessionDetails();
        this.layoutType = layoutType;
        //this.category_type= categortytype;


    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final RatingBar ratingBar;
        private final TextView txtPrice, txtName, txtWishList, txtProductMRP, txtOffer;
        private final ImageView imgItem;
        private final CardView crdProduct;
       // private final TextView txtStockStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            crdProduct = (CardView) itemView.findViewById(R.id.crdProduct);
            imgItem = (ImageView) itemView.findViewById(R.id.imgItem);
            txtName = (TextView) itemView.findViewById(R.id.txtItemName);
            txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);
            txtProductMRP = (TextView) itemView.findViewById(R.id.txtProductMRP);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            txtWishList = (TextView) itemView.findViewById(R.id.txtWishLiast);
            //txtStockStatus = (TextView) itemView.findViewById(R.id.txtStockStatus);

            txtOffer = (TextView) itemView.findViewById(R.id.txtOffer);

            txtWishList.setVisibility(View.GONE);
            //  imgItem.setScaleType(ImageView.ScaleType.FIT_XY);

          /*  if (userDetails.get(SessionManager.KEY_CATEGORY_TYPE).equals("dashboard") || userDetails.get(SessionManager.KEY_CATEGORY_TYPE).equals("category")) {*/
                // sets width to wrap content and height to 10 dp ->
                crdProduct.setLayoutParams(new CardView.LayoutParams(
                        CardView.LayoutParams.FILL_PARENT, CardView.LayoutParams.WRAP_CONTENT));
                crdProduct.setContentPadding(4, 4, 4, 4);
                crdProduct.setCardElevation(8);
                //  imgItem.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.FILL_PARENT ,CardView.LayoutParams.FILL_PARENT ));
          /*  } else {
                crdProduct.setCardElevation(4);
            }
*/

        }
    }

    @Override
    public NewProductsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        if (layoutType.equals("list")) {
            v = inflater.inflate(R.layout.row_single_product_listview, parent, false);
        } else {
            v = inflater.inflate(R.layout.row_single_product_gridview, parent, false);

        }
        MyViewHolder viewFolder = new MyViewHolder(v);
        return viewFolder;
    }

    @Override
    public void onBindViewHolder(final NewProductsRecyclerViewAdapter.MyViewHolder holder, final int position) {

        final ProductData pd = list_NewProcuts.get(position);

        holder.txtName.setTypeface(CustomFonts.typefaceCondensed(_context));
        holder.txtPrice.setTypeface(CustomFonts.typefaceCondensed(_context));
        holder.txtProductMRP.setTypeface(CustomFonts.typefaceCondensed(_context));
        holder.txtOffer.setTypeface(CustomFonts.typefaceCondensed(_context));


        holder.txtName.setText(pd.getProductname());
        //holder.txtPrice.setText(Html.fromHtml("<b>?" + pd.getPrice() + "</b>" + "\n ?<del>" + pd.getMrp() + "</del>"));
        holder.txtPrice.setText("\u20b9" + pd.getPrice());
        holder.txtProductMRP.setText("\u20b9" + pd.getMrp());


        Double offer = Double.parseDouble(pd.getPrice()) * 100 / Double.parseDouble(pd.getMrp());
        int percentage = 100 - offer.intValue();
        if (percentage == 0 || percentage < 0) {
            holder.txtOffer.setVisibility(View.GONE);
        } else {
            //  holder.txtOffer.setVisibility(View.GONE);
            holder.txtOffer.startAnimation(AnimationUtils.loadAnimation(_context, R.anim.fadein));

            if (percentage <= 9) {
                holder.txtOffer.setText(" " + percentage + "% off");
                //holder.imgItem.setLabelText(" " + percentage + "% off");
            } else {
                holder.txtOffer.setText("" + percentage + "% off");
               // holder.imgItem.setLabelText("" + percentage + "% off");
            }
        }


        final int IsExistInWishList = list_wishList.indexOf(pd.getProductid());
        if (IsExistInWishList == -1) {
            holder.txtWishList.setBackgroundResource(R.drawable.ic_wishlist_default);
        } else {
            holder.txtWishList.setBackgroundResource(R.drawable.ic_wishlist_seleceted2);

        }

       /* //Check Product availability
        if (pd.getInventory().equals("0")) {
            //SetVisible out of stock
            holder.txtStockStatus.setVisibility(View.VISIBLE);
            holder.txtStockStatus.setText("Out Of Stock");

        } else {
            //SetInVisible out of stock
            holder.txtStockStatus.setVisibility(View.GONE);
        }*/


        holder.txtWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(userDetails.get(SessionManager.KEY_USER_ID).equals("0"))
                {
                    sessionmanager.setNewUserSession("ItemDisplayActivity");
                    Intent intent = new Intent(_context , LoginActivity.class);
                    _context.startActivity(intent);




                }
                else
                {
                    holder.txtWishList.startAnimation(AnimationUtils.loadAnimation(_context, R.anim.clockwise_refresh));



                /*    dbhandler db = new dbhandler(_context);
                    SQLiteDatabase sd = db.getReadableDatabase();
                    sd = db.getWritableDatabase();*/

                    if (IsExistInWishList == -1) {
                        sendWishListDetailsToServer( pd.getProductid(), pd.getPrice(), "add", holder, pd,position);
                    } else {
                        sendWishListDetailsToServer( pd.getProductid(), pd.getPrice(), "remove", holder, pd,position);
                    }
                }





            }
        });


        try {

            if(layoutType.equals("grid") || layoutType.equals("list"))
            {




    /*            // Instantiate the RequestQueue.
                mImageLoader = MyApplication.getInstance()
                        .getImageLoader();
                //Image URL - This can point to any image file supported by Android
                //final String url = "http://goo.gl/0rkaBz";


                mImageLoader.get(pd.getImage_url(), ImageLoader.getImageListener(holder.imgItem,
                        R.drawable.app_logo, R.drawable
                                .app_logo));



                holder.imgItem.setImageUrl(pd.getImage_url(), mImageLoader);*/




                Picasso.with(_context)
                        .load(pd.getImage_url())
                        .placeholder(R.drawable.app_logo)
                        .error(R.drawable.app_logo)
                        .into(holder.imgItem);


                //Glide.with(_context).load(pd.getImage_url()).placeholder(R.drawable.splash_screen_500).error(R.drawable.splash_screen_500).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade(R.anim.fadein, 2000).fitCenter().into(holder.imgItem);




            }
            Log.d(TAG, "Final Image Path : " + pd.getImage_url());
            //.placeholder(R.mipmap.ic_launcher)






            // Picasso.with(_context).load(pd.getImage_url()).resize(1000, 400).centerInside().into(holder.imgItem);
            //  Picasso.with(_context).load(pd.getImage_url()).fit().into(holder.imgItem);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(_context, "Error in Image Loading  : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        holder.imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  try {
                    //Toast.makeText(_context, "Name : " + pd.getProductname() + " Id : " + pd.getProductid(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Clicked On Name : " + pd.getProductname() + " Id : " + pd.getProductid());
                    Intent intent = new Intent(_context,
                            SingleItemActivity.class);
                    sessionmanager.setProductDetails(pd.getProductid(), String.valueOf(IsExistInWishList),userDetails.get(SessionManager.KEY_PRODUCT_DESCR), pd.getImage_url(), userDetails.get(SessionManager.KEY_PRODUCT_RATING));


                    intent.putExtra("ActivityName", "ItemDisplayActivity");
                    sessionmanager.setActivityName("ItemDisplayActivity");
                    _context.startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }*/

            }
        });


    }

    @Override
    public int getItemCount() {
        return list_NewProcuts.size();
    }

    private void sendWishListDetailsToServer( final String ProductId, final String ProductPrice, final String type, final NewProductsRecyclerViewAdapter.MyViewHolder holder, final ProductData pd,final int pos) {


        String url_addtowishlist = AllKeys.WEBSITE + "manageWishlist/" + type + "/" + userDetails.get(SessionManager.KEY_USER_ID) + "/" + ProductId + "/" + AllKeys.convertEncodedString(AllKeys.getDateTime()) + "/" + AllKeys.convertEncodedString(AllKeys.getDateTime()) + "/" + ProductPrice + "";


        Log.d(TAG, "URL AddToWishList : " + url_addtowishlist);
        StringRequest str_addToWishList = new StringRequest(Request.Method.GET, url_addtowishlist, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Response ManageWishList : " + response);

                if (response.equals("1")) {


                    if (type.equals("add")) {
                        Log.d(TAG, "Wishlist Data : Add " + list_wishList.toString());
                        list_wishList.add(ProductId);
                        holder.txtWishList.setBackgroundResource(R.drawable.ic_wishlist_seleceted2);


                        // pd.setSelectionStatus(true);
                        //  int indexof_item = list_NewProcuts.indexOf(pd);
                        //  list_NewProcuts.set(indexof_item,pd);




                        Toast.makeText(_context, "Added to your wishlist", Toast.LENGTH_SHORT).show();
                    } else {

                        //pd.setSelectionStatus(false);
                        // int indexof_item = list_NewProcuts.indexOf(pd);
                        // list_NewProcuts.set(indexof_item,pd);


                        Log.d(TAG, "Wishlist Data : Remove " + list_wishList.toString());
                        final int index = list_wishList.indexOf(ProductId);
                        list_wishList.remove(index);
                        holder.txtWishList.setBackgroundResource(R.drawable.ic_wishlist_default);
                        Toast.makeText(_context, "Removed from your wishlist", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(_context, "Sorry,try again...", Toast.LENGTH_SHORT).show();
                }

               // db.close();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error == null) {

                }
                sendWishListDetailsToServer( ProductId, ProductPrice, type, holder, pd,pos);
                Log.d(TAG, "Error in ManageWishList : " + error.getMessage());
            }
        });
        MyApplication.getInstance().addToRequestQueue(str_addToWishList);

    }
}
