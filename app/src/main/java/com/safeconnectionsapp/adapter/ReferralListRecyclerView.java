package com.safeconnectionsapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.safeconnectionsapp.R;
import com.safeconnectionsapp.pojo.ReferralList;

import java.util.List;

/**
 * Created by Satish Gadde on 30-04-2016.
 */
public class ReferralListRecyclerView extends RecyclerView.Adapter<ReferralListRecyclerView.MyViewHolder> {

    private final Context _context;
    private List<ReferralList> referralList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout llMain;
        public TextView notificationid, txtname, txtrs, txtmobile,txtdate;

        public MyViewHolder(View view) {
            super(view);

            txtname = (TextView) view.findViewById(R.id.txtname);
            txtrs = (TextView) view.findViewById(R.id.txtrs);

            txtmobile = (TextView) view.findViewById(R.id.txtmobile);

            txtdate = (TextView) view.findViewById(R.id.txtdate);


            llMain = (LinearLayout)view.findViewById(R.id.llMain);


        }

    }


    public ReferralListRecyclerView(Context context, List<ReferralList> moviesList) {
        this._context = context;

        this.referralList = moviesList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_referal_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ReferralList rl = referralList.get(position);

        holder.txtname.setText("" + rl.getName());
        //holder.txtrs.setText("" + rl.getPrice());
        holder.txtrs.setText("" );

        holder.txtmobile.setText("" + rl.getMobile());

        holder.txtdate.setText("" + rl.getDatetime());







    }

    @Override
    public int getItemCount() {
        return referralList.size();
    }
}