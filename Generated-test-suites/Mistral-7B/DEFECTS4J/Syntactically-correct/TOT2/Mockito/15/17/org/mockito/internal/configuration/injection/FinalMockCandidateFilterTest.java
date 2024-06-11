import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock_success() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);
        Collection<Object> mocks = Arrays.asList(Mockito.mock(Object.class));

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(result.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_singleMock_failure() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);
        Collection<Object> mocks = Arrays.asList(Mockito.mock(Object.class));

        new FieldSetter(fieldInstance, field).set(mocks.get(0)); // Exception expected

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_multipleMocks_failure() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);
        Collection<Object> mocks = Arrays.asList(Mockito.mock(Object.class), Mockito.mock(Object.class));

        assertFalse(filter.filterCandidate(mocks, field, fieldInstance).thenInject());
    }
}