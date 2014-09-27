package de.mq.phone.domain.person.support;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.mq.phone.domain.person.AddressStringAware;
import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.PersonService;
import de.mq.phone.domain.person.PersonStringAware;

@Service
class PersonServiceImpl implements PersonService {
	
	private final  PersonRepository personRepository ; 
	@Autowired
	PersonServiceImpl(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.PersonService#persons(de.mq.phone.domain.person.Person, de.mq.phone.domain.person.Contact)
	 */
	@Override
	public final  List<Person>  persons(final PersonStringAware person, final AddressStringAware address, final Contact contact) {
		return personRepository.forCriterias(person,address, contact);
		
	}
	
	@Override
	public final void save(final Person person) {
		personRepository.save(person);
	}

	

}
