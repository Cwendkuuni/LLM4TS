import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidateWithSingleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = Mockito.mock(Object.class);
        Object fieldInstance = new Object();
        Field field = new Field(Object.class, "testField");

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertTrue(result instanceof OngoingInjecter);
        OngoingInjecter ongoingInjecter = (OngoingInjecter) result;
        assertTrue(ongoingInjecter.thenInject());
    }
}