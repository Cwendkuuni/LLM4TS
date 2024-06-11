import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter filter;
    private List<Object> mocks;
    private Object fieldInstance;
    private Object mock1;
    private Object mock2;
    private Object field;
    private Reporter reporter;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        fieldInstance = mock(Object.class);
        mock1 = mock(Object.class);
        mock2 = mock(Object.class);
        field = mock(Field.class);
        when(field.getName()).thenReturn("testField");
        reporter = mock(Reporter.class);
    }

    @Test
    public void testFilterCandidateOneMock() {
        mocks.add(mock1);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Check that the BeanPropertySetter is called first and the injection is successful
        new BeanPropertySetter(fieldInstance, field).set(mock1);
        verify(mock1).equals(injecter.thenInject());
        verifyNoInteractions(fieldInstance, reporter);

        // Check that the FieldSetter is called only if the BeanPropertySetter fails
        BeanPropertySetter propertySetterSpy = spy(new BeanPropertySetter(fieldInstance, field));
        when(propertySetterSpy.set(mock1)).thenThrow(new RuntimeException("Exception when setting property"));
        new FieldSetter(fieldInstance, field).set(mock1);
        verify(mock1).equals(injecter.thenInject());
        verify(reporter).cannotInjectDependency(field, mock1, new RuntimeException("Exception when setting property"));
    }

    @Test
    public void testFilterCandidateMultipleMocks() {
        mocks.clear();
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Check that no injection is done in this scenario
        verifyNoInteractions(mock1, mock2, fieldInstance, reporter);
        verify(injecter).thenInject();
        verify(injecter.thenInject()).isNull();
    }
}