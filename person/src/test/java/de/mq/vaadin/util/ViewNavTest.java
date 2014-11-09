package de.mq.vaadin.util;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.aop.SpringProxy;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.StringUtils;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;

public class ViewNavTest {
	
	
	private static final String ID = "19680528";
	private final ViewNav viewNav = new SimpleViewNavImpl();
	private final VaadinOperations vaadinOperations = Mockito.mock(VaadinOperations.class);
	private final Navigator navigator = Mockito.mock(Navigator.class);
	
	@Test
	public final void create() {
		Mockito.when(vaadinOperations.newNavigator()).thenReturn(navigator); 
		final View root = Mockito.mock(View.class);
		final Collection<View> views = new ArrayList<>(); 
		views.add(root);
		final View otherView = Mockito.mock(View.class, Mockito.withSettings().extraInterfaces(SpringProxy.class));
		views.add(otherView);
		viewNav.create(root, views, vaadinOperations);
		
		Mockito.verify(navigator, Mockito.times(1)).addView(StringUtils.uncapitalize(root.getClass().getSimpleName()), root);
		Mockito.verify(navigator, Mockito.times(1)).addView("object" , otherView);
	}
	
	@Test
	public final void navigateTo() {
		ReflectionTestUtils.setField(viewNav, "navigator", navigator);
		viewNav.navigateTo(View.class , ID);
		Mockito.verify(navigator).navigateTo(String.format("view/%s", ID));
	}
	
	@Test
	public final void navigateToWithOutParams() {
		ReflectionTestUtils.setField(viewNav, "navigator", navigator);
		viewNav.navigateTo(View.class);
		Mockito.verify(navigator).navigateTo("view");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void navigateToCGLIB() {
		viewNav.navigateTo(Mockito.mock(View.class).getClass());;
	}

}
