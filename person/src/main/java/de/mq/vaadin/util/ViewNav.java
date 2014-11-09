package de.mq.vaadin.util;

import java.util.Collection;

import com.vaadin.navigator.View;

public interface ViewNav {

	public abstract void create(View root, Collection<View> views, final VaadinOperations vaadinOperations);

	public abstract void navigateTo(Class<? extends View> clazz, final String ... params );

}