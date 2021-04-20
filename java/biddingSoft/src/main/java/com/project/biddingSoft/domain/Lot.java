/**
 * 
 */
package com.project.biddingSoft.domain;

import java.io.Serializable;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.annotations.VisibleForTesting;
import com.project.biddingSoft.dao.ILotRepo;

import com.project.biddingSoft.service.ExceptionsCreateor;
import com.project.biddingSoft.service.ExceptionsCreateor.BiddingSoftExceptions;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author nuchem
 *
 */
@Entity
@Component
@DiscriminatorValue("Lot")
@PrimaryKeyJoinColumn(name = "id")
@ToString
public class Lot extends Storable   {//implements IStorable
	
	
	@Autowired
	@Transient
	private static ExceptionsCreateor bidSoftExcepFactory;

	@Autowired
	@Qualifier("BiddingSoftExceptionsFactory")
	public void setBidSoftExcepFactory(ExceptionsCreateor bidSoftExcepFactory) {
		Lot.bidSoftExcepFactory = bidSoftExcepFactory;
	}

//	@Transient
//	private static final  double ONEINCR = 5.0;
	// Instance variables
//	@JsonProperty("id")
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private Long id;
	

	// @ElementCollection(targetClass=Lot.class)
	
	@JsonManagedReference(value="bidOnLot")
	@JsonProperty("bidList")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lot", // , orphanRemoval = truevariable in bid class - that links bid to a lot
			cascade = CascadeType.ALL)
	private List<Bid> bidList;
	@ToString.Exclude
	@JsonBackReference(value="lotOnUser")
	@ManyToOne( fetch = FetchType.LAZY)//cascade = CascadeType.REFRESH,
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false) // 
	@JsonProperty("user")
	private User user;
	
	

	private double highestBid;
//	 private void setHighestBid(Bid highestBid) {
//		this.highestBid = highestBid;
//	}
//	public void remove(Bid bid) {
//		this.bidList = new ArrayList<Bid>(	bidList.stream().filter(b -> !b.equals(bid)).collect(Collectors.toList() ));
//	}
	@Value("${Lot.title}")
	 @JsonProperty("title")
	public String title;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	@JsonProperty("description")
	@Value("${Lot.description}")
	@Column(name = "description")
	private String description;
	@Transient
	@Value("${Lot.timeZone}")
	@JsonProperty("ZONE")
	private ZoneId ZONE;
	@Value("${Lot.biddingIncrement}")
	@JsonProperty("biddingIncrement")
	private double biddingIncrement;
	public void setBiddingIncrement(double biddingIncrement) {
		this.biddingIncrement = biddingIncrement;
	}
	@JsonProperty("reservePrice")
	private double reservePrice = 0.0;
	public double getReservePrice() {
		return reservePrice;
	}
	public void setReservePrice(double reservePrice) {
		this.reservePrice = reservePrice;
	}

	@JsonProperty("startingPrice")
	@Value("${Lot.startingPrice}")
	private double startingPrice;
	public double getStartingPrice() {
		return startingPrice;
	}
	public void setStartingPrice(double startingPrice) {
		this.startingPrice = startingPrice;
	}

	@JsonProperty("startTime")
	private Instant startTime = Instant.now();
	@JsonProperty("endTime")
	private Instant endTime = Instant.now().plus(Duration.ofDays(1));
