package com.foodapp.murti.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.foodapp.murti.R;
import com.foodapp.murti.Utils.Api;
import com.foodapp.murti.Utils.AppController;
import com.foodapp.murti.Utils.MyPrefrences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Splash extends AppCompatActivity {
    Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        thread=new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent=new Intent(Splash.this,Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
            }
        };



        sendRegistrationTokenToServer(MyPrefrences.getgcm_token(getApplicationContext()));

        Log.d("sfsdfsdfsdgfsdgf", MyPrefrences.getgcm_token(getApplicationContext()));

        thread.start();
    }


    private void sendRegistrationTokenToServer(final String token) {
        //Getting the user id from shared preferences
        //We are storing gcm token for the user in our mysql database
        final String id = MyPrefrences.getUserID(getApplicationContext());
        //Log.w("GCMRegIntentService", "loadUserid:" + id);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.updateFcm,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {

                            Log.d("dfsdfsdfsdfsdfs",s);
                            JSONObject jsonObject=new JSONObject(s);
                            if (jsonObject.optString("status").equalsIgnoreCase("failure")){
                                //Toast.makeText(getApplicationContext(), "Some Error! Contact to Admin...", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Log.w("GCMRegIntentService", "sendRegistrationTokenToServer! ErrorListener:" );
                        Toast.makeText(getApplicationContext(), "sendRegistrationTokenToServer! ErrorListener", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fcm_id", token);
               // params.put("user_id", id);

                Log.d("fdsfsdgfsdgdfgd",token);
                //Log.d("fdsfsdgfsdgdfgd",id);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

}
