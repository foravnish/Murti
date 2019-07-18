package com.foodapp.murti.Fragments;


import android.app.Dialog;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.foodapp.murti.Activity.Navigation;
import com.foodapp.murti.Activity.PaymentAct;
import com.foodapp.murti.Activity.ThankAct;
import com.foodapp.murti.R;
import com.foodapp.murti.Utils.Api;
import com.foodapp.murti.Utils.AppController;
import com.foodapp.murti.Utils.DatabaseHandler;
import com.foodapp.murti.Utils.Getseter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Delivery extends Fragment {


    TextView orderitem,subtotal,charge,total,address;
    Button checkout;
    Dialog dialog;
    String[] arr;
    DatabaseHandler db;
    List<Getseter> DataList=new ArrayList<Getseter>();
    String delCharge,emailString, phoneString;
    TextView edit;
    String paymentMode="Cash on Delivery";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_delivery, container, false);
        orderitem=(TextView)view.findViewById(R.id.orderitem);
        subtotal=(TextView)view.findViewById(R.id.subtotal);
        charge=(TextView)view.findViewById(R.id.charge);
        total=(TextView)view.findViewById(R.id.total);
        address=(TextView)view.findViewById(R.id.address);
        checkout=(Button) view.findViewById(R.id.checkout);

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        Getseter.showdialog(dialog);

        getActivity().setTitle("Payment");


        db = new DatabaseHandler(getActivity());
        orderitem.setText(getArguments().get("orderitem").toString()+" Item");
        subtotal.setText(getArguments().get("cal_price").toString());

        edit=(TextView)view.findViewById(R.id.edit);

        charge.setText("0");
        final RadioGroup gr =(RadioGroup)view.findViewById(R.id.radiogroup) ;
        final int selectedId = gr.getCheckedRadioButtonId();
        final RadioButton radioSexButton = (RadioButton) view.findViewById(selectedId);

        gr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb=(RadioButton)view.findViewById(checkedId);
