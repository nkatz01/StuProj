/**
 * 
 */
package com.project.biddingSoft.domain;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.biddingSoft.dao.ILotRepo;
import com.project.biddingSoft.dao.IStorable;
import com.project.biddingSoft.dao.IUserRepo;

/**
 * @author nuchem
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Component
public class User implements IStorable {
	
	
	public static class UserBuilder{
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long id;
		private final String userName; 
		private String address; 
		private char[] password; 
		
		public UserBuilder(String userName, String password) {
			this.userName = userName; 
			this.password = password.toCharArray();
		
		}
		public UserBuilder address(String address) {
			this.address = address;
			return this;
		}
		public User build()
		{
			return new User(this);
		}
	
	}
	
	public User(UserBuilder userBuilder) {
		this( userBuilder.id, userBuilder.userName, userBuilder.address, userBuilder.password);
	}
	@JsonCreator
	public User(Long id, String username, String address, char[] password ) {
		this.id = id;
		this.username = username;
		this.address = address;
		this.password = password; 
	}

	public User() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String address; 
	private char[] password; 
	public Long getId() {
		return id;
	}
	@Override
	public void setId(Long id) {
		this.id = id;

	}

	@Column(name = "username")
	String username;

	@Override
	public boolean saveToRepo() throws IllegalArgumentException {
		try {
			iUserRepo.save(this);
		} catch (IllegalArgumentException e) {
			throw e;
		}
		return true;
	}

	@Override
	public Iterable<User> findAll() {
		return iUserRepo.findAll();
	}

	@Transient
	@Autowired
	private static IUserRepo iUserRepo;

	@Autowired
	public void setIUserRepo(IUserRepo iuserRepo) {
		 iUserRepo = iuserRepo;
	}

	@Override
	public Optional<? extends IStorable> findById(Long id) throws IllegalArgumentException {
		Optional<Lot> lot = null;
		  try {
			iUserRepo.findById(id);
		  } catch (IllegalArgumentException e) {
				throw e;
		}
		  return lot;
	}

	@Override
	public void deleteById(Long id) throws IllegalArgumentException {
		try {
			iUserRepo.deleteById(id);
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}
	 

}
