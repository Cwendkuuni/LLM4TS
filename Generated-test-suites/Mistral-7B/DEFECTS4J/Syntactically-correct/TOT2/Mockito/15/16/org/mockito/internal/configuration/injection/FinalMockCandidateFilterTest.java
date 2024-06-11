import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        mocks = mock(Collection.class);
        field = mock(Field.class);
        fieldInstance = spy(new Object());
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_OneMock_Success() {
        Object mock = mock(Object.class);
        when(mocks.iterator().next()).thenReturn(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
    }
}