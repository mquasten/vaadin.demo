package de.mq.phone.domain.person.support;



public class SimpleResultSetPagingImpl implements ModifyablePaging {

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

	
	public final boolean  first() {
		if( isBegin()){
			return false;
		}
		currentPage = 1L;
		return true;
	}

	
	public final boolean  last() {
		if( isEnd()) {
			return false;
		}
		currentPage = maxPages;
		return true;
		
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
