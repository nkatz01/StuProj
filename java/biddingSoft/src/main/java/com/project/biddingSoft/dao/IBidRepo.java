/**
 * 
 */
package com.project.biddingSoft.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import com.project.biddingSoft.domain.Storable;
import com.project.biddingSoft.domain.User;
import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.Lot;
/**
 * @author nuchem
 *
 */
//@NoRepositoryBean
@Repository
public interface IBidRepo extends  CrudRepository<Bid, Long> {
	
	
}
