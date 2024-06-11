import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ExtendedList;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setup() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_whenMockTypesMatch_thenNextFilterShouldBeCalled() {
        // given
        Collection<Object> mocks = new ExtendedList<>();
        mocks.add(new MockClassA());
        mocks.add(new MockClassB());
        Field field = getField("field");
        Object fieldInstance = new Object();

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        verify(nextMock).filterCandidate(argThat(mock -> mock.getClass().isAssignableFrom(field.getType())), field, fieldInstance);
        assertEquals(result, nextMock.filterCandidate(argThat(mock -> mock.getClass().isAssignableFrom(field.getType())), field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_whenMockTypesDoNotMatch_thenNextFilterShouldNotBeCalled() {
        // given
        Collection<Object> mocks = new ExtendedList<>();
        mocks.add(new MockClassA());
        mocks.add(new MockClassB());
        Field field = getField("field");
        Object fieldInstance = new Object();

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        verifyNoInteractions(nextMock);
        assertEquals(result, null);
    }

    @Test
    public void testFilterCandidate_whenOnlyOneMockMatches_thenItShouldBeReturned() {
        // given
        Collection<Object> mocks = new ExtendedList<>();
        mocks.add(new MockClassA());
        mocks.add(new MockClassB());
        Field field = getField("field");
        Object fieldInstance = new Object();

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        MockClassA mockClassA = (MockClassA) mocks.get(0);
        verify(nextMock).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
        assertEquals(result, mockClassA);
    }

    private Field getField(String name) {
        try {
            return TypeBasedCandidateFilter.class.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}