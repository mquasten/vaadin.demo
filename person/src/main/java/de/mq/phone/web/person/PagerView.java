package de.mq.phone.web.person;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
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

	
 static final String FIRST_BUTTON_ICON = "arrow_out.png";

 static final String BACK_BUTTON_ICON = "arrow_left.png";

 static final String LAST_BUTTON_ICON = "arrow_in.png";

 static final String NEXT_BUTTON_ICON = "arrow_right.png";

 static final long serialVersionUID = 1L;
	
 private final PersonSearchModel personSearchModel;
 private final PersonSearchController personSearchController;
 @Autowired
 PagerView( final PersonSearchModel personSearchModel, final PersonSearchController personSearchController) {
	 this.personSearchModel=personSearchModel;
	 this.personSearchController=personSearchController;
 }
	
 @PostConstruct
 final  void init() {
	 
	 
	
	 final Button nextButton = new Button();
	 nextButton.setIcon(new ThemeResource(NEXT_BUTTON_ICON));
	
	 final Button nextEndButton = new Button();
	 addComponent(nextButton);
	 nextEndButton.setIcon(new ThemeResource(LAST_BUTTON_ICON));
	// nextEndButton.setStyleName(BaseTheme.BUTTON_LINK);
	 addComponent(nextEndButton);
	 final Label pageLabel = new Label();
	
	 addComponent(pageLabel);
	 setComponentAlignment(pageLabel, Alignment.MIDDLE_CENTER);
	 final Button backButton = new Button();
	 backButton.setIcon(new ThemeResource(BACK_BUTTON_ICON));
	 addComponent(backButton);
	 final Button backEndButton = new Button();
	 backEndButton.setIcon(new ThemeResource(FIRST_BUTTON_ICON));
	 addComponent(backEndButton);
	 
	 nextButton.setDisableOnClick(true);
	 nextEndButton.setDisableOnClick(true);
	 backButton.setDisableOnClick(true);
	 backEndButton.setDisableOnClick(true);
	 
	 nextButton.setEnabled(false);
	 backButton.setEnabled(false);
	 nextEndButton.setEnabled(false);
	 backEndButton.setEnabled(false);
	
	
	
	 nextButton.addClickListener(event -> personSearchController.incPaging(personSearchModel));
	 nextEndButton.addClickListener(event -> personSearchController.endPaging(personSearchModel));
	 backButton.addClickListener(event -> personSearchController.decPaging(personSearchModel));
	 backEndButton.addClickListener(event -> personSearchController.beginPaging(personSearchModel));
	 
	 personSearchModel.register(event -> { 
		 nextButton.setEnabled(personSearchModel.getPaging().hasNextPage());
		 nextEndButton.setEnabled(! personSearchModel.getPaging().isEnd());
		 backButton.setEnabled(personSearchModel.getPaging().hasPreviousPage());
		 backEndButton.setEnabled(! personSearchModel.getPaging().isBegin());
		
		 pageLabel.setValue( String.format("%s/%s", personSearchModel.getPaging().currentPage(),  personSearchModel.getPaging().maxPages()));
	 }, PersonSearchModel.EventType.PagingChanges);
	 
 }

}
