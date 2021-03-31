/**
 * 
 */
package com.project.biddingSoft.domain;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.Basic;
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
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.project.biddingSoft.dao.ILotRepo;
import com.project.biddingSoft.dao.IStorable;
 
/**
 * @author nuchem
 *
 */
@Entity
@Component
@Inheritance(strategy = InheritanceType.JOINED)
public class Lot implements IStorable {
	

	//Instance variables
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	//  @ElementCollection(targetClass=Lot.class)
	 @OneToMany(
			 fetch = FetchType.LAZY,
			    mappedBy = "lot",//variable in bid class - that links bid to a lot
			     cascade = CascadeType.ALL,
			    orphanRemoval = true
			)
	private  List<Bid> bidList ;
	 @Value("${Lot.title}") 
	private  String title;
	@Value("${Lot.description}") 
	@Column(name = "description")
	private String description;
	@Value("${Lot.startingBid}")
	private  double startingBid = 0.0;
	@ManyToOne(cascade = CascadeType.PERSIST, 
			fetch = FetchType.LAZY)
	@JoinColumn(name="user_id", nullable = false )
	private  User user;
	@Value("${Lot.biddingIncrement}")
	private  double biddingIncrement;
	private  double reservePrice = 0.0;
	@Basic
	private  Instant startTime = Instant.now();
	@Basic
	private Instant endTime = Instant.now().plus(Duration.ofDays(1));
	@Basic
	@Value("#{T(java.time.Duration).parse('${spring.redis.triggerDuration}')}")
	private  Duration triggerDuration;
	public Duration getTriggerDuration() {
		return triggerDuration;
	}
	
	  @Value("#{T(java.time.Duration).parse('${spring.redis.autoExtendDuration}')}")
	private  Duration autoExtendDuration  ;
	

	public Duration getAutoExtendDuration() {
		return autoExtendDuration;
	}


	private Instant extendedEndtime = endTime;
//	@OneToOne(cascade = CascadeType.ALL, 
//			fetch = FetchType.LAZY)
//	@JoinColumn(name="highstBid_id", nullable=false)
// 	private Bid highestBid; 
	
	//Static variables
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_RESET = "\u001B[0m";
	private static final Logger logger = LoggerFactory.getLogger(Lot.class);
	
	//Constructors
	//@JsonCreator
	public Lot() {  

//		  this.bidList =  new ArrayList<Bid>();
//		   addBid(this, new Bid(this,this.startingBid));

		   this.bidList =  new ArrayList<Bid>(Arrays.asList(new Bid(this,this.startingBid) ));
		   //user.addLotToList(this);
		
	}
//	@PostConstruct
//	private void postConstruct() {
//	setAutoExtendDuration();
//	}

	public Lot(LotBuilder lotBuilder) {
		this.user = lotBuilder.user;
		this.bidList =  new ArrayList<Bid>(lotBuilder.bidList) ;
		this.title = lotBuilder.title;
		this.description =lotBuilder.description;
		this.startingBid = lotBuilder.startingBid;
		this.reservePrice = lotBuilder.reservePrice;
		this.biddingIncrement = lotBuilder.biddingIncrement;
		this.triggerDuration = lotBuilder.triggerDuration;
		this.autoExtendDuration = lotBuilder.autoExtendDuration;
		
		this.id = lotBuilder.id;		
		this.endTime = lotBuilder.endTime;
		 
		this.extendedEndtime = lotBuilder.extendedEndtime;
		
		//this.bidList.add(new Bid(this,this.startingBid));
		  addBid(this, new Bid(this,this.startingBid));
		  user.addLotToList(this);
	}
	
