package com.project.biddingSoft.testServices;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.project.biddingSoft.service.StorableService;
@Component
public class StorableTestService {
	public UUID newUUID() {
		return new UUID(StorableService.get64MostSignificantBitsForVersion1(), StorableService.get64LeastSignificantBitsForVersion1());
	}
}
