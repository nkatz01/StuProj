/**
 * 
 */
package com.project.biddingSoft.domain;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.biddingSoft.service.ExceptionsCreateor;
import com.project.biddingSoft.service.ExceptionsCreateor.AutobidNotSet;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author nuchem
 *
 */
@Entity
@Component
@DiscriminatorValue("Lot")
@PrimaryKeyJoinColumn(name = "id")
@ToString(callSuper = true, includeFieldNames = true)
@Setter
@Getter
public class Lot extends Storable {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((ZONE == null) ? 0 : ZONE.hashCode());
		result = prime * result + ((autoExtendDuration == null) ? 0 : autoExtendDuration.hashCode());
		result = prime * result + ((bidSet == null) ? 0 : bidSet.hashCode());
		long temp;
		temp = Double.doubleToLongBits(biddingIncrement);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((clock == null) ? 0 : clock.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((extendedEndtime == null) ? 0 : extendedEndtime.hashCode());
		temp = Double.doubleToLongBits(highestBid);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((leadingBidder == null) ? 0 : leadingBidder.hashCode());
		result = prime * result + ((pendingAutoBid == null) ? 0 : pendingAutoBid.hashCode());
		temp = Double.doubleToLongBits(reservePrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		temp = Double.doubleToLongBits(startingPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((triggerDuration == null) ? 0 : triggerDuration.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
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
		if (bidSet == null) {
			if (other.bidSet != null)
				return false;
		} else if (!bidSet.equals(other.bidSet))
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
		return true;
	}

	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	@Autowired
	@Transient
	private static ExceptionsCreateor bidSoftExcepFactory;

	@Autowired
	@Qualifier("BiddingSoftExceptionsFactory")
	public void setBidSoftExcepFactory(ExceptionsCreateor bidSoftExcepFactory) {
		Lot.bidSoftExcepFactory = bidSoftExcepFactory;
	}

	@JsonManagedReference(value = "bidOnLot")
	@JsonProperty("bidSet")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lot", cascade = CascadeType.ALL)
	private Set<Bid> bidSet = Collections.synchronizedSet(new HashSet<Bid>());
	
	@ToString.Exclude
	@JsonBackReference(value = "lotOnUser")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
	@JsonProperty("user")
	private User user;
	
	@Setter(AccessLevel.NONE)
	@Column(nullable = true)
	private double highestBid;
	@Value("${Lot.title}")
	@JsonProperty("title")
	public String title;

	@JsonProperty("description")
	@Value("${Lot.description}")
	private String description;
	@Transient
	@Value("${Lot.timeZone}")
	@JsonProperty("ZONE")
	private ZoneId ZONE;
	@Value("${Lot.biddingIncrement}")
	@JsonProperty("biddingIncrement")
	private double biddingIncrement;

	@JsonProperty("reservePrice")
	@Column(nullable = true)
	private double reservePrice = 0.0;

	@JsonProperty("startingPrice")
	@Value("${Lot.startingPrice}")
	private double startingPrice;

	@JsonProperty("startTime")
	@Column(nullable = false)
	private Instant startTime = Instant.now();
	@JsonProperty("endTime")
	@Column(nullable = false)
	private Instant endTime = Instant.now().plus(Duration.ofDays(1));

	@JsonIgnoreProperties(value = { "applications", "hibernateLazyInitializer" })
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "leadingBidder_userId", referencedColumnName = "id")
	@JsonProperty("leadingBidder")
	@Setter(AccessLevel.NONE)
	private User leadingBidder;

	@ToString.Exclude
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "autoBid_id")
	@JsonProperty("pendingAutoBid")
	@Setter(AccessLevel.NONE)
	private Bid pendingAutoBid;

	@Value("#{T(java.time.Duration).parse('${Lot.triggerDuration}')}")
	private Duration triggerDuration;
	@Value("#{T(java.time.Duration).parse('${Lot.autoExtendDuration}')}")
	private Duration autoExtendDuration;
	@Setter(AccessLevel.NONE)
	private Instant extendedEndtime = endTime;

	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	@Transient
	private Clock clock;

	// Constructors
	@JsonCreator
	public Lot() {

	}

	public Lot(LotBuilder lotBuilder) {
		this.user = lotBuilder.user;
		this.bidSet = lotBuilder.bidSet;
		this.title = lotBuilder.title;
		this.description = lotBuilder.description;
		this.startingPrice = lotBuilder.startingPrice;
		this.reservePrice = lotBuilder.reservePrice;
		this.biddingIncrement = lotBuilder.biddingIncrement;
		this.triggerDuration = lotBuilder.triggerDuration;
		this.autoExtendDuration = lotBuilder.autoExtendDuration;
		this.ZONE = lotBuilder.ZONE;
		clock = Clock.system(this.ZONE);
		this.endTime = lotBuilder.endTime;
		this.extendedEndtime = lotBuilder.extendedEndtime;
		user.addLotToSet(this);
	}

	@PostConstruct // is only called after default constructor
	public void postConstruct() {
		clock = Clock.system(ZONE);

	}

	public Optional<Lot> placeBid(Bid bid) {
		Objects.requireNonNull(bid);
		if (bid.getBidder().equals(this.user))
			throw bidSoftExcepFactory.new BidderOwnsLot();
		bid.setAmount(((int) bid.getAmount() / biddingIncrement) * biddingIncrement);

		try {
			addBid(bid);

			if (pendingAutoBid == null) {
				highestBid += biddingIncrement;
				 	if (bid.getAmount() > highestBid)//if over new highest bid
				 		pendingAutoBid = bid;
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
					if (bid.getAmount() <= highestBid)
						pendingAutoBid = null;
					else pendingAutoBid = bid;
						
				}
			}

		} catch (ExceptionsCreateor.LotHasEndedException e) {
			throw e;
		} catch (ExceptionsCreateor.BidTooLow e) {
			throw e;
		} catch (Exception e) {
			return Optional.empty();
		}

		return Optional.of(this);

	}

