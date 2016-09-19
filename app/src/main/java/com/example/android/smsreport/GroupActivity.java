package com.example.android.smsreport;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.AdapterView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.internal.Group;


public class GroupActivity extends AppCompatActivity {
    Button btn;
    ListView grouplist;
    ListView contactlist;
//    List<GroupInfo> groups = new ArrayList<GroupInfo>();
    GroupAdapter mAdapter;
    ContactAdapter contactAdapter;
    private  Realm realm;
    private final LinkedHashMap<String,ContactData> contactmap = new LinkedHashMap<>();
    RealmResults<ChoiceGroupid> results;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        btn = (Button)findViewById(R.id.groupbtn);
        grouplist = (ListView)findViewById(R.id.grouplist);
        contactlist = (ListView)findViewById(R.id.contactlist);
        mAdapter = new GroupAdapter();
        contactAdapter = new ContactAdapter();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> result;
                result = mAdapter.returnChoiceGroup();
/*                Log.i("EEEEEEEEEE",String.valueOf(result.size()));
                for(int i=0;i<result.size();i++){
                    Toast.makeText(getApplicationContext(),result.get(i),Toast.LENGTH_SHORT).show();
                }
                Log.i("EEEEEEEEEE","EEEEEEEEEE");*/
                realm.beginTransaction();
                results.deleteAllFromRealm();
                realm.commitTransaction();
                for(int i=0;i<result.size();i++){
                    realm.beginTransaction();
                    ChoiceGroupid gid = realm.createObject(ChoiceGroupid.class);
                    gid.setCode(result.get(i));
                    realm.commitTransaction();
                }
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.group_bar);
        grouplist.setAdapter(mAdapter);
        realm = Realm.getDefaultInstance();
        results = realm.where(ChoiceGroupid.class).findAll();
/*        Log.i("#######",String.valueOf(results.size()));
        for(int i=0;i<results.size();i++){
            Toast.makeText(getApplicationContext(),results.get(i).getCode(),Toast.LENGTH_SHORT).show();
        }
        Log.i("#######","#######");*/
        loadGroups();
        contactlist.setAdapter(contactAdapter);
        grouplist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        grouplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GroupInfo groupinfo = (GroupInfo) adapterView.getItemAtPosition(i);
                boolean groupChecked = groupinfo.isChecked();
                groupinfo.setChecked(!groupChecked);
                GroupView gv = (GroupView)view;
                gv.setChecked();
                if(groupChecked){
                    contactAdapter.removelist(groupinfo.showMember());
                }else{
                    contactAdapter.addlist(groupinfo.showMember());
                }
            }
        });
    }

    public void loadGroups() {

        //전체 연락처 리스트
        ContentResolver cr = getContentResolver();
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC ";
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, sortOrder);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[^0-9]", "");
                        ContactData cd = new ContactData();
                        cd.setGroupname("미지정");
                        cd.setName(name);
                        cd.setPhonenum(phoneNo);
                        cd.setId(id);
                        contactmap.put(id,cd);
//                        Toast.makeText(NativeContentProvider.this, "Name: " + name+ ", Phone No: " + phoneNo, Toast.LENGTH_SHORT).show();
                    }
                    pCur.close();
                }
            }
        }
        //그룹 리스트 뽑기
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
                boolean checkarray = Config.checkArray(results,c.getString(IDX_ID));
                g.setId(c.getString(IDX_ID));
                g.setTitle(c.getString(IDX_TITLE));
                g.setCount(users);
                g.setMembers(getContacts(c.getString(IDX_ID),c.getString(IDX_TITLE)));
                g.setChecked(checkarray);
//                Log.i(c.getString(IDX_ID),c.getString(IDX_TITLE));
//                Log.i(String.valueOf(Config.checkArray(results,c.getString(IDX_ID))),c.getString(IDX_TITLE));
                mAdapter.add(g);
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
        boolean checkarray=Config.checkArray(results,"all");
        g.setChecked(checkarray);
//        Log.i(String.valueOf(Config.checkArray(results,"all")),"미지정");
        List<ContactData> list = new ArrayList<ContactData>(contactmap.values());
        g.setMembers(list);
        if(checkarray){
            contactAdapter.addlist(list);
        }
        mAdapter.add(g);
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
                        new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER }, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + groupcursor.getLong(1), null, null);

                if (numberCursor.moveToFirst())
                {
                    int numberColumnIndex = numberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    do
                    {
                        String phoneNumber = numberCursor.getString(numberColumnIndex).replaceAll("[^0-9]", "");
                        ContactData member = new ContactData();
                        member.setName(groupcursor.getString(2));
                        member.setPhonenum(phoneNumber);
                        member.setGroupname(groupName);
                        member.setId(groupcursor.getString(1));
                        members.add(member);
//                        Log.i(phoneNumber+"  "+String.valueOf(groupcursor.getLong(1)), groupcursor.getString(2));
                        contactmap.remove(groupcursor.getString(1));
                    } while (numberCursor.moveToNext());
                    numberCursor.close();
                }
            }
        }
        return members;
    }
}
