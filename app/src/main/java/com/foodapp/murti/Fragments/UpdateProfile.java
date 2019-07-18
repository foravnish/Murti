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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateProfile extends Fragment {



    EditText fname,lname,house,street,complex,landmark,pincode,area,mobile,phone;
    Dialog dialog;
    Button submit;
    Spinner city;
    ArrayList<Getseter> DataList=new ArrayList<>();
    List<String> City_Names = new ArrayList<String>();
    String regexStr = "^[0-9]$";
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_update_profile2, container, false);

        fname=(EditText) view.findViewById(R.id.fname);
        lname=(EditText)view.findViewById(R.id.lname);
        house=(EditText)view.findViewById(R.id.house);
        street=(EditText)view.findViewById(R.id.street);
        complex=(EditText)view.findViewById(R.id.complex);
          landmark=(EditText)view.findViewById(R.id.landmark);
        city=(Spinner) view.findViewById(R.id.city);
        pincode=(EditText)view.findViewById(R.id.pincode);
//          area=(EditText)view.findViewById(R.id.area);
        mobile=(EditText)view.findViewById(R.id.mobile);
//        phone=(EditText)view.findViewById(R.id.phone);
        submit=(Button) view.findViewById(R.id.submit);

        getActivity().setTitle("Update Profile");


        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        Getseter.showdialog(dialog);

        Getseter.preferences = getActivity().getSharedPreferences("My_prefence", MODE_PRIVATE);
        Getseter.editor =Getseter.preferences.edit();


//        JsonObjectRequest jsonObjectRequest22=new JsonObjectRequest(Request.Method.GET, "http://hoomiehome.com/appcredentials/jsondata.php?getCities=1", null, new Response.Listener<JSONObject>() {
        JsonObjectRequest jsonObjectRequest22=new JsonObjectRequest(Request.Method.GET, Api.cityList, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Getseter.exitdialog(dialog);

                if (response.optString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = response.optJSONArray("message");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        DataList.add(new Getseter(jsonObject.optString("id"), jsonObject.optString("city"), null, null));
                        City_Names.add(jsonObject.optString("city"));
                    }
                    ArrayAdapter<String> adapter_state1 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, City_Names);
                    adapter_state1.setDropDownViewResource(R.layout.spinnertext);
                    city.setAdapter(adapter_state1);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Please connect to the internet.", Toast.LENGTH_SHORT).show();
                Getseter.exitdialog(dialog);
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest22);


        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("sdfvsdgvfdsgdf", DataList.get(position).getID());
                Getseter.editor.putString("city_id", DataList.get(position).getID());
                Getseter.editor.putString("city_name", DataList.get(position).getName());
                Getseter.editor.commit();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("sdfvsdgvfdsgdf", DataList.get(position).getID());
