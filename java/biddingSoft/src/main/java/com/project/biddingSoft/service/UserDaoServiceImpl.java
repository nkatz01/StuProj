/**
 * 
 */
package com.project.biddingSoft.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.project.biddingSoft.dao.IStorableRepo;
import com.project.biddingSoft.dao.IUserRepo;
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
public   class UserDaoServiceImpl implements IDaoService<User> {
	 
	@Autowired
	BiddingSoftMapper userMapper; 
	@Autowired
	private IUserRepo iUserRepo;


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
