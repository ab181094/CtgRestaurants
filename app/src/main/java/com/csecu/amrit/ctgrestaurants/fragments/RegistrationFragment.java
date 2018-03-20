package com.csecu.amrit.ctgrestaurants.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.csecu.amrit.ctgrestaurants.R;
import com.csecu.amrit.ctgrestaurants.controllers.NetworkController;
import com.csecu.amrit.ctgrestaurants.controllers.ToastController;
import com.csecu.amrit.ctgrestaurants.models.ResAndOwner;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {
    EditText etName, etOwner, etPhone, etPassword, etRePassword, etCapacity, etDescription;
    TextView tvLocation;
    Button btLocation, btNext;

    int PLACE_PICKER_REQUEST = 1;
    String latitude = null, longitude = null, op;
    ResAndOwner resAndOwner;
    NetworkController networkController;
    ToastController toastController;

    public RegistrationFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        etName = view.findViewById(R.id.reg_et_res_name);
        etOwner = view.findViewById(R.id.reg_et_own_name);
        etPhone = view.findViewById(R.id.reg_et_phone);
        etPassword = view.findViewById(R.id.reg_et_password);
        etRePassword = view.findViewById(R.id.reg_et_re_password);
        etCapacity = view.findViewById(R.id.reg_et_capacity);
        etDescription = view.findViewById(R.id.reg_et_description);
        tvLocation = view.findViewById(R.id.reg_tv_location);
        btLocation = view.findViewById(R.id.reg_bt_location);
        btNext = view.findViewById(R.id.reg_bt_next);

        btLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                ToastController toastController = new ToastController(getActivity());
                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                    toastController.errorToast(e.toString());
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                    toastController.errorToast(e.toString());
                }
            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                networkController = new NetworkController(getActivity());
                toastController = new ToastController(getActivity());

                if (!networkController.isNetworkAvailable()) {
                    toastController.warningToast("Check your internet connection");
                }

                String name = etName.getText().toString().trim();
                name = encodeString(name);
                String owner = etOwner.getText().toString().trim();
                owner = encodeString(owner);
                String phone = etPhone.getText().toString().trim();
                phone = encodeString(phone);
                String password = etPassword.getText().toString().trim();
                password = encodeString(password);
                String rePass = etRePassword.getText().toString().trim();
                rePass = encodeString(rePass);
                String capacity = etCapacity.getText().toString().trim();
                capacity = encodeString(capacity);
                String description = etDescription.getText().toString().trim();
                description = encodeString(description);

                if (latitude != null && longitude != null) {
                    latitude = encodeString(latitude);
                    longitude = encodeString(longitude);
                }

                String token = FirebaseInstanceId.getInstance().getToken();

                if (password.length() < 5) {
                    etPassword.setError("Password too short");
                    View focusView = etPassword;
                    focusView.requestFocus();
                } else {
                    if (!password.equals(rePass)) {
                        etRePassword.setError("Passwords don't match");
                        View focusView = etRePassword;
                        focusView.requestFocus();
                    } else {
                        if (name.length() == 0 || name == null) {
                            etName.setError("Fill this field");
                            View focusView = etName;
                            focusView.requestFocus();
                        } else {
                            if (owner.length() == 0 || owner == null) {
                                etOwner.setError("Fill this field");
                                View focusView = etOwner;
                                focusView.requestFocus();
                            } else {
                                if (phone.length() == 0 || phone == null) {
                                    etPhone.setError("Fill this field");
                                    View focusView = etPhone;
                                    focusView.requestFocus();
                                } else {
                                    if (op == null) {
                                        ResAndOwner resAndOwner = new ResAndOwner(name, owner, phone,
                                                password, capacity, description, longitude, latitude,
                                                token);

                                        ImageFragment fragment = new ImageFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putParcelable("ResAndOwner", resAndOwner);
                                        fragment.setArguments(bundle);
                                        FragmentManager manager = getActivity().getSupportFragmentManager();
                                        FragmentTransaction transaction = manager.beginTransaction();
                                        transaction.replace(R.id.home_container, fragment);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                    } else {
                                        resAndOwner.setName(name);
                                        resAndOwner.setOwner(owner);
                                        resAndOwner.setPassword(password);
                                        resAndOwner.setCapacity(capacity);
                                        resAndOwner.setDescription(description);
                                        if (latitude != null && longitude != null) {
                                            resAndOwner.setLatitude(latitude);
                                            resAndOwner.setLongitude(longitude);
                                        }
                                        resAndOwner.setToken(token);

                                        ImageFragment fragment = new ImageFragment();
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
                                }
                            }
                        }
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place location = PlacePicker.getPlace(getActivity(), data);
                String toastMsg = String.format("Place: %s", location.getName());

                tvLocation.setText(toastMsg);
                tvLocation.setVisibility(View.VISIBLE);

                latitude = String.valueOf(location.getLatLng().latitude);
                latitude = encodeString(latitude);
                longitude = String.valueOf(location.getLatLng().longitude);
                longitude = encodeString(longitude);
            }
        }
    }

    private String encodeString(String s) {
        ToastController toastController = new ToastController(getActivity());
        try {
            s = URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            toastController.errorToast(e.toString());
        }
        return s;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            resAndOwner = bundle.getParcelable("ResAndOwner");
            op = bundle.getString("op");

            etName.setText(decodeString(resAndOwner.getName()));
            etOwner.setText(decodeString(resAndOwner.getOwner()));
            etPhone.setText(decodeString(resAndOwner.getPhone()));
            etPassword.setText(decodeString(resAndOwner.getPassword()));
            etCapacity.setText(decodeString(resAndOwner.getCapacity()));
            etDescription.setText(decodeString(resAndOwner.getDescription()));
            tvLocation.setText(decodeString(resAndOwner.getLatitude() + ", " + resAndOwner.getLongitude()));
            tvLocation.setVisibility(View.VISIBLE);

            etPhone.setEnabled(false);
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
}
