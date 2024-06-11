package org.mockito.internal.configuration.injection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injection;
    private Object target;

    @Before
    public void setUp() {
        target = spy(new Object() {
            @InjectMocks
            private Field dependentField;
        });
        injection = new PropertyAndSetterInjection();
    }

    @Test
    public void testSimpleInjection() {
        Mockito.doReturn(mock(Field.class)).when(injection).mockCandidateFilter_filterCandidate(Collections.singletonList(new Field()), target.getClass().getDeclaredField("dependentField"), target);
        FieldInitializationReport report = injection.initializeInjectMocksField(target.getClass().getDeclaredField("dependentField"), target);
        Assert.assertNotNull(report);
        verify(target).setDependentField(Mockito.any());
        verifyNoMoreInteractions(target);
    }

    @Test(expected = MockitoException.class)
    public void testMultipleCandidatesByType() {
        Mockito.doReturn(mock(Field.class)).when(injection).mockCandidateFilter_filterCandidate(Collections.singletonList(new Field()), target.getClass().getDeclaredField("dependentField"), target);
        Mockito.doReturn(mock(Field.class)).when(injection).mockCandidateFilter_filterCandidate(Collections.singletonList(new Field()), target.getClass().getDeclaredField("anotherField"), target);
        assertFalse(injection.processInjection(target.getClass().getDeclaredField("dependentField"), target, Collections.singletonList(mock(Field.class))));
    }

    @Test(expected = MockitoException.class)
    public void testFailOnMultipleCandidatesByName() {
        Mockito.doReturn(mock(Field.class)).when(injection).mockCandidateFilter_filterCandidate(Collections.singletonList(new Field()), target.getClass().getDeclaredField("dependentField"), target);
        Mockito.doReturn(mock(Field.class)).when(injection).mockCandidateFilter_filterCandidate(Collections.singletonList(new Field()), target.getClass().getDeclaredField("anotherFieldWithSameName"), target);
        assertFalse(injection.processInjection(target.getClass().getDeclaredField("dependentField"), target, Collections.singletonList(mock(Field.class))));
    }

    @Test(expected = MockitoException.class)
    public void testCannotInjectFinalField() {
        Mockito.doReturn(mock(Field.class)).when(injection).mockCandidateFilter_filterCandidate(Collections.singletonList(new Field()), target.getClass().getDeclaredField("finalField"), target);
        assertFalse(injection.processInjection(target.getClass().getDeclaredField("finalField"), target, Collections.singletonList(mock(Field.class))));
    }

    @Test(expected = InvocationTargetException.class)
    public void testCannotInitializeField() {
        Mockito.doThrow(new IllegalStateException()).when(new FieldInitializer(target, target.getClass().getDeclaredField("dependentField"))).initialize();
        assertFalse(injection.processInjection(target.getClass().getDeclaredField("dependentField"), target, Collections.singletonList(mock(Field.class))));
    }

    @Test
    public void testEmptyHierarchy() {
        assertTrue(injection.processInjection(target.getClass().getDeclaredField("dependentField"), target, Collections.singletonList(mock(Field.class))));
        verifyZeroInteractions(target);
    }

    @Test
    public void testSimpleHierarchy() {
        Class<?> dependentClass = new Class<>() {};
        Mockito.doReturn(mock(Field.class)).when(injection).mockCandidateFilter_filterCandidate(Collections.singletonList(new Field()), target.getClass().getDeclaredField("dependentField"), target);
        Mockito.doReturn(mock(Field.class)).when(injection).mockCandidateFilter_filterCandidate(Collections.singletonList(new Field()), dependentClass, target.getClass().getDeclaredField("dependentField"));
        FieldInitializationReport report = injection.initializeInjectMocksField(target.getClass().getDeclaredField("dependentField"), target);
        Assert.assertNotNull(report);
        verify(target).setDependentField(Mockito.any());
        verify(dependentClass).setField(Mockito.any(), Mockito.any());
        verifyNoMoreInteractions(target);
    }

    @Test
    public void testMultipleCandidatesByTypeInHierarchy() {
        Class<?> dependentClass1 = new Class<>() {};
        Class<?> dependentClass2 = new Class<>() {};
        Mockito.doReturn(mock(Field.class)).when(injection).mockCandidateFilter_filterCandidate(Collections.singletonList(new Field()), target.getClass().getDeclaredField("dependentField"), target);
        Mockito.doReturn(mock(Field.class)).when(injection).mockCandidateFilter_filterCandidate(Collections.singletonList(new Field()), dependentClass1, target.getClass().getDeclaredField("dependentField"));
        Mockito.doReturn(mock(Field.class)).when(injection).mockCandidateFilter_filterCandidate(Collections.singletonList(new Field()), dependentClass2, target.getClass().getDeclaredField("dependentField"));
        assertFalse(injection.processInjection(target.getClass().getDeclaredField("dependentField"), target, Collections.singletonList(mock(Field.class))));
    }

    @Test
    public void testMultipleCandidatesByNameInHierarchy() {
        Class<?> dependentClass1 = new Class<>() {};
        Class<?> dependentClass2 = new Class<>() {};
        Mockito.doReturn(mock(Field.class)).when(injection).mockCandidateFilter_filterCandidate(Collections.singletonList(new Field()), target.getClass().getDeclaredField("dependentField"), target);
        Mockito.doReturn(mock(Field.class)).when(injection).mockCandidateFilter_filterCandidate(Collections.singletonList(new Field()), dependentClass1, target.getClass().getDeclaredField("dependentFieldWithSameName"));
        Mockito.doReturn(mock(Field.class)).when(injection).mockCandidateFilter_filterCandidate(Collections.singletonList(new Field()), dependentClass2, target.getClass().getDeclaredField("dependentFieldWithSameName"));
        assertFalse(injection.processInjection(target.getClass().getDeclaredField("dependentField"), target, Collections.singletonList(mock(Field.class))));
    }

    @Test
    public void testInjectMockCandidatesOnFields() {
        Mockito.doReturn(mock(Field.class)).when(injection).mockCandidateFilter_filterCandidate(Collections.singletonList(new Field()), target.getClass().getDeclaredField("field1"), target);
        Mockito.doReturn(mock(Field.class)).when(injection).mockCandidateFilter_filterCandidate(Collections.singletonList(new Field()), target.getClass().getDeclaredField("field2"), target);
        List<Field> orderedInstanceFields = injection.orderedInstanceFieldsFrom(target.getClass());
        boolean result = injection.injectMockCandidatesOnFields(Collections.singletonList(mock(Field.class)), target, false, orderedInstanceFields);
        assertTrue(result);
        verify(target).setField1(Mockito.any());
        verifyNoMoreInteractions(target);
    }

    @Test
    public void testNoCandidatesFound() {
        Mockito.doReturn(Collections.emptyList()).when(injection).orderedInstanceFieldsFrom(target.getClass());
        assertFalse(injection.processInjection(target.getClass().getDeclaredField("dependentField"), target, Collections.singletonList(mock(Field.class))));
    }

    @Test
    public void testNoInjectionOccurred() {
        Mockito.doReturn(Collections.singletonList(new Field())).when(injection).orderedInstanceFieldsFrom(target.getClass());
        assertFalse(injection.processInjection(target.getClass().getDeclaredField("dependentField"), target, Collections.singletonList(mock(Field.class))));
    }

    @Test
    public void testNoInjectionOccurredAndNoCandidatesFound() {
        Mockito.doReturn(Collections.emptyList()).when(injection).orderedInstanceFieldsFrom(target.getClass());
        assertFalse(injection.processInjection(target.getClass().getDeclaredField("dependentField"), target, Collections.singletonList(mock(Field.class))));
    }
}