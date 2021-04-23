package com.project.biddingSoft.domain;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO extends StorableDTO{
	private String username;
	private Set<Lot> lotsCreatedSet; //could improve by making sure this would also be synchronised 
	private Set<Bid> bidsBadeSet; //could improve by making sure this would also be synchronised 
}
