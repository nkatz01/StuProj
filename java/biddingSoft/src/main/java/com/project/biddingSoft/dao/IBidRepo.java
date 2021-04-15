/**
 * 
 */
package com.project.biddingSoft.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
@Qualifier("IBidRepo")
public interface IBidRepo extends   IStorableRepo<Bid> {
	 
	 
	
}
