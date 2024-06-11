package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.reflection.FieldSetter;
import org.mockito.internal.reflection.MockitoClassUtil;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Object fieldInstance;
    private Object mock1;
    private Object mock2;
    private Object mock3;
    private Field field;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        fieldInstance = mock(Object.class);
        mock1 = mock(Object.class);
        mock2 = mock(Object.class);
        mock3 = mock(Object.class);
        field = MockitoClassUtil.getDeclaredField(FinalMockCandidateFilter.class, "mocks");
        try {
            BeanPropertySetter.set(filter, "mocks", new ArrayList<>());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        BeanPropertySetter.set(filter, "mocks", Collections.singletonList(mock1));
        field = MockitoClassUtil.getDeclaredField(FinalMockCandidateFilter.class, "fieldInstance");
        try {
            BeanPropertySetter.set(filter, "fieldInstance", fieldInstance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        field = MockitoClassUtil.getDeclaredField(FinalMockCandidateFilter.class, "field");
        try {
            BeanPropertySetter.set(filter, "field", field);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSingleMockFiltering() {
        final List<Object> mocks = (List<Object>) BeanPropertyGetters.get(filter, "mocks");
        mocks.clear();
        mocks.add(mock2);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        verify(mock1).cannotInjectDependency(field, mock1, new Reporter());
        verify(mock2).cannotInjectDependency(field, mock2, new Reporter());
        verify(mock2).set(fieldInstance, field.get(mock1));
        verify(fieldInstance).equals(mock2);
    }

    @Test(expected = RuntimeException.class)
    public void testMultipleMocksNullReturn() {
        final List<Object> mocks = (List<Object>) BeanPropertyGetters.get(filter, "mocks");
        mocks.clear();
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        // This test should fail with a RuntimeException as the injecter is supposed to return null in this case
    }

    @Test
    public void testMultipleMocksPropertySetterSuccess() {
        final List<Object> mocks = (List<Object>) BeanPropertyGetters.get(filter, "mocks");
        mocks.clear();
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        verify(mock1).cannotInjectDependency(field, mock1, new Reporter());
        verify(mock2).set(fieldInstance, field.get(mock1));
        verify(mock2).set(fieldInstance, field.get(mock2));
        verify(mock3).cannotInjectDependency(field, mock3, new Reporter());
        verify(fieldInstance).equals(mock2);
    }

    @Test
    public void testMultipleMocksFieldSetterSuccess() {
        final List<Object> mocks = (List<Object>) BeanPropertyGetters.get(filter, "mocks");
        mocks.clear();
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        FieldSetter setter = new FieldSetter(fieldInstance, field);
        try {
            setter.set(mock2);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        verify(mock1).cannotInjectDependency(field, mock1, new Reporter());
        verify(mock2).set(fieldInstance, field.get(mock1));
        verify(mock3).cannotInjectDependency(field, mock3, new Reporter());
        verify(fieldInstance).equals(mock2);
    }

    @Test(expected = RuntimeException.class)
    public void testMultipleMocksExceptionDuringPropertySetter() {
        final List<Object> mocks = (List<Object>) BeanPropertyGetters.get(filter, "mocks");
        mocks.clear();
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        verify(mock1).cannotInjectDependency(field, mock1, new Reporter());
        verify(mock2).set(fieldInstance, field.get(mock1));
        verify(mock3).cannotInjectDependency(field, mock3, new Reporter());

        // This test should fail with a RuntimeException as an exception was thrown during the property setter process
    }

    @Test(expected = RuntimeException.class)
    public void testMultipleMocksExceptionDuringFieldSetter() {
        final List<Object> mocks = (List<Object>) BeanPropertyGetters.get(filter, "mocks");
        mocks.clear();
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        FieldSetter setter = new FieldSetter(fieldInstance, field);
        try {
            setter.set(mock2);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        try {
            OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
            injecter.thenInject();
        } catch (RuntimeException e) {
            throw new RuntimeException("Unexpected exception during test", e);
        }

        // This test should fail with a RuntimeException as an exception was thrown during the field setter process
    }
}