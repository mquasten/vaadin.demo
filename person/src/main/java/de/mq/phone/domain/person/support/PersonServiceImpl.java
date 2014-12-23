package de.mq.phone.domain.person.support;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Circle;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import de.mq.phone.domain.person.AddressStringAware;
import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.PersonService;
import de.mq.phone.domain.person.PersonStringAware;

@Service
class PersonServiceImpl implements PersonService {
	
	static final UUID DEFAULT_PERSON_ID = new UUID(19680528L, 19680528L);

	private final  PersonRepository personRepository ; 
	final CoordinatesRepository coordinatesRepository;
	@Autowired
	PersonServiceImpl(final PersonRepository personRepository, final CoordinatesRepository coordinatesRepository) {
		this.personRepository = personRepository;
		this.coordinatesRepository=coordinatesRepository;
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.PersonService#persons(de.mq.phone.domain.person.Person, de.mq.phone.domain.person.Contact)
	 */
	@Override
	public final  List<Person>  persons(final PersonStringAware person, final AddressStringAware address, final Contact contact, Circle circle,  final Paging paging) {
		return personRepository.forCriterias(person,address, contact, circle, paging);
		
	}
	
	@Override
	public final  ModifyablePaging paging(final PersonStringAware person, final AddressStringAware address, final Contact contact, final Circle circle, final int pageSize) {
		final Number counter = personRepository.countFor(person, address, contact, circle);
		return new SimpleResultSetPagingImpl(pageSize, counter.longValue());
	}
	
	@Override
	public final void save(final Person person) {
		if (person.hasAddress()) {
			person.address().assign(coordinatesRepository.forAddress(person.address()));
		}
		
		personRepository.save(person);
	}

	@Override
	public final Person person(final String id) {
		return personRepository.forId(id);
	}
	
	@Override
	public final void deletePerson(final String id) {
		personRepository.delete(id);
	}
	
	@Override
	public final Person defaultPerson() {
		final Person person = personRepository.forId(DEFAULT_PERSON_ID.toString());
		if( person != null){
			return person;
		}
		final Person newPerson = BeanUtils.instantiateClass(PersonImpl.class);
		ReflectionUtils.doWithFields(PersonImpl.class, field -> { 
			field.setAccessible(true);
			field.set(newPerson, DEFAULT_PERSON_ID.toString());
		}, field ->  field.isAnnotationPresent(Id.class) );
		personRepository.save(newPerson);
		return newPerson;
	}
}
