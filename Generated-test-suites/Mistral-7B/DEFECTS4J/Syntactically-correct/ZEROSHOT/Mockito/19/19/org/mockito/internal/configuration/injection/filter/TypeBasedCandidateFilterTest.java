package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ExtendedCollections;

import java.lang.reflect.Field;
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
        Class<?> mockType = String.class;
        Class<?> fieldType = String[].class;
        Collection<Object> mocks = ExtendedCollections.toList(new Object[]{new String(), new Object()});
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = new Object();
        Object expectedMock = new String();

        // When
        Object actualMock = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertTrue(fieldType.isAssignableFrom(mockType));
        assertEquals(expectedMock, actualMock);
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsNotAssignableFromMockType_thenNextFilterIsCalled() {
        // Given
        Class<?> mockType = Integer.class;
        Class<?> fieldType = String[].class;
        Collection<Object> mocks = ExtendedCollections.toList(new Object[]{new String(), new Integer()});
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = new Object();

        // When
        Mockito.when(nextMock.filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any()))
                .thenReturn(Mockito.mock(OngoingInjecter.class));

        Object actualMock = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNotSame(mockType, actualMock);
        Mockito.verify(nextMock).filterCandidate(mocks, field, fieldInstance);
    }
}