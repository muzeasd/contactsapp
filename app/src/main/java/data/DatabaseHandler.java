package data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.application.test.contactsapp.MainActivity;
import com.application.test.contactsapp.R;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import constants.FunctionType;
import models.Contact;
import utilities.BitmapUtility;

/**
 * Created by Muzammil on 21/03/2017.
 */

public class DatabaseHandler
{
    private static DatabaseHandler m_DatabaseHandler;
    private static Context mContext;

    private DatabaseHandler(){}

    public static DatabaseHandler getInstance()
    {
        if(m_DatabaseHandler == null)
        {
            synchronized (DatabaseHandler.class)
            {
                if(m_DatabaseHandler == null)
                {
                    m_DatabaseHandler = new DatabaseHandler();
                }
            }
        }

        return m_DatabaseHandler;
    }

    public static DatabaseHandler getInstance(Context context)
    {
        mContext = context;
        if(m_DatabaseHandler == null)
        {
            synchronized (DatabaseHandler.class)
            {
                if(m_DatabaseHandler == null)
                {
                    m_DatabaseHandler = new DatabaseHandler();
                }
            }
        }

        return m_DatabaseHandler;
    }

    public long AddContact(Context context, Contact contact)
    {
        SQLiteDatabase database = ContactReaderDbHelper.getInstance(context).getWritableDatabase();

        byte[] imageBytes = null;
        imageBytes = BitmapUtility.getBytes(contact.getPhoto());

        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_NAME, contact.getName());
        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_ADDRESS, contact.getAddress());
        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_PHONE, contact.getPhoneNo());
        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_PHONETYPE, contact.getPhoneNoType());
        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_EMAILID, contact.getEmail());
        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_CITY, contact.getCity());
        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_COUNTRY, contact.getCountry());
        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_SKYPEID, contact.getSkypeId());
        contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_PHOTO, imageBytes);

        long id = database.insert(ContactReaderContract.ContactEntry.TABLE_NAME, null, contentValues);

        return id;
    }

    public boolean UpdateContact(Context context, Contact contact)
    {
        boolean bResult = false;
        try {
            byte[] imageBytes = BitmapUtility.getBytes(contact.getPhoto());

            // update database
            SQLiteDatabase database = ContactReaderDbHelper.getInstance(context).getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_NAME, contact.getName());
            contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_ADDRESS, contact.getAddress());
            contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_PHONE, contact.getPhoneNo());
            contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_PHONETYPE, contact.getPhoneNoType());
            contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_EMAILID, contact.getEmail());
            contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_CITY, contact.getCity());
            contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_COUNTRY, contact.getCountry());
            contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_SKYPEID, contact.getSkypeId());
            contentValues.put(ContactReaderContract.ContactEntry.COLUMN_NAME_PHOTO, imageBytes);

            database.update(
                    ContactReaderContract.ContactEntry.TABLE_NAME,
                    contentValues,
                    ContactReaderContract.ContactEntry._ID + "=?",
                    new String[]{String.valueOf(contact.getId())});

            bResult = true;
        }
        catch ( SQLException sqle)
        {
            Toast.makeText(context, sqle.getMessage(), Toast.LENGTH_LONG).show();
        }

        return bResult;
    }

    public boolean DeleteContact(Context context, int contactId)
    {
        boolean bResult = false;
        try {
            // delete from database
            SQLiteDatabase database = ContactReaderDbHelper.getInstance(context).getWritableDatabase();
            String query = "DELETE FROM " + ContactReaderContract.ContactEntry.TABLE_NAME +
                    " WHERE " + ContactReaderContract.ContactEntry._ID + " = " + contactId;

            database.execSQL(query);
            bResult = true;
        }
        catch (SQLException sqle)
        {
            Toast.makeText(context, sqle.getMessage(), Toast.LENGTH_LONG).show();
        }

        return bResult;
    }

    public Contact  FindContact(Context context, int contactId, FunctionType functionType)
    {
        if(functionType == FunctionType.ContactsApp)
            return GetSqliteContact(context, contactId);
        else return GetSystemContact(context, contactId);

    }

    private Contact GetSqliteContact(Context context, int contactId)
    {
        // delete from database
        SQLiteDatabase database = ContactReaderDbHelper.getInstance(context).getReadableDatabase();

        String query = "SELECT * FROM " + ContactReaderContract.ContactEntry.TABLE_NAME +
                " WHERE " + ContactReaderContract.ContactEntry._ID + " = " + contactId;

        Cursor cursor = database.rawQuery(query, null);
        if(cursor == null || cursor.getCount() == 0) return null;

        cursor.moveToFirst();

        Contact contact = new Contact();
        contact.setId(cursor.getInt(cursor.getColumnIndex(ContactReaderContract.ContactEntry._ID)));
        contact.setName(cursor.getString(cursor.getColumnIndex(ContactReaderContract.ContactEntry.COLUMN_NAME_NAME)));
        contact.setAddress(cursor.getString(cursor.getColumnIndex(ContactReaderContract.ContactEntry.COLUMN_NAME_ADDRESS)));
        contact.setPhoneNo(cursor.getString(cursor.getColumnIndex(ContactReaderContract.ContactEntry.COLUMN_NAME_PHONE)));
        contact.setPhoneNoType(cursor.getInt(cursor.getColumnIndex(ContactReaderContract.ContactEntry.COLUMN_NAME_PHONETYPE)));
        contact.setEmail(cursor.getString(cursor.getColumnIndex(ContactReaderContract.ContactEntry.COLUMN_NAME_EMAILID)));
        contact.setCity(cursor.getString(cursor.getColumnIndex(ContactReaderContract.ContactEntry.COLUMN_NAME_CITY)));
        contact.setCountry(cursor.getString(cursor.getColumnIndex(ContactReaderContract.ContactEntry.COLUMN_NAME_COUNTRY)));
        contact.setSkypeId(cursor.getString(cursor.getColumnIndex(ContactReaderContract.ContactEntry.COLUMN_NAME_SKYPEID)));
        contact.setPhoto(BitmapUtility.getImage(cursor.getBlob(cursor.getColumnIndex(ContactReaderContract.ContactEntry.COLUMN_NAME_PHOTO))));

        return contact;
    }

    private Contact GetSystemContact(Context context, long contactId)
    {

        Contact contact = null;//new Contact();

        List<Contact> contactList = getSystemContacts(context, null);
        for(int index=0; index<contactList.size(); index++)
        {
            contact = contactList.get(index);
            if(contact.getId() == contactId)
                break;
        }

        return contact;

//        try
//        {
//            ContextWrapper contextWrapper = new ContextWrapper(context);
//            ContentResolver contentResolver = contextWrapper.getContentResolver();
//
//            // Build the Uri to query to table
//            Uri myPhoneUri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, String.valueOf(contactId));
//
//            Cursor cur = contentResolver.query(myPhoneUri, null, null, null, null);
//            if (cur.getCount() > 0)
//            {
//                while (cur.moveToNext())
//                {
//                    contact = new Contact();
//
//                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
//                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                    String phoneNo = "";
//                    if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
//                        Cursor pCur = contentResolver.query(
//                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                                null,
//                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
//                                new String[]{id}, null);
//                        while (pCur.moveToNext()) {
//                            phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
////                                phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE_HOME));
////                                phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
////                            Toast.makeText(getContext(), "Name: " + name + ", Phone No: " + phoneNo, Toast.LENGTH_SHORT).show();
//                        }
//                        pCur.close();
//                    }
//
//                    String address = "";
//                    Uri contact_address_uri = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
//                    Cursor addr_cur = contextWrapper.getContentResolver().query(contact_address_uri,null, null, null, null);
//                    while(addr_cur.moveToNext())
//                    {
//                        address = addr_cur.getString(addr_cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
////                            String  Postcode = addr_cur.getString(addr_cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
////                            String  City = addr_cur.getString(addr_cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
//                    }
//                    addr_cur.close();
//
//                    InputStream is = openThumbnailPhoto((long) Integer.parseInt(id), contentResolver);//openDisplayPhoto((long) Integer.parseInt(id), cr);
//
//                    byte[] imageBytes = IOUtils.toByteArray(is);
//                    Bitmap bmp = (imageBytes == null ? null : BitmapUtility.getImage(imageBytes));
//
//                    contact.setId(Integer.parseInt(id));
//                    contact.setName(name);
//                    contact.setAddress(address);
//                    contact.setPhoneNo(phoneNo);
//                    contact.setPhoneNoType(0);
//                    contact.setPhoto(bmp);
//                    contact.setFunctionType(FunctionType.System);
//
//                    contact.setEmail("");
//                    contact.setCity("");
//                    contact.setCountry("");
//                    contact.setSkypeId("");
//
//                }
//            }
//        }
//        catch (Exception ex)
//        {
//
//        }
//        return contact;
    }

    public void UpdateGUI(Context context, FunctionType functionType)
    {
        // now notify the corresponding fragment to update its GUI
        MainActivity mainActivity = (MainActivity) context;
        MainActivity.SectionsPagerAdapter pagerAdapter = mainActivity.getSectionsPagerAdapter();
        MainActivity.PlaceholderFragment fragment = (MainActivity.PlaceholderFragment) pagerAdapter.getFragment(functionType == FunctionType.ContactsApp ? 0 : 1);
        fragment.UpdateGUI(functionType);
    }

    public List<Contact> GetContactsAll(FunctionType functionType, Context context, Resources resources)
    {
        switch (functionType)
        {
            case ContactsApp:
                return getSqliteContacts(context, resources);
            case System:
                return getSystemContacts(context, resources);
        }

        return null;
    }

    private List<Contact> getSqliteContacts(Context context, Resources resources)
    {
        List<Contact> listContacts = new ArrayList<>();

        String query = "SELECT * FROM " + ContactReaderContract.ContactEntry.TABLE_NAME;
        SQLiteDatabase database = ContactReaderDbHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);

        if(cursor == null) return null;

        int id_index = cursor.getColumnIndex(ContactReaderContract.ContactEntry._ID);
        int name_index = cursor.getColumnIndex(ContactReaderContract.ContactEntry.COLUMN_NAME_NAME);
        int address_index = cursor.getColumnIndex(ContactReaderContract.ContactEntry.COLUMN_NAME_ADDRESS);
        int phoneNo_index = cursor.getColumnIndex(ContactReaderContract.ContactEntry.COLUMN_NAME_PHONE);
        int phoneNoType_index = cursor.getColumnIndex(ContactReaderContract.ContactEntry.COLUMN_NAME_PHONETYPE);
        int emailId_index = cursor.getColumnIndex(ContactReaderContract.ContactEntry.COLUMN_NAME_EMAILID);
        int city_index = cursor.getColumnIndex(ContactReaderContract.ContactEntry.COLUMN_NAME_CITY);
        int country_index = cursor.getColumnIndex(ContactReaderContract.ContactEntry.COLUMN_NAME_COUNTRY);
        int skypeId_index = cursor.getColumnIndex(ContactReaderContract.ContactEntry.COLUMN_NAME_SKYPEID);
        int photo_index = cursor.getColumnIndex(ContactReaderContract.ContactEntry.COLUMN_NAME_PHOTO);

        while(cursor.moveToNext())
        {
            long id = cursor.getLong(id_index);
            String name = cursor.getString(name_index);
            String address = cursor.getString(address_index);
            String phoneNo = cursor.getString(phoneNo_index);
            int phoneNoType = cursor.getInt(phoneNoType_index);
            String emailId = cursor.getString(emailId_index);
            String city = cursor.getString(city_index);
            String country = cursor.getString(country_index);
            String skypeId = cursor.getString(skypeId_index);
            byte[] photo = cursor.getBlob(photo_index);

            Bitmap bmp = (photo == null ?  BitmapFactory.decodeResource(resources, R.mipmap.ic_person_black_36dp) : BitmapUtility.getImage(photo));

            Contact contact = new Contact();
            contact.setId(id);
            contact.setName(name);
            contact.setAddress(address);
            contact.setPhoneNo(phoneNo);
            contact.setPhoneNoType(phoneNoType);
            contact.setEmail(emailId);
            contact.setCity(city);
            contact.setCountry(country);
            contact.setSkypeId(skypeId);
            contact.setPhoto(bmp);
            contact.setFunctionType(FunctionType.ContactsApp);

            listContacts.add(contact);
        }

        return  listContacts;
    }

    private List<Contact> getSystemContacts(Context context, Resources resources)
    {
        List<Contact> listContacts = new ArrayList<>();

        try
        {
            ContextWrapper contextWrapper = new ContextWrapper(context);

            ContentResolver cr = contextWrapper.getContentResolver();
            String[] projection = new String[]{ContactsContract.PhoneLookup._ID};
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            if (cur.getCount() > 0)
            {
                while (cur.moveToNext())
                {
                    Contact contact = new Contact();

                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String phoneNo = "";
                    if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                                phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE_HOME));
//                                phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                            Toast.makeText(getContext(), "Name: " + name + ", Phone No: " + phoneNo, Toast.LENGTH_SHORT).show();
                        }
                        pCur.close();
                    }
