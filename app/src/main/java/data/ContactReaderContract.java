package data;

import android.provider.BaseColumns;

/**
 * Created by Muzammil on 20/03/2017.
 */

public class ContactReaderContract
{
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ContactReaderContract() {}

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ContactEntry.TABLE_NAME + " (" +
                    ContactEntry._ID + " INTEGER PRIMARY KEY," +
                    ContactEntry.COLUMN_NAME_NAME + " TEXT," +
                    ContactEntry.COLUMN_NAME_ADDRESS + " TEXT," +
                    ContactEntry.COLUMN_NAME_CELLNO + " TEXT," +
                    ContactEntry.COLUMN_NAME_EMAILID + " TEXT," +
                    ContactEntry.COLUMN_NAME_CITY + " TEXT," +
                    ContactEntry.COLUMN_NAME_COUNTRY + " TEXT," +
                    ContactEntry.COLUMN_NAME_SKYPEID + " TEXT," +
                    ContactEntry.COLUMN_NAME_PHOTO + " BLOB)";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ContactEntry.TABLE_NAME;

    /* Inner class that defines the table contents */
    public static class ContactEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "Contacts";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_CELLNO = "cellNo";
        public static final String COLUMN_NAME_EMAILID = "emailId";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_COUNTRY = "country";
        public static final String COLUMN_NAME_SKYPEID = "skypeId";
        public static final String COLUMN_NAME_PHOTO = "photo";
    }
}
