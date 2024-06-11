package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
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
import org.powermock.reflect.Whitebox;
import org.powermock.modules.testng.PowerMockTestCase;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * PropertyAndSetterInjectionTest class to test the functionality of PropertyAndSetterInjection class.
 */
public class PropertyAndSetterInjectionTest extends PowerMockTestCase {

    private static final String CLASS_NAME = "org.test.TestClass";
    private static final String FIELD_NAME_1 = "field1";
    private static final String FIELD_NAME_2 = "field2";
    private static final String FIELD_TYPE_1 = "java.lang.String";
    private static final String FIELD_TYPE_2 = "org.test.TestClass";
    private static final String INJECT_MOCK_ANNOTATION = "org.mockito.InjectMocks";

    private PropertyAndSetterInjection propertyAndSetterInjection;
    private Object testClassInstance;

    @Rule
    public MockitoSystemRule mockitoSystemRule = new MockitoSystemRule();

    @Before
    public void setup() throws Exception {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
        TestClass testClass = new TestClass();
        testClassInstance = Whitebox.getInternalState(testClass, "instance");
        Whitebox.setInternalState(testClass, "instance", testClassInstance);
    }

    @Test
    public void testProcessInjection() throws Exception {
        Field injectMocksField = Whitebox.getInternalState(testClass, "injectMocksField");
        Set<Object> mockCandidates = Sets.newMockSafeHashSet();
        mockCandidates.add(new TestClass1());
        mockCandidates.add(new TestClass2());

        // Test the scenario where all dependencies are found through setters and fields.
        Whitebox.setInternalState(testClassInstance, "injectMocksField", new Object());
        Whitebox.setInternalState(injectMocksField, "field1", new String("Value1"));
        Whitebox.setInternalState(injectMocksField, "field2", new TestClass1());

        // Test the functionality of PropertyAndSetterInjection.processInjection method.
        boolean injectionOccurred = propertyAndSetterInjection.processInjection(injectMocksField, testClassInstance, mockCandidates);

        // Assert that all dependencies have been injected.
        assertSame(new TestClass1(), Whitebox.getInternalState(testClassInstance, "field1"));
        assertSame(new TestClass1(), Whitebox.getInternalState(Whitebox.getInternalState(testClassInstance, "injectMocksField"), "field2"));
        assertSame(new TestClass1(), mockCandidates.iterator().next());
        assertFalse(mockCandidates.iterator().hasNext());

        // Test the scenario where a dependency is not found through setters or fields.
        Whitebox.setInternalState(testClassInstance, "injectMocksField", new Object());
        Whitebox.setInternalState(injectMocksField, "field1", null);
        Whitebox.setInternalState(injectMocksField, "field2", null);

        // Test the functionality of PropertyAndSetterInjection.processInjection method.
        injectionOccurred = propertyAndSetterInjection.processInjection(injectMocksField, testClassInstance, mockCandidates);

        // Assert that no dependency has been injected.
        assertNull(Whitebox.getInternalState(testClassInstance, "field1"));
        assertNull(Whitebox.getInternalState(testClassInstance, "field2"));
        assertEquals(2, mockCandidates.size());
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjectionWithException() throws Exception {
        Field injectMocksField = Whitebox.getInternalState(testClass, "injectMocksField");
        Set<Object> mockCandidates = Sets.newMockSafeHashSet();
        mockCandidates.add(new TestClass1());
        mockCandidates.add(new TestClass2());

        // Test the scenario where an exception is thrown during initialization.
        Whitebox.setInternalState(testClassInstance, "injectMocksField", new Object());
        Whitebox.setInternalState(injectMocksField, "field1", new TestClass1());
        Whitebox.setInternalState(injectMocksField, "field2", new TestClass2());

        // Set up a mock exception.
        Whitebox.setInternalState(new TestClass1(), "exceptionMessage", "Exception Message");

        // Test the functionality of PropertyAndSetterInjection.processInjection method.
        propertyAndSetterInjection.processInjection(injectMocksField, testClassInstance, mockCandidates);

        // Assert that an exception has been thrown.
    }

    /**
     * TestClass for the test cases.
     */
    static class TestClass {
        @InjectMocks
        private static Field injectMocksField;
        private static Object instance;

        // Fields to be injected.
        private String field1;
        private TestClass field2;

        public static TestClass getInstance() {
            return (TestClass) instance;
        }
    }

    /**
     * TestClass 1.
     */
    static class TestClass1 {
        private String exceptionMessage;

        public String getExceptionMessage() {
            return exceptionMessage;
        }
    }

    /**
     * TestClass 2.
     */
    static class TestClass2 {
    }
}