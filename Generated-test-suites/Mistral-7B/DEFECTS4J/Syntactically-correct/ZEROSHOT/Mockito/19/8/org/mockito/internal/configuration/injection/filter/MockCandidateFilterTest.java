package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setup() {
        mockCandidateFilter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
    }

    @Test
    public void testFilterCandidateWithNullMocks() {
        Collection<Object> mocks = null;
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        assertNull(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithNullFieldToBeInjected() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = null;
        Object fieldInstance = mock(Object.class);

        assertNull(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithNullFieldInstance() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = null;

        assertNull(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithNoMatchingMock() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = new Object();

        when(mock1.getClass()).thenReturn(Object.class);
        when(mock2.getClass()).thenReturn(String.class);

        assertSame(mock1, mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithMatchingMock() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        Object mockWrapper = mock(Object.class);
        mocks.add(mock);
        mocks.add(mockWrapper);

        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = new Object();

        when(mock.getClass()).thenReturn(String.class);

        Answer<Object> answer = answer(mock);
        when(mockCandidateFilter).doAnswer(answer).when(mockCandidateFilter).filterCandidate(anyCollection(), any(Field.class), any(Object.class));

        Object result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertSame(mock, result);
        verify(mockCandidateFilter, times(1)).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }
}