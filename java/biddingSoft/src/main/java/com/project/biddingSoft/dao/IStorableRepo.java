package com.project.biddingSoft.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import com.project.biddingSoft.domain.Storable;
//@NoRepositoryBean
@Qualifier("IStorableRepo")
public interface IStorableRepo<T extends Storable> extends CrudRepository<T, Long> {

	@Query("select s from Storable s where s.id = ?1")
	 Optional< T> findByEntityId(Long id);
}
