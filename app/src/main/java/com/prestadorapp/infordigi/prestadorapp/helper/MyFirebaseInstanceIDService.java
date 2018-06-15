package com.prestadorapp.infordigi.prestadorapp.helper;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.prestadorapp.infordigi.prestadorapp.adapter.Common;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh(){
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Common.currentToken = refreshedToken;

        sendRegistrationToServe(refreshedToken);

    }

    private void sendRegistrationToServe(String token){

    }

}
