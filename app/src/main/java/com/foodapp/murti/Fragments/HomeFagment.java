package com.foodapp.murti.Fragments;


import android.app.Dialog;
import android.content.Context;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.eftimoff.viewpagertransformers.ForegroundToBackgroundTransformer;
import com.foodapp.murti.R;
import com.foodapp.murti.Utils.Api;
import com.foodapp.murti.Utils.AppController;
import com.foodapp.murti.Utils.DatabaseHandler;
import com.foodapp.murti.Utils.Getseter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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


        position=0;

        /*
          check 2

         */

        LayoutInflater myinflater=(LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup myHeader = (ViewGroup) myinflater.inflate(R.layout.fragment_placeholder1, gridview, false);
        gridview.addHeaderView(myHeader, null, false);


        viewPager= (ViewPager)myHeader. findViewById(R.id.viewpager);
        customPagerAdapter=new CustomPagerAdapter();
        indicator = (CircleIndicator)myHeader.findViewById(R.id.indicator);

        viewPager.setPageTransformer(true, new ForegroundToBackgroundTransformer());
        Getseter.showdialog(dialog);


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


}
