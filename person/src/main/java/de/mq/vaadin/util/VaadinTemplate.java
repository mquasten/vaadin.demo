package de.mq.vaadin.util;

import org.springframework.stereotype.Component;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

@Component
public class VaadinTemplate implements VaadinOperations {
	
	/* (non-Javadoc)
	 * @see de.mq.vaadin.util.VaadinOperations#showErrror(java.lang.String)
	 */
	@Override
	public final void showErrror(final String message) {
		Notification.show(message);
	}
	
	@Override
	public final Navigator newNavigator() {
		return new Navigator(UI.getCurrent(), UI.getCurrent());
	}
	
	@Override
	public void  addWindow(final Window window) {
		 UI.getCurrent().addWindow(window);;
	}

}
