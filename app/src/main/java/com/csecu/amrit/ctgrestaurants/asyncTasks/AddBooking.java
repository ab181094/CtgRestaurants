package com.csecu.amrit.ctgrestaurants.asyncTasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.csecu.amrit.ctgrestaurants.R;
import com.csecu.amrit.ctgrestaurants.activities.HomeActivity;
import com.csecu.amrit.ctgrestaurants.controllers.ToastController;
import com.csecu.amrit.ctgrestaurants.interfaces.AsyncResponse;
import com.csecu.amrit.ctgrestaurants.models.Booking;
import com.csecu.amrit.ctgrestaurants.models.ResAndOwner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Amrit on 07/03/2018.
 */

public class AddBooking extends AsyncTask {
    Activity activity;
    ToastController toastController;
    Booking booking;

    public AddBooking(Activity activity) {
        this.activity = activity;
        toastController = new ToastController(activity);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String reg_url = activity.getString(R.string.add_book_url);
        // String reg_url = "http://10.2.3.100/eat/insert.php";
        booking = (Booking) objects[0];
        try {
            String data = URLEncoder.encode("name", "UTF-8") + "=" +
                    URLEncoder.encode(booking.getName(), "UTF-8")
                    + "&" + URLEncoder.encode("phone", "UTF-8") + "=" +
                    URLEncoder.encode(booking.getPhone(), "UTF-8") + "&" +
                    URLEncoder.encode("occasion", "UTF-8") + "=" +
                    URLEncoder.encode(booking.getOccassion(), "UTF-8") + "&" +
                    URLEncoder.encode("person", "UTF-8") + "=" +
                    URLEncoder.encode(booking.getPerson(), "UTF-8") + "&" +
                    URLEncoder.encode("date", "UTF-8") + "=" +
                    URLEncoder.encode(booking.getDate(), "UTF-8") + "&" +
                    URLEncoder.encode("time", "UTF-8") + "=" +
                    URLEncoder.encode(booking.getTime(), "UTF-8") + "&" +
                    URLEncoder.encode("item", "UTF-8") + "=" +
                    URLEncoder.encode(booking.getItem(), "UTF-8") + "&" +
                    URLEncoder.encode("req", "UTF-8") + "=" +
                    URLEncoder.encode(booking.getReq(), "UTF-8") + "&" +
                    URLEncoder.encode("restaurant", "UTF-8") + "=" +
                    URLEncoder.encode(booking.getRestaurant(), "UTF-8") + "&" +
                    URLEncoder.encode("token", "UTF-8") + "=" +
                    URLEncoder.encode(booking.getToken(), "UTF-8");

            URL url = new URL(reg_url);
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

        activity.recreate();
    }
}
