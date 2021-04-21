/**
 * 
 */
package com.project.biddingSoft.service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.common.base.Defaults;

import com.project.biddingSoft.dao.IStorableRepo;
import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.Storable;
import com.project.biddingSoft.domain.StorableDTO;
import com.project.biddingSoft.domain.User;
import com.project.biddingSoft.domain.UserDTO;

/**
 * @author nuchem
 *
 */

public interface IService<T extends Storable> {

	public String persistEntity(T iStorable);

	public default void deleteAllEntities(IStorableRepo<Storable> repo) {
		repo.deleteAll();
	}

	public default void deleteEntity(IStorableRepo<Storable> repo, Long id) {
		repo.deleteById(id);
	}

	public String updateEntity(StorableDTO strblDto);

}
