/**
 * 
 */
package com.project.biddingSoft.domain;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.biddingSoft.dao.IStorable;



/**
 * @author nuchem
 *
 */
@Entity
//@DiscriminatorValue("Lot")
public  class Lot implements IStorable {
	

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private  Long id;
 	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	@Bean
	public Lot getSubtype() {
		return this;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "Lot [id=" + id + ", username=" + username + "]";
	}

	@Transient
	private  List<Bid> bidList; 
	@Transient
	private  User user;
	@Column(name="description")
	private String description;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name="username")
	private  String username;  
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
		this(new ArrayList<Bid>(), new User("defualt"));
	}
	 @JsonCreator
	public Lot( String username,String description) {
		this.username = username;
		this.description = description; 
		
	}
	
	
	
	
}
