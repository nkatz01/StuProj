/**
 * 
 */
package com.project.biddingSoft.service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.project.biddingSoft.dao.IStorable;
import com.project.biddingSoft.dao.IStorableRepo;
import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.Storable;
import com.project.biddingSoft.domain.User;
 
/**
 * @author nuchem
 *
 */

public interface IService<T extends IStorable> {
	
	public  String persistEntity( T  iStorable) ;

	//public Iterable<T> getAllRecordsForEnt() ;
//
//	public  boolean deleteAllEntities(Bid bid) ;
//
//	public  boolean deleteEntity(Bid bid)  ;
//
//	 public  default String getAttributes(CrudRepository<IStorable, Long> repo, IStorable storable) {
//			StringBuilder stringBuilder = new StringBuilder();
//			if(storable.getId()!=null )
//					if (repo.existsById(storable.getId()))
//				throw new IllegalArgumentException("User already exists");
//			repo.save(storable);
//			stringBuilder.append(storable.getClass().getName() + " "+ storable.getId());
//			return stringBuilder.toString();
//
//		
//	}
	//public  String updateEntity(Storable storable) ;

	 public  default String update( IStorableRepo<Storable> repo, Storable storable) {
	 if(storable.getId()!=null && repo.existsById(storable.getId()))
		
		 repo.save(storable);
		return new StringBuilder().append(storable.getClass().getName() + " "+ storable.getId()).toString();
	 }
	 
		
		public default Optional<Storable> getEntity(IStorableRepo<Storable> repo, Long id) {
			System.out.println(repo.count());
			return repo.findById(id);
		}
		
		
		public default  Iterable<Storable> getAllRecordsForEnt(IStorableRepo<Storable> repo) {
	
			return repo.findAll();
		}
	 
//		@Override
//		public String updateEntity(Lot lot) throws IllegalAccessException {
//			StringBuilder stringBuilder = new StringBuilder();
//			if(lot.getId()!=null && iLotRepo.existsById(lot.getId())) {
//				Lot lotFromRepo = iLotRepo.findById(lot.getId()).get();
//				Field[] fields = lot.getClass().getDeclaredFields();
//				for(Field field : fields) {
//				FieldUtils.writeDeclaredField(lotFromRepo, field.getName(),FieldUtils.readField(lot, field.getName().toString(), true), true);
//				}
//				iLotRepo.save(lotFromRepo);
//				stringBuilder.append(lotFromRepo.getClass().getName() + " "+ lotFromRepo.getId()).toString();
//			}
//			else 
//				stringBuilder.append(lot.getClass().getName() + " with "+ lot.getId().toString() +" not found.");
//				
//			return stringBuilder.toString();
//		}

		
//	//public String updateEntity(T  user) throws IllegalAccessException;
	//public Optional<T> getEntity(Long id) ;
}
