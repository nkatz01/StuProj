package biddingSoftware;

/**
 * Write a description of class User here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class User
{

    private String name;
    private  String userID;

    public User(String name)
    {
         this.name=name;
         //create new user row in dtbs and get row id
        //this.userID = dtbsRowID

    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public String getName()
    {

        return name;
    }
    public String getUserID()
    {

        return userID ;
    }
}
