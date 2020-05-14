package biddingSoftware;

import java.time.LocalDateTime;
import java.util.Date;

public class Lot {
   private int lotID;
   private  int noOfBids ;
   private  int bidID;
   private LocalDateTime expiryTime;
   private  int soldTo;
   private double startPrice;
   private  double increment;
   private  String description;
   private  double targetPrice;

   public  Lot(double startPrice, double targetPrice, double increment, String description ){
      this.startPrice = startPrice;
      this.targetPrice = targetPrice;
      this.increment = increment;
      this.description = description;

   }
}
