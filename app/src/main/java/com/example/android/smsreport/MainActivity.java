package com.example.android.smsreport;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;


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
    ListView contact_lv;
    ContactAdapter contactAdapter;
    RealmResults<ChoiceGroupid> results;
    RealmResults<ChoiceContactid> noSendList;


    long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
    boolean nowedit = false;
    final String[] situations = new String[]{"장애발생보고", "중간보고", "장애복구", "훈련상황"};
    Calendar ca = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
    String abc = df.format(ca.getTime());
    private Realm realm;
    int index = Integer.parseInt(PropertyManager.getInstance().getSituation());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();
        if (realm.where(Situation.class).equalTo("code", 0).findFirst() == null) {
            realm.beginTransaction();
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
            realm.commitTransaction();
        }
        noSendList=realm.where(ChoiceContactid.class).findAll();
//        for(int i=0;i<noSendList.size();i++){
//            Toast.makeText(getApplicationContext(),noSendList.get(i).getId(),Toast.LENGTH_SHORT).show();
//        }
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.main_bar);
        situattion_tv = (TextView) findViewById(R.id.getbackdata);
        all_tv = (TextView) findViewById(R.id.tv_all);
        content_tv = (TextView) findViewById(R.id.tv_content);
        save_btn = (Button) findViewById(R.id.btn_save);
        content_et = (EditText) findViewById(R.id.et_content);
        reporter_tv = (TextView) findViewById(R.id.tv_reporter);
        reporter_et = (EditText) findViewById(R.id.et_reporter);
        save_reporter_btn = (Button) findViewById(R.id.btn_save_repoter);
        contact_lv = (ListView) findViewById(R.id.lv_main_contact);
        all_tv.setText(abc);
        final Situation result = realm.where(Situation.class).equalTo("code", index).findFirst();
        situattion_tv.setText(result.getSituation());
        content_tv.setText(result.getSituationmgs());
        reporter_tv.setText(PropertyManager.getInstance().getReporter());
        contactAdapter = new ContactAdapter();
        contact_lv.setAdapter(contactAdapter);
        contact_lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        contact_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ContactData contactinfo = (ContactData) adapterView.getItemAtPosition(i);
                boolean contactChecked = contactinfo.isChecked();
                contactinfo.setChecked(!contactChecked);
                ContactView cv = (ContactView)view;
                cv.setChecked();
                Toast.makeText(getApplicationContext(),String.valueOf(contactChecked),Toast.LENGTH_SHORT).show();
                if(contactChecked){
                    final RealmResults<ChoiceContactid> rows = realm.where(ChoiceContactid.class).equalTo("id",contactinfo.getId()).findAll();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            // 전체 맞는 데이터를 삭제합니다{
                            rows.deleteAllFromRealm();
                        }
                    });
                }else{
                    realm.beginTransaction();
                    ChoiceContactid cid = realm.createObject(ChoiceContactid.class);
                    cid.setId(contactinfo.getId());
                    realm.commitTransaction();
                }
            }
        });
        loadcontact();

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
                if (!nowedit) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("상황 선택");
                    builder.setSingleChoiceItems(situations, index, new DialogInterface.OnClickListener() {
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
                nowedit = true;
                content_tv.setVisibility(View.GONE);
                content_et.setText(content_tv.getText());
                content_et.setVisibility(View.VISIBLE);
                save_btn.setVisibility(View.VISIBLE);
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nowedit = false;
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
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_group:
                Intent i = new Intent(this, GroupActivity.class);
                startActivityForResult(i, 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String stredittext = data.getStringExtra("edittextvalue");
            loadcontact();
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

    private void loadcontact() {
        boolean isallchosen = false;
        contactAdapter.items.clear();
        results = realm.where(ChoiceGroupid.class).findAll();
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).getCode().equals("all")) {
                isallchosen = true;
                break;
            }
        }
        if (isallchosen) {
            allmember();
        } else {
            for (int i = 0; i < results.size(); i++) {
                contactAdapter.addlist(getContacts(results.get(i).getCode(), results.get(i).getGroupname()));
            }

        }
        contactAdapter.notifyDataSetChanged();
    }

    private List getContacts(String groupID, String groupName) {
        List<ContactData> members = new ArrayList<ContactData>();
        Uri uri = ContactsContract.Data.CONTENT_URI;

        String[] projection = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };

        String selection = null;
        String[] selectionArgs = null;

        if (groupID != null && !"".equals(groupID)) {
            selection = ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + " = ?";
            selectionArgs = new String[]{groupID};
        }

        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC ";

        Cursor groupcursor = getContentResolver().query(uri, projection,
                selection, selectionArgs, sortOrder);

        Set<Long> seen = new HashSet<Long>();
        while (groupcursor.moveToNext()) {
            long raw = groupcursor.getLong(1);
            if (!seen.contains(raw)) {
                seen.add(raw);
                Cursor numberCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + groupcursor.getLong(1), null, null);

                if (numberCursor.moveToFirst()) {
                    int numberColumnIndex = numberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    do {
                        String phoneNumber = numberCursor.getString(numberColumnIndex).replaceAll("[^0-9]", "");
                        String id = groupcursor.getString(1);
                        ContactData member = new ContactData();
                        member.setName(groupcursor.getString(2));
                        member.setPhonenum(phoneNumber);
                        member.setGroupname(groupName);
                        member.setId(groupcursor.getString(1));
                        if(Config.checkContactArray(noSendList,id)){
                            member.setChecked(true);
                        }else{
                            member.setChecked(false);
                        }
                        members.add(member);
                    } while (numberCursor.moveToNext());
                    numberCursor.close();
                }
            }
        }
        return members;
    }

    private void allmember() {
        //전체 연락처 리스트
        ContentResolver cr = getContentResolver();
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC ";
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, sortOrder);
        LinkedHashMap<String, ContactData> contactmap = new LinkedHashMap<>();

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[^0-9]", "");
                        ContactData cd = new ContactData();
                        cd.setGroupname("미지정");
                        cd.setName(name);
                        cd.setPhonenum(phoneNo);
                        cd.setId(id);
                        if(Config.checkContactArray(noSendList,id)){
                            cd.setChecked(true);
                        }else{
                            cd.setChecked(false);
                        }
                        contactmap.put(id, cd);
