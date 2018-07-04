package com.foodapp.murti.Fragments;


import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
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
public class TermsAndConditions extends Fragment {


   TextView textview;
    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_terms_and_conditions, container, false);
        textview=(TextView)view.findViewById(R.id.textview);

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        Getseter.showdialog(dialog);

//        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, "http://hoomiehome.com/appcredentials/jsondata.php?getTerms=1", null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Api.termAndCondition, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("fgfhfgh",response.toString());

                    Getseter.exitdialog(dialog);

                    if (response.optString("status").equalsIgnoreCase("success")) {
                        try {
                            JSONArray jsonArray=response.getJSONArray("message");
                            JSONObject jsonObject= jsonArray.getJSONObject(0);
                            textview.setText(Html.fromHtml(jsonObject.optString("content")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
        return view;
    }

}
