package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ExtendedCollections;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setUp() {
        nextMock = Mockito.mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_WhenFieldTypeIsAssignableFromMockedType_ThenReturnMatchingMock() {
        // given
        Collection<Object> mocks = ExtendedCollections.newArrayList();
        mocks.add(mock(String.class));
        mocks.add(mock(List.class));
        mocks.add(mock(TypeBasedCandidateFilter.class));

        Field field = Mockito.fieldogsGet(this, "field");

        Object fieldInstance = new Object() {
        };

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertEquals(mock(String.class).getClass(), result.getMock().getClass());
    }

    @Test
    public void testFilterCandidate_WhenFieldTypeIsNotAssignableFromMockedType_ThenReturnNextFilterResult() throws IllegalAccessException, InvocationTargetException {
        // given
        Collection<Object> mocks = ExtendedCollections.newArrayList();
        mocks.add(mock(String.class));
        mocks.add(mock(List.class));
        mocks.add(mock(TypeBasedCandidateFilter.class));

        Field field = Mockito.fieldogsGet(this, "field");

        Object fieldInstance = new Object() {
        };

        Mockito.doReturn(mock(Integer.class)).when(nextMock).filterCandidate(Mockito.eq(ExtendedCollections.newArrayList(mock(String.class), mock(List.class)), Mockito.eq(field), Mockito.eq(fieldInstance)));

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        Mockito.verify(nextMock).filterCandidate(Mockito.eq(ExtendedCollections.newArrayList(mock(String.class), mock(List.class), mock(TypeBasedCandidateFilter.class)), Mockito.eq(field), Mockito.eq(fieldInstance)));
        assertEquals(mock(Integer.class).getClass(), result.getMock().getClass());
    }
}