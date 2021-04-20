/**
 * 
 */
package com.project.biddingSoft.domain;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
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

import lombok.EqualsAndHashCode;
import lombok.ToString;




/**
 * @author nuchem
 *
 */

@ToString
@Entity
@Component
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("Bid")
public class Bid extends Storable {//implements IStorable 
	 
	@ToString.Exclude
	@JsonBackReference(value="bidOnUser")
	@ManyToOne(fetch = FetchType.LAZY )//orphanRemoval=true cascade = CascadeType.REFRESH,
	@JoinColumn(name = "bidderUser_id", referencedColumnName = "id", nullable=false)
	@JsonProperty(value = "bidder")
	//	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private User bidder;
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private Long id;
	 
	//@Override
//	public Long getId() {
//		return super.id;
//	}
//	//@Override
//	public void setId(Long id) {
//		super.id = id;
//	}

	public void setBidder(User bidder) {
		this.bidder = bidder;
	}

	@ToString.Exclude
	@ManyToOne(
			fetch = FetchType.LAZY//in order for jackson to work cascade = CascadeType.REFRESH
			 
			
		)
	@JsonBackReference(value="bidOnLot")
	@JoinColumn(name = "lot_id", referencedColumnName = "id", nullable=false)
	 @JsonProperty(value = "lot")
	private Lot lot;
	//static variables
	


public void setLot(Lot lot) {
		this.lot = lot;
	}


@JsonProperty("amount")
	private double amount; 
	
	
	
	@JsonCreator
	public Bid() {

		
	}

	
	public User getBidder() {
		return bidder;
	}
	public void setAmount(double amount) {//delete
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
	

}
