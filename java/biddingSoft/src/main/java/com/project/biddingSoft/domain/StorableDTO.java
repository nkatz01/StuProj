package com.project.biddingSoft.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", defaultImpl = UserDTO.class)
@JsonSubTypes({ @JsonSubTypes.Type(value = LotDTO.class, name = "lotdto"),
		@JsonSubTypes.Type(value = BidDTO.class, name = "biddto")  })
public abstract class StorableDTO {
	
	
	public StorableDTO() {
		
	}
private Long id; 
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
	
}
