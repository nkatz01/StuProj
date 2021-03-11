/**
 * 
 */
package com.project.biddingSoft.domain;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.biddingSoft.dao.IStorable;

/**
 * @author nuchem
 *
 */
@Entity
public class User implements IStorable {

	 @JsonCreator 
	public User(  String username) {
		 
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private  Long id;
 	public Long getId() {
 		return id;
 	}
 	public void setId(Long id) {
		this.id = id;
		
	}
 	@Column(name = "username")
 	String username; 
//	@Override
//	public Storable getSubtype() {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	

}
