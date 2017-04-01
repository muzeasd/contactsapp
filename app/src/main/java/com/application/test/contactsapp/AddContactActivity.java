package com.application.test.contactsapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaFormat;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import data.ContactReaderContract;
import data.ContactReaderDbHelper;
import data.DatabaseHandler;
import models.Contact;
import utilities.BitmapUtility;

public class AddContactActivity extends AppCompatActivity
{

    static final int REQUEST_IMAGE_CAPTURE_ADD_CONTACT = 1000;

    ImageView m_ImageViewAddContact;
    Spinner m_SpinnerPhoneNoType;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        m_ImageViewAddContact = (ImageView)findViewById(R.id.imageViewAddContact);
        m_ImageViewAddContact.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(cameraIntent.resolveActivity(getPackageManager()) != null)
                {
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE_ADD_CONTACT);
                }
            }
        });

        m_SpinnerPhoneNoType = (Spinner) findViewById(R.id.spinnerPhoneNoType);
        List<String> list = new ArrayList<>();
        list.add("Mobile");
        list.add("Landline");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        m_SpinnerPhoneNoType.setAdapter(dataAdapter);
        m_SpinnerPhoneNoType.setSelection(0);
    }

    /*  Must Be public, Must Return void, Must define a View as parameter (this was the view we clicked on)*/
    public void OnClick_AddContact(View view)
    {
        String name = ((EditText) findViewById(R.id.txtName)).getText().toString();
        String address = ((EditText) findViewById(R.id.txtAddress)).getText().toString();
        String phoneNo = ((EditText) findViewById(R.id.txtCellNo)).getText().toString();
        String email = ((EditText) findViewById(R.id.txtEmail)).getText().toString();
        String city = ((EditText) findViewById(R.id.txtCity)).getText().toString();
        String country = ((EditText) findViewById(R.id.txtCountry)).getText().toString();
        String skypeId = ((EditText) findViewById(R.id.txtSkypeId)).getText().toString();
        Bitmap photo = ((BitmapDrawable)m_ImageViewAddContact.getDrawable()).getBitmap();

        String strPhoneNoType = (String)m_SpinnerPhoneNoType.getSelectedItem();
        int phoneNoType = (strPhoneNoType.trim().toLowerCase().equals("mobile") ? 0 : 1);

        Contact contact = new Contact();
        contact.setName(name);
        contact.setAddress(address);
        contact.setPhoneNo(phoneNo);
        contact.setPhoneNoType(phoneNoType);
        contact.setEmail(email);
        contact.setCity(city);
        contact.setCountry(country);
        contact.setSkypeId(skypeId);
        contact.setPhoto(photo);

        long id = DatabaseHandler.getInstance().AddContact(this, contact);
        if(id >= 0)
        {
            Toast.makeText(this, "Contact added", Toast.LENGTH_SHORT).show();

            ((EditText) findViewById(R.id.txtName)).setText("");
            ((EditText) findViewById(R.id.txtAddress)).setText("");
            ((EditText) findViewById(R.id.txtCellNo)).setText("");
            ((EditText) findViewById(R.id.txtEmail)).setText("");
            ((EditText) findViewById(R.id.txtCity)).setText("");
            ((EditText) findViewById(R.id.txtCountry)).setText("");
            ((EditText) findViewById(R.id.txtSkypeId)).setText("");
            m_ImageViewAddContact.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_person_black_36dp));
        }

//        byte[] imageBytes = null;
//        if(photo != null)
//            imageBytes = BitmapUtility.getBytes(photo);
//
//        SQLiteDatabase database = ContactReaderDbHelper.getInstance(this).getWritableDatabase();
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_NAME, name);
//        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_ADDRESS, address);
//        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_PHONE, phoneNo);
//        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_PHONETYPE, phoneNoType);
//        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_EMAILID, email);
//        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_CITY, city);
//        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_COUNTRY, country);
//        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_SKYPEID, skypeId);
//        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_PHOTO, imageBytes);

//        long id = database.insert(ContactReaderContract.ContactEntry.TABLE_NAME, null, contentValues);

        int a = 10;
        int b = 10;
        int result = a + b;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_IMAGE_CAPTURE_ADD_CONTACT && resultCode == RESULT_OK)
        {
            Bundle bundle = data.getExtras();
            Bitmap bmp = (Bitmap)bundle.get("data");
            ((ImageView)findViewById(R.id.imageViewAddContact)).setImageBitmap(bmp);
        }
    }
}