	public static boolean addBid(Lot lot, Bid bid) {
	// logger.info(ANSI_RED + bid + ANSI_RESET);
		return lot.getBidList().add(bid);//handle exception
		 
		 
	}
	
	
	public boolean placeBid(Bid bid) throws Exception {
		boolean success;
		boolean beforeExtEndTime; 
		Instant now = Instant.now(Clock.systemDefaultZone()) ;
		try {
		Objects.requireNonNull(bid); 
		
		beforeExtEndTime = now.compareTo(extendedEndtime) < 1 ; 
	   success = beforeExtEndTime && getHighestBid().getAmount() <= bid.getAmount() ? bidList.add(bid) : false ;
		}
		catch(Exception e) {
			throw e;
		}
		
		extendedEndtime =  now.compareTo(extendedEndtime.plus(autoExtendDuration)) <1 ? extendedEndtime.plus(autoExtendDuration) : extendedEndtime;
		endTime = extendedEndtime;
		return success; 
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
//	public Lot() {
//		this(new ArrayList<Bid>(), new User("defualt"));
//	}

	public double getStartingBid() {
		return startingBid;
	}
	
	public Instant getExtendedEndTime() {
		return extendedEndtime;
	}

	public Bid getHighestBid() {
		return  bidList.stream().max(Comparator.comparing(Bid::getAmount)).orElseThrow(NoSuchElementException::new);
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;		
	}
 
		public void setUser(User user) {
			this.user = user;
		}
	public Instant getStartTime() {
		return startTime;
	}

	public Instant getEndTime() {
		return endTime;
	}
 
	//@JsonProperty(value = "bidList")
	public List<Bid> getBidList() {
		return bidList;
	} 
	//@JsonProperty(value = "user")
	public User getUser() {
		return user;
	}

	public String getDescription() {
		return description;
	}
	
	@Override
	public String toString() {
		return "Lot [id=" + id + ", user=" + user + ", bidList=" + bidList + ", title=" + title + ", startingBid="
				+ startingBid + ", biddingIncrement=" + biddingIncrement + ", reservePrice=" + reservePrice
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", triggerDuration=" + triggerDuration
				+ ", autoExtendDuration=" + autoExtendDuration + ", extendedEndtime=" + extendedEndtime
				+ ", description=" + description + "]";
	}

	@Override
	public boolean saveToRepo() throws IllegalArgumentException {
		try {
			iLotRepo.save(this);
		} catch (IllegalArgumentException e) {
			throw e;
		}
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
		try {
			lot = iLotRepo.findById(this.id);
		} catch (IllegalArgumentException e) {
			throw e;
		}
		return lot;
	}

	@Override
	public void delete() throws IllegalArgumentException {
		try {
			iLotRepo.deleteById(this.id);
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

	
	
	public static class LotBuilder {
//		public Instant getExtendedEndtime() {
//			return extendedEndtime;
//		}
//
//		public void setExtendedEndtime(Instant extendedEndtime) {
//			this.extendedEndtime = extendedEndtime;
//		}

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long id;
		
		private User user;
		private String title;
		private String description;
		private double startingBid;
		private double biddingIncrement;
		private double reservePrice;
		private Duration triggerDuration;
		private Duration autoExtendDuration;
		private Instant extendedEndtime;
		private  List<Bid> bidList ;
		private Instant endTime;

		public LotBuilder description(String description) {
			this.description = description;
			return this;
		}
		
		public Instant getEndTime() {
			return endTime;
		}

		public LotBuilder(ArrayList<Bid> bidList) {
			this.bidList =  new ArrayList<Bid>(bidList) ;
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

		public LotBuilder startingBid(double startingBid) {
			this.startingBid = startingBid;
			return this;
		}

		public LotBuilder biddingIncrement(double biddingIncrement) {
			this.biddingIncrement = biddingIncrement;
			return this;
		}

	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((autoExtendDuration == null) ? 0 : autoExtendDuration.hashCode());
		result = prime * result + ((bidList == null) ? 0 : bidList.hashCode());
		long temp;
		temp = Double.doubleToLongBits(biddingIncrement);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((extendedEndtime == null) ? 0 : extendedEndtime.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		temp = Double.doubleToLongBits(reservePrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		temp = Double.doubleToLongBits(startingBid);
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (Double.doubleToLongBits(reservePrice) != Double.doubleToLongBits(other.reservePrice))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (Double.doubleToLongBits(startingBid) != Double.doubleToLongBits(other.startingBid))
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
