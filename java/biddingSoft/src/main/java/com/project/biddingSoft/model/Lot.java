/**
 * 
 */
package com.project.biddingSoft.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author nuchem
 *
 */
public class Lot {

	private final List<Bid> bidList; 
	private final UUID userID;
	/**
	 * @param bidList
	 * @param userID
	 */
	public Lot(List<Bid> bidList, UUID userID) {
		
		this.bidList = bidList;
		this.userID = userID;
	}
	
	public Lot(UUID userID) {
		this(new ArrayList<Bid>(), userID);
	}
	
	public Lot() {
		this(new ArrayList<Bid>(), UUID.randomUUID());
	}
	
	
}
