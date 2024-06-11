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
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.runners.MockitoJUnitRunnerDelegate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Unit tests for {@link PropertyAndSetterInjection} class.
 */
public class PropertyAndSetterInjectionTest {

    private static final Class<?> TESTED_CLASS = PropertyAndSetterInjection.class;
    private static final MockitoJUnitRunner DELEGATE = new MockitoJUnitRunner();
    private MockitoJUnitRunnerDelegate delegate;

    @Before
    public void init() {
        delegate = new MockitoJUnitRunnerDelegate(DELEGATE);
    }

    @Test
    public void testProcessInjection_whenFieldAnnotatedWithInjectMocksAnnotatedAndTypeMatches_thenMockInjected() {
        // given
        Object owner = mock(Object.class);
        Class<?> fieldType = String.class;
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(fieldType));
        Field field = getPrivateField(owner, "injectMocksField");
        field.setAccessible(true);
        field.setAnnotation(MockitointernalConfigurationInjectionInjectMocks.class);

        // when
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        boolean result = injection.processInjection(field, owner, mockCandidates);

        // then
        assertNotNull(result);
        assertFalse(mockCandidates.isEmpty());
        assertNull(field.get(owner)); // field should be injected
    }

    @Test
    public void testProcessInjection_whenFieldAnnotatedWithInjectMocksAnnotatedButNoMatchingType_thenNoMockInjected() {
        // given
        Object owner = mock(Object.class);
        Field field = getPrivateField(owner, "injectMocksField");
        field.setAccessible(true);
        field.setAnnotation(MockitointernalConfigurationInjectionInjectMocks.class);

        // when
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(Integer.class));
        boolean result = injection.processInjection(field, owner, mockCandidates);

        // then
        assertNotNull(result);
        assertTrue(mockCandidates.isEmpty());
        assertNull(field.get(owner)); // field should not be injected
    }

    @Test
    public void testProcessInjection_whenFieldAnnotatedWithInjectMocksAnnotatedAndNoMatchingTypeButNameMatches_thenMockInjected() {
        // given
        Object owner = mock(Object.class);
        Class<?> fieldType = String.class;
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(Integer.class), mock(fieldType));
        Field field = getPrivateField(owner, "injectMocksField");
        field.setAccessible(true);
        field.setAnnotation(MockitointernalConfigurationInjectionInjectMocks.class);

        // when
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        boolean result = injection.processInjection(field, owner, mockCandidates);

        // then
        assertNotNull(result);
        assertFalse(mockCandidates.isEmpty());
        assertNull(field.get(owner)); // field should be injected
    }

    @Test
    public void testProcessInjection_whenFieldAnnotatedWithInjectMocksAnnotatedAndNoMatchingTypeOrName_thenNoMockInjected() {
        // given
        Object owner = mock(Object.class);
        Field field = getPrivateField(owner, "injectMocksField");
        field.setAccessible(true);
        field.setAnnotation(MockitointernalConfigurationInjectionInjectMocks.class);

        // when
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(Integer.class), mock(String.class));
        boolean result = injection.processInjection(field, owner, mockCandidates);

        // then
        assertNotNull(result);
        assertTrue(mockCandidates.isEmpty());
        assertNull(field.get(owner)); // field should not be injected
    }

    // Add more test cases here to cover all other scenarios

    private Field getPrivateField(Object object, String name) {
        try {
            return object.getClass().getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}