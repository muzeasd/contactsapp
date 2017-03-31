package recyclerview;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.test.contactsapp.ManageContactActivity;
import com.application.test.contactsapp.R;

import java.util.List;

import data.DatabaseHandler;
import models.Contact;
import models.ContactTag;

/**
 * Created by Muzammil on 01/03/2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ListContactViewHolder>
{

    private List<Contact> mListContacts;
    private SparseBooleanArray selectedItems;

    public final static class ListContactViewHolder extends RecyclerView.ViewHolder {
        public TextView id, name, address, phone;
        public ImageView imgView;

        public ListContactViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.txtNameContactRow);
            address = (TextView) itemView.findViewById(R.id.txtAddressContactRow);
            phone = (TextView) itemView.findViewById(R.id.txtPhoneNoContactRow);
            imgView = (ImageView) itemView.findViewById(R.id.imageViewContactRow);
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
    public ListContactViewHolder onCreateViewHolder(final ViewGroup parent, int viewType)
    {
//        String parent_name = parent.getParent().getClass().getName();

        // inflate the view
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false);

        // click_listener for this view
        View.OnClickListener onClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                ContactTag contactTag = (ContactTag)itemView.getTag();

                Intent intent = new Intent(parent.getContext(), ManageContactActivity.class);
                intent.putExtra("contact_id", contactTag.getContactId());
                intent.putExtra("contact_function_type", contactTag.getFunctionType().getValue());
                parent.getContext().startActivity(intent);

//                // first delete the contact from database
//                DatabaseHandler.getInstance().DeleteContact(parent.getContext(), contactTag.getContactId());
//                // then delete from the GUI list
//                DatabaseHandler.getInstance().UpdateGUI(parent.getContext(), contactTag.getFunctionType());
            }
        };

        // attach the click_listener
        itemView.setOnClickListener(onClickListener);

        return new ListContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListContactViewHolder holder, int position)
    {
        Contact contact = mListContacts.get(position);
        holder.name.setText(contact.getName());
        holder.address.setText(contact.getAddress());
        holder.phone.setText(contact.getPhoneNo());
        holder.imgView.setImageBitmap(contact.getPhoto());

        ContactTag contactTag = new ContactTag();
        contactTag.setContactId((int)contact.getId());
        contactTag.setFunctionType(contact.getFunctionType());

        holder.itemView.setTag(contactTag);
    }

    @Override
    public int getItemCount()
    {
        if(mListContacts == null) return 0;
        return mListContacts.size();
    }
}
