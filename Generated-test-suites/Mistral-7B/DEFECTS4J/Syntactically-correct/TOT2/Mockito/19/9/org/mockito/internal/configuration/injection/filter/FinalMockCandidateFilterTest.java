import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldType;
import org.mockito.internal.util.reflection.Reflections;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.mockito.internal.util.reflection.FieldType.FIELD;

public class FinalMockCandidateFilterTest {
    @Test
    public void testFilterCandidate() {
        final MockCandidateFilter filter = new FinalMockCandidateFilter();
        final Collection<Object> mocks = Arrays.asList(new MockObject(), new MockObject());
        final Field field = Reflections.getField(MockObject.class, "testField");
        final Object fieldInstance = new MockObject();

        final Object actual = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(actual);
    }
}