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
import com.foodapp.murti.Utils.MyPrefrences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignnUp extends AppCompatActivity {

    Button signup;
    EditText re_password,password,mobile,email,name;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Dialog dialog;
    JSONObject jsonObject ;
    String refValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signn_up);

        name=(EditText)findViewById(R.id.name);
        email=(EditText)findViewById(R.id.email);
        mobile=(EditText)findViewById(R.id.mobile);
        password=(EditText)findViewById(R.id.password);
        re_password=(EditText)findViewById(R.id.re_password);
        signup=(Button)findViewById(R.id.signup);

        dialog=new Dialog(SignnUp.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        Getseter.preferences = getApplicationContext().getSharedPreferences("My_prefence", MODE_PRIVATE);
        Getseter.editor =Getseter.preferences.edit();



        if (MyPrefrences.getRefer(getApplicationContext()).equals("")){
            refValue="no-referal";
        }
        else{
            refValue=MyPrefrences.getRefer(getApplicationContext());
        }

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!name.getText().toString().equals("")){

                    if (!email.getText().toString().equals("")){

                        if (email.getText().toString().trim().matches(emailPattern)){
                            if (!mobile.getText().toString().equals("")){

                                if (!password.getText().toString().equals("")){

                                    if (password.getText().toString().equalsIgnoreCase(re_password.getText().toString())) {
//                                        Toast.makeText(getApplicationContext(), "yes", Toast.LENGTH_SHORT).show();
                                          register();
                                    }
                                    else{
                                        Toast.makeText(SignnUp.this, "Please enter same password", Toast.LENGTH_SHORT).show();
                                    }

                                    Getseter.showdialog(dialog);
                                }
                                else
                                {
                                    Toast.makeText(SignnUp.this, "Please enter password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(SignnUp.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(SignnUp.this, "Please enter valid Email", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else
                    {
                        Toast.makeText(SignnUp.this, "Please enter Email", Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    Toast.makeText(SignnUp.this, "Please enter name", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void register() {



        StringRequest stringRequest=new StringRequest(Request.Method.POST, Api.userRegistration  , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Getseter.exitdialog(dialog);

                Log.d("sdfdsgdfsgdsf",response);

                try {
                    jsonObject = new JSONObject(response);

                    if (jsonObject.optString("status").equals("success")) {

                        Toast.makeText(getApplicationContext(), jsonObject.optString("message").toString(), Toast.LENGTH_SHORT).show();

                    Getseter.editor.putString("fname",name.getText().toString());
                    Getseter.editor.putString("mobile",mobile.getText().toString());
                    Getseter.editor.putString("emailid",email.getText().toString());
                    Getseter.editor.commit();

                    startActivity(new Intent(SignnUp.this,Login.class));

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
                Toast.makeText(getApplicationContext(), "Please connect to the internet...", Toast.LENGTH_SHORT).show();
                Getseter.exitdialog(dialog);

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

               // params.put("register", "1");
                params.put("fname", name.getText().toString());
                params.put("email", email.getText().toString());
                params.put("mobile",  mobile.getText().toString());
                params.put("password", password.getText().toString());
                params.put("refValue", refValue.toString());


                return params;
            }
        };

//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(27000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);



//        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, "http://hoomiehome.com/appcredentials/jsondata.php?register=1&name="+name.getText().toString()+"&email="+email.getText().toString()+"&mobile="+mobile.getText().toString()+"&password="+password.getText().toString(), null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//
//                Log.d("fgfhfgh",response.toString());
//                if (response.optString("success").equals("1")) {
//                    Getseter.exitdialog(dialog);
//                    Toast.makeText(SignnUp.this, response.optString("message").toString(), Toast.LENGTH_SHORT).show();
//                    Getseter.editor.putString("fname",name.getText().toString());
//                    Getseter.editor.putString("mobile",mobile.getText().toString());
//                    Getseter.editor.putString("emailid",email.getText().toString());
//                    Getseter.editor.commit();
//
//                    startActivity(new Intent(SignnUp.this,Login.class));
//
//                }
//                else if (response.optString("success").equals("0")){
//                    Getseter.exitdialog(dialog);
//                    Toast.makeText(SignnUp.this, response.optString("message").toString(), Toast.LENGTH_SHORT).show();
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
