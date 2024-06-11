import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter filter;
    private Field testField;
    private Object testObject;
    private Collection<Object> testMocks;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        testField = new FieldType(int.class, "testField") {};
        testObject = new Object() {};
        testMocks = new ArrayList<>();
        testMocks.add(mock(Object.class));
    }

    @Test
    public void testFilterCandidate() {
        OngoingInjecter result = filter.filterCandidate(testMocks, testField, testObject);
        assertNotNull(result);
    }
}