//	 @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
//	 @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY) //eager, as otherwise jackson has an issue
//	 @JsonBackReference(value="leadingLotsOnBidder")
//	 @JoinColumn(name = "leadingBidder_userId", referencedColumnName = "id", nullable = true)//, nullable = true, updatable = true, insertable = true interesting question - if a user is deleted, all their dependencies are deleted including bids that are currently leading on other lots
//	  @JsonProperty(value = "leadingBidder")
//	 private User leadingBidder;
	 @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
	 @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY,  orphanRemoval = true) //eager, as otherwise jackson has an issue

	 @JoinColumn(name = "leadingBidder_userId", referencedColumnName = "id")//, nullable = true, updatable = true, insertable = true interesting question - if a user is deleted, all their dependencies are deleted including bids that are currently leading on other lots
	  @JsonProperty("leadingBidder")
	private User leadingBidder;

	 @ToString.Exclude
 	@OneToOne(cascade = CascadeType.ALL,  fetch = FetchType.LAZY, orphanRemoval = true) // , orphanRemoval=true
	@JoinColumn(name = "autoBid_id")
	  @JsonProperty("pendingAutoBid")
	private Bid pendingAutoBid;

	 @JsonIgnore //otherwise jackson has an issue
	public Bid getPendingAutoBid() {
		if (pendingAutoBid != null )
			return pendingAutoBid;
		else
			throw bidSoftExcepFactory.new AutobidNotSet();
	}
	public User getLeadingBidder() {
		return leadingBidder;
	}
	public Instant getEndTime() {
		return endTime;
	}
	public double getBiddingIncrement() {
		return biddingIncrement;
	}

	public Bid getBid(Bid bid) {
		return bidList.stream().filter(b -> b.equals(bid)).findFirst().orElseThrow(NoSuchElementException::new);
	}

	public Bid getBid(int id) {
		return bidList.get(id);
	}

	// @Basic
	@Value("#{T(java.time.Duration).parse('${Lot.triggerDuration}')}")
	private Duration triggerDuration;

	@Value("#{T(java.time.Duration).parse('${Lot.autoExtendDuration}')}")
	private Duration autoExtendDuration;
	private Instant extendedEndtime = endTime;
//	@OneToOne(cascade = CascadeType.ALL, 
//			fetch = FetchType.LAZY)
//	@JoinColumn(name="highstBid_id", nullable=false)
// 	private Bid highestBid; 

	// Static variables
	public static  String ANSI_RED = "\u001B[31m";
	public static  String ANSI_RESET = "\u001B[0m";
	private static  Logger logger = LoggerFactory.getLogger(Lot.class);
	@Transient
	private Clock clock;

	// Constructors
	@JsonCreator
	public Lot() {
		this.bidList = new ArrayList<Bid>();// remove
	}

	public Lot(LotBuilder lotBuilder) {
		this.user = lotBuilder.user;
		this.bidList = new ArrayList<Bid>(lotBuilder.bidList);
		this.title = lotBuilder.title;
		this.description = lotBuilder.description;
		this.startingPrice = lotBuilder.startingPrice;
		this.reservePrice = lotBuilder.reservePrice;
		this.biddingIncrement = lotBuilder.biddingIncrement;
		this.triggerDuration = lotBuilder.triggerDuration;
		this.autoExtendDuration = lotBuilder.autoExtendDuration;
		this.ZONE = lotBuilder.ZONE;
		clock = Clock.system(this.ZONE);
		// this.highestBid = lotBuilder.highestBid;
		this.id = lotBuilder.id;
		this.endTime = lotBuilder.endTime;
		this.extendedEndtime = lotBuilder.extendedEndtime;
		user.addLotToList(this);
	}

	@PostConstruct // is only called after defautl constructor
	public void postConstruct() {
		clock = Clock.system(ZONE);

	}



	public Optional<Lot> placeBid(Bid bid) {
		bid.setAmount(((int) bid.getAmount() / biddingIncrement) * biddingIncrement);
		try {
		 addBid(bid);
			
			if (pendingAutoBid == null) {
				highestBid += biddingIncrement;
				isOverHigestBid(bid);
				leadingBidder = bid.getBidder();
			} else {
				if (bid.getAmount() < pendingAutoBid.getAmount())
					highestBid = bid.getAmount() + biddingIncrement;
				else if (bid.getAmount() == pendingAutoBid.getAmount()) {
					highestBid = pendingAutoBid.getAmount();
					pendingAutoBid = null;
				} else {
					highestBid = pendingAutoBid.getAmount() + biddingIncrement;
					leadingBidder = bid.getBidder();
					if (!isOverHigestBid(bid))
						pendingAutoBid = null;
				}
			}
		
		}
		catch (ExceptionsCreateor.LotHasEndedException e) {
			throw e;
		}
		catch (ExceptionsCreateor.BidTooLow e) {
			throw e;
		}		
		catch (Exception e) {
			return Optional.empty();
		}
		
		return Optional.of(this) ;

	}
	private void addBid(Bid bid) {
		Instant now = Instant.now(clock);
		Objects.requireNonNull(bid);
		if (!hasLotExpired(now) && bidHighEnough(bid))
			 bidList.add(bid);
		else
			throw bidSoftExcepFactory.new LotHasEndedException();
		
		if (isInTriggerPeriod(now))
			endTime = extendedEndtime = extendedEndtime.plus(autoExtendDuration);
		
	}

	private boolean isOverHigestBid(Bid bid) {
		if (bid.getAmount() > getHighestBid()) {
			pendingAutoBid = bid;
			return true;
		}
		return false;
	}




	private boolean isInTriggerPeriod(Instant now) {
		return now.compareTo(extendedEndtime) < 0 && now.compareTo(extendedEndtime.minus(triggerDuration)) >= 0;
	}

	private boolean hasLotExpired(Instant now) {
		return now.compareTo(extendedEndtime) >= 0;
	}

	private boolean bidHighEnough(Bid bid) {
		boolean isHihger = false;
		if (getHighestBid() > 0.0)// there are bid/s already placed
			isHihger = bid.getAmount() >= getHighestBid() + biddingIncrement;
		else {// no bids placed yet
				// there's startingPrice and bid is less OR there's isn't startingPrice and bid
				// is lower than 1 increment
			if (startingPrice > 0.0 && bid.getAmount() < (startingPrice))
				throw bidSoftExcepFactory.new BidTooLow(startingPrice);
			else if (startingPrice == 0.0 && bid.getAmount() < (startingPrice + biddingIncrement))
				throw bidSoftExcepFactory.new BidTooLow(startingPrice + biddingIncrement);
			else
				isHihger = true;

		}
		if (!isHihger)
			throw bidSoftExcepFactory.new BidTooLow(bid.getAmount(), getHighestBid() + biddingIncrement);
		return true;

	}

