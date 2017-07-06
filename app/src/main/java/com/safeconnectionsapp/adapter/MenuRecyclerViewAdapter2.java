package com.safeconnectionsapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.safeconnectionsapp.R;
import com.safeconnectionsapp.pojo.Menudata;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by SATHISH on 29-Jun-17.
 */

public class MenuRecyclerViewAdapter2 extends RecyclerView.Adapter<MenuRecyclerViewAdapter2.MyViewHolder> {


    private final Context _context;
    private ArrayList<Menudata> list_menudata;
    private String TAG  =MenuRecyclerViewAdapter2.class.getSimpleName();

    public MenuRecyclerViewAdapter2(Context context, ArrayList<Menudata> lmenu) {
        this._context = context;
        this.list_menudata = lmenu;


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView ivMenuImg;
        private final TextView tvMenuTitle;
        private final ImageView ivSelected;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivMenuImg = (CircleImageView) itemView.findViewById(R.id.ivMenu);
            tvMenuTitle = (TextView) itemView.findViewById(R.id.tvMenuTitle);
            ivSelected = (ImageView)itemView.findViewById(R.id.ivSelected);


        }
    }

    @Override
    public MenuRecyclerViewAdapter2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(_context).inflate(R.layout.row_single_menu, parent, false);
        MyViewHolder holder = new MyViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(MenuRecyclerViewAdapter2.MyViewHolder holder, int position) {

        Menudata md = list_menudata.get(position);

        holder.tvMenuTitle.setText(md.getMenuname());
        Log.d(TAG , "IMAGE URL : "+md.getImage());
        Glide.with(_context).load(md.getImage()).crossFade().into(holder.ivMenuImg);

        if(md.isSelectionstatus() == true)
        {
            holder.ivSelected.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.ivSelected.setVisibility(View.INVISIBLE);

        }



        //Glide.with(_context).load(md.getImage()).crossFade().into(holder.ivMenuImg);

    }

    @Override
    public int getItemCount() {
        return list_menudata.size();
    }
}
