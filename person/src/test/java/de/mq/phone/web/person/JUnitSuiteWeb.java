package de.mq.phone.web.person;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ConverterQualifierTest.class,
		ItemToPersonSearchEntitySetConverterTest.class,
		PersonListToItemContainerConverterTest.class,
		PersonSearchControllerTest.class, PersonSearchModelTest.class,
		StartViewTest.class,UserModelTest.class, PersonSearchViewTest.class })
public class JUnitSuiteWeb {

}
