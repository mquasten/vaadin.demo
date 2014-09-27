package de.mq.vaadin.util;



import java.util.Locale;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.WrappedHttpSession;
import com.vaadin.ui.UI;

public abstract class AbstractUIBeanInjector  extends UI {
	
	private static final long serialVersionUID = 1L;
	
	abstract protected void init(); 
	
	
	
	private Locale locale=Locale.ENGLISH; 

	@Override
	protected final void init(final VaadinRequest request) {
		WebApplicationContextUtils.getRequiredWebApplicationContext(((WrappedHttpSession) request.getWrappedSession()).getHttpSession().getServletContext()).getAutowireCapableBeanFactory().autowireBean(this);
		if( request.getLocale() != null){
		    locale = request.getLocale();
		}
		
		init();
		
		
	}
	
	protected final Locale locale() {
		return locale;
	}
	
	
	

	



	

}
