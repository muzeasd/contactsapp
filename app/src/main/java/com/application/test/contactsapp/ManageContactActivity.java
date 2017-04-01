package com.application.test.contactsapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import constants.FunctionType;
import data.DatabaseHandler;
import models.Contact;
import utilities.BitmapUtility;

public class ManageContactActivity extends AppCompatActivity
{

    ImageView m_ImageViewUpdateContact;

    int mContactId = -1;
    int mContactFunctionType = -1;

    boolean isEnabled = false;

    static final int REQUEST_IMAGE_CAPTURE_MANAGE_CONTACT = 1010;

    Spinner m_SpinnerPhoneNoType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_contact);

        View.OnClickListener imageBoxClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(cameraIntent.resolveActivity(getPackageManager()) != null)
                {
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE_MANAGE_CONTACT);
                }
            }
        };

        // Add PhoneNoType
        m_SpinnerPhoneNoType = ((Spinner) findViewById(R.id.spinnerPhoneNoTypeUpdateContact));
        List<String> list = new ArrayList<>();
        list.add("Mobile");
        list.add("Landline");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        m_SpinnerPhoneNoType.setAdapter(dataAdapter);

        // set ClickListener on ImageView
        m_ImageViewUpdateContact = (ImageView) findViewById(R.id.imageViewUpdateContact);
        m_ImageViewUpdateContact.setOnClickListener(imageBoxClickListener);
        (findViewById(R.id.textViewImageUpdateContact)).setOnClickListener(imageBoxClickListener);

        // retrieve data from intent_extra
        mContactId = getIntent().getIntExtra("contact_id", -1);
        mContactFunctionType = getIntent().getIntExtra("contact_function_type", -1);

        // setup initial gui
        (findViewById(R.id.txtNameUpdateContact)).setEnabled(false);
        (findViewById(R.id.txtAddressUpdateContact)).setEnabled(false);
        (findViewById(R.id.txtPhoneNoUpdateContact)).setEnabled(false);
        (findViewById(R.id.txtEmailUpdateContact)).setEnabled(false);
        (findViewById(R.id.txtCityUpdateContact)).setEnabled(false);
        (findViewById(R.id.txtCountryUpdateContact)).setEnabled(false);
        (findViewById(R.id.txtSkypeIdUpdateContact)).setEnabled(false);
        m_SpinnerPhoneNoType.setEnabled(false);
        m_ImageViewUpdateContact.setEnabled(false);
        (findViewById(R.id.textViewImageUpdateContact)).setVisibility(View.INVISIBLE);

        FunctionType contactFunctionType = (mContactFunctionType == 0 ? FunctionType.ContactsApp : FunctionType.System);
        Contact contact = DatabaseHandler.getInstance().FindContact(this, mContactId, contactFunctionType);

        // enable delete button only when it is sqlite contact
        (findViewById(R.id.btnDeleteContact)).setEnabled(mContactFunctionType == 0 ? true : false);

        // get all values and show them
        ((EditText) findViewById(R.id.txtNameUpdateContact)).setText(contact.getName());
        ((EditText) findViewById(R.id.txtAddressUpdateContact)).setText(contact.getAddress());
        ((EditText) findViewById(R.id.txtPhoneNoUpdateContact)).setText(contact.getPhoneNo());
        m_SpinnerPhoneNoType.setSelection(contact.getPhoneNoType());
        ((EditText) findViewById(R.id.txtEmailUpdateContact)).setText(contact.getEmail());
        ((EditText) findViewById(R.id.txtCityUpdateContact)).setText(contact.getCity());
        ((EditText) findViewById(R.id.txtCountryUpdateContact)).setText(contact.getCountry());
        ((EditText) findViewById(R.id.txtSkypeIdUpdateContact)).setText(contact.getSkypeId());
        m_ImageViewUpdateContact.setImageBitmap(contact.getPhoto());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_IMAGE_CAPTURE_MANAGE_CONTACT && resultCode == RESULT_OK)
        {
            Bundle bundle = data.getExtras();
            Bitmap bmp = (Bitmap)bundle.get("data");
            ((ImageView)findViewById(R.id.imageViewUpdateContact)).setImageBitmap(bmp);
        }
    }

    public void OnClick_ToggleEdit(View view)
    {
        isEnabled = !isEnabled;

        (findViewById(R.id.txtNameUpdateContact)).setEnabled(isEnabled);
        (findViewById(R.id.txtAddressUpdateContact)).setEnabled(isEnabled);
        (findViewById(R.id.txtPhoneNoUpdateContact)).setEnabled(isEnabled);
        (findViewById(R.id.txtEmailUpdateContact)).setEnabled(isEnabled);
        (findViewById(R.id.txtCityUpdateContact)).setEnabled(isEnabled);
        (findViewById(R.id.txtCountryUpdateContact)).setEnabled(isEnabled);
        (findViewById(R.id.txtSkypeIdUpdateContact)).setEnabled(isEnabled);
        m_SpinnerPhoneNoType.setEnabled(isEnabled);

        if(isEnabled) (findViewById(R.id.textViewImageUpdateContact)).setVisibility(View.VISIBLE);
        else (findViewById(R.id.textViewImageUpdateContact)).setVisibility(View.INVISIBLE);
    }

    public void OnClick_DeleteContact(final View view)
    {
        if(mContactFunctionType == 1) return;

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Delete contact ?");
        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                boolean bResult = DatabaseHandler.getInstance().DeleteContact(view.getContext(), mContactId);
                if(bResult)
                {
                    Toast.makeText(view.getContext(), "Contact deleted.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
        adb.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        });

        adb.show();
    }

    public void OnClick_UpdateContact(View view)
    {
        if(!isEnabled) return;

        String name = ((EditText) findViewById(R.id.txtNameUpdateContact)).getText().toString();
        String address = ((EditText) findViewById(R.id.txtAddressUpdateContact)).getText().toString();
        String phoneNo = ((EditText) findViewById(R.id.txtPhoneNoUpdateContact)).getText().toString();
        String email = ((EditText) findViewById(R.id.txtEmailUpdateContact)).getText().toString();
        String city = ((EditText) findViewById(R.id.txtCityUpdateContact)).getText().toString();
        String country = ((EditText) findViewById(R.id.txtCountryUpdateContact)).getText().toString();
        String skypeId = ((EditText) findViewById(R.id.txtSkypeIdUpdateContact)).getText().toString();
        Bitmap photo = ((BitmapDrawable)m_ImageViewUpdateContact.getDrawable()).getBitmap();

        String strPhoneNoType = (String)m_SpinnerPhoneNoType.getSelectedItem();
        int phoneNoType = (strPhoneNoType.trim().toLowerCase().equals("mobile") ? 0 : 1);

        Contact contact = new Contact();
        contact.setId(mContactId);
        contact.setName(name);
        contact.setAddress(address);
        contact.setPhoneNo(phoneNo);
        contact.setPhoneNoType(phoneNoType);
        contact.setEmail(email);
        contact.setCity(city);
        contact.setCountry(country);
        contact.setSkypeId(skypeId);
        contact.setPhoto(photo);

        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        boolean bResult = dbHandler.UpdateContact(this, contact);
        if(bResult)
            Toast.makeText(this, "Contact updated.", Toast.LENGTH_SHORT).show();
    }
}
