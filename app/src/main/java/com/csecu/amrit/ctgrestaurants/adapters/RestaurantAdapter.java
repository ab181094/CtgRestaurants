package com.csecu.amrit.ctgrestaurants.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.bumptech.glide.Glide;
import com.csecu.amrit.ctgrestaurants.R;
import com.csecu.amrit.ctgrestaurants.controllers.ToastController;
import com.csecu.amrit.ctgrestaurants.models.ResAndOwner;
import com.csecu.amrit.ctgrestaurants.viewHolders.MyViewHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * Created by Amrit on 08/03/2018.
 */

public class RestaurantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<ResAndOwner> resList = new ArrayList<>();

    public RestaurantAdapter(Context context, ArrayList<ResAndOwner> resList) {
        this.context = context;
        this.resList = resList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item,
                parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        final ResAndOwner resAndOwner = resList.get(position);

        String name = resAndOwner.getName();
        name = decodeString(name);

        viewHolder.tvName.setText(name);
        if (resAndOwner.getImage() != null) {
            final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            mStorageRef.child("Photos/" + resAndOwner.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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

        /*viewHolder.btMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resAndOwner.getLatitude() == null || resAndOwner.getLatitude().length() == 0) {
                    ToastController toastController = new ToastController(context);
                    toastController.errorToast("Map isn't available");
                } else {
                    Intent intent = new Intent(context, MapsActivity.class);
                    intent.putExtra("resAndOwner", resAndOwner);
                    context.startActivity(intent);
                }
            }
        });*/
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
        return resList.size();
    }

    public ResAndOwner getItem(int position) {
        return resList.get(position);
    }
}
