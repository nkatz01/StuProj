package com.project.biddingSoft.service;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public    class ExceptionsCreateor {
	
	private ExceptionsCreateor() {//so that we can't directly instanciate this
		 
	}
	
	@Bean
	public static ExceptionsCreateor BiddingSoftExceptionsFactory() {//this works for both, default as well as parameterized constructors 
		return new ExceptionsCreateor();
	}
	
  public class LotHasEndedException extends BiddingSoftExceptions {
	private static final String throwableMsg = "lot endTime is qual or before current time";
	private static final int id = 1;
	
	public LotHasEndedException() {
		super(id, new Throwable(throwableMsg));
	}

}
  
  public class BiddingSoftExceptions extends RuntimeException{
  
	private BiddingSoftExceptions(String errorMessage, Throwable err) {
		super(errorMessage, err);
	}
	
	
	private BiddingSoftExceptions(int id, Throwable err) {
		
		super(String.valueOf(id), err);
	}
  }
//  public class LotHasEndedException extends BiddingSoftException {
//		
//		
//	public LotHasEndedException(String errorMessage, Throwable err) {
//		super(errorMessage, err);
//	}
//
//}
  
  
}
