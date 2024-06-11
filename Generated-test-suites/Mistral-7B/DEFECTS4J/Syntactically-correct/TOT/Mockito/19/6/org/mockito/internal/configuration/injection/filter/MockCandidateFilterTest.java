package org.mockito.internal.configuration.injection.filter.test;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Arrays;
import static org.junit.Assert.*;

public class MockCandidateFilterTest {

    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;
    private MockCandidateFilter filter;

    @Before
    public void setup() {
        mocks = new ArrayList<>();
        fieldToBeInjected = Mockito.mock(Field.class);
        fieldInstance = Mockito.mock(Object.class);
        filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
    }

    @Test
    public void testFilterCandidate_whenFieldIsInTheMocksCollection_thenFilterInstanceIsReturned() {
        mocks.add(filter);
        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertNull(result);
        verify(filter).onInject(fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenFieldIsNotInTheMocksCollection_thenNullIsReturned() {
        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertNull(result);
        verify(filter, never()).onInject(fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenFilterThrowsAnException_thenNullIsReturned() throws Exception {
        Field privateFilterField = filter.getClass().getDeclaredField("filter");
        privateFilterField.setAccessible(true);
        Object filterToReplace = privateFilterField.get(filter);
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                throw new Exception("Filter Exception");
            }
        }).when(filterToReplace).filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any());

        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertNull(result);
        verify(filter, never()).onInject(fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenFilterReturnsANonNullObject_thenItIsReturned() {
        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertNotNull(result);
        verify(filter).onInject(fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenFilterReturnsNull_thenNullIsReturned() {
        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertNull(result);
        verify(filter, never()).onInject(fieldToBeInjected, fieldInstance);
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidate_whenFilterThrowsARuntimeException_thenNullIsReturned() {
        Mockito.doThrow(new RuntimeException("Filter Exception"))
                .when(filter)
                .filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any());

        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertNull(result);
    }
}