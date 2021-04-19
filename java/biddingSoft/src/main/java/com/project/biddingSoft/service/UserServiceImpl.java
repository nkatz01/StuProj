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
import com.project.biddingSoft.dao.IStorable;
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
//	@Autowired
//	public void setIStorableRepo(IStorableRepo istorableRepo) {
//		iStorableRepo = istorableRepo;
//	}
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
	 
	public String updateEntity(StorableDTO userDto){
		String mesg = ""+ userDto.getId();
		try {
			User user = iUserRepo.findById(userDto.getId()).get();
			iUserRepo.save(user);//(CrudRepository<IStorable, Long> )
		} catch (Exception e) {
			 mesg = " could not be updated " + e.getMessage();
		}
		return mesg; 
	}
	 
//	public String updateEntity(User user){
//		StringBuilder stringBuilder = new StringBuilder();
//		if(user.getId()!=null && iUserRepo.existsById(user.getId())) {
//			User userFromRepo = iUserRepo.findById(user.getId()).get();
//			Field[] fields = userFromRepo.getClass().getDeclaredFields();
//			try {
//				for(Field field : fields) {
//					FieldUtils.writeDeclaredField(userFromRepo, field.getName(),FieldUtils.readField(user, field.getName().toString(), true), true);
//				}
//				iUserRepo.save(userFromRepo);
//				stringBuilder.append(userFromRepo.getClass().getName() + " "+ userFromRepo.getId()).toString();
//				
//			} catch (Exception e) {
//				stringBuilder.append("/n" + e.getMessage() +"/n");
//			}
//		}
//		else 
//			stringBuilder.append(user.getClass().getName() + " with "+ user.getId().toString() +" not found.");
//			
//		return stringBuilder.toString();
//	}
//	@Override
//	public String updateEntity(Storable user){
//		String mesg = null;
//		try {
//			mesg = IService.super.update(iStorableRepo, user);//(CrudRepository<IStorable, Long> )
//			
//		} catch (IllegalAccessException e) {
//			 mesg = "cannot access some fields";
//		}
//		return mesg; 
//	}
//	@Override
//	public String updateEntity(User user) {
//		if(user.getId()!=null && iUserRepo.existsById(user.getId()))
//			iUserRepo.save(user);
//		return new StringBuilder().append(user.getClass().getName() + " "+ user.getId()).toString();
//	}
//	@Override
//	public Iterable<User> getAllRecordsForEnt() {
//
//		return iUserRepo.findAll();
//	}

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

//	@Override
//	public Optional<User> getEntity(Long id) {
//		return iUserRepo.findById(id);
//	}

}
