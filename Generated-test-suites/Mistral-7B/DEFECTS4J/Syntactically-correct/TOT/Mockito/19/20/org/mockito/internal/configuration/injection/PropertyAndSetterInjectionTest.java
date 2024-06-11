package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.TestUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injection;
    private List<Object> mockCandidates;
    private Object objectUnderTest;

    @Before
    public void setUp() {
        injection = new PropertyAndSetterInjection();
        objectUnderTest = new TestClass();
        mockCandidates = new ArrayList<>();
        mockCandidates.add(new MockClassA());
        mockCandidates.add(new MockClassB());
        mockCandidates.add(new MockClassC());
    }

    @Test
    public void testProcessInjection_InjectMockAnnotatedField_NoDependencyFound() {
        Field injectMocksField = TestUtils.getDeclaredField(objectUnderTest, "injectMocksField");
        FieldInitializationReport report = injection.initializeInjectMocksField(injectMocksField, objectUnderTest);

        assertFalse(report.wasInjected());
        assertNull(report.injectedObject());
        assertFalse(report.hadError());
    }

    @Test
    public void testProcessInjection_InjectMockAnnotatedField_DependencyFoundByType() {
        Field injectMocksField = TestUtils.getDeclaredField(objectUnderTest, "injectMocksField");
        Field mockField = TestUtils.getDeclaredField(TestClass.class, "mockField");
        mockCandidates.clear();
        mockCandidates.add(new MockClassA());
        mockCandidates.add(new MockClassB());
        mockCandidates.add(new MockClassC());

        assertFalse(injection.processInjection(injectMocksField, objectUnderTest, mockCandidates));

        assertTrue(mockCandidates.contains(mockField.getType()));
        assertFalse(mockCandidates.contains(new MockClassA().getClass()));
        assertFalse(mockCandidates.contains(new MockClassB().getClass()));

        FieldInitializationReport report = injection.initializeInjectMocksField(injectMocksField, objectUnderTest);

        assertTrue(report.wasInjected());
        assertSame(report.injectedObject(), mockField.get(objectUnderTest));
        assertFalse(report.hadError());
    }

    @Test
    public void testProcessInjection_InjectMockAnnotatedField_DependencyFoundByName() {
        Field injectMocksField = TestUtils.getDeclaredField(objectUnderTest, "injectMocksField");
        Field mockField = TestUtils.getDeclaredField(TestClass.class, "mockField");
        mockCandidates.clear();
        mockCandidates.add(new MockClassA());
        mockCandidates.add(new MockClassB());
        mockCandidates.add(new MockClassC());

        assertFalse(injection.processInjection(injectMocksField, objectUnderTest, mockCandidates));

        assertTrue(mockCandidates.contains(mockField.getName()));
        assertFalse(mockCandidates.contains(new MockClassA().getClass().getName()));
        assertFalse(mockCandidates.contains(new MockClassB().getClass().getName()));

        FieldInitializationReport report = injection.initializeInjectMocksField(injectMocksField, objectUnderTest);

        assertTrue(report.wasInjected());
        assertSame(report.injectedObject(), mockField.get(objectUnderTest));
        assertFalse(report.hadError());
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjection_InjectMockAnnotatedField_DependencyNotFound() {
        Field injectMocksField = TestUtils.getDeclaredField(objectUnderTest, "injectMocksField");
        FieldInitializationReport report = injection.initializeInjectMocksField(injectMocksField, objectUnderTest);

        assertFalse(report.wasInjected());
        assertNull(report.injectedObject());
        assertTrue(report.hadError());

        injection.processInjection(injectMocksField, objectUnderTest, mockCandidates);
    }

    @Test
    public void testProcessInjection_FieldWithConstructor_DependencyFoundByType() {
        Field injectMocksField = TestUtils.getDeclaredField(objectUnderTest, "injectMocksField");
        Class<?> dependencyClass = TestClassWithDependency.class;
        mockCandidates.clear();
        mockCandidates.add(dependencyClass);
        mockCandidates.add(new MockClassB());
        mockCandidates.add(new MockClassC());

        assertFalse(injection.processInjection(injectMocksField, objectUnderTest, mockCandidates));

        assertTrue(mockCandidates.contains(dependencyClass));
        assertFalse(mockCandidates.contains(new MockClassA().getClass()));
        assertFalse(mockCandidates.contains(new MockClassB().getClass()));

        FieldInitializationReport report = injection.initializeInjectMocksField(injectMocksField, objectUnderTest);

        assertTrue(report.wasInjected());
        assertSame(report.injectedObject(), TestUtils.createInstance(dependencyClass));
        assertFalse(report.hadError());
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjection_FieldWithConstructor_DependencyNotFound() {
        Field injectMocksField = TestUtils.getDeclaredField(objectUnderTest, "injectMocksField");
        mockCandidates.clear();
        mockCandidates.add(new MockClassA());
        mockCandidates.add(new MockClassB());
        mockCandidates.add(new MockClassC());

        assertFalse(injection.processInjection(injectMocksField, objectUnderTest, mockCandidates));

        FieldInitializationReport report = injection.initializeInjectMocksField(injectMocksField, objectUnderTest);

        assertFalse(report.wasInjected());
        assertNull(report.injectedObject());
        assertTrue(report.hadError());

        injection.processInjection(injectMocksField, objectUnderTest, mockCandidates);
    }
}

class TestClass {
    @InjectMocks
    private Object injectMocksField;

    // Private field for testing
    private Object mockField;
}

class TestClassWithDependency {
    @InjectMocks
    private TestClassWithDependency dependency;
}

class MockClassA {}

class MockClassB {}

class MockClassC {}