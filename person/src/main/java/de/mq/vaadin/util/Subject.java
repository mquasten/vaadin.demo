package de.mq.vaadin.util;



public interface Subject<Model, EventType> {

	void register(final Observer<EventType> observer, final EventType event);

	void remove(final Observer<EventType> observer, final EventType event);

	void remove(final Observer<EventType> observer);

	void notifyObservers(final EventType event);


}