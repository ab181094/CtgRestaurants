package com.csecu.amrit.ctgrestaurants.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.csecu.amrit.ctgrestaurants.R;

/**
 * Created by Amrit on 08/03/2018.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView tvName;
    // public ImageButton btMap;

    public MyViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.res_imageView);
        tvName = itemView.findViewById(R.id.res_tv_name);
        // btMap = itemView.findViewById(R.id.res_bt_map);
    }
}
