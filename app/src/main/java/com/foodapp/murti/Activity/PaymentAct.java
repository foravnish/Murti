package com.foodapp.murti.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.foodapp.murti.PayUMoney.AppEnvironment;
import com.foodapp.murti.PayUMoney.AppPreference;
import com.foodapp.murti.R;
import com.foodapp.murti.Utils.Api;
import com.foodapp.murti.Utils.AppController;
import com.foodapp.murti.Utils.DatabaseHandler;
import com.foodapp.murti.Utils.Getseter;
import com.foodapp.murti.Utils.MyPrefrences;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class PaymentAct extends AppCompatActivity {
    TextView orderitem,subtotal,charge,total,address;
    Button payNow;

    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;
    private AppPreference mAppPreference;
//
    String merKey,merId,salt;
    Dialog dialog;
    DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        orderitem=(TextView)findViewById(R.id.orderitem);
        subtotal=(TextView)findViewById(R.id.subtotal);
        charge=(TextView)findViewById(R.id.charge);
        total=(TextView)findViewById(R.id.total);
        payNow=(Button) findViewById(R.id.payNow);


        orderitem.setText(getIntent().getStringExtra("item"));
        subtotal.setText(getIntent().getStringExtra("subtotal"));
        charge.setText(getIntent().getStringExtra("charge"));
        total.setText(getIntent().getStringExtra("total"));

        dialog=new Dialog(PaymentAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);


        Log.d("productArr",getIntent().getStringExtra("productArr"));
        Log.d("email",getIntent().getStringExtra("email"));
        Log.d("phone",getIntent().getStringExtra("phone"));
        Log.d("charge",getIntent().getStringExtra("charge"));

        mAppPreference = new AppPreference();
        merKey= AppEnvironment.SANDBOX.merchant_Key();
        merId=AppEnvironment.SANDBOX.merchant_ID();
        salt=AppEnvironment.SANDBOX.salt();

        db = new DatabaseHandler(getApplicationContext());


//        intent.putExtra("item",orderitem.getText().toString());
//        intent.putExtra("subtotal",subtotal.getText().toString());
//        intent.putExtra("charge",charge.getText().toString());
//        intent.putExtra("total",total.getText().toString());
//        intent.putExtra("productArr",cartItemsArray.toString());
//        intent.putExtra("email",emailString.toString());
//        intent.putExtra("phone",phoneString.toString());



        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launchPayUMoneyFlow("", getIntent().getStringExtra("total"));


            }
        });

    }


    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }


    private void launchPayUMoneyFlow(String package_name, String total_value) {

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

        //Use this to set your custom text on result screen button
//        payUmoneyConfig.setDoneButtonText(((EditText) findViewById(R.id.status_page_et)).getText().toString());

        //Use this to set your custom title for the activity
//        payUmoneyConfig.setPayUmoneyActivityTitle(((EditText) findViewById(R.id.activity_title_et)).getText().toString());

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        double amount = 0;
        try {
//            amount = 1;
            amount = Double.parseDouble(total_value.toString());
            Log.d("sdfsdfsddsgdf",total_value.toString());
           // Log.d("fgdfgdfhdhdh", String.valueOf(Double.parseDouble(totlPrice.getText().toString())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("sdfsfsdfgsgsd", Getseter.preferences.getString("mobile",""));

        String txnId = System.currentTimeMillis() + "";
//        String phone = mobile_til.getEditText().getText().toString().trim();
        String phone = Getseter.preferences.getString("mobile","");
//        String productName = mAppPreference.getProductInfo();
//        String firstName = mAppPreference.getFirstName();
//        String email = email_til.getEditText().getText().toString().trim();
//        String email = MyPrefrences.getEMAILID(getApplicationContext()).toString();
        String email = MyPrefrences.getEMAILID(getApplicationContext());
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";

        AppEnvironment appEnvironment = ((AppController) getApplication()).getAppEnvironment();
        builder.setAmount(amount)
                .setTxnId(txnId)
                .setPhone(phone)
                .setProductName("Spinof  "+package_name)
                .setFirstName(Getseter.preferences.getString("uname",""))
                .setEmail(email)
                .setsUrl(appEnvironment.surl())
                .setfUrl(appEnvironment.furl())
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(appEnvironment.debug())
//                .setKey(appEnvironment.merchant_Key())
                .setKey(merKey.toString())
//                .setMerchantId(appEnvironment.merchant_ID());
                .setMerchantId(merId.toString());

        Log.d("dfdsfdsfsdfsd",merKey);
        Log.d("dfdsfdsfsdfsd",merId);

        try {
            mPaymentParams = builder.build();

            /*
             * Hash should always be generated from your server side.
             * */
            generateHashFromServer(mPaymentParams);

            /*            *//**
             * Do not use below code when going live
             * Below code is provided to generate hash from sdk.
             * It is recommended to generate hash from server side only.
             * *//*
            mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);

           if (AppPreference.selectedTheme != -1) {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,MainActivity.this, AppPreference.selectedTheme,mAppPreference.isOverrideResultScreen());
            } else {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,MainActivity.this, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
            }*/

        } catch (Exception e) {
            // some exception occurred
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            // payNowButton.setEnabled(true);
        }
    }

    private PayUmoneySdkInitializer.PaymentParam calculateServerSideHashAndInitiatePayment1(final PayUmoneySdkInitializer.PaymentParam paymentParam) {

        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = paymentParam.getParams();
        stringBuilder.append(params.get(PayUmoneyConstants.KEY) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.TXNID) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF1) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF2) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF3) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF4) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF5) + "||||||");

        AppEnvironment appEnvironment = ((AppController) getApplication()).getAppEnvironment();
