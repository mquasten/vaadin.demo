package de.mq.phone.domain.person.support;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.geo.Circle;
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
	
	private static final String ADDRESS_COORDINATES_LOCATION_NAME = "address.geoCoordinates.location";
	private static final String ADDRESS_NAME = "address.address";
	private static final String CONTACTS_NAME = "contacts.contact";
	static final String PERSON_COLLECTION_NAME = "person";
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
	public final List<Person>forCriterias(final PersonStringAware person, final AddressStringAware address, final Contact contact, final Circle circle, final Paging paging) {
		final Query query = query(person, address, contact, circle);
		query.limit(paging.pageSize().intValue());
		query.with(new Sort(new Order(PERSON_COLLECTION_NAME)));
		query.skip(paging.firstRow().intValue());
		return Collections.unmodifiableList(mongoOperations.find(query, Person.class, PERSON_COLLECTION_NAME));
	
	}
	
	@Override
	public final Number countFor(final PersonStringAware person, final AddressStringAware address, final Contact contact, final Circle circle) {
		return Long.valueOf(mongoOperations.count(query(person, address, contact, circle), PERSON_COLLECTION_NAME));
	}

	private Query query(final PersonStringAware person, final AddressStringAware address, final Contact contact, final Circle circle) {
		final Query query = new Query();
		
		if( StringUtils.hasText(person.person())){
			query.addCriteria(new Criteria(PERSON_COLLECTION_NAME).regex(StringUtils.trimWhitespace(person.person())));
		}
		
		if(  StringUtils.hasText(contact.contact())) {
			query.addCriteria(new Criteria(CONTACTS_NAME).regex(StringUtils.trimWhitespace(contact.contact())));
		}
		
		if( StringUtils.hasLength(address.address())){
			query.addCriteria(new Criteria(ADDRESS_NAME).regex(StringUtils.trimWhitespace(address.address())));
		}
		
		if( circle.getRadius().getValue() >= 0) {
			query.addCriteria( new Criteria(ADDRESS_COORDINATES_LOCATION_NAME).withinSphere(circle));
		}
	
		return query;
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