//	public Lot(List<Bid> bidList, User user) {
//		
//		this.bidList = bidList;
//		this.user = user;
//	}
//	
//	public Lot(User user) {
//		this(new ArrayList<Bid>(), user);
//	}
//	

	public double getHighestBid() {
		return highestBid;
//			 bidList.stream().max(Comparator.comparingDouble(Bid::getAmount)).orElseThrow(NoSuchElementException::new);
	}

//	//@Override
//	public void setId(Long id) {
//		super.id = id;
//	}
//	//@Override
//	public Long getId() {
//		return super.id;
//	}

	public void setUser(User user) {//remove
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}


	public boolean contains(Bid bid) {
		return bidList.contains(bid);
	}
	// @JsonProperty(value = "user")




	public static class LotBuilder {


		private Long id;
		private ZoneId ZONE;
		private User user;
		private String title;
		private String description;
		private double startingPrice;
		private double biddingIncrement;
		private double reservePrice;
		private Duration triggerDuration;
		private Duration autoExtendDuration;
		private Instant extendedEndtime;
		private List<Bid> bidList;
		private Instant endTime;

		public LotBuilder description(String description) {
			this.description = description;
			return this;
		}

		public LotBuilder timeZone(ZoneId ZONE) {
			this.ZONE = ZONE;
			return this;
		}


		public LotBuilder(ArrayList<Bid> bidList) {
			this.bidList = new ArrayList<Bid>(bidList);
		}

		public LotBuilder reservePrice(double reservePrice) {
			this.reservePrice = reservePrice;
			return this;
		}

		public LotBuilder endTime(Instant endTime) {
			this.endTime = endTime;
			this.extendedEndtime = this.endTime;
			return this;
		}

		public LotBuilder triggerDuration(Duration triggerDuration) {
			this.triggerDuration = triggerDuration;
			return this;
		}

		public LotBuilder autoExtendDuration(Duration autoExtendDuration) {
			this.autoExtendDuration = autoExtendDuration;
			return this;
		}

		public Lot build() {
			return new Lot(this);
		}

		public LotBuilder user(User user) {
			this.user = user;
			return this;
		}

		public LotBuilder title(String title) {
			this.title = title;
			return this;
		}

		public LotBuilder startingPrice(double startingPrice) {
			this.startingPrice = startingPrice;
			return this;
		}

		public LotBuilder biddingIncrement(double biddingIncrement) {
			this.biddingIncrement = biddingIncrement;
			return this;
		}

	}
	


	

}
