package de.mq.phone.web.person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import com.vaadin.navigator.View;

import de.mq.vaadin.util.VaadinOperations;
import de.mq.vaadin.util.ViewNav;

public class StartViewTest {
	
	final StartView startView = new StartView();
	final PersonSearchView personSearchView = Mockito.mock(PersonSearchView.class);
	final UserModel userModel = Mockito.mock(UserModel.class);
	final ViewNav viewNav = Mockito.mock(ViewNav.class);
	final Collection<View> views = new ArrayList<>();
	final VaadinOperations vaadinOperations = Mockito.mock(VaadinOperations.class);
	
	@Before
	public final void setup() {
		ReflectionTestUtils.setField(startView, "personSearchView", personSearchView);
		ReflectionTestUtils.setField(startView, "userModel", userModel);
		ReflectionTestUtils.setField(startView, "locale", Locale.GERMAN);
		ReflectionTestUtils.setField(startView, "viewNav", viewNav);
		ReflectionTestUtils.setField(startView, "views", views);
		ReflectionTestUtils.setField(startView, "vaadinOperations", vaadinOperations);
	}
	
	@Test
	public final void init() {
		
		
		
		
		Assert.assertNull(startView.getContent());
		startView.init();
		Assert.assertEquals(personSearchView, startView.getContent());
		Mockito.verify(userModel, Mockito.times(1)).setLocale(Locale.GERMAN);
		Mockito.verify(viewNav,  Mockito.times(1)).create(personSearchView, views, vaadinOperations);
		
	}
	@Test
	public final void initLocaleAssined() {
		ReflectionTestUtils.setField(startView, "personSearchView", personSearchView);
		ReflectionTestUtils.setField(startView, "userModel", userModel);
		ReflectionTestUtils.setField(startView, "locale", Locale.GERMAN);
		Mockito.when(userModel.getLocale()).thenReturn(Locale.ENGLISH);
		startView.init();
		
		Mockito.verify(userModel, Mockito.times(0)).setLocale(Matchers.any(Locale.class));
		Mockito.verify(viewNav,  Mockito.times(1)).create(personSearchView, views, vaadinOperations);
		
	}

}
