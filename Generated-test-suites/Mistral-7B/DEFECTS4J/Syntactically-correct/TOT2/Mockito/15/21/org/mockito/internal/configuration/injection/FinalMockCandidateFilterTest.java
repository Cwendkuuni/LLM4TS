package org.mockito.internal.configuration.injection;

import org.junit.Assert.*;
import org.mockito.*;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.*;

import java.lang.reflect.*;
import java.util.*;

public class FinalMockCandidateFilterTest {

    @org.junit.Test
    public void testFilterCandidate_SingleMock_Success() {
        // Test case for Expert 1
    }

    @org.junit.Test(expected = MockitoException.class)
    public void testFilterCandidate_SingleMock_Exception() {
        // Test case for Expert 1
    }

    @org.junit.Test
    public void testFilterCandidate_MultipleMocks_Failure() {
        // Test case for Expert 1
    }

    @org.junit.Test
    public void testFilterCandidate_EmptyCollection_Failure() {
        // Test case for Expert 1
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_NullCollection() {
        // Test case for Expert 2
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullField() {
        // Test case for Expert 2
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullFieldInstance() {
        // Test case for Expert 2
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullMockType() {
        Collection<Object> mocks = Mockito.mock(Collection.class);
        Mockito.when(mocks.getClass()).thenReturn(String.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, Mockito.mock(Field.class), Mockito.mock(Object.class));

        assertFalse(injecter.thenInject());
    }

    @org.junit.Test(expected = MockitoException.class)
    public void testFilterCandidate_FieldAccessFailed() {
        final Collection<Object> mocks = Mockito.mock(Collection.class);
        Mockito.when(mocks.size()).thenReturn(1);
        final Field field = Mockito.mock(Field.class);
        final Object fieldInstance = Mockito.mock(Object.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Mockito.doThrow(new RuntimeException("Exception")).when(injecter).thenInject();
        injecter.thenInject();
    }
}