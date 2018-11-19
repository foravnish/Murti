package com.foodapp.murti.Fragments;


import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.foodapp.murti.R;
import com.foodapp.murti.Utils.Api;
import com.foodapp.murti.Utils.AppController;
import com.foodapp.murti.Utils.Getseter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountDetails extends Fragment {


    TextView fname,lname,house,street,complex,landmark,city,pincode,area,mobile,phone;
    Dialog dialog;
    TextView edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_account_details, container, false);
        fname=(TextView)view.findViewById(R.id.fname);
        lname=(TextView)view.findViewById(R.id.lname);
        house=(TextView)view.findViewById(R.id.house);
        street=(TextView)view.findViewById(R.id.street);
        complex=(TextView)view.findViewById(R.id.complex);
        landmark=(TextView)view.findViewById(R.id.landmark);
        city=(TextView)view.findViewById(R.id.city);
        pincode=(TextView)view.findViewById(R.id.pincode);
        area=(TextView)view.findViewById(R.id.area);
        mobile=(TextView)view.findViewById(R.id.mobile);
//        phone=(TextView)view.findViewById(R.id.phone);
        edit=(TextView)view.findViewById(R.id.edit);

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        Getseter.showdialog(dialog);

        getActivity().setTitle("Account Details");
        Log.d("sdfsdfsdfsdfsd",Getseter.preferences.getString("user_id",""));
//        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, "http://hoomiehome.com/appcredentials/jsondata.php?getUserDetails=1&user_id="+Getseter.preferences.getString("user_id",""), null, new Response.Listener<JSONObject>() {
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Api.getUserDetails+"?userId="+Getseter.preferences.getString("user_id",""), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Getseter.exitdialog(dialog);
                try {
                    if (response.optString("status").equals("success")) {
                        JSONArray jsonArray=response.getJSONArray("message");

                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            fname.setText(jsonObject.optString("fname").toString());
                            lname.setText(jsonObject.optString("lname").toString());
                            house.setText(jsonObject.optString("house_no").toString());
                            street.setText(jsonObject.optString("street").toString());
                            complex.setText(jsonObject.optString("complex").toString());
                            landmark.setText(jsonObject.optString("landmark").toString());
                            //                city.setText(jsonObject.optString("city_id").toString());
                            city.setText(Getseter.preferences.getString("city_name", ""));
                            pincode.setText(jsonObject.optString("pincode").toString());
                            area.setText(jsonObject.optString("area").toString());
                            mobile.setText(jsonObject.optString("mobile").toString());
                            // phone.setText(jsonObject.optString("phone").toString());
                        }
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
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new UpdateProfile();
                FragmentManager manager=getFragmentManager();
                Bundle bundle=new Bundle();
                bundle.putString("type","none");
                bundle.putString("orderitem", "");
                bundle.putString("cal_price","");
                bundle.putString("totalItem","");
                bundle.putInt("length",0);
                FragmentTransaction ft=manager.beginTransaction();
                fragment.setArguments(bundle);
                ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
            }
        });
        return view;
    }

}
