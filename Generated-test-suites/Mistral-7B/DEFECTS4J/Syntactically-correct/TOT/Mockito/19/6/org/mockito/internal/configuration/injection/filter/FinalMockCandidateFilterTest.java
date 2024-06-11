import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.invocation.InvocationOnMock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Reporter reporter;
    private List<Object> mocks;
    private Object fieldInstance;
    private Field field;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        reporter = mock(Reporter.class);
        mocks = new ArrayList<>();
        fieldInstance = new Object();
        field = new Field("testField", Object.class, fieldInstance.getClass());
    }

    @Test
    public void testSingleMock() {
        Object mock = new Object();
        mocks.add(mock);

        when(filter.filterCandidate(mocks, field, fieldInstance)).thenReturn(mockInjecter());

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object injectedObject = injecter.thenInject();

        verify(reporter, times(0)).cannotInjectDependency(ArgumentMatchers.any(Field.class), ArgumentMatchers.eq(mock), ArgumentMatchers.any(RuntimeException.class));
        assertSame(mock, injectedObject);
    }

    @Test
    public void testMultipleMocks() {
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);

        when(filter.filterCandidate(mocks, field, fieldInstance)).thenReturn(multipleMocksInjecter());

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object injectedObject = injecter.thenInject();

        assertNull(injectedObject);
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionWhenInjecting() {
        Object mock = new Object();
        mocks.add(mock);

        when(filter.filterCandidate(mocks, field, fieldInstance)).thenReturn(exceptionThrowingInjecter());

        filter.filterCandidate(mocks, field, fieldInstance).thenInject();
    }

    @Test
    public void testNoInjectingWhenNoMocks() {
        when(filter.filterCandidate(Collections.emptyList(), field, fieldInstance)).thenReturn(noInjectingInjecter());

        OngoingInjecter injecter = filter.filterCandidate(Collections.emptyList(), field, fieldInstance);
        Object injectedObject = injecter.thenInject();

        assertNull(injectedObject);
    }

    private OngoingInjecter mockInjecter() {
        return mock(OngoingInjecter.class);
    }

    private OngoingInjecter multipleMocksInjecter() {
        return new OngoingInjecter() {
            @Override
            public Object thenInject() {
                return null;
            }
        };
    }

    private OngoingInjecter exceptionThrowingInjecter() {
        return new OngoingInjecter() {
            @Override
            public Object thenInject() throws RuntimeException {
                throw new RuntimeException("Test exception");
            }
        };
    }

    private OngoingInjecter noInjectingInjecter() {
        return new OngoingInjecter() {
            @Override
            public Object thenInject() {
                return null;
            }
        };
    }
}