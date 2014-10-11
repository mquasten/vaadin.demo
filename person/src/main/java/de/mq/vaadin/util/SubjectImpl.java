package de.mq.vaadin.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.springframework.util.Assert;




public class SubjectImpl<Model,EventType > implements Subject<Model, EventType> {
	
	private Map<EventType, Collection<Observer<EventType>>> observers = new HashMap<>(); 
	
	/* (non-Javadoc)
	 * @see de.mq.vaadin.util.Subject#register(de.mq.vaadin.util.Observer, EventType)
	 */
	@Override
	public final void register(final Observer<EventType>  observer, final EventType event) {
		Assert.notNull(event);
		Assert.notNull(observer);
		if( ! observers.containsKey(event) ) {
			observers.put(event, new HashSet<Observer<EventType>>());
		}
		final Collection<Observer<EventType>> observerList  =  observers.get(event);
		Assert.notNull(observerList);
		observerList.add(observer);
	}
	
	/* (non-Javadoc)
	 * @see de.mq.vaadin.util.Subject#remove(de.mq.vaadin.util.Observer, EventType)
	 */
	@Override
	public final void remove(final Observer<EventType>  observer, final EventType event) {
		Assert.notNull(event);
		Assert.notNull(observer);
		if( ! observers.containsKey(event)){
			return;
		}
		final Collection<Observer<EventType>> observerList  =  observers.get(event);
		observerList.remove(observer);
	}
	
	/* (non-Javadoc)
	 * @see de.mq.vaadin.util.Subject#remove(de.mq.vaadin.util.Observer)
	 */
	@Override
	public final void remove(final Observer<EventType>  observer) {
		observers.values().forEach(observerList -> observerList.remove(observer));
	}
	
	/* (non-Javadoc)
	 * @see de.mq.vaadin.util.Subject#notifyObservers(Model, EventType)
	 */
	@Override
	public final  void notifyObservers(final EventType event) {
		Assert.notNull(event);
		if( ! observers.containsKey(event) ) {
			return;
		}
		
		observers.get(event).forEach(observer -> observer.process(event));
		
	}

}
