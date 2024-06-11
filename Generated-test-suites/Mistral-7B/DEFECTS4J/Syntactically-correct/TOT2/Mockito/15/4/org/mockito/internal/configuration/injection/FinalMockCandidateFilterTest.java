import org.junit.Test;
import org.mockito.TestAnnotationStrings;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidateWithOneMock() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = spy(new Object());

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Object mock = mocks.iterator().next();
        assertEquals(result, new OngoingInjecter() {
            public boolean thenInject() {
                try {
                    new FieldSetter(fieldInstance, field).set(mock);
                } catch (Exception e) {
                    throw new RuntimeException("Unexpected exception", e); // mockito doesn't throw exceptions, so we simulate it
                }
                return true;
            }
        });
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = spy(new Object());

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(result, new OngoingInjecter() {
            public boolean thenInject() {
                return false;
            }
        });
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithNullCollection() {
        Collection<Object> mocks = null;
        Field field = mock(Field.class);
        Object fieldInstance = spy(new Object());

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullField() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = null;
        Object fieldInstance = spy(new Object());

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullFieldInstance() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = null;

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidateWithEmptyCollectionAndNullField() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = null;

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, new Object());

        assertEquals(result, new OngoingInjecter() {
            public boolean thenInject() {
                return false;
            }
        });
    }

    @Test
    public void testFilterCandidateWithEmptyCollectionAndNonNullField() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, new Object());

        assertEquals(result, new OngoingInjecter() {
            public boolean thenInject() {
                return false;
            }
        });
    }
}