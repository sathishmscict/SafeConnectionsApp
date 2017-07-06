package com.safeconnectionsapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.safeconnectionsapp.R;
import com.safeconnectionsapp.pojo.Category;
import com.safeconnectionsapp.session.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Satish Gadde on 30-04-2016.
 */
public class CategoryAdapterRecyclerView extends RecyclerView.Adapter<CategoryAdapterRecyclerView.MyViewHolder> {

    private final Context _context;
    private final SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private List<Category> list_categoty;
    private ImageLoader mImageLoader;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivCategory;
        public TextView tvcategory;

        public MyViewHolder(View view) {
            super(view);


            tvcategory = (TextView) view.findViewById(R.id.tvcategory);

            ivCategory = (ImageView) view.findViewById(R.id.ivCategory);


        }

    }


    public CategoryAdapterRecyclerView(Context context, List<Category> catList) {
        this.list_categoty = catList;
        this._context = context;

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_single_category, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Category category = list_categoty.get(position);

        holder.tvcategory.setText(category.getCategoryname());


        try {
            //Glide.with(_context).load(list_categoty.get(position).getImageurl()).placeholder(R.drawable.app_logo).error(R.drawable.app_logo).crossFade(R.anim.fadein, 2000).override(500, 500).into(holder.ivCategory);

            Picasso.with(_context)
                    .load(list_categoty.get(position).getImageurl())
                    .placeholder(R.drawable.app_logo)
                    .error(R.drawable.app_logo)
                    .into(holder.ivCategory);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return list_categoty.size();
    }
}