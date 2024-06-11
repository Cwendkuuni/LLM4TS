package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.util.Collections;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;
    private Object fieldInstance;

    @Before
    public void setup() {
        mockCandidateFilter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate_WhenFieldIsAccessible_ThenFilteredMockIsReturned() {
        // given
        Field fieldToBeInjected = Mockito.spy(Mockito.mock(Field.class));
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));

        // when
        Object filteredMock = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // then
        assertSame(filteredMock, mocks.get(0));
        verify(mockCandidateFilter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidate_WhenFieldIsNotAccessible_ThenOriginalInstanceIsReturned() {
        // given
        Field fieldToBeInjected = Mockito.spy(Mockito.mock(Field.class));
        fieldToBeInjected.setAccessible(false);
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));

        // when
        Object originalInstance = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // then
        assertSame(originalInstance, fieldInstance);
        verify(mockCandidateFilter, Mockito.never()).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidate_WhenFieldIsAlreadyInjected_ThenOriginalInstanceIsReturned() {
        // given
        Field fieldToBeInjected = Mockito.spy(Mockito.mock(Field.class));
        when(fieldToBeInjected.get(fieldInstance)).thenReturn(mock(Object.class));
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));

        // when
        Object originalInstance = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // then
        assertSame(originalInstance, fieldInstance);
        verify(mockCandidateFilter, Mockito.never()).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidate_WhenFieldIsInjectedWithAnswer_ThenFilteredMockIsReturned() {
        // given
        Field fieldToBeInjected = Mockito.spy(Mockito.mock(Field.class));
        Object mockToReplace = mock(Object.class);
        Answer<Object> answer = Mockito.answer(mockToReplace);
        when(fieldToBeInjected.getType()).thenReturn(mock(Class.class));
        when(fieldToBeInjected.get(fieldInstance)).thenAnswer(answer);
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));

        // when
        Object filteredMock = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // then
        assertSame(filteredMock, mockToReplace);
        verify(mockCandidateFilter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // verify that answer was called with the expected arguments
        verify(answer).answer(Mockito.eq(mocks), Mockito.eq(fieldToBeInjected), Mockito.eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_WhenFieldIsInjectedWithInvocationOnMock_ThenFilteredMockIsReturned() {
        // given
        Field fieldToBeInjected = Mockito.spy(Mockito.mock(Field.class));
        Object mockToReplace = mock(Object.class);
        InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
        when(fieldToBeInjected.getType()).thenReturn(mock(Class.class));
        when(fieldToBeInjected.get(fieldInstance)).thenReturn(invocationOnMock);
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));

        // when
        Object filteredMock = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // then
        assertSame(filteredMock, mockToReplace.getMockForConcrete());
        verify(mockCandidateFilter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // verify that invocationOnMock was called with the expected arguments
        verify(invocationOnMock).callRealMethod();
    }

}