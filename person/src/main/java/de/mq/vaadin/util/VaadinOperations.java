package de.mq.vaadin.util;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Window;

public interface VaadinOperations {

	void showErrror(String message);

	Navigator newNavigator();


	void addWindow(final Window window);

}