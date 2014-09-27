package de.mq.phone.domain.person.support;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import de.mq.phone.domain.person.Contact;

class PhoneImpl extends AbstractContact implements Contact {
	
	private final String countryCode;
	private final String nationalDestinationCode;
	private final String subscriberNumber;
	
	
	PhoneImpl(final String subscriberNumber) {
		
		this(null, null,subscriberNumber);
	}

	
	PhoneImpl(final String nationalDestinationCode, final String subscriberNumber) {
		this(null, nationalDestinationCode, subscriberNumber);
	}

	
	PhoneImpl(final String countryCode, final String nationalDestinationCode, final String subscriberNumber) {
		this.countryCode = replaceLeadingZeros(countryCode);
		this.nationalDestinationCode = replaceLeadingZeros(nationalDestinationCode);
		this.subscriberNumber = replaceLeadingZeros(subscriberNumber);
	}
	
	@SuppressWarnings("unused")
	private PhoneImpl() {
		this(null,null,null);
	}

	String replaceLeadingZeros(final String text) {
		if( text == null){
			return null;
		}
		return text.replaceFirst("^[0]+", "");
	}


	@Override
	public String contact() {
		final StringBuilder builder = new StringBuilder();
		
		builder.append(zeros());
		
		if( StringUtils.hasText(countryCode) ) {
			builder.append(countryCode);
			builder.append(" ");
		}
		if( StringUtils.hasText(nationalDestinationCode)){
			builder.append(nationalDestinationCode);
			builder.append(" ");
		}
		
		if( StringUtils.hasText(subscriberNumber)){
			builder.append(subscriberNumber);
		}
	
		return builder.toString();
		
	}
	

	

	private String zeros() {
		if( StringUtils.hasText(countryCode)) {
			Assert.hasText(nationalDestinationCode);
			Assert.hasText(subscriberNumber);
			return "+";
		}
		if(StringUtils.hasText(nationalDestinationCode)){
			Assert.hasText(subscriberNumber);
			return "0";
		}
		return ""; 
	}
	
	
	

}
