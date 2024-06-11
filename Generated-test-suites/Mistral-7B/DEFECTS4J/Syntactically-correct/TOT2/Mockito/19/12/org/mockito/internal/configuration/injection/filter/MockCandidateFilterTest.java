import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;

    @Before
    public void setup() {
        filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
    }

    @Test
    public void testFilterCandidateWithNullMocks() {
        Collection<Object> mocks = null;
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithNullFieldToBeInjected() {
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field fieldToBeInjected = null;
        Object fieldInstance = mock(Object.class);

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithNullFieldInstance() {
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = null;

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithInjectedField() {
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        when(fieldToBeInjected.get(fieldInstance)).thenReturn(new Object());

        assertSame(fieldInstance, filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidateWithException() {
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        filter = new ExceptionThrowingMockCandidateFilter();

        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidate() {
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        assertSame(fieldInstance, filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }
}

class ExceptionThrowingMockCandidateFilter implements MockCandidateFilter {
    @Override
    public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
        throw new RuntimeException("Test exception");
    }
}