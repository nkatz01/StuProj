package com.project.biddingSoft.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


public class BidDTO extends StorableDTO{
	
	public User getBidder() {
		return bidder;
	}
	public void setBidder(User bidder) {
		this.bidder = bidder;
	}
	public Lot getLot() {
		return lot;
	}
	public void setLot(Lot lot) {
		this.lot = lot;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	private User bidder;
	private Lot lot;
	private Double amount; 

}
