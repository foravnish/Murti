package com.foodapp.murti;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.foodapp.murti.Utils.AppController;
import com.payumoney.sdkui.ui.utils.RecyclerViewOnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> implements AdapterView.OnClickListener
{
    private ArrayList<HashMap<String,String>> products_arrayList;
    private LayoutInflater layoutInflater;
    Context context;
    RecyclerView recyclerView;

    public ProductsAdapter(Context context, ArrayList<HashMap<String,String>> products_arrayList,RecyclerView recyclerView)
    {
        /*
         * RecyclerViewAdapter Constructor to Initialize Data which we get from RecyclerViewFragment
         **/

        layoutInflater = LayoutInflater.from(context);
        this.products_arrayList = products_arrayList;
        this.context=context;
        this.recyclerView=recyclerView;
        recyclerView.setOnClickListener(this);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        /*
         * LayoutInflater is used to Inflate the view
         * from fragment_listview_adapter
         * for showing data in RecyclerView
         **/

        View view = layoutInflater.inflate(R.layout.custon_subcatagory_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductsAdapter.MyViewHolder holder, int position)
    {
        /*
         * onBindViewHolder is used to Set all the respective data
         * to Textview or Imagview form worldpopulation_pojoArrayList
         * ArrayList Object.
         **/

//            if (!TextUtils.isEmpty(products_arrayList.get(position).getTitle()))
//            {
//                String cropName = products_arrayList.get(position).getTitle().substring(0,1).toUpperCase() +  products_arrayList.get(position).getTitle().substring(1);
//
//                holder.textview_product.setText(cropName);
//            }
        holder.name.setText(products_arrayList.get(position).get("product_name"));
        holder.price.setText("₹ "+products_arrayList.get(position).get("mrp_price"));
        holder.newprice.setText("₹ "+products_arrayList.get(position).get("sell_price"));
        holder.off.setText(products_arrayList.get(position).get("discount"));
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        holder.imageView.setImageUrl(products_arrayList.get(position).get("photo"),imageLoader);





    }

    @Override
    public int getItemCount()
    {
        /*
         * getItemCount is used to get the size of respective worldpopulation_pojoArrayList ArrayList
         **/

        return products_arrayList.size();
    }

    @Override
    public void onClick(View view) {
        Log.d("fsdfsdfsdgfsd", "dfgdfg");
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,price,newprice,off;
        Button button1;
        NetworkImageView imageView;

        /**
         * MyViewHolder is used to Initializing the view.
         **/

        MyViewHolder(View itemView)
        {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            imageView = (NetworkImageView) itemView.findViewById(R.id.imageView);
            price = (TextView) itemView.findViewById(R.id.price);
            newprice = (TextView) itemView.findViewById(R.id.newprice);
            off = (TextView) itemView.findViewById(R.id.off);
            button1 = (Button) itemView.findViewById(R.id.button1);

            itemView.setTag(itemView);


//            recyclerView.setOnTouchListener((View.OnTouchListener) this);

        }

//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            Log.d("fsdfsdfsdgfsd", "dfgdfg");
//
//            return false;
//        }

//        @Override
//        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//            Log.d("fsdfsdfsdgfsd", "dfgdfg");
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> adapterView) {

//        }

//        @Override
//        public void onClick(View view) {
//            Log.d("fsdfsdfsdgfsd", "dfgdfg");
//        }

//        @Override
//        public void onClick(View view) {
//           // int position = recyclerView.getChildAdapterPosition(view);
//          //  int position = recyclerView.getChildLayoutPosition(view);
//
//            Log.d("fsdfsdfsdgfsd", String.valueOf("vbgvg"));
//        }


//        @Override
//        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//            Log.d("fsdfsdfsdgfsd", String.valueOf("vbgvg"));
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> adapterView) {
//
//        }

//        @Override
//        public void onItemClick(View view, int i) {
//            Log.d("fsdfsdfsdgfsd", String.valueOf("vbgvg"));
//        }
    }
}