//
//                    int type = cur.getInt(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
//                    switch (type)
//                    {
//                        case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
//                            contact.setPhoneNoType(1);  // Landline
//                            break;
//                        case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
//                            contact.setPhoneNoType(0);  // Mobile
//                            break;
//                        case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
//                            contact.setPhoneNoType(1);  // Landline
//                            break;
//                    }

                    String address = "";
                    Uri contact_address_uri = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
                    Cursor addr_cur = contextWrapper.getContentResolver().query(contact_address_uri,null, null, null, null);
                    while(addr_cur.moveToNext())
                    {
                        address = addr_cur.getString(addr_cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
//                            String  Postcode = addr_cur.getString(addr_cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
//                            String  City = addr_cur.getString(addr_cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                    }
                    addr_cur.close();

                    InputStream is = openThumbnailPhoto((long) Integer.parseInt(id), cr);//openDisplayPhoto((long) Integer.parseInt(id), cr);

                    byte[] imageBytes = IOUtils.toByteArray(is);
//                    Bitmap bmp = (imageBytes == null ? BitmapFactory.decodeResource(resources, R.mipmap.ic_person_black_36dp) : BitmapUtility.getImage(imageBytes));
                    Bitmap bmp = (imageBytes == null ? null : BitmapUtility.getImage(imageBytes));

                    contact.setId(Integer.parseInt(id));
                    contact.setName(name);
                    contact.setAddress(address);
                    contact.setPhoneNo(phoneNo);
                    contact.setPhoneNoType(0);
                    contact.setPhoto(bmp);
                    contact.setFunctionType(FunctionType.System);

                    contact.setEmail("");
                    contact.setCity("");
                    contact.setCountry("");
                    contact.setSkypeId("");

                    listContacts.add(contact);
                }
            }
        }
        catch (Exception ex)
        {

        }

        return  listContacts;
    }


    static InputStream openThumbnailPhoto(long contactId, ContentResolver contentResolver) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = contentResolver.query(photoUri,
                new String[] {ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return new ByteArrayInputStream(data);
                }
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    static InputStream openLargeDisplayPhoto(long contactId, ContentResolver contentResolver) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri displayPhotoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO);
        try {
            AssetFileDescriptor fd = contentResolver.openAssetFileDescriptor(displayPhotoUri, "r");
            return fd.createInputStream();
        } catch (IOException e) {
            return null;
        }
    }
}
