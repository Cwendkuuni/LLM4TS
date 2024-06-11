import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.stubs.MockFunction;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {
    private Collection<Object> mocks;
    private Object fieldInstance;
    private Field field;

    @Before
    public void setup() {
        mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        fieldInstance = mock(Object.class);
        field = new Field("testField", Object.class) {
            @Override
            public Object get(Object obj) {
                return null;
            }

            @Override
            public void set(Object obj, Object value) {
                when(obj).then(new MockFunction<Object, Object>() {
                    @Override
                    public Object apply(Object input) {
                        setActualValue(value);
                        return null;
                    }
                });
            }
        };
    }

    @Test
    public void testSingleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        verify(mocks.get(0)).equals(fieldInstance);
        verifyZeroInteractions(mocks.get(1));
    }

    @Test
    public void testMultipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        verifyZeroInteractions(mocks);
    }

    @Test(expected = MockitoException.class)
    public void testExceptionWhenInjecting() throws Exception {
        FieldSetter setter = new FieldSetter(fieldInstance, field);
        when(mocks.get(0)).then(new MockFunction<Object, Object>() {
            @Override
            public Object apply(Object input) {
                setter.set("notMockedObject", new Object());
                throw new Exception("Test Exception");
            }
        });

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        // This test should throw an exception
    }
}