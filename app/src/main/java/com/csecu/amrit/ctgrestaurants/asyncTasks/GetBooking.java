package com.csecu.amrit.ctgrestaurants.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.csecu.amrit.ctgrestaurants.R;
import com.csecu.amrit.ctgrestaurants.interfaces.AsyncResponse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Amrit on 08/03/2018.
 */

public class GetBooking extends AsyncTask {
    public AsyncResponse delegate = null;
    String JSON_STRING = null;
    Context context;

    public GetBooking(Context context) {
        this.context = context;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String restaurants_url = context.getString(R.string.get_book_url);
        try {
            URL url = new URL(restaurants_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            while ((JSON_STRING = bufferedReader.readLine()) != null) {
                stringBuilder.append(JSON_STRING + "\n");
            }

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return stringBuilder.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        delegate.processFinish(o);
    }
}
