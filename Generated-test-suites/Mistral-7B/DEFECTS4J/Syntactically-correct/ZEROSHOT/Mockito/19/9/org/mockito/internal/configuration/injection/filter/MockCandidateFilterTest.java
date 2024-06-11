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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;
    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;

    @Before
    public void setUp() {
        mockCandidateFilter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
        mocks = new ArrayList<>();
        try {
            fieldToBeInjected = Mockito.class.getDeclaredField("mocks");
            fieldToBeInjected.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Mockito class changed", e);
        }
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate() throws IllegalAccessException {
        // Given
        Object spy = mock(Object.class, new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object mockedObject = invocation.getMock();
                mocks.add(mockedObject);
                return mockedObject;
            }
        });

        // When
        Object filteredObject = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        assertSame(filteredObject, fieldInstance);
        verify(mockCandidateFilter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        verify(mockCandidateFilter).filterCandidate(eq(mocks), eq(fieldToBeInjected), eq(fieldInstance));
        verify(mockCandidateFilter, times(1)).filterCandidate(anyCollection(), eq(fieldToBeInjected), eq(fieldInstance));

        Object secondMock = mock(Object.class, new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object mockedObject = invocation.getMock();
                mocks.add(mockedObject);
                return mockedObject;
            }
        });

        mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, secondMock);

        assertSame(2, mocks.size());
    }

}