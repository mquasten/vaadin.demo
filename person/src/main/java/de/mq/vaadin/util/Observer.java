package de.mq.vaadin.util;



@FunctionalInterface
public interface Observer<EventType>  {

	void  process(final EventType event);
	
}