package de.mq.phone.domain.person.support;

public interface Paging {

	boolean hasNextPage();
	boolean isEnd();
	boolean hasPreviousPage();
	boolean isBegin();

	Number firstRow();
	Number pageSize();
	Number maxPages();
	Number currentPage();

}