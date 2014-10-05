package de.mq.phone.web.person;

import de.mq.phone.domain.person.Person;
import de.mq.vaadin.util.Subject;

interface PersonEditModel extends  Subject<PersonEditModel, PersonEditModel.EventType > {
	enum EventType {
		PersonChanged;
	}
	
	void setPerson(final Person person);

	Person getPerson();

}