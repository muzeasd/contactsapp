package models;

import constants.FunctionType;

/**
 * Created by Muzammil on 22/03/2017.
 */

public class ContactTag
{
    private int contactId;
    private FunctionType functionType;

    public FunctionType getFunctionType()
    {
        return functionType;
    }

    public void setFunctionType(FunctionType functionType)
    {
        this.functionType = functionType;
    }

    public int getContactId()
    {
        return contactId;
    }

    public void setContactId(int contactId)
    {
        this.contactId = contactId;
    }
}
