package com.foodapp.murti.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.foodapp.murti.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Thankyou extends Fragment {




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_thankyou, container, false);


            TextView textDialog1 = (TextView) view.findViewById(R.id.textDialog1);
            TextView textDialog2 = (TextView) view.findViewById(R.id.textDialog2);
            TextView email = (TextView) view.findViewById(R.id.email);
            TextView phone = (TextView) view.findViewById(R.id.phone);

            textDialog1.setText("You have just ordered the "+ getArguments().get("products"));
            textDialog2.setText("Your order will delivered soon. Your order no. is: "+getArguments().get("orderno"));
            email.setText("Email :"+getArguments().getString("email"));
            phone.setText("For any Assistance contact : "+getArguments().getString("phone"));

        Button yes=(Button)view.findViewById(R.id.yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new History();
                FragmentManager manager=getFragmentManager();
                FragmentTransaction ft=manager.beginTransaction();
                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
                ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
            }
        });
        return view;
    }

}
