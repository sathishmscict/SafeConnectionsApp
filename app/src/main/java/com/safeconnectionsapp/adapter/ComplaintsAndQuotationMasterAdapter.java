package com.safeconnectionsapp.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.safeconnectionsapp.R;
import com.safeconnectionsapp.pojo.ComplaintAndQuotaionMaster;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by SATHISH on 08-Jul-17.
 */

public class ComplaintsAndQuotationMasterAdapter extends RecyclerView.Adapter<ComplaintsAndQuotationMasterAdapter.MyViewHolder> {

    private final Context _context;
    private final ArrayList<ComplaintAndQuotaionMaster> list_complatintandQuotation;


    public ComplaintsAndQuotationMasterAdapter(Context context, ArrayList<ComplaintAndQuotaionMaster> lst) {

        this._context = context;
        this.list_complatintandQuotation = lst;


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvDescr;
        private final ImageView ivImage;
        private final ImageView ivCapture;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvDescr  =(TextView)itemView.findViewById(R.id.tvDescr);
            ivImage= (ImageView)itemView.findViewById(R.id.iv);
            ivCapture = (ImageView)itemView.findViewById(R.id.ivCapture);


        }
    }

    @Override
    public ComplaintsAndQuotationMasterAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_context).inflate(R.layout.row_single_complaint_quotation,parent ,false);
        MyViewHolder v = new MyViewHolder(view);
        return v;
    }

    @Override
    public void onBindViewHolder(ComplaintsAndQuotationMasterAdapter.MyViewHolder holder, int position) {


        holder.tvDescr.setText(list_complatintandQuotation.get(position).getComplaintdescr());

        try {
            //Glide.with(_context).load(list_categoty.get(position).getImageurl()).placeholder(R.drawable.app_logo).error(R.drawable.app_logo).crossFade(R.anim.fadein, 2000).override(500, 500).into(holder.ivCategory);

            Picasso.with(_context)
                    .load(list_complatintandQuotation.get(position).getImageurl())
                    .placeholder(R.drawable.app_logo)
                    .error(R.drawable.app_logo)
                    .into(holder.ivImage);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return list_complatintandQuotation.size();
    }
}