	private void addBid(Bid bid) {
		Instant now = Instant.now(clock);
		
		if (!hasLotExpired(now) && isBidHighEnough(bid))
			bidSet.add(bid);
		else
			throw bidSoftExcepFactory.new LotHasEndedException();

		if (isInTriggerPeriod(now))
			extendedEndtime = extendedEndtime.plus(autoExtendDuration);

	}



	private boolean isInTriggerPeriod(Instant now) {
		return now.compareTo(extendedEndtime) < 0 && now.compareTo(extendedEndtime.minus(triggerDuration)) >= 0;
	}

	private boolean hasLotExpired(Instant now) {
		return now.compareTo(extendedEndtime) >= 0;
	}

	private boolean isBidHighEnough(Bid bid) {
		boolean isHihger = false;
		if (highestBid > 0.0)// there are bid/s already placed
			isHihger = bid.getAmount() >= highestBid + biddingIncrement;
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
			throw bidSoftExcepFactory.new BidTooLow(bid.getAmount(), highestBid + biddingIncrement);
		return true;

	}

	@JsonIgnore
	public Bid getPendingAutoBid() {
		if (pendingAutoBid != null)
			return pendingAutoBid;
		else
			throw bidSoftExcepFactory.new AutobidNotSet();
	}

	public Bid getBid(int index) {
		Iterator<Bid> iter = bidSet.iterator();
		Bid bid = null;
		for (int i = 0; iter.hasNext(); i++) {
			bid = iter.next();
			if (i == index)
				return bid;
		}
		throw new IndexOutOfBoundsException();
	}

	public boolean contains(Bid bid) {
		return bidSet.contains(bid);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public final static class LotBuilder {

		
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
		private Set<Bid> bidSet;
		private Instant endTime;

		public LotBuilder description(String description) {
			this.description = description;
			return this;
		}

		public LotBuilder timeZone(ZoneId ZONE) {
			this.ZONE = ZONE;
			return this;
		}

		public LotBuilder(Set<Bid> bidSet) {
			this.bidSet = Collections.synchronizedSet(new HashSet<Bid>(bidSet));
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
