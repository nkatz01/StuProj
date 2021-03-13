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
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
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
@Component
public class User implements IStorable {

	@JsonCreator
	public User(String username) {

		this.username = username;
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

	public Long getId() {
		return id;
	}

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
