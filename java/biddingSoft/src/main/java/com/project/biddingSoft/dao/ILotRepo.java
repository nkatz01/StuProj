/**
 * 
 */
package com.project.biddingSoft.dao;

import org.springframework.data.repository.CrudRepository;

import com.project.biddingSoft.model.Lot;
/**
 * @author nuchem
 *
 */
public interface ILotRepo extends CrudRepository<Lot, Long> {

}
