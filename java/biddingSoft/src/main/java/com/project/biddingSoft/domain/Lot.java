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
import com.project.biddingSoft.dao.IStorable;
import com.project.biddingSoft.service.ExceptionsCreateor;
import com.project.biddingSoft.service.ExceptionsCreateor.BiddingSoftExceptions;

/**
 * @author nuchem
 *
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")

@Entity
@Component
//@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("Lot")
@PrimaryKeyJoinColumn(name = "id")

public class Lot extends Storable implements IStorable  {
	
	
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
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lot", // variable in bid class - that links bid to a lot
			cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Bid> bidList;
	
	@JsonBackReference(value="lotOnUser")
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
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
	private String title;
	@JsonProperty("description")
	@Value("${Lot.description}")
	@Column(name = "description")
	private String description;
	@Transient
	@Value("${Lot.timeZone}")
	@JsonProperty("ZONE")
	private ZoneId ZONE;
	@Value("${Lot.biddingIncrement}")
	@JsonProperty("reservePrice")
	private double biddingIncrement;
	private double reservePrice = 0.0;
	@JsonProperty("startingPrice")
	@Value("${Lot.startingPrice}")
	private double startingPrice;
	@JsonProperty("startTime")
	private Instant startTime = Instant.now();
	@JsonProperty("endTime")
	private Instant endTime = Instant.now().plus(Duration.ofDays(1));
	 @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
	 @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY) //eager, as otherwise jackson has an issue
	 @JsonBackReference(value="leadingLotsOnBidder")
	 @JoinColumn(name = "leadingBidder_userId", referencedColumnName = "id", nullable = true)//, nullable = true, updatable = true, insertable = true interesting question - if a user is deleted, all their dependencies are deleted including bids that are currently leading on other lots
	  @JsonProperty(value = "leadingBidder")
	 private User leadingBidder;
//	public void setLeadingBidder(User leadingBidder) {
//		this.leadingBidder = leadingBidder;
//	}
 	@OneToOne(cascade = CascadeType.ALL,  fetch = FetchType.LAZY, orphanRemoval = true) // , orphanRemoval=true
	@JoinColumn(name = "autoBid_id")
	  @JsonProperty("pendingAutoBid")
	private Bid pendingAutoBid;

	 @JsonIgnore //otherwise jackson has an issue
	public Bid getPendingAutoBid() {
		if (pendingAutoBid != null)
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
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_RESET = "\u001B[0m";
	private static final Logger logger = LoggerFactory.getLogger(Lot.class);

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

//	public  static Optional<Lot> addBid(Lot lot, Bid bid)
//			throws UnsupportedOperationException, ClassCastException, IllegalArgumentException, NullPointerException {
//		
//		boolean succeeded = lot.placeBid(bid);// handle exception
//		 if (succeeded) {
//			 if (bid.getAmount()>=lot.getHighestBid() + lot.biddingIncrement)
//				 lot.pendingAutoBid = bid;
//			 lot.highestBid = lot.highestBid + lot.biddingIncrement;
//			 lot.bidList.add(bid);
//			 lot.leadingBidder = bid.getBidder();
//			
//		 }
//		return succeeded ? Optional.of(lot)  : Optional.empty();
//
//	}

	public Optional<Lot> placeBid(Bid bid) {
		bid.setAmount(((int) bid.getAmount() / biddingIncrement) * biddingIncrement);
		boolean succeeded = addBid(bid);// handle exception
		if (succeeded) {
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

		return succeeded ? Optional.of(this) : Optional.empty();

	}

	private boolean isOverHigestBid(Bid bid) {
		if (bid.getAmount() > getHighestBid()) {
			pendingAutoBid = bid;
			return true;
		}
		return false;
	}

//	public boolean placeBid(Bid bid) {
//		return placeBid(bid, Clock.system(ZONE));
//	}
	@Transient
	private Clock clock;

	private boolean addBid(Bid bid) {
		boolean success;
		Instant now = Instant.now(clock);
		Objects.requireNonNull(bid);
		if (!hasLotExpired(now) && bidHighEnough(bid))
			success = bidList.add(bid);
		else
			throw bidSoftExcepFactory.new LotHasEndedException();

		if (isInTriggerPeriod(now))
			endTime = extendedEndtime = extendedEndtime.plus(autoExtendDuration);
		return success;
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
	@Override
	public String toString() {
		return "Lot [id=" + id + ", bidList=" + bidList + ", highestBid=" + highestBid + ", title=" + title
				+ ", description=" + description + ", ZONE=" + ZONE + ", user=" + user + ", biddingIncrement="
				+ biddingIncrement + ", reservePrice=" + reservePrice + ", startingPrice=" + startingPrice
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", leadingBidder=" + leadingBidder
				+ ", pendingAutoBid=" + pendingAutoBid + ", triggerDuration=" + triggerDuration
				+ ", autoExtendDuration=" + autoExtendDuration + ", extendedEndtime=" + extendedEndtime + ", clock="
				+ clock + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ZONE == null) ? 0 : ZONE.hashCode());
		result = prime * result + ((autoExtendDuration == null) ? 0 : autoExtendDuration.hashCode());
		result = prime * result + ((bidList == null) ? 0 : bidList.hashCode());
		long temp;
		temp = Double.doubleToLongBits(biddingIncrement);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((clock == null) ? 0 : clock.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((extendedEndtime == null) ? 0 : extendedEndtime.hashCode());
		temp = Double.doubleToLongBits(highestBid);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((leadingBidder == null) ? 0 : leadingBidder.hashCode());
		result = prime * result + ((pendingAutoBid == null) ? 0 : pendingAutoBid.hashCode());
		temp = Double.doubleToLongBits(reservePrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		temp = Double.doubleToLongBits(startingPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((triggerDuration == null) ? 0 : triggerDuration.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	} 

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lot other = (Lot) obj;
		if (ZONE == null) {
			if (other.ZONE != null)
				return false;
		} else if (!ZONE.equals(other.ZONE))
			return false;
		if (autoExtendDuration == null) {
			if (other.autoExtendDuration != null)
				return false;
		} else if (!autoExtendDuration.equals(other.autoExtendDuration))
			return false;
		if (bidList == null) {
			if (other.bidList != null)
				return false;
		} else if (!bidList.equals(other.bidList))
			return false;
		if (Double.doubleToLongBits(biddingIncrement) != Double.doubleToLongBits(other.biddingIncrement))
			return false;
		if (clock == null) {
			if (other.clock != null)
				return false;
		} else if (!clock.equals(other.clock))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (extendedEndtime == null) {
			if (other.extendedEndtime != null)
				return false;
		} else if (!extendedEndtime.equals(other.extendedEndtime))
			return false;
		if (Double.doubleToLongBits(highestBid) != Double.doubleToLongBits(other.highestBid))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (leadingBidder == null) {
			if (other.leadingBidder != null)
				return false;
		} else if (!leadingBidder.equals(other.leadingBidder))
			return false;
		if (pendingAutoBid == null) {
			if (other.pendingAutoBid != null)
				return false;
		} else if (!pendingAutoBid.equals(other.pendingAutoBid))
			return false;
		if (Double.doubleToLongBits(reservePrice) != Double.doubleToLongBits(other.reservePrice))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (Double.doubleToLongBits(startingPrice) != Double.doubleToLongBits(other.startingPrice))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (triggerDuration == null) {
			if (other.triggerDuration != null)
				return false;
		} else if (!triggerDuration.equals(other.triggerDuration))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

}
