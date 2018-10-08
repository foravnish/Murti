package com.foodapp.murti.Fragments;


import android.app.Dialog;
import android.content.Context;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.eftimoff.viewpagertransformers.ForegroundToBackgroundTransformer;
import com.foodapp.murti.R;
import com.foodapp.murti.Utils.Api;
import com.foodapp.murti.Utils.AppController;
import com.foodapp.murti.Utils.DatabaseHandler;
import com.foodapp.murti.Utils.Getseter;
import com.jakewharton.disklrucache.Util;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFagment extends Fragment  {

    private ImageView image;
    ListView gridview;
    Fragment fragment;
    Adapter adapter;
    Dialog dialog;
    ViewPager viewPager;
    CircleIndicator indicator ;
    CustomPagerAdapter  customPagerAdapter;
    List<Getseter> ImageList = new ArrayList<Getseter>();
    int myLastVisiblePos;
    RelativeLayout relative;
    Thread thread;

    int currentPage;
    DatabaseHandler db;
    List<Getseter> DataList=new ArrayList<Getseter>();
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000; // time in milliseconds between successive task executions.

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    List<HashMap<String,String>> AllProducts ;
    ShimmerTextView head_daily_needs;
    Shimmer shimmer;
    int position;
    private static int NUM_PAGES=0;
    private Handler handler=new Handler();
    private Runnable runnale=new Runnable(){
        public void run(){
            viewPager.setCurrentItem(position,true);
            if(position>=NUM_PAGES ) position=0;
            else position++;
            // Move to the next page after 10s
            handler.postDelayed(runnale, PERIOD_MS);
        }
    };





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home_fagment, container, false);

        gridview=(ListView) view.findViewById(R.id.gridview);
        relative=(RelativeLayout)view.findViewById(R.id.relative);
        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        db = new DatabaseHandler(getActivity());
        adapter=new Adapter();
        AllProducts = new ArrayList<>();




        position=0;

        /*
          check 2

         */

        //TODO Tranding
        getTrading();


        // END Tranding


        LayoutInflater myinflater=(LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup myHeader = (ViewGroup) myinflater.inflate(R.layout.fragment_placeholder1, gridview, false);
        gridview.addHeaderView(myHeader, null, false);


        viewPager= (ViewPager)myHeader. findViewById(R.id.viewpager);
        customPagerAdapter=new CustomPagerAdapter();
        indicator = (CircleIndicator)myHeader.findViewById(R.id.indicator);


        mRecyclerView = (RecyclerView) myHeader.findViewById(R.id.recycler_view2);
        head_daily_needs = (ShimmerTextView) myHeader.findViewById(R.id.head_daily_needs);
        mRecyclerView.setHasFixedSize(true);

        shimmer = new Shimmer();
        shimmer.start(head_daily_needs);
        // The number of Columns
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        viewPager.setPageTransformer(true, new ForegroundToBackgroundTransformer());
        Getseter.showdialog(dialog);


        mRecyclerView.addOnItemTouchListener(
                new SubCatagoryFragmentPage.RecyclerItemClickListener(getActivity(), new SubCatagoryFragmentPage.RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        Log.d("fsdfsdfsdgfsd", "dfgdfg"+position);


                        Fragment fragment=new CatagoryViewFragment();
                        FragmentManager manager=getFragmentManager();
                        FragmentTransaction ft=manager.beginTransaction();
                        ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
                        Bundle bundle=new Bundle();
                        bundle.putString("product_id",AllProducts.get(position).get("id").toString());
                        //bundle.putString("product_image",DataList.get(position).getDesc().toString());
                        fragment.setArguments(bundle);

                    }
                })
        );


        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET,Api.homeBannerList , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

