package org.mockito.internal.configuration;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.FieldType;
import org.mockito.mockitosis.*;
import org.mockito.verification.VerificationMode;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultInjectionEngineTest {

    @Test
    public void testInjectMocksOnFields() {
        Reporter reporter = Mockito.spy(Reporter.class);
        Set<Field> injectMocksFields = getFieldsAnnotatedWithInjectMocks();
        Set<Object> mocks = createMocks();
        Object testClassInstance = new TestClass();

        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        for (Field field : injectMocksFields) {
            FieldType fieldType = new FieldType(field);
            Object injectedMock = field.get(testClassInstance);
            printMockInfo(field, injectedMock, mocks);
            Assert.assertNotNull(injectedMock);
        }

        // Verify that all mocks are injected only once
        Set<Object> remainingMocks = new HashSet<>(mocks);
        for (Field field : getAllFieldsFromClass(TestClass.class)) {
            Object fieldInstance = field.get(testClassInstance);
            if (fieldInstance != null) {
                mockCandidateFilterTest(remainingMocks, field, fieldInstance);
            }
        }
        Assert.assertEquals(Collections.emptySet(), remainingMocks);

        Mockito.verifyNoMoreInteractions(reporter);
    }

    @Test
    public void testInjectMocksOnFields_MockitoException() {
        Reporter reporter = Mockito.spy(Reporter.class);
        Set<Field> injectMocksFields = getFieldsAnnotatedWithInjectMocks();
        Set<Object> mocks = createMocks();
        Object testClassInstance = new TestClass();

        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        try {
            injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        } catch (MockitoException e) {
            // Expected
        }

        for (Field field : injectMocksFields) {
            Assert.assertNull(field.get(testClassInstance));
        }

        Mockito.verify(reporter).cannotInitializeForInjectMocksAnnotation(Mockito.anyString(), Mockito.any(MockitoException.class));
    }

    @Test
    public void testInjectMockCandidate_MultipleCandidates() {
        Reporter reporter = Mockito.spy(Reporter.class);
        Set<Field> injectMocksFields = getFieldsAnnotatedWithInjectMocks();
        Set<Object> mocks = createMocks();
        Object testClassInstance = new TestClass();

        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        Set<Object> multipleCandidates = new HashSet<>();
        multipleCandidates.add(new ClassWithMultipleCandidates());
        multipleCandidates.add(new ClassWithMultipleCandidates());

        try {
            injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        } catch (MockitoException e) {
            // Expected
        }

        Mockito.verifyNoInteractions(reporter);

        for (Field field : injectMocksFields) {
            Assert.assertNull(field.get(testClassInstance));
        }
    }

    @Test
    public void testInjectMockCandidate_OneCandidate() {
        Reporter reporter = Mockito.spy(Reporter.class);
        Set<Field> injectMocksFields = getFieldsAnnotatedWithInjectMocks();
        Set<Object> mocks = createMocks();
        Object testClassInstance = new TestClass();

        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        Set<Object> oneCandidate = new HashSet<>();
        oneCandidate.add(new ClassWithOneCandidate());

        try {
            injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        } catch (MockitoException e) {
            // Expected
        }

        Mockito.verifyNoInteractions(reporter);

        for (Field field : injectMocksFields) {
            Object injectedMock = field.get(testClassInstance);
            Assert.assertNotNull(injectedMock);
            Assert.assertEquals(oneCandidate.iterator().next(), injectedMock);
        }
    }

    @Test
    public void testInjectMockCandidate_NullField() {
        Reporter reporter = Mockito.spy(Reporter.class);
        Set<Field> injectMocksFields = getFieldsAnnotatedWithInjectMocks();
        Set<Object> mocks = createMocks();
        Object testClassInstance = new TestClass();

        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        try {
            injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        } catch (MockitoException e) {
            // Expected
        }

        Mockito.verifyNoInteractions(reporter);

        for (Field field : injectMocksFields) {
            Assert.assertNull(field.get(testClassInstance));
        }
    }

    private List<Field> getAllFieldsFromClass(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .sorted(Comparator.comparing(f -> f.getName()))
                .collect(Collectors.toList());
    }

    private Set<Field> getFieldsAnnotatedWithInjectMocks() {
        return Arrays.stream(TestClass.class.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(InjectMocks.class))
                .collect(Collectors.toSet());
    }

    private Set<Object> createMocks() {
        Set<Object> mocks = new HashSet<>();
        mocks.add(Mockito.mock(ClassWithOneCandidate.class));
        mocks.add(Mockito.mock(ClassWithMultipleCandidates.class));
        return mocks;
    }

    private void printMockInfo(Field field, Object mock, Set<Object> mocks) {
        String className = mock.getClass().getName();
        String fieldName = field.getName();
        System.out.println("Injected mock '" + fieldName + "' of type '" + className + "' for field '" + field.getName() + "'.");
        System.out.println("Other mocks: " + mocks);
    }

    private void mockCandidateFilterTest(Set<Object> mocks, Field field, Object fieldInstance) {
        Mockito.verify(mocks).thenReturn(fieldInstance);
    }

    private static class TestClass {
        @InjectMocks
        private ClassWithOneCandidate oneMock;

        @InjectMocks
        private ClassWithMultipleCandidates multipleMocks;
    }

    private static class ClassWithOneCandidate {
    }

    private static class ClassWithMultipleCandidates {
    }
}