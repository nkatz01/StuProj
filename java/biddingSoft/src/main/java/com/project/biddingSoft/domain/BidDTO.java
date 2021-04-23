package com.project.biddingSoft.domain;



import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BidDTO extends StorableDTO{
	private User bidder;
	private Lot lot;
	private Double amount; 

}
