package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter.OngoingInjecter;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private TypeBasedCandidateFilter nextMock;

    @Before
    public void setUp() {
        nextMock = mock(TypeBasedCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_emptyMocks_returnsNull() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(result);
    }

    @Test
    public void testFilterCandidate_mocksOfDifferentTypes_returnsNextFilter() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        mocks.add(mock(Integer.class));

        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        Field typeField = mock(Field.class);
        typeField.setType(Integer.class);

        when(nextMock.filterCandidate(anyList(), eq(field), eq(fieldInstance))).thenReturn(mock(OngoingInjecter.class));

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextMock).filterCandidate(argThat(type -> type.contains(any(Integer.class))), eq(field), eq(fieldInstance));
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_mocksOfSameType_returnsFirstMatchingMock() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Integer.class));
        mocks.add(mock(String.class));
        mocks.add(mock(Integer.class));

        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        List<Object> mockTypeMatches = new ArrayList<>();
        mockTypeMatches.add(mocks.get(1)); // String mock should be filtered out

        when(nextMock.filterCandidate(argThat(type -> type.isEmpty()), eq(field), eq(fieldInstance))).thenReturn(mock(OngoingInjecter.class));

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextMock).filterCandidate(eq(mockTypeMatches), eq(field), eq(fieldInstance));
        assertSame(mocks.get(1), result.getMock());
    }
}