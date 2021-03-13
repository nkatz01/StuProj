/**
 * 
 */
package com.project.biddingSoft.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.biddingSoft.dao.ILotRepo;
import com.project.biddingSoft.dao.IStorable;
import com.project.biddingSoft.service.DaoServiceImpl2;

/**
 * @author nuchem
 *
 */
@Entity
@Component
public class Lot implements IStorable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

//	@JsonIgnore
//	@JsonProperty(value = "bidList")
//	public List<Bid> getBidList() {
//		return bidList;
//	}
//
//	public void setBidList(List<Bid> bidList) {
//		this.bidList = bidList;
//	}
//	@JsonIgnore
//	@JsonProperty(value = "user")
//	public User getUser() {
//		return user;
//	}

//	public void setUser(User user) {
//		this.user = user;
//	}

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

	public Lot() {
	}

//	@Transient
//	private  List<Bid> bidList; 
//	@Transient
//	private  User user;
	@Column(name = "description")
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "username")
	private String username;

	/**
	 * @param bidList
	 * @param user
	 */
//	public Lot(List<Bid> bidList, User user) {
//		
//		this.bidList = bidList;
//		this.user = user;
//	}
//	
//	public Lot(User user) {
//		this(new ArrayList<Bid>(), user);
//	}
//	
//	public Lot() {
//		this(new ArrayList<Bid>(), new User("defualt"));
//	}
	@JsonCreator
	public Lot(String username, String description) {
		this.username = username;
		this.description = description;

	}

	@Override
	public boolean saveToRepo() throws IllegalArgumentException {
		try {
			 iLotRepo.save(this);
		} catch (IllegalArgumentException e) {
			throw e;
		}
		return true;
	}

	@Override
	public Iterable<Lot> findAll() {
		return iLotRepo.findAll();
	}

 
	@Transient
	@Autowired
	private static ILotRepo iLotRepo;

	@Autowired
	public void setILotRepo(ILotRepo ilotrepo) {
		 iLotRepo = ilotrepo;
	}

	@Override
	public Optional<? extends IStorable> findById(Long id) throws IllegalArgumentException {
		Optional<Lot> lot = null;
		try {
			lot = iLotRepo.findById(id);
		} catch (IllegalArgumentException e) {
			throw e;
		}
		return lot;
	}

	@Override
	public void deleteById(Long id) throws IllegalArgumentException {
		try {
			iLotRepo.deleteById(id);
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

}
