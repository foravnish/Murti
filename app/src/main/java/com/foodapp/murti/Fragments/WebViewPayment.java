package com.foodapp.murti.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.foodapp.murti.R;



public class WebViewPayment extends Fragment {
    WebView webView;
    String url="https://www.payumoney.com/pay/#/merchant/7C6AFB800ED16D11004AACA6546C5E8D?param=5827062";
//    String url="https://www.payumoney.com";
//    String url="http://www.google.com";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_web_view_payment, container, false);
        webView= (WebView)view.findViewById(R.id.webview);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);


//        Toast.makeText(getActivity(), getArguments().getString("mode"), Toast.LENGTH_SHORT).show();
//        webView.setWebViewClient(new MyBrowser());
//
//        webView.getSettings().setLoadsImagesAutomatically(true);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//
//        webView.loadUrl(url);
        return view;
    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


}
