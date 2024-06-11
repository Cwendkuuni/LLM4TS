package org.mockito.internal.configuration.injection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injection;
    private Object testObject;

    @Before
    public void setUp() {
        injection = new PropertyAndSetterInjection();
        testObject = new DummyClass();
    }

    @Test
    public void testProcessInjectionWithNoAnnotatedFields() {
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(SomeClass.class));

        Assert.assertFalse(injection.processInjection(null, testObject, mocks));
    }

    @Test
    public void testProcessInjectionWithOnlyFinalFields() {
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(SomeClass.class));

        Field finalField = new DummyClass().getClass().getDeclaredField("finalField");
        finalField.setAccessible(true);
        finalField.setFinal(true);

        Assert.assertFalse(injection.processInjection(finalField, testObject, mocks));
    }

    @Test
    public void testProcessInjectionWithNoMockCandidates() {
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(SomeClass.class));

        Field field = new DummyClass().getClass().getDeclaredField("noMockCandidateField");

        FieldInitializationReport report = injection.initializeInjectMocksField(field, testObject);

        Assert.assertNull(report);
        verifyNoMoreInteractions(mocks);
    }

    @Test
    public void testProcessInjectionWithOneMockCandidate() {
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(SomeClass.class));

        Field field = new DummyClass().getClass().getDeclaredField("mockCandidateField");

        FieldInitializationReport report = injection.initializeInjectMocksField(field, testObject);

        Assert.assertNotNull(report);
        verify(mocks).then(invocation -> {
            Object mock = invocation.getMock();
            injection.mockCandidateFilter.filterCandidate(mocks, field, testObject).thenInject();
            return mock;
        });
        verifyNoMoreInteractions(mocks);
    }

    @Test
    public void testProcessInjectionWithMultipleMockCandidates() {
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(SomeClass.class));
        mocks.add(mock(AnotherClass.class));

        Field field = new DummyClass().getClass().getDeclaredField("multipleMockCandidatesField");

        FieldInitializationReport report = injection.initializeInjectMocksField(field, testObject);

        Assert.assertNotNull(report);
        verify(mocks).thenAnswer(invocation -> {
            Object mock = invocation.getMock();
            return injection.mockCandidateFilter.findMockCandidateByName(mocks, field, testObject);
        });
        verifyNoMoreInteractions(mocks);
    }

    @Test
    public void testProcessInjectionWithNoMockCandidatesAndNoConstructor() {
        Set<Object> mocks = new HashSet<>();

        Field field = new DummyClass().getClass().getDeclaredField("noMockCandidateOrConstructorField");

        FieldInitializationReport report = injection.initializeInjectMocksField(field, testObject);

        Assert.assertNull(report);
        verifyNoMoreInteractions(mocks);
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjectionWithException() {
        Set<Object> mocks = new HashSet<>();

        Field field = new DummyClass().getClass().getDeclaredField("exceptionField");

        FieldInitializationReport report = injection.initializeInjectMocksField(field, testObject);

        Assert.assertNull(report);
        verify(mocks).thenAnswer(invocation -> {
            throw new InvocationTargetException(new RuntimeException("Test exception"));
        });
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjectionWithExceptionInConstructor() {
        Set<Object> mocks = new HashSet<>();

        Class<?> exceptionClass = new DummyExceptionClass();

        Field field = new DummyClass().getClass().getDeclaredField("exceptionInConstructorField");

        try {
            injection.initializeInjectMocksField(field, testObject);
        } catch (MockitoException e) {
            Assert.assertEquals(exceptionClass, e.getCause().getCause().getClass());
            throw e;
        }
    }

    private static class DummyClass {
        @InjectMocks
        private SomeField someField;
        private final SomeField finalField = new SomeField();
        private SomeField noMockCandidateField;
        private SomeField multipleMockCandidatesField;
        private SomeField noMockCandidateOrConstructorField;
        private SomeField exceptionField;
        private SomeField exceptionInConstructorField;

        @InjectMocks
        private static class NestedStaticClass {
            @InjectMocks
            private SomeField nestedStaticSomeField;
        }

        static class NestedClass {
            @InjectMocks
            private SomeField nestedClassSomeField;
        }
    }

    private static class DummyExceptionClass {
    }

    private static class SomeField {
    }

    private static class SomeClass {
    }

    private static class AnotherClass {
    }
}