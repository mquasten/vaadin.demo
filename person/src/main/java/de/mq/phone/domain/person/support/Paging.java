package de.mq.phone.domain.person.support;

public interface Paging {

	public  boolean hasNextPage();

	public  boolean hasPreviousPage();

	public  void inc();

	public  void dec();

	public  void first();

	public void last();

	public  Number firstRow();

	public  Number pageSize();

	Number maxPages();

}