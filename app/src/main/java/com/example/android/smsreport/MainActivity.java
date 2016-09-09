package com.example.android.smsreport;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.app.ActionBar;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements  OnDateSetListener {
    TimePickerDialog mDialogAll;
    TextView situattion_tv;
    TextView all_tv;
    TextView content_tv;
    EditText content_et;
    Button save_btn;
    TextView reporter_tv;
    EditText reporter_et;
    Button save_reporter_btn;


    long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
    boolean nowedit=false;
    final String[] situations = new String[] {"장애발생보고","중간보고","장애복구","훈련상황"};
    Calendar ca = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
    String abc= df.format(ca.getTime());
    private  Realm realm;
    int index =Integer.parseInt(PropertyManager.getInstance().getSituation());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();
/*        realm.beginTransaction();
        Situation sit = realm.createObject(Situation.class);
        sit.setCode(0);
        sit.setSituation("장애발생보고");
        sit.setSituationmgs("내용을 입력해주세요.");
        Situation sit1 = realm.createObject(Situation.class);
        sit1.setCode(1);
        sit1.setSituation("중간보고");
        sit1.setSituationmgs("내용을 입력해주세요.");
        Situation sit2 = realm.createObject(Situation.class);
        sit2.setCode(2);
        sit2.setSituation("장애복구");
        sit2.setSituationmgs("내용을 입력해주세요.");
        Situation sit3 = realm.createObject(Situation.class);
        sit3.setCode(3);
        sit3.setSituation("훈련상황");
        sit3.setSituationmgs("내용을 입력해주세요.");
        realm.commitTransaction();*/
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.main_bar);
        situattion_tv = (TextView)findViewById(R.id.getbackdata);
        all_tv = (TextView)findViewById(R.id.tv_all);
        content_tv = (TextView)findViewById(R.id.tv_content);
        save_btn = (Button)findViewById(R.id.btn_save);
        content_et = (EditText)findViewById(R.id.et_content);
        reporter_tv = (TextView)findViewById(R.id.tv_reporter);
        reporter_et = (EditText)findViewById(R.id.et_reporter);
        save_reporter_btn = (Button)findViewById(R.id.btn_save_repoter);
        all_tv.setText(abc);
        final Situation result = realm.where(Situation.class).equalTo("code", index).findFirst();
        situattion_tv.setText(result.getSituation());
        content_tv.setText(result.getSituationmgs());
        reporter_tv.setText(PropertyManager.getInstance().getReporter());



        mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("Cancel")
                .setSureStringId("Sure")
                .setTitleStringId("TimePicker")
                .setYearText("Year")
                .setMonthText("Month")
                .setDayText("Day")
                .setHourText("Hour")
                .setMinuteText("Minute")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(12)
                .build();



        situattion_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nowedit){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("상황 선택");
                builder.setSingleChoiceItems(situations,index,new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PropertyManager.getInstance().setSituation(String.valueOf(i));
                        index = i;
                        Situation result1 = realm.where(Situation.class)
                                .equalTo("code", i)
                                .findFirst();
                        situattion_tv.setText(result1.getSituation());
                        content_tv.setText(result1.getSituationmgs());
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
                }
            }
        });


        all_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogAll.show(getSupportFragmentManager(), "all");
            }
        });

        content_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nowedit=true;
                content_tv.setVisibility(View.GONE);
                content_et.setText(content_tv.getText());
                content_et.setVisibility(View.VISIBLE);
                save_btn.setVisibility(View.VISIBLE);
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nowedit=false;
                String content = content_et.getText().toString();
                content_tv.setText(content);
                Situation result1 = realm.where(Situation.class)
                        .equalTo("code", index)
                        .findFirst();
                realm.beginTransaction();
                result1.setSituationmgs(content);
                realm.commitTransaction();
                content_et.setVisibility(View.GONE);
                save_btn.setVisibility(View.GONE);
                content_tv.setVisibility(View.VISIBLE);
            }
        });

        reporter_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reporter_tv.setVisibility(View.GONE);
                reporter_et.setText(reporter_tv.getText());
                reporter_et.setVisibility(View.VISIBLE);
                save_reporter_btn.setVisibility(View.VISIBLE);
            }
        });

        save_reporter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = reporter_et.getText().toString();
                reporter_tv.setText(content);
                PropertyManager.getInstance().setReporter(content);
                reporter_et.setVisibility(View.GONE);
                save_reporter_btn.setVisibility(View.GONE);
                reporter_tv.setVisibility(View.VISIBLE);
            }
        });
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

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        String text = getDateToString(millseconds);
        all_tv.setText(text);
    }

    public String getDateToString(long time) {
        Date d = new Date(time);
        return df.format(d);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}