/**
 * 
 */
package com.project.biddingSoft.domain;

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
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.biddingSoft.dao.IBidRepo;
import com.project.biddingSoft.dao.IStorable;



/**
 * @author nuchem
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Component
public class Bid implements IStorable {
	
	public Bid() {

		
	}

	
	public void setAmount(double amount) {//delete
		this.amount = amount;
	}
	public Bid(BidBuilder bidBuilder) {
		this.id = bidBuilder.id;
		this.lot = bidBuilder.lot;
		this.amount = bidBuilder.amount;
	}
	//instance variables
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne(
			cascade = CascadeType.PERSIST,
		    fetch = FetchType.LAZY
		)
	@JoinColumn(name = "lot_id", nullable=false)
	private Lot lot;
	//static variables
		@Transient
	@Autowired
	private static IBidRepo iBidRepo;
	private double amount; 
	
	 
	public double getAmount() {
		return amount;
	}
	
	public Bid(Lot lot, double amount) {
		this.lot = lot;
		this.amount = amount;
	}

	//setters, getters
	@Autowired
	public void setIRepo(IBidRepo ibidRepo) {
		 iBidRepo = ibidRepo;
	}
	
	public Lot getLot() {
		return lot;
	}
	
	


	@Override
	public Long getId() {
		return id;
	}
	

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	//Persistence handling
	@Override
	public boolean saveToRepo() throws IllegalArgumentException {
		try {
			 iBidRepo.save(this);
		} catch (IllegalArgumentException e) {
			throw e;
		}
		return true;
	}

	@Override
	public Iterable<Bid> findAll() {
		return iBidRepo.findAll();
	}


	@Override
	public Optional<? extends IStorable> find() {
		Optional<Bid> bid = null;
		try {
			bid = iBidRepo.findById(id);
		} catch (IllegalArgumentException e) {
			throw e;
		}
		return bid;
	}

	@Override
	public void delete() throws IllegalArgumentException {
		try {
			iBidRepo.deleteById(this.id);
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

	public static class BidBuilder{
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long id;
		private Lot lot;
		private double amount;
		
		public BidBuilder(Lot lot) {
			this.lot = lot;
			
		}
		public BidBuilder amount(double amount) {
			this.amount  = amount; 
			return this;
		}
		public Bid build() {
			
			return new Bid(this);
		}
	}

 
//	public static class BidComp implements Comparator<Bid>{
//	@Override
//	public int compare(Bid o1, Bid o2) {
//		 
//		return  ((Double) o1.amount).compareTo(((Double) o2.amount));
//	}
//	}
}
