package com.foodapp.murti.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.foodapp.murti.R;
import com.foodapp.murti.Utils.Api;
import com.foodapp.murti.Utils.AppController;
import com.foodapp.murti.Utils.Getseter;
import com.foodapp.murti.Utils.MyPrefrences;
import com.jakewharton.disklrucache.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login extends AppCompatActivity  {

    Button login,signup;
    EditText password,email;
    TextView forgotssword,skip;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Dialog dialog;
    Spinner city;
    ArrayList<Getseter> DataList=new ArrayList<>();
    List<String> City_Names = new ArrayList<String>();
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String otpString;
    EditText otp_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login=(Button)findViewById(R.id.login);
        signup=(Button)findViewById(R.id.signup);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        forgotssword=(TextView)findViewById(R.id.forgotssword);
        skip=(TextView)findViewById(R.id.skip);
       // city=(Spinner) findViewById(R.id.city);

        dialog=new Dialog(Login.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        Getseter.preferences = getSharedPreferences("My_prefence", MODE_PRIVATE);
        Getseter.editor =Getseter.preferences.edit();

        if (checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.
        }
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Navigation.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
                MyPrefrences.setUserLogin(getApplicationContext(),false);
            }
        });
        forgotssword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog2 = new Dialog(Login.this);
//                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog2.setContentView(R.layout.get_mobile_no);
                //  dialog2.setCancelable(false);

                final EditText mobile_edit= (EditText) dialog2.findViewById(R.id.mobile_edit);
             //   mobile_edit.setText(mobile.getText().toString());
                //TextView recieve= (TextView) dialog2.findViewById(R.id.recieve);
                //recieve.setText("Sent OTP on "+mob);
                Button submit2=(Button)dialog2.findViewById(R.id.submit2);
                submit2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (TextUtils.isEmpty(mobile_edit.getText().toString())){
                            Toast.makeText(Login.this, "Please Enter Mobile No.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            mobileVerify_API(mobile_edit.getText().toString());
                        }
                    }
                });

                dialog2.show();

//                    startActivity(new Intent(Login.this,ForgotPassword.class));
//                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popRegistration();

