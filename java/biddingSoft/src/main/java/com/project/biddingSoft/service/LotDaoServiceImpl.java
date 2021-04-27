/**
 * 
 */
package com.project.biddingSoft.service;


import javax.persistence.Transient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.project.biddingSoft.dao.ILotRepo;
import com.project.biddingSoft.dao.IUserRepo;
import com.project.biddingSoft.domain.BiddingSoftMapper;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.LotDTO;
import com.project.biddingSoft.domain.StorableDTO;

/**
 * @author nuchem
 *
 */
@Service
@Component
@Qualifier("LotServiceImpl")
public class LotDaoServiceImpl implements IDaoService<Lot> {
	@Autowired
	private BiddingSoftMapper lotMapper;
	@Autowired
	private  ILotRepo iLotRepo;
	@Autowired
	private  IUserRepo iUserRepo;


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
		if (!lot.getUser().createdLotscontainsLot(lot))
			lot.getUser().addLotToSet(lot);
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
