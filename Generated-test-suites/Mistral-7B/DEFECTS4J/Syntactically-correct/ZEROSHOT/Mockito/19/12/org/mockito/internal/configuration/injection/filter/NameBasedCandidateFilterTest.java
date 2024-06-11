package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.MockCreationSettings;
import org.mockito.internal.util.naming.Name;
import org.mockito.stub.MockSettings;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.MockUtil.newMock;
import static org.mockito.internal.util.MockUtil.spy;
import static org.mockito.internal.util.MockUtil.testSpy;

public class NameBasedCandidateFilterTest {

	private NameBasedCandidateFilter filter;
	private NameBasedCandidateFilter nextFilter;
	private MockUtil mockUtil;

	@Before
	public void setUp() {
		mockUtil = new MockUtil();
		nextFilter = mock(NameBasedCandidateFilter.class);
		filter = new NameBasedCandidateFilter(nextFilter);
	}

	@Test
	public void testFilterCandidate_singleMock_noConflict() {
		Object mock = newMock(String.class, new MockCreationSettings().name("mock1"));
		Field field = new Field("testField", String.class);
		Object fieldInstance = new Object();
		OngoingInjecter result = filter.filterCandidate(new ArrayList<Object>() {{ add(mock); }}, field, fieldInstance);

		assertEquals(mock, result.getMock());
		assertEquals(field, result.getField());
		assertEquals(fieldInstance, result.getFieldInstance());
	}

	@Test
	public void testFilterCandidate_multipleMocks_noConflict() {
		Object mock1 = newMock(String.class, new MockCreationSettings().name("mock1"));
		Object mock2 = newMock(String.class, new MockCreationSettings().name("mock2"));
		Field field = new Field("testField", String.class);
		Object fieldInstance = new Object();

		List<Object> mocks = new ArrayList<Object>() {{ add(mock1); add(mock2); }} ;

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		assertEquals(mock1, result.getMock());
		assertEquals(field, result.getField());
		assertEquals(fieldInstance, result.getFieldInstance());
	}

	@Test
	public void testFilterCandidate_multipleMocks_conflictingName_nextFilterIsCalled() {
		Object mock1 = newMock(String.class, new MockCreationSettings().name("mock1"));
		Object mock2 = newMock(String.class, new MockCreationSettings().name("mock1"));
		Field field = new Field("testField", String.class);
		Object fieldInstance = new Object();

		when(nextFilter.filterCandidate(anyCollection(), eq(field), eq(fieldInstance))).thenReturn(new OngoingInjecter(mock2, field, fieldInstance));

		List<Object> mocks = new ArrayList<Object>() {{ add(mock1); add(mock2); }} ;

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		assertEquals(mock2, result.getMock());
		assertEquals(field, result.getField());
		assertEquals(fieldInstance, result.getFieldInstance());
	}

	@Test
	public void testFilterCandidate_multipleMocks_conflictingName_noNextFilter() {
		Object mock1 = newMock(String.class, new MockCreationSettings().name("mock1"));
		Object mock2 = newMock(String.class, new MockCreationSettings().name("mock1"));
		Field field = new Field("testField", String.class);
		Object fieldInstance = new Object();

		List<Object> mocks = new ArrayList<Object>() {{ add(mock1); add(mock2); }} ;

		try {
			filter.filterCandidate(mocks, field, fieldInstance);
			fail("ExpectedIllegalArgumentException to be thrown.");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testFilterCandidate_singleSpy_noConflict() {
		Object fieldInstance = new Object();
		Object mock = testSpy(String.class, new MockCreationSettings().name("mock1"));
		Field field = new Field("testField", String.class);

		OngoingInjecter result = filter.filterCandidate(new ArrayList<Object>() {{ add(mock); }}, field, fieldInstance);

		assertEquals(mock, result.getMock());
		assertEquals(field, result.getField());
		assertEquals(fieldInstance, result.getFieldInstance());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testFilterCandidate_noMocksProvided() {
		Field field = new Field("testField", String.class);
		Object fieldInstance = new Object();

		filter.filterCandidate(null, field, fieldInstance);
	}

	@Test
	public void testFilterCandidate_multipleMocks_sameName_nextFilterIsCalled() {
		Object mock1 = newMock(String.class, new MockCreationSettings().name("mock1"));
		Object mock2 = newMock(String.class, new MockCreationSettings().name("mock1"));
		Field field = new Field("testField", String.class);
		Object fieldInstance = new Object();

		when(nextFilter.filterCandidate(anyCollection(), eq(field), eq(fieldInstance))).thenReturn(new OngoingInjecter(mock2, field, fieldInstance));

		List<Object> mocks = new ArrayList<Object>() {{ add(mock1); add(mock2); }} ;

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		assertEquals(mock2, result.getMock());
		assertEquals(field, result.getField());
		assertEquals(fieldInstance, result.getFieldInstance());
	}
}