package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ExtendedLists;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setUp() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_whenMockTypeMatchesFieldType_thenNextFilterShouldBeCalled() {
        // Given
        Collection<Object> mocks = ExtendedLists.newArrayList(new MockType1(), new MockType2());
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(nextMock).filterCandidate(argThat(c-> ExtendedLists.isSubList(c, ExtendedLists.newArrayList(new MockType1().getClass()))), field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenMockTypeDoesNotMatchFieldType_thenNextFilterShouldNotBeCalled() {
        // Given
        Collection<Object> mocks = ExtendedLists.newArrayList(new MockType1(), new MockType2());
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verifyNoInteractions(nextMock);
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsAssignableFromFirstMockType_thenFirstMockShouldBeReturned() {
        // Given
        Collection<Object> mocks = ExtendedLists.newArrayList(new MockTypeWithFieldType(), new MockType1());
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertThat(result.getMock(), is(instanceOf(MockTypeWithFieldType.class)));
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsAssignableFromSecondMockType_thenNextFilterShouldBeCalled() {
        // Given
        Collection<Object> mocks = ExtendedLists.newArrayList(new MockType1(), new MockTypeWithFieldType());
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(nextMock).filterCandidate(argThat(c-> ExtendedLists.isSubList(c, ExtendedLists.newArrayList(new MockTypeWithFieldType().getClass()))), field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenThereIsNoMockThatMatchesFieldType_thenNullShouldBeReturned() {
        // Given
        Collection<Object> mocks = ExtendedLists.newArrayList(new MockType1(), new MockType2());
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNull(result);
    }
}

class MockType1 {
}

class MockType2 {
}

class MockTypeWithFieldType {
}