//                JSONObject jsonObject1 = jsonObject.optJSONObject("GetNotificationsResult");


                ImageList.clear();

                if (jsonObject.optString("status").equalsIgnoreCase("success")){
                    JSONArray jsonArray = jsonObject.optJSONArray("message");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject11 = jsonArray.getJSONObject(i);
                            NUM_PAGES=jsonArray.length();

                            //if (jsonObject11.optString("TowerName").equals(uppertowername.toString()) ||  jsonObject11.optString("TowerName").equals("AllTower")) {
                            ImageList.add(new Getseter(jsonObject11.optString("title"), jsonObject11.optString("target_url"), jsonObject11.optString("short_description"), jsonObject11.optString("photo")));
                            //}

                            viewPager.setAdapter(customPagerAdapter);

                            indicator.setViewPager(viewPager);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }






//                Timer timer = new Timer();
//                timer.scheduleAtFixedRate(new SliderTimer(), 3000, 3000);





//                 handler = new Handler();
//                 Update = new Runnable() {
//                    public void run() {
//                        if (currentPage == ImageList.size()) {
//                            currentPage = 0;
//                        }
//                        currentPage = currentPage+1;
//                    }
//                };
//
//                viewPager.setCurrentItem(currentPage, true);
//                handler.postDelayed(Update, 3000);




