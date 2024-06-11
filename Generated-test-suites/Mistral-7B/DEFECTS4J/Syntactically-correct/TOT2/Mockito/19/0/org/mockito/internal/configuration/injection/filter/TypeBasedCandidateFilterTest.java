import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TypeBasedCandidateFilterTest {

    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;
    private MockCandidateFilter filter;

    @Before
    public void setup() {
        mocks = new ArrayList<>();
        field = Mockito.mock(Field.class);
        fieldInstance = Mockito.mock(Object.class);
        filter = new TypeBasedCandidateFilter(null);
    }

    @Test
    public void testConstructor() {
        assertNotNull(filter);
    }

    @Test
    public void testConstructorWithNext() {
        MockCandidateFilter nextInstance = Mockito.mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextInstance);
        assertNotNull(filter);
        verify(nextInstance).filterCandidate(ArgumentMatchers.anyCollection(), eq(null), eq(null));
    }

    @Test
    public void testFilterCandidateEmptyMocks() {
        List<Object> result = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFilterCandidateNoMatch() {
        mocks.add(new String("mock1"));
        List<Object> result = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFilterCandidateOneMatch() {
        mocks.add(new MockitoTestClass());
        List<Object> result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(1, result.size());
        assertEquals(mocks.get(0), result.get(0));
    }

    @Test
    public void testFilterCandidateNullMocks() {
        List<Object> result = filter.filterCandidate(null, field, fieldInstance);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFilterCandidateNullField() {
        mocks.add(new MockitoTestClass());
        List<Object> result = filter.filterCandidate(mocks, null, fieldInstance);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFilterCandidateNullFieldInstance() {
        mocks.add(new MockitoTestClass());
        List<Object> result = filter.filterCandidate(mocks, field, null);

        assertTrue(result.isEmpty());
    }
}