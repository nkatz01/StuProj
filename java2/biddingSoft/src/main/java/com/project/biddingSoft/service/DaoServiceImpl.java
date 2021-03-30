/**
 * 
 */
package com.project.biddingSoft.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;
import java.lang.reflect.*;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.project.biddingSoft.BiddingSoftwareApplication;
import com.project.biddingSoft.dao.ILotRepo;
import com.project.biddingSoft.dao.IStorable;
import com.project.biddingSoft.domain.Lot;

/**
 * @author nuchem
 *
 */
@Service
@Component
public class DaoServiceImpl {
	private static final Logger logger = LoggerFactory.getLogger(DaoServiceImpl.class);

	public synchronized boolean persistEntity(IStorable entity) throws IllegalArgumentException {

		try {
			entity.saveToRepo();
		} catch (IllegalArgumentException e) {
			logger.info("Error is ", e);
			throw e;
		}
		return true;

	}

	public Iterable<? extends IStorable> getAllRecordsForEnt(IStorable entity) {

		return entity.findAll();
	}

	public synchronized boolean deleteAllEntities(IStorable entity) {
		try {
			StreamSupport.stream(getAllRecordsForEnt(entity).spliterator(), false)
					.forEach(l -> l.delete());
		} catch (IllegalArgumentException e) {
			logger.info("Error is ", e);
			throw e;
		}
		return getAllRecordsForEnt(entity).iterator().hasNext() == false;
	}

	public synchronized boolean deleteEntity(IStorable entity) throws IllegalArgumentException {
		try {
			entity.delete();
		} catch (IllegalArgumentException e) {
			logger.info("Error is ", e);
			throw e;
		}
		return entity.find().isEmpty(); // todo: implement thread safety in case entity is inserted in between

	}

	public Optional<? extends IStorable> getEntity(IStorable entity) {
		return entity.find();
	}

}
