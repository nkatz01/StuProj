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


public class LotDTO extends StorableDTO{
	


	public List<Bid> getBidList() {
		return bidList;
	}

	public void setBidList(List<Bid> bidList) {
		this.bidList = bidList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Double getHighestBid() {
		return highestBid;
	}

	public void setHighestBid(Double highestBid) {
		this.highestBid = highestBid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ZoneId getZONE() {
		return ZONE;
	}

	public void setZONE(ZoneId zONE) {
		ZONE = zONE;
	}

	public Double getBiddingIncrement() {
		return biddingIncrement;
	}

	public void setBiddingIncrement(Double biddingIncrement) {
		this.biddingIncrement = biddingIncrement;
	}

	public Double getReservePrice() {
		return reservePrice;
	}

	public void setReservePrice(Double reservePrice) {
		this.reservePrice = reservePrice;
	}

	public Double getStartingPrice() {
		return startingPrice;
	}

	public void setStartingPrice(Double startingPrice) {
		this.startingPrice = startingPrice;
	}

	public Instant getStartTime() {
		return startTime;
	}

	public void setStartTime(Instant startTime) {
		this.startTime = startTime;
	}

	public Instant getEndTime() {
		return endTime;
	}

	public void setEndTime(Instant endTime) {
		this.endTime = endTime;
	}

	public User getLeadingBidder() {
		return leadingBidder;
	}

	public void setLeadingBidder(User leadingBidder) {
		this.leadingBidder = leadingBidder;
	}

	public Bid getPendingAutoBid() {
		return pendingAutoBid;
	}

	public void setPendingAutoBid(Bid pendingAutoBid) {
		this.pendingAutoBid = pendingAutoBid;
	}

	public Duration getTriggerDuration() {
		return triggerDuration;
	}

	public void setTriggerDuration(Duration triggerDuration) {
		this.triggerDuration = triggerDuration;
	}

	public Duration getAutoExtendDuration() {
		return autoExtendDuration;
	}

	public void setAutoExtendDuration(Duration autoExtendDuration) {
		this.autoExtendDuration = autoExtendDuration;
	}

	public Instant getExtendedEndtime() {
		return extendedEndtime;
	}

	public void setExtendedEndtime(Instant extendedEndtime) {
		this.extendedEndtime = extendedEndtime;
	}

	public Clock getClock() {
		return clock;
	}

	public void setClock(Clock clock) {
		this.clock = clock;
	}

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
