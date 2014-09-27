package de.mq.phone.domain.person.support;



import org.springframework.util.StringUtils;

import de.mq.phone.domain.person.Address;
import de.mq.phone.domain.person.AddressStringAware;
import de.mq.phone.domain.person.GeoCoordinates;
import de.mq.vaadin.util.BeforeSave;


class AddressImpl implements Address {
	
	private final String zipCode;
	
	private final String city;
	
	private final String street;
	
	private final String houseNumber;
	
	private GeoCoordinates geoCoordinates;
	
	@SuppressWarnings("unused")
	private String address;
	
	AddressImpl(final String zipCode, final String city, final String street, final String houseNumber) {
		this.zipCode = zipCode;
		this.city = city;
		this.street = street;
		this.houseNumber = houseNumber;
	}
	
	AddressImpl() {
		this(null,null,null,null);
	}
	
	/* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.Address#zipCode()
	 */
	@Override
	public final String zipCode() {
		return zipCode;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.Address#city()
	 */
	@Override
	public final String  city() {
		return city;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.Address#street()
	 */
	@Override
	public final String street(){
		return street;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.Address#houseNumber()
	 */
	@Override
	public final String houseNumber() {
		return houseNumber;
	}

	
	
	/* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.Address#address()
	 */
	@Override
	public final String address() {
		final StringBuilder builder = new StringBuilder();
		boolean streetAware=false;
		if( StringUtils.hasText(street)) {
			builder.append(StringUtils.trimWhitespace(street));
			streetAware=true;
		}
		
		if( StringUtils.hasText(houseNumber) && (streetAware)){
			builder.append(" ");
			builder.append(StringUtils.trimWhitespace(houseNumber));
		}
		
		if( StringUtils.hasText(zipCode)) {
			appendIfNoFirst(builder, ", ");
			builder.append(StringUtils.trimWhitespace(zipCode));
		}
		
		if( StringUtils.hasText(city)){
			appendKommaIfNotExists(builder,streetAware);
			appendIfNoFirst(builder, " ");
			builder.append(StringUtils.trimWhitespace(city));
		}
		return builder.toString();
	}

	@BeforeSave
	void beforeSave() {
		this.address=address();
	}


	private void appendKommaIfNotExists(final StringBuilder builder, final boolean streetAware) {
		if( ! streetAware ){
			return ; 
		}
	/*	if(builder.length()==0){
			return;
		} */
		if(  builder.indexOf(",") < 0) {
			builder.append(",");
		}
	}



	private void appendIfNoFirst(final StringBuilder builder, final String text) {
		if( builder.length() == 0 ) {
			return;
		}
		builder.append(text);
	
	}
	
	/* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.Address#assign(de.mq.phone.domain.person.support.GeoCoordinates)
	 */
	@Override
	public final void assign(final GeoCoordinates geoCoordinates){
		this.geoCoordinates=geoCoordinates;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.Address#coordinates()
	 */
	@Override
	public final GeoCoordinates coordinates() {
		return geoCoordinates;
	}
	
	@Override
	public final int hashCode() {
		final String address = address();
		if( ! StringUtils.hasText(address) ) {
			return super.hashCode();
		}
		return address.hashCode();
		
	}
	
	@Override
	public final boolean equals(final Object obj) {
		if (!(obj instanceof AddressStringAware)) {
			return false;
		}
		final AddressStringAware otherAddressStringAware = (AddressStringAware) obj;
		return address().equals(otherAddressStringAware.address());
		
	}
	

}
