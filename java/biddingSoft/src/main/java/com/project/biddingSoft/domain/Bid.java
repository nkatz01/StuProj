/**
 * 
 */
package com.project.biddingSoft.domain;

import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.biddingSoft.dao.IBidRepo;
import com.project.biddingSoft.dao.ILotRepo;
import com.project.biddingSoft.dao.IStorable;

/**
 * @author nuchem
 *
 */
@Entity
@Component
public class Bid implements IStorable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Transient
	@Autowired
	private static IBidRepo iBidRepo;

	@Autowired
	public void setIRepo(IBidRepo ibidRepo) {
		 iBidRepo = ibidRepo;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

//	@Override
//	public Storable getSubtype() {
//		// TODO Auto-generated method stub
//		return null;
//	}
	@Override
	public boolean saveToRepo() throws IllegalArgumentException {
		try {
			 iBidRepo.save(this);
		} catch (IllegalArgumentException e) {
			throw e;
		}
		return true;
	}

	@Override
	public Iterable<Bid> findAll() {
		return iBidRepo.findAll();
	}


	@Override
	public Optional<? extends IStorable> findById(Long id) {
		Optional<Bid> bid = null;
		try {
			bid = iBidRepo.findById(id);
		} catch (IllegalArgumentException e) {
			throw e;
		}
		return bid;
	}

	@Override
	public void deleteById(Long id) {
		try {
			iBidRepo.deleteById(id);
		} catch (IllegalArgumentException e) {
			throw e;
		}
		
	}

}
