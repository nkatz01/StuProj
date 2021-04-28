/**
 * 
 */
package com.project.biddingSoft.service;

import com.project.biddingSoft.dao.IStorableRepo;
import com.project.biddingSoft.domain.Storable;
import com.project.biddingSoft.domain.StorableDTO;

/**
 * @author nuchem
 *
 */

public interface IDaoService<T extends Storable> {

	public String persistEntity(T storable);

	public String updateEntity(StorableDTO strblDto);

}
