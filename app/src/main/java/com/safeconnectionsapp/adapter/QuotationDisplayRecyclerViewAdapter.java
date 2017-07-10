package com.safeconnectionsapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.safeconnectionsapp.R;
import com.safeconnectionsapp.pojo.ComplaintAndQuotaionMaster;
import com.safeconnectionsapp.session.SessionManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by chiranjeevi mateti on 10-Jan-17.
 */

public class QuotationDisplayRecyclerViewAdapter extends RecyclerView.Adapter<QuotationDisplayRecyclerViewAdapter.MyViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private final SessionManager sessionManager;

    private HashMap<String, String> userDetails= new HashMap<String, String>();


    ArrayList<ComplaintAndQuotaionMaster> listOrderData;
    private String TAG = QuotationDisplayRecyclerViewAdapter.class.getSimpleName();

    public QuotationDisplayRecyclerViewAdapter(Context context, ArrayList<ComplaintAndQuotaionMaster> lstclientorderdata) {
        this.context = context;
        this.listOrderData = lstclientorderdata;
        inflater = LayoutInflater.from(context);

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();




    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtOrderStatus, txtOrderDate, txtOrderId;
        private final Button btnTrack;

        private final TextView txtItemName;



        private final TextView tvDescr;
        private final TextView tvRequestDay;
        private final TextView tvRequestMonth;
        private final TextView tvRequestTime;


        //private final EditText edtOrderId;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtOrderId = (TextView) itemView.findViewById(R.id.txtOrderID);
            txtOrderDate = (TextView) itemView.findViewById(R.id.txtOrderDate);
            txtOrderStatus = (TextView) itemView.findViewById(R.id.txtOrderStatus);
            btnTrack = (Button) itemView.findViewById(R.id.btnTrack);

            txtItemName = (TextView) itemView.findViewById(R.id.txtItemName);


            tvDescr = (TextView)itemView.findViewById(R.id.tvDescr);
            tvRequestDay = (TextView)itemView.findViewById(R.id.txtRequestDay);
            tvRequestMonth = (TextView)itemView.findViewById(R.id.txtRequestMonth);
            tvRequestTime = (TextView)itemView.findViewById(R.id.txtRequestTime);







        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View     view = inflater.inflate(R.layout.row_single_quotation, parent, false);


        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        ComplaintAndQuotaionMaster cm = listOrderData.get(position);


        holder.tvRequestTime.setVisibility(View.GONE);


        holder.tvDescr.setText(cm.getComplaintdescr());


        try {
            /*DateFormat originalFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = originalFormat.parse("August 21, 2012");
            String formattedDate = targetFormat.format(date);  // 20120821*/

            //DateFormat originalFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
            DateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");
            Date date = originalFormat.parse(cm.getComplaintDate());
            String formattedDate = targetFormat.format(date);  // 20120821

            holder.txtOrderDate.setText(Html.fromHtml("DATE : <b>" + formattedDate + "</b>"));
            holder.txtOrderId.setText(Html.fromHtml("ORDER ID : <b>" + cm.getComplaintId() + "    "+ formattedDate +"</b>"));

            //holder.txtOrderId.setText(formattedDate+" ( Order ID : " + cm.getComplaintId() + ")");
            holder.txtOrderId.setText("  Quotation ID : " + cm.getComplaintId() + "");





        } catch (ParseException e) {
            e.printStackTrace();
        }




        try
        {
            DateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("MMM,yyyy,d");
            Date date = originalFormat.parse(cm.getComplaintDate());
            String formattedDate = targetFormat.format(date);  // 20120821
            List<String> myList = new ArrayList<String>(Arrays.asList(formattedDate.split(",")));

            holder.tvRequestMonth.setText(myList.get(0) + " , " + myList.get(1));
            holder.tvRequestDay.setText(myList.get(2));



            long timeInMilliseconds = 0;
            String givenDateString = "Wed Apr 19 16:08:28 GMT+05:30 2017";
            givenDateString = cm.getComplaintDate() + " " + "01:00:00 AM"  + " GMT+05:30 " + myList.get(1);
            //18-04-2017 18:47:31 PM GMT+05:30 2017
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a z yyyy");
            try {
                Date mDate = sdf.parse(givenDateString);
                timeInMilliseconds = mDate.getTime();

                timeInMilliseconds =timeInMilliseconds + (3600 * 24 * 7*1000);
                System.out.println("Date in milli :: " + timeInMilliseconds);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //////////////Compleyte CountDownTime/////////////////


        } catch (ParseException e) {
            e.printStackTrace();
        }






       /* try {

            holder.txtOrderStatus.setText(Html.fromHtml("STATUS : <b><font color='#4CAF50'>"+ dbhandler.getOrderStatusNameByStatusId(Integer.parseInt(cm.getOrder_status())) +"</font></b>"));

        } catch (NumberFormatException e) {
            holder.txtOrderStatus.setText("STATUS : ");
            e.printStackTrace();
        }

        try {
            holder.txtQuantity.setText("Quantity :"+(int)Double.parseDouble(cm.getQuantity())+"\n "+dbhandler.getCompanyNameByCompnayId(Integer.parseInt(cm.getCompanyid())));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }*/

        holder.btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Tracking information not found", Toast.LENGTH_SHORT).show();

            }
        });


        holder.txtItemName.setText(cm.getCategoryname());







    }

    @Override
    public int getItemCount() {
        return listOrderData.size();
    }

}
