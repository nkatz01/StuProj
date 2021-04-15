package com.project.biddingSoft.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import com.project.biddingSoft.domain.Storable;
@NoRepositoryBean

public interface IStorableRepo<T extends Storable> extends CrudRepository<T, Long> {

}
