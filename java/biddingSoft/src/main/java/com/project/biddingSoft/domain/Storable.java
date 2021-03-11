/**
 * 
 */
package com.project.biddingSoft.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.*;
import javax.persistence.MappedSuperclass;

import org.springframework.context.annotation.Bean;

import com.project.biddingSoft.dao.IStorable;

/**
 * @author nuche
 *
 */

@MappedSuperclass
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name="storable_type")
//@Table(name="Storable")
public   class Storable implements IStorable {
	
	
 
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected  Long id;
	
	// public abstract Long getId() ;
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	@Override
	public Long getId() {
		return id;
	}
}
