package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter.OngoingInjecter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
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
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

public class NameBasedCandidateFilterTest {

	private NameBasedCandidateFilter filter;
	private MockUtil mockUtilMock;
	private MockCandidateFilter nextMock;

	@Before
	public void setUp() {
		mockUtilMock = mock(MockUtil.class);
		nextMock = mock(MockCandidateFilter.class);
		filter = new NameBasedCandidateFilter(nextMock);
		PowerMockito.mockStatic(MockUtil.class);
		when(MockUtil.class.getDeclaredField("getMockName")).then(new Answer<Field>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return invocation.getArgumentAt(0, Field.class);
			}
		});
	}

	@Test
	public void testFilterCandidate_NoConflictingFields_NextFilterIsUsed() throws Exception {
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock(Object.class));
		mocks.add(mock(Object.class));
		Field field = new Field("testField", String.class, mock(Class.class));
		Object fieldInstance = mock(Object.class);

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		assertNotNull(result);
		verify(nextMock).filterCandidate(mocks, field, fieldInstance);
	}

	@Test
	public void testFilterCandidate_ConflictingFields_CorrectFieldIsUsed() throws Exception {
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock(Object1.class));
		mocks.add(mock(Object2.class));
		Field field = new Field("testField", String.class, mock(Class.class));
		Object fieldInstance = mock(Object.class);

		Object obj1 = mock(Object1.class);
		Object obj2 = mock(Object2.class);
		Whitebox.setInternalState(obj1, "mockName", "mockName1");
		Whitebox.setInternalState(obj2, "mockName", "mockName2");

		mocks.add(obj1);
		mocks.add(obj2);

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		assertNotNull(result);
		assertEquals(obj1, result.getMock());
		verify(nextMock).filterCandidate(mocks, field, fieldInstance);
	}
}

class Object1 {
	private static String mockName;

	public String getMockName() {
		return mockName;
	}
}

class Object2 {
	private static String mockName;

	public String getMockName() {
		return mockName;
	}
}