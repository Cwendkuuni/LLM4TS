package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.mockitoapi.MockCandidateFilter;
import org.mockito.internal.configuration.mockitoapi.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.WhiteboxImpl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class FinalMockCandidateFilterTest {

    private MockCandidateFilter filter;
    private Reporter reporter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        reporter = mock(Reporter.class);
        mocks = new ArrayList<>();
        field = mock(Field.class);
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate_SingleMock_Success() {
        Object mock = mock(Object.class);
        PowerMockito.spy(mock);
        WhiteboxImpl.setInternalState(mocks, "size", 1);
        Whitebox.setInternalState(mocks, "list", Arrays.asList(mock));

        PowerMockito.spy(field);
        WhiteboxImpl.setInternalState(field, "name", "testField");

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mock, result.thenInject());
    }

    @Test
    public void testFilterCandidate_SingleMock_BeanPropertySetterFailure_FieldSetterSuccess() {
        Object mock = mock(Object.class);
        PowerMockito.spy(mock);
        WhiteboxImpl.setInternalState(mocks, "size", 1);
        Whitebox.setInternalState(mocks, "list", Arrays.asList(mock));

        PowerMockito.spy(field);
        WhiteboxImpl.setInternalState(field, "name", "testField");

        try {
            Whitebox.setInternalState(fieldInstance, "testField", new Object() {});
            new BeanPropertySetter(fieldInstance, field).set(mock);
            throw new RuntimeException("Exception occurred during property setter");
        } catch (RuntimeException e) {
            PowerMockito.doThrow(e).when(reporter).cannotInjectDependency(any(Field.class), any(Object.class), any(RuntimeException.class));
        }

        filter = new FinalMockCandidateFilter() {
            @Override
            public Reporter getReporter() {
                return reporter;
            }
        };

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mock, result.thenInject());
    }

    @Test
    public void testFilterCandidate_SingleMock_FieldSetterFailure_NoInject() {
        Object mock = mock(Object.class);
        PowerMockito.spy(mock);
        WhiteboxImpl.setInternalState(mocks, "size", 1);
        Whitebox.setInternalState(mocks, "list", Arrays.asList(mock));

        PowerMockito.spy(field);
        WhiteboxImpl.setInternalState(field, "name", "testField");

        try {
            new FieldSetter(fieldInstance, field).set(mock);
            throw new RuntimeException("Exception occurred during field setter");
        } catch (RuntimeException e) {
            PowerMockito.doThrow(e).when(reporter).cannotInjectDependency(any(Field.class), any(Object.class), any(RuntimeException.class));
        }

        filter = new FinalMockCandidateFilter() {
            @Override
            public Reporter getReporter() {
                return reporter;
            }
        };

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(null, result.thenInject());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoInject() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        PowerMockito.spy(mock1);
        PowerMockito.spy(mock2);
        WhiteboxImpl.setInternalState(mocks, "size", 2);
       Whitebox.setInternalState(mocks, "list", Arrays.asList(mock1, mock2));

        PowerMockito.spy(field);
        WhiteboxImpl.setInternalState(field, "name", "testField");

        filter = new FinalMockCandidateFilter();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(null, result.thenInject());
    }
}