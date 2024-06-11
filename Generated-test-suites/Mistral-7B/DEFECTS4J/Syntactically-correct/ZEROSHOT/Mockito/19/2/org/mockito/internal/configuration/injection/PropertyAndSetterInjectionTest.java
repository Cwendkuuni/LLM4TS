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
import org.mockito.internal.util.reflection.testutil.FieldTestUtils;
import org.mockito.internal.util.reflection.testutil.TestClassWithInjectMocksAnnotatedFields;
import org.mockito.internal.util.reflection.testutil.TestClassWithMockCandidates;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Test class for PropertyAndSetterInjection Testing all methods in the class PropertyAndSetterInjection.
 */
public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection propertyAndSetterInjection;
    private TestClassWithMockCandidates testClassWithMockCandidates;
    private TestClassWithInjectMocksAnnotatedFields testClassWithInjectMocksAnnotatedFields;
    private List<Field> orderedInstanceFields;
    private Set<Object> mockCandidates;

    @Before
    public void setUp() {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
        testClassWithMockCandidates = new TestClassWithMockCandidates();
        testClassWithInjectMocksAnnotatedFields = new TestClassWithInjectMocksAnnotatedFields();
        mockCandidates = Sets.newMockSafeHashSet();
        mockCandidates.add(testClassWithMockCandidates.getMockCandidate1());
        mockCandidates.add(testClassWithMockCandidates.getMockCandidate2());
    }

    @Test
    public void testProcessInjection() throws Exception {
        Field injectMocksField = testClassWithInjectMocksAnnotatedFields.getClass().getDeclaredField("injectMocksField");
        Object injectMocksFieldOwner = testClassWithInjectMocksAnnotatedFields;

        // Test when the processInjection method initializes the field annotated by @InjectMocks
        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);
        Object injectedFieldInstance = report.getFieldInstance();

        // Test if the injected field instance is null
        assertThat(injectedFieldInstance, is(nullValue()));

        // Test when the processInjection method injects mocks using setters
        Field setterField = testClassWithInjectMocksAnnotatedFields.getClass().getDeclaredField("setterField");
        setterField.setAccessible(true);
        Object mockToInject = mock(TestClassWithMockCandidates.class);
        setterField.set(injectMocksFieldOwner, mockToInject);

        // Test when the processInjection method injects mocks using fields
        injectedFieldInstance = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Test if the injected field instance is not null
        assertThat(injectedFieldInstance, is(notNullValue()));

        // Test if the mock is injected correctly
        verify(mockToInject).someMethod();

        // Test when the processInjection method does not inject any mock
        Field nonInjectableField = testClassWithInjectMocksAnnotatedFields.getClass().getDeclaredField("nonInjectableField");
        nonInjectableField.setAccessible(true);
        nonInjectableField.set(injectMocksFieldOwner, "non-mock value");

        // Test when the processInjection method fails to inject a mock using setter
        Field unreachableSetterField = testClassWithInjectMocksAnnotatedFields.getClass().getDeclaredField("unreachableSetterField");
        unreachableSetterField.setAccessible(true);
        Field unreachableField = testClassWithInjectMocksAnnotatedFields.getClass().getDeclaredField("unreachableField");
        unreachableField.setAccessible(true);
        unreachableSetterField.set(injectMocksFieldOwner, unreachableField);

        try {
            propertyAndSetterInjection.processInjection(unreachableField, injectMocksFieldOwner, mockCandidates);
            fail();
        } catch (MockitoException e) {
            // Expected
        }
    }

    @Test
    public void testInjectMockCandidates() throws Exception {
        Class<?> awaitingInjectionClazz = testClassWithInjectMocksAnnotatedFields.getClass();

        // Test when the injectMockCandidates method injects mocks using setters
        orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);
        propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mockCandidates, testClassWithInjectMocksAnnotatedFields);
        Field mockInjectedField = orderedInstanceFields.get(0);
        Object mockInjected = mockCandidates.iterator().next();
        verify(mockInjected).someMethod();

        // Test when the injectMockCandidates method injects mocks using fields
        mockCandidates.remove(mockInjected);
        verifyNoMoreInteractions(mockInjected);

        mockInjectedField.setAccessible(true);
        mockInjectedField.set(testClassWithInjectMocksAnnotatedFields, mockInjected);
        propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mockCandidates, testClassWithInjectMocksAnnotatedFields);
        verify(mockInjected, times(2)).someMethod();

        // Test when the injectMockCandidates method does not inject any mock
        TestClassWithNoInjectableFields testClassWithNoInjectableFields = new TestClassWithNoInjectableFields();
        propertyAndSetterInjection.injectMockCandidates(testClassWithNoInjectableFields.getClass(), mockCandidates, testClassWithNoInjectableFields);
        verifyNoMoreInteractions(mockCandidates.toArray());
    }

    @Test
    public void testInitializeInjectMocksField() throws Exception {
        Field injectMocksField = testClassWithInjectMocksAnnotatedFields.getClass().getDeclaredField("injectMocksField");
        Object injectMocksFieldOwner = testClassWithInjectMocksAnnotatedFields;

        // Test when the initializeInjectMocksField method initializes the field annotated by @InjectMocks using a no-arg constructor
        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);
        Object fieldInstance = report.getFieldInstance();

        // Test when the field is annotated with @InjectMocks but is not initialized
        Class<?> fieldType = injectMocksField.getType();
        Constructor<?> constructor = fieldType.getDeclaredConstructor();

        // Test when the initializeInjectMocksField method fails to initialize the field
        try {
            propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);
            fail();
        } catch (MockitoException e) {
            // Expected
        }

        // Test when the initializeInjectMocksField method initializes the field annotated by @InjectMocks using the provided constructor
        constructor.setAccessible(true);
        Object constructorInstance = constructor.newInstance();
        report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner, constructorInstance);
        assertThat(report.getFieldInstance(), is(notNullValue()));
    }
}