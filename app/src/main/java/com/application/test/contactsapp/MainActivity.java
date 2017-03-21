package com.application.test.contactsapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import constants.FunctionType;
import data.ContactReaderContract;
import data.ContactReaderDbHelper;
import data.DatabaseHandler;
import models.Contact;
import recyclerview.ContactsAdapter;
import utilities.BitmapUtility;

public class MainActivity extends AppCompatActivity
{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener(){

            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft)
            {
                mViewPager.setCurrentItem(tab.getPosition());
                mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    // This method will be invoked when a new page becomes selected.
                    @Override
                    public void onPageSelected(int position) {
                        actionBar.setSelectedNavigationItem(position);
                    }

                    // This method will be invoked when the current page is scrolled
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        // Code goes here
                    }

                    // Called when the scroll state changes:
                    // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
                    @Override
                    public void onPageScrollStateChanged(int state) {
                        // Code goes here
                    }
                });
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft)
            {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft)
            {

            }
        };

        // Add 3 tabs, specifying the tab's text and TabListener
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++)
        {
            ActionBar.Tab tab =  actionBar.newTab();
            tab.setText(mSectionsPagerAdapter.getPageTitle(i));
            tab.setTabListener(tabListener);

            actionBar.addTab(tab);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_menu)
        {
            Intent intent = new Intent(this, AddContactActivity.class);
            startActivity(intent);
        }
        if (id == R.id.search_menu)
        {
            Toast.makeText(this, "Search Menu", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.action_settings)
        {
            Toast.makeText(this, "Settings Menu", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public SectionsPagerAdapter getSectionsPagerAdapter()
    {
        return mSectionsPagerAdapter;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment
    {
        private int FRAGMENT_INDEX = -1;
        private List<Contact> mContactlist = new ArrayList<>();
        private RecyclerView mRecyclerView;
        private ContactsAdapter mAdapter;

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() { }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber)
        {
            PlaceholderFragment fragment = new PlaceholderFragment();

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);

            FRAGMENT_INDEX = getArguments().getInt(ARG_SECTION_NUMBER);
            if(FRAGMENT_INDEX == 1)
            {
                mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
                mAdapter = new ContactsAdapter(mContactlist);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager((MainActivity)getActivity());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.setAdapter(mAdapter);

                PrepareContacts_ContactsApp();
            }
            else
            {
                mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
                mAdapter = new ContactsAdapter(mContactlist);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager((MainActivity)getActivity());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.setAdapter(mAdapter);

                PrepareContacts_System();
            }

            return rootView;
        }

        @Override
        public void onStart()
        {
            super.onStart();
            UpdateGUI(FRAGMENT_INDEX == 1 ? FunctionType.ContactsApp : FunctionType.System);
        }

        public void UpdateGUI(FunctionType functionType)
        {
            if(functionType == FunctionType.ContactsApp)
                PrepareContacts_ContactsApp();
//            else
//                PrepareContacts_System();
        }

        void PrepareContacts_ContactsApp()
        {
            DatabaseHandler dbHandler = DatabaseHandler.getInstance();
            List<Contact> contactList = dbHandler.GetContactsAll(FunctionType.ContactsApp, getContext(), getResources());

            mContactlist.clear();
            mContactlist.addAll(contactList);
            if(mAdapter != null) mAdapter.notifyDataSetChanged();
        }

        void PrepareContacts_System()
        {
            mContactlist.clear();

            Contact contact = new Contact();
            contact.setName("System");
            contact.setAddress("Lahore");
            contact.setPhoneNo("123456789");

            Contact contact2 = new Contact();
            contact2.setName("System");
            contact2.setAddress("Multan");
            contact2.setPhoneNo("123456789");

            Contact contact3 = new Contact();
            contact3.setName("System");
            contact3.setAddress("Karachi");
            contact3.setPhoneNo("123456789");

            Contact contact4 = new Contact();
            contact4.setName("System");
            contact4.setAddress("Pindi");
            contact4.setPhoneNo("123456789");

            Contact contact5 = new Contact();
            contact5.setName("System");
            contact5.setAddress("Haiderabad");
            contact5.setPhoneNo("123456789");

            mContactlist.add(contact);
            mContactlist.add(contact2);
            mContactlist.add(contact3);
            mContactlist.add(contact4);
            mContactlist.add(contact5);


            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {
        protected Hashtable<Integer, WeakReference<Fragment>> fragmentReferences = new Hashtable<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment = PlaceholderFragment.newInstance(position + 1);
            fragmentReferences.put(position, new WeakReference<Fragment>(fragment));
            return fragment;
        }

        @Override
        public int getCount()
        {
            return 2;
        }

        public Fragment getFragment(int fragmentId) {
            WeakReference<Fragment> ref = fragmentReferences.get(fragmentId);
            return ref == null ? null : ref.get();
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position)
            {
                case 0:
                    return "ContactsApp";
                case 1:
                    return "System";
            }
            return null;
        }
    }
}
