package de.mq.vaadin.util;

@FunctionalInterface
public interface Mapper<Source,Target> {

	Target mapInto(final Source source ,final Target target);
}
