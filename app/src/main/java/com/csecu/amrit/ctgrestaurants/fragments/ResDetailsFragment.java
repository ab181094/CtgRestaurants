package com.csecu.amrit.ctgrestaurants.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csecu.amrit.ctgrestaurants.activities.MapsActivity;
import com.csecu.amrit.ctgrestaurants.R;
import com.csecu.amrit.ctgrestaurants.activities.HomeActivity;
import com.csecu.amrit.ctgrestaurants.asyncTasks.DeleteRestaurant;
import com.csecu.amrit.ctgrestaurants.controllers.ToastController;
import com.csecu.amrit.ctgrestaurants.interfaces.AsyncResponse;
import com.csecu.amrit.ctgrestaurants.models.ResAndOwner;
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
public class ResDetailsFragment extends Fragment implements AsyncResponse {
    ResAndOwner resAndOwner;
    ImageView imageView;
    TextView tvName, tvOwner, tvPhone, tvPassword, tvCapacity, tvDescription, tvPasswordName;
    Button btLocation;
    Boolean status = false;
    private static int TIME_OUT = 2000;
    DeleteRestaurant deleteRestaurant;

    public ResDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_res_details, container, false);

        imageView = view.findViewById(R.id.res_details_imageView);
        tvName = view.findViewById(R.id.res_details_tvName);
        tvOwner = view.findViewById(R.id.res_details_tvOwner);
        tvPhone = view.findViewById(R.id.res_details_tvPhone);
        tvPassword = view.findViewById(R.id.res_details_tvPassword);
        tvCapacity = view.findViewById(R.id.res_details_tvCapacity);
        tvDescription = view.findViewById(R.id.res_details_tvDescription);
        btLocation = view.findViewById(R.id.res_details_btLocation);
        tvPasswordName = view.findViewById(R.id.res_details_tvPasswordName);

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(getString(R.string.app_name),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        final Boolean auth = sharedpreferences.getBoolean(getString(R.string.auth), false);

        deleteRestaurant = new DeleteRestaurant(getActivity(), getActivity().getSupportFragmentManager());
        deleteRestaurant.delegate = this;

        final Animation open = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        final Animation close = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        final Animation forward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_forward);
        final Animation backward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_backward);

        final FloatingActionButton fab = view.findViewById(R.id.res_details_fab);
        final FloatingActionButton fab_delete = view.findViewById(R.id.res_details_fab_delete);
        final FloatingActionButton fab_update = view.findViewById(R.id.res_details_fab_update);
        final FloatingActionButton fab_booking = view.findViewById(R.id.res_details_fab_booking);

        if (auth) {
            fab_booking.setVisibility(View.GONE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status) {
                    if (auth) {
                        fab.startAnimation(backward);
                        fab_update.startAnimation(close);
                        fab_delete.startAnimation(close);

                        fab_update.setClickable(false);
                        fab_delete.setClickable(false);
                        status = false;
                    } else {
                        fab.startAnimation(backward);
                        fab_booking.startAnimation(close);

                        fab_booking.setClickable(false);
                        status = false;
                    }
                } else {
                    if (auth) {
                        fab.startAnimation(forward);
                        fab_update.startAnimation(open);
                        fab_delete.startAnimation(open);
                        fab_update.setClickable(true);
                        fab_delete.setClickable(true);
                        status = true;
                    } else {
                        fab.startAnimation(forward);
                        fab_booking.startAnimation(open);

                        fab_booking.setClickable(true);
                        status = true;
                    }
                }
            }
        });

        fab_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrationFragment fragment = new RegistrationFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("ResAndOwner", resAndOwner);
                bundle.putString("op", "update");
                fragment.setArguments(bundle);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.owner_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LovelyStandardDialog(getActivity(), LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                        .setTopColorRes(R.color.colorPrimaryDark)
                        .setButtonsColorRes(R.color.colorAccent)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle(R.string.confirmation)
                        .setMessage(R.string.delete_message)
                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteRestaurant.execute(resAndOwner.getId());
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });

        fab_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookingFragment fragment = new BookingFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("ResAndOwner", resAndOwner);
                fragment.setArguments(bundle);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.home_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            resAndOwner = bundle.getParcelable("ResAndOwner");

            tvName.setText(decodeString(resAndOwner.getName()));
            tvOwner.setText(decodeString(resAndOwner.getOwner()));
            tvPhone.setText(decodeString(resAndOwner.getPhone()));
            tvPassword.setText(decodeString(resAndOwner.getPassword()));
            tvCapacity.setText(decodeString(resAndOwner.getCapacity()));
            tvDescription.setText(decodeString(resAndOwner.getDescription()));

            SharedPreferences sharedpreferences = getActivity().getSharedPreferences(getString(R.string.app_name),
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            Boolean auth = sharedpreferences.getBoolean(getString(R.string.auth), false);

            if (!auth) {
                tvPasswordName.setVisibility(View.GONE);
                tvPassword.setVisibility(View.GONE);
            }

            if (resAndOwner.getImage() != null) {
                final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                mStorageRef.child("Photos/" + resAndOwner.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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

            btLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (resAndOwner.getLatitude() == null || resAndOwner.getLatitude().length() == 0) {
                        ToastController toastController = new ToastController(getActivity());
                        toastController.errorToast("Map isn't available");
                    } else {
                        Intent intent = new Intent(getActivity(), MapsActivity.class);
                        intent.putExtra("resAndOwner", resAndOwner);
                        getActivity().startActivity(intent);
                    }
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
        if (output != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    getActivity().finish();
                }
            }, TIME_OUT);
        }
    }
}
