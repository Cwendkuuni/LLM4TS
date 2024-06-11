package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.mockito.Mockito;
import org.mockito.quality.Strictness;
import org.mockito.verification.VerificationMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {

	private NameBasedCandidateFilter filter;
	private MockCandidateFilter nextFilter;
	private Collection<Object> mocks;
	private Object fieldInstance;
	private Field field;

	@Rule
	public TestRule mockitoRule = Mockito.rule().mockitoInstance(Mockito.mock(Strictness.LENIENT));

	@Before
	public void setUp() {
		nextFilter = mock(MockCandidateFilter.class);
		filter = new NameBasedCandidateFilter(nextFilter);
		mocks = new ArrayList<>();
		mocks.add(mock(Object.class));
		mocks.add(mock(Object.class));
		fieldInstance = mock(Object.class);
		field = Mockito.spy(Mockito.field(Object.class, "fieldName"));
	}

	@Test
	public void testFilterCandidate_multipleMocksWithSameName_returnCorrectMock() {
		// Given
		String mockName = field.getName();
		String expectedMockName = mockUtilGetMockName(mockName);

		// When
		OngoingInjecter actualResult = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		assertEquals(expectedMockName, actualResult.getMock().getClass().getName());
	}

	@Test
	public void testFilterCandidate_multipleMocksWithDifferentNames_nextFilterIsCalled() {
		// Given
		when(nextFilter.filterCandidate(mocks, field, fieldInstance)).thenReturn(mock(OngoingInjecter.class));

		// When
		OngoingInjecter actualResult = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		verify(nextFilter).filterCandidate(argThat(m -> mocks.containsAll(m)), field, fieldInstance);
		verifyNoInteractions(mocks.get(0));
		verifyNoInteractions(mocks.get(1));
	}

	@Test
	public void testFilterCandidate_singleMock_nextFilterIsCalled() {
		// Given

		// When
		OngoingInjecter actualResult = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		verify(nextFilter).filterCandidate(mocks, field, fieldInstance);
	}

	private String mockUtilGetMockName(String fieldName) {
		return mockUtil.getMockName(mock(Object.class)).toString();
	}
}