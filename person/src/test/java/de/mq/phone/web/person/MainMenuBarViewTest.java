package de.mq.phone.web.person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.Window;

import de.mq.phone.domain.person.Person;
import de.mq.phone.web.person.UserModel.EventType;
import de.mq.vaadin.util.Observer;
import de.mq.vaadin.util.VaadinOperations;
import de.mq.vaadin.util.ViewNav;

public class MainMenuBarViewTest {

	private final UserModel userModel = Mockito.mock(UserModel.class);
	private final ViewNav viewNav = Mockito.mock(ViewNav.class);
	private final PersonEditController personEditController = Mockito.mock(PersonEditController.class);
	private final MessageSource messageSource = Mockito.mock(MessageSource.class);
	
	private final PersonSearchController personSearchController = Mockito.mock(PersonSearchController.class);
	
	private final PersonSearchModel personSearchModel = Mockito.mock(PersonSearchModel.class);

	private  VaadinOperations vaadinOperations = Mockito.mock(VaadinOperations.class);
	private final MainMenuBarView mainMenuBarView = new MainMenuBarView(vaadinOperations, userModel, viewNav, personEditController,personSearchController, personSearchModel, messageSource);

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
		Mockito.when(messageSource.getMessage(MainMenuBarView.I18N_MENU_USER, null,  Locale.GERMAN)).thenReturn(MainMenuBarView.I18N_MENU_USER);
		Mockito.when(messageSource.getMessage(MainMenuBarView.I18N_PAGE_SIZE_BOX, null,  Locale.GERMAN)).thenReturn(MainMenuBarView.I18N_PAGE_SIZE_BOX);
		Mockito.when(messageSource.getMessage(MainMenuBarView.I18N_PAGE_SIZE_SAVE, null,  Locale.GERMAN)).thenReturn(MainMenuBarView.I18N_PAGE_SIZE_SAVE);
		Mockito.when(messageSource.getMessage(MainMenuBarView.I18N_LANGUAGE_BOX, null,  Locale.GERMAN)).thenReturn(MainMenuBarView.I18N_LANGUAGE_BOX);
		
		Mockito.doAnswer(invocation -> {
			observers.put((EventType) invocation.getArguments()[1], (Observer<EventType>) invocation.getArguments()[0]);
			return null;
		}).when(userModel).register((Observer<EventType>) Mockito.any(Observer.class), Mockito.any(UserModel.EventType.class));
	}

	@Test
	public final void initDefaultAddress() {
		
		mainMenuBarView.init();
		observers.get(EventType.LocaleChanges).process(EventType.LocaleChanges);
		ComponentTestHelper.components(mainMenuBarView, components);
		final MenuBar menu = (MenuBar) components.get(MainMenuBarView.I18N_NENU_SETTINGS);
		Assert.assertEquals(1, menu.getItems().size());
		Assert.assertEquals(MainMenuBarView.I18N_NENU_SETTINGS, menu.getItems().get(0).getText());
		Assert.assertNull(menu.getItems().get(0).getCommand());

		Assert.assertEquals(2, menu.getItems().get(0).getChildren().size());
		Assert.assertEquals(MainMenuBarView.I18N_MENU_ADDRESS, menu.getItems().get(0).getChildren().get(0).getText());

		final Command command = menu.getItems().get(0).getChildren().get(0).getCommand();
		command.menuSelected(menu.getItems().get(0).getChildren().get(0));

		Mockito.verify(viewNav, Mockito.times(1)).navigateTo(PersonEditView.class, person.id());
		
		

	}
	
	@Test()
	public final void initPageSize() {
		Mockito.when(userModel.getPageSize()).thenReturn(10);
		Collection<Integer> pageSizes = new ArrayList<>();
		pageSizes.add(userModel.getPageSize());
		pageSizes.add(2*userModel.getPageSize());
		Mockito.when(userModel.getPageSizes()).thenReturn(pageSizes);
		final Collection<Locale> locales = new ArrayList<>();
		locales.add(Locale.GERMAN);
		locales.add(Locale.ENGLISH);
		Mockito.when(userModel.getSupportedLocales()).thenReturn(locales);
		
		mainMenuBarView.init();
		observers.get(EventType.LocaleChanges).process(EventType.LocaleChanges);
		ComponentTestHelper.components(mainMenuBarView, components);
		final MenuBar menu = (MenuBar) components.get(MainMenuBarView.I18N_NENU_SETTINGS);
		Assert.assertEquals(1, menu.getItems().size());
		Assert.assertEquals(MainMenuBarView.I18N_NENU_SETTINGS, menu.getItems().get(0).getText());
		Assert.assertNull(menu.getItems().get(0).getCommand());

		
		Assert.assertEquals(2, menu.getItems().get(0).getChildren().size());
		final Command settingsCommand = menu.getItems().get(0).getChildren().get(1).getCommand();
	
		settingsCommand.menuSelected(menu.getItems().get(0).getChildren().get(1));
		final ArgumentCaptor<Window> windowCaptor = ArgumentCaptor.forClass(Window.class);
		Mockito.verify(vaadinOperations, Mockito.times(1)).addWindow(windowCaptor.capture());
		final Window window = windowCaptor.getValue();
		
		final Map<String, Component> components = new HashMap<>(); 
		ComponentTestHelper.components(window, components);
		
		final ComboBox box = (ComboBox) components.get(MainMenuBarView.I18N_PAGE_SIZE_BOX);
		Assert.assertEquals(userModel.getPageSize(), box.getValue());
		Assert.assertEquals(2, box.getItemIds().size());
		Assert.assertEquals(pageSizes,  box.getItemIds());
		
		
		final ComboBox languageBox = (ComboBox) components.get(MainMenuBarView.I18N_LANGUAGE_BOX);
		Assert.assertEquals(Locale.GERMAN,languageBox.getValue());
		
		locales.forEach(locale -> Assert.assertEquals(locale.getDisplayLanguage(), languageBox.getItemCaption(locale)));

		Mockito.when(userModel.pageSizeChanged(userModel.getPageSize())).thenReturn(true);
		
		Assert.assertEquals(locales, languageBox.getItemIds());
		
		
		final Button button = (Button) components.get(MainMenuBarView.I18N_PAGE_SIZE_SAVE);
		
		@SuppressWarnings("unchecked")
		final Collection<ClickListener> listeners = (Collection<ClickListener>) button.getListeners(ClickEvent.class);
		Assert.assertEquals(1, listeners.size());
		final ClickEvent event = Mockito.mock(ClickEvent.class);
		listeners.iterator().next().buttonClick(event);
		
		Mockito.verify(personSearchController, Mockito.times(1)).assignPersons(personSearchModel, userModel.getPageSize() );
		
		Mockito.when(userModel.getPageSize()).thenReturn(5);
		
		
		
		
		Mockito.when(userModel.pageSizeChanged(Mockito.anyInt())).thenReturn(true);
		Mockito.doThrow(new IllegalArgumentException("Don't worry only for test")).when(personSearchController).assignPersons(personSearchModel, 5);
		listeners.iterator().next().buttonClick(event);
		
		Mockito.reset(personSearchController);
		Mockito.when(userModel.pageSizeChanged(Mockito.anyInt())).thenReturn(false);
		listeners.iterator().next().buttonClick(event);
		Mockito.verify(personSearchController, Mockito.times(0)).assignPersons(Mockito.any(PersonSearchModel.class), Mockito.anyInt());
	}

}
