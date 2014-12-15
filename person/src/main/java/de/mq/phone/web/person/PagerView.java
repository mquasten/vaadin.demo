package de.mq.phone.web.person;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class PagerView extends HorizontalLayout{

	
	private static final long serialVersionUID = 1L;
	
	
 @PostConstruct
 final  void init() {
	 
	 
	 
	 final Button nextButton = new Button();
	 nextButton.setIcon(new ThemeResource("arrow_right.png"));
	
	 final Button nextEndButton = new Button();
	 addComponent(nextButton);
	 nextEndButton.setIcon(new ThemeResource("arrow_in.png"));
	// nextEndButton.setStyleName(BaseTheme.BUTTON_LINK);
	 addComponent(nextEndButton);
	 final Label pageLabel = new Label();
	 pageLabel.setValue("Seite 1/1");
	 addComponent(pageLabel);
	 setComponentAlignment(pageLabel, Alignment.MIDDLE_CENTER);
	 final Button backButton = new Button();
	 backButton.setIcon(new ThemeResource("arrow_left.png"));
	 addComponent(backButton);
	 final Button backEndButton = new Button();
	 backEndButton.setIcon(new ThemeResource("arrow_out.png"));
	 addComponent(backEndButton);
	 
 }

}
