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

import org.apache.commons.lang3.reflect.FieldUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.project.biddingSoft.BiddingSoftwareApplication;
import com.project.biddingSoft.dao.ILotRepo;

import com.project.biddingSoft.dao.IStorableRepo;
import com.project.biddingSoft.dao.IUserRepo;
import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.Storable;
import com.project.biddingSoft.domain.StorableDTO;
import com.project.biddingSoft.domain.User;
import com.project.biddingSoft.domain.UserDTO;
import com.project.biddingSoft.domain.BiddingSoftMapper;

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
	 
	@Autowired
	BiddingSoftMapper userMapper; 
	@Autowired
	private static IUserRepo iUserRepo;

	
	@Autowired
	public void setIUserRepo(IUserRepo iuserRepo) {
		 iUserRepo = iuserRepo;
	}
	@Autowired
	private   IStorableRepo<Storable> iStorableRepo;

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
	public String updateEntity(StorableDTO userDto){
		String mesg = User.class.getName() + " "+ userDto.getId();
		try {
			User user = iUserRepo.findById(userDto.getId()).get();
			userMapper.updateUserFromDto((UserDTO)userDto, user);
			iUserRepo.save(user);
			mesg += " successfully updated";
		} catch (Exception e) {
			 mesg += " could not be updated: " + e.getMessage();
		}
		return mesg; 
	}
	 


}
