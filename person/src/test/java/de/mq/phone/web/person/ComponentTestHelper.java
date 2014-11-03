package de.mq.phone.web.person;

import java.util.Iterator;
import java.util.Map;

import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;

public class ComponentTestHelper {
	
	public static void  components(final Component component, final Map<String, Component> components) {

		if (component.getCaption() != null) {
			components.put(component.getCaption(), component);
		}

		if (!(component instanceof HasComponents)) {
			return;
		}
		final Iterator<Component> it = ((HasComponents) component).iterator();

		while (it.hasNext()) {
			final Component child = it.next();
			
			components(child, components);
		}
	}

}
