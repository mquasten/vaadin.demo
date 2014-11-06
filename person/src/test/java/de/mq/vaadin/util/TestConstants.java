package de.mq.vaadin.util;

import org.springframework.context.MessageSource;

public class TestConstants {
	
	public static BindingResultsToFieldGroupMapper newBindingResultsToFieldGroupMapper(final MessageSource messageSource) {
		return new BindingResultsToFieldGroupMapperImpl(messageSource); 
	}

}
