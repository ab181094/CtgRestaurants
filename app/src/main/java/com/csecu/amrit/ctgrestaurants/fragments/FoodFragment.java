package com.csecu.amrit.ctgrestaurants.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csecu.amrit.ctgrestaurants.R;
import com.csecu.amrit.ctgrestaurants.asyncTasks.AddFood;
import com.csecu.amrit.ctgrestaurants.asyncTasks.UpdateFood;
import com.csecu.amrit.ctgrestaurants.controllers.ToastController;
import com.csecu.amrit.ctgrestaurants.interfaces.AsyncResponse;
import com.csecu.amrit.ctgrestaurants.models.Food;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodFragment extends Fragment implements AsyncResponse {
    ImageView imageView;
    TextView tvName, tvPrice, tvDescription, tvType, tvRating;
    RatingBar ratingBar;
    Bundle bundle;
    Food food;
    UpdateFood updateFood;
    Boolean status = false;

    public FoodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food, container, false);

        imageView = view.findViewById(R.id.food_details_imageView);
        tvName = view.findViewById(R.id.food_details_tvName);
        tvPrice = view.findViewById(R.id.food_details_tvPrice);
        tvDescription = view.findViewById(R.id.food_details_tvDescription);
        tvType = view.findViewById(R.id.food_details_tvType);
        tvRating = view.findViewById(R.id.food_details_tvRating);
        ratingBar = view.findViewById(R.id.ratingBar);
        ratingBar.setNumStars(5);

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(getString(R.string.app_name),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Boolean auth = sharedpreferences.getBoolean(getString(R.string.auth), false);

        updateFood = new UpdateFood(getActivity());
        updateFood.delegate = this;

        if (auth) {
            final Animation open = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
            final Animation close = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
            final Animation forward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_forward);
            final Animation backward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_backward);

            final FloatingActionButton fab = view.findViewById(R.id.food_fab);
            fab.setVisibility(View.VISIBLE);
            final FloatingActionButton fab_comment = view.findViewById(R.id.food_fab_comment);
            final FloatingActionButton fab_update = view.findViewById(R.id.food_fab_update);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (status) {
                        fab.startAnimation(backward);
                        fab_update.startAnimation(close);
                        fab_comment.startAnimation(close);

                        fab_update.setClickable(false);
                        fab_comment.setClickable(false);
                        status = false;
                    } else {
                        fab.startAnimation(forward);
                        fab_update.startAnimation(open);
                        fab_comment.startAnimation(open);
                        fab_update.setClickable(true);
                        fab_comment.setClickable(true);
                        status = true;
                    }
                }
            });

            fab_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddFoodFragment fragment = new AddFoodFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("food", food);
                    bundle.putString("op", "update");
                    fragment.setArguments(bundle);
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.owner_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });

            fab_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bundle = this.getArguments();
        if (bundle != null) {
            food = bundle.getParcelable("food");

            tvName.setText(decodeString(food.getName()));
            tvPrice.setText(decodeString(food.getPrice()));
            tvDescription.setText(decodeString(food.getDescription()));
            tvType.setText(decodeString(food.getType()));
            tvRating.setText(decodeString(String.valueOf(food.getRating())));

            if (food.getImage() != null) {
                final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                mStorageRef.child("Foods/" + food.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getActivity())
                                .load(uri)
                                .into(imageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        ToastController toastController = new ToastController(getActivity());
                        toastController.errorToast("Failed to load image");
                    }
                });
            }

            SharedPreferences sharedpreferences = getActivity().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            float rating = sharedpreferences.getFloat(String.valueOf(food.getId()), (float) 0.0);
            ratingBar.setRating(rating);

            updateFood = new UpdateFood(getActivity());
            updateFood.delegate = this;

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    SharedPreferences sharedpreferences = getActivity().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    Float previousRating = sharedpreferences.getFloat(String.valueOf(food.getId()), (float) 0.0);
                    editor.putFloat(String.valueOf(food.getId()), v);
                    editor.commit();

                    new LovelyStandardDialog(getActivity(), LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                            .setTopColorRes(R.color.colorPrimaryDark)
                            .setButtonsColorRes(R.color.colorAccent)
                            .setIcon(R.mipmap.ic_launcher)
                            .setTitle(R.string.rating)
                            .setMessage(R.string.feedback)
                            .setPositiveButton(android.R.string.ok, null)
                            .show();

                    Float diff = v - previousRating;
                    food.setRating(food.getRating() + diff);

                    updateFood.execute(food);
                }
            });
        }
    }

    private String decodeString(String s) {
        ToastController toastController = new ToastController(getActivity());
        try {
            s = URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            toastController.errorToast(e.toString());
        }
        return s;
    }

    @Override
    public void processFinish(Object output) {
        getActivity().recreate();
    }
}