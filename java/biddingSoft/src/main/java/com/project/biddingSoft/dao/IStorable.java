/**
 * 
 */
package com.project.biddingSoft.dao;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.User;

/**
 * @author nuche
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", defaultImpl = Lot.class)
@JsonSubTypes({ @JsonSubTypes.Type(value = User.class, name = "user"),
		@JsonSubTypes.Type(value = Bid.class, name = "bid") })
public interface IStorable {


	public void setId(Long id);

	public boolean saveToRepo();

	public Iterable<? extends IStorable> findAll();//change to static?

	public Optional<? extends IStorable> find();

	 
	public void delete( );
}
