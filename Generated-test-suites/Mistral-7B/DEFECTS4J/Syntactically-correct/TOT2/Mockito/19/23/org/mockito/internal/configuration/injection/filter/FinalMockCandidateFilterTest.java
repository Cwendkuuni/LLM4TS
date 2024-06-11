import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldType;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;
    @Captor
    ArgumentCaptor<Object> captor;

    @Before
    public void setup() {
        mocks = new ArrayList<>();
        field = mock(Field.class);
        fieldInstance = spy(new Object());
    }

    @Test
    public void testFilterCandidateWithNullCollection() {
        filter = new FinalMockCandidateFilter();
        assertNull(filter.filterCandidate(null, field, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithNullField() {
        filter = new FinalMockCandidateFilter();
        assertNull(filter.filterCandidate(mocks, null, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithEmptyCollection() {
        filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        verify(injecter).thenInject();
        verifyNoMoreInteractions(injecter);
    }

    @Test
    public void testFilterCandidateWithMultipleCandidatesOfDifferentTypes() {
        Object mock1 = mock(Object1.class);
        Object mock2 = mock(Object2.class);
        mocks.add(mock1);
        mocks.add(mock2);

        filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        verify(fieldInstance).set(mock1);
        verify(mock1).equals(injecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithFieldAccessModifiers() {
        field.setAccessible(true); // Set accessible to private field
        Object mock = mock(Object.class);
        mocks.add(mock);

        filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        verify(fieldInstance).set(mock);
        verify(mock).equals(injecter.thenInject());
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionHandlingWhenSettingField() {
        filter = new FinalMockCandidateFilter();
        filter.filterCandidate(mocks, field, new Object() {
            public void set(Object o) {
                throw new RuntimeException();
            }
        });
    }

    @Test
    public void testSettingMockWithWrongType() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, new String());

        assertNull(injecter.thenInject());
    }
}

class Object1 {
}

class Object2 {
}