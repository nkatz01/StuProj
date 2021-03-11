package com.project.biddingSoft.dao;

import org.springframework.data.repository.CrudRepository;

import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.User;

public interface IUserRepo extends  CrudRepository<User, Long> {

}
