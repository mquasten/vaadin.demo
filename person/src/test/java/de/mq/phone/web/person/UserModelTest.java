package de.mq.phone.web.person;

import java.util.Locale;





import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import de.mq.phone.web.person.UserModel.EventType;
import de.mq.vaadin.util.Observer;

public class UserModelTest {
	
	private final UserModel userModel = new UserModelImpl();
	
	@Test
	public final void supportedLocales() {
		Assert.assertTrue(userModel.getSupportedLocales().isEmpty());
		((UserModelImpl)userModel).init();
		Assert.assertEquals(2, userModel.getSupportedLocales().size());
		Assert.assertTrue(userModel.getSupportedLocales().contains(Locale.GERMAN));
		Assert.assertTrue(userModel.getSupportedLocales().contains(Locale.ENGLISH));
	}
	
	@Test
	public final void changeLocale() {
		@SuppressWarnings("unchecked")
		Observer<UserModel, UserModel.EventType> observer = Mockito.mock(Observer.class);
		userModel.register(observer, UserModel.EventType.LocaleChanges);
		Assert.assertNull(userModel.getLocale());
		userModel.setLocale(Locale.GERMAN);
		Assert.assertEquals(Locale.GERMAN, userModel.getLocale());
	}
	
	@Test
	public final void eventType() {
		Assert.assertEquals(1, EventType.values().length);
		for(UserModel.EventType eventType : EventType.values()){
			Assert.assertEquals(eventType, EventType.valueOf(eventType.name()));
		}
	}

}
