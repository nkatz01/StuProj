package com.project.biddingSoft.domain;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", defaultImpl = UserDTO.class)
@JsonSubTypes({ @JsonSubTypes.Type(value = LotDTO.class, name = "lotdto"),
		@JsonSubTypes.Type(value = BidDTO.class, name = "biddto") })
@Setter
@Getter
public abstract class StorableDTO {
	private Long id; 
	private UUID businessId;
}
