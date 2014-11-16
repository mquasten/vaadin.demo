package de.mq.phone.web.person;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	private static final long serialVersionUID = 1L;
	
	private final UserModel userModel;
	private final ViewNav viewNav;
	@Autowired
	MainMenuBarView(final UserModel userModel, final ViewNav viewNav) {
		this.userModel = userModel;
		this.viewNav = viewNav;
	}

	

	@PostConstruct
	void init() {
		final MenuBar menubar = new MenuBar();
		setCompositionRoot(menubar);
		final Command command = item -> {
			viewNav.navigateTo(PersonEditView.class);
		};
		userModel.register(event -> { 
			menubar.removeItems();
			
			menubar.addItem("Einstellungen", null).addItem("Addresse",  command);
			System.out.println("localize");
			
		}, UserModel.EventType.LocaleChanges);
	}

}
