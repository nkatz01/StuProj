package com.project.biddingSoft.domain;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LotDTO extends StorableDTO{
	
	private Set<Bid> bidSet;
	private User user;
	private Double highestBid;
	private String title;
	private String description;
	private ZoneId ZONE;
	private Double biddingIncrement;
	private Double reservePrice ;
	private Double startingPrice;
	private Instant endTime ;
	private Duration triggerDuration;
	private Duration autoExtendDuration;
	
}
