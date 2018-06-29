package com.foodapp.murti.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.foodapp.murti.R;

public class ThankAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank);

        TextView textDialog1 = (TextView) findViewById(R.id.textDialog1);
        TextView textDialog2 = (TextView) findViewById(R.id.textDialog2);
        TextView email = (TextView) findViewById(R.id.email);
        TextView phone = (TextView) findViewById(R.id.phone);


        email.setText("Email Id :"+getIntent().getStringExtra("email"));
        phone.setText("Contact  No: "+getIntent().getStringExtra("phone"));
        //textDialog1.setText("You have just ordered the "+ getArguments().get("products"));
        textDialog2.setText("Your order no. is: "+getIntent().getStringExtra("orderno"));
       // email.setText("Email :"+getArguments().getString("email"));
      //  phone.setText("Phone : "+getArguments().getString("phone"));

        Button yes=(Button)findViewById(R.id.yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ThankAct.this,Navigation.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ThankAct.this,Navigation.class));
        finish();

        super.onBackPressed();
    }
}
