package com.project.biddingSoft.domain;



import java.util.UUID;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.annotations.NaturalId;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.project.biddingSoft.service.StorableService;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", defaultImpl = User.class)
@JsonSubTypes({ @JsonSubTypes.Type(value = Lot.class, name = "lot"),
		@JsonSubTypes.Type(value = Bid.class, name = "bid")})
@Inheritance(strategy = InheritanceType.JOINED) 
@DiscriminatorColumn(name = "entity_type")
@Entity
@Component
@Setter
@Getter
@ToString
public abstract class Storable  {
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((businessId == null) ? 0 : businessId.hashCode());
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
		Storable other = (Storable) obj;
		if (businessId == null) {
			if (other.businessId != null)
				return false;
		} else if (!businessId.equals(other.businessId))
			return false;
		
		return true;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Setter(AccessLevel.NONE)
	protected Long id;	

	@NaturalId
	protected UUID businessId = new UUID(StorableService.get64MostSignificantBitsForVersion1(), StorableService.get64LeastSignificantBitsForVersion1());

	 
}
