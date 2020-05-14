package biddingSoftware;

import java.time.LocalDateTime;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {

     //if argument provided in args exists in dtbs already, user already exists
     // if (RetreieUserFrmDtbs(args[0]))
     //    String id = args[0];
     // else{
            User user1 =  createNewUser(args[0]);
            String id = user1.getUserID();
     //    }

	    BiddingUtils biddingSession = new BiddingUtils(UUID.fromString(id));
	    biddingSession.CreateLot(25.00, 100.00, 5.00, "cupboard",  LocalDateTime.now());
	    biddingSession.GetLotsOfUser();


    }

    public static User createNewUser(String name){
        return  new User(name);
    }
}
