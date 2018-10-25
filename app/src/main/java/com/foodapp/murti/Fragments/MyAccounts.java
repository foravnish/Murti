package com.foodapp.murti.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.foodapp.murti.Activity.Login;
import com.foodapp.murti.Activity.Navigation;
import com.foodapp.murti.R;
import com.foodapp.murti.Utils.MyPrefrences;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccounts extends Fragment {


    LinearLayout account,terms,contact,about;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_accounts, container, false);
        account=(LinearLayout)view.findViewById(R.id.account);
        terms=(LinearLayout)view.findViewById(R.id.terms);
        contact=(LinearLayout)view.findViewById(R.id.contact);
        about=(LinearLayout)view.findViewById(R.id.about);

        getActivity().setTitle("My Accounts");
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyPrefrences.getUserLogin(getActivity())==false){
                    popForLogin();
                }
                else {
                    Fragment fragment=new AccountDetails();
                    FragmentManager manager=getFragmentManager();
                    FragmentTransaction ft=manager.beginTransaction();
                    ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
                    ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
                }


            }
        });


        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new TermsAndConditions();
                FragmentManager manager=getFragmentManager();
                FragmentTransaction ft=manager.beginTransaction();
                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
                ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
            }
        });


        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment=new ContactUs();
                FragmentManager manager=getFragmentManager();
                FragmentTransaction ft=manager.beginTransaction();
                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
                ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
            }
        });


        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new AboutUs();
                FragmentManager manager=getFragmentManager();
                FragmentTransaction ft=manager.beginTransaction();
                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout,R.anim.frag_fade_right, R.anim.frag_fad_left);
                ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
            }
        });

        return view;

    }

    private void popForLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Please Login to Profile")
                .setCancelable(false)
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent=new Intent(getActivity(),Login.class);
                        startActivity(intent);
                        getActivity(). overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        getActivity().finish();
                    }
                })
                .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();



                    }
                });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Murti Foods");
        alert.show();
    }


}
