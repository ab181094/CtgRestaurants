package com.csecu.amrit.ctgrestaurants.asyncTasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ProgressBar;

import com.csecu.amrit.ctgrestaurants.R;
import com.csecu.amrit.ctgrestaurants.controllers.ToastController;
import com.csecu.amrit.ctgrestaurants.interfaces.AsyncResponse;
import com.csecu.amrit.ctgrestaurants.models.ResAndOwner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Amrit on 11/03/2018.
 */

public class Login extends AsyncTask {
    Context context;
    String JSON_STRING = null;
    String key, value;
    FragmentManager manager;
    ResAndOwner resAndOwner;
    public AsyncResponse delegate = null;

    public Login(Context context, FragmentManager supportFragmentManager) {
        this.context = context;
        manager = supportFragmentManager;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String login_url = context.getString(R.string.restaurants_url);
        // String reg_url = "http://10.2.3.100/eat/insert.php";
        key = (String) objects[0];
        value = (String) objects[1];
        try {
            URL url = new URL(login_url);
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
        String result = o.toString().trim();
        ToastController toastController = new ToastController(context);
        if (parseAll(result)) {
            toastController.successToast("Successful");

            SharedPreferences sharedpreferences = context.getSharedPreferences(context.
                    getString(R.string.app_name), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(context.getString(R.string.auth), true);
            editor.commit();

            delegate.processFinish(resAndOwner);
        } else {
            toastController.errorToast("Wrong Username or password");
        }
    }

    private boolean parseAll(String result) {
        JSONObject jsonObject;
        JSONArray jsonArray;

        try {
            jsonObject = new JSONObject(result);
            jsonArray = jsonObject.getJSONArray("server_response");
            for (int count = 0; count < jsonArray.length(); count++) {
                JSONObject object = jsonArray.getJSONObject(count);
                int id = 0;
                try {
                    id = object.getInt("ID");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String name = object.getString("Name");
                String owner = object.getString("Owner");
                String phone = object.getString("Phone");
                String password = object.getString("Password");
                String capacity = object.getString("Capacity");
                String description = object.getString("Description");
                String latitude = object.getString("Latitude");
                String longitude = object.getString("Longitude");
                String token = object.getString("Token");
                String image = object.getString("Image");

                if (phone.equals(key) && password.equals(value)) {
                    resAndOwner = new ResAndOwner();
                    resAndOwner.setId(id);
                    resAndOwner.setName(name);
                    resAndOwner.setOwner(owner);
                    resAndOwner.setPhone(phone);
                    resAndOwner.setPassword(password);
                    resAndOwner.setCapacity(capacity);
                    resAndOwner.setDescription(description);
                    resAndOwner.setLatitude(latitude);
                    resAndOwner.setLongitude(longitude);
                    resAndOwner.setToken(token);
                    resAndOwner.setImage(image);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            ToastController toastController = new ToastController(context);
            toastController.warningToast(e.toString());
        }
        return false;
    }
}
