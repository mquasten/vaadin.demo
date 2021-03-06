package de.mq.phone.web.person;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ConverterQualifierTest.class,
		ItemToPersonSearchEntitySetConverterTest.class,
		PersonListToItemContainerConverterTest.class,
		PersonSearchControllerTest.class, PersonSearchModelTest.class,
		StartViewTest.class,UserModelTest.class, PersonSearchViewTest.class,
		PersonEditModelTest.class	, PersonEditControllerTest.class	, EMailValidatorTest.class, 
		PhoneValidatorTest.class, PersonFieldSetValidatorTest.class, ContactMapperTest.class,
		ContactMapperTest.class, MapToPersonConverterTest.class, ValidatorQualifierTest.class,
		ContactEditorViewTest.class, PersonEditViewTest.class , StringCollectionToContainerConverterTest.class, 
		DistanceValidatorTest.class, MainMenuBarViewTest.class, PagerViewTest.class 
})
public class JUnitSuiteWeb {

}