//                startActivity(new Intent(Login.this,SignnUp.class));
//                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!email.getText().toString().equals("")){
                   // if (email.getText().toString().trim().matches(emailPattern)) {
                        if (!password.getText().toString().equals("")){
                            loginfunction();
                            Getseter.showdialog(dialog);
                        }
                        else{
                            Toast.makeText(Login.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                        }
//                    }
//                    else{
//                        Toast.makeText(Login.this, "Plaase enter valid email id", Toast.LENGTH_SHORT).show();
//                    }
                }
                else{
                    Toast.makeText(Login.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();

                }
                //startActivity(new Intent(Login.this,Navigation.class));

            }
        });


        if (Getseter.preferences.contains("user_id")){
            startActivity(new Intent(Login.this,Navigation.class));
            finish();
        }
    }


    private void loginfunction() {


        StringRequest stringRequest=new StringRequest(Request.Method.POST, Api.Login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Getseter.exitdialog(dialog);

                JSONObject jsonObject;
                Log.d("sdfdsgdfsgdsf",response);

                try {
                    jsonObject = new JSONObject(response);

                    if (jsonObject.optString("status").equals("success")) {

                        Getseter.exitdialog(dialog);

                         //Toast.makeText(Login.this, jsonObject.optString("message").toString(), Toast.LENGTH_SHORT).show();

                        JSONArray jsonArray=jsonObject.optJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);

                            MyPrefrences.setUserLogin(getApplicationContext(),true);
                            Getseter.editor.putString("user_id", jsonObject1.optString("id"));
                            Getseter.editor.putString("uname", jsonObject1.optString("fname"));
//                            Getseter.editor.putString("emailid", jsonObject1.optString("email"));
                            MyPrefrences.setEMAILID(getApplicationContext(),jsonObject1.optString("email"));
                            MyPrefrences.setMyRefrel(getApplicationContext(),jsonObject1.optString("referer"));
                            Getseter.editor.commit();
                        }
                    startActivity(new Intent(Login.this,Navigation.class));
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                    finish();

                    }
                    else {
                        Getseter.exitdialog(dialog);
                       Toast.makeText(getApplicationContext(), jsonObject.optString("message").toString(), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Please connect to the internet.", Toast.LENGTH_SHORT).show();
                Getseter.exitdialog(dialog);

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

               // params.put("login", "1");
                params.put("username", email.getText().toString());
                params.put("password", password.getText().toString());


                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(27000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);


//        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, "http://hoomiehome.com/appcredentials/jsondata.php?login=1&email="+email.getText().toString()+"&password="+password.getText().toString(), null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                Log.d("fgfhfgh",response.toString());
//                if (response.optString("success").equals("1")) {
//                    Getseter.exitdialog(dialog);
//                    Toast.makeText(Login.this, response.optString("message").toString(), Toast.LENGTH_SHORT).show();
//                    Getseter.editor.putString("user_id",response.optString("user_id"));
//                    Getseter.editor.putString("uname",response.optString("uname"));
//                    Getseter.editor.putString("emailid",email.getText().toString());
//                    Getseter.editor.commit();
//                    startActivity(new Intent(Login.this,Navigation.class));
//                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//
//                    finish();
//
//                }
//                else if (response.optString("success").equals("0")){
//                    Getseter.exitdialog(dialog);
//                    Toast.makeText(Login.this, response.optString("message").toString(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), "Please connect to the internet.", Toast.LENGTH_SHORT).show();
//                Getseter.exitdialog(dialog);
//            }
//        });
//        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    private void mobileVerify_API(final String mobileNo) {

        Getseter.showdialog(dialog);

        RequestQueue queue = Volley.newRequestQueue(Login.this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Api.forgetPassword, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Getseter.exitdialog(dialog);
                Log.e("dfsjfdfsdfgd", "Login Response: " + response);


                try {
                    JSONObject jsonObject=new JSONObject(response);
                    // if (jsonObject.getString("status").equalsIgnoreCase("success")){

                    if (jsonObject.optString("status").equals("success")) {

                        optVerfy(mobileNo);

                    }
                    else{
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
//                        Util.errorDialog(Login.this,jsonObject.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Getseter.exitdialog(dialog);
                Log.e("fdgdfgdfgd", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),"Please Connect to the Internet", Toast.LENGTH_LONG).show();

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.e("fgdfgdfgdf","Inside getParams");

                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("mobile", mobileNo);


                return params;
            }

//                        @Override
//                        public Map<String, String> getHeaders() throws AuthFailureError {
//                            Log.e("fdgdfgdfgdfg","Inside getHeaders()");
//                            Map<String,String> headers=new HashMap<>();
//                            headers.put("Content-Type","application/x-www-form-urlencoded");
//                            return headers;
//                        }
        };
        // Adding request to request queue
        queue.add(strReq);


    }


    private void optVerfy(final String mobileNo) {

        final Dialog dialog2 = new Dialog(Login.this);
//                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.otp_dialog_verfy);
//        dialog2.setCancelable(false);

        otp_edit= (EditText) dialog2.findViewById(R.id.otp_edit);
        final EditText newpass= (EditText) dialog2.findViewById(R.id.newpass);
        final EditText newpass2= (EditText) dialog2.findViewById(R.id.newpass2);
        //TextView recieve= (TextView) dialog2.findViewById(R.id.recieve);
        //recieve.setText("Sent OTP on "+mob);
        Button submit2=(Button)dialog2.findViewById(R.id.submit2);
        submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(otp_edit.getText().toString())){

                    Toast.makeText(Login.this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                }

               else if (TextUtils.isEmpty(newpass.getText().toString())){

                        Toast.makeText(Login.this, "Please Enter New Password", Toast.LENGTH_SHORT).show();
                    }
                else if (TextUtils.isEmpty(newpass2.getText().toString())){

                    Toast.makeText(Login.this, "Please Enter Confirm New Password", Toast.LENGTH_SHORT).show();
                }
                else if (!newpass2.getText().toString().equals(newpass.getText().toString())){

                    Toast.makeText(Login.this, "Confirm Password not match", Toast.LENGTH_SHORT).show();
                }
                    else {
                        savePass_API(mobileNo, otp_edit.getText().toString(),newpass2.getText().toString());
                    }


            }
        });
        dialog2.show();


    }

    private void savePass_API(final String mob, final String otp, final String password) {

        Getseter.showdialog(dialog);



        RequestQueue queue = Volley.newRequestQueue(Login.this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Api.userChangePassword, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Getseter.exitdialog(dialog);
                Log.e("OTP", "top Response: " + response);


                try {
                    JSONObject jsonObject=new JSONObject(response);
                    // if (jsonObject.getString("status").equalsIgnoreCase("success")){

                    if (jsonObject.optString("status").equals("success")) {

                        Toast.makeText(Login.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),Login.class));
                       // newPasswordApi(jsonObject1.optString("id"));



                    }
                    else{
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Getseter.exitdialog(dialog);
                Log.e("fdgdfgdfgd", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),"Please Connect to the Internet or Wrong Password", Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.e("fgdfgdfgdf","Inside getParams");

                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("otp", otp);
                params.put("mobile", mob);
                params.put("new_password", password);

                return params;
            }

//                        @Override
//                        public Map<String, String> getHeaders() throws AuthFailureError {
//                            Log.e("fdgdfgdfgdfg","Inside getHeaders()");
//                            Map<String,String> headers=new HashMap<>();
//                            headers.put("Content-Type","application/x-www-form-urlencoded");
//                            return headers;
//                        }
        };
        // Adding request to request queue
        queue.add(strReq);

    }


    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);

        int receiveSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);

        int readSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).
                registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");

