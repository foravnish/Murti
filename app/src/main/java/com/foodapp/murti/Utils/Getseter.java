package com.foodapp.murti.Utils;

import android.app.Dialog;
import android.content.SharedPreferences;

import com.foodapp.murti.R;

/**
 * Created by rosen on 11-04-2017.
 */

public class Getseter{


    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;

    public  static String url="http://staging.rosensoft.com/fortis/api/";
    //private variables
    String _id;
    String _name;
    String _desc;
    String _count;

    String img;
    String cdate;
    String udate;
    String udate3;

    String img2;
    String cdate2;
    String udate2;

    String productId;


    public static void showdialog(Dialog dialog) {

        dialog.setContentView(R.layout.dialogprogress);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    public static void exitdialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    // Empty constructor

    // constructor
    public Getseter(String id, String name, String _desc,String _count){
        this._id = id;
        this._name = name;
        this._desc = _desc;
        this._count = _count;
    }


    ////  8
    public Getseter(String id, String name, String _desc,String _count, String img, String cdate,String udate,String udate3,String productId){
        this._id = id;
        this._name = name;
        this._desc = _desc;
        this._count = _count;
        this.img = img;
        this.cdate = cdate;
        this.udate = udate;
        this.udate3 = udate3;
        this.productId = productId;
    }

    ////  10
    public Getseter(String id, String name, String _desc,String _count, String img, String cdate,String udate,String img2, String cdate2,String udate2){
        this._id = id;
        this._name = name;
        this._desc = _desc;
        this._count = _count;
        this.img = img;
        this.cdate = cdate;
        this.udate = udate;
        this.img2 = img2;
        this.cdate2 = cdate2;
        this.udate2 = udate2;
    }

    public Getseter() {

    }


    // getting ID
    public String getID(){
        return this._id;
    }

    // setting id
    public void setID(String id){
        this._id = id;
    }

    // getting name
    public String getName(){
        return this._name;
    }

    // setting name
    public void setName(String name){
        this._name = name;
    }

    // getting phone number
    public String getDesc(){
        return this._desc;
    }

    // setting phone number
    public void setDesc(String _desc){
        this._desc = _desc;
    }

    public String getCount(){
        return this._count;
    }

    // setting phone number
    public void setCount(String _count){
        this._count = _count;
    }

    public String getImg(){
        return this.img;
    }
    // setting name
    public void setImg(String img){
        this.img = img;
    }
    // getting phone number
    public String getCdate(){
        return this.cdate;
    }

    // setting phone number
    public void setCdate(String cdate){
        this.cdate = cdate;
    }

    public String getUdate(){
        return this.udate;
    }


    // setting phone number
    public void setUdate(String udate){
        this.udate = udate;
    }

    public void setUdate3(String udate3){
        this.udate3 = udate3;
    }

    public String getImg2(){
        return this.img2;
    }

    // setting name
    public void setImg2(String img2){
        this.img2 = img2;
    }

    // getting phone number
    public String getCdate2(){
        return this.cdate2;
    }

    // setting phone number
    public void setCdate2(String cdate2){
        this.cdate2 = cdate2;
    }

    public String getUdate2(){
        return this.udate2;
    }
    public String getUdate3(){
        return this.udate3;
    }

    // setting phone number
    public void setUdate2(String udate2){
        this.udate2 = udate2;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }
}
