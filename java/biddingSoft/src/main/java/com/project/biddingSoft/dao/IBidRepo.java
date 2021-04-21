/**
 * 
 */
package com.project.biddingSoft.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.project.biddingSoft.domain.Bid;

/**
 * @author nuchem
 *
 */

@Repository
@Transactional
@Qualifier("IBidRepo")
public interface IBidRepo extends   IStorableRepo<Bid> {	
}
