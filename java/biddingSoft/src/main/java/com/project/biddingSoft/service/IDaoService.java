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

	public String persistEntity(T iStorable);

	public default void deleteAllEntities(IStorableRepo<Storable> repo) {
		repo.deleteAll();
	}

	public default void deleteEntity(IStorableRepo<Storable> repo, Long id) {
		repo.deleteById(id);
	}

	public String updateEntity(StorableDTO strblDto);

}
