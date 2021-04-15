/**
 * 
 */
package com.project.biddingSoft.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.User;
/**
 * @author nuchem
 *
 */
@Repository 
@Transactional
@Qualifier("ILotRepo")
public interface ILotRepo extends  IStorableRepo<Lot> {
}