//                TextView tv = (TextView) findViewById(R.id.txtview);
//                tv.setText(message);

                Log.d("sgdfgdfgdfgdfd",message);
                Log.d("gsdfgsdfdgvd",message.substring(message.length()-8));
                otpString=message.substring(message.length()-8).replaceAll("\\s+","");
                otp_edit.setText(otpString);
            }
        }
    };




    private void popRegistration() {


        final Dialog dialog2 = new Dialog(Login.this);
//                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.get_mobile_no);
        //  dialog2.setCancelable(false);

        final EditText mobile_edit= (EditText) dialog2.findViewById(R.id.mobile_edit);
      //  mobile_edit.setText(mobile.getText().toString());
//        TextView recieve= (TextView) dialog2.findViewById(R.id.recieve);
//        recieve.setText("Sent OTP on "+mob);
        Button submit2=(Button)dialog2.findViewById(R.id.submit2);
        submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(mobile_edit.getText().toString())){

                    Toast.makeText(Login.this, "Please Enter Mobile No.", Toast.LENGTH_SHORT).show();
                }

                else {

                    sedMessageForRegistration(mobile_edit.getText().toString(),dialog2);

                }
            }
        });

        dialog2.show();



    }

    private void sedMessageForRegistration(final String mobileNo, final Dialog dialog) {

        Getseter.showdialog(dialog);


        RequestQueue queue = Volley.newRequestQueue(Login.this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Api.registrationWithOtp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Getseter.exitdialog(dialog);
                Log.e("fdsfsdfsdgsdfgdsf", "forgetPassword Response: " + response);


                try {
                    JSONObject jsonObject=new JSONObject(response);
                    // if (jsonObject.getString("status").equalsIgnoreCase("success")){

                    if (jsonObject.optString("status").equals("success")) {


                        popForOTP(mobileNo);
                        //  dialog.dismiss();


                    }
                    else{
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
//                        Util.errorDialog(Login.this,jsonObject.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Getseter.exitdialog(dialog);
                Log.e("fdgdfgdfgd", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),"Please Connect to the Internet", Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.e("fgdfgdfgdf","Inside getParams");

                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("mobile_number", mobileNo);


                return params;
            }

//                        @Override
//                        public Map<String, String> getHeaders() throws AuthFailureError {
//                            Log.e("fdgdfgdfgdfg","Inside getHeaders()");
//                            Map<String,String> headers=new HashMap<>();
//                            headers.put("Content-Type","application/x-www-form-urlencoded");
//                            return headers;
//                        }
        };
        // Adding request to request queue
        queue.add(strReq);


    }

    private void popForOTP(final String mob) {

        final Dialog dialog2 = new Dialog(Login.this);
//                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.setotp);
        //  dialog2.setCancelable(false);

        otp_edit= (EditText) dialog2.findViewById(R.id.otp_edit);
        // mobile_edit.setText(mobile.getText().toString());
        TextView recieve= (TextView) dialog2.findViewById(R.id.recieve);
        recieve.setText("Sent OTP on "+mob);
        Button submit2=(Button)dialog2.findViewById(R.id.submit2);
        submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(otp_edit.getText().toString())){

                    Toast.makeText(Login.this, "Please Enter OTP.", Toast.LENGTH_SHORT).show();
                }

                else {

                    verfifationOTP(otp_edit.getText().toString(),dialog2,mob);

                }
            }
        });

        dialog2.show();
    }


    private void verfifationOTP(final String otp, Dialog dialog2, final String mobileNo) {


        Getseter.showdialog(dialog);

        RequestQueue queue = Volley.newRequestQueue(Login.this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Api.verifyRegistrationOtp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Getseter.exitdialog(dialog);
                Log.e("fdsfsdfsdgsdfgdsf", "forgetPassword Response: " + response);


                try {
                    JSONObject jsonObject=new JSONObject(response);
                    // if (jsonObject.getString("status").equalsIgnoreCase("success")){

                    if (jsonObject.optString("status").equals("success")) {

                        Intent intent=new Intent(Login.this,SignnUp.class);
                        intent.putExtra("mobile",mobileNo);
                        startActivity(intent);
//                        startActivity(new Intent(Login.this,Registration.class));


                        //  popForOTP(mobileNo);
                        //  dialog.dismiss();


                    }
                    else{
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
//                        Util.errorDialog(Login.this,jsonObject.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Getseter.exitdialog(dialog);
                Log.e("fdgdfgdfgd", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),"Please Connect to the Internet", Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.e("fgdfgdfgdf","Inside getParams");

                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("mobile_number", mobileNo);
                params.put("otp", otp.replace(" ",""));


                return params;
            }

//                        @Override
//                        public Map<String, String> getHeaders() throws AuthFailureError {
//                            Log.e("fdgdfgdfgdfg","Inside getHeaders()");
//                            Map<String,String> headers=new HashMap<>();
//                            headers.put("Content-Type","application/x-www-form-urlencoded");
//                            return headers;
//                        }
        };
        // Adding request to request queue
        queue.add(strReq);


    }



}
