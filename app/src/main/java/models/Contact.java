package models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import constants.FunctionType;
import constants.PhoneNoType;

/**
 * Created by Muzammil on 01/03/2017.
 */

public class Contact //implements Parcelable
{
    private long id;
    private String name;
    private String address;
    private String phoneNo;
    private PhoneNoType phoneNoType;
    private String email;
    private String city;
    private String country;
    private String skypeId;
    private Bitmap photo;
    private FunctionType functionType;

    public void setPhoneNoType(int index)
    {
        this.phoneNoType = index == 1 ? PhoneNoType.Mobile : PhoneNoType.Landline;
    }

    public int getPhoneNoType()
    {
        return (int) phoneNoType.getValue();
    }

    public void setFunctionType(FunctionType functionType)
    {
        this.functionType = functionType;
    }

    public FunctionType getFunctionType()
    {
        return this.functionType;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getSkypeId()
    {
        return skypeId;
    }

    public void setSkypeId(String skypeId)
    {
        this.skypeId = skypeId;
    }

    public Bitmap getPhoto()
    {
        return photo;
    }

    public void setPhoto(Bitmap photo)
    {
        this.photo = photo;
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

    public String getPhoneNo()
    {
        return phoneNo;
    }

    public void setPhoneNo(String phone)
    {
        this.phoneNo= phone;
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
