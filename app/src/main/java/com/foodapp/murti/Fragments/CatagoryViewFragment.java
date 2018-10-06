package com.foodapp.murti.Fragments;


import android.app.Dialog;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.foodapp.murti.Activity.Navigation;
import com.foodapp.murti.R;
import com.foodapp.murti.Utils.Api;
import com.foodapp.murti.Utils.AppController;
import com.foodapp.murti.Utils.DatabaseHandler;
import com.foodapp.murti.Utils.Getseter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class CatagoryViewFragment extends Fragment {

    String image;
    NetworkImageView parallax_header_imageview;
    TextView mrp,disount,selling,name,desc,integer_number,button1,size;
    Dialog dialog;
    Button decrease,increase;
    static int number=1;
    DatabaseHandler db;
    List<Getseter> DataList=new ArrayList<Getseter>();
    JSONObject jsonObject;
    JSONObject jsonObject1;
    List<Getseter> Catag=new ArrayList<Getseter>();
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_cat_view, container, false);
        parallax_header_imageview=(NetworkImageView)view.findViewById(R.id.parallax_header_imageview);

        mrp=(TextView)view.findViewById(R.id.mrp);
        disount=(TextView)view.findViewById(R.id.disount);
        selling=(TextView)view.findViewById(R.id.selling);
        name=(TextView)view.findViewById(R.id.name);
        desc=(TextView)view.findViewById(R.id.desc);
        size=(TextView)view.findViewById(R.id.size);
        integer_number=(TextView)view.findViewById(R.id.integer_number);
        db = new DatabaseHandler(getActivity());
        decrease=(Button) view.findViewById(R.id.decrease);
        increase=(Button)view.findViewById(R.id.increase);
        button1=(TextView)view.findViewById(R.id.button1);
        number=1;
//        DataList=db.getAllCatagory();

        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (integer_number.getText().toString().equals("1"))
                {

                }
                else {
                    number = number - 1;
                    integer_number.setText("" + number);
                }
            }
        });

        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (integer_number.getText().toString().equals("5")){
                    Toast.makeText(getActivity(), "No more...", Toast.LENGTH_SHORT).show();
                }
                else {
                    number = number + 1;
                    integer_number.setText("" + number);
                }

            }
        });

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        Getseter.preferences = getActivity().getSharedPreferences("My_prefence", MODE_PRIVATE);
        Getseter.editor =Getseter.preferences.edit();

        Getseter.showdialog(dialog);



//        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, "http://hoomiehome.com/appcredentials/jsondata.php?getProductDetails=1&product_id="+getArguments().getString("product_id"), null, new Response.Listener<JSONObject>() {
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Api.productDetails+"?productId="+getArguments().getString("product_id"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("sdgsdgdf",getArguments().getString("product_id"));

                Log.d("fsddgdgdfgdfgd", String.valueOf(response));

                Getseter.exitdialog(dialog);
                if (response.optString("status").equalsIgnoreCase("success")){


                    JSONArray jsonArray =response.optJSONArray("message");
                    for (int i=0;i<jsonArray.length();i++){
                        jsonObject =jsonArray.optJSONObject(i);

                        JSONArray jsonArray1=jsonObject.optJSONArray("sizes");
                        for (int j=0;j<jsonArray1.length();j++){
                            jsonObject1=jsonArray1.optJSONObject(j);
                            mrp.setText("₹ "+jsonObject1.optString("mrp_price"));
                            mrp.setPaintFlags(mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            size.setText(jsonObject1.optString("size"));
                            disount.setText("₹ "+jsonObject1.optString("discount")+" OFF");
                            selling.setText(jsonObject1.optString("sell_price"));
                        }
                        //  DataList.add(new Getseter(jsonObject.optString("product_id"),jsonObject.optString("product_name"),jsonObject.optString("product_image"),null));

                        name.setText(jsonObject.optString("product_name"));

                        desc.setText(jsonObject.optString("description"));
                        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                        parallax_header_imageview.setImageUrl(jsonObject.optString("photo"),imageLoader);
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

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataList=db.getAllCatagory();
                int num=DataList.size();
                num=num+1;
                Navigation.textOne.setText(num+"");

                Log.d("fgdgfdgh", String.valueOf(DataList.size()));
                Log.d("fgdgfdgh", String.valueOf(num));

                Getseter.editor.putString("ordername",name.getText().toString());
                Getseter.editor.commit();
                double m1=Integer.parseInt(selling.getText().toString());
                double m2=Integer.parseInt(integer_number.getText().toString());
                double m=m1*m2;

//               double cal_price=selling.getText().toString()* integer_number.getText().toString();
                db.addCatagory(new Getseter(jsonObject1.optString("id"), jsonObject.optString("product_name"), jsonObject.optString("description"), jsonObject.optString("photo"), jsonObject1.optString("mrp_price"), jsonObject1.optString("sell_price"), integer_number.getText().toString(),String.valueOf(m) ));
                Toast.makeText(getActivity(), "Added in Cart", Toast.LENGTH_SHORT).show();

//                Catag=db.getAllCatagory();

//                db.addCatagory(new Getseter("dfd", "dfd", "dfd", "dfd"));
//                Log.d("dfgdfghdfhdfh",Catag.toString());
            }
        });


        return view;
    }

}
