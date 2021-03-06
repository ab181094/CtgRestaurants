package com.csecu.amrit.ctgrestaurants.asyncTasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentManager;

import com.csecu.amrit.ctgrestaurants.R;
import com.csecu.amrit.ctgrestaurants.activities.HomeActivity;
import com.csecu.amrit.ctgrestaurants.activities.OwnerActivity;
import com.csecu.amrit.ctgrestaurants.controllers.ToastController;
import com.csecu.amrit.ctgrestaurants.interfaces.AsyncResponse;
import com.csecu.amrit.ctgrestaurants.models.ResAndOwner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Amrit on 10/03/2018.
 */

public class DeleteRestaurant extends AsyncTask {
    Context context;
    ToastController toastController;
    private static int TIME_OUT = 2000;
    FragmentManager manager;
    public AsyncResponse delegate = null;

    public DeleteRestaurant(Context context, FragmentManager manager) {
        this.context = context;
        toastController = new ToastController(context);
        this.manager = manager;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String delete_url = context.getString(R.string.delete_url);
        int id = (int) objects[0];
        try {
            String data = URLEncoder.encode("id", "UTF-8") + "=" +
                    URLEncoder.encode(String.valueOf(id), "UTF-8");

            URL url = new URL(delete_url);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            return line;
        } catch (Exception e) {
            return e.toString();
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        toastController.infoToast(o.toString());

        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        // Boolean status = sharedpreferences.getBoolean(getString(R.string.auth), false);
        editor.putBoolean(context.getString(R.string.auth), false);
        editor.commit();

        delegate.processFinish(1);
    }
}
