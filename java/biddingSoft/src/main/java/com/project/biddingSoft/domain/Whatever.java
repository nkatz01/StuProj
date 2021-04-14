package com.project.biddingSoft.domain;

import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.biddingSoft.dao.IStorable;
import com.project.biddingSoft.dao.IUserRepo;
import com.project.biddingSoft.dao.IWhateverRepo;
@Entity
@Component
@Inheritance(strategy = InheritanceType.JOINED)
public class Whatever implements IStorable {
	public Whatever () {}
	 @JsonProperty("user")
	
	 @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
		@JoinColumn(name = "user", nullable = true, referencedColumnName = "id") // 
	private User user;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	public Long getId() {
		return id;
	}
	@Transient
	@Autowired
	private static IWhateverRepo iWhateverRepo;
	@Transient
	@Autowired
	private static IUserRepo iUserRepo;
	@Autowired
	public void setIWhateverRepo(IWhateverRepo iWhateverRepo) {
		Whatever.iWhateverRepo = iWhateverRepo;
	}
	@Autowired
	public void setIUserRepo(IUserRepo iUserRepo) {
		Whatever.iUserRepo = iUserRepo;
	}
	@Override
	public void setId(Long id) {
		this.id = id;
		
	}
	@Transactional
	@Override
	public boolean saveToRepo() {
	//	System.out.println("whatasdfsdafsdajflsdfjdslkf");
		
				//iUserRepo.save(this.getUser());
	if (this.user!=null)
		this.getUser().delete();
	
				iWhateverRepo.save(this);
				 System.out.println(iWhateverRepo.existsById(this.id));
			return true;
		
	}

	@Override
	public Iterable<? extends IStorable> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends IStorable> find() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

}
