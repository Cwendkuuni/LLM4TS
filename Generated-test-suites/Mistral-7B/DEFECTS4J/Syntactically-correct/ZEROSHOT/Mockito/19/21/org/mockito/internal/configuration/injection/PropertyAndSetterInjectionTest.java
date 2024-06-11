package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.TestUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;
import static org.mockito.internal.util.reflection.TestUtils.getField;
import static org.mockito.internal.util.reflection.TestUtils.setField;
import static org.mockito.internal.util.reflection.TestUtils.setPrivateField;

/**
 * Test class for PropertyAndSetterInjection.
 */
public class PropertyAndSetterInjectionTest {

    private static final Class<?> TEST_CLASS = PropertyAndSetterInjection.class;
    private static final Class<?> MOCKITO_INTERNAL_CONFIGURATION_INJECTION_MOCK_INJECTION_STRATEGY = MockInjectionStrategy.class;

    private PropertyAndSetterInjection propertyAndSetterInjection;
    private MockCandidateFilter mockCandidateFilter;
    private ListUtil.Filter<Field> notFinalOrStatic;
    private SuperTypesLastSorter superTypesLastSorter;

    @Before
    public void setUp() {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
        mockCandidateFilter = propertyAndSetterInjection.mockCandidateFilter;
        notFinalOrStatic = propertyAndSetterInjection.notFinalOrStatic;
        superTypesLastSorter = propertyAndSetterInjection.new SuperTypesLastSorter();
    }

    @Test
    public void testProcessInjection() throws InstantiationException, IllegalAccessException {
        Class<?> testClass = TestUtils.getTestClass(TEST_CLASS, TestUtils.TEST_CLASS_SUFFIX);
        Object testObject = testClass.newInstance();
        Field injectMocksField = getField(testObject, "propertyAndSetterInjectionField");
        injectMocksField.setAccessible(true);
        Set<Object> mockCandidates = newMockSafeHashSet(3);

        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, testObject);
        Assert.assertNotNull(report);

