package de.mq.phone.domain.person.support;


public interface ModifyablePaging  extends Paging {
	 boolean  inc();


	boolean  dec(); 
	
	boolean  first(); 
	
	boolean last(); 
}
