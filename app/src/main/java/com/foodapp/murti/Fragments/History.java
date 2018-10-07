package com.foodapp.murti.Fragments;


import android.app.Dialog;
import android.content.Context;
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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.foodapp.murti.R;
import com.foodapp.murti.Utils.Api;
import com.foodapp.murti.Utils.AppController;
import com.foodapp.murti.Utils.Getseter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class History extends Fragment {

    private ImageView image;
    GridView gridview;
    ArrayList<Getseter> DataList=new ArrayList<>();
    Fragment fragment;
    Adapter adapter;
    Dialog dialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_history, container, false);

        gridview=(GridView)view.findViewById(R.id.gridview);

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        adapter=new Adapter();

        Getseter.showdialog(dialog);

//
//        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, "http://hoomiehome.com/appcredentials/jsondata.php?order_history=1&user_id="+Getseter.preferences.getString("user_id",""), null, new Response.Listener<JSONObject>() {
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Api.orderHistory+"?userId="+Getseter.preferences.getString("user_id",""), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                DataList.clear();
                Getseter.exitdialog(dialog);

                Log.d("fdssddgfd",Getseter.preferences.getString("user_id",""));


                if (response.optString("status").equals("failure")){
                    Toast.makeText(getActivity(), "No order here", Toast.LENGTH_SHORT).show();
                }

                else {
                    try {
                        JSONArray jsonArray = response.optJSONArray("message");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.optJSONObject(i);

                            DataList.add(new Getseter(jsonObject.optString("order_date"), jsonObject.optString("id"), jsonObject.optString("shipping_amount"), jsonObject.optString("total_amount"), jsonObject.optString("payment_method"), jsonObject.optString("order_status"), null,null));

                            gridview.setAdapter(adapter);
                        }
                    } catch (Exception e) {
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


//        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Fragment fragment=new HistoryDetails();
//                FragmentManager manager=getFragmentManager();
//                FragmentTransaction ft=manager.beginTransaction();
//                ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
//                Bundle bundle=new Bundle();
//                bundle.putString("order_id",DataList.get(position).getName().toString());
//                bundle.putString("totalamount",DataList.get(position).getCount().toString());
//                fragment.setArguments(bundle);
//            }
//        });

        return view;
    }

    class  Adapter extends BaseAdapter {

        NetworkImageView image;
        LayoutInflater inflater;
        TextView status,method,amount,shipping,date;
        Adapter(){
            inflater=(LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        @Override
        public int getCount() {
            return DataList.size();
        }

        @Override
        public Object getItem(int position) {
            return DataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView=inflater.inflate(R.layout.custonlistview_history,parent,false);


            date=(TextView)convertView.findViewById(R.id.date);
            shipping=(TextView)convertView.findViewById(R.id.shipping);
            method=(TextView)convertView.findViewById(R.id.method);
            amount=(TextView)convertView.findViewById(R.id.amount);
            status=(TextView)convertView.findViewById(R.id.status);


            date.setText(DataList.get(position).getID().toString());
            shipping.setText("₹ "+DataList.get(position).getDesc().toString());
            amount.setText("₹ "+DataList.get(position).getCount().toString());
            method.setText(DataList.get(position).getImg().toString());
            status.setText(DataList.get(position).getCdate().toString());

            return convertView;
        }
    }


}
