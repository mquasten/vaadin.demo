package de.mq.phone.web.person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;

public class ComponentTestHelper {
	
	public static void  components(final Component component, final Map<String, Component> components) {

		if (component.getCaption() != null) {
			components.put(component.getCaption(), component);
		}

		handleComponents(component, components);
	}



	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void handleComponents(final Component component, final Map components) {
		if (!(component instanceof HasComponents)) {
			return;
		}
		final Iterator<Component> it = ((HasComponents) component).iterator();
		if( it == null){
			return;
		}
		while (it.hasNext()) {
			final Component child = it.next();
			
			components(child, components);
		}
	}
	
	
	
	public static void  componentsByClass(final Component component, final Map<Class<? extends Component>, Collection<Component>> components) {
		
			if( ! components.containsKey(component.getClass())) {
				components.put(component.getClass(),  new ArrayList<Component>());
			}
			components.get(component.getClass()).add(component);
			if (!(component instanceof HasComponents)) {
				return;
			}
			final Iterator<Component> it = ((HasComponents) component).iterator();
			
			if( it == null){
				return;
			}
			while (it.hasNext()) {
				componentsByClass(it.next(), components);
			}
	}

	
}
