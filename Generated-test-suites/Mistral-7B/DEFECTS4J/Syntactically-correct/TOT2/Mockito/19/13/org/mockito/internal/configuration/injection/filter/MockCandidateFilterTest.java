import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;
    private OngoingInjecter injectedObject;

    @Before
    public void setup() {
        mocks = new ArrayList<>();
        fieldToBeInjected = mock(Field.class);
        fieldInstance = spy(new Object());
        filter = Mockito.mock(MockCandidateFilter.class);
    }

    @Test
    public void testFilterCandidate() {
        // Given

        // When
        injectedObject = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }
}