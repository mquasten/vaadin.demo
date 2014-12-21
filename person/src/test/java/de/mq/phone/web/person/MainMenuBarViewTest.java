package de.mq.phone.web.person;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;

import com.vaadin.ui.Component;

import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;

import de.mq.phone.domain.person.Person;
import de.mq.phone.web.person.UserModel.EventType;
import de.mq.vaadin.util.Observer;
import de.mq.vaadin.util.ViewNav;

public class MainMenuBarViewTest {

	private final UserModel userModel = Mockito.mock(UserModel.class);
	private final ViewNav viewNav = Mockito.mock(ViewNav.class);
	private final PersonEditController personEditController = Mockito.mock(PersonEditController.class);
	private final MessageSource messageSource = Mockito.mock(MessageSource.class);
	
	private final PersonSearchController personSearchController = Mockito.mock(PersonSearchController.class);
	
	private final PersonSearchModel personSearchView = Mockito.mock(PersonSearchModel.class);

	private final MainMenuBarView mainMenuBarView = new MainMenuBarView(userModel, viewNav, personEditController,personSearchController, personSearchView, messageSource);

	private final Map<EventType, Observer<EventType>> observers = new HashMap<>();

	private final Map<String, Component> components = new HashMap<>();

	private final Person person = Mockito.mock(Person.class);

	@SuppressWarnings("unchecked")
	@Before
	public final void setup() {
		Mockito.when(person.id()).thenReturn("19680528");
		Mockito.when(personEditController.defaultPerson()).thenReturn(person);
		Mockito.when(userModel.getLocale()).thenReturn(Locale.GERMAN);
		Mockito.when(messageSource.getMessage(MainMenuBarView.I18N_NENU_SETTINGS, null, Locale.GERMAN)).thenReturn(MainMenuBarView.I18N_NENU_SETTINGS);
		Mockito.when(messageSource.getMessage(MainMenuBarView.I18N_MENU_ADDRESS, null, Locale.GERMAN)).thenReturn(MainMenuBarView.I18N_MENU_ADDRESS);
		Mockito.when(messageSource.getMessage(MainMenuBarView.I18N_MENU_USER, null, Locale.GERMAN)).thenReturn(MainMenuBarView.I18N_MENU_USER);
		
		
		Mockito.doAnswer(invocation -> {
			observers.put((EventType) invocation.getArguments()[1], (Observer<EventType>) invocation.getArguments()[0]);
			return null;
		}).when(userModel).register((Observer<EventType>) Mockito.any(Observer.class), Mockito.any(UserModel.EventType.class));
	}

	@Test
	public final void init() {
		
		mainMenuBarView.init();
		observers.get(EventType.LocaleChanges).process(EventType.LocaleChanges);
		ComponentTestHelper.components(mainMenuBarView, components);
		MenuBar menu = (MenuBar) components.get(MainMenuBarView.I18N_NENU_SETTINGS);
		Assert.assertEquals(1, menu.getItems().size());
		Assert.assertEquals(MainMenuBarView.I18N_NENU_SETTINGS, menu.getItems().get(0).getText());
		Assert.assertNull(menu.getItems().get(0).getCommand());

		Assert.assertEquals(2, menu.getItems().get(0).getChildren().size());
		Assert.assertEquals(MainMenuBarView.I18N_MENU_ADDRESS, menu.getItems().get(0).getChildren().get(0).getText());

		final Command command = menu.getItems().get(0).getChildren().get(0).getCommand();
		command.menuSelected(menu.getItems().get(0).getChildren().get(0));

		Mockito.verify(viewNav, Mockito.times(1)).navigateTo(PersonEditView.class, person.id());

	}

}
