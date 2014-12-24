package de.mq;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.mq.phone.domain.person.support.JUnitSuiteDomain;
import de.mq.phone.web.person.JUnitSuiteWeb;
import de.mq.vaadin.util.JUnitSuiteUtil;

@RunWith(Suite.class)
@SuiteClasses({JUnitSuiteWeb.class, JUnitSuiteUtil.class, JUnitSuiteDomain.class})
public class AllTests {
	

	

	
	
}
