package com.project.biddingSoft.domain;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.biddingSoft.dao.IStorable;
//@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED) 
@DiscriminatorColumn(name = "entity_type")
@Entity
@Component
public abstract class Storable implements IStorable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
 protected Long id;
	
	public Long getId() {
		return id;
	}
	//@Override
	public void setId(Long id) {
		id = id;
	}

}
