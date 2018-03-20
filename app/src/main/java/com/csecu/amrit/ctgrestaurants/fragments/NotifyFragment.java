package com.csecu.amrit.ctgrestaurants.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.csecu.amrit.ctgrestaurants.R;
import com.csecu.amrit.ctgrestaurants.asyncTasks.Notify;
import com.csecu.amrit.ctgrestaurants.controllers.ToastController;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotifyFragment extends Fragment {
    EditText etTitle, etMessage;
    Button btSend;

    public NotifyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notify, container, false);
        etTitle = view.findViewById(R.id.notify_etTitle);
        etMessage = view.findViewById(R.id.notify_etMessage);
        btSend = view.findViewById(R.id.notify_btSend);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = etTitle.getText().toString().trim();
                String message = etMessage.getText().toString().trim();

                if (title.length() == 0 || title == null) {
                    etTitle.setError("Fill this field");
                    View focusView = etTitle;
                    focusView.requestFocus();
                } else {
                    if (message.length() == 0 || message == null) {
                        etMessage.setError("Fill this field");
                        View focusView = etMessage;
                        focusView.requestFocus();
                    } else {
                        etTitle.setText("");
                        etMessage.setText("");
                        Notify notify = new Notify(getActivity());
                        try {
                            notify.execute(title, message);
                        } catch (Exception e) {
                            ToastController toastController = new ToastController(getActivity());
                            toastController.errorToast(e.toString());
                        }
                    }
                }
            }
        });
        return view;
    }

}