//            }
//        });

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Api.getUserDetails+"?userId="+Getseter.preferences.getString("user_id",""), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Getseter.exitdialog(dialog);

                Log.d("sdfsdfdgdgfg", String.valueOf(response));

                try {
                    if (response.optString("status").equals("success")) {
                        JSONArray jsonArray=response.getJSONArray("message");

                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);


                            fname.setText(jsonObject.optString("fname").toString());
                            lname.setText(jsonObject.optString("lname").toString());
                            house.setText(jsonObject.optString("house_no").toString());
                            street.setText(jsonObject.optString("street").toString());
                            complex.setText(jsonObject.optString("area").toString());
                            landmark.setText(jsonObject.optString("landmark").toString());
    //                city.setText(jsonObject.optString("city_id").toString());
                            pincode.setText(jsonObject.optString("pincode").toString());
                            complex.setText(jsonObject.optString("complex").toString());
                            mobile.setText(jsonObject.optString("mobile").toString());
    //                phone.setText(jsonObject.optString("phone").toString());

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




        /////  TODO Without Validation

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendData();


//                if (!fname.getText().toString().equals("")){
//                    if (!lname.getText().toString().equals("")){
//                        if (!house.getText().toString().equals("")){
//                            if (!street.getText().toString().equals("")){
//                                if (!complex.getText().toString().equals("")){
//                                    if (!pincode.getText().toString().equals("")){
//                                        if (!mobile.getText().toString().equals("")){
//                                            if (mobile.getText().toString().length()==10){
//                                                sendData();
//                                            }
//                                            else {
//                                                Toast.makeText(getActivity(), "Please enter Mobile number 10 digits", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                        else {
//                                            Toast.makeText(getActivity(), "Please enter Mobile number", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                    else{
//                                        Toast.makeText(getActivity(), "Please enter Pin code", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                                else{
//                                    Toast.makeText(getActivity(), "Please enter Complex", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                            else {
//                                Toast.makeText(getActivity(), "Please enter Street number", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        else{
//                            Toast.makeText(getActivity(), "Please enter House number", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    else{
//                        Toast.makeText(getActivity(), "Please enter Last Name", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                else
//                {
//                    Toast.makeText(getActivity(), "Please enter First Name", Toast.LENGTH_SHORT).show();
//                }



            }

            private void sendData() {

                Getseter.showdialog(dialog);
                //StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://hoomiehome.com/appcredentials/jsondata.php?", new Response.Listener<String>() {
                StringRequest stringRequest=new StringRequest(Request.Method.POST, Api.updateUserDetails, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Getseter.exitdialog(dialog);

                        Log.d("sdfdsgdfsgdsf",response);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                            if (jsonObject.optString("status").equals("success")) {

                                Toast.makeText(getActivity(), jsonObject.optString("message").toString(), Toast.LENGTH_SHORT).show();

                                if (getArguments().getString("type").equalsIgnoreCase("none")) {

                                    Toast.makeText(getActivity(), "Submit successfully.", Toast.LENGTH_SHORT).show();
//                                    Fragment fragment = new AccountDetails();
//                                    FragmentManager manager = getFragmentManager();
//                                    FragmentTransaction ft = manager.beginTransaction();
//                                    ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout, R.anim.frag_fade_right, R.anim.frag_fad_left);
//                                    ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                                }
                                else  if (getArguments().getString("type").equalsIgnoreCase("prod")) {

                                    Fragment fragment=new Delivery();
                                    FragmentManager manager=getFragmentManager();
                                    FragmentTransaction ft=manager.beginTransaction();
                                    ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
                                    ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
                                    Bundle bundle=new Bundle();
                                    bundle.putString("orderitem", getArguments().get("orderitem").toString());
                                    bundle.putString("cal_price", getArguments().get("cal_price").toString());
                                    bundle.putString("totalItem", getArguments().get("totalItem").toString());
                                    bundle.putInt("length", getArguments().getInt("length"));
                                    fragment.setArguments(bundle);

                                }
                            }
                            else {
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

                        // params.put("update_profile", "1");
                        params.put("userId", Getseter.preferences.getString("user_id",""));
                        params.put("fname", fname.getText().toString());
                        params.put("lname", lname.getText().toString());
                        params.put("house_no", house.getText().toString());
                        params.put("street", street.getText().toString());
                        params.put("complex", complex.getText().toString());
                        params.put("landmark", landmark.getText().toString());
                        params.put("pincode", pincode.getText().toString());
                        params.put("mobile", mobile.getText().toString());
                        //params.put("dob", "25/07/17");
                        params.put("city_id", Getseter.preferences.getString("city_id",""));

                        Getseter.editor.putString("fname",fname.getText().toString());
                        Getseter.editor.putString("lname",lname.getText().toString());
                        Getseter.editor.putString("house_no",house.getText().toString());
                        Getseter.editor.putString("street",street.getText().toString());
                        Getseter.editor.putString("area",complex.getText().toString());
                        Getseter.editor.putString("landmark",landmark.getText().toString());
                        Getseter.editor.putString("pincode",pincode.getText().toString());
                        Getseter.editor.putString("mobile",mobile.getText().toString());
                        Getseter.editor.putString("city_name",city.getSelectedItem().toString());
                        Getseter.editor.commit();

                        Log.d("fgdfgdfgdfdf",Getseter.preferences.getString("city_id",""));
                        Log.d("fgdfgdfgdfdf",Getseter.preferences.getString("user_id",""));
                        return params;
                    }
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                AppController.getInstance().addToRequestQueue(stringRequest);
            }
        });

        /////





