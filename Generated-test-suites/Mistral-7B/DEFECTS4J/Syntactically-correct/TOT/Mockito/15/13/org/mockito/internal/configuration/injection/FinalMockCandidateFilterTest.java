import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class FinalMockCandidateFilterTest {

    private static final String FIELD_NAME = "testField";
    private static final Object FIELD_VALUE = new Object();
    private static final Object MOCK_INSTANCE = new Object();
    private static final Object ANOTHER_MOCK_INSTANCE = new Object();
    private static final List<Object> MOCKS = new ArrayList<>(Arrays.asList(MOCK_INSTANCE, ANOTHER_MOCK_INSTANCE));

    private FinalMockCandidateFilter filter;
    private Field field;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        field = getField(MOCK_INSTANCE, FIELD_NAME);
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidateWithSingleMock() {
        // Given
        Object mock = Mockito.spy(MOCK_INSTANCE);

        // When
        OngoingInjecter injecter = filter.filterCandidate(Collections.singletonList(mock), field, MOCK_INSTANCE);

        // Then
        assertTrue(injecter.thenInject());
        assertNotNull(field.get(mock));
        assertNotNull(field.get(MOCK_INSTANCE));
        assertEquals(FIELD_VALUE, field.get(mock));
        assertEquals(FIELD_VALUE, field.get(MOCK_INSTANCE));
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        // Given

        // When
        OngoingInjecter injecter = filter.filterCandidate(MOCKS, field, MOCK_INSTANCE);

        // Then
        assertFalse(injecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithException() throws Exception {
        // Given
        doThrow(new Exception("Test Exception")).when(field).set(ArgumentMatchers.any());

        // When
        OngoingInjecter injecter = filter.filterCandidate(Collections.singletonList(MOCK_INSTANCE), field, MOCK_INSTANCE);

        // Then
        injecter.thenInject();
    }

    private Field getField(Object instance, String fieldName) throws NoSuchFieldException {
        return instance.getClass().getDeclaredField(fieldName);
    }
}