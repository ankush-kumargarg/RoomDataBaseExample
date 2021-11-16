package com.demo.contact.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.RoomDatabase;

import com.demo.contact.Constant;
import com.demo.contact.R;
import com.demo.contact.RoomDatabase.MyRoomDataBase;
import com.demo.contact.model.ContactModel;

import java.util.ArrayList;

public class ContactDetailActivity extends AppCompatActivity {
    private ImageView ivBack, ivMenu, ivMessage, ivphone1, ivphone2, ivMessage2, ivEmail1, ivEmail2;
    private TextView tvName, tvMob1, tvMob2, tvEmail1, tvEmail2, tvAddress1,tvTittleAddress;
    private RelativeLayout rlPhone2, rlemail2;

    ArrayList<ContactModel> contactlist = new ArrayList<>();

    private ContactModel contactModel;
    private MyRoomDataBase roomDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        ivBack = findViewById(R.id.iv_back);
        ivMenu = findViewById(R.id.iv_menu);

        ivMessage = findViewById(R.id.iv_message1);
        ivphone1 = findViewById(R.id.iv_phone1);
        ivphone2 = findViewById(R.id.iv_phone2);
        ivMessage2 = findViewById(R.id.iv_message2);
        ivEmail1 = findViewById(R.id.iv_email1);
        ivEmail2 = findViewById(R.id.iv_email2);
        tvName = findViewById(R.id.tv_full_name);
        tvMob1 = findViewById(R.id.tv_mobile1);
        tvMob2 = findViewById(R.id.tv_mobile2);
        tvEmail1 = findViewById(R.id.tv_email1);
        tvEmail2 = findViewById(R.id.tv_email2);
        tvAddress1 = findViewById(R.id.tv_address);
        rlemail2 = findViewById(R.id.rl_email_container2);
        rlPhone2 = findViewById(R.id.rl_phone_container2);
        tvTittleAddress = findViewById(R.id.tv_title_address);

        MyRoomDataBase myRoomDataBaseinstance = MyRoomDataBase.getMyRoomInstance(this);
        roomDatabase = myRoomDataBaseinstance;

        if (getIntent() != null) {
            contactlist = (ArrayList<ContactModel>) getIntent().getSerializableExtra(Constant.CONTACT_LIST);
        }
        contactModel = new ContactModel();
        contactModel = contactlist.get(0);

        if (!contactModel.getPhone2().equals("")) {
            rlPhone2.setVisibility(View.VISIBLE);
            tvMob2.setText(contactModel.getPhone2());
        }
        if (!contactModel.getEmail2().equals("")) {
            rlemail2.setVisibility(View.VISIBLE);
            tvEmail2.setText(contactModel.getEmail2());
        }
        tvMob1.setText(contactModel.getPhone1());
        tvEmail1.setText(contactModel.getEmail1());
        tvName.setText(contactModel.getFirstName()+" "+contactModel.getLastName());

        if (!contactModel.getAddress().equals("")) {
            tvTittleAddress.setVisibility(View.VISIBLE);
            tvAddress1.setVisibility(View.VISIBLE);
            tvAddress1.setText(contactModel.getAddress());
        }

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPopUpMenu(view);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void openPopUpMenu(View view) {
        PopupMenu popup = new PopupMenu(ContactDetailActivity.this, view);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.edit:
                        Intent intent = new Intent(ContactDetailActivity.this, EditContactActivity.class);
                        intent.putExtra(Constant.CONTACT_LIST,contactlist);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.delete:
                        deleteDialog();
                        break;
                    default:
                        return false;
                }
                return false;
            }
        });
        popup.inflate(R.menu.popup_menu);
        popup.show();
    }

    private void deleteDialog() {
        Dialog dialog = new Dialog(ContactDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.delete_dialog);
        TextView cancelTV = dialog.findViewById(R.id.cancelTV);
        TextView okTV = dialog.findViewById(R.id.okTV);

        okTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roomDatabase.mainDao().deleteUser(contactModel);
                dialog.dismiss();
                finish();
            }
        });


        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}