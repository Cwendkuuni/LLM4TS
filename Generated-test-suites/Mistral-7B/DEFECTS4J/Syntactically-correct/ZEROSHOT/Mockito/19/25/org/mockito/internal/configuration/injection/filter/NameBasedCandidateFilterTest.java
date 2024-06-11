package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InjectableField;
import org.mockito.invocation.MockInvocation;
import org.mockito.stub.Answer;
import org.mockito.stub.Stub;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {

	private NameBasedCandidateFilter filter;
	private MockCandidateFilter nextMock;
	private MockUtil mockUtilMock;

	@Before
	public void setup() {
		mockUtilMock = mock(MockUtil.class);
		nextMock = mock(MockCandidateFilter.class);
		filter = new NameBasedCandidateFilter(nextMock);
		when(mockUtilMock.getMockName(any(Object.class))).thenAnswer(new Answer<String>() {
			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				Object mock = invocation.getMock();
				return "mock" + mock.hashCode();
			}
		});
		when(nextMock.filterCandidate(any(Collection.class), any(Field.class), any(Object.class)))
				.thenCallRealMethod();
	}

	@Test
	public void testFilterCandidate_noConflictingNames_nextFilterCalled() {
		// given
		Field field = new Field("testField", String.class, null);
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock(String.class));
		mocks.add(mock(String.class));

		// when
		OngoingInjecter result = filter.filterCandidate(mocks, field, spy(Object.class));

		// then
		verify(nextMock).filterCandidate(mocks, field, spy(Object.class));
		assertThat(result, is(nextMock));
	}

	@Test
	public void testFilterCandidate_conflictingName_correctMockReturned() {
		// given
		Field field = new Field("testField", String.class, null);
		Collection<Object> mocks = Arrays.asList(
				mock(String.class, "mock1"),
				mock(String.class, "mock2"),
				mock(String.class, "mock1")
		);
		Object fieldInstance = mock(Object.class);

		// when
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// then
		assertThat(result, is(mocks.get(0)));
	}

	@Test
	public void testFilterCandidate_noMocks_nextFilterCalled() {
		// given
		Field field = new Field("testField", String.class, null);
		Collection<Object> mocks = Collections.emptyList();

		// when
		OngoingInjecter result = filter.filterCandidate(mocks, field, spy(Object.class));

		// then
		verify(nextMock).filterCandidate(mocks, field, spy(Object.class));
		assertThat(result, is(nextMock));
	}

	@Test
	public void testFilterCandidate_noConflictingNames_nextFilterReturnsEmptyCollection() {
		// given
		Field field = new Field("testField", String.class, null);
		Collection<Object> mocks = Arrays.asList(
				mock(String.class, "mock1"),
				mock(String.class, "mock2")
		);

		// when
		when(nextMock.filterCandidate(any(Collection.class), any(Field.class), any(Object.class)))
				.thenReturn(Collections.emptyList());
		OngoingInjecter result = filter.filterCandidate(mocks, field, spy(Object.class));

		// then
		assertThat(result, is(Collections.emptyList()));
		verify(nextMock).filterCandidate(mocks, field, spy(Object.class));
	}
}

class Field {
	String name;
	Class<?> type;
	Object declaringClass;

	public Field(String name, Class<?> type, Object declaringClass) {
		this.name = name;
		this.type = type;
		this.declaringClass = declaringClass;
	}
}