package data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.application.test.contactsapp.MainActivity;
import com.application.test.contactsapp.R;

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

    public void UpdateContact(Context context, Contact contact)
    {

    }

    public void DeleteContact(Context context, int contactId)
    {
        // delete from database
        SQLiteDatabase database = ContactReaderDbHelper.getInstance(context).getWritableDatabase();
        String query = "DELETE FROM " + ContactReaderContract.ContactEntry.TABLE_NAME +
                " WHERE " + ContactReaderContract.ContactEntry._ID + " = " + contactId;

        database.execSQL(query);
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
                return getSystemContacts();
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

    private List<Contact> getSystemContacts()
    {
        List<Contact> listContacts = new ArrayList<>();

        //contact.setFunctionType(FunctionType.System);

        return  listContacts;
    }
}
