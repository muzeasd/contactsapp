package com.application.test.contactsapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

        // Following 3 buttons' visibility depends upon whether it is sqlite contact ?
        FunctionType functionType = (mContactFunctionType == 0 ? FunctionType.ContactsApp : FunctionType.System);
        (findViewById(R.id.btnDeleteContact)).setVisibility(functionType == FunctionType.ContactsApp ? View.VISIBLE : View.INVISIBLE);
        (findViewById(R.id.btnToggleEditContact)).setVisibility(functionType == FunctionType.ContactsApp ? View.VISIBLE : View.INVISIBLE);
        //(findViewById(R.id.btnUpdateContact)).setVisibility(View.INVISIBLE);

        // find contact with given contactId
        Contact contact = DatabaseHandler.getInstance().FindContact(getApplicationContext(), mContactId, functionType);
        if(contact != null)
        {
            if(contact.getPhoto() == null)
                contact.setPhoto(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_person_black_36dp));

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

    private void ToggleEditCommunicationButtons(boolean value)
    {
        (findViewById(R.id.btnSendText)).setEnabled(value);
        (findViewById(R.id.btnMakeCall)).setEnabled(value);
    }

    private void ToggleEditUpdateButton(boolean value)
    {
        ((Button)findViewById(R.id.btnUpdateContact)).setEnabled(value);
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

        ToggleEditCommunicationButtons(isEnabled ? false : true);
        ToggleEditUpdateButton(isEnabled ? true : false);

        (findViewById(R.id.textViewImageUpdateContact)).setVisibility(isEnabled ? View.VISIBLE : View.INVISIBLE);
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
        if(!isEnabled)return;

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

    public void OnClick_SendText(View view)
    {
        String phoneNumber = ((EditText) findViewById(R.id.txtPhoneNoUpdateContact)).getText().toString();
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:" + phoneNumber));
        startActivity(sendIntent);
    }

    public void OnClick_MakeCall(View view)
    {
        String phoneNumber = ((EditText) findViewById(R.id.txtPhoneNoUpdateContact)).getText().toString();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
        startActivity(intent);
    }
}
