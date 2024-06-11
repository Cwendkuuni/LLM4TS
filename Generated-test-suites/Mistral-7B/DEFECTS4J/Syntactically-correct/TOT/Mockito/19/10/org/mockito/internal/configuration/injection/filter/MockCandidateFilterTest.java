package org.mockito.internal.configuration.injection.filter;

import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidateWithValidArguments() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = Mockito.mock(Object.class);
        Field fieldToBeInjected = Mockito.mock(Field.class);
        MockCandidateFilter filter = Mockito.spy(Mockito.any(MockCandidateFilter.class));

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        assertNotNull(result);
        Mockito.verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        Mockito.verifyNoMoreInteractions(filter);
        Mockito.verifyNoInteractions(mocks);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullMocks() {
        // Given
        Collection<Object> mocks = null;
        Object fieldInstance = Mockito.mock(Object.class);
        Field fieldToBeInjected = Mockito.mock(Field.class);
        MockCandidateFilter filter = Mockito.spy(Mockito.any(MockCandidateFilter.class));

        // When
        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        Mockito.verifyNoInteractions(filter);
        Mockito.verifyNoInteractions(fieldToBeInjected);
        Mockito.verifyNoInteractions(fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullField() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = Mockito.mock(Object.class);
        Field fieldToBeInjected = null;
        MockCandidateFilter filter = Mockito.spy(Mockito.any(MockCandidateFilter.class));

        // When
        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        Mockito.verifyNoInteractions(filter);
        Mockito.verifyNoInteractions(mocks);
        Mockito.verifyNoInteractions(fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullFieldInstance() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = null;
        Field fieldToBeInjected = Mockito.mock(Field.class);
        MockCandidateFilter filter = Mockito.spy(Mockito.any(MockCandidateFilter.class));

        // When
        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        Mockito.verifyNoInteractions(filter);
        Mockito.verifyNoInteractions(mocks);
        Mockito.verifyNoInteractions(fieldToBeInjected);
    }
}