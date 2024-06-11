package org.mockito.internal.configuration.injection.filter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.assertThrows;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter filter;
    private Object mock1;
    private Object mock2;
    private Object mock3;
    private Object fieldInstance;

    @Before
    public void setup() {
        mock1 = mock(Object.class);
        mock2 = mock(Object.class);
        mock3 = mock(Object.class);
        fieldInstance = new Object();

        filter = new TypeBasedCandidateFilter(new IgnoringTypeBasedCandidateFilter()) {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return super.filterCandidate(mocks, field, fieldInstance);
            }
        };
    }

    @Test
    public void testFilterCandidate_whenMockTypeMatches_thenReturnFilteredMock() {
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        Field field = new Field() {
            @Override
            public Class<?> getType() {
                return Object.class;
            }
        };

        Object filteredMock = filter.filterCandidate(mocks, field, fieldInstance);

        verify(filter).filterCandidate(Collections.singletonList(mock1), field, fieldInstance);
        verify(mock1).toString();
        assertEquals(mock1, filteredMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_whenFieldTypeDoesNotMatchAnyMock_thenThrowException() {
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        Field field = new Field() {
            @Override
            public Class<?> getType() {
                return String.class;
            }
        };

        assertThrows(IllegalArgumentException.class, () -> filter.filterCandidate(mocks, field, fieldInstance));
    }
}

class IgnoringTypeBasedCandidateFilter implements MockCandidateFilter {
    @Override
    public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
        return null;
    }
}