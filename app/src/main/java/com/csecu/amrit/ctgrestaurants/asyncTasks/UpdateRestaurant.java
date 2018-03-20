package com.csecu.amrit.ctgrestaurants.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.csecu.amrit.ctgrestaurants.R;
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

public class UpdateRestaurant extends AsyncTask {
    Context context;
    ToastController toastController;
    ResAndOwner resAndOwner;
    public AsyncResponse delegate = null;

    public UpdateRestaurant(Context context) {
        this.context = context;
        toastController = new ToastController(context);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String update_res_url = context.getString(R.string.update_res_url);
        // String reg_url = "http://10.2.3.100/eat/insert.php";
        resAndOwner = (ResAndOwner) objects[0];
        try {
            String data = URLEncoder.encode("name", "UTF-8") + "=" +
                    URLEncoder.encode(resAndOwner.getName(), "UTF-8")
                    + "&" + URLEncoder.encode("owner", "UTF-8") + "=" +
                    URLEncoder.encode(resAndOwner.getOwner(), "UTF-8") + "&" +
                    URLEncoder.encode("phone", "UTF-8") + "=" +
                    URLEncoder.encode(resAndOwner.getPhone(), "UTF-8") + "&" +
                    URLEncoder.encode("password", "UTF-8") + "=" +
                    URLEncoder.encode(resAndOwner.getPassword(), "UTF-8") + "&" +
                    URLEncoder.encode("capacity", "UTF-8") + "=" +
                    URLEncoder.encode(resAndOwner.getCapacity(), "UTF-8") + "&" +
                    URLEncoder.encode("description", "UTF-8") + "=" +
                    URLEncoder.encode(resAndOwner.getDescription(), "UTF-8") + "&" +
                    URLEncoder.encode("latitude", "UTF-8") + "=" +
                    URLEncoder.encode(resAndOwner.getLatitude(), "UTF-8") + "&" +
                    URLEncoder.encode("longitude", "UTF-8") + "=" +
                    URLEncoder.encode(resAndOwner.getLongitude(), "UTF-8") + "&" +
                    URLEncoder.encode("token", "UTF-8") + "=" +
                    URLEncoder.encode(resAndOwner.getToken(), "UTF-8") + "&" +
                    URLEncoder.encode("image", "UTF-8") + "=" +
                    URLEncoder.encode(resAndOwner.getImage(), "UTF-8");

            URL url = new URL(update_res_url);
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

        delegate.processFinish(1);
    }
}
