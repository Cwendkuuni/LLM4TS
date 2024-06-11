import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Field;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        fieldInstance = mock(Object.class);
        filter = mock(MockCandidateFilter.class);
    }

    @Test
    public void testFilterCandidate() {
        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(injectedObject);
        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }
}