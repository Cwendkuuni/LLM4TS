import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockFilter;
    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;

    @Before
    public void setup() {
        mocks = new ArrayList<>();
        fieldToBeInjected = mock(Field.class);
        fieldInstance = mock(Object.class);
        mockFilter = Mockito.mock(MockCandidateFilter.class);
    }

    @Test
    public void testFilterCandidate() {
        OngoingInjecter result = mockFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        verify(mockFilter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }
}