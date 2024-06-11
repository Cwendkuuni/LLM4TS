package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ExtendedArrayList;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setUp() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsAssignableFromMockedObjectType_thenReturnNextFilter() {
        // given
        Collection<Object> mocks = new ExtendedArrayList<>();
        mocks.add(mock(String.class));
        mocks.add(mock(Object.class));

        Field field = Mockito.field(Object.class, "field");
        Object fieldInstance = new Object();

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        Mockito.verify(nextMock).filterCandidate(Mockito.argThat(c -> c.contains(Mockito.isInstance(field.getType()))), field, fieldInstance);
        assertEquals(nextMock, result.getFilter());
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsNotAssignableFromMockedObjectType_thenReturnEmptyList() {
        // given
        Collection<Object> mocks = new ExtendedArrayList<>();
        mocks.add(mock(String.class));
        mocks.add(mock(Object.class));

        Field field = Mockito.field(Object.class, "field");
        Object fieldInstance = new Object();

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        Mockito.verifyNoMoreInteractions(nextMock);
        assertEquals(new ArrayList<>(), result.getCandidates());
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsAssignableFromMockedObjectType_andNextFilterReturnsNull_thenReturnNull() {
        // given
        Collection<Object> mocks = new ExtendedArrayList<>();
        mocks.add(mock(String.class));
        mocks.add(mock(Object.class));

        Field field = Mockito.field(Object.class, "field");
        Object fieldInstance = new Object();

        Mockito.when(nextMock.filterCandidate(Mockito.any(Collection.class), Mockito.eq(field), Mockito.eq(fieldInstance)))
                .thenReturn(null);

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        Mockito.verify(nextMock).filterCandidate(Mockito.argThat(c -> c.contains(Mockito.isInstance(field.getType()))), field, fieldInstance);
        Mockito.verifyNoMoreInteractions(nextMock);
        assertEquals(null, result);
    }
}