package com.project.biddingSoft.domain;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BidDTO extends StorableDTO{
	

	private User bidder;
	private Lot lot;
	private Double amount; 

}
