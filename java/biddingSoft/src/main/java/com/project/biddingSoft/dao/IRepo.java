/**
 * 
 */
package com.project.biddingSoft.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import com.project.biddingSoft.domain.Storable;
import com.project.biddingSoft.domain.User;
import com.project.biddingSoft.domain.Lot;
/**
 * @author nuchem
 *
 */
//@NoRepositoryBean
public interface IRepo extends  CrudRepository<Lot, Long> {
}
