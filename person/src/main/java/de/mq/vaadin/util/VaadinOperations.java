package de.mq.vaadin.util;

import com.vaadin.navigator.Navigator;

public interface VaadinOperations {

	void showErrror(String message);

	Navigator newNavigator();

}