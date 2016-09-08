package com.example.android.smsreport;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;
import com.wx.wheelview.widget.WheelViewDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView situattion_tv;
    TextView date_tv;
    TextView time_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.main_bar);
        situattion_tv = (TextView)findViewById(R.id.getbackdata);
        date_tv = (TextView)findViewById(R.id.tv_date);
        time_tv = (TextView)findViewById(R.id.tv_time);
        situattion_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] items = new String[] {"장애발생보고","중간보고","장애복구","훈련상황"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("상황 선택");
                builder.setSingleChoiceItems(items,1,new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        situattion_tv.setText(items[i]);
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        SimpleDateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일");
        SimpleDateFormat tf = new SimpleDateFormat("hh시 mm분 ss초");


        Calendar c = Calendar.getInstance();
        String date = df.format(c.getTime());
        String time = tf.format(c.getTime());

        date_tv.setText(String.valueOf(date));
        time_tv.setText(String.valueOf(time));

        date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        time_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


       /* PropertyManager.getInstance().getEmail();
        PropertyManager.getInstance().getPassword();
        PropertyManager.getInstance().setEmail(email);
        PropertyManager.getInstance().setPassword(password);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_group :
                Intent i = new Intent(this,  GroupActivity.class);
                startActivityForResult(i, 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String stredittext=data.getStringExtra("edittextvalue");
            situattion_tv.setText(stredittext);
        }
    }
    public void showDialog(View view) {
/*        WheelViewDialog dialog = new WheelViewDialog(this);
        final List<String> myData = new ArrayList<String>();
        myData.add("장애발생보고");
        myData.add("중간보고");
        myData.add("장애복구");
        myData.add("훈련상황");
        dialog.setTitle("wheelview dialog").setItems(myData).setButtonText("Close").setDialogStyle(Color
                .parseColor("#6699ff")).setCount(5).show();*/

    }
}
