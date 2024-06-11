package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.collections.Iterators;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setup() {
        nextMock = Mockito.mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsAssignableFromMockType_thenMockIsReturned() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(String.class));
        mocks.add(Mockito.mock(Integer.class));

        Field field = Whitebox.getInternalState(this, "field");
        Object fieldInstance = new Object();

        // When
        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertTrue(field.getType().isAssignableFrom(mocks.get(0).getClass()));
        assertEquals(mocks.get(0), result);
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsNotAssignableFromMockType_thenNextFilterIsCalled() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(String.class));
        mocks.add(Mockito.mock(Integer.class));

        Field field = Whitebox.getInternalState(this, "field");
        Object fieldInstance = new Object();

        // When
        Mockito.when(nextMock.filterCandidate(Iterators.getOnlyElement(mocks), field, fieldInstance)).thenReturn(new Object());

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNotSame(mocks.get(0), result);
        assertNotSame(mocks.get(1), result);
        Mockito.verify(nextMock).filterCandidate(Iterators.getOnlyElement(mocks), field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenNoMockMatches_thenNullIsReturned() {
        // Given
        Collection<Object> mocks = new ArrayList<>();

        Field field = Whitebox.getInternalState(this, "field");
        Object fieldInstance = new Object();

        // When
        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_whenFilterCandidateThrowsException_thenExceptionIsThrown() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(String.class));
        mocks.add(Mockito.mock(Integer.class));

        Field field = Whitebox.getInternalState(this, "field");
        Object fieldInstance = new Object();

        Mockito.when(nextMock.filterCandidate(Iterators.getOnlyElement(mocks), field, fieldInstance))
                .thenThrow(new RuntimeException());

        // When & Then
        assertThrows(RuntimeException.class, () -> filter.filterCandidate(mocks, field, fieldInstance));
    }
}