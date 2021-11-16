package com.demo.contact.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.contact.Constant;
import com.demo.contact.R;
import com.demo.contact.RoomDatabase.MyRoomDataBase;
import com.demo.contact.adapter.ContactAdapter;
import com.demo.contact.model.ContactModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout rlCreateContact;
    private RecyclerView rvContact;
    private ContactAdapter contactAdapter;
    private List<ContactModel> contactList = new ArrayList<>();
    private String[] contact = {"ankush", "Ajay", "naveen", "Sahil"};

    MyRoomDataBase myRoomDataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rlCreateContact = findViewById(R.id.rl_create_contact);
        rvContact = findViewById(R.id.rv_contact);

        MyRoomDataBase myRoomDataBaseinstance = MyRoomDataBase.getMyRoomInstance(this);
        myRoomDataBase = myRoomDataBaseinstance;

        rvContact.setLayoutManager(new LinearLayoutManager(this));
        rvContact.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));

        contactList.addAll(myRoomDataBase.mainDao().getAllContact());

       /* for (int i = 0; i < contact.length; i++) {
            ContactModel contactModel = new ContactModel();
            contactModel.setFirstName(contact[i]);
            contactList.add(contactModel);
        }*/

        contactAdapter = new ContactAdapter(MainActivity.this, contactList, new ContactAdapter.OnClickMenuListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, ContactDetailActivity.class);
                ArrayList<ContactModel> cntList = new ArrayList<>();
                cntList.add(contactList.get(position));
                intent.putExtra(Constant.CONTACT_LIST, cntList);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(int position) {
                openDialog(position);
            }
        });
        rvContact.setAdapter(contactAdapter);

        rlCreateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateContactActivity.class);
                startActivity(intent);
            }
        });
    }

    private void openDialog(int position) {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.call_message_dailog);
        RelativeLayout rlphone1 = dialog.findViewById(R.id.rl_phone_container1);
        RelativeLayout rlphone2 = dialog.findViewById(R.id.rl_phone_container2);
        RelativeLayout rlemail1 = dialog.findViewById(R.id.rl_email_container1);
        RelativeLayout rlemail2 = dialog.findViewById(R.id.rl_email_container2);
        ImageView phone1 = dialog.findViewById(R.id.iv_phone1);
        ImageView phone2 = dialog.findViewById(R.id.iv_phone2);
        ImageView message1 = dialog.findViewById(R.id.iv_message1);
        ImageView message2 = dialog.findViewById(R.id.iv_message2);
        ImageView email1 = dialog.findViewById(R.id.iv_email1);
        ImageView email2 = dialog.findViewById(R.id.iv_email2);
        TextView tvEmail1 = dialog.findViewById(R.id.tv_email1);
        TextView tvEmail2 = dialog.findViewById(R.id.tv_email2);
        TextView tvPhone1 = dialog.findViewById(R.id.tv_mobile1);
        TextView tvPhone2 = dialog.findViewById(R.id.tv_mobile2);
        if (!contactList.get(position).getPhone2().equals("")) {
            rlphone2.setVisibility(View.VISIBLE);
            tvPhone2.setText(contactList.get(position).getPhone2());
        }
        if (!contactList.get(position).getEmail2().equals("")) {
            rlemail2.setVisibility(View.VISIBLE);
            tvEmail2.setText(contactList.get(position).getEmail2());
        }
        tvPhone1.setText(contactList.get(position).getPhone1());
        tvEmail1.setText(contactList.get(position).getEmail1());

        phone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + tvPhone1.getText().toString()));
                startActivity(intent);
            }
        });
        phone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + tvPhone2.getText().toString()));
                startActivity(intent);
            }
        });
        message1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", "" + tvPhone1.getText().toString());
                startActivity(smsIntent);
            }
        });
        message2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS();
            }
        });
        email1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        email2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog.show();
    }

    private void sendSMS() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
        {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(this); // Need to change the build to API 19

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "text");

            {
                sendIntent.setPackage(defaultSmsPackageName);
            }
            startActivity(sendIntent);

        } else // For early versions, do what worked for you before.
        {
            Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", "phoneNumber");
            smsIntent.putExtra("sms_body", "message");
            startActivity(smsIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        contactList.clear();
        contactList.addAll(myRoomDataBase.mainDao().getAllContact());
        contactAdapter.notifyDataSetChanged();
    }
}