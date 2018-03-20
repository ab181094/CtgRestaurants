package com.csecu.amrit.ctgrestaurants.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.csecu.amrit.ctgrestaurants.R;
import com.csecu.amrit.ctgrestaurants.controllers.ToastController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Amrit on 07/03/2018.
 */

public class UploadToken extends AsyncTask {
    Context context;
    ToastController toastController;

    public UploadToken(Context context) {
        this.context = context;
        toastController = new ToastController(context);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String insert_url = context.getString(R.string.insert_url);
        String token = (String) objects[0];
        try {
            String data = URLEncoder.encode("token", "UTF-8") + "=" +
                    URLEncoder.encode(token, "UTF-8");

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
    }
}
