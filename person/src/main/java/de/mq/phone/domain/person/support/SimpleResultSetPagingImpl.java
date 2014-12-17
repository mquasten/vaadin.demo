package de.mq.phone.domain.person.support;

public class SimpleResultSetPagingImpl implements Paging {

	private final Number pageSize;

	private final Number maxPages;

	private Number currentPage;

	public SimpleResultSetPagingImpl(final Number pageSize, final Number counter) {
		this.pageSize = pageSize;
		maxPages = Double.valueOf(Math.ceil( counter.doubleValue() / pageSize.doubleValue())).longValue();
		currentPage = 1L;
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

	/* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.Paging#inc()
	 */
	@Override
	public final void inc() {
		if (!hasNextPage()) {
			return;
		}
		currentPage = new Long(currentPage.longValue() + 1L);
	}

	/* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.Paging#dec()
	 */
	@Override
	public final void dec() {
		if (!hasPreviousPage()) {
			return;
		}
		currentPage = new Long(currentPage.longValue() - 1L);
	}

	/* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.Paging#first()
	 */
	@Override
	public final void first() {
		currentPage = 1L;
	}

	/* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.Paging#last()
	 */
	@Override
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
}
