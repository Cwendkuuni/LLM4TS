import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Object mock1;
    private Object mock2;
    private Object fieldInstance;
    private Field field;

    @Before
    public void setup() {
        mock1 = mock(Object.class);
        mock2 = mock(Object.class);
        fieldInstance = mock(Object.class);
        field = Mockito.spy(Mockito.mock(Field.class));
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object injectedMock = injecter.thenInject();

        verify(mock1).setField(fieldInstance, field.get(mock1));
        verify(mock1).setFieldValue(fieldInstance, field.get(mock1), any(FieldType.class));
        verify(mock1).setPropertyValue(fieldInstance, field.getName(), any(Object.class));
        verify(mock1).setPropertyValue(fieldInstance, field.getName(), any(Class.class), any(Object.class));
        verify(fieldInstance).setField(injectedMock, field);
        verify(fieldInstance).setFieldValue(injectedMock, field, any(Object.class));
        verify(fieldInstance).setPropertyValue(injectedMock, field.getName(), any(Object.class));
        verify(fieldInstance).setPropertyValue(injectedMock, field.getName(), any(Class.class), any(Object.class));

        assertSame(injectedMock, injecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        filter = spy(new FinalMockCandidateFilter());
        doThrow(new RuntimeException("Test exception")).when(filter).new Reporter();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        verify(filter).new Reporter();
        verify(filter, never()).new BeanPropertySetter(any(), any(), any());
        verify(filter, never()).new FieldSetter(any(), any());

        assertNull(injecter.thenInject());
    }

    @Test(expected = RuntimeException.class)
    public void testThrowsReporterExceptionWhenCantInjectDependency() {
        List<Object> mocks = Collections.singletonList(mock1);

        filter = spy(new FinalMockCandidateFilter());
        doThrow(new RuntimeException("Test exception")).when(filter).new Reporter();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();
    }

    @Test
    public void testPropertySetterInjectsFirst() {
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);

        filter = spy(new FinalMockCandidateFilter());

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        verify(mock1).setPropertyValue(fieldInstance, field.getName(), any(Object.class));

        assertSame(mock1, injecter.thenInject());
    }

    @Test
    public void testFieldAccessInjectsSecond() {
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        filter = spy(new FinalMockCandidateFilter());

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        verify(fieldInstance, never()).setField(mock1, any());
        verify(fieldInstance, never()).setFieldValue(mock1, any(), any());
        verify(fieldInstance, never()).setPropertyValue(mock1, anyString(), any(), any());

        verify(fieldInstance).setField(mock2, any());

        assertNull(injecter.thenInject());
    }
}