//        stringBuilder.append(appEnvironment.salt());
        stringBuilder.append(salt.toString());

        String hash = hashCal(stringBuilder.toString());
        paymentParam.setMerchantHash(hash);

        return paymentParam;
    }

    public void generateHashFromServer(PayUmoneySdkInitializer.PaymentParam paymentParam) {
        //nextButton.setEnabled(false); // lets not allow the user to click the button again and again.

        HashMap<String, String> params = paymentParam.getParams();

        // lets create the post params
        StringBuffer postParamsBuffer = new StringBuffer();
        postParamsBuffer.append(concatParams(PayUmoneyConstants.KEY, params.get(PayUmoneyConstants.KEY)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.AMOUNT, params.get(PayUmoneyConstants.AMOUNT)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.TXNID, params.get(PayUmoneyConstants.TXNID)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.EMAIL, params.get(PayUmoneyConstants.EMAIL)));
        postParamsBuffer.append(concatParams("productinfo", params.get(PayUmoneyConstants.PRODUCT_INFO)));
        postParamsBuffer.append(concatParams("firstname", params.get(PayUmoneyConstants.FIRSTNAME)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF1, params.get(PayUmoneyConstants.UDF1)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF2, params.get(PayUmoneyConstants.UDF2)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF3, params.get(PayUmoneyConstants.UDF3)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF4, params.get(PayUmoneyConstants.UDF4)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF5, params.get(PayUmoneyConstants.UDF5)));

        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();

        Log.d("fgdfhgdfhdfhd",postParams);

        // lets make an api call
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);
    }
    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }

    private class GetHashesFromServerTask extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PaymentAct.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... postParams) {

            String merchantHash = "";
            try {
                //TODO Below url is just for testing purpose, merchant needs to replace this with their server side hash generation url
                URL url = new URL(Api.HashCode);

                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());
                Log.d("fgdfgdfgdfg",response.toString());
                Iterator<String> payuHashIterator = response.keys();
                while (payuHashIterator.hasNext()) {
                    String key = payuHashIterator.next();
                    switch (key) {
                        /**
                         * This hash is mandatory and needs to be generated from merchant's server side
                         *
                         */
                        case "payment_hash":
                            merchantHash = response.getString(key);
                            break;
                        default:
                            break;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return merchantHash;
        }

        @Override
        protected void onPostExecute(String merchantHash) {
            super.onPostExecute(merchantHash);

            progressDialog.dismiss();
            // payNowButton.setEnabled(true);

            Log.d("fgdfgdfgdfg",merchantHash.toString());

            //  merchantHash="35eceef8992006b59b2f46d7ef6ce13b3dcgfhfgj258996b871f863ed7fghfgjhfgee9e76bb357209f04b488afcc6a687f354a13750421e0ec85bcb40006441df530b84831c69b4";

            if (merchantHash.isEmpty() || merchantHash.equals("")) {
                Toast.makeText(getApplicationContext(), "Could not generate hash", Toast.LENGTH_SHORT).show();
            } else {
                mPaymentParams.setMerchantHash(merchantHash);
                if (AppPreference.selectedTheme != -1) {
                    PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, PaymentAct.this, AppPreference.selectedTheme, mAppPreference.isOverrideResultScreen());
                } else {
                    PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,  PaymentAct.this, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        Log.d("gffgdfgfhdhdh","true");
        Log.d("MainActivity123", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {

                String payuResponse = transactionResponse.getPayuResponse();
                String merchantResponse = transactionResponse.getTransactionDetails();


                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //Success Transaction
                    Log.d("responseprint","success "+payuResponse);
                    Log.d("responseprint","success "+merchantResponse);

                    new AlertDialog.Builder(PaymentAct.this)
                            .setCancelable(false)
                            .setMessage("Payment Success...")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }).show();
                  //  new BuyPackagesApi2(getApplicationContext(),"success","payment_success").execute();

                    placeOrder();


                } else {
                    //Failure Transaction
                    Log.d("responseprint","failed "+payuResponse);

                    new AlertDialog.Builder(PaymentAct.this)
                            .setCancelable(false)
                            .setMessage("Payment Failed...")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }).show();

                   // new BuyPackagesApi2(getApplicationContext(),"failed","payment_failed").execute();
                }

                // Response from Payumoney

//                new AlertDialog.Builder(PayActivity.this)
//                        .setCancelable(false)
//                        .setMessage("Payu's Data : " + payuResponse + "\n\n\n Merchant's Data: " + merchantResponse)
//                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                dialog.dismiss();
//                            }
//                        }).show();

            } else if (resultModel != null && resultModel.getError() != null) {
                Log.d("dfsdgdfgdfg", "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d("dfsdgdfgdfg", "Both objects are null!");
            }
        }
    }

    private void placeOrder() {
        Getseter.showdialog(dialog);


        StringRequest stringRequest=new StringRequest(Request.Method.POST, Api.placeUserOrders, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Getseter.exitdialog(dialog);
                Log.d("fgvdgdfgdfgd",response.toString());

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    if (jsonObject.optString("status").equals("success")) {

                        db.deleteCatogory();
                        Navigation.textOne.setText("0");

                        JSONObject jsonObject1=jsonObject.optJSONObject("message");



                        Intent intent=new Intent(getApplicationContext(), ThankAct.class);
                        intent.putExtra("orderno",jsonObject1.optString("order_id"));
                        intent.putExtra("email",getIntent().getStringExtra("email"));
                        intent.putExtra("phone",getIntent().getStringExtra("phone"));
                        startActivity(intent);



                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Some error! Please try again...", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Please connect to the internet.", Toast.LENGTH_SHORT).show();
                Log.d("fgvdgdfgdfgd2",error.toString());
                Getseter.exitdialog(dialog);

            }
        }){




            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();


                params.put("user_id",Getseter.preferences.getString("user_id",""));
                params.put("amount", subtotal.getText().toString());
                params.put("discount", "0");
                params.put("total_amount", subtotal.getText().toString());
                params.put("ship_fname",Getseter.preferences.getString("fname",""));
                params.put("ship_lname", Getseter.preferences.getString("lname",""));
                params.put("ship_email", Getseter.preferences.getString("emailid",""));
                params.put("ship_house_no", Getseter.preferences.getString("house_no",""));
                params.put("ship_street", Getseter.preferences.getString("street",""));
                params.put("ship_landmark", Getseter.preferences.getString("landmark",""));
                params.put("ship_pincode", Getseter.preferences.getString("pincode",""));
                params.put("ship_area", Getseter.preferences.getString("area",""));
                params.put("ship_city_id", Getseter.preferences.getString("city_id",""));
                params.put("ship_mobile",Getseter.preferences.getString("mobile",""));
                params.put("expres_amt", total.getText().toString());
                params.put("shipping_amount", getIntent().getStringExtra("charge"));
                params.put("mode", "Payment");


                Log.d("user_id",Getseter.preferences.getString("user_id",""));
                Log.d("amount", subtotal.getText().toString());
                Log.d("discount", "0");
                Log.d("total_amount", subtotal.getText().toString());
                Log.d("ship_fname",Getseter.preferences.getString("fname",""));
                Log.d("ship_lname", Getseter.preferences.getString("lname",""));
                Log.d("ship_email", Getseter.preferences.getString("emailid",""));
                Log.d("ship_house_no", Getseter.preferences.getString("house_no",""));
                Log.d("ship_street", Getseter.preferences.getString("street",""));
                Log.d("ship_landmark", Getseter.preferences.getString("landmark",""));
                Log.d("ship_pincode", Getseter.preferences.getString("pincode",""));
                Log.d("ship_area", Getseter.preferences.getString("area",""));
                Log.d("ship_city_id", Getseter.preferences.getString("city_id",""));
                Log.d("ship_mobile",Getseter.preferences.getString("mobile",""));
                Log.d("expres_amt", total.getText().toString());
                Log.d("shipping_amount", getIntent().getStringExtra("charge"));


                params.put("productArr",getIntent().getStringExtra("productArr"));

                Log.d("dfgdgdfgdfhgdfh",getIntent().getStringExtra("productArr"));


                return params;
            }
        };


//                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                        0,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                 AppController.getInstance().addToRequestQueue(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);

    }


}
