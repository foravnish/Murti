package com.foodapp.murti.Fragments;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.foodapp.murti.R;
import com.foodapp.murti.Utils.MyPrefrences;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReferAFriend extends Fragment {


    public ReferAFriend() {
        // Required empty public constructor
    }

    Dialog dialog;
    TextView content;

    Button share;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_refer_afriend3, container, false);


        share= (Button) view.findViewById(R.id.share);
        content= (TextView) view.findViewById(R.id.content);


        getActivity().setTitle("Refer A Friend");

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
//                String shareBody =comName.getText().toString()+ " "+phone.getText().toString();
                String shareBody ="Hi, \n" +
                        "Please Install Murti app. Click on the link below. https://play.google.com/store/apps/details?id=com.foodapp.murti&referrer="+ MyPrefrences.getMyRefrel(getActivity());

                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Details");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));


            }
        });
        return view;
    }

}
