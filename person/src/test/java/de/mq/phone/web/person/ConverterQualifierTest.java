package de.mq.phone.web.person;

import org.junit.Test;

public class ConverterQualifierTest {
	
	@Test	
	public final void types() {
		for(final ConverterQualifier.Type type :  ConverterQualifier.Type.values()) {
			ConverterQualifier.Type.valueOf(type.name());
		}
	}

}
