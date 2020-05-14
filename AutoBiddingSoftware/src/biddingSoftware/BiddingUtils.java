package biddingSoftware;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import  java.util.UUID;

public class BiddingUtils {
    private  final  UUID uuid;
    public BiddingUtils(UUID  uuid){
        this.uuid = uuid;
    }

    public  String CreateLot(double startPrice,
                             double targetPrice,
                             double increment,
                             String description,
                             LocalDateTime expiryTime){
        //create lot in dtbs and return lotID
    return "";
    }

    public  List<Lot> GetLotsOfUser(){
        //return list associated with this uuid

        return  new ArrayList<Lot>();
    }

    public  void UpdateLotDescription(String lotID, String newDescription){
        //if lot exists, updated description

    }


}
