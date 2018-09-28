package com.foodapp.murti;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.foodapp.murti.Utils.AppController;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder>
{
    private ArrayList<HashMap<String,String>> products_arrayList;
    private LayoutInflater layoutInflater;


    public ProductsAdapter(Context context, ArrayList<HashMap<String,String>> products_arrayList)
    {
        /*
         * RecyclerViewAdapter Constructor to Initialize Data which we get from RecyclerViewFragment
         **/

        layoutInflater = LayoutInflater.from(context);
        this.products_arrayList = products_arrayList;
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
        }
    }
}
