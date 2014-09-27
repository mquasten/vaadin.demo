package de.mq.vaadin.util;



@FunctionalInterface
public interface Observer<Model, EventType>  {

	void  process(final Model model, final EventType event);
	
}