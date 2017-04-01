package models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import constants.FunctionType;
import constants.PhoneNoType;
import utilities.BitmapUtility;

/**
 * Created by Muzammil on 01/03/2017.
 */

public class Contact implements Parcelable
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
        this.phoneNoType = (index == 0 ? PhoneNoType.Mobile : PhoneNoType.Landline);
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

    public Contact() {}

    protected Contact(Parcel in)
    {
        id = in.readLong();
        name = in.readString();
        address = in.readString();
        phoneNo = in.readString();
        phoneNoType.setValue(in.readInt());
        email = in.readString();
        city = in.readString();
        country = in.readString();
        skypeId = in.readString();
        photo = (Bitmap)in.readParcelable(getClass().getClassLoader());
//        byte[] imageBytes = null;
//        in.readByteArray(imageBytes);
//        photo = BitmapUtility.getImage(imageBytes);
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>()
    {
        @Override
        public Contact createFromParcel(Parcel in)
        {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size)
        {
            return new Contact[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(phoneNo);
        dest.writeInt(phoneNoType.getValue());
        dest.writeString(email);
        dest.writeString(city);
        dest.writeString(country);
        dest.writeString(skypeId);
        dest.writeParcelable(photo, flags);
//        byte[] imageBytes = BitmapUtility.getBytes(this.getPhoto())
//        dest.writeByteArray(imageBytes);
    }
}
