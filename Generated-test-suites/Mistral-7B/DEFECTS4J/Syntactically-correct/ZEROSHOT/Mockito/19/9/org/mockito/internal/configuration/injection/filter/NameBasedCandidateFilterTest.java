package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {

	private NameBasedCandidateFilter filter;
	private MockCandidateFilter nextMock;
	private MockUtil mockUtilMock;
	private Collection<Object> mocks;
	private Object fieldInstance;
	private Field field;

	@Before
	public void setUp() {
		nextMock = mock(MockCandidateFilter.class);
		mockUtilMock = mock(MockUtil.class);
		when(mockUtilMock.getMockName(mock(Object.class))).thenAnswer(new Answer<String>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return "mockName";
			}
		});

		filter = new NameBasedCandidateFilter(nextMock);
		filter.mockUtil = mockUtilMock;
		mocks = new ArrayList<>();
		mocks.add(PowerMockito.spy(mock(Object.class)));
		mocks.add(PowerMockito.spy(mock(Object.class)));
		field = Whitebox.getInternalState(mock(Object.class), "fieldName");
		fieldInstance = mock(Object.class);
	}

	@Test
	public void testFilterCandidate_singleMock() {
		// Given
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		assertNotNull(result);
		verify(nextMock).filterCandidate(mocks, field, fieldInstance);
	}

	@Test
	public void testFilterCandidate_multipleMocks() {
		// Given
		when(mockUtilMock.getMockName(mocks.get(0))).thenReturn("mockName1");
		when(mockUtilMock.getMockName(mocks.get(1))).thenReturn("mockName2");

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		assertEquals("mockName1", result.getMock().getClass().getName());
		verify(nextMock).filterCandidate(mocks, field, fieldInstance);
	}

	@Test
	public void testFilterCandidate_conflictingFields() {
		// Given
		when(mockUtilMock.getMockName(mocks.get(0))).thenReturn("mockName1");
		when(mockUtilMock.getMockName(mocks.get(1))).thenReturn("mockName2");
		when(mockUtilMock.getMockName(mocks.get(2))).thenReturn("mockName1");

		Stubber stubber = PowerMockito.mockStatic(Whitebox.class);
		stubber.when(Whitebox.getInternalState(mock(Object.class), "field1")).thenReturn(field);
		stubber.when(Whitebox.getInternalState(mock(Object.class), "field2")).thenReturn(field);

		// When
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		assertEquals("mockName1", result.getMock().getClass().getName());
		verify(nextMock).filterCandidate(mocks, field, fieldInstance);
	}
}