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

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setup() {
        mockCandidateFilter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
    }

    @Test
    public void testFilterCandidate_nullCollection() {
        Collection<Object> mocks = null;
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        assertNull(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_emptyCollection() {
        Collection<Object> mocks = new ArrayList<>();

        Field fieldToBeInjected = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        Mockito.doAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(mockCandidateFilter).filterCandidate(Mockito.eq(mocks), Mockito.eq(fieldToBeInjected), Mockito.eq(fieldInstance));

        assertNull(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_filterCandidateReturned() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(Object.class));

        Field fieldToBeInjected = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        OngoingInjecter ongoingInjecter = Mockito.mock(OngoingInjecter.class);

        Mockito.doAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                return ongoingInjecter;
            }
        }).when(mockCandidateFilter).filterCandidate(Mockito.eq(mocks), Mockito.eq(fieldToBeInjected), Mockito.eq(fieldInstance));

        assertSame(ongoingInjecter, mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }
}