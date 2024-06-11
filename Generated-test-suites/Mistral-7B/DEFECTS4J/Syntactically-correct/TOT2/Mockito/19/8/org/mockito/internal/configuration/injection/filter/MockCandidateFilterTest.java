import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;

    @Before
    public void setup() {
        mocks = new ArrayList<>();
        fieldToBeInjected = Mockito.spy(Field.class);
        fieldInstance = mock(Object.class);
        filter = Mockito.spy(MockCandidateFilter.class);
    }

    @Test
    public void testFilterCandidate() {
        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        verify(filter).filterCandidate(mocks.toArray(), fieldToBeInjected, fieldInstance);
        verify(filter).injectFields(result);
    }
}