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

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author nuchem
 *
 */

@ToString(callSuper = true, includeFieldNames = true)
@Entity
@Component
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("Bid")
@Setter
@Getter
public class Bid extends Storable {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Bid other = (Bid) obj;
		if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
			return false;
		if (lot == null) {
			if (other.lot != null)
				return false;
		} else if (!lot.equals(other.lot))
			return false;
		return true;
	}

	@ToString.Exclude
	@JsonBackReference(value = "bidOnUser")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bidderUser_id", referencedColumnName = "id", nullable = false)
	@JsonProperty(value = "bidder")

	private User bidder;// setter/getter for the purpose of update testing

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY

	)
	@JsonBackReference(value = "bidOnLot")
	@JoinColumn(name = "lot_id", referencedColumnName = "id", nullable = false)
	@JsonProperty(value = "lot")
	@Getter(AccessLevel.NONE)
	private Lot lot; // setter for the purpose of update testing

	@JsonProperty("amount")
	private double amount;

	@JsonCreator
	public Bid() {

	}

	public Bid(BidBuilder bidBuilder) {
		this.id = bidBuilder.id;
		this.lot = bidBuilder.lot;
		this.amount = bidBuilder.amount;
		this.bidder = bidBuilder.bidder;
		this.bidder.addBidToSet(this);
	}
	// instance variables

	public Bid(Lot lot, double amount, User bidder) {
		this.lot = lot;
		this.amount = amount;
		this.bidder = bidder;
		this.bidder.addBidToSet(this);
	}

	// setters, getters

	public Lot getLot() {
		return lot;
	}

	public static class BidBuilder {

		private Long id;
		private Lot lot;
		private double amount;
		private User bidder;

		public BidBuilder(Lot lot) {
			this.lot = lot;

		}

		public BidBuilder amount(double amount) {
			this.amount = amount;
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
