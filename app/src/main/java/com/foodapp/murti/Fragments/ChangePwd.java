package com.foodapp.murti.Fragments;


import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePwd extends Fragment {

    EditText current,newpass,renewpass;
    Button submit;
    Dialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_change_pwd, container, false);
        current=(EditText)view.findViewById(R.id.current);
        newpass=(EditText)view.findViewById(R.id.newpass);
        renewpass=(EditText)view.findViewById(R.id.renewpass);
        submit=(Button)view.findViewById(R.id.submit);

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        getActivity().setTitle("Change Password");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(50); //You can manage the time of the blink with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(3);

                if (!current.getText().toString().equals("")){
                    if (!newpass.getText().toString().equals("")){
                        if (!renewpass.getText().toString().equals("")){
                            if (newpass.getText().toString().equals(renewpass.getText().toString())){
                                //Toast.makeText(getActivity(), "Successfully", Toast.LENGTH_SHORT).show();
                                changePassword();
                                Getseter.showdialog(dialog);
                            }
                            else{
                                Toast.makeText(getActivity(), "Password dose not match", Toast.LENGTH_SHORT).show();
                                newpass.startAnimation(anim);
                                renewpass.startAnimation(anim);
                            }
                        }
                        else{
                            Toast.makeText(getActivity(), "Re-enter new password ", Toast.LENGTH_SHORT).show();
                            renewpass.startAnimation(anim);
                        }
                    }
                    else{
                        Toast.makeText(getActivity(), "Enter new password", Toast.LENGTH_SHORT).show();
                        newpass.startAnimation(anim);
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Enter current password", Toast.LENGTH_SHORT).show();
                    current.startAnimation(anim);
                }
            }
        });


        return view;
    }

    private void changePassword() {


        //StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://hoomiehome.com/appcredentials/jsondata.php?", new Response.Listener<String>() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Api.changeUserPassword, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Getseter.exitdialog(dialog);

                JSONObject jsonObject;
                Log.d("sdfdsgdfsgdsf",response);

                try {
                    jsonObject = new JSONObject(response);

                    if (jsonObject.optString("status").equals("success")) {

                        Getseter.exitdialog(dialog);
                        Toast.makeText(getActivity(), "Password Successfully Changed.", Toast.LENGTH_SHORT).show();
                        current.setText("");
                        newpass.setText("");
                        renewpass.setText("");

                    }
                    else {
                        Getseter.exitdialog(dialog);
                    Toast.makeText(getActivity(), jsonObject.optString("message").toString(), Toast.LENGTH_SHORT).show();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Please connect to the internet.", Toast.LENGTH_SHORT).show();
                Getseter.exitdialog(dialog);

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

              //  params.put("change_pass", "1");
                params.put("userId", Getseter.preferences.getString("user_id",""));
                params.put("oldPassword", current.getText().toString());
                params.put("newPassword", newpass.getText().toString());
                //params.put("confirm_password", renewpass.getText().toString());


                return params;
            }
        };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest);

//
//        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, "http://hoomiehome.com/appcredentials/jsondata.php?change_pass=1&user_id="+Getseter.preferences.getString("user_id","")+"&old_password="+current.getText().toString()+"&new_password="+newpass.getText().toString()+"&confirm_password="+renewpass.getText().toString(), null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                Log.d("fgfhfgh",response.toString());
//                if (response.optString("success").equals("1")) {
//                    Getseter.exitdialog(dialog);
//                    Toast.makeText(getActivity(), response.optString("message").toString(), Toast.LENGTH_SHORT).show();
//                    current.setText("");
//                    newpass.setText("");
//                    renewpass.setText("");
//
//                }
//                else if (response.optString("success").equals("0")){
//                    Getseter.exitdialog(dialog);
//                    Toast.makeText(getActivity(), response.optString("message").toString(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(), "Please connect to the internet.", Toast.LENGTH_SHORT).show();
//                Getseter.exitdialog(dialog);
//            }
//        });
//        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }

}
