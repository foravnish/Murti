package com.foodapp.murti.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.foodapp.murti.ProductsAdapter;
import com.foodapp.murti.R;
import com.foodapp.murti.Utils.Api;
import com.foodapp.murti.Utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubCatagoryFragmentPage extends Fragment {


    public SubCatagoryFragmentPage() {
        // Required empty public constructor
    }

    private int pageIndex = 0;
    private RelativeLayout progressBar;
    private RecyclerView products_rclv;
    private ArrayList<HashMap<String, String>> products_arrayList;
    private LinearLayoutManager linearLayoutManager;
    private boolean isLoading = false;
    ProductsAdapter productsAdapter;
    JSONObject jsonObject;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sub_catagory_fragment_page, container, false);

        progressBar = (RelativeLayout) view.findViewById(R.id.rltv_progressBar);
        products_rclv = (RecyclerView) view.findViewById(R.id.rclv_products);
        products_arrayList = new ArrayList<>();


        Log.d("sdfsdfsdfsdfsd",getArguments().getString("cat_id"));

        getProductData(Api.subCategoriesList+"?catId=" + getArguments().getString("cat_id").toString()+"&page_no=0");


        products_rclv.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        Log.d("fsdfsdfsdgfsd", "dfgdfg"+position);


                Fragment fragment=new CatagoryViewFragment();
                FragmentManager manager=getFragmentManager();
                FragmentTransaction ft=manager.beginTransaction();
                ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
                Bundle bundle=new Bundle();
                bundle.putString("product_id",products_arrayList.get(position).get("id").toString());
                //bundle.putString("product_image",DataList.get(position).getDesc().toString());
                fragment.setArguments(bundle);

                    }
                })
        );


        products_rclv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                        isLoading = true;

                        pageIndex++;

//                        String ProductsUrl = ConstantData.service_URL + "search?page="+pageIndex+"&pageSize=15";
                        String ProductsUrl = Api.subCategoriesList+"?catId=" + getArguments().getString("cat_id").toString()+"&page_no="+pageIndex;

                        getProductData(ProductsUrl);
                    }
                }
            }
        });


        return view;
    }

    private void getProductData(String ProductsUrl) {
        progressBar.setVisibility(View.VISIBLE);

        final int pageNumber = pageIndex;

        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ProductsUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.length() >= 1) {
                    //  mainActPresenter.setAllData(response,pageIndex);

                    try {
                        Log.d("gdfgdfgdfgdfgdf", String.valueOf(response));
                        JSONArray productsJsonArray = response.getJSONArray("message");

                        // if (productsJsonArray.length() >= 1) {
//                            if (pageIndex == 0) {
//                                products_arrayList.clear();// --> Clear method is used to clear the ArrayList
//                            }

                        for (int i = 0; i < productsJsonArray.length(); i++) {
                            JSONObject products_jsonObject = productsJsonArray.getJSONObject(i);

//                                Products_Pojo products_pojo = new Products_Pojo();
                            HashMap<String, String> map = new HashMap<>();

                            if (products_jsonObject.has("id")) {
                                if (!(products_jsonObject.isNull("id"))) {


                                    Log.d("dfgdfgdfgdfvbcbcvbcgdf","true1");
                                    JSONArray jsonArraySizes=products_jsonObject.getJSONArray("sizes");
                                    Log.d("dfgdfgdfgdfvbcbcvbcgdf","true2");
                                    if (jsonArraySizes.length()!=0) {
                                        jsonObject = jsonArraySizes.getJSONObject(0);
                                        Log.d("fdgdfdgdfggdfgd", jsonObject.optString("sell_price"));
                                        map.put("mrp_price", jsonObject.getString("mrp_price"));
                                        map.put("sell_price", jsonObject.getString("sell_price"));
                                        map.put("discount", jsonObject.getString("discount"));
                                    }
                                    Log.d("dfgdfgdfgdfvbcbcvbcgdf","true3");
                                    Log.d("gdfgdfgdfghdfgs", jsonArraySizes.toString());

//                                        products_pojo.setTitle(products_jsonObject.getString("name"));
                                    map.put("id", products_jsonObject.getString("id"));
                                    map.put("product_name", products_jsonObject.getString("product_name"));
                                    map.put("product_code", products_jsonObject.getString("product_code"));
                                    map.put("photo", products_jsonObject.getString("photo"));
                                    map.put("description", products_jsonObject.getString("description"));

                                }
                            }
                            products_arrayList.add(map);
                        }
//                            mactView.updateData(products_arrayList);

                        if (pageIndex == 0) {
                            productsAdapter = new ProductsAdapter(getActivity(), products_arrayList,products_rclv);

                            products_rclv.setAdapter(productsAdapter);

//                            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
                           // products_rclv.setLayoutManager(mLayoutManager);

                            linearLayoutManager = new LinearLayoutManager(getActivity());
                            products_rclv.setLayoutManager(linearLayoutManager);
                        } else {
                            isLoading = false;
                            if (productsAdapter != null) {
                                // notifyDataSetChanged is used to Notify The Adapter afer having Changes in RecyclerView
                                productsAdapter.notifyDataSetChanged();
                            }
                        }

//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    if (pageNumber != 0) {
                        pageIndex = pageNumber - 1;
                    }
                }

                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        mJsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        MySingleton.getInstance(MainActivity.this).addToRequestQueue(mJsonObjectRequest);
        AppController.getInstance().addToRequestQueue(mJsonObjectRequest);

    }


    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View childView = rv.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, rv.getChildAdapterPosition(childView));
            }
            return false;
        }

        public interface OnItemClickListener {
            public void onItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

//        @Override
//        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
//            View childView = view.findChildViewUnder(e.getX(), e.getY());
//            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
//                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
//            }
//            return false;
//        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


}
