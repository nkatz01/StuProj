package com.project.biddingSoft.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.project.biddingSoft.domain.User;
@Repository
public interface IUserRepo extends  CrudRepository<User, Long> {
	 

}
