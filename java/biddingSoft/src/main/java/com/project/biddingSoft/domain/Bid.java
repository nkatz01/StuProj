/**
 * 
 */
package com.project.biddingSoft.domain;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.project.biddingSoft.dao.IBidRepo;
import com.project.biddingSoft.dao.IStorable;



/**
 * @author nuchem
 *
 */
//@JsonIdentityInfo(
//		  generator = ObjectIdGenerators.PropertyGenerator.class, 
//		  property = "id")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Component
public class Bid implements IStorable{
	 
	@ManyToOne(cascade = CascadeType.PERSIST
			,fetch = FetchType.LAZY
			 )//orphanRemoval=true
	@JoinColumn(name = "bidder_userId", referencedColumnName = "id", nullable=false)
	@JsonProperty(value = "bidder")
	@JsonBackReference(value="bidOnUser")
	//	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private User bidder;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	 
	public Long getId() {
		return id;
	}


	@ManyToOne(
			cascade = CascadeType.PERSIST
			,fetch = FetchType.EAGER//in order for jackson to work
			
		)
	@JsonBackReference(value="bidOnLot")
	@JoinColumn(name = "lot_id", referencedColumnName = "id", nullable=false)
	 @JsonProperty(value = "lot")
	private Lot lot;
	//static variables
	@Transient
	@Autowired
	private static IBidRepo iBidRepo;
	
	private double amount; 
	
	
	
	@JsonCreator
	public Bid() {

		
	}

	
	public User getBidder() {
		return bidder;
	}
	 void setAmount(double amount) {//delete
		this.amount = amount;
	}
	public Bid(BidBuilder bidBuilder) {
		this.id = bidBuilder.id;
		this.lot = bidBuilder.lot;
		this.amount = bidBuilder.amount;
		this.bidder = bidBuilder.bidder;
		this.bidder.addBidToList(this);
	}
	//instance variables
	
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	 
	public double getAmount() {
		return amount;
	}
	
	public Bid(Lot lot, double amount, User bidder) {
		this.lot = lot;
		this.amount = amount;
		this.bidder = bidder; 
		this.bidder.addBidToList(this);
	}

	//setters, getters
	
	public Lot getLot() {
		return lot;
	}
	
	


	
	
	@Autowired
	public void setIRepo(IBidRepo ibidRepo) {
		iBidRepo = ibidRepo;
	}


	//Persistence handling
	@Override
	public boolean saveToRepo() throws IllegalArgumentException {
			 iBidRepo.save(this);
		return true;
	}

	@Override
	public Iterable<Bid> findAll() {
		return iBidRepo.findAll();
	}


	@Override
	public Optional<? extends IStorable> find() {
		Optional<Bid> bid = null;
			bid = iBidRepo.findById(id);
		return bid;
	}

	@Override
	public void delete() {
		//lot.remove(lot.getBid(this));
			iBidRepo.deleteById(this.id);
		
			 
	}

	public static class BidBuilder{
//		@Id
//		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long id;
		private Lot lot;
		private double amount;
		private User bidder;
		public BidBuilder(Lot lot) {
			this.lot = lot;
			
		}
		public BidBuilder amount(double amount) {
			this.amount  = amount; 
			return this;
		}
		public BidBuilder bidder(User bidder) {
			this.bidder = bidder; 
			return this;
		}
		public Bid build() {
			
			return new Bid(this);
		}
	}
	
	@Override
	public String toString() {
		return "Bid [bidder=" + bidder + ", id=" + id + ", lot=" + lot + ", amount=" + amount + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((bidder == null) ? 0 : bidder.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lot == null) ? 0 : lot.hashCode());
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
		Bid other = (Bid) obj;
		if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
			return false;
		if (bidder == null) {
			if (other.bidder != null)
				return false;
		} else if (!bidder.equals(other.bidder))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lot == null) {
			if (other.lot != null)
				return false;
		} else if (!lot.equals(other.lot))
			return false;
		return true;
	}

 
//	public static class BidComp implements Comparator<Bid>{
//	@Override
//	public int compare(Bid o1, Bid o2) {
//		 
//		return  ((Double) o1.amount).compareTo(((Double) o2.amount));
//	}
//	}
}
