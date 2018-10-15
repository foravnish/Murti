package com.foodapp.murti.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by rosen on 11-04-2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "fortisManager";

    // Contacts table name
    private static final String TABLE_CART = "Cart";


    // Contacts Table Columns names
    private static final String c_cat_id = "c_cat_id";
    private static final String c_cat_name = "c_cat_name";
    private static final String c_cat_price = "c_cat_price";
    private static final String c_cat_qty = "c_cat_qty";

    private static final String c_cat_rec_amount= "c_cat_rec_amount";
    private static final String c_cat_price2 = "c_cat_price2";
    private static final String c_cat_qty2 = "c_cat_qty2";
    private static final String c_cat_cal_price = "c_cat_cal_price";
    private static final String c_cat_cal_pid = "c_cat_cal_pid";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CATAGORY_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + c_cat_id + " TEXT," + c_cat_name + " TEXT,"
                + c_cat_price  +  " TEXT,"   + c_cat_rec_amount  +  " TEXT,"
                + c_cat_price2  +  " TEXT,"   + c_cat_qty2  +  " TEXT,"
                + c_cat_qty + " TEXT, " +   c_cat_cal_price + " TEXT," + c_cat_cal_pid + " TEXT"+ ")";
        db.execSQL(CREATE_CATAGORY_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact


   public void addCatagory(Getseter Catagory) {
        SQLiteDatabase db = this.getWritableDatabase();


       ContentValues values = new ContentValues();
        values.put(c_cat_id, Catagory.getID()); // Contact Name
        values.put(c_cat_name, Catagory.getName()); // Contact Name
        values.put(c_cat_price, Catagory.getDesc()); // Contact Phone
        values.put(c_cat_rec_amount, Catagory.getCount()); // Contact Phone
        values.put(c_cat_price2, Catagory.getImg()); // Contact Name
       values.put(c_cat_qty2, Catagory.getCdate()); // Contact Phone
       values.put(c_cat_qty, Catagory.getUdate()); // Contact Phone
       values.put(c_cat_cal_price, Catagory.getUdate3()); // Contact Phone
       values.put(c_cat_cal_pid, Catagory.getProductId()); // Contact Phone
      // values.put(c_cat_qty, Catagory.getImg()); // Contact Name


        // Inserting Row
        db.insert(TABLE_CART, null, values);
        db.close(); // Closing database connection
    }


    // Getting single contact


    // Getting All Contacts
    public List<Getseter> getAllCatagory() {
        List<Getseter> contactList = new ArrayList<Getseter>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CART;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Getseter contact = new Getseter();
                contact.setID(cursor.getString(0));
                contact.setName(cursor.getString(1));
                contact.setDesc(cursor.getString(2));
                contact.setCount(cursor.getString(3));
                contact.setImg(cursor.getString(4));
                contact.setCdate(cursor.getString(5));
                contact.setUdate(cursor.getString(6));
                contact.setUdate3(cursor.getString(7));
                contact.setProductId(cursor.getString(8));
//                contact.setImg(cursor.getString(4));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }




    // Deleting single contact
    public void deleteCatogory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART,null,
                new String[] {  });
        db.close();
    }
    public void removeSingleContact(Getseter Catagory) {
        //Open the database
        SQLiteDatabase database = this.getWritableDatabase();

        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        database.execSQL("DELETE FROM " + TABLE_CART + " WHERE " + c_cat_id + "= '" + Catagory.getID() + "'");

        //Close the database
        database.close();
    }



}