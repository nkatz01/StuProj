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



/**
 * @author nuchem
 *
 */
@Entity
public class Lot {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private  Integer id;
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
