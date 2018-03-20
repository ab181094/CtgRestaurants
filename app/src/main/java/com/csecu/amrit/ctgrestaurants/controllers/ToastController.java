package com.csecu.amrit.ctgrestaurants.controllers;

import android.content.Context;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

/**
 * Created by Amrit on 3/4/2018.
 */

public class ToastController {
    Context context;

    public ToastController(Context context) {
        this.context = context;
    }

    public void successToast(String s) {
        Toasty.success(context, s, Toast.LENGTH_LONG, true).show();
    }

    public void errorToast(String s) {
        Toasty.error(context, s, Toast.LENGTH_LONG, true).show();
    }

    public void warningToast(String s) {
        Toasty.warning(context, s, Toast.LENGTH_LONG, true).show();
    }

    public void infoToast(String s) {
        Toasty.info(context, s, Toast.LENGTH_LONG, true).show();
    }
}
