import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.TestFields;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        Field testField = TestFields.privateField(FinalMockCandidateFilter.class, "matchingMock");
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Object mock = mock(Object.class);
        Object fieldInstance = spy(new Object());
        Collection<Object> mocks = Collections.singletonList(mock);
        Field field = new Field("testField") {
            public Class<Object> getType() {
                return Object.class;
            }
        };

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        assertEquals(mock, result);
    }
}