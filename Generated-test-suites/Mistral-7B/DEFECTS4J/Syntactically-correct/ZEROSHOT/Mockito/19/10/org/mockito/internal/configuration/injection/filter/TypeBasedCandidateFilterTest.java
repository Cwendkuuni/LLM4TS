package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.quality.strategies.TypeFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void init() {
        nextMock = Mockito.mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsAssignableFromMockType_thenMockIsAddedToList() {
        Class<?> fieldType = String.class;
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        mocks.add(mock(Integer.class));

        List<Object> expectedMockTypeMatches = new ArrayList<>();
        expectedMockTypeMatches.add(mock(String.class));

        Field field = Mockito.spy(new Field("testField", String.class, null, null));
        Object fieldInstance = new Object();

        List<Object> actualMockTypeMatches = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(expectedMockTypeMatches, actualMockTypeMatches);
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsNotAssignableFromMockType_thenMockIsNotAddedToList() {
        Class<?> fieldType = String.class;
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        mocks.add(mock(Integer.class));

        List<Object> expectedMockTypeMatches = Collections.emptyList();

        Field field = Mockito.spy(new Field("testField", String.class, null, null));
        Object fieldInstance = new Object();

        Field anotherField = Mockito.spy(new Field("anotherTestField", Integer.class, null, null));
        Object anotherFieldInstance = new Object();

        filter.filterCandidate(mocks, field, fieldInstance);
        filter.filterCandidate(mocks, anotherField, anotherFieldInstance);

        List<Object> actualMockTypeMatches = nextMock.filterCandidate(mocks, field, fieldInstance);

        assertEquals(expectedMockTypeMatches, actualMockTypeMatches);
        assertEquals(1, mocks.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_whenMocksAreNull_thenIllegalArgumentExceptionIsThrown() {
        filter.filterCandidate(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_whenFieldIsNull_thenIllegalArgumentExceptionIsThrown() {
        List<Object> mocks = new ArrayList<>();
        filter.filterCandidate(mocks, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_whenFieldInstanceIsNull_thenIllegalArgumentExceptionIsThrown() {
        List<Object> mocks = new ArrayList<>();
        filter.filterCandidate(mocks, new Field("testField", String.class, null, null), null);
    }
}