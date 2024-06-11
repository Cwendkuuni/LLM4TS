package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;
    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;

    @Before
    public void setup() {
        mockCandidateFilter = Mockito.mock(MockCandidateFilter.class);
        mocks = new ArrayList<>();
        fieldToBeInjected = Mockito.spy(Mockito.mock(Field.class));
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate_EmptyMocksCollection() {
        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                throw new RuntimeException("Mock collection is empty.");
            }
        });

        verify(mockCandidateFilter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        verify(mockCandidateFilter).throwRuntimeException(Mockito.eq(mocks), Mockito.eq(fieldToBeInjected), Mockito.eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_FilterCandidateCall() {
        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(Mockito.mock(OngoingInjecter.class));

        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object mock = invocation.getArguments()[0];
                Mockito.verify(mock).someMockMethod();
                return null;
            }
        }).when(fieldToBeInjected).setValue(Mockito.any());

        mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        verify(mockCandidateFilter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidate_ThrowsRuntimeException() {
        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                throw new RuntimeException("Exception message.");
            }
        });

        mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testThrowRuntimeException() {
        mockCandidateFilter.throwRuntimeException(mocks, fieldToBeInjected, fieldInstance);
        verify(mockCandidateFilter).throwRuntimeException(mocks, fieldToBeInjected, fieldInstance);
    }

}