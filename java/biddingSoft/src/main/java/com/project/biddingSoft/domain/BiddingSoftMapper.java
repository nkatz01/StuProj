package com.project.biddingSoft.domain;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import com.project.biddingSoft.service.UserServiceImpl;
@Mapper(componentModel = "spring")
public interface BiddingSoftMapper {
	
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, 
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
	void updateUserFromDto(UserDTO userDto, @MappingTarget User user);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, 
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void updateLotFromDto(LotDTO lotDto, @MappingTarget Lot lot);
	
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, 
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
	void updateBidFromDto(BidDTO bidDto, @MappingTarget Bid bid);
		
	
}
