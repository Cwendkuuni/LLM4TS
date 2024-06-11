package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.util.Fields;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.reflection.Reflections.getFieldValue;
import static org.mockito.internal.util.reflection.Reflections.setFieldValue;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection strategy;
    private Object targetObject;
    private static final Class<MyClass> targetClass = MyClass.class;

    @Before
    public void setUp() {
        strategy = new PropertyAndSetterInjection();
        targetObject = new MyClass();
    }

    @Test
    public void testNoInjectMocksAnnotation() {
        // Given
        Field field = Fields.privateField(targetClass, "fieldWithoutInjectMocksAnnotation");
        // When
        boolean result = strategy.processInjection(field, targetObject, Sets.newHashSet());
        // Then
        assertFalse("No injection should occur for a field without @InjectMocks", result);
    }

    @Test
    public void testInjectMocksAnnotationOnFinalField() {
        // Given
        Field field = Fields.privateField(targetClass, "fieldWithInjectMocksAnnotationOnFinalField");
        // When
        boolean result = strategy.processInjection(field, targetObject, Sets.newHashSet());
        // Then
        assertFalse("No injection should occur for a final field", result);
    }

    @Test
    public void testInjectMocksAnnotationOnStaticField() {
        // Given
        Field field = Fields.privateField(targetClass, "fieldWithInjectMocksAnnotationOnStaticField");
        // When
        boolean result = strategy.processInjection(field, targetObject, Sets.newHashSet());
        // Then
        assertFalse("No injection should occur for a static field", result);
    }

    @Test
    public void testInjectMocksAnnotationOnFieldWithNoMatchingMockCandidates() {
        // Given
        Field field = Fields.privateField(targetClass, "fieldWithNoMatchingMockCandidates");
        Set<Object> mockCandidates = Sets.newHashSet(new MockClass1(), new MockClass2());
        // When
        FieldInitializationReport report = strategy.initializeInjectMocksField(field, targetObject);
        boolean result = strategy.injectMockCandidates(targetClass, mockCandidates, targetObject);
        // Then
        assertFalse("No injection should occur when there are no matching mock candidates", result);
    }

    @Test
    public void testInjectMocksAnnotationOnFieldWithMultipleMatchingMockCandidates() {
        // Given
        Field field = Fields.privateField(targetClass, "fieldWithMultipleMatchingMockCandidates");
        Set<Object> mockCandidates = Sets.newHashSet(new MockClass1(), new MockClass2(), new MockClass1());
        // When
        FieldInitializationReport report = strategy.initializeInjectMocksField(field, targetObject);
        boolean result = strategy.injectMockCandidates(targetClass, mockCandidates, targetObject);
        // Then
        assertTrue("One mock should be injected", result);
        assertNull("The injected mock should be removed from the mocks collection", mockCandidates.contains(report.getMock()));
    }

    @Test(expected = MockitoException.class)
    public void testInjectMocksAnnotationOnFieldWithExceptionDuringInitialization() {
        // Given
        Field field = Fields.privateField(targetClass, "fieldWithExceptionDuringInitialization");
        Set<Object> mockCandidates = Sets.newHashSet(new MockClass1(), new MockClass2());
        // When
        strategy.processInjection(field, targetObject, mockCandidates);
        // Then
        // An exception should be thrown during the initialization of the field
    }

    @Test(expected = MockitoException.class)
    public void testInjectMocksAnnotationOnFieldWithExceptionDuringInjection() {
        // Given
        Field field = Fields.privateField(targetClass, "fieldWithExceptionDuringInjection");
        Set<Object> mockCandidates = Sets.newHashSet(new MockClass1(), new MockClass2());
        // When
        strategy.processInjection(field, targetObject, mockCandidates);
        // Then
        // An exception should be thrown during the injection of the mock
    }

    @Test
    public void testInjectMocksAnnotationOnFieldWithNoSetter() {
        // Given
        Field field = Fields.privateField(targetClass, "fieldWithNoSetter");
        Set<Object> mockCandidates = Sets.newHashSet(new MockClass1(), new MockClass2());
        // When
        boolean result = strategy.processInjection(field, targetObject, mockCandidates);
        // Then
        assertTrue("The field without a setter should be injected via field injection", result);
    }

    @Test
    public void testInjectMocksAnnotationOnFieldWithSetter() {
        // Given
        Field field = Fields.privateField(targetClass, "fieldWithSetter");
        Set<Object> mockCandidates = Sets.newHashSet(new MockClass1(), new MockClass2());
        // When
        boolean result = strategy.processInjection(field, targetObject, mockCandidates);
        // Then
        assertTrue("The field with a setter should be injected via setter if possible", result);
    }
}

class MyClass {
    @InjectMocks
    private MyInnerClass innerClass;

    // other fields
}

class MyInnerClass {}

class MockClass1 {}

class MockClass2 {}