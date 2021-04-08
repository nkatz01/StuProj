/**
 * 
 */
package com.project.biddingSoft.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.project.biddingSoft.domain.Lot;
/**
 * @author nuchem
 *
 */
@Repository 
public interface ILotRepo extends  CrudRepository<Lot, Long> {
}
