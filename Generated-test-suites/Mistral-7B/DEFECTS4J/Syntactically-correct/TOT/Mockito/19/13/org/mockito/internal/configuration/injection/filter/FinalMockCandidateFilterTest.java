package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.MockUtil;
import org.mockito.internal.util.reflection.StubbingOperationFailedException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Object fieldInstance;
    private Object mock1;
    private Object mock2;
    private Object mock3;
    private Field field;

    @Before
    public void setUp() throws Exception {
        field = MockUtil.getDeclaredField(FinalMockCandidateFilter.class.getDeclaredField("fieldInstance"), FinalMockCandidateFilter.class);
        fieldInstance = mock(Object.class);
        mock1 = mock(Object.class);
        mock2 = mock(Object.class);
        mock3 = mock(Object.class);
        filter = new FinalMockCandidateFilter();
        FieldSetter.setFieldValue(filter, "fieldInstance", fieldInstance);
        FieldSetter.setFieldValue(filter, "mocks", Arrays.asList(mock1, mock2, mock3));
        field = MockUtil.getDeclaredField(FinalMockCandidateFilter.class.getDeclaredField("field"), FinalMockCandidateFilter.class);
    }

    @Test
    public void testSingleMatchingMock() {
        BeanPropertySetter bps = new BeanPropertySetter(fieldInstance, field);
        bps.set(mock1);

        OngoingInjecter injecter = filter.filterCandidate(Collections.singletonList(mock1), field, fieldInstance);
        Object result = injecter.thenInject();

        verify(mock1).equals(result);
    }

    @Test
    public void testMultipleMocks_BeanPropertySetterSuccess() {
        BeanPropertySetter bps = new BeanPropertySetter(fieldInstance, field);
        bps.set(mock1);

        OngoingInjecter injecter = filter.filterCandidate(Arrays.asList(mock1, mock2, mock3), field, fieldInstance);
        Object result = injecter.thenInject();

        verify(mock1).equals(result);
        verifyNoInteractions(mock2, mock3);
    }

    @Test(expected = Reporter.ValidationError.class)
    public void testMultipleMocks_BeanPropertySetterFailure() {
        OngoingInjecter injecter = filter.filterCandidate(Arrays.asList(mock1, mock2, mock3), field, fieldInstance);
        injecter.thenInject();

        // This test case should throw Reporter.ValidationError due to the absence of correct mock.
    }

    @Test(expected = StubbingOperationFailedException.class)
    public void testMultipleMocks_FieldSetterFailure() {
        OngoingInjecter injecter = filter.filterCandidate(Arrays.asList(mock1, mock2, mock3), field, fieldInstance);
        injecter.thenInject();

        // This test case should throw StubbingOperationFailedException due to the failure of field setting.
    }
}