//                textViewChoice.setText("You Selected "+rb.getText());
               // Toast.makeText(getActivity(),rb.getText()+"", Toast.LENGTH_SHORT).show();
                paymentMode=rb.getText().toString();
            }
        });





        chackingAddress();
       // Toast.makeText(getActivity(),radioSexButton.getText(), Toast.LENGTH_SHORT).show();


        JsonObjectRequest jsonObjectRequest2=new JsonObjectRequest(Request.Method.GET, Api.contactDetails, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("fgdgdgfgfdfgd",response.toString());

                Getseter.exitdialog(dialog);
                try {
                    JSONObject jsonObject=response.getJSONObject("message");


                    emailString= jsonObject.optString("email");
                    phoneString= jsonObject.optString("contactno");



                    if (    Getseter.preferences.getString("pincode","").equals("141001")||
                            Getseter.preferences.getString("pincode","").equals("141002")||
                            Getseter.preferences.getString("pincode","").equals("141003")||
                            Getseter.preferences.getString("pincode","").equals("141004")||
                            Getseter.preferences.getString("pincode","").equals("141005")||
                            Getseter.preferences.getString("pincode","").equals("141006")||
                            Getseter.preferences.getString("pincode","").equals("141007")||
                            Getseter.preferences.getString("pincode","").equals("141008")){




                        Log.d("Dgdfgdfgfg","true");
                                if (Double.parseDouble(getArguments().get("cal_price").toString())<400){
                                    delCharge= jsonObject.optString("del_charge");
                                }
                                else{
                                    delCharge= "0";
                                }
                    }
                    else{
                        Log.d("Dgdfgdfgfg","false");

                        Log.d("dgdfgdfgdfgsd",getArguments().getString("totalItem"));
                        int t_item=Integer.parseInt(getArguments().getString("totalItem"));

                        int nw_charge=Integer.parseInt(jsonObject.optString("del_charge"));

                        delCharge= String.valueOf(t_item*nw_charge);


                    }
                    charge.setText(delCharge+"");
                    Log.d("gdfghfghfgh",delCharge);
                    double t1=Double.parseDouble(subtotal.getText().toString());
                    double t2=Double.parseDouble(charge.getText().toString());
                    total.setText(String.valueOf(t1+t2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Please connect to the internet.", Toast.LENGTH_SHORT).show();
                Getseter.exitdialog(dialog);

                Fragment fragment=new CartFragment();
                FragmentManager fm=getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
                ft.replace(R.id.content_frame, fragment).addToBackStack(null);
                ft.commit();

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest2);



        db = new DatabaseHandler(getActivity());
        DataList=db.getAllCatagory();
//
//        Getseter.editor.putString("fname",fname.getText().toString());
//        Getseter.editor.putString("lname",lname.getText().toString());
//        Getseter.editor.putString("house_no",house.getText().toString());
//        Getseter.editor.putString("street",street.getText().toString());
//        Getseter.editor.putString("landmark",landmark.getText().toString());
//        Getseter.editor.putString("pincode",pincode.getText().toString());
//        Getseter.editor.putString("area",area.getText().toString());
//        Getseter.editor.putString("mobile",mobile.getText().toString());
////                        Getseter.editor.putString("city",city.getText().toString());

        address.setText("Address :"+" \n"+"Name: "+Getseter.preferences.getString("fname","")+" "+Getseter.preferences.getString("lname","")
        +" House No: "+Getseter.preferences.getString("house_no","")+" Street No: "+Getseter.preferences.getString("street","")
        +" Landmark: "+Getseter.preferences.getString("landmark","")+" Pincode: "+Getseter.preferences.getString("pincode","")
        +" Area: "+Getseter.preferences.getString("area","")+" City: "+Getseter.preferences.getString("city_name","")
        +" Mobile No: "+Getseter.preferences.getString("mobile",""));
//        Toast.makeText(getActivity(), ""+getArguments().getInt("length"), Toast.LENGTH_SHORT).show();


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new UpdateProfile();
                FragmentManager manager=getFragmentManager();
                Bundle bundle=new Bundle();
                bundle.putString("type","prod");
                bundle.putString("orderitem", getArguments().get("orderitem").toString());
                bundle.putString("cal_price",getArguments().get("cal_price").toString());
                bundle.putString("totalItem",getArguments().get("totalItem").toString());
                bundle.putInt("length",getArguments().getInt("length"));
                FragmentTransaction ft=manager.beginTransaction();
                fragment.setArguments(bundle);
                ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
            }
        });

        /// TODO WITHOUT VALIDATION



