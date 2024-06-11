package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.BaseMockitoException;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;
    private Object testClassInstance;

    @Before
    public void setUp() throws Exception {
        testClassInstance = new TestClass();
        injectionEngine = new DefaultInjectionEngine();
    }

    @Test
    public void shouldInjectMocksOnFieldsAnnotatedWithInjectMocksCorrectly() {
        // Given
        Set<Field> injectMocksFields = Arrays.asList(TestClass.class.getDeclaredFields())
                .stream()
                .filter(Field::isAnnotationPresent)
                .filter(field -> field.isAnnotationPresent(InjectMocks.class))
                .collect(Collectors.toSet());
        Set<Object> mocks = new HashSet<>();
        mocks.add(Mockito.mock(MockedClass1.class));
        mocks.add(Mockito.mock(MockedClass2.class));

        // When
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Then
        for (Field field : injectMocksFields) {
            Object injectedMock = null;
            try {
                injectedMock = Whitebox.getInternalState(testClassInstance, field.getName());
            } catch (NoSuchFieldException e) {
                // This case is expected, since the field is private
            }
            assertNotNull(injectedMock);
            verify(injectedMock).someMethod(); // Replace "someMethod" with the actual method to test
        }
    }

    @Test(expected = BaseMockitoException.class)
    public void shouldFailWhenNoValidMockCandidateIsFound() {
        // Given
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(TestClass.class.getDeclaredField("injectMocksFieldWithNoMockCandidate"));
        Set<Object> mocks = new HashSet<>();

        // When
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Then
        // The test will fail due to the BaseMockitoException being thrown
    }

    @Test
    public void shouldInjectMocksOnFieldsAnnotatedWithInjectMocksInSuperClass() {
        // Given
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(TestClassWithSuperclass.class.getDeclaredField("injectMocksFieldInSuperclass"));
        Set<Object> mocks = new HashSet<>();
        mocks.add(Mockito.mock(MockedClass1.class));
        TestClassWithSuperclass testClassInstance = new TestClassWithSuperclass();

        // When
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Then
        Object injectedMock = Whitebox.getInternalState(testClassInstance, "injectMocksFieldInSuperclass");
        assertNotNull(injectedMock);
        verify(injectedMock).someMethod(); // Replace "someMethod" with the actual method to test
    }

    @Test
    public void shouldNotFailWhenNoInjectMocksAnnotationIsPresent() {
        // Given
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(TestClass.class.getDeclaredField("fieldWithoutInjectMocksAnnotation"));
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new TestClass();

        // When
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Then
        // No exception is thrown, since there's no @InjectMocks annotation on the field
    }

    private static class TestClass {
        @InjectMocks
        private MockedClass1 mockedClass1;

        private MockedClass2 mockedClass2;

        // Add other fields as needed
    }

    private static class TestClassWithSuperclass extends TestClass {
        @InjectMocks
        private MockedClass3 mockedClass3;

        // Add other fields as needed
    }

    private interface MockedClass1 {
        void someMethod();
    }

    private interface MockedClass2 {
        void someMethod();
    }

    private interface MockedClass3 {
        void someMethod();
    }
}