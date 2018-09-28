package com.foodapp.murti.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sub_catagory_fragment_page, container, false);

        progressBar = (RelativeLayout) view.findViewById(R.id.rltv_progressBar);
        products_rclv = (RecyclerView) view.findViewById(R.id.rclv_products);
        products_arrayList = new ArrayList<>();
        String ProductsUrl = "http://www.json-generator.com/api/json/get/ckDtZPkgJe?indent=6";

        Log.d("sdfsdfsdfsdfsd",getArguments().getString("cat_id"));

        getProductData(Api.subCategoriesList+"?catId=" + getArguments().getString("cat_id").toString()+"&page_no=0");

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
//                                        products_pojo.setTitle(products_jsonObject.getString("name"));
                                    map.put("id", products_jsonObject.getString("id"));
                                    map.put("product_name", products_jsonObject.getString("product_name"));
                                    map.put("product_code", products_jsonObject.getString("product_code"));
                                    map.put("photo", products_jsonObject.getString("photo"));
                                    map.put("photo", products_jsonObject.getString("photo"));
                                    map.put("description", products_jsonObject.getString("description"));
                                }
                            }
                            products_arrayList.add(map);
                        }
//                            mactView.updateData(products_arrayList);

                        if (pageIndex == 0) {
                            productsAdapter = new ProductsAdapter(getActivity(), products_arrayList);

                            products_rclv.setAdapter(productsAdapter);

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



}
