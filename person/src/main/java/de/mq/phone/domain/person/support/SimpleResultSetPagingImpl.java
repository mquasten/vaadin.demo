package de.mq.phone.domain.person.support;



public class SimpleResultSetPagingImpl implements Paging {

	private final Number pageSize;

	private final Number maxPages;

	private Number currentPage;

	SimpleResultSetPagingImpl(final Number pageSize, final Number counter) {
		this.pageSize = pageSize;
		maxPages = Double.valueOf(Math.ceil( counter.doubleValue() / pageSize.doubleValue())).longValue();
		currentPage = 1L;
	}
	
	public SimpleResultSetPagingImpl() {
		this(0,0);
	}
	
	public SimpleResultSetPagingImpl(final Paging paging) {
		this.pageSize = paging.pageSize();
		maxPages =  paging.maxPages();
		currentPage = paging.currentPage();
	}

	/* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.Paging#hasNextPage()
	 */
	@Override
	public final boolean hasNextPage() {
		return currentPage.longValue() < maxPages.longValue();
	}

	/* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.Paging#hasPreviousPage()
	 */
	@Override
	public final boolean hasPreviousPage() {
		return currentPage.longValue() > 1L;
	}

	
	public final boolean  inc() {
		if (!hasNextPage()) {
			return false;
		}
		currentPage = new Long(currentPage.longValue() + 1L);
		return true;
	}


	public final boolean  dec() {
		if (!hasPreviousPage()) {
			return false;
		}
		currentPage = new Long(currentPage.longValue() - 1L);
		return true;
	}

	
	public final void first() {
		currentPage = 1L;
	}

	
	public final void last() {
		currentPage = maxPages;
	}

	/* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.Paging#firstRow()
	 */
	@Override
	public final Number firstRow() {
		return (currentPage.longValue() - 1L) * pageSize.longValue();
	}

	/* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.Paging#pageSize()
	 */
	@Override
	public final Number pageSize() {
		return pageSize;
	}
	
	@Override
	public final Number maxPages() {
		 return maxPages;
	}

	@Override
	public final Number currentPage() {
		return currentPage;
	}

	@Override
	public boolean isEnd() {
		return currentPage.longValue() >= maxPages.longValue();
	}

	@Override
	public boolean isBegin() {
		return currentPage.longValue() <= 1L;
	}
}
