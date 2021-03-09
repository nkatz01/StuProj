/**
 * 
 */
package com.project.biddingSoft.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;



/**
 * @author nuchem
 *
 */
@Entity
public class Lot {
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@JsonIgnore
	@JsonProperty(value = "bidList")
	public List<Bid> getBidList() {
		return bidList;
	}

	public void setBidList(List<Bid> bidList) {
		this.bidList = bidList;
	}
	@JsonIgnore
	@JsonProperty(value = "user")
	public User getUser() {
		return user;
	}
	 
	public void setUser(User user) {
		this.user = user;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "Lot [id=" + id + ", userName=" + userName + "]";
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private  Long id;
	@Transient
	private  List<Bid> bidList; 
	@Transient
	private  User user;
	
	
	private  String userName;  
	/**
	 * @param bidList
	 * @param user
	 */
	public Lot(List<Bid> bidList, User user) {
		
		this.bidList = bidList;
		this.user = user;
	}
	
	public Lot(User user) {
		this(new ArrayList<Bid>(), user);
	}
	
	public Lot() {
		this(new ArrayList<Bid>(), new User());
	}
	
	public Lot(String user) {
		userName = user;
		
	}
	
	
	
	
}
