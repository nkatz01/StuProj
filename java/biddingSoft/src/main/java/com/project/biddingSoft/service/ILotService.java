/**
 * 
 */
package com.project.biddingSoft.service;

import java.util.List;
import java.util.UUID;

import com.project.biddingSoft.model.Bid;
import com.project.biddingSoft.model.Lot;
import com.project.biddingSoft.model.User;

/**
 * @author nuchem
 *
 */
public interface ILotService {
	public Lot addLot();
	public Lot addLot(User user);
	public boolean addLot(Lot Lot, User user, List<Bid> bidList);
}
