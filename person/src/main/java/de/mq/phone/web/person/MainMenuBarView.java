package de.mq.phone.web.person;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.mq.vaadin.util.VaadinOperations;
import de.mq.vaadin.util.ViewNav;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class MainMenuBarView extends CustomComponent {

	static final String I18N_MENU_ADDRESS = "settings_address";

	static final String I18N_MENU_USER = "settings_user";
	static final String I18N_PAGE_SIZE_SAVE = "settings_page_size_save";
	static final String I18N_PAGE_SIZE_BOX = "settings_page_size_box";

	static final String I18N_NENU_SETTINGS = "menu_settings";

	private static final long serialVersionUID = 1L;

 static final String I18N_LANGUAGE_BOX = "settings_language_box";

	private final UserModel userModel;
	private final ViewNav viewNav;
	private final PersonEditController personEditController;
	private final PersonSearchController personSearchController;
	private final PersonSearchModel personSearchModel;
	private final MessageSource messageSource;

	private final VaadinOperations vaadinOperations;

	@Autowired
	MainMenuBarView(final VaadinOperations vaadinOperations, final UserModel userModel, final ViewNav viewNav, final PersonEditController personEditController, final PersonSearchController personSearchController, final PersonSearchModel personSearchModel, final MessageSource messageSource) {
		this.vaadinOperations = vaadinOperations;
		this.userModel = userModel;
		this.viewNav = viewNav;
		this.personEditController = personEditController;
		this.personSearchController = personSearchController;
		this.personSearchModel = personSearchModel;
		this.messageSource = messageSource;
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

		Window dialog = new Window(getString(I18N_MENU_USER));

		final VerticalLayout content = new VerticalLayout();
		final FormLayout formLayout = new FormLayout();
		content.addComponent(formLayout);
		final ComboBox comboBox = new ComboBox();
		comboBox.setNewItemsAllowed(false);
		comboBox.setInvalidAllowed(false);
		comboBox.setNullSelectionAllowed(false);

		comboBox.addItems(userModel.getPageSizes());
		formLayout.addComponent(comboBox);

		final ComboBox languageBox = new ComboBox();
		formLayout.addComponent(languageBox);
		languageBox.setNullSelectionAllowed(false);

		languageBox.setNewItemsAllowed(false);

		languageBox.setContainerDataSource(new BeanItemContainer<Locale>(Locale.class, userModel.getSupportedLocales()));

		content.setMargin(true);
		dialog.setContent(content);
		dialog.setModal(true);
		dialog.setResizable(false);

		final BeanFieldGroup<UserModel> binder = new BeanFieldGroup<>(UserModel.class);
		binder.setBuffered(true);
		binder.setItemDataSource(userModel);
		binder.bind(comboBox, "pageSize");

		binder.bind(languageBox, "locale");

		final Button okButton = new Button();
		content.addComponent(okButton);
		content.setComponentAlignment(okButton, Alignment.BOTTOM_RIGHT);
		dialog.center();
		okButton.addClickListener(event -> {
			try {
				
				final Number pagesize = userModel.getPageSize();
				binder.commit();
				dialog.close();
				if( userModel.pageSizeChanged(pagesize.intValue())) {
					personSearchController.assignPersons(personSearchModel, userModel.getPageSize());
				}
				

			} catch (final Exception e) {
				
			}
		});

		userModel.register(event -> {

			menubar.removeItems();

			final MenuItem settings = menubar.addItem(getString(I18N_NENU_SETTINGS), null);
			settings.addItem(getString(I18N_MENU_ADDRESS), command);
			settings.addItem(getString(I18N_MENU_USER), item -> vaadinOperations.addWindow(dialog));
			comboBox.setCaption(getString(I18N_PAGE_SIZE_BOX));
			okButton.setCaption(getString(I18N_PAGE_SIZE_SAVE));
			languageBox.setCaption(getString(I18N_LANGUAGE_BOX));
			languageBox.getItemIds().forEach(itemId -> languageBox.setItemCaption(itemId, ((Locale) itemId).getDisplayLanguage(userModel.getLocale())));


		}, UserModel.EventType.LocaleChanges);
	}

	private String getString(final String key) {
		return messageSource.getMessage(key, null, getLocale());
	}

}