//                handler = new Handler();
//
//                Update = new Runnable() {
//
//                    public void run() {
//                        if (currentPage == ImageList.size()) {
//                            currentPage = 0;
//                        }
//                        viewPager.setCurrentItem(currentPage++);
//                    }
//                };
//                new Timer().schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        handler.post(Update);
//                    }
//                }, 3000, 3000);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "Please connect to the Internet", Toast.LENGTH_SHORT).show();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest2);


        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Api.categoryList, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                DataList.clear();
                Getseter.exitdialog(dialog);

                if (response.optString("status").equalsIgnoreCase("success")){

                    JSONArray jsonArray=response.optJSONArray("message");

                    Log.d("fsdgsdgsf", String.valueOf(jsonArray.length()));
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.optJSONObject(i);

                        DataList.add(new Getseter(jsonObject.optString("id"),jsonObject.optString("category"),jsonObject.optString("photo"),jsonObject.optString("photo")));

                        gridview.setAdapter(adapter);
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





        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Fragment fragment=new SubCatagoryFragmentPage();
                FragmentManager manager=getFragmentManager();
                FragmentTransaction ft=manager.beginTransaction();
                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
                ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
                Bundle bundle=new Bundle();
                position=position-1;
                bundle.putString("cat_id",DataList.get(position).getID().toString());
                bundle.putString("type","normal");
                bundle.putString("query","");
                Log.d("fsdgfsdgdf",DataList.get(position).getID().toString());
                Log.d("fsdgfsdgdf", String.valueOf(position));
                fragment.setArguments(bundle);
            }
        });

        //gridview.addHeaderView(ImageView);
        return view;
    }

    private void getTrading() {
        Getseter.showdialog(dialog);
        JsonObjectRequest jsonObjReqOffers = new JsonObjectRequest(Request.Method.GET,
                Api.trendingList, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Respose", response.toString());
                Getseter.exitdialog(dialog);
                try {
                    // Parsing json object response
                    // response will be a json object
//                    String name = response.getString("name");
                    HashMap<String,String> hashMap = null;
                    if (response.getString("status").equalsIgnoreCase("success")){

                        AllProducts.clear();
                        JSONArray jsonArray=response.getJSONArray("message");
                        int len=5;
                        if (jsonArray.length()<=5){
                            len=jsonArray.length();
                        }

                        for (int i=0;i<len;i++) {
                           JSONObject jsonObject = jsonArray.getJSONObject(i);


                            HashMap<String,String> map=new HashMap<>();
                            map.put("id", jsonObject.optString("id"));
                            map.put("category", jsonObject.optString("product_name"));
                            map.put("photo", jsonObject.optString("photo"));



//                                map.put("heading", jsonObject.optString("heading"));
//                                map.put("description", jsonObject.optString("description"));
//                                map.put("offer_type", jsonObject.optString("offer_type"));
//                                map.put("discount", jsonObject.optString("discount"));
//                                map.put("actual_price", jsonObject.optString("actual_price"));
//                                map.put("offer_price", jsonObject.optString("offer_price"));
//                                map.put("coupon_code", jsonObject.optString("coupon_code"));
//                                map.put("offer_from", jsonObject.optString("offer_from"));
//                                map.put("offer_to", jsonObject.optString("offer_to"));
//                                map.put("image", jsonObject.optString("image"));
//                                map.put("posted_date", jsonObject.optString("posted_date"));
//
//                             jsonObject2=jsonObject.getJSONObject("comapnyDetails");
//
//                            map.put("new_keywords", jsonObject2.optString("new_keywords"));
//                            map.put("company_name", jsonObject2.optString("company_name"));
//                            map.put("address", jsonObject2.optString("address"));
//                            map.put("c1_mobile1", jsonObject2.optString("c1_mobile1"));
//                            map.put("c1_fname", jsonObject2.optString("c1_fname")+" "+jsonObject2.optString("c1_mname")+" "+jsonObject2.optString("c1_lname"));


                            mAdapter = new HLVAdapter(getActivity());

                            mRecyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                            AllProducts.add(map);


//
////                            hashMap = new HashMap<>();
////                            hashMap.put("id",jsonObject.optString("id"));
////                            hashMap.put("cat_id",jsonObject.optString("cat_id"));
////                            hashMap.put("eventName",jsonObject.optString("eventName"));
////                            hashMap.put("photo",jsonObject.optString("photo"));
////
////                            viewPager.setAdapter(mCustomPagerAdapter);
////                            indicator.setViewPager(viewPager);
////                            mCustomPagerAdapter.notifyDataSetChanged();


                            //AllEvents.add(new Const(jsonObject.optString("id"),jsonObject.optString("eventVen"),jsonObject.optString("eventName"),jsonObject.optString("photo"),jsonObject.optString("eventDate"),jsonObject.optString("meta_description"),jsonObject.optString("orgBy")));

//                            viewPager.setAdapter(mCustomPagerAdapter);
//                            indicator.setViewPager(viewPager);
//                            //mCustomPagerAdapter.notifyDataSetChanged();

                        }
                    }
                    else if (response.getString("status").equalsIgnoreCase("failure")){


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("dfsdfsdfsdfgsd",e.getMessage());
                    Getseter.exitdialog(dialog);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Respose", "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        "Error! Please Connect to the internet", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                Getseter.exitdialog(dialog);
            }
        });

        // Adding request to request queue
        jsonObjReqOffers.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(jsonObjReqOffers);
    }

    static class ViewHolder {

        NetworkImageView image;
        TextView name;

    }

    public void onPause(){
        super.onPause();
        if(handler!=null)
            handler.removeCallbacks(runnale);
    }
    public void onResume(){
        super.onResume();
        // Start auto screen slideshow after 1s
        handler.postDelayed(runnale, PERIOD_MS);
    }



    class  Adapter extends BaseAdapter{

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
            convertView=inflater.inflate(R.layout.custonlistview,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.image =(NetworkImageView)convertView.findViewById(R.id.imageView);
            viewHolder.name=(TextView)convertView.findViewById(R.id.name);

            ImageLoader imageLoader = AppController.getInstance().getImageLoader();

            viewHolder.name.setText(DataList.get(position).getName().toString());
            viewHolder.image.setImageUrl(DataList.get(position).getDesc().toLowerCase(),imageLoader);

            return convertView;
        }
    }

    private class CustomPagerAdapter extends PagerAdapter {
        LayoutInflater layoutInflater;
        Button download;
        public CustomPagerAdapter() {
        }

        @Override
        public int getCount() {
            return ImageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {

            return view==object;
        }
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            NetworkImageView networkImageView;

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.custom_photogallery, container, false);
            networkImageView = (NetworkImageView) view.findViewById(R.id.networkImageView);


            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            networkImageView.setImageUrl(ImageList.get(position).getCount().toString(), imageLoader);


            (container).addView(view);

//            thread=new Thread(){
//                @Override
//                public void run() {
//                    super.run();
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    viewPager.setCurrentItem(2);
//                }
//            };
//
//            thread.start();
//



            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            (container).removeView((LinearLayout) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.POSITION_NONE;
        }
    }



    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < ImageList.size()) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }



    public class HLVAdapter extends RecyclerView.Adapter<HLVAdapter.ViewHolder> {

        ArrayList<String> alName;
        ArrayList<Integer> alImage;
        Context context;

        public HLVAdapter(Context context) {
            super();
            this.context = context;
            this.alName = alName;
            this.alImage = alImage;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_tranding, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {


            viewHolder.title.setText(AllProducts.get(i).get("category"));
//            viewHolder.actPrice.setText(AllProducts.get(i).get("offer_price"));
//            viewHolder.desc.setText(AllProducts.get(i).get("description"));
//            viewHolder.oldPrice.setText(AllProducts.get(i).get("actual_price"));


            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            viewHolder.imageNetworking.setImageUrl(AllProducts.get(i).get("photo").replace(" ","%20"),imageLoader);

//            viewHolder.actPrice.setPaintFlags(viewHolder.actPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


//            final Typeface tvFont = Typeface.createFromAsset(getActivity().getAssets(), "muli_bold.ttf");
            //final Typeface tvFont2 = Typeface.createFromAsset(getActivity().getAssets(), "muli.ttf");
//            viewHolder.tvSpecies.setTypeface(tvFont);
//            viewHolder.actPrice.setTypeface(tvFont);
//            viewHolder.desc.setTypeface(tvFont2);
//            viewHolder.oldPrice.setTypeface(tvFont2);


            //viewHolder.imgThumbnail.setImageResource(alImage.get(i));

//            viewHolder.setClickListener(new ItemClickListener() {
//                @Override
//                public void onClick(View view, int position, boolean isLongClick) {
//                    if (isLongClick) {
//                        Toast.makeText(context, "#" + position + " - " + alName.get(position) + " (Long click)", Toast.LENGTH_SHORT).show();
//                        context.startActivity(new Intent(context, MainActivity.class));
//                    } else {
//                        Toast.makeText(context, "#" + position + " - " + alName.get(position), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return AllProducts.size();
        }



        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

            public NetworkImageView imageNetworking;

            //private ItemClickListener clickListener;

            public ViewHolder(View itemView) {
                super(itemView);
                imageNetworking = (NetworkImageView) itemView.findViewById(R.id.imageNetworking);
                title = (TextView) itemView.findViewById(R.id.title);
//                actPrice = (TextView) itemView.findViewById(R.id.actPrice);
//                desc = (TextView) itemView.findViewById(R.id.desc);
//                oldPrice = (TextView) itemView.findViewById(R.id.oldPrice);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            public TextView title,act_price,oldPrice,actPrice,desc;

            @Override
            public void onClick(View view) {

//                int position = mRecyclerView.getChildLayoutPosition(view);
//                Fragment fragment=new ListedPage();
//                FragmentManager manager=getFragmentManager();
//                Bundle bundle = new Bundle();
//                bundle.putString("id",AllProducts.get(position).get("subcat_id"));
//                bundle.putString("title",MyPrefrences.getCityName(getActivity()));
//                bundle.putString("search","no");
//                bundle.putString("keyowd","");
//                bundle.putString("value","");
//                FragmentTransaction ft=manager.beginTransaction();
//                fragment.setArguments(bundle);
//                ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();



            }

            @Override
            public boolean onLongClick(View view) {
                return false;
            }

//            public void setClickListener(ItemClickListener itemClickListener) {
//                this.clickListener = itemClickListener;
//            }

//            @Override
//            public void onClick(View view) {
//                clickListener.onClick(view, getPosition(), false);
//            }

//            @Override
//            public boolean onLongClick(View view) {
//                clickListener.onClick(view, getPosition(), true);
//                return true;
//            }
        }

    }



    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private SubCatagoryFragmentPage.RecyclerItemClickListener.OnItemClickListener mListener;

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

        public RecyclerItemClickListener(Context context, SubCatagoryFragmentPage.RecyclerItemClickListener.OnItemClickListener listener) {
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
