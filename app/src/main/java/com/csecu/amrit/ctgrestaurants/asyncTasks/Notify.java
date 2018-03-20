package com.csecu.amrit.ctgrestaurants.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.csecu.amrit.ctgrestaurants.R;
import com.csecu.amrit.ctgrestaurants.controllers.ToastController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Amrit on 13/03/2018.
 */

public class Notify extends AsyncTask {
    Context context;

    public Notify(Context context) {
        this.context = context;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String insert_url = context.getString(R.string.send_url);
        String title = (String) objects[0];
        String message = (String) objects[1];
        try {

            String data = URLEncoder.encode("title", "UTF-8") + "=" +
                    URLEncoder.encode(title, "UTF-8");
            data += "&" + URLEncoder.encode("message", "UTF-8") + "=" +
                    URLEncoder.encode(message, "UTF-8");

            URL url = new URL(insert_url);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            return "Success!";
        } catch (Exception e) {
            return "Failed";
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        ToastController toastController = new ToastController(context);
        toastController.infoToast(o.toString());
    }
}
