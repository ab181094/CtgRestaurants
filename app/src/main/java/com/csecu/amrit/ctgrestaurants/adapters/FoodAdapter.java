package com.csecu.amrit.ctgrestaurants.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.csecu.amrit.ctgrestaurants.R;
import com.csecu.amrit.ctgrestaurants.controllers.ToastController;
import com.csecu.amrit.ctgrestaurants.models.Food;
import com.csecu.amrit.ctgrestaurants.viewHolders.FoodViewHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * Created by Amrit on 10/03/2018.
 */

public class FoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<Food> foodsList = new ArrayList<>();

    public FoodAdapter(Context context, ArrayList<Food> foodsList) {
        this.context = context;
        this.foodsList = foodsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,
                parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final FoodViewHolder viewHolder = (FoodViewHolder) holder;
        final Food food = foodsList.get(position);

        String name = decodeString(food.getName());
        // viewHolder.tvName.setText(name);

        String price = decodeString(food.getPrice());
        viewHolder.tvName.setText(name + " --- " + price + " TAKA");

        if (food.getImage() != null) {
            final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            mStorageRef.child("Foods/" + food.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context)
                            .load(uri)
                            .into(viewHolder.imageView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    ToastController toastController = new ToastController(context);
                    toastController.errorToast("Failed to load image");
                }
            });
        }
    }

    private String decodeString(String s) {
        ToastController toastController = new ToastController(context);
        try {
            s = URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            toastController.errorToast(e.toString());
        }
        return s;
    }

    @Override
    public int getItemCount() {
        return foodsList.size();
    }

    public Food getItem(int position) {
        return foodsList.get(position);
    }
}
