package recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.test.contactsapp.R;

import java.util.List;

import models.Contact;

/**
 * Created by Muzammil on 01/03/2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ListContactViewHolder>
{

    private List<Contact> mListContacts;
    private SparseBooleanArray selectedItems;

    public final static class ListContactViewHolder extends RecyclerView.ViewHolder {
        public TextView name, address, phone;
        public ImageView imgView;

        public ListContactViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            address = (TextView) itemView.findViewById(R.id.address);
            phone = (TextView) itemView.findViewById(R.id.phone);
            imgView = (ImageView) itemView.findViewById(R.id.imageViewViewContact);
        }
    }

    public ContactsAdapter(List<Contact> modelData)
    {
        if (modelData == null)
        {
            throw new IllegalArgumentException("modelData must not be null");
        }

        mListContacts = modelData;
        selectedItems = new SparseBooleanArray();
    }

    @Override
    public ListContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false);
        return new ListContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListContactViewHolder holder, int position)
    {
        Contact contact = mListContacts.get(position);
        holder.name.setText(contact.getName());
        holder.address.setText(contact.getAddress());
        holder.phone.setText(contact.getCellNo());
        holder.imgView.setImageBitmap(contact.getPhoto());
    }

    @Override
    public int getItemCount()
    {
        if(mListContacts == null) return 0;
        return mListContacts.size();
    }
}
