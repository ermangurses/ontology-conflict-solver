package edu.arizona.biosemantics.conflictsolver;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh(){

        String token = FirebaseInstanceId.getInstance().getToken();
        SharedPreferencesManager.getInstance(this).setToken(token);
    }
}
