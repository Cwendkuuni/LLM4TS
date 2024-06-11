package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.mockitosis.annotatedclasses.AnnotatedTestClass;
import org.mockito.mockitosis.testclasses.TestClassWithInjectMocks;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;
    private Set<Object> mocks;

    @Before
    public void setUp() {
        injectionEngine = new DefaultInjectionEngine();
        mocks = new HashSet<>();
    }

    @Test
    public void testInjectMocksOnFields() {
        TestClassWithInjectMocks testClass = new AnnotatedTestClass<>(TestClassWithInjectMocks.class);
        Set<Field> injectMocksFields = Arrays.asList(testClass.getFieldsAnnotatedWith(Mockito.InjectMocks.class)).stream().collect(HashSet::new, HashSet::add, HashSet::addAll).iterator();
        Object testClassInstance = Mockito.spy(testClass.getTestClass());

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        for (Field field : injectMocksFields) {
            Object fieldInstance = Mockito.getFieldsAccessibleValue(testClassInstance, field);
            assertNotNull(fieldInstance);
            Class<?> fieldClass = fieldInstance.getClass();
            while (fieldClass != Object.class) {
                Field mockCandidate = Arrays.stream(fieldClass.getDeclaredFields()).filter(DefaultInjectionEngineTest::isMockCandidate).findFirst().orElse(null);
                if (mockCandidate != null) {
                    Object mock = mocks.iterator().next();
                    Mockito.verify(mock).isMock();
                    Mockito.verifyNoMoreInteractions(mock);
                    field.setAccessible(true);
                    field.set(testClassInstance, mock);
                    Mockito.verify(mock).isSpy();
                    Mockito.verifyNoMoreInteractions(mock);
                    mocks.remove(mock);
                }
                fieldClass = fieldClass.getSuperclass();
            }
        }
    }

    @Test(expected = MockitoException.class)
    public void testExceptionOnFailedToInitializeField() {
        TestClassWithInjectMocks testClass = new AnnotatedTestClass<>(TestClassWithInjectMocks.class);
        Set<Field> injectMocksFields = Arrays.asList(testClass.getFieldsAnnotatedWith(Mockito.InjectMocks.class)).stream().collect(HashSet::new, HashSet::add, HashSet::addAll).iterator();
        Object testClassInstance = Mockito.spy(testClass.getTestClass());
        Field fieldWithException = new Field("fieldWithException", String.class, testClass.getTestClass(), null);
        Mockito.doThrow(new RuntimeException("Test Exception")).when(testClassInstance).newField(Mockito.eq("fieldWithException"));
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
    }

    @Test
    public void testNoMockCandidateAvailable() {
        TestClassWithInjectMocks testClass = new AnnotatedTestClass<>(TestClassWithInjectMocks.class);
        Set<Field> injectMocksFields = Arrays.asList(testClass.getFieldsAnnotatedWith(Mockito.InjectMocks.class)).stream().collect(HashSet::new, HashSet::add, HashSet::addAll).iterator();
        Object testClassInstance = Mockito.spy(testClass.getTestClass());
        Class<?> awaitingInjectionClazz = TestClassWithInjectMocks.class.getSuperclass();
        while (!awaitingInjectionClazz.equals(Object.class)) {
            for (Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
                if (isMockCandidate(field)) {
                    throw new IllegalStateException("Mock candidate found, test case must fail if no mock candidate is available.");
                }
            }
            awaitingInjectionClazz = awaitingInjectionClazz.getSuperclass();
        }
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
    }

    private boolean isMockCandidate(Field field) {
        return field.isAnnotationPresent(Mockito.Mock.class) || field.isAnnotationPresent(Mockito.Spy.class);
    }

    private Field[] orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
        return new DefaultInjectionEngine().orderedInstanceFieldsFrom(awaitingInjectionClazz);
    }
}