//        checkout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                placeOrder();
//
////                if (!Getseter.preferences.getString("fname","").toString().equals("") &&
////                        !Getseter.preferences.getString("lname","").toString().equals("") &&
////                        !Getseter.preferences.getString("house_no","").toString().equals("") &&
////                        !Getseter.preferences.getString("street","").toString().equals("") &&
////                        !Getseter.preferences.getString("area","").toString().equals("") &&
////                        !Getseter.preferences.getString("pincode","").toString().equals("") &&
////                        !Getseter.preferences.getString("city_name","").toString().equals("") &&
////                        !Getseter.preferences.getString("mobile","").toString().equals("")){
//////
////                    if (paymentMode.equals("Cash on Delivery")){
////
////                        placeOrder();
////
//////                        Intent intent=new Intent(getActivity(), ThankAct.class);
//////                        intent.putExtra("orderno",jsonObject1.optString("order_id"));
//////                        intent.putExtra("email",emailString.toString());
//////                        intent.putExtra("phone",phoneString.toString());
//////                        startActivity(intent);
//////
////                    }
////                    else if (paymentMode.equals("Credit/Debit card")){
////
////
////
////                        JSONArray cartItemsArray = null;
////
////                        try {
////
////                            cartItemsArray = new JSONArray();
////                            JSONObject cartItemsObjedct;
////                            for (int i = 0; i < getArguments().getInt("length"); i++) {
////                                cartItemsObjedct = new JSONObject();
////                                cartItemsObjedct.putOpt("Product_name", DataList.get(i).getName().toString());
////                                cartItemsObjedct.putOpt("Price", DataList.get(i).getCdate().toString());
////                                cartItemsObjedct.putOpt("Quantity", DataList.get(i).getUdate().toString());
////                                cartItemsObjedct.putOpt("Product_Id",DataList.get(i).getID().toString());
////                                cartItemsObjedct.putOpt("reciver_amount", DataList.get(i).getUdate3().toString());
////                                cartItemsArray.put(cartItemsObjedct);
////                                Log.d("fsdfsdgfsdgfds", String.valueOf(i));
////                            }
////                            //  dataObj.put("productArr", cartItemsArray);
////                        } catch (JSONException e) { // TODO Auto-generated catch bloc
////                            e.printStackTrace();
////                        }
////
//////                        params.put("productArr",cartItemsArray.toString());
////
////                        Log.d("dfgdgdfgdfhgdfh",cartItemsArray.toString());
////
////
////
////                        Intent intent=new Intent(getActivity(), PaymentAct.class);
////
////                        intent.putExtra("item",orderitem.getText().toString());
////                        intent.putExtra("subtotal",subtotal.getText().toString());
////                        intent.putExtra("charge",charge.getText().toString());
////                        intent.putExtra("total",total.getText().toString());
////                        intent.putExtra("productArr",cartItemsArray.toString());
////                        intent.putExtra("email",emailString.toString());
////                        intent.putExtra("phone",phoneString.toString());
////                        startActivity(intent);
////
////                    }
////                }
////                else{
////                    Fragment fragment=new UpdateProfile();
////                    FragmentManager manager=getFragmentManager();
////                    FragmentTransaction ft=manager.beginTransaction();
////                    ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
////                    ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
////
////                }
//
//            }
//
//            private void placeOrder() {
//                Getseter.showdialog(dialog);
//
//
//                StringRequest stringRequest=new StringRequest(Request.Method.POST, Api.placeUserOrders, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Getseter.exitdialog(dialog);
//                        Log.d("fgvdgdfgdfgd",response.toString());
//
//                        JSONObject jsonObject = null;
//                        try {
//                            jsonObject = new JSONObject(response);
//
//                            if (jsonObject.optString("status").equals("success")) {
//
//                                db.deleteCatogory();
//                                Navigation.textOne.setText("0");
//
//                                JSONObject jsonObject1=jsonObject.optJSONObject("message");
//
//
//                                Intent intent=new Intent(getActivity(), ThankAct.class);
//                                intent.putExtra("orderno",jsonObject1.optString("order_id"));
//                                intent.putExtra("email",emailString.toString());
//                                intent.putExtra("phone",phoneString.toString());
//                                startActivity(intent);
//
//                            }
//                            else{
//                                Toast.makeText(getActivity(), "Some error! Please try again...", Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getActivity(), "Please connect to the internet.", Toast.LENGTH_SHORT).show();
//                        Log.d("fgvdgdfgdfgd2",error.toString());
//                        Getseter.exitdialog(dialog);
//
//                    }
//                }){
//
//
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<String, String>();
//
//
//                        params.put("user_id",Getseter.preferences.getString("user_id",""));
//                        params.put("amount", subtotal.getText().toString());
//                        params.put("discount", "0");
//                        params.put("total_amount", subtotal.getText().toString());
//                        params.put("ship_fname",Getseter.preferences.getString("fname",""));
//                        params.put("ship_lname", Getseter.preferences.getString("lname",""));
//                        params.put("ship_email", Getseter.preferences.getString("emailid",""));
//                        params.put("ship_house_no", Getseter.preferences.getString("house_no",""));
//                        params.put("ship_street", Getseter.preferences.getString("street",""));
//                        params.put("ship_landmark", Getseter.preferences.getString("landmark",""));
//                        params.put("ship_pincode", Getseter.preferences.getString("pincode",""));
//                        params.put("ship_area", Getseter.preferences.getString("area",""));
//                        params.put("ship_city_id", Getseter.preferences.getString("city_id",""));
//                        params.put("ship_mobile",Getseter.preferences.getString("mobile",""));
//                        params.put("expres_amt", total.getText().toString());
//                        params.put("shipping_amount", delCharge.toString());
//                        params.put("mode", "COD");
//
//
//                        Log.d("user_id",Getseter.preferences.getString("user_id",""));
//                        Log.d("amount", subtotal.getText().toString());
//                        Log.d("discount", "0");
//                        Log.d("total_amount", subtotal.getText().toString());
//                        Log.d("ship_fname",Getseter.preferences.getString("fname",""));
//                        Log.d("ship_lname", Getseter.preferences.getString("lname",""));
//                        Log.d("ship_email", Getseter.preferences.getString("emailid",""));
//                        Log.d("ship_house_no", Getseter.preferences.getString("house_no",""));
//                        Log.d("ship_street", Getseter.preferences.getString("street",""));
//                        Log.d("ship_landmark", Getseter.preferences.getString("landmark",""));
//                        Log.d("ship_pincode", Getseter.preferences.getString("pincode",""));
//                        Log.d("ship_area", Getseter.preferences.getString("area",""));
//                        Log.d("ship_city_id", Getseter.preferences.getString("city_id",""));
//                        Log.d("ship_mobile",Getseter.preferences.getString("mobile",""));
//                        Log.d("expres_amt", total.getText().toString());
//                        Log.d("shipping_amount", delCharge.toString());
//
//
//                        //  JSONObject dataObj = new JSONObject();
//                        JSONArray cartItemsArray = null;
//
//                        try {
//                            cartItemsArray = new JSONArray();
//                            JSONObject cartItemsObjedct;
//                            for (int i = 0; i < getArguments().getInt("length"); i++) {
//                                cartItemsObjedct = new JSONObject();
//                                cartItemsObjedct.putOpt("Product_name", DataList.get(i).getName().toString());
//                                cartItemsObjedct.putOpt("Price", DataList.get(i).getCdate().toString());
//                                cartItemsObjedct.putOpt("Quantity", DataList.get(i).getUdate().toString());
//                                cartItemsObjedct.putOpt("Product_Id",DataList.get(i).getID().toString());
//                                cartItemsObjedct.putOpt("reciver_amount", DataList.get(i).getUdate3().toString());
//                                cartItemsArray.put(cartItemsObjedct);
//                                Log.d("fsdfsdgfsdgfds", String.valueOf(i));
//                            }
//                            //  dataObj.put("productArr", cartItemsArray);
//                        } catch (JSONException e) { // TODO Auto-generated catch bloc
//                            e.printStackTrace();
//                        }
//
//                        params.put("productArr",cartItemsArray.toString());
//
//                        Log.d("dfgdgdfgdfhgdfh",cartItemsArray.toString());
//
//
//                        return params;
//                    }
//                };
//
//
////                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
////                        0,
////                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
////                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
////                 AppController.getInstance().addToRequestQueue(stringRequest);
//
//                stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                stringRequest.setShouldCache(false);
//                AppController.getInstance().addToRequestQueue(stringRequest);
//
//            }
//        });



        ///TODO WITH VALIDATION
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (paymentMode.equals("Cash on Delivery")){

                    if (Getseter.preferences.getString("pincode", "").equals("141001") ||
                            Getseter.preferences.getString("pincode", "").equals("141002") ||
                            Getseter.preferences.getString("pincode", "").equals("141003") ||
                            Getseter.preferences.getString("pincode", "").equals("141004") ||
                            Getseter.preferences.getString("pincode", "").equals("141005") ||
                            Getseter.preferences.getString("pincode", "").equals("141006") ||
                            Getseter.preferences.getString("pincode", "").equals("141007") ||
                            Getseter.preferences.getString("pincode", "").equals("141008")) {


                        placeOrder();
                    }else{
                        Toast.makeText(getActivity(), "Sorry! Cash on Delivery not available on this pincode.", Toast.LENGTH_LONG).show();
                    }
//
                }
                else if (paymentMode.equals("Credit/Debit card")){



                    JSONArray cartItemsArray = null;

                    try {

                        cartItemsArray = new JSONArray();
                        JSONObject cartItemsObjedct;
                        for (int i = 0; i < getArguments().getInt("length"); i++) {
                            cartItemsObjedct = new JSONObject();
                            cartItemsObjedct.putOpt("Product_name", DataList.get(i).getName().toString());
                            cartItemsObjedct.putOpt("Price", DataList.get(i).getCdate().toString());
                            cartItemsObjedct.putOpt("Quantity", DataList.get(i).getUdate().toString());
                            cartItemsObjedct.putOpt("Product_Id",DataList.get(i).getID().toString());
                            cartItemsObjedct.putOpt("reciver_amount", DataList.get(i).getUdate3().toString());
                            cartItemsArray.put(cartItemsObjedct);
                            Log.d("fsdfsdgfsdgfds", String.valueOf(i));
                        }
                        //  dataObj.put("productArr", cartItemsArray);
                    } catch (JSONException e) { // TODO Auto-generated catch bloc
                        e.printStackTrace();
                    }

//                        params.put("productArr",cartItemsArray.toString());

                    Log.d("dfgdgdfgdfhgdfh",cartItemsArray.toString());



                    Intent intent=new Intent(getActivity(), PaymentAct.class);

                    intent.putExtra("item",orderitem.getText().toString());
                    intent.putExtra("subtotal",subtotal.getText().toString());
                    intent.putExtra("charge",charge.getText().toString());
                    intent.putExtra("total",total.getText().toString());
                    intent.putExtra("productArr",cartItemsArray.toString());
                    intent.putExtra("email",emailString.toString());
                    intent.putExtra("phone",phoneString.toString());
                    startActivity(intent);

                }




            }

            private void placeOrder() {
                Getseter.showdialog(dialog);


                StringRequest stringRequest=new StringRequest(Request.Method.POST, Api.placeUserOrders, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Getseter.exitdialog(dialog);
                        Log.d("fgvdgdfgdfgd",response.toString());

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                            if (jsonObject.optString("status").equals("success")) {

                                db.deleteCatogory();
                                Navigation.textOne.setText("0");

                                JSONObject jsonObject1=jsonObject.optJSONObject("message");


                                    Intent intent=new Intent(getActivity(), ThankAct.class);
                                    intent.putExtra("orderno",jsonObject1.optString("order_id"));
                                    intent.putExtra("email",emailString.toString());
                                    intent.putExtra("phone",phoneString.toString());
                                    startActivity(intent);



                            }
                            else{
                                Toast.makeText(getActivity(), "Some error! Please try again...", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Please connect to the internet.", Toast.LENGTH_SHORT).show();
                        Log.d("fgvdgdfgdfgd2",error.toString());
                        Getseter.exitdialog(dialog);

                    }
                }){




                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();


                        params.put("user_id",Getseter.preferences.getString("user_id",""));
                        params.put("amount", subtotal.getText().toString());
                        params.put("discount", "0");
                        params.put("total_amount", subtotal.getText().toString());
                        params.put("ship_fname",Getseter.preferences.getString("fname",""));
                        params.put("ship_lname", Getseter.preferences.getString("lname",""));
                        params.put("ship_email", Getseter.preferences.getString("emailid",""));
                        params.put("ship_house_no", Getseter.preferences.getString("house_no",""));
                        params.put("ship_street", Getseter.preferences.getString("street",""));
                        params.put("ship_landmark", Getseter.preferences.getString("landmark",""));
                        params.put("ship_pincode", Getseter.preferences.getString("pincode",""));
                        params.put("ship_area", Getseter.preferences.getString("area",""));
                        params.put("ship_city_id", Getseter.preferences.getString("city_id",""));
                        params.put("ship_mobile",Getseter.preferences.getString("mobile",""));
                        params.put("expres_amt", total.getText().toString());
                        params.put("shipping_amount", delCharge.toString());
                        params.put("mode", "COD");


                        Log.d("user_id",Getseter.preferences.getString("user_id",""));
                        Log.d("amount", subtotal.getText().toString());
                        Log.d("discount", "0");
                        Log.d("total_amount", subtotal.getText().toString());
                        Log.d("ship_fname",Getseter.preferences.getString("fname",""));
                        Log.d("ship_lname", Getseter.preferences.getString("lname",""));
                        Log.d("ship_email", Getseter.preferences.getString("emailid",""));
                        Log.d("ship_house_no", Getseter.preferences.getString("house_no",""));
                        Log.d("ship_street", Getseter.preferences.getString("street",""));
                        Log.d("ship_landmark", Getseter.preferences.getString("landmark",""));
                        Log.d("ship_pincode", Getseter.preferences.getString("pincode",""));
                        Log.d("ship_area", Getseter.preferences.getString("area",""));
                        Log.d("ship_city_id", Getseter.preferences.getString("city_id",""));
                        Log.d("ship_mobile",Getseter.preferences.getString("mobile",""));
                        Log.d("expres_amt", total.getText().toString());
                        Log.d("shipping_amount", delCharge.toString());


                      //  JSONObject dataObj = new JSONObject();
                        JSONArray cartItemsArray = null;

                        try {
                            cartItemsArray = new JSONArray();
                            JSONObject cartItemsObjedct;
                            for (int i = 0; i < getArguments().getInt("length"); i++) {
                                cartItemsObjedct = new JSONObject();
                                cartItemsObjedct.putOpt("Product_name", DataList.get(i).getName().toString());
                                cartItemsObjedct.putOpt("Price", DataList.get(i).getCdate().toString());
                                cartItemsObjedct.putOpt("Quantity", DataList.get(i).getUdate().toString());
                                cartItemsObjedct.putOpt("Product_Id",DataList.get(i).getID().toString());
                                cartItemsObjedct.putOpt("reciver_amount", DataList.get(i).getUdate3().toString());
                                cartItemsArray.put(cartItemsObjedct);
                                Log.d("fsdfsdgfsdgfds", String.valueOf(i));
                            }
                          //  dataObj.put("productArr", cartItemsArray);
                        } catch (JSONException e) { // TODO Auto-generated catch bloc
                            e.printStackTrace();
                        }

                        params.put("productArr",cartItemsArray.toString());

                        Log.d("dfgdgdfgdfhgdfh",cartItemsArray.toString());


                        return params;
                    }
                };


