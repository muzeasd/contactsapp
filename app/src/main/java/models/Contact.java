package models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Muzammil on 01/03/2017.
 */

public class Contact //implements Parcelable
{
    private String name;
    private String address;
    private String phone;
    private String email;
    private Bitmap Photo;

    public Bitmap getPhoto()
    {
        return Photo;
    }

    public void setPhoto(Bitmap photo)
    {
        Photo = photo;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }



//    protected Contact(Parcel in)
//    {
//        name = in.readString();
//        address = in.readString();
//        phone = in.readString();
//        email = in.readString();
//        Photo = in.readParcelable(Bitmap.class.getClassLoader());
//    }
//
//    public static final Creator<Contact> CREATOR = new Creator<Contact>()
//    {
//        @Override
//        public Contact createFromParcel(Parcel in)
//        {
//            return new Contact(in);
//        }
//
//        @Override
//        public Contact[] newArray(int size)
//        {
//            return new Contact[size];
//        }
//    };
//
//    @Override
//    public int describeContents()
//    {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags)
//    {
//        dest.writeString(name);
//        dest.writeString(address);
//        dest.writeString(phone);
//        dest.writeString(email);
//        dest.writeParcelable(Photo, flags);
//    }
}
