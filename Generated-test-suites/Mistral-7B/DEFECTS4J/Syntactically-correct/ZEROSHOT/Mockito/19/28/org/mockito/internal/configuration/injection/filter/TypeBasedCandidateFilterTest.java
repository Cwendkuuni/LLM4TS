package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.mock.MockComponent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private TypeBasedCandidateFilter nextFilter;
    @MockComponent
    private MockCandidateFilter nextMockFilter;

    @Before
    public void setUp() {
        nextFilter = new TypeBasedCandidateFilter(nextMockFilter);
        filter = new TypeBasedCandidateFilter(nextFilter);
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsAssignableFromMock_thenReturnFilteredMock() {
        // given
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(String.class);
        Object mock2 = mock(Integer.class);
        Object mock3 = mock(Field.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        Field field = new Field() {
            public Class<?> getType() {
                return Field.class;
            }
        };
        Object fieldInstance = mock(Object.class);

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertEquals(mock3, result.getMock());
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsNotAssignableFromMock_thenReturnNextFilterResult() {
        // given
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(String.class);
        Object mock2 = mock(Integer.class);
        Object mock3 = mock(Field.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        Field field = new Field() {
            public Class<?> getType() {
                return String.class;
            }
        };
        Object fieldInstance = mock(Object.class);

        // when
        OngoingInjecter nextResult = nextFilter.filterCandidate(new ArrayList<>(), field, fieldInstance);

        // then
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(nextResult.getMock(), result.getMock());
    }

    @Test
    public void testFilterCandidate_whenNextFilterIsNull_thenReturnNull() {
        // given
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = new Field() {
            public Class<?> getType() {
                return Object.class;
            }
        };
        Object fieldInstance = mock(Object.class);

        // when
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);

        // then
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(null, result);
    }
}