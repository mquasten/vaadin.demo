package de.mq.phone.domain.person.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class PagingTest {
	
	
	private static final String CURRENT_PAGE2 = "currentPage";
	private static final long SECOND_PAGE = 2L;
	private static final int MAX_PAGES = 11;
	private static final int CURRENT_PAGE = 5;
	private static final int COUNTER = 102;
	private static final int PAGE_SIZE = 10;

	@Test
	public final void create() {
		final Paging paging = new SimpleResultSetPagingImpl(PAGE_SIZE, COUNTER );
		Assert.assertEquals((long) Math.ceil((double) COUNTER/PAGE_SIZE), paging.maxPages());
		Assert.assertEquals((long) 1, paging.currentPage());
		Assert.assertEquals(PAGE_SIZE, paging.pageSize());
		
	}
	
	@Test
	public final void createEmpty() {
		final Paging paging = new SimpleResultSetPagingImpl();
		Assert.assertEquals(0, paging.pageSize());
		Assert.assertEquals(1L, paging.currentPage());
		Assert.assertEquals(0L, paging.maxPages());
	}
	
	@Test
	public final void createCopy() {
		final Paging paging = Mockito.mock(Paging.class);
		Mockito.when(paging.currentPage()).thenReturn(CURRENT_PAGE);
		Mockito.when(paging.pageSize()).thenReturn(PAGE_SIZE);
		Mockito.when(paging.maxPages()).thenReturn(MAX_PAGES);
		final Paging newPaging = new SimpleResultSetPagingImpl(paging);
		Assert.assertEquals(CURRENT_PAGE, newPaging.currentPage());
		Assert.assertEquals(PAGE_SIZE, newPaging.pageSize());
		Assert.assertEquals(MAX_PAGES, newPaging.maxPages());
	}
	
	@Test
	public final void  hasNextPage() {
		Assert.assertFalse(new SimpleResultSetPagingImpl(PAGE_SIZE, PAGE_SIZE ).hasNextPage());
		Assert.assertTrue(new SimpleResultSetPagingImpl(PAGE_SIZE, PAGE_SIZE+1 ).hasNextPage());
	}
	
	@Test
	public final void hasPreviousPage() {
		final Paging paging = new SimpleResultSetPagingImpl(PAGE_SIZE, PAGE_SIZE );
		Assert.assertFalse(paging.hasPreviousPage());
		ReflectionTestUtils.setField(paging, CURRENT_PAGE2, SECOND_PAGE);
		Assert.assertTrue(paging.hasPreviousPage());
		
	}
	
	@Test
	public final void inc(){
		final SimpleResultSetPagingImpl paging = new SimpleResultSetPagingImpl(PAGE_SIZE, PAGE_SIZE+1 );
		
		Assert.assertTrue(paging.inc());
		Assert.assertFalse(paging.inc());
	}
	
	@Test
	public final void desc() {
		final SimpleResultSetPagingImpl paging = new SimpleResultSetPagingImpl(PAGE_SIZE, PAGE_SIZE+1 );
		ReflectionTestUtils.setField(paging, CURRENT_PAGE2, SECOND_PAGE);
		Assert.assertTrue(paging.dec());
		Assert.assertFalse(paging.dec());
		
	}
	
	@Test
	public final void first() {
		final SimpleResultSetPagingImpl paging = new SimpleResultSetPagingImpl(PAGE_SIZE, PAGE_SIZE+1 );
		ReflectionTestUtils.setField(paging, CURRENT_PAGE2, SECOND_PAGE);
		Assert.assertEquals(SECOND_PAGE, paging.currentPage());
		paging.first();
		Assert.assertEquals(1L, paging.currentPage());
	}
	
	@Test
	public final void last() {
		final SimpleResultSetPagingImpl paging = new SimpleResultSetPagingImpl(PAGE_SIZE, COUNTER );
		paging.last();
		Assert.assertEquals((long) MAX_PAGES, paging.currentPage());

	}
	
	@Test
	public final void  firstRow() {
		final Paging paging = new SimpleResultSetPagingImpl(PAGE_SIZE, COUNTER );
		ReflectionTestUtils.setField(paging, CURRENT_PAGE2, 6);
		
		Assert.assertEquals((long) 50, paging.firstRow());
	}
	
	@Test
	public final void isEnd() {
		final Paging paging = new SimpleResultSetPagingImpl(PAGE_SIZE, COUNTER );
		Assert.assertFalse(paging.isEnd());
		ReflectionTestUtils.setField(paging, CURRENT_PAGE2, MAX_PAGES);
		Assert.assertTrue(paging.isEnd());
	}
	
	@Test
	public final void isBegin() {
		final Paging paging = new SimpleResultSetPagingImpl(PAGE_SIZE, COUNTER );
		Assert.assertTrue(paging.isBegin());
		ReflectionTestUtils.setField(paging, CURRENT_PAGE2, SECOND_PAGE);
		Assert.assertFalse(paging.isBegin());
	}

}
