import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

import java.lang.reflect.Field;
import java.util.Collection;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate_NullMocks() {
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);
        Object fieldInstance = Mockito.mock(Object.class);
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Collection<Object> mocks = null;

        try {
            filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
            fail("expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }

        Mockito.verifyZeroInteractions(filter);
        Mockito.verifyZeroInteractions(mocks);
        Mockito.verifyZeroInteractions(fieldToBeInjected);
        Mockito.verifyZeroInteractions(fieldInstance);
    }

    @Test
    public void testFilterCandidate_NullFieldToBeInjected() {
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);
        Object fieldInstance = Mockito.mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = null;

        try {
            filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
            fail("expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }

        Mockito.verifyZeroInteractions(filter);
        Mockito.verifyZeroInteractions(mocks);
        Mockito.verifyZeroInteractions(fieldToBeInjected);
        Mockito.verifyZeroInteractions(fieldInstance);
    }

    @Test
    public void testFilterCandidate_NullFieldInstance() {
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = null;

        try {
            filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
            fail("expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }

        Mockito.verifyZeroInteractions(filter);
        Mockito.verifyZeroInteractions(mocks);
        Mockito.verifyZeroInteractions(fieldToBeInjected);
        Mockito.verifyZeroInteractions(fieldInstance);
    }

    @Test
    public void testFilterCandidate_EmptyMocks() {
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);
        Object fieldInstance = Mockito.mock(Object.class);
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Collection<Object> mocks = new ArrayList<>();

        Mockito.when(filter.filterCandidate(Mockito.anyCollection(), ArgumentMatchers.eq(fieldToBeInjected), ArgumentMatchers.eq(fieldInstance)))
                .thenReturn(Mockito.mock(OngoingInjecter.class));

        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Mockito.verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        Mockito.verifyNoMoreInteractions(filter);
        Mockito.verifyNoMoreInteractions(mocks);
        Mockito.verifyNoMoreInteractions(fieldToBeInjected);
        Mockito.verifyNoMoreInteractions(fieldInstance);

        assertNotNull(result);
    }

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);
        Object fieldInstance = Mockito.mock(Object.class);
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Collection<Object> mocks = new ArrayList<>();

        Mockito.when(filter.filterCandidate(Mockito.anyCollection(), ArgumentMatchers.eq(fieldToBeInjected), ArgumentMatchers.eq(fieldInstance)))
                .thenReturn(Mockito.mock(OngoingInjecter.class));

        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Mockito.verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        Mockito.verifyNoMoreInteractions(filter);
        Mockito.verifyNoMoreInteractions(mocks);
        Mockito.verifyNoMoreInteractions(fieldToBeInjected);
        Mockito.verifyNoMoreInteractions(fieldInstance);

        assertNotNull(result);
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidate_Exception() throws Exception {
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);
        Object fieldInstance = Mockito.mock(Object.class);
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Collection<Object> mocks = new ArrayList<>();

        Mockito.when(filter.filterCandidate(Mockito.anyCollection(), ArgumentMatchers.eq(fieldToBeInjected), ArgumentMatchers.eq(fieldInstance)))
                .thenThrow(new RuntimeException("test exception"));

        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Mockito.verify(filter).filterCandidate(Mockito.anyCollection(), ArgumentMatchers.eq(fieldToBeInjected), ArgumentMatchers.eq(fieldInstance));
        Mockito.verifyNoMoreInteractions(filter);
        Mockito.verifyNoMoreInteractions(mocks);
        Mockito.verifyNoMoreInteractions(fieldToBeInjected);
        Mockito.verifyNoMoreInteractions(fieldInstance);
    }

}