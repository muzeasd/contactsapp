package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Muzammil on 20/03/2017.
 */

public class ContactReaderDbHelper extends SQLiteOpenHelper
{
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MyContacts.db";

    private static ContactReaderDbHelper m_ContactReaderDbHelper;

    public static ContactReaderDbHelper getInstance(Context context)
    {
        if(m_ContactReaderDbHelper == null)
        {
            synchronized (ContactReaderDbHelper.class)
            {
                if(m_ContactReaderDbHelper == null)
                {
                    m_ContactReaderDbHelper = new ContactReaderDbHelper(context);
                }
            }
        }

        return m_ContactReaderDbHelper;
    }

    private ContactReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(data.ContactReaderContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(data.ContactReaderContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
