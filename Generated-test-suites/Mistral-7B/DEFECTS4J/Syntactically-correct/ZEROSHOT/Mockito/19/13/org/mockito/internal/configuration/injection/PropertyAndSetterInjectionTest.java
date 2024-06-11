package org.mockito.internal.configuration.injection;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;
import static org.mockito.internal.util.reflection.ReflectionUtils.getField;
import static org.mockito.internal.util.reflection.ReflectionUtils.setField;
import static org.mockito.internal.util.reflection.ReflectionUtils.setAccessible;
import static org.mockito.junit.MockitoJUnitRunner.mock;
import static org.mockito.junit.MockitoJUnitRunner.spy;
import static org.mockito.junit.MockitoJUnitRunner.when;
import static org.mockito.stubbing.AnswerWithCalls.calls;
import static org.mockito.stubbing.AnswerWithCalls.returns;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PropertyAndSetterInjectionTest {

    @Test
    public void testProcessInjection() throws Exception {
        Class<?> testClass = TestClass.class;
        Object testObject = mock(TestClass.class);

        Field injectMocksField = getField(TestClass.class, "injectMocksField");
        setAccessible(injectMocksField);
        injectMocksField.set(testObject, new Object[]{});

        List<Object> mockCandidates = newArrayList(mock(Dependency1.class), mock(Dependency1.class), mock(Dependency2.class));

        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();

        assertTrue(propertyAndSetterInjection.processInjection(injectMocksField, testObject, mockCandidates));

        Object dependency1 = getFieldValue(testObject, "dependency1");
        assertNotNull(dependency1);
        assertTrue(dependency1 instanceof Dependency1);

        Object dependency2 = getFieldValue(testObject, "dependency2");
        assertNotNull(dependency2);
        assertTrue(dependency2 instanceof Dependency2);
    }

    @Test
    public void testProcessInjection_noMockFound() throws Exception {
        Class<?> testClass = TestClass.class;
        Object testObject = mock(TestClass.class);

        Field injectMocksField = getField(TestClass.class, "injectMocksField");
        setAccessible(injectMocksField);
        injectMocksField.set(testObject, new Object[]{});

        List<Object> mockCandidates = newArrayList(mock(NonMatchingType.class), mock(NonMatchingType.class));

        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();

        assertFalse(propertyAndSetterInjection.processInjection(injectMocksField, testObject, mockCandidates));

        assertNull(getFieldValue(testObject, "dependency1"));
        assertNull(getFieldValue(testObject, "dependency2"));
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjection_initializationException() throws Exception {
        Class<?> testClass = TestClass.class;
        Object testObject = mock(TestClass.class);

        Field injectMocksField = getField(TestClass.class, "injectMocksField");
        setAccessible(injectMocksField);
        injectMocksField.set(testObject, new Object[]{});

        List<Object> mockCandidates = newArrayList(mock(InitializationExceptionThrowingDependency.class), mock(Dependency1.class));

        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();

        propertyAndSetterInjection.processInjection(injectMocksField, testObject, mockCandidates);
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjection_noSetterOrField() throws Exception {
        Class<?> testClass = TestClassWithNoSetterOrField.class;
        Object testObject = spy(testClass);

        Field injectMocksField = getField(TestClassWithNoSetterOrField.class, "injectMocksField");
        setAccessible(injectMocksField);
        injectMocksField.set(testObject, new Object[]{});

        List<Object> mockCandidates = newArrayList(mock(Dependency1.class), mock(Dependency1.class), mock(Dependency2.class));

        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();

        propertyAndSetterInjection.processInjection(injectMocksField, testObject, mockCandidates);
    }

    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Field field = getField(object.getClass(), fieldName);
        setAccessible(field);
        return field.get(object);
    }

    private <T> List<T> newArrayList(T... elements) {
        return new ArrayList<>(Arrays.asList(elements));
    }
}

class TestClass {
    @InjectMocks
    private Dependency1 dependency1;

    @InjectMocks
    private Dependency2 dependency2;

    private Dependency1 dependency1Field;

    private Dependency2 dependency2Field;

    public Dependency1 getDependency1() {
        return dependency1;
    }

    public Dependency2 getDependency2() {
        return dependency2;
    }

    public Dependency1 dependency1;

    public Dependency2 dependency2;
}

class TestClassWithNoSetterOrField {
    @InjectMocks
    private Dependency1 dependency1;

    private Dependency1 dependency1Field;

    public Dependency1 getDependency1() {
        return dependency1;
    }
}

class Dependency1 {}

class Dependency2 {}

class InitializationExceptionThrowingDependency {
    public InitializationExceptionThrowingDependency() {
        throw new RuntimeException("Initialization Exception");
    }
}