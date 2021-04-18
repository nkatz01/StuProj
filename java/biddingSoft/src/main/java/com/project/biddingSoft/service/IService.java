/**
 * 
 */
package com.project.biddingSoft.service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.reflect.FieldUtils;
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
	public default  void deleteAllEntities(IStorableRepo<Storable> repo) {
		repo.deleteAll();
		
	}
//
public default void deleteEntity(IStorableRepo<Storable> repo, Long id) {
	  repo.deleteById(id);
	
}  
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
	 public  String updateEntity(Storable storable) ;

//	 public  default String update( IStorableRepo<Storable> repo, Storable storable) {
//	 if(storable.getId()!=null && repo.existsById(storable.getId()))
//		
//		 repo.save(storable);
//		return new StringBuilder().append(storable.getClass().getName() + " "+ storable.getId()).toString();
//	 }
	 
		
		

	 
		
		public default String update(IStorableRepo<Storable> repo, Storable storable) throws IllegalAccessException {
			StringBuilder stringBuilder = new StringBuilder();
			if(storable.getId()!=null && repo.existsById(storable.getId())) {
				Storable strblFromRepo = repo.findById(storable.getId()).get();
				Field[] fields = strblFromRepo.getClass().getDeclaredFields();
				try {
					for(Field field : fields) {
						//if(field.getName()!="id") {
						if(FieldUtils.readField(storable, field.getName().toString(), true)!=null)
						FieldUtils.writeDeclaredField(strblFromRepo, field.getName(),FieldUtils.readField(storable, field.getName().toString(), true), true);
						//}
						}
					repo.save(strblFromRepo);
					stringBuilder.append(strblFromRepo.getClass().getName() + " "+ strblFromRepo.getId()).toString();
					
				} catch (Exception e) {
					stringBuilder.append("/n" + e.getMessage() +"/n");
				}
			}
			else 
				stringBuilder.append(storable.getClass().getName() + " with "+ storable.getId().toString() +" not found.");
				
			return stringBuilder.toString();
		}

		
//	//public String updateEntity(T  user) throws IllegalAccessException;

}
