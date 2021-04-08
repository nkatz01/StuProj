/**
 * 
 */
package com.project.biddingSoft.domain;

 import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.Transient;
import javax.persistence.JoinColumn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.biddingSoft.dao.IStorable;
import com.project.biddingSoft.dao.IUserRepo;
 /**
 * @author nuchem
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Component
//@Transactional
public class User implements IStorable {
	
	


	
	
	
	public User(UserBuilder userBuilder) {
	this.id =	 userBuilder.id;
	this.username =	 userBuilder.username;
	this.lotsCreatedList =	 userBuilder.lotsCreatedList;
	this.bidsList =	new ArrayList<Bid>( userBuilder.bidsList);
	}
	 
//	@JsonCreator
	public User(Long id, String username,  List<Lot> lotsCreatedList ) {//String address, char[] password,
		this.id = id;
		this.username = username;
//		this.address = address;
//		this.password = password; 
 		this.lotsCreatedList = new ArrayList<Lot>(  lotsCreatedList );
	}
 @OneToMany(
		 fetch = FetchType.LAZY,
	    mappedBy = "user",//variable in Lot class - links a lot with a given user
	     cascade = CascadeType.ALL,
	  orphanRemoval = true
	   
	)
	List<Lot> lotsCreatedList; 
 
 @OneToMany(
		 fetch = FetchType.LAZY,
	    mappedBy = "bidder",//variable in Bid class - links a Bid with a given user
	     cascade = CascadeType.ALL,
	  orphanRemoval = true
	   
	)
// @JoinTable(name= "users_bids", joinColumns={@JoinColumn(referencedColumnName="id")}
//		 , inverseJoinColumns={@JoinColumn(referencedColumnName="id")}) 
	private List<Bid> bidsList; 
 
 	public Lot getLot(int id) throws IndexOutOfBoundsException{
 		Lot lot =null;
			 lot = lotsCreatedList.get(id);		
 		return lot;
 		
 	}
 	public boolean addLotToList(Lot lot) {//handle exception
 		return !lotsCreatedList.contains(lot) && lotsCreatedList.add(lot);
 		}
	public User() {
		this.bidsList = new ArrayList<Bid>();

	}
	public boolean containsLot(Lot lot) {
		return this.lotsCreatedList.contains(lot);
		
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
//	private String address; 
//	private char[] password; 
	
	@Override
	public void setId(Long id) {
		this.id = id;

	}

	@Column(name = "username")
	String username;

	@Override
	public boolean saveToRepo() throws IllegalArgumentException {
			iUserRepo.save(this);
		return true;
	}

	@Override
	public Iterable<User> findAll() {
		return iUserRepo.findAll();
	}

	@Transient
	@Autowired
	private static IUserRepo iUserRepo;

	@Autowired
	public void setIUserRepo(IUserRepo iuserRepo) {
		 iUserRepo = iuserRepo;
	}

	@Override
	public Optional<? extends IStorable> find() throws IllegalArgumentException {
		Optional<User> user = null;
		user =	iUserRepo.findById(this.id);
		  return user;
	}

	@Override
	public void delete() throws IllegalArgumentException {
			iUserRepo.deleteById(this.id);
		
	}

	public static class UserBuilder{
//		@Id
//		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long id;
		
		private  String username; 
//		private String address; 
//		private char[] password; 
		private List<Lot> lotsCreatedList;
		private List<Bid> bidsList;
		public UserBuilder(String username, String password ) {
			this.username = username; 
			//this.password = password.toCharArray();
		
		}
//		public UserBuilder address(String address) {
//			this.address = address;
//			return this;
//		}
		public UserBuilder lotsCreated(List<Lot> lotsCreatedList) {
			this.lotsCreatedList = lotsCreatedList;
			return this;
		}
		public UserBuilder bidsCreated(List<Bid> bidsList) {
			this.bidsList = bidsList;
			return this;
		}
		
		public User build()
		{
			return new User(this);
		}
	
	}

}
