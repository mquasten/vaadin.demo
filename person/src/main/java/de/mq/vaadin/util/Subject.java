package de.mq.vaadin.util;

public interface Subject<Model, EventType> {

	void register(final Observer<Model, EventType> observer, final EventType event);

	void remove(final Observer<Model, EventType> observer, final EventType event);

	void remove(final Observer<Model, EventType> observer);

	void notifyObservers(final Model model, final EventType event);

}