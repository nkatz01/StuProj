/**
 * 
 */
package com.project.biddingSoft.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.project.biddingSoft.dao.IStorable;
import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.User;
 
/**
 * @author nuchem
 *
 */

public interface IService<IStorable> {
	
public  String persistEntity( IStorable  iStorable) ;

	public Iterable<IStorable> getAllRecordsForEnt() ;
//
//	public  boolean deleteAllEntities(Bid bid) ;
//
//	public  boolean deleteEntity(Bid bid)  ;
//
	public String updateEntity(IStorable user);
	public Optional<IStorable> getEntity(Long id) ;
}