        // For each field in the class hierarchy
        Class<?> fieldClass = report.fieldClass();
        Object fieldInstanceNeedingInjection = report.fieldInstance();
        while (fieldClass != Object.class) {
            boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(fieldClass, mockCandidates, fieldInstanceNeedingInjection);
            Assert.assertTrue(injectionOccurred);
            fieldClass = fieldClass.getSuperclass();
        }
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjectionWithException() throws InstantiationException, IllegalAccessException {
        Class<?> testClass = TestUtils.getTestClass(TEST_CLASS, TestUtils.TEST_CLASS_SUFFIX);
        Object testObject = testClass.newInstance();
        Field injectMocksField = getField(testObject, "propertyAndSetterInjectionFieldWithException");
        injectMocksField.setAccessible(true);
        Set<Object> mockCandidates = newMockSafeHashSet(3);

        propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, testObject);
    }

    @Test
    public void testInjectMockCandidates() {
        Class<?> testClass = TestUtils.getTestClass(TEST_CLASS, TestUtils.TEST_CLASS_SUFFIX);
        Object testObject = testClass.newInstance();

        Set<Object> mockCandidates = newMockSafeHashSet(3);
        mockCandidates.add(new Object() {});
        mockCandidates.add(new Object() {});
        mockCandidates.add(new Object() {});

        List<Field> orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(testObject.getClass());
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, testObject, false, orderedInstanceFields);
        Assert.assertTrue(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidatesOnFields_NoMockFound() {
        Class<?> testClass = TestUtils.getTestClass(TEST_CLASS, TestUtils.TEST_CLASS_SUFFIX);
        Object testObject = testClass.newInstance();

        Set<Object> mockCandidates = newMockSafeHashSet(3);

        List<Field> orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(testObject.getClass());
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, testObject, false, orderedInstanceFields);
        Assert.assertFalse(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidatesOnFields_MockFoundByType() {
        Class<?> testClass = TestUtils.getTestClass(TEST_CLASS, TestUtils.TEST_CLASS_SUFFIX);
        Object testObject = testClass.newInstance();

        Set<Object> mockCandidates = newMockSafeHashSet(3);
        mockCandidates.add(new MockitoTestMock() {});
        mockCandidates.add(new Object() {});
        mockCandidates.add(new Object() {});

        List<Field> orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(testObject.getClass());
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, testObject, false, orderedInstanceFields);
        Assert.assertTrue(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidatesOnFields_MockFoundByName() {
        Class<?> testClass = TestUtils.getTestClass(TEST_CLASS, TestUtils.TEST_CLASS_SUFFIX);
        Object testObject = testClass.newInstance();

        Set<Object> mockCandidates = newMockSafeHashSet(3);
        mockCandidates.add(new Object() {});
        mockCandidates.add(new MockitoTestMock() {});
        mockCandidates.add(new Object() {});

        List<Field> orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(testObject.getClass());
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, testObject, false, orderedInstanceFields);
        Assert.assertTrue(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidatesOnFields_MockFoundByTypeAndName() {
        Class<?> testClass = TestUtils.getTestClass(TEST_CLASS, TestUtils.TEST_CLASS_SUFFIX);
        Object testObject = testClass.newInstance();

        Set<Object> mockCandidates = newMockSafeHashSet(3);
        mockCandidates.add(new Object() {});
        mockCandidates.add(new MockitoTestMock() {});
        mockCandidates.add(new MockitoTestMockNamed() {});

        List<Field> orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(testObject.getClass());
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, testObject, false, orderedInstanceFields);
        Assert.assertTrue(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidatesOnFields_MockNotFound() {
        Class<?> testClass = TestUtils.getTestClass(TEST_CLASS, TestUtils.TEST_CLASS_SUFFIX);
        Object testObject = testClass.newInstance();

        Set<Object> mockCandidates = newMockSafeHashSet(3);

        List<Field> orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(testObject.getClass());
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, testObject, false, orderedInstanceFields);
        Assert.assertFalse(injectionOccurred);
    }

    @Test
    public void testProcessInjection_FieldIsFinal() throws InstantiationException, IllegalAccessException {
        Class<?> testClass = TestUtils.getTestClass(TEST_CLASS, TestUtils.TEST_CLASS_SUFFIX);
        Object testObject = testClass.newInstance();
        Field injectMocksField = getField(testObject, "propertyAndSetterInjectionFieldIsFinal");
        injectMocksField.setAccessible(true);
        Set<Object> mockCandidates = newMockSafeHashSet(3);

        propertyAndSetterInjection.processInjection(injectMocksField, testObject, mockCandidates);
    }

    @Test
    public void testProcessInjection_FieldIsStatic() throws InstantiationException, IllegalAccessException {
        Class<?> testClass = TestUtils.getTestClass(TEST_CLASS, TestUtils.TEST_CLASS_SUFFIX);
        Object testObject = testClass.newInstance();
        Field injectMocksField = getField(testObject, "propertyAndSetterInjectionFieldIsStatic");
        injectMocksField.setAccessible(true);
        Set<Object> mockCandidates = newMockSafeHashSet(3);

        propertyAndSetterInjection.processInjection(injectMocksField, testObject, mockCandidates);
    }

    @Test
    public void testInitializeInjectMocksField_ConstructorThrowException() throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?> testClass = TestUtils.getTestClass(TEST_CLASS, TestUtils.TEST_CLASS_SUFFIX);
        Object testObject = testClass.newInstance();
        Field injectMocksField = getField(testObject, "propertyAndSetterInjectionFieldConstructorException");
        injectMocksField.setAccessible(true);
        Set<Object> mockCandidates = newMockSafeHashSet(3);

        try {
            propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, testObject);
        } catch (MockitoException e) {
            Assert.assertEquals("Cannot initialize field 'propertyAndSetterInjectionFieldConstructorException' value", e.getMessage());
        }
    }

    @Test
    public void testInitializeInjectMocksField_NoConstructor() throws InstantiationException, IllegalAccessException {
        Class<?> testClass = TestUtils.getTestClass(TEST_CLASS, TestUtils.TEST_CLASS_SUFFIX);
        Object testObject = testClass.newInstance();
        Field injectMocksField = getField(testObject, "propertyAndSetterInjectionFieldNoConstructor");
        injectMocksField.setAccessible(true);
        Set<Object> mockCandidates = newMockSafeHashSet(3);

        propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, testObject);
    }

    @Test
    public void testInjectMockCandidatesOnFields_MockNotInjectedViaPropertySetter() throws InstantiationException, IllegalAccessException {
        Class<?> testClass = TestUtils.getTestClass(TEST_CLASS, TestUtils.TEST_CLASS_SUFFIX);
        Object testObject = testClass.newInstance();

        Set<Object> mockCandidates = newMockSafeHashSet(3);
        mockCandidates.add(new MockitoTestMock() {});
        mockCandidates.add(new Object() {});
        mockCandidates.add(new Object() {});

        List<Field> orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(testObject.getClass());
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, testObject, false, orderedInstanceFields);
        Assert.assertFalse(injectionOccurred);
    }

    private static class MockitoTestMock {
    }

    private static class MockitoTestMockNamed {
        public String name = "MockitoTestMockNamed";
    }
}

class MockitoTestMockField {
    @InjectMocks
    private PropertyAndSetterInjection propertyAndSetterInjectionField;
}

class MockitoTestMockFieldWithException {
    @InjectMocks
    private PropertyAndSetterInjection propertyAndSetterInjectionFieldWithException;

    private Object fieldInstanceNeedingInjection;

    public Object getFieldInstanceNeedingInjection() {
        return fieldInstanceNeedingInjection;
    }
}

class MockitoTestMockFieldIsFinal {
    @InjectMocks
    private final PropertyAndSetterInjection propertyAndSetterInjectionFieldIsFinal;
}

class MockitoTestMockFieldIsStatic {
    @InjectMocks
    private static PropertyAndSetterInjection propertyAndSetterInjectionFieldIsStatic;
}

class MockitoTestMockFieldConstructorException {
    @InjectMocks
    private PropertyAndSetterInjection propertyAndSetterInjectionFieldConstructorException;

    public PropertyAndSetterInjection getPropertyAndSetterInjectionFieldConstructorException() {
        return propertyAndSetterInjectionFieldConstructorException;
    }
}

class MockitoTestMockFieldNoConstructor {
    @InjectMocks
    private PropertyAndSetterInjection propertyAndSetterInjectionFieldNoConstructor;
}