package com.foodapp.murti.Activity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.foodapp.murti.R;
import com.foodapp.murti.Utils.AppController;
import com.foodapp.murti.Utils.Getseter;

import org.json.JSONObject;

public class ForgotPassword extends AppCompatActivity {

    Button submit;
    EditText email;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password1);
        email=(EditText)findViewById(R.id.email);
        submit=(Button)findViewById(R.id.submit);

        dialog=new Dialog(ForgotPassword.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              if (!email.getText().toString().equals("")){
                  if (email.getText().toString().trim().matches(emailPattern)){
                      forgotpasswordfunction();
                      Getseter.showdialog(dialog);
                  }
                  else{
                      Toast.makeText(ForgotPassword.this, "Please enter valid email id", Toast.LENGTH_SHORT).show();
                  }
              }
              else{
                  Toast.makeText(ForgotPassword.this, "Please enter email id", Toast.LENGTH_SHORT).show();
              }


            }
        });
    }

    private void forgotpasswordfunction() {


        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, "http://hoomiehome.com/appcredentials/jsondata.php?forgotpass=1&email="+email.getText().toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                Log.d("fgfhfgh",response.toString());
                if (response.optString("success").equals("1")) {
                    Getseter.exitdialog(dialog);
                    Toast.makeText(ForgotPassword.this, response.optString("message").toString(), Toast.LENGTH_SHORT).show();

                    final Dialog dialog = new Dialog(ForgotPassword.this);
                    // Include dialog.xml file
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.message_dialog);
                    // Set dialog title
                    // set values for custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.textDialog);
                    text.setText(response.optString("message").toString());
                    dialog.show();
                    Button yes = (Button) dialog.findViewById(R.id.yes);

                    // if decline button is clicked, close the custom dialog
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Close dialog
                            dialog.dismiss();
                            System.exit(0);
                        }
                    });





                }
                else if (response.optString("success").equals("0")){
                    Getseter.exitdialog(dialog);
                    Toast.makeText(ForgotPassword.this, response.optString("message").toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Please connect to the internet.", Toast.LENGTH_SHORT).show();
                Getseter.exitdialog(dialog);
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }
}
