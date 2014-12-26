package de.mq.phone.web.person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.mq.vaadin.util.SubjectImpl;

@Component
@Scope("session")
public class UserModelImpl  extends SubjectImpl<UserModel, UserModel.EventType> implements UserModel    {

	static final int DEFAULT_PAGE_SIZE = 10;

	private final Collection<Locale> supportedLocales = new ArrayList<>();
	
	private Locale locale ; 
	
	private int pageSize = DEFAULT_PAGE_SIZE ;


	public final Integer getPageSize() {
		return pageSize;
	}


	public final void setPageSize(final Integer pageSize) {
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
	
	@Override   
	public final Collection<Integer> getPageSizes() {
	 final Collection<Integer> pageSize = new ArrayList<>();
	 pageSize.add(3);
	 pageSize.add(5);
	 pageSize.add(10);
	 pageSize.add(20);
	 pageSize.add(50);
	 pageSize.add(100);
    return Collections.unmodifiableCollection(pageSize);
	}

}