//                        Toast.makeText(NativeContentProvider.this, "Name: " + name+ ", Phone No: " + phoneNo, Toast.LENGTH_SHORT).show();
                    }
                    pCur.close();
                }
            }
        }
        final String[] GROUP_PROJECTION = new String[] {
                ContactsContract.Groups._ID,
                ContactsContract.Groups.TITLE,
                ContactsContract.Groups.SUMMARY_WITH_PHONES
        };

        Cursor c = getContentResolver().query(
                ContactsContract.Groups.CONTENT_SUMMARY_URI,
                GROUP_PROJECTION,
                ContactsContract.Groups.DELETED+"!='1' AND "+
                        ContactsContract.Groups.GROUP_VISIBLE+"!='0' "
                ,
                null,
                null);
        final int IDX_ID = c.getColumnIndex(ContactsContract.Groups._ID);
        final int IDX_TITLE = c.getColumnIndex(ContactsContract.Groups.TITLE);

        while (c.moveToNext()) {
//            Log.i("code########3$$$$",c.getString(IDX_ID));
            int users = c.getInt(c.getColumnIndex(ContactsContract.Groups.SUMMARY_WITH_PHONES));
            if(users>0){
                GroupInfo g = new GroupInfo();
                boolean checkarray = Config.checkGroupArray(results,c.getString(IDX_ID));
                g.setId(c.getString(IDX_ID));
                g.setTitle(c.getString(IDX_TITLE));
                g.setCount(users);
                g.setMembers(getContacts(c.getString(IDX_ID),c.getString(IDX_TITLE)));
                g.setChecked(checkarray);
                if(checkarray){
                    contactAdapter.addlist(g.showMember());
                }
            }
        }
        c.close();
        //기타리스트 지정
        GroupInfo g = new GroupInfo();
        g.setId("all");
        g.setTitle("미지정");
        g.setCount(0);
        boolean checkarray=Config.checkGroupArray(results,"all");
        g.setChecked(checkarray);
//        Log.i(String.valueOf(Config.checkArray(results,"all")),"미지정");
        List<ContactData> list = new ArrayList<ContactData>(contactmap.values());
        g.setMembers(list);
        if(checkarray){
            contactAdapter.addlist(list);
        }
    }
}