package com.project.biddingSoft.service;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public  class ExceptionsCreateor {
	
	private ExceptionsCreateor() {//so that we can't directly instanciate this
		 
	}
	
	@Bean
	public final static ExceptionsCreateor BiddingSoftExceptionsFactory() {//this works for both, default as well as parameterized constructors 
		return new ExceptionsCreateor();
	}
	
  public class LotHasEndedException extends BiddingSoftExceptions {
	private static final String throwableMsg = "lot endTime is qual or before current time";
	private static final int id = 1;
	
	public LotHasEndedException() {
		super(id, new Throwable(throwableMsg));
	}

}
  
  
  public class BiddingSoftExceptions extends RuntimeException{
  
	  private   String throwableMsg ;
	private BiddingSoftExceptions(String errorMessage, Throwable err) {
		super(errorMessage, err);
		throwableMsg = errorMessage;

	}
	
	public String getId() {
		return getMessage();
	}
	public   String getMsg() {
		return throwableMsg;
	}
	
	private BiddingSoftExceptions(int id, Throwable err) {
		
		super(String.valueOf(id), err);
	}
  }
  public class BidTooLow extends BiddingSoftExceptions {
		private static final String throwableMsg = "bid amount less or equal to ";
		private static final int id = 2;
		
		public BidTooLow(double newBidAmount, double highestBidAmount, double biddingIncrement) {
			super(id, new Throwable(String.format(throwableMsg + "current highest bid + biddingIncrement: %.2f <= %.2f + %.2f",newBidAmount, highestBidAmount, biddingIncrement)));
		}
		public BidTooLow(double amount) {
			super(id, new Throwable(String.format(throwableMsg + "startingPrice=%.2f",amount)));
		}

		public BidTooLow(double bidAmount, double biddingIncrement) {
			super(id, new Throwable(String.format(throwableMsg + "biddingIncrement: %.2f <= %.2f",bidAmount, biddingIncrement)));
		}	

	}

  
  public class AutobidNotSet extends BiddingSoftExceptions {
	  private static final String throwableMsg = "this lot has no active autobid in place at the moment";
	  private static final int id = 3;
	  
	  public AutobidNotSet() {
		  super(id, new Throwable(throwableMsg));
	  }
	  
  }
  public class BidderOwnsLot extends BiddingSoftExceptions {
	  private static final String throwableMsg = "owner of a lot can not bid on it";
	  private static final int id = 4;
	  
	  public BidderOwnsLot() {
		  super(id, new Throwable(throwableMsg));
	  }
	  
  }
  public class AlreadyLeadingBidder extends BiddingSoftExceptions {
	  private static final String throwableMsg = "bidder already in control of lot";
	  private static final int id = 5;
	  
	  public AlreadyLeadingBidder() {
		  super(id, new Throwable(throwableMsg));
	  }
	  
  }
  
}
