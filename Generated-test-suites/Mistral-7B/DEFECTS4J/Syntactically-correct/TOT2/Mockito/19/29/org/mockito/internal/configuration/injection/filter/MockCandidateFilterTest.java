import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.stubbing.OngoingStubbing;

import java.util.ArrayList;
import java.util.Collection;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = null; //replace with a real field
        Object fieldInstance = new Object();
        OngoingStubbing<MockCandidateFilter> ongoingStubbing = Mockito.doReturn(fieldInstance).when(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Mockito.verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        ongoingStubbing.thenReturn(result);

        assertSame(result, fieldInstance);
    }

}