
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.FieldSetter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class FinalMockCandidateFilterTest {

    private Collection<Object> mocks;
    private Object fieldInstance;
    private Field field;

    @Before
    public void setUp() {
        mocks = new ArrayList<>();
        fieldInstance = mock(String.class);
        field = Mockito.spy(Field.class);
    }

    @Test
    public void testSingleMock() {
        Object mock = mock(ClassWithField.class);
        mocks.add(mock);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        // Verify that the field was set on the mock
        verify(mock).someMethodThatCanBeCalledAfterFieldSet(fieldInstance);
    }

    @Test(expected = MockitoException.class)
    public void testExceptionWhenSettingField() {
        Object mock = new Object() {
            public void setField(Object value) throws Exception {
                throw new Exception();
            }
        };
        mocks.add(mock);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();
    }

    @Test
    public void testNoMatchingMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        // No exception should be thrown and nothing should be injected
        verifyNoMoreInteractions(mocks);
    }

    @Test
    public void testFieldAlreadySet() {
        Object mock = mock(ClassWithField.class);
        mocks.add(mock);

        // Set the field in the mock before the test
        FieldSetter setter = new FieldSetter(mock, field);
        setter.set(fieldInstance);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        // No exception should be thrown and nothing should be injected since the field is already set
        verifyNoMoreInteractions(mocks);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEmptyMockCollection() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        try {
            filter.filterCandidate(Collections.emptyList(), field, fieldInstance);
        } catch (MockitoException e) {
            throw new AssertionError("Unexpected MockitoException", e);
        }

        // An UnsupportedOperationException should be thrown instead since the method is not supported with an empty collection
    }
}

class ClassWithField {
    private String field;

    public void someMethodThatCanBeCalledAfterFieldSet(String field) {
        // Some method that can be called after the field is set
    }
}
