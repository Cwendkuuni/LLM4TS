package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        field = Mockito.spy(new Field("testField", int.class, java.lang.reflect.AccessibleObject.SETTER) {
            @Override
            public Object get(Object obj) throws IllegalAccessException {
                return null;
            }

            @Override
            public void set(Object obj, Object value) throws IllegalAccessException {
                // do nothing
            }

            @Override
            public boolean isAccessible() {
                return true;
            }
        });
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate_oneMock_behaviorSetterSuccess() {
        when(mocks.iterator().next()).thenReturn(mock(Object.class));
        when(new BeanPropertySetter(fieldInstance, field).set(mocks.iterator().next())).thenReturn(true);

        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
        Object actualResult = actual.thenInject();

        verify(mocks.iterator().next()).equals(fieldInstance);
        verifyZeroInteractions(mocks.iterator().next());
        verify(fieldInstance).equals(actualResult);
    }

    @Test
    public void testFilterCandidate_oneMock_behaviorSetterFailure() {
        when(mocks.iterator().next()).thenReturn(mock(Object.class));
        when(new BeanPropertySetter(fieldInstance, field).set(mocks.iterator().next())).thenThrow(new RuntimeException());

        try {
            OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
            actual.thenInject();
        } catch (MockitoException e) {
            verify(mocks.iterator().next()).equals(fieldInstance);
            verify(fieldInstance).equals(e.getCause().getCausedByException().getCause());
        }
    }

    @Test
    public void testFilterCandidate_oneMock_fieldSetterSuccess() {
        when(mocks.iterator().next()).thenReturn(mock(Object.class));
        when(new FieldSetter(fieldInstance, field).set(mocks.iterator().next())).thenReturn(true);

        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
        Object actualResult = actual.thenInject();

        verify(mocks.iterator().next()).equals(fieldInstance);
        verifyZeroInteractions(mocks.iterator().next());
        verify(fieldInstance).equals(actualResult);
    }

    @Test
    public void testFilterCandidate_oneMock_fieldSetterFailure() {
        when(mocks.iterator().next()).thenReturn(mock(Object.class));
        when(new FieldSetter(fieldInstance, field).set(mocks.iterator().next())).thenThrow(new RuntimeException());

        try {
            OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
            actual.thenInject();
        } catch (MockitoException e) {
            verify(mocks.iterator().next()).equals(fieldInstance);
            verify(fieldInstance).equals(e.getCause().getCausedByException().getCause());
        }
    }

    @Test
    public void testFilterCandidate_multipleMocks_returnNullInjecter() {
        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);

        verify(mocks, Mockito.atLeastOnce()).size();
        verify(mocks).forEach(mock -> {
            verifyZeroInteractions(mock);
        });
        verify(fieldInstance).equals(actual.thenInject());
        verifyZeroInteractions(actual);
    }
}