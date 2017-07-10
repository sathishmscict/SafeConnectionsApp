package com.safeconnectionsapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.safeconnectionsapp.R;

import com.safeconnectionsapp.pojo.ComplaintAndQuotaionMaster;
import com.safeconnectionsapp.pojo.OrderMaster;
import com.safeconnectionsapp.session.SessionManager;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by chiranjeevi mateti on 10-Jan-17.
 */

public class OrderDisplayRecyclerViewAdapter extends RecyclerView.Adapter<OrderDisplayRecyclerViewAdapter.MyViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private final SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();


    ArrayList<OrderMaster> listOrderData;
    private String TAG = OrderDisplayRecyclerViewAdapter.class.getSimpleName();

    public OrderDisplayRecyclerViewAdapter(Context context, ArrayList<OrderMaster> lstclientorderdata) {
        this.context = context;
        this.listOrderData = lstclientorderdata;
        inflater = LayoutInflater.from(context);

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();


    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtOrderStatus, txtOrderDate, txtOrderId;
        private final Button btnTrack;
        private final ImageView imgItem;
        private final TextView txtItemName;
        private final TextView txtItemPrice;
        private final TextView txtQuantity;


        //private final EditText edtOrderId;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtOrderId = (TextView) itemView.findViewById(R.id.txtOrderID);
            txtOrderDate = (TextView) itemView.findViewById(R.id.txtOrderDate);
            txtOrderStatus = (TextView) itemView.findViewById(R.id.txtOrderStatus);
            btnTrack = (Button) itemView.findViewById(R.id.btnTrack);
            imgItem = (ImageView) itemView.findViewById(R.id.imgItem);
            txtItemName = (TextView) itemView.findViewById(R.id.txtItemName);
            txtItemPrice = (TextView) itemView.findViewById(R.id.txtItemPrice);
            txtQuantity = (TextView) itemView.findViewById(R.id.txtQuantity);


        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.row_single_order, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        OrderMaster cm = listOrderData.get(position);


        try {
            /*DateFormat originalFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = originalFormat.parse("August 21, 2012");
            String formattedDate = targetFormat.format(date);  // 20120821*/

            //DateFormat originalFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
            DateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");
            Date date = originalFormat.parse(cm.getOrderdate());
            String formattedDate = targetFormat.format(date);  // 20120821

            holder.txtOrderDate.setText(Html.fromHtml("DATE : <b>" + formattedDate + "</b>"));
            holder.txtOrderId.setText(Html.fromHtml("ORDER ID : <b>" + cm.getOrderid() + "    " + formattedDate + "</b>"));

            holder.txtOrderId.setText(formattedDate + " ( Order ID : " + cm.getOrderid() + ")");


        } catch (ParseException e) {
            e.printStackTrace();
        }


        /*try {

            holder.txtOrderStatus.setText(Html.fromHtml("STATUS : <b><font color='#4CAF50'>" + dbhandler.getOrderStatusNameByStatusId(Integer.parseInt(cm.getOrder_status())) + "</font></b>"));

        } catch (NumberFormatException e) {
            holder.txtOrderStatus.setText("STATUS : ");
            e.printStackTrace();
        }*/
        holder.txtOrderStatus.setVisibility(View.GONE);

        try {
            holder.txtQuantity.setText("Quantity :" + (int) Double.parseDouble(cm.getProduct_qty()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        holder.btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Tracking information not found", Toast.LENGTH_SHORT).show();

            }
        });


        holder.txtItemName.setText(cm.getProductname());


        try {
            holder.txtItemPrice.setText(Html.fromHtml("\u20b9 <b>" + Double.parseDouble(cm.getPrice()) * Double.parseDouble(cm.getProduct_qty()) + "</b>"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        /*if (cm.getItemSize().equals("")) {
            holder.txtItemPrice.setText(Html.fromHtml("\u20b9 <b>" + Double.parseDouble(cm.getPrice()) * Double.parseDouble(cm.getQuantity()) + "</b>"));
        } else {
            holder.txtItemPrice.setText(Html.fromHtml("\u20b9 <b>" + Double.parseDouble(cm.getPrice()) * Double.parseDouble(cm.getQuantity()) + "</b> | SIZE : <b>" + cm.getItemSize() + "</b>"));
        }*/


        try {


            Glide.with(context).load(cm.getImage()).placeholder(R.drawable.app_logo).error(R.drawable.app_logo).crossFade(R.anim.fadein, 2000).into(holder.imgItem);

            //Picasso.with(context).load(cm.getImage()).placeholder(R.drawable.app_logo).error(R.drawable.app_logo).centerInside().into(holder.imgItem);

            //Picasso.with(context).load(cm.getImage()).placeholder(R.drawable.app_logo).error(R.drawable.app_logo).resize(1000, 400).centerInside().into(holder.imgItem);


            Log.d(TAG, "Final Image Path : " + cm.getImage());
            //.placeholder(R.mipmap.ic_launcher)


            // Picasso.with(_context).load(pd.getImage_url()).resize(1000, 400).centerInside().into(holder.imgItem);
            //  Picasso.with(_context).load(pd.getImage_url()).fit().into(holder.imgItem);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error in Image Loading  : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public int getItemCount() {
        return listOrderData.size();
    }

}
