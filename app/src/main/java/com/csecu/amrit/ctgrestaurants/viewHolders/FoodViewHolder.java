package com.csecu.amrit.ctgrestaurants.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.csecu.amrit.ctgrestaurants.R;

/**
 * Created by Amrit on 10/03/2018.
 */

public class FoodViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView tvName;
    // public TextView tvPrice;

    public FoodViewHolder(View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.food_imageView);
        tvName = itemView.findViewById(R.id.food_tv_name);
        // tvPrice = itemView.findViewById(R.id.food_tv_price);
    }
}
