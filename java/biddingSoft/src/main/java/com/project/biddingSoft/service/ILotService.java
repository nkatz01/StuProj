/**
 * 
 */
package com.project.biddingSoft.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.project.biddingSoft.domain.Lot;
 
/**
 * @author nuchem
 *
 */

public interface ILotService {
	public Lot addLot();
//	public Lot addLot(User user);
//	public boolean addLot(Lot Lot, User user, List<Bid> bidList);
}
