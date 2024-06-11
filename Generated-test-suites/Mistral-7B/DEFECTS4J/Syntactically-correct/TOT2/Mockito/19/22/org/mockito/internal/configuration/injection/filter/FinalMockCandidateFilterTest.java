import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.*;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.*;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Object mock1;
    private Object mock2;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        mocks = Arrays.asList(mock1 = Mockito.mock(Object.class), mock2 = Mockito.mock(Object.class));
        field = Mockito.mock(Field.class);
        fieldInstance = Mockito.mock(Object.class);
    }

    @Test
    public void testFilterCandidateWithSingleMock() {
        when(field.get(fieldInstance)).thenReturn(mock1);
        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(mock1, actual.thenInject());
    }

    @Test(expected = IllegalStateException.class)
    public void testFilterCandidateWithEmptyMocksCollection() {
        OngoingInjecter actual = filter.filterCandidate(Collections.emptyList(), field, fieldInstance);
        assertNull(actual);
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndPropertySetterSuccess() {
        when(field.get(fieldInstance)).thenReturn(mock1);
        when(new BeanPropertySetter(fieldInstance, field).canSet(mock2)).thenReturn(true);

        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(mock2, actual.thenInject());
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidateWithMultipleMocksAndFieldAccessFailure() {
        when(field.get(fieldInstance)).thenReturn(mock1);
        when(new BeanPropertySetter(fieldInstance, field).canSet(mock2)).thenReturn(false);
        when(new FieldSetter(fieldInstance, field).canSet(mock2)).thenReturn(false);

        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(actual);
    }
}