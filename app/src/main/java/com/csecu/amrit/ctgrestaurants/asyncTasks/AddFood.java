package com.csecu.amrit.ctgrestaurants.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.csecu.amrit.ctgrestaurants.R;
import com.csecu.amrit.ctgrestaurants.controllers.ToastController;
import com.csecu.amrit.ctgrestaurants.interfaces.AsyncResponse;
import com.csecu.amrit.ctgrestaurants.models.Food;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Amrit on 10/03/2018.
 */

public class AddFood extends AsyncTask {
    Context context;
    ToastController toastController;
    Food food;
    public AsyncResponse delegate = null;

    public AddFood(Context context) {
        this.context = context;
        toastController = new ToastController(context);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        // String reg_url = context.getString(R.string.reg_url);
        String food_url = context.getString(R.string.foods_url);
        food = (Food) objects[0];
        try {
            String data = URLEncoder.encode("name", "UTF-8") + "=" +
                    URLEncoder.encode(food.getName(), "UTF-8")
                    + "&" + URLEncoder.encode("price", "UTF-8") + "=" +
                    URLEncoder.encode(food.getPrice(), "UTF-8") + "&" +
                    URLEncoder.encode("description", "UTF-8") + "=" +
                    URLEncoder.encode(food.getDescription(), "UTF-8") + "&" +
                    URLEncoder.encode("type", "UTF-8") + "=" +
                    URLEncoder.encode(food.getType(), "UTF-8") + "&" +
                    URLEncoder.encode("image", "UTF-8") + "=" +
                    URLEncoder.encode(food.getImage(), "UTF-8") + "&" +
                    URLEncoder.encode("restaurant", "UTF-8") + "=" +
                    URLEncoder.encode(String.valueOf(food.getRestaurant()), "UTF-8") + "&" +
                    URLEncoder.encode("rating", "UTF-8") + "=" +
                    URLEncoder.encode(String.valueOf(food.getRating()), "UTF-8");

            URL url = new URL(food_url);
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
