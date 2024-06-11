
package org.mockito.internal.configuration.injection.filter;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockCandidateFilterTest {

    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;

    @Test
    public void testFilterCandidate_WhenValidMock_ThenReturnsOngoingInjecter() {
        // Given
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);
        OngoingInjecter ongoingInjecter = Mockito.mock(OngoingInjecter.class);
        Mockito.when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(ongoingInjecter);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        verify(filter).filter(fieldToBeInjected, fieldInstance);
        Mockito.assertEquals(ongoingInjecter, result);
    }

    @Test
    public void testFilterCandidate_WhenEmptyMockCollection_ThenNullIsReturned() {
        // Given
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);
        Collection<Object> emptyMocks = Collections.emptyList();

        // When
        OngoingInjecter result = filter.filterCandidate(emptyMocks, Mockito.mock(Field.class), Mockito.mock(Object.class));

        // Then
        Mockito.assertNull(result);
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidate_WhenExceptionThrownDuringFiltering_ThenPropagatesException() {
        // Given
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        try {
            Mockito.doThrow(new RuntimeException("Test exception")).when(filter).filter(ArgumentMatchers.any(Field.class), ArgumentMatchers.any(Object.class));
            OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        } catch (InvocationTargetException | UndeclaredThrowableException e) {
            throw e.getCause();
        }
    }
}

