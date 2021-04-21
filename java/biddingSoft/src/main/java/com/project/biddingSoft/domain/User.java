/**
 * 
 */
package com.project.biddingSoft.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.JoinColumn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import com.project.biddingSoft.dao.IUserRepo;
import com.rits.cloning.Cloner;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author nuchem
 *
 */
@Setter
@Getter
@ToString
@Entity
@Component
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("User")
public class User extends Storable {
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	@Autowired
	private Cloner cloner;
	public User(UserBuilder userBuilder) {
		this.id = userBuilder.id;
		this.username = userBuilder.username;
		this.lotsCreatedList = new ArrayList<Lot>(userBuilder.lotsCreatedList);
		this.bidsBadeList = new ArrayList<Bid>(userBuilder.bidsBadeList);
	}

	public User(Long id, String username, List<Lot> lotsCreatedList) {
		this.id = id;
		this.username = username;
		this.lotsCreatedList = new ArrayList<Lot>(lotsCreatedList);
		this.bidsBadeList = new ArrayList<Bid>(bidsBadeList);
	}

	@Column(name = "username")
	private String username;
	@JsonProperty("lotsCreatedList")
	@JsonManagedReference(value = "lotOnUser")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", // , variable in Lot class - links a lot with a given user
			cascade = CascadeType.ALL

	)
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	// you don't ever use this list
	List<Lot> lotsCreatedList = new ArrayList<Lot>();

	@JsonManagedReference(value = "bidOnUser")
	@JsonProperty("bidsBadeList")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bidder", // variable in Bid class - links a Bid with a given user
			cascade = CascadeType.ALL)
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	private List<Bid> bidsBadeList = new ArrayList<Bid>();

	public Lot getLot(int id) {
		return lotsCreatedList.get(id);
		

	}

	public boolean addLotToList(Lot lot) {// handle exception
		// This method shouldn't return antyhing
		// and its simpler if you do it as an if and then add
		//also, does this need to be an ordered list? If not, just use a set and then you don't have to worry about checking before adding
		// If this needs to be a list, you will need to think about concurrency - two methods could add to the list at once and then you have two items in the list
		return !lotsCreatedList.contains(lot) && lotsCreatedList.add(lot);
	}

	public boolean addBidToList(Bid bid) {// handle exception
		// this is the same as the lots list, if you need to keep insertion order, you need to keep the list and manage concurrency, otherwise change to a set and all is good.
		return !bidsBadeList.contains(bid) && bidsBadeList.add(bid);
	}

	@JsonCreator
	public User() {


	}

	public boolean createdLotscontainsLot(Lot lot) {
		return this.lotsCreatedList.contains(lot);

	}

	public static class UserBuilder {

		private Long id;

		private String username;
		private List<Lot> lotsCreatedList;
		private List<Bid> bidsBadeList;

		public UserBuilder(String username, String password) {
			this.username = username;

		}

		public UserBuilder lotsCreated(List<Lot> lotsCreatedList) {
			this.lotsCreatedList = new ArrayList<Lot>( lotsCreatedList);
			return this;
		}

		public UserBuilder bidsCreated(List<Bid> bidsBadeList) {
			this.bidsBadeList = new ArrayList<Bid>( bidsBadeList);
			return this;
		}

		public User build() {
			return new User(this);
		}

	}

}
