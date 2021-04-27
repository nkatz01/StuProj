package com.project.biddingSoft.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.biddingSoft.domain.User;

@Repository
@Transactional
@Qualifier("IUserRepo")
public interface IUserRepo extends  IStorableRepo<User>{
}
