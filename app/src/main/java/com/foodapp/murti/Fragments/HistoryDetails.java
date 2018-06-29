package com.foodapp.murti.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.foodapp.murti.R;
import com.foodapp.murti.Utils.AppController;
import com.foodapp.murti.Utils.Getseter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryDetails extends Fragment {


    TextView date,time,orderid,name,address,contact,amount,discount,shippinamount,totalamount,payment_method,ordername,orderqty,orderprice;
    Dialog dialog;
    ListView listview;
    List<Getseter> DataList=new ArrayList<>();
    Adapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_history_details, container, false);

        date=(TextView)view.findViewById(R.id.date);
        time=(TextView)view.findViewById(R.id.time);
        orderid=(TextView)view.findViewById(R.id.orderid);
        name=(TextView)view.findViewById(R.id.name);
        address=(TextView)view.findViewById(R.id.address);
        contact=(TextView)view.findViewById(R.id.contact);
        amount=(TextView)view.findViewById(R.id.amount);
        discount=(TextView)view.findViewById(R.id.discount);
        shippinamount=(TextView)view.findViewById(R.id.shippinamount);
        totalamount=(TextView)view.findViewById(R.id.totalamount);
        payment_method=(TextView)view.findViewById(R.id.payment_method);
        listview=(ListView) view.findViewById(R.id.listview);
        adapter=new Adapter();
//        ordername=(TextView)view.findViewById(R.id.ordername);
//        orderqty=(TextView)view.findViewById(R.id.orderqty);
//        orderprice=(TextView)view.findViewById(R.id.orderprice);

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        Getseter.showdialog(dialog);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, "http://hoomiehome.com/appcredentials/jsondata.php?order_details=1&user_id="+Getseter.preferences.getString("user_id","")+"&order_id="+getArguments().getString("order_id",""), null, new Response.Listener<JSONObject>() {
//            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, "http://hoomiehome.com/appcredentials/jsondata.php?order_history=1&user_id="+Getseter.preferences.getString("user_id",""), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                Getseter.exitdialog(dialog);
                DataList.clear();
                JSONArray jsonArray=response.optJSONArray("orders");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.optJSONObject(i);

                    date.setText(jsonObject.optString("order_date"));
                    time.setText(jsonObject.optString("order_time"));
                    orderid.setText(jsonObject.optString("order_id"));
                    name.setText(jsonObject.optString("name").toUpperCase());
                    address.setText(jsonObject.optString("address"));
                    contact.setText(jsonObject.optString("contact"));
                    amount.setText("₹ "+jsonObject.optString("amount"));
                    discount.setText("₹ "+jsonObject.optString("discount"));
                    shippinamount.setText("₹ "+jsonObject.optString("shipping_amount"));
//                    totalamount.setText("₹ "+jsonObject.optString("total_amount"));
                    totalamount.setText("₹ "+getArguments().getString("totalamount").toString());
                    payment_method.setText(jsonObject.optString("payment_method"));

                    JSONArray jsonArray1=jsonObject.optJSONArray("itmes");
                    for (int j=0;j<jsonArray1.length();j++){
                        JSONObject jsonObject1=jsonArray1.optJSONObject(j);
                          DataList.add(new Getseter(jsonObject1.optString("item_name"),jsonObject1.optString("price"),jsonObject1.optString("qty"),jsonObject1.optString("subtotal")));
//                        ordername.setText(jsonObject1.optString("item_name"));
//                        orderqty.setText(jsonObject1.optString("qty"));
//                        orderprice.setText("₹ "+jsonObject1.optString("price"));
                        listview.setAdapter(adapter);
                        Log.d("gdfgdfhgdfh",jsonObject.optString("payment_method"));
                        Log.d("gdfgdfhgdfh",jsonObject1.optString("qty"));
                    }
                  //  DataList.add(new Getseter(jsonObject.optString("order_date"),jsonObject.optString("order_time"),jsonObject.optString("order_id"),jsonObject.optString("total_amount"),jsonObject.optString("payment_method"),jsonObject.optString("order_status"),null));


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

    static class ViewHolder {


        TextView name,price,qty,subtotal;

    }


    class  Adapter extends BaseAdapter {

        LayoutInflater inflater;
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
            ViewHolder viewHolder;
            convertView=inflater.inflate(R.layout.custonlistview1,parent,false);
            viewHolder = new ViewHolder();

            viewHolder.name=(TextView)convertView.findViewById(R.id.name);
            viewHolder.price=(TextView)convertView.findViewById(R.id.price);
            viewHolder.qty=(TextView)convertView.findViewById(R.id.qty);
            viewHolder.subtotal=(TextView)convertView.findViewById(R.id.subtotal);

            viewHolder.name.setText(DataList.get(position).getID().toString());
            viewHolder.price.setText(DataList.get(position).getName().toString());
            viewHolder.qty.setText(DataList.get(position).getDesc().toString());
            viewHolder.subtotal.setText(DataList.get(position).getCount().toString());


            return convertView;
        }
    }



}
