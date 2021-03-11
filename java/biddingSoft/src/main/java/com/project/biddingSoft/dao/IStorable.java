/**
 * 
 */
package com.project.biddingSoft.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.User;

/**
 * @author nuche
 *
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        defaultImpl = Lot.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = User.class, name = "user"),
        @JsonSubTypes.Type(value = Bid.class, name = "bid")
})
 public interface IStorable {
	 
 
 	public  Long getId()  ;

	public void setId(Long id) ;
}
