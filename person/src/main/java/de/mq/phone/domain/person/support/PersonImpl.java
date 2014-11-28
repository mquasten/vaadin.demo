package de.mq.phone.domain.person.support;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

import de.mq.phone.domain.person.Address;
import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.PersonStringAware;
import de.mq.vaadin.util.BeforeSave;


@Document(collection="person")
class PersonImpl implements Person {
	
	@Id
	private String id ; 
	
	private String name;
	
	private String firstname;
	
	private Set<Contact>  contacts = new HashSet<Contact>();
	
	private Address address;
	
	private String alias;
	
	private BankingAccount bankingAccount;
	
	@SuppressWarnings("unused")
	private String person; 
	
	
	public PersonImpl(final String name, final String firstname, final String alias) {
		this.name = name;
		this.firstname = firstname;
		this.alias=alias;
	}
	
	public PersonImpl(String name, String firstname) {
		this(name, firstname, null);
	}
	
	PersonImpl(String name) {
		this(name, null, null);
		
	}

	@SuppressWarnings("unused")
	private PersonImpl() {
		
	}
		
	@Override
	public final void assign(Contact contact){
		contacts.add(contact);
	}

	@Override
	public String firstname() {
		
		return firstname;
	}

	@Override
	public String name() {
		return name;
	}
	
	@Override
	public final Collection<Contact> contacts() {
		return Collections.unmodifiableSet(contacts);
	}

	@Override
	public String id() {
		return id;
	}
	
	@Override
	public final Address address() {
		return address;
	}

	@Override
	public void assign(final Address address) {
		this.address=address;
		
	}
	
	@Override
	public final String alias(){
		return alias;
	}
	
	@Override
	public void assign(final BankingAccount bankingAccount) {
		this.bankingAccount=bankingAccount;
	}
	
	@Override
	public final BankingAccount bankingAccount() {
		return bankingAccount;
	}

	@Override
	public final String person() {
		final StringBuilder builder = new StringBuilder();
		if( StringUtils.hasText(firstname) ) {
			builder.append(StringUtils.trimWhitespace(firstname));
		}
		
		if( StringUtils.hasText(name)) {
			addBlankfIfNotEmpty(builder);
			builder.append(StringUtils.trimWhitespace(name));
		}
		
		if( StringUtils.hasText(alias)){
			addBlankfIfNotEmpty(builder);
			builder.append("(" + StringUtils.trimWhitespace(alias) + ")");
		}
		return builder.toString();
	}

	private void addBlankfIfNotEmpty(final StringBuilder builder) {
		if( builder.length() >0 ) {
			builder.append(" ");
		}
	}
	
	@BeforeSave
	final void beforeSave(){
		person=person();
	}

	@Override
	public final int hashCode() {
		if( StringUtils.hasLength(id)) {
			return id.hashCode();
		}
		final String person = person();
		if( !StringUtils.hasText(person)) {
			return super.hashCode();
		}
		return person.hashCode();
	}

	@Override
	public final  boolean equals(final Object obj) {
		if (!(obj instanceof PersonStringAware)) {
			return false;
		}
		final PersonStringAware otherPerson = (PersonStringAware) obj;
		final String otherId = PersonEntities.id(otherPerson);
		if(( id() != null)&&(otherId!=null)){
			return id().equals(otherId);
		}
		
		return person().equals(otherPerson.person());
	}
	
	@Override
	public final boolean hasAddress() {
		if( address()==null) {
			return false;
		}
		return StringUtils.hasText(address.address());
	}
	
	@Override
	public final boolean hasGeoCoordinates() {
		if( address==null){
			return false;
		}
		return address.hasGeoCoordinates();
	}
	
	
}
