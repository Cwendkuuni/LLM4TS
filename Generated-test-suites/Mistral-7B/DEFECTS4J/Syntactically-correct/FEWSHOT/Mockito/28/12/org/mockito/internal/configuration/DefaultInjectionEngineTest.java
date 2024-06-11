package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertNotNull;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;
    private Set<Object> mocks;
    private Object testClassInstance;

    @Before
    public void setup() {
        injectionEngine = new DefaultInjectionEngine();
        mocks = new HashSet<>();
        testClassInstance = new Object() {};
    }

    @Test
    public void testInjectMocksOnFields() {
        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);
        Field field3 = mock(Field.class);

        when(field1.getType()).thenReturn(String.class);
        when(field2.getType()).thenReturn(Integer.class);
        when(field3.getType()).thenReturn(Double.class);

        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(field1);
        injectMocksFields.add(field2);
        injectMocksFields.add(field3);

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // verify mocks were injected in the correct order
        verify(field1, times(1)).set(testClassInstance, any(String.class));
        verify(field2, times(1)).set(testClassInstance, any(Integer.class));
        verify(field3, times(1)).set(testClassInstance, any(Double.class));

        // verify mocks were removed from the mocks set after injection
        assertFalse(mocks.contains(field1));
        assertFalse(mocks.contains(field2));
        assertFalse(mocks.contains(field3));
    }

    @Test
    public void testInjectMockCandidate() {
        Class<?> mockClass = MockitoJUnitRunner.class;
        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);

        when(field1.getType()).thenReturn(mockClass);
        when(field2.getType()).thenReturn(String.class);

        injectionEngine.injectMockCandidate(mockClass, mocks, testClassInstance);

        // verify the mock was injected if it was a candidate
        verify(field1, times(1)).set(testClassInstance, any(mockClass.getClass()));

        // verify no action was taken if no candidate was found
        verifyNoMoreInteractions(field2);
    }

    @Test(expected = MockitoException.class)
    public void testInjectMockCandidateWithMultipleCandidates() {
        Class<?> mockClass1 = String.class;
        Class<?> mockClass2 = Integer.class;

        Field field = mock(Field.class);

        when(field.getType()).thenReturn(mockClass1);

        injectionEngine.injectMockCandidate(mockClass1, mocks, testClassInstance);

        // the first candidate was injected, the second one should not be injected
        injectionEngine.injectMockCandidate(mockClass2, mocks, testClassInstance);
    }

    @Test
    public void testOrderedInstanceFieldsFrom() {
        Class<?> clazz = MockitoJUnitRunner.class;
        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);

        when(field1.getType()).thenReturn(Integer.class);
        when(field2.getType()).thenReturn(String.class);

        when(clazz.getDeclaredFields()).thenReturn(new Field[]{field1, field2});

        DefaultInjectionEngine engine = spy(new DefaultInjectionEngine());
        Field[] fields = engine.orderedInstanceFieldsFrom(clazz);

        // verify the fields are ordered from sub-type to super-type
        assertEquals(field1, fields[0]);
        assertEquals(field2, fields[1]);
    }

    @Test
    public void testSupertypesLastComparator() {
        Class<?> superClass = Object.class;
        Class<?> subClass1 = String.class;
        Class<?> subClass2 = Integer.class;

        DefaultInjectionEngine engine = new DefaultInjectionEngine();

        Comparator<Field> comparator = engine.supertypesLast;

        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);

        when(field1.getType()).thenReturn(subClass1);
        when(field2.getType()).thenReturn(superClass);

        int result = comparator.compare(field1, field2);

        // verify the sub-type field comes before the super-type field
        assertEquals(-1, result);
    }
}