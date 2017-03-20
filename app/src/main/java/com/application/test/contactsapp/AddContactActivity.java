package com.application.test.contactsapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaFormat;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import data.ContactReaderContract;
import data.ContactReaderDbHelper;
import utilities.BitmapUtility;

public class AddContactActivity extends AppCompatActivity
{

    static final int REQUEST_IMAGE_CAPTURE = 1000;

    ImageView m_ImageViewAddContact;

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
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }

    /*  Must Be public, Must Return void, Must define a View as parameter (this was the view we clicked on)*/
    public void OnClick_AddContact(View view)
    {
        String name = ((EditText) findViewById(R.id.txtName)).getText().toString();
        String address = ((EditText) findViewById(R.id.txtAddress)).getText().toString();
        String cellNo = ((EditText) findViewById(R.id.txtCellNo)).getText().toString();
        String email = ((EditText) findViewById(R.id.txtEmail)).getText().toString();
        String city = ((EditText) findViewById(R.id.txtCity)).getText().toString();
        String country = ((EditText) findViewById(R.id.txtCountry)).getText().toString();
        String skypeId = ((EditText) findViewById(R.id.txtSkypeId)).getText().toString();
        Bitmap photo = ((BitmapDrawable)m_ImageViewAddContact.getDrawable()).getBitmap();

        byte[] imageBytes = null;
        if(photo != null)
            imageBytes = BitmapUtility.getBytes(photo);

        SQLiteDatabase database = ContactReaderDbHelper.getInstance(this).getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_NAME, name);
        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_ADDRESS, address);
        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_CELLNO, cellNo);
        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_EMAILID, email);
        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_CITY, city);
        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_COUNTRY, country);
        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_SKYPEID, skypeId);
        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_PHOTO, imageBytes);

        long id = database.insert(ContactReaderContract.ContactEntry.TABLE_NAME, null, contentValues);

        int a = 10;
        int b = 10;
        int result = a + b;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle bundle = data.getExtras();
            Bitmap bmp = (Bitmap)bundle.get("data");
            ((ImageView)findViewById(R.id.imageViewAddContact)).setImageBitmap(bmp);
        }
    }
}
