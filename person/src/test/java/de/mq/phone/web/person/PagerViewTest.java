package de.mq.phone.web.person;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

import de.mq.phone.domain.person.support.ModifyablePaging;
import de.mq.phone.web.person.PersonSearchModel.EventType;
import de.mq.vaadin.util.Observer;



public class PagerViewTest {
	
	private PersonSearchModel personSearchModel=Mockito.mock(PersonSearchModel.class);
	
	private PersonSearchController personSearchController = Mockito.mock(PersonSearchController.class);
	
	private final PagerView pagerView = new PagerView(personSearchModel, personSearchController);
	
	private ModifyablePaging paging = Mockito.mock(ModifyablePaging.class);
	private Observer<EventType> observer=null;
	
	private final Map<Class<? extends Component>, Collection<Component>> components = new HashMap<>();
	
	private final ClickEvent clickEvent = Mockito.mock(ClickEvent.class);
	private Label label = null;
	private Button nextButton=null; 
	private Button backButton=null;
	private Button lastButton=null;
	private Button firstButton=null;
	 
	
	@SuppressWarnings("unchecked")
	@Before
	public final void setup() {
		Mockito.when(personSearchModel.getPaging()).thenReturn(paging);
		
		Mockito.doAnswer(invocation -> observer=(Observer<EventType>) invocation.getArguments()[0]).when(personSearchModel).register(Mockito.any(Observer.class), Mockito.any(PersonSearchModel.EventType.class));
		pagerView.init();
		ComponentTestHelper.componentsByClass(pagerView,  components);	
		
		
		Assert.assertEquals(1,components.get(Label.class).size());
		label = (Label) components.get(Label.class).iterator().next();
		nextButton=null; 
		backButton=null;
		lastButton=null;
		firstButton=null;
		
		nextButton=button(PagerView.NEXT_BUTTON_ICON);
		backButton=button(PagerView.BACK_BUTTON_ICON);
		lastButton=button(PagerView.LAST_BUTTON_ICON);
		firstButton=button(PagerView.FIRST_BUTTON_ICON);
		
		Assert.assertNotNull(label);
		Assert.assertNotNull(nextButton);
		Assert.assertNotNull(backButton);
		Assert.assertNotNull(lastButton);
		Assert.assertNotNull(firstButton);
	}

	private Button button(final String iconAsString) {
		return (Button) components.get(Button.class).stream().filter(component -> iconAsString.equals( ((Button)component).getIcon().toString())).findFirst().get();
	}
	
	@Test
	public final void  init() {
		
		Assert.assertEquals("", label.getValue());
		Assert.assertFalse(nextButton.isEnabled());
		Assert.assertFalse(backButton.isEnabled());
		Assert.assertFalse(lastButton.isEnabled());
		Assert.assertFalse(firstButton.isEnabled());
		
	}
	
	@Test
	public final void initFirstPage() {
		Mockito.when(paging.hasNextPage()).thenReturn(true);
		Mockito.when(paging.isBegin()).thenReturn(true);
		Mockito.when(paging.currentPage()).thenReturn(1);
		Mockito.when(paging.maxPages()).thenReturn(10);
		
		observer.process(EventType.PagingChanges);
		
	   Assert.assertEquals("1/10", label.getValue());
		Assert.assertTrue(nextButton.isEnabled());
		Assert.assertFalse(backButton.isEnabled());
		Assert.assertTrue(lastButton.isEnabled());
		Assert.assertFalse(firstButton.isEnabled());
	}
	
	@Test
	public final void initLastPage() {
		Mockito.when(paging.hasNextPage()).thenReturn(false);
		Mockito.when(paging.hasPreviousPage()).thenReturn(true);
		Mockito.when(paging.isEnd()).thenReturn(true);
		Mockito.when(paging.isBegin()).thenReturn(false);
		Mockito.when(paging.currentPage()).thenReturn(10);
		Mockito.when(paging.maxPages()).thenReturn(10);
		
		observer.process(EventType.PagingChanges);
		
		
		Assert.assertEquals("10/10", label.getValue());
		Assert.assertFalse(nextButton.isEnabled());
		Assert.assertTrue(backButton.isEnabled());
		Assert.assertFalse(lastButton.isEnabled());
		Assert.assertTrue(firstButton.isEnabled());
	}
	
	
	@Test
	public final void nextPage() {
		executeButton(nextButton);
		Mockito.verify(personSearchController, Mockito.times(1)).incPaging(personSearchModel);
	}
	
	@Test
	public final void prevPage() {
		executeButton(backButton);
		Mockito.verify(personSearchController, Mockito.times(1)).decPaging(personSearchModel);
	}
	
	@Test
	public final void endPage() {
		executeButton(lastButton);
		Mockito.verify(personSearchController, Mockito.times(1)).endPaging(personSearchModel);
	}
	
	@Test
	public final void beginPage() {
		executeButton(firstButton);
		Mockito.verify(personSearchController, Mockito.times(1)).beginPaging(personSearchModel);
	}
	
	

	private void executeButton(final Button button) {
		button.getListeners(ClickEvent.class).forEach( listener -> ((ClickListener)listener).buttonClick(clickEvent));
	}

}
