package com.project.biddingSoft.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


public class UserDTO extends StorableDTO{
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<Lot> getLotsCreatedList() {
		return lotsCreatedList;
	}
	public void setLotsCreatedList(List<Lot> lotsCreatedList) {
		this.lotsCreatedList = lotsCreatedList;
	}
	public List<Bid> getBidsBadeList() {
		return bidsBadeList;
	}
	public void setBidsBadeList(List<Bid> bidsBadeList) {
		this.bidsBadeList = bidsBadeList;
	}
	private Long id; 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	private String username;
	private List<Lot> lotsCreatedList; 
	private List<Bid> bidsBadeList; 
}
