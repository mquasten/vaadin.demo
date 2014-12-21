package de.mq.phone.web.person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.mq.vaadin.util.SubjectImpl;

@Component
@Scope("session")
public class UserModelImpl  extends SubjectImpl<UserModel, UserModel.EventType> implements UserModel    {

	private final Collection<Locale> supportedLocales = new ArrayList<>();
	
	private Locale locale ; 
	
	private Integer pageSize = 10 ;


	public Integer getPageSize() {
		return pageSize;
	}


	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}


	@PostConstruct
	void init() {
		supportedLocales.add(Locale.GERMAN);
		supportedLocales.add(Locale.ENGLISH);
	}
	
	
	@Override
	public final Locale getLocale() {
		return locale;
	}

	@Override         
	public final void setLocale(final Locale locale) {
		this.locale=locale;
		notifyObservers(UserModel.EventType.LocaleChanges);
	}

	@Override       
	public final Collection<Locale> getSupportedLocales() {
		return supportedLocales;
		
	}
	

}
