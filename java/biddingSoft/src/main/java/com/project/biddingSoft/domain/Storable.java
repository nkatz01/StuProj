package com.project.biddingSoft.domain;



import java.util.Objects;
import java.util.UUID;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.NaturalId;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.project.biddingSoft.service.StorableService;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", defaultImpl = User.class)
@JsonSubTypes({ @JsonSubTypes.Type(value = Lot.class, name = "lot"),
		@JsonSubTypes.Type(value = Bid.class, name = "bid")  })
@Inheritance(strategy = InheritanceType.JOINED) 
@DiscriminatorColumn(name = "entity_type")
@Entity
@Component

@ToString
public abstract class Storable  {
	
	@Override
	public int hashCode() {
		return Objects.hashCode(businessId);
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
		return Objects.equals(businessId, other.businessId);
			
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
 protected Long id;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	@NaturalId
	protected UUID businessId = new UUID(StorableService.get64MostSignificantBitsForVersion1(), StorableService.get64LeastSignificantBitsForVersion1());

	public UUID getBusinessId() {
		return businessId;
	}
	public void setBusinessId(UUID businessId) {
		this.businessId = businessId;
	}

}
