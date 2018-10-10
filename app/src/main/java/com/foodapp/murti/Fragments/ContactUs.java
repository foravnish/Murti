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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.foodapp.murti.Utils.Api;
import com.google.android.gms.maps.GoogleMap;
import com.foodapp.murti.R;
import com.foodapp.murti.Utils.AppController;
import com.foodapp.murti.Utils.Getseter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
//public class ContactUs extends Fragment implements OnMapReadyCallback{
public class ContactUs extends Fragment {
    private GoogleMap mMap;
    Dialog dialog;

    TextView email,fax,number,address;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
//        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        getActivity().setTitle("Contact Us");
        email=(TextView)view.findViewById(R.id.email);
        fax=(TextView)view.findViewById(R.id.fax);
        number=(TextView)view.findViewById(R.id.number);
        address=(TextView)view.findViewById(R.id.address);

//
//        MapFragment mapFragment1 = (MapFragment)getActivity().getFragmentManager().findFragmentById(R.id.map);
//        mapFragment1.getMapAsync(this);

        Getseter.showdialog(dialog);
        //JsonObjectRequest jsonObjectRequest2=new JsonObjectRequest(Request.Method.GET, "http://hoomiehome.com/appcredentials/jsondata.php?getSetting=1", null, new Response.Listener<JSONObject>() {
        JsonObjectRequest jsonObjectRequest2=new JsonObjectRequest(Request.Method.GET, Api.contactDetails, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Getseter.exitdialog(dialog);


                if (response.optString("status").equalsIgnoreCase("success")) {
                    try {

                        JSONObject  jsonObject=response.getJSONObject("message");
                        Log.d("dfsfsdfsfgsd","tree");



                        address.setText(jsonObject.optString("contact_address"));
                        number.setText(jsonObject.optString("contactno"));
                        fax.setText(jsonObject.optString("fax"));
                        email.setText(jsonObject.optString("email"));
Log.d("fsdfsdfsdfgsd",jsonObject.optString("contactno"));
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
        AppController.getInstance().addToRequestQueue(jsonObjectRequest2);


        return  view;
    }


//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney, Australia, and move the camera.
//        LatLng sydney = new LatLng(28.6988,77.2926);
////        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.611847, 77.388483), 12.0f));
//
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Home ie Home"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        mMap.animateCamera( CameraUpdateFactory.zoomTo(14) );
//    }
}
