package org.junit.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.fields.FieldAccessibilityHelper;
import org.mockito.internal.configuration.fields.Fields;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.FieldWithAnnotations;
import org.mockito.internal.util.reflection.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine defaultInjectionEngine;
    private Set<Object> mocks = new HashSet<>();
    private Set<Field> injectMocksFields = new HashSet<>();
    private Object testClassInstance;

    @Before
    public void setUp() throws Exception {
        defaultInjectionEngine = new DefaultInjectionEngine();

        testClassInstance = getTestClassInstance();
        injectMocksFields = new HashSet<>(Arrays.asList(getFieldsAnnotatedWithInjectMocks(testClassInstance.getClass())));
        mocks = new HashSet<>( Arrays.asList(Mockito.mock(String.class), Mockito.mock(Integer.class)) );
    }

    @Test
    public void testInjectMocksOnFields() throws Exception {
        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        for (Field field : injectMocksFields) {
            Object fieldValue = FieldAccessibilityHelper.getFieldValue(testClassInstance, field);
            verify(fieldValue).equals(mocks.iterator().next());
        }
    }

    @Test
    public void testInjectMockCandidate() throws Exception {
        DefaultInjectionEngine defaultInjectionEngine = new DefaultInjectionEngine();

        Set<Object> mocks = new HashSet<>();
        Object fieldInstance = Mockito.mock(String.class);
        Class<?> awaitingInjectionClazz = String.class;

        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        verify(defaultInjectionEngine, times(1)).mockCandidateFilter(eq(mocks), any(Field.class), any());
    }

    @Test
    public void testOrderedInstanceFieldsFrom() throws Exception {
        DefaultInjectionEngine defaultInjectionEngine = new DefaultInjectionEngine();

        Class<?> awaitingInjectionClazz = String.class;

        Field[] orderedInstanceFields = defaultInjectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        Field expectedField = Fields.privateField(awaitingInjectionClazz, "fieldName");

        assertArrayEquals(new Field[]{expectedField}, orderedInstanceFields);
    }

    @Test(expected = MockitoException.class)
    public void testInjectMocksOnFieldsWithDuplicates() throws Exception {
        DefaultInjectionEngine defaultInjectionEngine = new DefaultInjectionEngine();

        Set<Field> injectMocksFields = new HashSet<>(Arrays.asList(getField1(), getField1(), getField2()));
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = getTestClassInstance();

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
    }

    @Test
    public void testInjectMockCandidateNoCandidates() throws Exception {
        DefaultInjectionEngine defaultInjectionEngine = new DefaultInjectionEngine();

        Set<Object> mocks = new HashSet<>();
        Object fieldInstance = Mockito.mock(String.class);
        Class<?> awaitingInjectionClazz = Class.forName("NonExistentClass");

        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        verify(defaultInjectionEngine, never()).mockCandidateFilter(any(Set.class), any(Field.class), any());
    }

    @Test
    public void testInjectMockCandidateMultipleCandidates() throws Exception {
        DefaultInjectionEngine defaultInjectionEngine = new DefaultInjectionEngine();

        Set<Object> mocks = new HashSet<>(Arrays.asList(Mockito.mock(String.class), Mockito.mock(Integer.class), Mockito.mock(Float.class)));
        Object fieldInstance = Mockito.mock(String.class);
        Class<?> awaitingInjectionClazz = String.class;

        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        verify(defaultInjectionEngine, times(2)).mockCandidateFilter(eq(mocks), any(Field.class), any());
    }

    private Object getTestClassInstance() throws Exception {
        Class<?> testClass = Class.forName("TestClass");
        return ReflectionUtils.newInstance(testClass);
    }

    private Set<Field> getFieldsAnnotatedWithInjectMocks(Class<?> clazz) throws Exception {
        Set<Field> fields = new HashSet<>();
        Field[] declaredFields = clazz.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(InjectMocks.class)) {
                fields.add(field);
            }
        }

        if (clazz.getSuperclass() != null) {
            fields.addAll(getFieldsAnnotatedWithInjectMocks(clazz.getSuperclass()));
        }

        return fields;
    }

    private Field getField1() throws Exception {
        return Fields.privateField(TestClass.class, "field1");
    }

    private Field getField2() throws Exception {
        return Fields.privateField(TestClass.class, "field2");
    }
}

class TestClass {
    @InjectMocks
    private String field1;

    @InjectMocks
    private Integer field2;
}