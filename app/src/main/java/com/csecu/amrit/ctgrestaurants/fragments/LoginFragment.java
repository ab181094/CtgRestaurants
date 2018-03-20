package com.csecu.amrit.ctgrestaurants.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.csecu.amrit.ctgrestaurants.activities.OwnerActivity;
import com.csecu.amrit.ctgrestaurants.asyncTasks.Login;
import com.csecu.amrit.ctgrestaurants.controllers.NetworkController;
import com.csecu.amrit.ctgrestaurants.controllers.ToastController;
import com.csecu.amrit.ctgrestaurants.interfaces.AsyncResponse;
import com.csecu.amrit.ctgrestaurants.models.ResAndOwner;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements AsyncResponse {
    EditText etPhone, etPassword;
    Button btSubmit;
    TextView tvReg;
    NetworkController networkController;
    ToastController toastController;
    Login login;
    private static int TIME_OUT = 2000;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        etPhone = view.findViewById(R.id.login_et_contact);
        etPassword = view.findViewById(R.id.login_et_password);
        tvReg = view.findViewById(R.id.login_tv_reg);
        btSubmit = view.findViewById(R.id.login_bt_submit);

        login = new Login(getActivity(), getActivity().getSupportFragmentManager());
        login.delegate = this;

        tvReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrationFragment fragment = new RegistrationFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.home_container, fragment);
                transaction.commit();
            }
        });

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                networkController = new NetworkController(getActivity());
                toastController = new ToastController(getActivity());

                if (networkController.isNetworkAvailable()) {
                    String phone = etPhone.getText().toString().trim();
                    phone = encodeString(phone);
                    String password = etPassword.getText().toString().trim();
                    password = encodeString(password);

                    if (phone.length() == 0 || phone == null) {
                        etPhone.setError("Fill this field");
                        View focusView = etPhone;
                        focusView.requestFocus();
                    } else {
                        if (password.length() == 0 || password == null) {
                            etPassword.setError("Fill this field");
                            View focusView = etPassword;
                            focusView.requestFocus();
                        } else {
                            try {
                                login.execute(phone, password);
                            } catch (Exception e) {
                                toastController.errorToast(e.toString());
                            }
                        }
                    }
                } else {
                    toastController.errorToast("Check your internet connection");
                }
            }
        });

        return view;
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
    public void processFinish(final Object output) {
        if (output != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getActivity(), OwnerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("ResAndOwner", (ResAndOwner) output);
                    startActivity(intent);
                    getActivity().finish();
                }
            }, TIME_OUT);
        }
    }
}
