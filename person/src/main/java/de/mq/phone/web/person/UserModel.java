package de.mq.phone.web.person;

import java.util.Collection;
import java.util.Locale;

import de.mq.vaadin.util.Subject;

public interface UserModel extends Subject<UserModel, UserModel.EventType> {
	
	public static  final String[] THEMES =  new String[] { "valo", "reindeer", "runo", "chameleon" };
	 
	public enum EventType {
		LocaleChanges
	}
	
	Locale getLocale();
	
	void setLocale(final Locale locale);
	
	Collection<Locale> getSupportedLocales();

	String getTheme();
	
	
	

	
}