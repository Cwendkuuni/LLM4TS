package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class NameBasedCandidateFilterTest {

	private NameBasedCandidateFilter filter;
	private MockCandidateFilter nextMock;
	private MockUtil mockUtilSpy;

	@Before
	public void setup() {
		nextMock = mock(MockCandidateFilter.class);
		mockUtilSpy = spy(new MockUtil());
		filter = new NameBasedCandidateFilter(nextMock);
	}

	@Test
	public void testFilterCandidate_MultipleMocksWithSameName_ReturnNextFilter() {
		// Given
		String mockName1 = "mock1";
		String mockName2 = "mock1";
		Object mock1 = mock(Object.class, new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocationOnMock) {
				return mockName1;
			}
		});
		Object mock2 = mock(Object.class, new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocationOnMock) {
				return mockName2;
			}
		});
		List<Object> mocks = new ArrayList<>(Arrays.asList(mock1, mock2));
		Collection<Object> mocksCollection = (Collection<Object>) mocks;
		Field field = MockUtil.class.getField("FIELD_NAME"); // replace "FIELD_NAME" with the actual field name
		Object fieldInstance = mock(Object.class);

		// When
		OngoingInjecter result = filter.filterCandidate(mocksCollection, field, fieldInstance);

		// Then
		verify(nextMock).filterCandidate(mockNameMatches, field, fieldInstance);
		// Replace "mockNameMatches" with the actual list of mock names that should be passed to next filter
	}

	@Test
	public void testFilterCandidate_MultipleMocksWithDifferentNames_ReturnMatchingMock() {
		// Given
		String mockName1 = "mock1";
		String mockName2 = "mock2";
		Object mock1 = mock(Object.class, new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocationOnMock) {
				return mockName1;
			}
		});
		Object mock2 = mock(Object.class, new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocationOnMock) {
				return mockName2;
			}
		});
		List<Object> mocks = new ArrayList<>(Arrays.asList(mock1, mock2));
		Collection<Object> mocksCollection = (Collection<Object>) mocks;
		Field field = MockUtil.class.getField("FIELD_NAME"); // replace "FIELD_NAME" with the actual field name
		Object fieldInstance = mock(Object.class);

		// When
		OngoingInjecter result = filter.filterCandidate(mocksCollection, field, fieldInstance);

		// Then
		if (mockName1.equals(field.getName())) {
			verify(mockUtilSpy).getMockName(mock1);
			verify(nextMock).filterCandidate(Collections.singletonList(mock1), field, fieldInstance);
		} else {
			verify(mockUtilSpy).getMockName(mock2);
			verify(nextMock).filterCandidate(Collections.singletonList(mock2), field, fieldInstance);
		}
	}

	@Test
	public void testFilterCandidate_SingleMock_ReturnNextFilter() {
		// Given
		Object mock = mock(Object.class);
		Collection<Object> mocksCollection = Arrays.asList(mock);
		Field field = MockUtil.class.getField("FIELD_NAME"); // replace "FIELD_NAME" with the actual field name
		Object fieldInstance = mock(Object.class);

		// When
		OngoingInjecter result = filter.filterCandidate(mocksCollection, field, fieldInstance);

		// Then
		verify(nextMock).filterCandidate(Collections.singletonList(mock), field, fieldInstance);
	}
}