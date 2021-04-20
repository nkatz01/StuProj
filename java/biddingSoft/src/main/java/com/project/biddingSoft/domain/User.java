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
@DiscriminatorValue("User")
public class User extends Storable   {//implements  IStorable
	
	


	
	
	
	public User(UserBuilder userBuilder) {
	this.id =	 userBuilder.id;
	this.username =	 userBuilder.username;
	this.lotsCreatedList =	new ArrayList<Lot>( userBuilder.lotsCreatedList); 
	this.bidsBadeList = 	new ArrayList<Bid>( userBuilder.bidsBadeList);
	}
 

	public User(Long id, String username,  List<Lot> lotsCreatedList ) {//String address, char[] password,
		this.id = id;
		this.username = username;
//		this.address = address;
//		this.password = password; 
 		this.lotsCreatedList = new ArrayList<Lot>(  lotsCreatedList );
 		this.bidsBadeList = new ArrayList<Bid>(  bidsBadeList );
	}
	@Column(name = "username")
	private String username;
//	@JsonManagedReference(value="leadingLotsOnBidder")
//	@JsonProperty("leadingLots")
//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "leadingBidder", // variable in bid class - that links bid to a lot
//			cascade = CascadeType.ALL, orphanRemoval = true)
//	private List<Lot> leadingLots;
	
   @JsonProperty("lotsCreatedList")
   @JsonManagedReference(value="lotOnUser")
 @OneToMany(
		 fetch = FetchType.LAZY,
	    mappedBy = "user",// ,	  orphanRemoval = truevariable in Lot class - links a lot with a given user
	     cascade = CascadeType.ALL
	    
	   
	) 
	List<Lot> lotsCreatedList; 
	
   @JsonManagedReference(value="bidOnUser")
 @JsonProperty("bidsBadeList")//causes recursion in curl request
 @OneToMany(
		 fetch = FetchType.LAZY,
	    mappedBy = "bidder",//variable in Bid class - links a Bid with a given user
	     cascade = CascadeType.ALL
	    // ,	  orphanRemoval = true
	   
	)
// @JoinTable(name= "users_bids", joinColumns={@JoinColumn(referencedColumnName="id")}
//		 , inverseJoinColumns={@JoinColumn(referencedColumnName="id")}) 
	private List<Bid> bidsBadeList; 
 
 
   
// 	public void setBidsBadeList(List<Bid> bidsBadeList) {
// 		this.bidsBadeList.clear();
// 		if (this.bidsBadeList!=null)
//	this.bidsBadeList.addAll(bidsBadeList);
//}


	public Lot getLot(int id) throws IndexOutOfBoundsException{
 		Lot lot =null;
			 lot = lotsCreatedList.get(id);		
 		return lot;
 		
 	}
 	public boolean addLotToList(Lot lot) {//handle exception
 		return !lotsCreatedList.contains(lot) && lotsCreatedList.add(lot);
 		}
 	
	public boolean addBidToList(Bid bid) {//handle exception
 		return !bidsBadeList.contains(bid) && bidsBadeList.add(bid);
 		}
 	 @JsonCreator
	public User() {
		 this.bidsBadeList = new ArrayList<Bid>();
		 this.lotsCreatedList = new ArrayList<Lot>();

	}
	public boolean createdLotscontainsLot(Lot lot) {
		return this.lotsCreatedList.contains(lot);
		
	}
	public String getUsername() {
		return username;
	}
//	public Bid getLot(Lot lot) {
//		return bidList.stream().filter(b -> b.equals(bid)).findFirst().orElseThrow(NoSuchElementException::new);
//	}
	public void setUsername(String username) {
		this.username = username;
	}
//	@JsonProperty("id")
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private Long id;

//	//@Override
//	public Long getId() {
//		return super.id;
//	}
//
//	//@Override
//	public void setId(Long id) {
//		super.id = id;
//
//	}


	

	public static class UserBuilder{
//		@Id
//		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long id;
		
		private  String username; 
//		private String address; 
//		private char[] password; 
		private List<Lot> lotsCreatedList;
		private List<Bid> bidsBadeList;
		public UserBuilder(String username, String password ) {
			this.username = username; 
			//this.password = password.toCharArray();
		
		}
//		public UserBuilder address(String address) {
//			this.address = address;
//			return this;
//		}
		public UserBuilder lotsCreated(List<Lot> lotsCreatedList) {//fix
			this.lotsCreatedList = lotsCreatedList;
			return this;
		}
		public UserBuilder bidsCreated(List<Bid> bidsBadeList) {//fix
			this.bidsBadeList = bidsBadeList;
			return this;
		}
		
		public User build()
		{
			return new User(this);
		}
	
	}
	




}
