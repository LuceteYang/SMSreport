package com.example.android.smsreport;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class GroupActivity extends AppCompatActivity {
    Button btn;
    ListView grouplist;
    ListView contactlist;
    List<GroupInfo> groups = new ArrayList<GroupInfo>();
    GroupAdapter mAdapter;
    ContactAdapter contactAdapter;
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
                Intent intent = new Intent();
                intent.putExtra("edittextvalue","GroupToMain");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.group_bar);
        loadGroups();
        contactlist.setAdapter(contactAdapter);
        grouplist.setAdapter(mAdapter);
        grouplist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    public void loadGroups() {

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
                g.setId(c.getString(IDX_ID));
                g.setTitle(c.getString(IDX_TITLE));
                g.setCount(users);
                g.setMembers(getContacts(c.getString(IDX_ID)));
                Log.i(c.getString(IDX_ID),c.getString(IDX_TITLE));
                groups.add(g);
            }
        }

        GroupInfo g = new GroupInfo();
        g.setId("all");
        g.setTitle("기타");
        g.setCount(0);
        groups.add(g);
        c.close();
        for(int i = 0;i<groups.size();i++){
            mAdapter.add(groups.get(i));
        }


    }
    private List getContacts(String groupID) {
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
            selection = ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID
                    + " = ?";
            selectionArgs = new String[]{groupID};
        }

        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC ";

        Cursor groupcursor = getContentResolver().query(uri, projection,
                selection, selectionArgs, sortOrder);

//        MatrixCursor result = new MatrixCursor(projection);
        Set<Long> seen = new HashSet<Long>();
        while (groupcursor.moveToNext()) {
            long raw = groupcursor.getLong(1);
            if (!seen.contains(raw)) {
//                seen.add(raw);
//                result.addRow(new Object[]{cursor.getLong(0), cursor.getLong(1), cursor.getString(2)});
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
//                        Log.i(phoneNumber+"  "+String.valueOf(groupcursor.getLong(1)), groupcursor.getString(2));
                        members.add(member);
                        contactAdapter.add(member);
                    } while (numberCursor.moveToNext());
                    numberCursor.close();
                }
            }
        }
        return members;
//        return result;
    }
}
