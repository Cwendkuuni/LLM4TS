package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.MockitoMethod;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {

	private NameBasedCandidateFilter filter;
	private MockCandidateFilter nextFilter;
	private MockUtil mockUtil;

	@Before
	public void setUp() {
		mockUtil = mock(MockUtil.class);
		nextFilter = spy(new MockCandidateFilter() {
			@Override
			public Object filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
				return null;
			}
		});
		filter = new NameBasedCandidateFilter(nextFilter);
		filter.mockUtil = mockUtil;
	}

	@Test
	public void testFilterCandidate_oneMockWithSameNameAsField_returnNextFilter() {
		// Given
		Object mock = mock(String.class);
		String fieldName = "mockName";
		Field field = new Field("fieldName", String.class, null);
		Object fieldInstance = new Object();
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock);

		when(mockUtil.getMockName(mock)).thenReturn(fieldName);

		// When
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		assertEquals(nextFilter, result.filter);
	}

	@Test
	public void testFilterCandidate_oneMockWithDifferentNameAsField_returnNull() {
		// Given
		Object mock = mock(String.class);
		String fieldName = "mockDifferentName";
		Field field = new Field("fieldName", String.class, null);
		Object fieldInstance = new Object();
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock);

		when(mockUtil.getMockName(mock)).thenReturn(fieldName);

		// When
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		assertNull(result);
	}

	@Test
	public void testFilterCandidate_multipleMocksWithSameNameAsField_returnNextFilterWithCorrectMock() {
		// Given
		Object mock1 = mock(String.class);
		Object mock2 = mock(String.class);
		String fieldName = "mockName";
		Field field = new Field("fieldName", String.class, null);
		Object fieldInstance = new Object();
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock1);
		mocks.add(mock2);

		when(mockUtil.getMockName(mock1)).thenReturn(fieldName);

		// When
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		assertEquals(nextFilter, result.filter);
		assertEquals(mock1, result.injected);
	}

	@Test
	public void testFilterCandidate_multipleMocksWithDifferentNameAsField_returnNull() {
		// Given
		Object mock1 = mock(String.class);
		Object mock2 = mock(String.class);
		String fieldName1 = "mockName1";
		String fieldName2 = "mockName2";
		Field field = new Field("fieldName", String.class, null);
		Object fieldInstance = new Object();
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock1);
		mocks.add(mock2);

		when(mockUtil.getMockName(mock1)).thenReturn(fieldName1);
		when(mockUtil.getMockName(mock2)).thenReturn(fieldName2);

		// When
		OngoingInjecter result1 = filter.filterCandidate(mocks, field, fieldInstance);
		OngoingInjecter result2 = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		assertNull(result1);
		assertNull(result2);
	}

	@Test
	public void testFilterCandidate_noMockWithNameAsField_returnNextFilter() {
		// Given
		Field field = new Field("fieldName", String.class, null);
		Object fieldInstance = new Object();
		Collection<Object> mocks = new ArrayList<>();

		// When
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		assertEquals(nextFilter, result.filter);
	}
}