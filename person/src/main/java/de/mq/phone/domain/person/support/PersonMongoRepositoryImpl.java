package de.mq.phone.domain.person.support;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import de.mq.phone.domain.person.Address;
import de.mq.phone.domain.person.AddressStringAware;
import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.PersonStringAware;
import de.mq.vaadin.util.BeforeSave;

@Repository
public class PersonMongoRepositoryImpl implements PersonRepository {
	
	private final MongoOperations mongoOperations;

	@Autowired
	public PersonMongoRepositoryImpl(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.PersonPepository#save(de.mq.phone.domain.person.support.PersonImpl)
	 */
	@Override
	public final void save(final Person person) {
		invokeCallback(person);
		skipEmptyString(person);
		person.contacts().forEach(contact -> {
			skipEmptyString(contact);
			invokeCallback(contact);
		} );
		
		
		invokeAddressCalbback(person.address());
		
		if( person.bankingAccount() != null){
			skipEmptyString(person.bankingAccount());
			invokeCallback(person.bankingAccount());
		}
		
		mongoOperations.save(person);
	}
	
	
	private void skipEmptyString(final Object entity) {
		ReflectionUtils.doWithFields(entity.getClass(), field -> { 
			field.setAccessible(true);
			ReflectionUtils.setField(field, entity, null);
		}, field -> { 
			if(  ! field.getType().equals(String.class)) {
				return false;
			}
			field.setAccessible(true);
			if( StringUtils.hasText((String) ReflectionUtils.getField(field, entity))) {
				return false;
			}
			return true;
	    });
	}
	
	
	
	void invokeAddressCalbback(final Address address) {
		if( address == null) {
			return;
		}
		skipEmptyString(address);
		invokeCallback(address);
		if( address.coordinates()==null ) {
			return;
		}
		invokeCallback(address.coordinates());
	}

	private void invokeCallback(final Object entity) {
		ReflectionUtils.doWithMethods(entity.getClass(), method -> {

			if (!method.isAnnotationPresent(BeforeSave.class)) {
				return;
			}
			method.setAccessible(true);
			ReflectionUtils.invokeMethod(method, entity);

		});
	}
	
	public final void dropPersons() {
		mongoOperations.dropCollection(PersonImpl.class);
	}
	
	/* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.PersonPepository#forCriterias(de.mq.phone.domain.person.Person, de.mq.phone.domain.person.Contact)
	 */
	@Override
	public final List<Person>forCriterias(final PersonStringAware person, final AddressStringAware address, final Contact contact) {
		final Query query = new Query();
		
		if( StringUtils.hasText(person.person())){
			query.addCriteria(new Criteria("person").regex(StringUtils.trimWhitespace(person.person())));
		}
		
		if(  StringUtils.hasText(contact.contact())) {
			query.addCriteria(new Criteria("contacts.contact").regex(StringUtils.trimWhitespace(contact.contact())));
		}
		
		if( StringUtils.hasLength(address.address())){
			query.addCriteria(new Criteria("address.address").regex(StringUtils.trimWhitespace(address.address())));
		}
		
		return Collections.unmodifiableList(mongoOperations.find(query, Person.class, "person"));
	
	}
	
	@Override
	public final Person forId(final String id) {
		return mongoOperations.findById(id, PersonImpl.class);
	}
	
	@Override
	public final void delete( final String id) {
		final Person person = forId(id);
		mongoOperations.remove(person);
	}

}
