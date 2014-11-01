package de.mq.phone.web.person;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

public class ValidatorQualifierTest {
	
	@Test
	public final void values() {
		Arrays.stream(ValidatorQualifier.Type.values()).forEach(value -> Assert.assertEquals(value, ValidatorQualifier.Type.valueOf(value.name())));
	}

}
