/**
 * 
 */
package com.project.biddingSoft.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author nuchem
 *
 */
@Setter
@Getter
@ToString(callSuper = true, includeFieldNames = true)
@Entity
@Component
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("User")
public class User extends Storable {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((bidsBadeSet == null) ? 0 : bidsBadeSet.hashCode());
		result = prime * result + ((lotsCreatedSet == null) ? 0 : lotsCreatedSet.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		User other = (User) obj;
		if (bidsBadeSet == null) {
			if (other.bidsBadeSet != null)
				return false;
		} else if (!bidsBadeSet.equals(other.bidsBadeSet))
			return false;
		if (lotsCreatedSet == null) {
			if (other.lotsCreatedSet != null)
				return false;
		} else if (!lotsCreatedSet.equals(other.lotsCreatedSet))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	public User(UserBuilder userBuilder) {
		this.id = userBuilder.id;
		this.username = userBuilder.username;
		this.lotsCreatedSet = userBuilder.lotsCreatedSet;// lot builder has already done the sync work
		this.bidsBadeSet = userBuilder.bidsBadeSet;
	}

	public User(Long id, String username, Set<Lot> lotsCreatedSet, Set<Bid> bidsBadeSet) {
		this.id = id;
		this.username = username;
		this.lotsCreatedSet = Collections.synchronizedSet(new HashSet<Lot>(lotsCreatedSet));
		this.bidsBadeSet = Collections.synchronizedSet(new HashSet<Bid>(bidsBadeSet));
	}

	@Column(name = "username")
	private String username;
	@JsonProperty("lotsCreatedSet")
	@JsonManagedReference(value = "lotOnUser")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", // , variable in Lot class - links a lot with a given user
			cascade = CascadeType.ALL

	)
	
	@Getter(AccessLevel.NONE)
	private Set<Lot> lotsCreatedSet = Collections.synchronizedSet(new HashSet<Lot>());

	@JsonManagedReference(value = "bidOnUser")
	@JsonProperty("bidsBadeSet")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bidder", // variable in Bid class - links a Bid with a given user
			cascade = CascadeType.ALL)
	private Set<Bid> bidsBadeSet = Collections.synchronizedSet(new HashSet<Bid>());

	public boolean addLotToSet(Lot lot) {
		return lotsCreatedSet.add(lot);
	}

	public boolean addBidToSet(Bid bid) {
		return bidsBadeSet.add(bid);
	}

	@JsonCreator
	public User() {

	}

	@JsonIgnore
	public int getNumberOfLots() {
		return this.lotsCreatedSet.size();
	}

	public boolean createdLotscontainsLot(Lot lot) {
		return this.lotsCreatedSet.contains(lot);

	}

	public final static class UserBuilder {

		private Long id;

		private String username;
		private Set<Lot> lotsCreatedSet;
		private Set<Bid> bidsBadeSet;

		public UserBuilder(String username, String password) {
			this.username = username;

		}

		public UserBuilder lotsCreated(Set<Lot> lotsCreatedSet) {
			this.lotsCreatedSet = Collections.synchronizedSet(new HashSet<Lot>(lotsCreatedSet));
			return this;
		}

		public UserBuilder bidsCreated(Set<Bid> bidsBadeSet) {
			this.bidsBadeSet = Collections.synchronizedSet(new HashSet<Bid>(bidsBadeSet));
			return this;
		}

		public User build() {
			return new User(this);
		}

	}

}
