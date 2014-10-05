package de.mq.phone.web.person;

import java.util.Map;

import org.springframework.core.convert.converter.Converter;

import de.mq.phone.domain.person.Person;
import de.mq.vaadin.util.Mapper;

interface MapToPersonMapper extends  Mapper<Map<String,?>, Person>, Converter<Person,Map<String,Object>> {

	Person mapInto(Map<String, ?> mapValues, Person person);

	Map<String, Object> convert(Person source);

}