//                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                        0,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                 AppController.getInstance().addToRequestQueue(stringRequest);

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                stringRequest.setShouldCache(false);
                AppController.getInstance().addToRequestQueue(stringRequest);

            }
        });

        return view;
    }

    public void chackingAddress() {

        if (!Getseter.preferences.getString("fname","").toString().equals("") &&
                //   !Getseter.preferences.getString("lname","").toString().equals("") &&
                !Getseter.preferences.getString("house_no","").toString().equals("") &&
                !Getseter.preferences.getString("street","").toString().equals("") &&
                // !Getseter.preferences.getString("area","").toString().equals("") &&
                !Getseter.preferences.getString("pincode","").toString().equals("") &&
                !Getseter.preferences.getString("city_name","").toString().equals("") &&
                !Getseter.preferences.getString("mobile","").toString().equals("")){




        }
        else{
            Fragment fragment=new UpdateProfile();
            FragmentManager manager=getFragmentManager();
            Bundle bundle=new Bundle();
            bundle.putString("type","prod");
            bundle.putString("orderitem", getArguments().get("orderitem").toString());
            bundle.putString("cal_price",getArguments().get("cal_price").toString());
            bundle.putString("totalItem",getArguments().get("totalItem").toString());
            bundle.putInt("length",getArguments().getInt("length"));
            FragmentTransaction ft=manager.beginTransaction();
            fragment.setArguments(bundle);
            ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
            ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();

        }
    }

}
