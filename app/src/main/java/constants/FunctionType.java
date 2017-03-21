package constants;

/**
 * Created by Muzammil on 21/03/2017.
 */

public enum FunctionType
{
    ContactsApp(0),
    System(1);

    private final int id;
    FunctionType(int id) {this.id = id;}
    public int getValue(){return this.id;}
}
