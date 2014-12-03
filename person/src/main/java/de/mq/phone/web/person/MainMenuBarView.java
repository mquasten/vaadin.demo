package de.mq.phone.web.person;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;

import de.mq.vaadin.util.ViewNav;


@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class MainMenuBarView  extends CustomComponent{
	
	static final String I18N_MENU_ADDRESS = "settings_address";

	static final String I18N_NENU_SETTINGS = "menu_settings";

	private static final long serialVersionUID = 1L;
	
	private final UserModel userModel;
	private final ViewNav viewNav;
	private final PersonEditController personEditController;
	private final MessageSource messageSource;
	
	@Autowired
	MainMenuBarView(final UserModel userModel, final ViewNav viewNav, final PersonEditController personEditController, final MessageSource messageSource) {
		this.userModel = userModel;
		this.viewNav = viewNav;
		this.personEditController=personEditController;
		this.messageSource=messageSource;
	}

	

	@PostConstruct
	void init() {
		setLocale(userModel.getLocale());
		final MenuBar menubar = new MenuBar();
		menubar.setCaption(getString(I18N_NENU_SETTINGS));
		setCompositionRoot(menubar);
		final Command command = item -> {
			viewNav.navigateTo(PersonEditView.class, personEditController.defaultPerson().id());
		};
		userModel.register(event -> { 
			
			menubar.removeItems();
			
			menubar.addItem(getString(I18N_NENU_SETTINGS), null).addItem(getString(I18N_MENU_ADDRESS),  command);
			
			
		}, UserModel.EventType.LocaleChanges);
	}
	
	private String getString(final String key) {
		return messageSource.getMessage(key, null, getLocale());
	}

}
