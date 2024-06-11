package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldType;
import org.mockito.internal.util.reflection.ReflectionUtils;
import org.mockito.internal.util.reflection.TypeInformation;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.testng.PowerMockTestCase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.powermock.api.mockito.stub.PowerMockitoStubs.stubField;

public class FinalMockCandidateFilterTest extends PowerMockTestCase {

    private FinalMockCandidateFilter filter;
    private Object mockedObject;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setup() {
        mockedObject = mock(Object.class);
        mocks = new ArrayList<>();
        mocks.add(spy(mockedObject));
        filter = new FinalMockCandidateFilter();
        field = ReflectionUtils.getDeclaredField(mockedObject, "fieldName");
        fieldInstance = ReflectionUtils.getFieldValue(mockedObject, field);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateOneMock() {
        // Given
        FieldType fieldType = new TypeInformation(field.getType()).asFieldType();

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        injecter.thenInject();

        // Check that the field was set
        Object value = ReflectionUtils.getFieldValue(mockedObject, field);
        assertEquals(mockedObject, value);

        // Check that the reporter was called
        verify(new Reporter()).cannotInjectDependency(any(Field.class), any(Object.class), any(RuntimeException.class));
    }

    @Test
    public void testFilterCandidateMultipleMocks() {
        // Given
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNotNull(injecter);
    }

    @Test
    public void testFilterCandidatePropertySetterSuccess() {
        // Given
        PowerMockito.mockStatic(BeanPropertySetter.class);
        PowerMockito.whenNew(BeanPropertySetter.class, any(Object.class), any(Field.class)).thenReturn(new BeanPropertySetter(fieldInstance, field));

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        injecter.thenInject();

        // Check that the property was set
        Object value = ReflectionUtils.getFieldValue(mockedObject, field);
        assertEquals(mockedObject, value);

        // Verify that BeanPropertySetter was called
        PowerMockito.verifyNew(BeanPropertySetter.class, any(Object.class), any(Field.class)).withArguments(any(Object.class), any(Field.class));
    }

    @Test
    public void testFilterCandidatePropertySetterFailure() {
        // Given
        PowerMockito.mockStatic(BeanPropertySetter.class);
        PowerMockito.whenNew(BeanPropertySetter.class, any(Object.class), any(Field.class)).thenReturn(new BeanPropertySetter(fieldInstance, field) {
            @Override
            public boolean set(Object target) {
                throw new RuntimeException();
            }
        });

        // When
        try {
            filter.filterCandidate(mocks, field, fieldInstance).thenInject();
        } catch (RuntimeException e) {
            // Expected
        }

        // Then
        // Check that the field wasn't set
        Object value = ReflectionUtils.getFieldValue(mockedObject, field);
        assertNull(value);

        // Check that the reporter was called
        verify(new Reporter()).cannotInjectDependency(any(Field.class), any(Object.class), any(RuntimeException.class));
    }

    @Test
    public void testFilterCandidateFieldSetterSuccess() {
        // Given
        PowerMockito.mockStatic(FieldSetter.class);
        PowerMockito.whenNew(FieldSetter.class, any(Field.class), any(Object.class)).thenReturn(new FieldSetter(field, fieldInstance));

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        injecter.thenInject();

        // Check that the field was set
        Object value = ReflectionUtils.getFieldValue(mockedObject, field);
        assertEquals(fieldInstance, value);

        // Verify that FieldSetter was called
        PowerMockito.verifyNew(FieldSetter.class, any(Field.class), any(Object.class)).withArguments(any(Field.class), any(Object.class));
    }

    @Test
    public void testFilterCandidateFieldSetterFailure() {
        // Given
        PowerMockito.mockStatic(FieldSetter.class);
        PowerMockito.whenNew(FieldSetter.class, any(Field.class), any(Object.class)).thenReturn(new FieldSetter(field, fieldInstance) {
            @Override
            public void set(Object target, Object value) {
                throw new RuntimeException();
            }
        });

        // When
        try {
            filter.filterCandidate(mocks, field, fieldInstance).thenInject();
        } catch (RuntimeException e) {
            // Expected
        }

        // Then
        // Check that the field wasn't set
        Object value = ReflectionUtils.getFieldValue(mockedObject, field);
        assertNull(value);

        // Check that the reporter was called
        verify(new Reporter()).cannotInjectDependency(any(Field.class), any(Object.class), any(RuntimeException.class));
    }

    @Test
    public void testFilterCandidateNoMocks() {
        // Given
        mocks.clear();

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNotNull(injecter);
    }

    @Test
    public void testFilterCandidateNoField() {
        // Given
        Object objectWithoutField = mock(Object.class);

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, null, fieldInstance);

        // Then
        assertNotNull(injecter);
    }
}