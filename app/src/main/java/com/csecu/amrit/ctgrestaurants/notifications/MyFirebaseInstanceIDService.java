package com.csecu.amrit.ctgrestaurants.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.csecu.amrit.ctgrestaurants.R;
import com.csecu.amrit.ctgrestaurants.asyncTasks.UploadToken;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Amrit on 15-10-2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Boolean status = sharedpreferences.getBoolean(getString(R.string.first), true);

        if (status) {
            editor.putBoolean(getString(R.string.first), false);
            editor.commit();

            UploadToken uploadToken = new UploadToken(getApplicationContext());
            uploadToken.execute(token);
        }
    }
}
