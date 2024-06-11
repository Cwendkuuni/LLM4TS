import static org.mockito.Mockito.*;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertNotNull(result);
        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }
}