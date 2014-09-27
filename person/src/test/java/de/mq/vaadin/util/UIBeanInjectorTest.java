package de.mq.vaadin.util;

import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.WrappedHttpSession;


public class UIBeanInjectorTest {
	
	final VaadinRequest request = Mockito.mock(VaadinRequest.class);
	final WrappedHttpSession wrappedSession = Mockito.mock(WrappedHttpSession.class);
	final AbstractUIBeanInjector uiBeanInjector = Mockito.mock(AbstractUIBeanInjector.class);
	final HttpSession httpSession = Mockito.mock(HttpSession.class);
	final ServletContext servletContext = Mockito.mock(ServletContext.class);
	final WebApplicationContext ctx = Mockito.mock(WebApplicationContext.class);
	final AutowireCapableBeanFactory autowireCapableBeanFactory = Mockito.mock(AutowireCapableBeanFactory.class);
	
	@Before
	public final void setup(){
		Mockito.when(request.getWrappedSession()).thenReturn(wrappedSession);
		Mockito.when(httpSession.getServletContext()).thenReturn(servletContext);
		Mockito.when(wrappedSession.getHttpSession()).thenReturn(httpSession);
		Mockito.when(servletContext.getAttribute( WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE)).thenReturn(ctx);
		Mockito.when(ctx.getAutowireCapableBeanFactory()).thenReturn(autowireCapableBeanFactory);
	}
	
	@Test
	public final void initWithoutLocale() {
	
		
		uiBeanInjector.init(request);
		
		Mockito.verify(autowireCapableBeanFactory, Mockito.times(1)).autowireBean(uiBeanInjector);
		Mockito.verify(uiBeanInjector, Mockito.times(1)).init();
		Assert.assertNull(uiBeanInjector.locale());
	}
	
	@Test
	public final void initWithLocale() {
		Mockito.when(request.getLocale()).thenReturn(Locale.GERMAN);
		uiBeanInjector.init(request);
		Assert.assertEquals(Locale.GERMAN, uiBeanInjector.locale());
		
	}
	
	
}
