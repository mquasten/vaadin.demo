package de.mq.vaadin.util;

import org.springframework.stereotype.Component;

import com.vaadin.ui.Notification;

@Component
public class VaadinTemplate implements VaadinOperations {
	
	/* (non-Javadoc)
	 * @see de.mq.vaadin.util.VaadinOperations#showErrror(java.lang.String)
	 */
	@Override
	public final void showErrror(final String message) {
		Notification.show(message);
	}

}
