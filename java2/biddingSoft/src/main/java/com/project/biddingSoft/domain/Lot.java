/**
 * 
 */
package com.project.biddingSoft.domain;

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

import javax.annotation.PostConstruct;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.annotations.VisibleForTesting;
import com.project.biddingSoft.dao.ILotRepo;
import com.project.biddingSoft.dao.IStorable;
import com.project.biddingSoft.service.ExceptionsCreateor;
import com.project.biddingSoft.service.ExceptionsCreateor.BiddingSoftExceptions;
/**
 * @author nuchem
 *
 */
@Entity
@Component
@Inheritance(strategy = InheritanceType.JOINED)
//@Transactional
public class Lot implements IStorable {
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
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	// @ElementCollection(targetClass=Lot.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lot", // variable in bid class - that links bid to a lot
			cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Bid> bidList;
//	@Basic
//	@OneToOne(cascade = CascadeType.ALL)//orphanRemoval=true
//	@JoinColumn(name = "highest_bid_id", referencedColumnName = "id")
	private double highestBid;  
//	 private void setHighestBid(Bid highestBid) {
//		this.highestBid = highestBid;
//	}

	@Value("${Lot.title}")
	private String title;
	@Value("${Lot.description}")
	@Column(name = "description")
	private String description;
	@Transient
	@Value("${Lot.timeZone}")
	private ZoneId ZONE;
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false) // , referencedColumnName = "id"
	private User user;
	@Value("${Lot.biddingIncrement}")
	private double biddingIncrement;
	private double reservePrice = 0.0;
	@Value("${Lot.startingPrice}")
	private double startingPrice;
	// @Basic
	private Instant startTime = Instant.now();
	private Instant endTime = Instant.now().plus(Duration.ofDays(1));
	@OneToOne(cascade = CascadeType.ALL)//orphanRemoval=true
	@JoinColumn(name = "leadingBidder_userId", referencedColumnName = "id")
	private User leadingBidder;
	public User getLeadingBidder() {
		return leadingBidder;
	}
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy="lot")//, orphanRemoval=true
	@JoinColumn(name = "autoBid_bid_id")
	private Bid pendingAutoBid; 
	

	public Bid getPendingAutoBid() {
		return pendingAutoBid;
	}

	public Instant getEndTime() {
		return endTime;
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
	// @JsonCreator
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
	
	public   Optional<Lot> addBid(Bid bid)
			throws UnsupportedOperationException, ClassCastException, IllegalArgumentException, NullPointerException {
		
		boolean succeeded = placeBid(bid);// handle exception
		 if (succeeded) {
			 if (bid.getAmount()>=getHighestBid() + biddingIncrement)
				 pendingAutoBid = bid;
			 highestBid = highestBid + biddingIncrement;
			 bidList.add(bid);
			 leadingBidder = bid.getBidder();
			
		 }
		return succeeded ? Optional.of(this)  : Optional.empty();

	}

//	public boolean placeBid(Bid bid) {
//		return placeBid(bid, Clock.system(ZONE));
//	}
	@Transient
	private Clock clock;

	
	private boolean placeBid(Bid bid)
			throws DateTimeException, ArithmeticException, NullPointerException, ExceptionsCreateor.BidTooLow {
		boolean success;
		Instant now = Instant.now(clock);
//		System.out.println(endTime.toString());
//		System.out.println(extendedEndtime.toString());
		Objects.requireNonNull(bid);
		//System.out.println(hasLotExpired(now));
		//if(getBidList().size()<1 && bid.getAmount()<)
			
		if (!hasLotExpired(now) && bidHighEnough(bid))
			success = bidList.add(bid);
		else
			throw bidSoftExcepFactory.new LotHasEndedException();

		if (isInTriggerPeriod(now))
			// System.out.println(endTime.toString());
			endTime = extendedEndtime = extendedEndtime.plus(autoExtendDuration);

//		System.out.println(extendedEndtime.toString());

		return success;
	}
	
	

	public boolean isInTriggerPeriod(Instant now) {
		return now.compareTo(extendedEndtime) < 0 && now.compareTo(extendedEndtime.minus(triggerDuration)) >= 0;
	}

	public boolean hasLotExpired(Instant now) {
		return now.compareTo(extendedEndtime) >= 0;
	}

	public boolean bidHighEnough(Bid bid) throws ExceptionsCreateor.BidTooLow {
		boolean isHihger = false;
		if (getHighestBid() > 0.0)
			isHihger = getHighestBid() + biddingIncrement >= bid.getAmount();
		else {// no bids placed yet
			if (bid.getAmount() < (startingPrice + biddingIncrement))
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

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public void setUser(User user) {
		this.user = user;
	}

	// @JsonProperty(value = "bidList")
//	private List<Bid> getBidList() {
//		return bidList;
//	}
	public boolean contains(Bid bid) {
		return bidList.contains(bid);
	}
	// @JsonProperty(value = "user")

	@Override
	public String toString() {
		return "Lot [id=" + id + ", user=" + user + ", bidList=" + bidList + ", title=" + title + ", startingPrice="
				+ startingPrice + ", biddingIncrement=" + biddingIncrement + ", reservePrice=" + reservePrice
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", triggerDuration=" + triggerDuration
				+ ", autoExtendDuration=" + autoExtendDuration + ", extendedEndtime=" + extendedEndtime
				+ ", description=" + description + "]";
	}

	@Override
	public boolean saveToRepo() throws IllegalArgumentException {
		// Thread.dumpStack();
		iLotRepo.save(this);
		return true;
	}

	@Override
	public Iterable<Lot> findAll() {
		return iLotRepo.findAll();
	}

	@Transient
	@Autowired
	private static ILotRepo iLotRepo;

	@Autowired
	public void setILotRepo(ILotRepo ilotrepo) {
		iLotRepo = ilotrepo;
	}

	@Override
	public Optional<? extends IStorable> find() throws IllegalArgumentException {
		Optional<Lot> lot = null;
		lot = iLotRepo.findById(this.id);
		return lot;
	}

	@Override
	public void delete() throws IllegalArgumentException {
		iLotRepo.deleteById(this.id);
	}

	public static class LotBuilder {

//		@Id
//		@GeneratedValue(strategy = GenerationType.AUTO)
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

		// private Bid highestBid;
		public LotBuilder description(String description) {
			this.description = description;
			return this;
		}

		public LotBuilder timeZone(ZoneId ZONE) {
			this.ZONE = ZONE;
			return this;
		}

//		public LotBuilder highestBid(Bid highestBid) {
//			this.highestBid = highestBid; 
//			return this;
//		}

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
