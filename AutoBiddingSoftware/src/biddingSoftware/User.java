package biddingSoftware;


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


    public String getName()
    {

        return name;
    }
    public String getUserID()
    {

        return userID ;
    }
}
