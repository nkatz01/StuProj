/**
 * 
 */
package com.project.biddingSoft.domain;

 import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.biddingSoft.dao.ILotRepo;
import com.project.biddingSoft.dao.IStorable;
import com.project.biddingSoft.dao.IUserRepo;
import com.project.biddingSoft.service.LotService;
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
		
		private  String userName; 
		private String address; 
		private char[] password; 
		private List<Lot> lotList;
		public UserBuilder(String userName, String password ) {
			this.userName = userName; 
			this.password = password.toCharArray();
		
		}
		public UserBuilder address(String address) {
			this.address = address;
			return this;
		}
		public UserBuilder lotList(List<Lot> lotList) {
			this.lotList = lotList;
			return this;
		}
		public User build()
		{
			return new User(this);
		}
	
	}
	
	public User(UserBuilder userBuilder) {
		this( userBuilder.id, userBuilder.userName, userBuilder.address, userBuilder.password, userBuilder.lotList);
	}
	 
//	@JsonCreator
	public User(Long id, String username, String address, char[] password, List<Lot> lotList ) {
		this.id = id;
		this.username = username;
		this.address = address;
		this.password = password; 
 		this.lotList = new ArrayList<Lot>(  lotList );
	}
 @OneToMany(
		 fetch = FetchType.LAZY,
	    mappedBy = "user",//variable in Lot class - links a lot with a given user
	     cascade = CascadeType.ALL,
	  orphanRemoval = true
	   
	)
	List<Lot> lotList; 
 	public boolean addLotToList(Lot lot) {//handle exception
 		return !lotList.contains(lot) && lotList.add(lot);
 		}
	public User() {
	}
	public boolean containsLot(Lot lot) {
		return this.lotList.contains(lot);
		
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
	@Override
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
	public Optional<? extends IStorable> find() throws IllegalArgumentException {
		Optional<User> user = null;
		  try {
		user =	iUserRepo.findById(this.id);
		  } catch (IllegalArgumentException e) {
				throw e;
		}
		  return user;
	}

	@Override
	public void delete() throws IllegalArgumentException {
		try {
			iUserRepo.deleteById(this.id);
		} catch (IllegalArgumentException e) {
			throw e;
		}
		
	}
	 
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lotList == null) ? 0 : lotList.hashCode());
		result = prime * result + Arrays.hashCode(password);
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lotList == null) {
			if (other.lotList != null)
				return false;
		} else if (!lotList.equals(other.lotList))
			return false;
		if (!Arrays.equals(password, other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}