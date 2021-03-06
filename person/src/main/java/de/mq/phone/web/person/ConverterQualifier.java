package de.mq.phone.web.person;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Qualifier;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface ConverterQualifier {

		public enum Type{
			PersonList2Container,
			Item2PersonSearchSet,
			StringList2Container;
		}
		
		Type value();
}
