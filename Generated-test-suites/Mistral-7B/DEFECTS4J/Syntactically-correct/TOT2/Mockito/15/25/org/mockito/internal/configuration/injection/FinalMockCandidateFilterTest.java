import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.mockitoSession;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Object mockObject;
    private Object fieldInstance;
    private Field field;

    @Before
    public void setup() {
        mockObject = mockitoSession.startMock(Object.class);
        fieldInstance = new Object();
        field = new Field("testField", Object.class, mockObject.getClass());
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidateSingleMock() {
        Collection<Object> mocks = Arrays.asList(mockObject);

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assert(actualInjecter instanceof OngoingInjecter);
        assertTrue(actualInjecter.thenInject());

        new FieldSetter(fieldInstance, field).set(mockObject);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateException() {
        Collection<Object> mocks = Arrays.asList(mockObject);

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assert(actualInjecter instanceof OngoingInjecter);
        try {
            new FieldSetter(fieldInstance, field).set(null);
        } catch (Exception e) {
            throw new MockitoException("Problems injecting dependency in " + field.getName(), e);
        }
        assertFalse(actualInjecter.thenInject());
    }

    @Test
    public void testFilterCandidateMultipleMocks() {
        Collection<Object> mocks = Arrays.asList(mockObject, new Object());

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assert(actualInjecter instanceof OngoingInjecter);
        assertFalse(actualInjecter.thenInject());
    }

    @Test
    public void testFilterCandidateEmptyCollection() {
        Collection<Object> mocks = Collections.emptyList();

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assert(actualInjecter instanceof OngoingInjecter);
        assertFalse(actualInjecter.thenInject());
    }

    @Test
    public void testFilterCandidateNullCollection() {
        Collection<Object> mocks = null;

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assert(actualInjecter instanceof OngoingInjecter);
        assertFalse(actualInjecter.thenInject());
    }

    @Test
    public void testFilterCandidateNullField() {
        Collection<Object> mocks = Arrays.asList(mockObject);
        Field nullField = null;

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, nullField, fieldInstance);

        assert(actualInjecter instanceof OngoingInjecter);
        assertFalse(actualInjecter.thenInject());
    }

    @Test
    public void testFilterCandidateNullFieldInstance() {
        Collection<Object> mocks = Arrays.asList(mockObject);

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, null);

        assert(actualInjecter instanceof OngoingInjecter);
        assertFalse(actualInjecter.thenInject());
    }
}