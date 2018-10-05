package com.foodapp.murti.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
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
public class SubCatagoryFragment extends Fragment {

    public int counting;
    Button button1,button2,button3,button4,button5,button6;
    String cc;

    ArrayList<Getseter> DataList=new ArrayList<>();
    ArrayList<Getseter> DataList2=new ArrayList<>();
    Fragment fragment;
    Adapter adapter;
    //    Adapter2 adapter2;
    Dialog dialog;
    GridView gridview;
    JSONObject jsonObject;
    JSONArray jsonArray;
    LinearLayout liner;
  //  JSONObject jsonObject1;
    //    Adapter2 adapter2;
    public SubCatagoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view= inflater.inflate(R.layout.fragment_catagory, container, false);

        gridview=(GridView)view.findViewById(R.id.gridview);


        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        adapter=new Adapter();
//        adapter2=new Adapter2();
        //adapter2= new Adapter2();

        Getseter.showdialog(dialog);


        if (getArguments().getString("type").equalsIgnoreCase("search")){

           // searchApi();
        }

        else if (getArguments().getString("type").equalsIgnoreCase("normal")) {

            Log.d("fsdgdsfgdfgh", getArguments().getString("cat_id").toString());
            //JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, "http://hoomiehome.com/appcredentials/jsondata.php?getProducts=1&cat_id=60"+getArguments().getString("cat_id").toString(), null, new Response.Listener<JSONObject>() {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Api.subCategoriesList + "?catId=" + getArguments().getString("cat_id").toString(), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.d("dgdfgdfgdfgsdfgswts", String.valueOf(response));
                    DataList.clear();
                    Getseter.exitdialog(dialog);
                    if (response.optString("status").equalsIgnoreCase("success"))
                        jsonArray = response.optJSONArray("message");

                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.optJSONObject(i);


                            JSONArray jsonArray1 = jsonObject.optJSONArray("sizes");
                            DataList2.clear();
                            Log.d("fdgdgdfgd", jsonArray1.toString());
                            for (int j = 0; j < jsonArray1.length(); j++) {
                               JSONObject jsonObject1 = jsonArray1.optJSONObject(j);

                                Log.d("gdfgdfgdfghdfgs", jsonArray1.toString());
                                Log.d("fdgdfdgdfggdfgd", jsonObject1.optString("sell_price"));

                                DataList.add(new Getseter(jsonObject.optString("id"), jsonObject.optString("product_name"), jsonObject.optString("photo"), jsonObject1.optString("id"), jsonObject1.optString("psize_id"), jsonObject1.optString("size"), jsonObject1.optString("mrp_price"), jsonObject1.optString("discount"), jsonObject1.optString("sell_price"), jsonObject1.optString("psize_image")));
//                                DataList.add(new Getseter(jsonObject.optString("id"), jsonObject.optString("product_name"), jsonObject.optString("photo"), null, null, null, null, null,null,null));

                                //DataList2.add(new Getseter(jsonObject1.optString("product_id"),jsonObject1.optString("psize_id"),jsonObject1.optString("size"),jsonObject1.optString("mrp_price"),jsonObject1.optString("discount"),jsonObject1.optString("sell_price"),jsonObject1.optString("psize_image")));


                            }
                            //gridview2.setAdapter(adapter2);
                            gridview.setAdapter(adapter);

                        }
                    } catch (Exception e) {
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
        }

//        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Fragment fragment=new CatagoryViewFragment();
//                FragmentManager manager=getFragmentManager();
//                FragmentTransaction ft=manager.beginTransaction();
//                ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
//                Bundle bundle=new Bundle();
//                bundle.putString("product_id",DataList.get(position).getID().toString());
//                //bundle.putString("product_image",DataList.get(position).getDesc().toString());
//                fragment.setArguments(bundle);
//            }
//        });


        return view;
    }

