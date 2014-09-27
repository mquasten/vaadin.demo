package de.mq.vaadin.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.springframework.util.Assert;




public class SubjectImpl<Model,EventType > implements Subject<Model, EventType> {
	
	private Map<EventType, Collection<Observer<Model, EventType>>> observers = new HashMap<>(); 
	
	/* (non-Javadoc)
	 * @see de.mq.vaadin.util.Subject#register(de.mq.vaadin.util.Observer, EventType)
	 */
	@Override
	public final void register(final Observer<Model, EventType>  observer, final EventType event) {
		Assert.notNull(event);
		Assert.notNull(observer);
		if( ! observers.containsKey(event) ) {
			observers.put(event, new HashSet<Observer<Model, EventType>>());
		}
		final Collection<Observer<Model, EventType>> observerList  =  observers.get(event);
		Assert.notNull(observerList);
		observerList.add(observer);
	}
	
	/* (non-Javadoc)
	 * @see de.mq.vaadin.util.Subject#remove(de.mq.vaadin.util.Observer, EventType)
	 */
	@Override
	public final void remove(final Observer<Model, EventType>  observer, final EventType event) {
		Assert.notNull(event);
		Assert.notNull(observer);
		if( ! observers.containsKey(event)){
			return;
		}
		final Collection<Observer<Model, EventType>> observerList  =  observers.get(event);
		observerList.remove(observer);
	}
	
	/* (non-Javadoc)
	 * @see de.mq.vaadin.util.Subject#remove(de.mq.vaadin.util.Observer)
	 */
	@Override
	public final void remove(final Observer<Model, EventType>  observer) {
		for(final Collection<Observer<Model, EventType>> observerList : observers.values()){
			observerList.remove(observer);
		}
	}
	
	/* (non-Javadoc)
	 * @see de.mq.vaadin.util.Subject#notifyObservers(Model, EventType)
	 */
	@Override
	public final  void notifyObservers(final Model model, final EventType event) {
		Assert.notNull(event);
		Assert.notNull(model);
		if( ! observers.containsKey(event) ) {
			return;
		}
		for(final Observer<Model, EventType> observer : observers.get(event)){
			observer.process(model, event);;
		}
	}

}
