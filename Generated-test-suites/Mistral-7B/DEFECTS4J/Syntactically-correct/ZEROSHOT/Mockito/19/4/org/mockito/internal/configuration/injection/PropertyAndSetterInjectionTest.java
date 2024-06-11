package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockitoMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.internal.util.reflection.FieldUtil.getDeclaredField;
import static org.mockito.internal.util.reflection.FieldUtil.setAccessible;

@RunWith(MockitoJUnitRunner.class)
public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injection;
    private Object mock;

    @MockitoMock
    private static class ClassWithInjectedField {
        @InjectMocks
        private Object fieldToBeInjected;
    }

    @Before
    public void setUp() {
        injection = new PropertyAndSetterInjection();
    }

    @Test
    public void testProcessInjection_noInjectionOccurs() {
        // Given:
        ClassWithInjectedField classWithInjectedField = new ClassWithInjectedField();
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mock);

        // When:
        assertFalse(injection.processInjection(getDeclaredField(ClassWithInjectedField.class, "fieldToBeInjected"), classWithInjectedField, mockCandidates));

        // Then: no injection occurs
    }

    @Test
    public void testProcessInjection_injectionOccurs() {
        // Given:
        ClassWithInjectedField classWithInjectedField = new ClassWithInjectedField();
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mock);

        Stubber stubber = mock(Answer.class);
        when(stubber.callRealMethod()).thenAnswer(i -> mock);
        mockCandidates.add(stubber);

        // When:
        assertTrue(injection.processInjection(getDeclaredField(ClassWithInjectedField.class, "fieldToBeInjected"), classWithInjectedField, mockCandidates));

        // Then: injection occurs
        verify(classWithInjectedField, times(1)).setFieldToBeInjected(mock);
    }

    @Test(expected = InvocationTargetException.class)
    public void testInitializeInjectMocksField_initializationFails() {
        // Given:
        Class<?> fieldClass = Object.class;
        Object fieldInstanceNeedingInjection = new Object();
        Field field = mock(Field.class);

        // When:
        injection.initializeInjectMocksField(field, fieldInstanceNeedingInjection);

        // Then: an exception is thrown
    }

    @Test
    public void testInitializeInjectMocksField_initializationSucceeds() {
        // Given:
        Class<?> fieldClass = Object.class;
        Object fieldInstanceNeedingInjection = new Object();
        Field field = mock(Field.class);

        when(field.getType()).thenReturn(Object.class);
        when(field.get(any(Object.class))).thenReturn(fieldInstanceNeedingInjection);

        // When:
        FieldInitializationReport report = injection.initializeInjectMocksField(field, fieldInstanceNeedingInjection);

        // Then: initialization succeeds
        assertEquals(fieldClass, report.fieldClass());
        assertEquals(fieldInstanceNeedingInjection, report.fieldInstance());
    }

    @Test
    public void testInjectMockCandidates_noInjectionOccurs() {
        // Given:
        Class<?> awaitingInjectionClazz = Object.class;
        Set<Object> mocks = new HashSet<>();
        Object instance = new Object();

        // When:
        assertFalse(injection.injectMockCandidates(awaitingInjectionClazz, mocks, instance));

        // Then: no injection occurs
    }

    @Test
    public void testInjectMockCandidates_injectionOccurs() {
        // Given:
        Class<?> awaitingInjectionClazz = Object.class;
        Set<Object> mocks = new HashSet<>();
        Object instance = new Object();
        Object mockToBeInjected = mock(Object.class);
        mocks.add(mockToBeInjected);

        // When:
        assertTrue(injection.injectMockCandidates(awaitingInjectionClazz, mocks, instance));

        // Then: injection occurs
        verify(instance).setField(eq(mockToBeInjected), any());
    }

    @Test
    public void testOrderedInstanceFieldsFrom_returnsOrderedFields() {
        // Given:
        Class<?> awaitingInjectionClazz = Object.class;

        // When:
        List<Field> orderedFields = injection.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // Then: ordered fields are returned
        assertEquals(Arrays.asList(awaitingInjectionClazz.getDeclaredFields()), orderedFields);
    }
}