/**
 * 
 */
package com.project.biddingSoft.domain;
import com.project.biddingSoft.service.UserService;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.project.biddingSoft.dao.ILotRepo;
import com.project.biddingSoft.dao.IStorable;

/**
 * @author nuchem
 *
 */
@Entity
@Component
public class Lot implements IStorable {

	 
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private final Long id;
	@Autowired
	@Qualifier("getMeSimpleUser")
	@OneToOne
 	private final User user;
	@ElementCollection(targetClass=Lot.class)
	private final List<Bid> bidList = new ArrayList<Bid>();
	@Value("${Lot.title}")
	private final String title;
 	private final double startingPrice;
 	@Value("${Lot.biddingIncrement}")
	private final double biddingIncrement; 
	private final double reservePrice; 
	
	private final Instant startTime = Instant.now(); 
	private  Instant endTime = Instant.now().plus(Duration.ofDays(1)); 
	@Value("${Lot.triggerDuration}") 	
	private final Duration triggerDuration;
	@Value("${Lot.autoExtendDuration}")
	private final Duration autoExtendDuration;
	private  Instant extendedEndtime; 
	public Lot(LotBuilder lotBuilder) {
		this(
		lotBuilder.id,
		 lotBuilder.user,
		 lotBuilder.description,
		 lotBuilder.title,
		 lotBuilder.startingPrice,
		 lotBuilder.biddingIncrement,
		lotBuilder.reservePrice,
		 
		lotBuilder.endTime,
		lotBuilder.triggerDuration,
		 lotBuilder.autoExtendDuration,
		 lotBuilder.extendedEndtime);
	}
	@JsonCreator
	public Lot(Long id, @Qualifier("getMeSimpleUser") User user, String title, String description,       
	double startingPrice,       
	double biddingIncrement,    
	double reservePrice,  
	Instant endTime,       
	Duration triggerDuration,   
	Duration autoExtendDuration,
	Instant extendedEndtime   ) {
		this.id =id;
		this.user = user;
		this.title = title;
		this.startingPrice = startingPrice;
		this.biddingIncrement = biddingIncrement;
		this.reservePrice = reservePrice;
		this.endTime = endTime;
		this.triggerDuration = triggerDuration;
		this.autoExtendDuration =autoExtendDuration;
		this.extendedEndtime =extendedEndtime;

	}
 
	@Override
	public String toString() {
		return "Lot [id=" + id + ", user=" + user + ", bidList=" + bidList + ", title=" + title + ", startingPrice="
				+ startingPrice + ", biddingIncrement=" + biddingIncrement + ", reservePrice=" + reservePrice
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", triggerDuration=" + triggerDuration
				+ ", autoExtendDuration=" + autoExtendDuration + ", extendedEndtime=" + extendedEndtime
				+ ", description=" + description + ", username=" + username + "]";
	}
	public Long getId() {
		return id;
	}

	 

//	@JsonIgnore
//	@JsonProperty(value = "bidList")
//	public List<Bid> getBidList() {
//		return bidList;
//	}
//
//	public void setBidList(List<Bid> bidList) {
//		this.bidList = bidList;
//	}
//	@JsonIgnore
//	@JsonProperty(value = "user")
//	public User getUser() {
//		return user;
//	}

//	public void setUser(User user) {
//		this.user = user;
//	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	

//	public Lot() {
//	}

//	@Transient
//	private  List<Bid> bidList; 
//	@Transient
//	private  User user;
	@Column(name = "description")
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "username")
	private String username;

	/**
	 * @param bidList
	 * @param user
	 */
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
	public Optional<? extends IStorable> findById(Long id) throws IllegalArgumentException {
		Optional<Lot> lot = null;
		try {
			lot = iLotRepo.findById(id);
		} catch (IllegalArgumentException e) {
			throw e;
		}
		return lot;
	}

	@Override
	public void deleteById(Long id) throws IllegalArgumentException {
		try {
			iLotRepo.deleteById(id);
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}
	
	
	public static class LotBuilder {
		public Instant getExtendedEndtime() {
			return extendedEndtime;
		}
		public void setExtendedEndtime(Instant extendedEndtime) {
			this.extendedEndtime = extendedEndtime;
		}
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long id;
		private  User user; 
		
		private  String title;
		private  String description;
		private  double startingPrice;
		private  double biddingIncrement; 
		private  double reservePrice; 
		 
		private  Instant endTime; 
		public Instant getEndTime() {
			return endTime;
		}
		public void setEndTime(Instant endTime) {
			this.endTime = endTime;
		}
		private  Duration triggerDuration; 
		private  Duration autoExtendDuration;
		private  Instant extendedEndtime; 
		public  LotBuilder() {
			 
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
		public LotBuilder description(String description) {
			this.description = description;
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
	public Long setId() {
		// TODO Auto-generated method stub
		return null;
	}
}
