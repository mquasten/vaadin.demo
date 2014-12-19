package de.mq.phone.domain.person.support;

public interface Paging {

	boolean hasNextPage();
	boolean isEnd();
	boolean hasPreviousPage();
	boolean isBegin();
	boolean inc();
	void dec();
	void first();
	void last();
	Number firstRow();
	Number pageSize();
	Number maxPages();
	Number currentPage();

}