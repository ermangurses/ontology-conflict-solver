package edu.arizona.biosemantics.conflictsolver;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    String mExpertId;

    @Override
    public void onTokenRefresh(){

        String token = FirebaseInstanceId.getInstance().getToken();
        registerToken(token);

    }

    private void registerToken(String token) {

        mExpertId = String.valueOf(SharedPreferencesManager.getInstance(this).getExpertId());

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("expertId",mExpertId)
                .add("Token",token)
                .build();

        Request request = new Request.Builder()
                .url(Constants.URL_REGISTERTOKEN)
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