//    private void searchApi() {
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Api.searchSubCat + "?keyword=" + getArguments().getString("query").toString(), null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                DataList.clear();
//                Getseter.exitdialog(dialog);
//                if (response.optString("status").equalsIgnoreCase("success")) {
//                    jsonArray = response.optJSONArray("message");
//
//                    try {
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            jsonObject = jsonArray.optJSONObject(i);
//
//                            JSONArray jsonArray1 = jsonObject.optJSONArray("sizes");
//                            //DataList2.clear();
//                            Log.d("fdgdgdfgd", jsonArray1.toString());
//                            for (int j = 0; j < jsonArray1.length(); j++) {
//                                jsonObject1 = jsonArray1.optJSONObject(j);
//
//                                Log.d("gdfgdfgdfghdfgs", jsonArray1.toString());
//                                Log.d("fdgdfdgdfggdfgd", jsonObject1.optString("sell_price"));
//
//                                DataList.add(new Getseter(jsonObject.optString("id"), jsonObject.optString("product_name"), jsonObject.optString("photo"), jsonObject1.optString("id"), jsonObject1.optString("psize_id"), jsonObject1.optString("size"), jsonObject1.optString("mrp_price"), jsonObject1.optString("discount"), jsonObject1.optString("sell_price"), jsonObject1.optString("psize_image")));
//
//                                //DataList2.add(new Getseter(jsonObject1.optString("product_id"),jsonObject1.optString("psize_id"),jsonObject1.optString("size"),jsonObject1.optString("mrp_price"),jsonObject1.optString("discount"),jsonObject1.optString("sell_price"),jsonObject1.optString("psize_image")));
//
//
//                            }
//                            //gridview2.setAdapter(adapter2);
//                            gridview.setAdapter(adapter);
//
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                else{
//                    Toast.makeText(getActivity(), "No product found...", Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(), "Please connect to the internet.", Toast.LENGTH_SHORT).show();
//                Getseter.exitdialog(dialog);
//            }
//        });
//        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
//
//    }

    class  Adapter extends BaseAdapter {

        NetworkImageView image;
        LayoutInflater inflater;
        GridView gridview2;



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
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView=inflater.inflate(R.layout.custon_subcatagory_view,parent,false);

            // gridview2=(GridView)convertView.findViewById(R.id.gridview2);

            // adapter2=new Adapter2();

//            JSONArray jsonArray1=jsonObject.optJSONArray("sizes");
//            DataList2.clear();
//            Log.d("fdgdgdfgd",jsonArray1.toString());
//            for(int j=0;j<jsonArray1.length();j++){
//                jsonObject1=jsonArray1.optJSONObject(j);
//                Log.d("gdfgdfgdfghdfgs",jsonArray1.toString());
//                Log.d("fdgdfdgdfggdfgd",jsonObject1.optString("psize_id"));
//
//                DataList2.add(new Getseter(jsonObject1.optString("product_id"),jsonObject1.optString("psize_id"),jsonObject1.optString("size"),jsonObject1.optString("mrp_price"),jsonObject1.optString("discount"),jsonObject1.optString("sell_price"),jsonObject1.optString("psize_image")));
//
            // gridview2.setAdapter(adapter2);
//            }

            image =(NetworkImageView)convertView.findViewById(R.id.imageView);
            TextView name=(TextView)convertView.findViewById(R.id.name);
            TextView price=(TextView)convertView.findViewById(R.id.price);
            TextView off=(TextView)convertView.findViewById(R.id.off);
            TextView newprice=(TextView)convertView.findViewById(R.id.newprice);
            TextView size=(TextView)convertView.findViewById(R.id.size);

            LinearLayout liner=(LinearLayout)convertView.findViewById(R.id.liner);

   

            Button button1=(Button)convertView.findViewById(R.id.button1);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getActivity(), "cart", Toast.LENGTH_SHORT).show();
                    Fragment fragment=new CatagoryViewFragment();
                    FragmentManager manager=getFragmentManager();
                    FragmentTransaction ft=manager.beginTransaction();
                    ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
                    ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
                    Bundle bundle=new Bundle();
                    bundle.putString("product_id",DataList.get(position).getID().toString());
                    //bundle.putString("product_image",DataList.get(position).getDesc().toString());
                    Log.d("gdfgdfgdfhdfh", String.valueOf(position));
                    Log.d("gdfgdfgdfhdfh", DataList.get(position).getID().toString());
                    fragment.setArguments(bundle);
                }
            });


            liner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getActivity(), "cc", Toast.LENGTH_SHORT).show();
                    Fragment fragment=new CatagoryViewFragment();
                    FragmentManager manager=getFragmentManager();
                    FragmentTransaction ft=manager.beginTransaction();
                    ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
                    ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
                    Bundle bundle=new Bundle();
                    bundle.putString("product_id",DataList.get(position).getID().toString());
                    //bundle.putString("product_image",DataList.get(position).getDesc().toString());
                    Log.d("gdfgdfgdfhdfh", String.valueOf(position));
                    fragment.setArguments(bundle);
                }
            });
//            gridview2=(GridView)convertView.findViewById(R.id.gridview2);

//            adapter2=new Adapter2();

            //gridview2.setAdapter(adapter2);


            ImageLoader imageLoader = AppController.getInstance().getImageLoader();

            name.setText(DataList.get(position).getName().toString());
           // size.setText(DataList.get(position).getCdate().toString());
           // price.setText("₹ "+DataList.get(position).getUdate().toString());
          //  off.setText("₹ "+DataList.get(position).getImg2().toString()+" OFF");
          //  newprice.setText("₹ "+DataList.get(position).getCdate2().toString());
          //  price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            //Log.d("fdgdgdfg",DataList.get(position).getCdate().toString());
            image.setImageUrl(DataList.get(position).getDesc().toLowerCase(),imageLoader);

            //gridview2.setAdapter(adapter2);
            return convertView;
        }
    }
//    class  Adapter2 extends BaseAdapter {
//
//        NetworkImageView image;
//        LayoutInflater inflater;
//        Adapter2(){
//            inflater=(LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        }
//        @Override
//        public int getCount() {
//            return DataList2.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return DataList2.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            convertView=inflater.inflate(R.layout.custon_subcatagory_view2,parent,false);
//
////            image =(NetworkImageView)convertView.findViewById(R.id.imageView);
////            TextView name=(TextView)convertView.findViewById(R.id.name);
//            TextView price=(TextView)convertView.findViewById(R.id.price);
//
////            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
//
////            name.setText(DataList.get(position).getName().toString());
//            price.setText(DataList2.get(position).getCdate().toString());
//            Log.d("fdgdgdfg",DataList2.get(position).getCdate().toString());
////            image.setImageUrl(DataList.get(position).getDesc().toLowerCase(),imageLoader);
//
//            return convertView;
//        }
//    }


}
