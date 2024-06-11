package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockCandidateFilter;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter nextMock;
    private TypeBasedCandidateFilter filterUnderTest;

    @Before
    public void setUp() {
        nextMock = mock(MockCandidateFilter.class);
        filterUnderTest = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_whenMocksAreTypeMatches_thenNextFilterIsCalled() {
        // Given
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(String.class);
        Object mock2 = mock(Integer.class);
        mocks.add(mock1);
        mocks.add(mock2);

        // When
        OngoingInjecter result = filterUnderTest.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(nextMock).filterCandidate(argThat(m -> m.contains(mock1)), field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenMocksAreNotTypeMatches_thenNextFilterIsNotCalled() {
        // Given
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(String.class);
        Object mock2 = mock(Object.class); // Not assignable from field's type
        mocks.add(mock1);
        mocks.add(mock2);

        // When
        filterUnderTest.filterCandidate(mocks, field, fieldInstance);

        // Then
        verifyNoInteractions(nextMock);
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsAnInterface_thenAllImplementationsAreConsidered() {
        // Given
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(AnInterface.class);
        Object mock2 = mock(AnotherImplementationOfAnInterface.class);
        mocks.add(mock1);
        mocks.add(mock2);

        // When
        OngoingInjecter result = filterUnderTest.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(nextMock).filterCandidate(argThat(m -> m.contains(mock1) || m.contains(mock2)), field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsAnAbstractClass_thenAllSubclassesAreConsidered() {
        // Given
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        Object mockBaseClass = mock(ABaseClass.class);
        Object mockSubclass1 = mock(ASubclass1.class);
        Object mockSubclass2 = mock(ASubclass2.class);
        mocks.add(mockBaseClass);
        mocks.add(mockSubclass1);
        mocks.add(mockSubclass2);

        // When
        OngoingInjecter result = filterUnderTest.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(nextMock).filterCandidate(argThat(m -> m.contains(mockBaseClass) || m.contains(mockSubclass1) || m.contains(mockSubclass2)), field, fieldInstance);
    }
}