package de.mq.phone.web.person;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;

import de.mq.vaadin.util.AbstractUIBeanInjector;
import de.mq.vaadin.util.ViewNav;


public class StartView extends AbstractUIBeanInjector{

	private static final long serialVersionUID = 1L;
	@Autowired
	private PersonSearchView personSearchView;
	
	@Autowired
	private UserModel userModel;
	@Autowired
	private Collection<View> views ; 
	@Autowired
	private ViewNav viewNav;
	
	@Override
	protected void init()  {
		setContent(personSearchView);		
		
		
		
		viewNav.create(personSearchView, views);
		
		if( userModel.getLocale() == null){
			userModel.setLocale(locale());
		}
		
		
		
		
	
	}
}
