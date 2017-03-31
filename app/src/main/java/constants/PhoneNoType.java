package constants;

/**
 * Created by Muzammil on 21/03/2017.
 */

public enum PhoneNoType
{
    Mobile(1),
    Landline(2);

    private int id;
    PhoneNoType(int id) {this.id = id;}
    public int getValue(){return this.id;}
    public void setValue(int id){this.id = id;}
}
