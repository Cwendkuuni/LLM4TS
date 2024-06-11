package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.mockitoapi.MockCandidateFilter;
import org.mockito.internal.configuration.mockitoapi.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldType;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.reflect.WhiteboxImpl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FinalMockCandidateFilterTest {

    private Reporter reporter;
    private Field field;
    private Object fieldInstance;
    private Collection<Object> mocks;
    private FinalMockCandidateFilter filter;
    private OngoingInjecter injecter;

    @Before
    public void setUp() throws Exception {
        reporter = mock(Reporter.class);
        field = Whitebox.getInternalState(this, "field");
        fieldInstance = mock(FieldType.class);
        mocks = new ArrayList<>();
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_SingleMock_PropertySetterSuccess() throws Exception {
        Object mock = mock(Object.class);
        when(field.get(fieldInstance)).thenReturn("propertyValue");

        injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object returnedMock = injecter.thenInject();

        assertEquals(mock, returnedMock);
        verify(reporter).nothingToInject(field, mock);
    }

    @Test
    public void testFilterCandidate_SingleMock_PropertySetterFailure() throws Exception {
        Object mock = mock(Object.class);
        when(field.get(fieldInstance)).thenReturn("propertyValue");
        when(reporter::cannotInjectDependency).thenAnswer(i -> {});

        injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object returnedMock = injecter.thenInject();

        assertEquals(mock, returnedMock);
        verify(reporter).cannotInjectDependency(field, mock, new RuntimeException());
    }

    @Test
    public void testFilterCandidate_SingleMock_FieldSetterSuccess() throws Exception {
        Object mock = mock(Object.class);
        when(field.get(fieldInstance)).thenReturn(new FieldType() {
            private Object value;

            @Override
            public Object getValue(Object instance) {
                return value;
            }

            @Override
            public void setValue(Object instance, Object value) {
                this.value = value;
            }
        });
        when(this.getValue(fieldInstance)).thenReturn(null);

        WhiteboxImpl.setInternalState(filter, "reporter", reporter);
        injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object returnedMock = injecter.thenInject();

        assertEquals(mock, returnedMock);
        verify(reporter).nothingToInject(field, mock);
    }

    @Test
    public void testFilterCandidate_SingleMock_FieldSetterFailure() throws Exception {
        Object mock = mock(Object.class);
        when(field.get(fieldInstance)).thenReturn(new FieldType() {
            private Object value;

            @Override
            public Object getValue(Object instance) {
                return value;
            }

            @Override
            public void setValue(Object instance, Object value) {
                this.value = value;
            }
        });
        when(this.getValue(fieldInstance)).thenReturn(mock);
        when(reporter::cannotInjectDependency).thenAnswer(i -> {});

        WhiteboxImpl.setInternalState(filter, "reporter", reporter);
        injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object returnedMock = injecter.thenInject();

        assertEquals(mock, returnedMock);
        verify(reporter).cannotInjectDependency(field, mock, new RuntimeException());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_ReturnsNullInjecter() throws Exception {
        injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(injecter);
        assertEquals(null, injecter.thenInject());
    }

    @Test(expected = IllegalAccessException.class)
    public void testFilterCandidate_ThrowsException_PropertySetter() throws Exception {
        Object mock = mock(Object.class);
        when(field.get(fieldInstance)).thenReturn("propertyValue");

        filter.filterCandidate(mocks, field, fieldInstance);
        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = IllegalAccessException.class)
    public void testFilterCandidate_ThrowsException_FieldSetter() throws Exception {
        Object mock = mock(Object.class);
        when(field.get(fieldInstance)).thenReturn(new FieldType() {
            private Object value;

            @Override
            public Object getValue(Object instance) {
                return value;
            }

            @Override
            public void setValue(Object instance, Object value) {
                this.value = value;
            }
        });
        when(this.getValue(fieldInstance)).thenReturn(mock);

        filter.filterCandidate(mocks, field, fieldInstance);
        filter.filterCandidate(mocks, field, fieldInstance);
    }
}