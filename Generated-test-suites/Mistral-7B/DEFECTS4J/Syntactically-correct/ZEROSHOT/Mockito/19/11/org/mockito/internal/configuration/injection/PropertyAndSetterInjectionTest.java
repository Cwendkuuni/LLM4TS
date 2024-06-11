package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import static org.junit.Assert.*;
import static org.mockito.internal.util.reflection.Reflection.*;

public class PropertyAndSetterInjectionTest {

    private static final Class<PropertyAndSetterInjection> CLASS = PropertyAndSetterInjection.class;
    private static final MockCandidateFilter FILTER = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));

    @Rule
    public MockitoSystemRule systemRule = new MockitoSystemRule();

    @Before
    public void setUp() {
        systemRule.loadFile("org/mockito/internal/configuration/injection/PropertyAndSetterInjection.class");
    }

    @Test
    public void testProcessInjectionSuccess() throws Exception {
        // given
        Object owner = newObject(Object.class);
        Class<?> targetClass = newClass(TestClass.class);
        Field injectMocksField = newField(targetClass, "injectMocks", Object.class);
        injectMocksField.setAccessible(true);
        injectMocksField.set(owner, new Object());

        Set<Object> mockCandidates = newMockSafeHashSet(new Object[]{newObject(TestMockA.class), newObject(TestMockB.class)});

        // when
        PropertyAndSetterInjection sut = new PropertyAndSetterInjection();
        boolean result = sut.processInjection(injectMocksField, owner, mockCandidates);

        // then
        assertTrue(result);
        assertEquals(1, mockCandidates.size());

        Field injectedField = getFieldValue(injectMocksField, owner);
        assertEquals(newObject(TestMockA.class), injectedField);
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjectionFailure() throws Exception {
        // given
        Object owner = newObject(Object.class);
        Class<?> targetClass = newClass(TestClass.class);
        Field injectMocksField = newField(targetClass, "injectMocks", Object.class);
        injectMocksField.setAccessible(true);
        injectMocksField.set(owner, new Object());

        Set<Object> mockCandidates = newMockSafeHashSet(new Object[]{newObject(TestMockA.class), newObject(TestMockB.class), newObject(TestMockC.class)});

        // when
        PropertyAndSetterInjection sut = new PropertyAndSetterInjection();
        sut.processInjection(injectMocksField, owner, mockCandidates);

        // then
        fail("Expected MockitoException");
    }

    @Test
    public void testInitializeInjectMocksFieldSuccess() throws Exception {
        // given
        Object owner = newObject(Object.class);
        Class<?> targetClass = newClass(TestClass.class);
        Field injectMocksField = newField(targetClass, "injectMocks", Object.class);
        injectMocksField.setAccessible(true);
        injectMocksField.set(owner, new Object());
        Field fieldToInject = newField(targetClass, "fieldToInject", Object.class);
        fieldToInject.setAccessible(true);
        fieldToInject.set(owner, new Object());

        // when
        PropertyAndSetterInjection sut = new PropertyAndSetterInjection();
        FieldInitializationReport report = sut.initializeInjectMocksField(injectMocksField, owner);

        // then
        assertEquals(targetClass, report.fieldClass());
        assertEquals(owner, report.fieldInstance());
    }

    @Test(expected = MockitoException.class)
    public void testInitializeInjectMocksFieldFailure() throws Exception {
        // given
        Object owner = newObject(Object.class);
        Class<?> targetClass = newClass(TestClass.class);
        Field injectMocksField = newField(targetClass, "injectMocks", Object.class);
        injectMocksField.setAccessible(true);
        injectMocksField.set(owner, new Object());
        Field fieldToInject = newField(targetClass, "fieldToInject", Object.class);
        fieldToInject.setAccessible(true);
        fieldToInject.set(owner, new Object());

        // when
        PropertyAndSetterInjection sut = new PropertyAndSetterInjection();
        sut.initializeInjectMocksField(injectMocksField, owner);

        // then
        fail("Expected MockitoException");
    }

    @Test
    public void testInjectMockCandidatesSuccess() throws Exception {
        // given
        Class<?> awaitingInjectionClazz = newClass(TestClass.class);
        Object owner = newObject(awaitingInjectionClazz);
        Set<Object> mockCandidates = newMockSafeHashSet(new Object[]{newObject(TestMockA.class), newObject(TestMockB.class)});

        // when
        PropertyAndSetterInjection sut = new PropertyAndSetterInjection();
        boolean result = sut.injectMockCandidates(awaitingInjectionClazz, mockCandidates, owner);

        // then
        assertTrue(result);
        assertEquals(1, mockCandidates.size());

        Object injected = getFieldValue(getField("fieldToInject", owner), owner);
        assertEquals(newObject(TestMockA.class), injected);
    }

    @Test
    public void testInjectMockCandidatesFailure() throws Exception {
        // given
        Class<?> awaitingInjectionClazz = newClass(TestClass.class);
        Object owner = newObject(awaitingInjectionClazz);
        Set<Object> mockCandidates = newMockSafeHashSet(new Object[]{newObject(TestMockA.class), newObject(TestMockB.class), newObject(TestMockC.class)});

        // when
        PropertyAndSetterInjection sut = new PropertyAndSetterInjection();
        boolean result = sut.injectMockCandidates(awaitingInjectionClazz, mockCandidates, owner);

        // then
        assertFalse(result);
        assertEquals(3, mockCandidates.size());
    }

    @Test
    public void testInjectMockCandidatesOnFieldsSuccess() throws Exception {
        // given
        Class<?> awaitingInjectionClazz = newClass(TestClass.class);
        Object owner = newObject(awaitingInjectionClazz);
        Set<Object> mockCandidates = newMockSafeHashSet(new Object[]{newObject(TestMockA.class), newObject(TestMockB.class)});

        // when
        PropertyAndSetterInjection sut = new PropertyAndSetterInjection();
        boolean result = sut.injectMockCandidatesOnFields(mockCandidates, owner, false, getDeclaredFields(awaitingInjectionClazz));

        // then
        assertTrue(result);
        assertEquals(1, mockCandidates.size());

        Object injected = getFieldValue(getField("fieldToInject1", owner), owner);
        assertEquals(newObject(TestMockA.class), injected);
    }

    @Test
    public void testInjectMockCandidatesOnFieldsFailure() throws Exception {
        // given
        Class<?> awaitingInjectionClazz = newClass(TestClass.class);
        Object owner = newObject(awaitingInjectionClazz);
        Set<Object> mockCandidates = newMockSafeHashSet(new Object[]{newObject(TestMockA.class), newObject(TestMockB.class), newObject(TestMockC.class)});

        // when
        PropertyAndSetterInjection sut = new PropertyAndSetterInjection();
        boolean result = sut.injectMockCandidatesOnFields(mockCandidates, owner, false, getDeclaredFields(awaitingInjectionClazz));

        // then
        assertFalse(result);
        assertEquals(3, mockCandidates.size());
    }

    @Test
    public void testOrderedInstanceFields() throws Exception {
        // given
        Class<?> awaitingInjectionClazz = newClass(TestClass.class);

        // when
        List<Field> result = new PropertyAndSetterInjection().orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // then
        List<Field> expected = Arrays.asList(
                getField("fieldToInject3", awaitingInjectionClazz),
                getField("fieldToInject2", awaitingInjectionClazz),
                getField("fieldToInject1", awaitingInjectionClazz)
        );

        assertEquals(expected, result);
    }

    private static class TestClass {
        @InjectMocks
        private Object injectMocks;

        private Object fieldToInject;
        private Object fieldToInject1;
        private Object fieldToInject2;
        private Object fieldToInject3;
    }

    private static class TestMockA {}

    private static class TestMockB {}

    private static class TestMockC {}
}