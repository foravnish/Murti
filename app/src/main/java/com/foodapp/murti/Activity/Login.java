package com.foodapp.murti.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.foodapp.murti.R;
import com.foodapp.murti.Utils.Api;
import com.foodapp.murti.Utils.AppController;
import com.foodapp.murti.Utils.Getseter;

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
    TextView forgotssword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Dialog dialog;
    Spinner city;
    ArrayList<Getseter> DataList=new ArrayList<>();
    List<String> City_Names = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login=(Button)findViewById(R.id.login);
        signup=(Button)findViewById(R.id.signup);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        forgotssword=(TextView)findViewById(R.id.forgotssword);
       // city=(Spinner) findViewById(R.id.city);

        dialog=new Dialog(Login.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        Getseter.preferences = getSharedPreferences("My_prefence", MODE_PRIVATE);
        Getseter.editor =Getseter.preferences.edit();


        forgotssword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(Login.this,ForgotPassword.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,SignnUp.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!email.getText().toString().equals("")){
                    if (email.getText().toString().trim().matches(emailPattern)) {
                        if (!password.getText().toString().equals("")){
                            loginfunction();
                            Getseter.showdialog(dialog);
                        }
                        else{
                            Toast.makeText(Login.this, "Plaase enter password", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(Login.this, "Plaase enter valid email id", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(Login.this, "Plaase enter email id", Toast.LENGTH_SHORT).show();
                }
                //startActivity(new Intent(Login.this,Navigation.class));

            }
        });

        //Getseter.showdialog(dialog);
//        JsonObjectRequest jsonObjectRequest22=new JsonObjectRequest(Request.Method.GET, "http://hoomiehome.com/appcredentials/jsondata.php?getCities=1", null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                //Getseter.exitdialog(dialog);
//
//                JSONArray jsonArray=response.optJSONArray("cities");
//                for (int i=0;i<jsonArray.length();i++){
//                    JSONObject jsonObject=jsonArray.optJSONObject(i);
//                    DataList.add(new Getseter(jsonObject.optString("city_id"),jsonObject.optString("city_name"),null,null));
//                    City_Names.add(jsonObject.optString("city_name"));
//                }
//                ArrayAdapter<String> adapter_state1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item,    City_Names);
//                adapter_state1.setDropDownViewResource(R.layout.spinnertext);
//                city.setAdapter(adapter_state1);
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), "Please connect to the internet.", Toast.LENGTH_SHORT).show();
//              //  Getseter.exitdialog(dialog);
//            }
//        });
//        AppController.getInstance().addToRequestQueue(jsonObjectRequest22);

//        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("sdfvsdgvfdsgdf", DataList.get(position).getID());
//                Getseter.editor.putString("city_id", DataList.get(position).getID());
//                Getseter.editor.putString("city_name", DataList.get(position).getName().toString());
//                Getseter.editor.commit();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

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

                            Getseter.editor.putString("user_id", jsonObject1.optString("id"));
                            Getseter.editor.putString("uname", jsonObject1.optString("fname"));
                            Getseter.editor.putString("emailid", email.getText().toString());
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
}
