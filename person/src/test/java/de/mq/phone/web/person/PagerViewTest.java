package de.mq.phone.web.person;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;






import com.vaadin.ui.Button;
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
	
	 final Map<Class<? extends Component>, Collection<Component>> components = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	@Before
	public final void setup() {
		Mockito.when(personSearchModel.getPaging()).thenReturn(paging);
		
		Mockito.doAnswer(invocation -> observer=(Observer<EventType>) invocation.getArguments()[0]).when(personSearchModel).register(Mockito.any(Observer.class), Mockito.any(PersonSearchModel.EventType.class));
		pagerView.init();
		ComponentTestHelper.componentsByClass(pagerView,  components);	
	}
	
	@Test
	public final void  init() {
		Assert.assertEquals(1,components.get(Label.class).size());
		final Label label = (Label) components.get(Label.class).iterator().next();
		Button nextButton=null; 
		Button backButton=null;
		Button lastButton=null;
		Button firstButton=null;
		for(final Component component : components.get(Button.class)){
			final String icon = ((Button)component).getIcon().toString();
			if(PagerView.NEXT_BUTTON_ICON.equals(icon)){
				nextButton=(Button)component;
				continue;
			}
			if(PagerView.BACK_BUTTON_ICON.equals(icon)){
				backButton=(Button)component;
				continue;
			}
			if(PagerView.LAST_BUTTON_ICON.equals(icon)){
				lastButton=(Button)component;
				continue;
			}
			if(PagerView.FIRST_BUTTON_ICON.equals(icon)){
				firstButton=(Button)component;
				continue;
			}
		}
		
		Assert.assertNotNull(label);
		Assert.assertNotNull(nextButton);
		Assert.assertNotNull(backButton);
		Assert.assertNotNull(lastButton);
		Assert.assertNotNull(firstButton);
		Assert.assertEquals("", label.getValue());
		Assert.assertFalse(nextButton.isEnabled());
		Assert.assertFalse(backButton.isEnabled());
		Assert.assertFalse(lastButton.isEnabled());
		Assert.assertFalse(firstButton.isEnabled());
		
		
		Mockito.when(paging.hasNextPage()).thenReturn(true);
		Mockito.when(paging.currentPage()).thenReturn(1);
		Mockito.when(paging.maxPages()).thenReturn(10);
		
		observer.process(EventType.PagingChanges);
		
	    Assert.assertEquals("1/10", label.getValue());
		Assert.assertTrue(nextButton.isEnabled());
		Assert.assertFalse(backButton.isEnabled());
		Assert.assertTrue(lastButton.isEnabled());
		Assert.assertTrue(firstButton.isEnabled());
		
	}

}
