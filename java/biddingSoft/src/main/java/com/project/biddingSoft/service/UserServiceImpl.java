/**
 * 
 */
package com.project.biddingSoft.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

import javax.persistence.Transient;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.lang.reflect.*;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.project.biddingSoft.BiddingSoftwareApplication;
import com.project.biddingSoft.dao.ILotRepo;
import com.project.biddingSoft.dao.IStorable;
import com.project.biddingSoft.dao.IUserRepo;
import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.User;

/**
 * @author nuchem
 *
 */
@Service
@Component
@Qualifier("UserServiceImpl")
public   class UserServiceImpl implements IService<User> {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	 public UserServiceImpl() {
	}
	 
	
	@Transient
	@Autowired
	private static IUserRepo iUserRepo;

	@Autowired
	public void setIUserRepo(IUserRepo iuserRepo) {
		 iUserRepo = iuserRepo;
	}
	@Override
 	public  String persistEntity(User user)  {

		StringBuilder stringBuilder = new StringBuilder();
		if(user.getId()!=null )
				if (iUserRepo.existsById(user.getId()))
			throw new IllegalArgumentException("User already exists");
		iUserRepo.save(user);
		stringBuilder.append(user.getClass().getName() + " "+ user.getId());
		return stringBuilder.toString();

	}
	@Override
	public Iterable<User> getAllRecordsForEnt() {

		return iUserRepo.findAll();
	}

//	public  boolean deleteAllEntities(User user) {
//		
//			StreamSupport.stream(getAllRecordsForEnt(user).spliterator(), false)
//					.forEach(u -> iUserRepo.delete(u));
//	
//		return getAllRecordsForEnt(user).iterator().hasNext() == false;
//	}
//
//	public  boolean deleteEntity(User user)  {
//		
//		iUserRepo.delete(user);	
//	
//		return iUserRepo.findById(user.getId()).isEmpty(); 
//
//	}

	@Override
	public Optional<User> getEntity(Long id) {
		return iUserRepo.findById(id);
	}

}
