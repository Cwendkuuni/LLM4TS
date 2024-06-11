import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.ReflectUtils;
import org.mockito.mockitoSession;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock() {
        // Given
        MockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = ReflectUtils.findField(Object.class, "mockField");
        Object fieldInstance = mock(Object.class);

        // When
        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(fieldInstance).set(mocks.get(0), field.get(mocks.get(0)));
        verify(mocks.get(0)).setField(fieldInstance);
        assertSame(result, mocks.get(0));
    }

    @Test
    public void testFilterCandidate_noMatchingMock() {
        // Given
        MockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = ReflectUtils.findField(Object.class, "mockField");
        Object fieldInstance = mock(Object.class);

        // When
        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verifyZeroInteractions(fieldInstance);
        assertNull(result);
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidate_exception() {
        // Given
        MockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = ReflectUtils.findField(Object.class, "mockField");
        Object fieldInstance = mock(Object.class);

        // When & Then
        filter.filterCandidate(mocks, field, fieldInstance);
        new Reporter().cannotInjectDependency(field, mocks.get(0), new RuntimeException("Test exception"));
    }
}