////// TODO With Validation
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (!fname.getText().toString().equals("")){
//                    if (!lname.getText().toString().equals("")){
//                        if (!house.getText().toString().equals("")){
//                            if (!street.getText().toString().equals("")){
//                                if (!complex.getText().toString().equals("")){
//                                    if (!pincode.getText().toString().equals("")){
//                                        if (!mobile.getText().toString().equals("")){
//                                            if (mobile.getText().toString().length()==10){
//                                               sendData();
//                                            }
//                                            else {
//                                                Toast.makeText(getActivity(), "Please enter Mobile number 10 digits", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                        else {
//                                            Toast.makeText(getActivity(), "Please enter Mobile number", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                    else{
//                                        Toast.makeText(getActivity(), "Please enter Pin code", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                                else{
//                                    Toast.makeText(getActivity(), "Please enter Complex", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                            else {
//                                Toast.makeText(getActivity(), "Please enter Street number", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        else{
//                            Toast.makeText(getActivity(), "Please enter House number", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    else{
//                        Toast.makeText(getActivity(), "Please enter Last Name", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                else
//                {
//                    Toast.makeText(getActivity(), "Please enter First Name", Toast.LENGTH_SHORT).show();
//                }
//
//
//
//            }
//
//            private void sendData() {
//
//                Getseter.showdialog(dialog);
//                //StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://hoomiehome.com/appcredentials/jsondata.php?", new Response.Listener<String>() {
//                StringRequest stringRequest=new StringRequest(Request.Method.POST, Api.updateUserDetails, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Getseter.exitdialog(dialog);
//
//                        Log.d("sdfdsgdfsgdsf",response);
//                        JSONObject jsonObject = null;
//                        try {
//                            jsonObject = new JSONObject(response);
//
//                            if (jsonObject.optString("status").equals("success")) {
//
//                                Toast.makeText(getActivity(), jsonObject.optString("message").toString(), Toast.LENGTH_SHORT).show();
//
//                                Fragment fragment=new AccountDetails();
//                                FragmentManager manager=getFragmentManager();
//                                FragmentTransaction ft=manager.beginTransaction();
//                                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
//                                ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
//                            }
//                            else {
//                                Toast.makeText(getActivity(), jsonObject.optString("message").toString(), Toast.LENGTH_SHORT).show();
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getActivity(), "Please connect to the internet.", Toast.LENGTH_SHORT).show();
//                        Getseter.exitdialog(dialog);
//
//                    }
//                }){
//
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<String, String>();
//
//                       // params.put("update_profile", "1");
//                        params.put("userId", Getseter.preferences.getString("user_id",""));
//                        params.put("fname", fname.getText().toString());
//                        params.put("lname", lname.getText().toString());
//                        params.put("house_no", house.getText().toString());
//                        params.put("street", street.getText().toString());
//                        params.put("complex", complex.getText().toString());
//                        params.put("landmark", landmark.getText().toString());
//                        params.put("pincode", pincode.getText().toString());
//                        params.put("mobile", mobile.getText().toString());
//                        //params.put("dob", "25/07/17");
//                        params.put("city_id", Getseter.preferences.getString("city_id",""));
//
//                        Getseter.editor.putString("fname",fname.getText().toString());
//                        Getseter.editor.putString("lname",lname.getText().toString());
//                        Getseter.editor.putString("house_no",house.getText().toString());
//                        Getseter.editor.putString("street",street.getText().toString());
//                        Getseter.editor.putString("area",complex.getText().toString());
//                        Getseter.editor.putString("landmark",landmark.getText().toString());
//                        Getseter.editor.putString("pincode",pincode.getText().toString());
//                        Getseter.editor.putString("mobile",mobile.getText().toString());
//                        Getseter.editor.putString("city_name",city.getSelectedItem().toString());
//                        Getseter.editor.commit();
//
//                        Log.d("fgdfgdfgdfdf",Getseter.preferences.getString("city_id",""));
//                        Log.d("fgdfgdfgdfdf",Getseter.preferences.getString("user_id",""));
//                        return params;
//                    }
//                };
//
//                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                        0,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//                 AppController.getInstance().addToRequestQueue(stringRequest);
//            }
//        });

        return view;
    }

}
