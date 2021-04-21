package com.project.biddingSoft.domain;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.biddingSoft.service.ExceptionsCreateor;
import com.project.biddingSoft.service.ExceptionsCreateor.AutobidNotSet;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LotDTO extends StorableDTO{
	
	private List<Bid> bidList;
	private User user;
	private Double highestBid;
	private String title;
	private String description;
	private ZoneId ZONE;
	private Double biddingIncrement;
	private Double reservePrice ;
	private Double startingPrice;
	private Instant startTime;
	private Instant endTime ;
	private User leadingBidder;
	private Bid pendingAutoBid;
	private Duration triggerDuration;
	private Duration autoExtendDuration;
	private Instant extendedEndtime = endTime;
	private Clock clock;
	
}
