/**
 * 
 */
package com.project.biddingSoft.service;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

import javax.persistence.Transient;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.lang.reflect.*;
import java.time.Clock;
import java.time.Duration;
import java.time.ZoneId;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.biddingSoft.BiddingSoftwareApplication;
import com.project.biddingSoft.dao.ILotRepo;

import com.project.biddingSoft.dao.IStorableRepo;
import com.project.biddingSoft.dao.IUserRepo;
import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.BiddingSoftMapper;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.LotDTO;
import com.project.biddingSoft.domain.Storable;
import com.project.biddingSoft.domain.StorableDTO;
import com.project.biddingSoft.domain.User;
import com.project.biddingSoft.domain.UserDTO;

/**
 * @author nuchem
 *
 */
@Service
@Component
@Qualifier("LotServiceImpl")
public class LotServiceImpl implements IService<Lot> {
	private static final Logger logger = LoggerFactory.getLogger(LotServiceImpl.class);
	@Autowired
	private BiddingSoftMapper lotMapper;
	@Transient
	@Autowired
	private static ILotRepo iLotRepo;
	@Transient
	@Autowired
	private static IUserRepo iUserRepo;

	@Autowired
	public void setILotRepo(ILotRepo ilotrepo) {
		iLotRepo = ilotrepo;
	}

	@Autowired
	public void setIUserRepo(IUserRepo iuserRepo) {
		iUserRepo = iuserRepo;
	}

	@Override
	public String updateEntity(StorableDTO lotDto) {
		String mesg = "" + Lot.class.getName() + " " + lotDto.getId();
		try {
			Lot lot = iLotRepo.findById(lotDto.getId()).get();
			lotMapper.updateLotFromDto((LotDTO) lotDto, lot);
			iLotRepo.save(lot);
			mesg += " successfully updated";
		} catch (Exception e) {
			mesg += " could not be updated: " + e.getMessage();
		}
		return mesg;
	}

	@Override
	public String persistEntity(Lot lot) {
		StringBuilder stringBuilder = new StringBuilder();
		// reduntant check, can just remove
		if (!lot.getUser().createdLotscontainsLot(lot))
			lot.getUser().addLotToList(lot);
		if (lot.getUser().getId() == null) {
			iUserRepo.save(lot.getUser());
			stringBuilder.append(lot.getUser().getClass().getName() + " " + lot.getUser().getId());

		}
		else {
			iLotRepo.save(lot);
		}
		stringBuilder.append("\n" + lot.getClass().getName() + " " + lot.getId());// gets saved via cascade, in the	 first case.
		return stringBuilder.toString();

	}

}
