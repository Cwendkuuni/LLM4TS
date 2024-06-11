import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stub.Stub;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {
    @Test
    public void testFilterCandidateWithSingleMock() {
        MockCandidateFilter filter = new FinalMockCandidateFilter();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(fieldInstance).getClass();
        verify(field).set(result, result);
        verify(mocks.get(0)).getClass();
    }
}