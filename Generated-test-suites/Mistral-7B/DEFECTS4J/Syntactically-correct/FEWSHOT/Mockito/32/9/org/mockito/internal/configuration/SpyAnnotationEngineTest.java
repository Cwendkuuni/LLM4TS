package org.mockito.internal.configuration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.FieldAccessibleHelper;

public class SpyAnnotationEngineTest {

    private static final Class<SpyAnnotationEngine> TEST_CLASS = SpyAnnotationEngine.class;

    @Mock
    private AnnotationEngine annotationEngine;
    @Mock
    private Field testField;
    @Mock
    private Object testInstance;

    @Spy
    private FieldAccessibleHelper fieldAccessibleHelper;

    private SpyAnnotationEngine systemUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        systemUnderTest = new SpyAnnotationEngine() {
            @Override
            public Object createMockFor(Annotation annotation, Field field) {
                return annotationEngine.createMockFor(annotation, field);
            }

            @SuppressWarnings("deprecation")
            @Override
            public void process(Class<?> context, Object testClass) {
                fieldAccessibleHelper.makeAccessible(testField);
                super.process(context, testClass);
            }

            @Override
            void assertNoAnnotations(Class annotation, Field field, Class... undesiredAnnotations) {
                annotationEngine.assertNoAnnotations(annotation, field, undesiredAnnotations);
            }
        };
    }

    @Test
    public void testCreateMockFor() {
        assertNull(systemUnderTest.createMockFor(null, null));
        assertNull(systemUnderTest.createMockFor(Mockito.mock(Annotation.class), null));
    }

    @Test
    public void testProcess_noAnnotationPresent() {
        Mockito.when(testField.isAnnotationPresent(Spy.class)).thenReturn(false);

        systemUnderTest.process(TEST_CLASS, this);
        verifyZeroInteractions(testField, annotationEngine);
    }

    @Test
    public void testProcess_annotationPresent() {
        Mockito.when(testField.isAnnotationPresent(Spy.class)).thenReturn(true);

        Mockito.when(testField.get(this)).thenReturn(testInstance);

        systemUnderTest.process(TEST_CLASS, this);

        Mockito.verify(annotationEngine).createMockFor(Spy.class, testField);
        Mockito.verify(testField).set(this, Mockito.argThat(instance -> instance == Mockito.spy(testInstance)));
        Mockito.reset(testField);

        Mockito.verify(testField).setAccessible(true);
        Mockito.verify(testField).setAccessible(true);
    }

    @Test
    public void testProcess_instanceIsAlreadyAMock() {
        Mockito.when(testField.isAnnotationPresent(Spy.class)).thenReturn(true);
        Mockito.when(testField.get(this)).thenReturn(Mockito.mock(Object.class));

        systemUnderTest.process(TEST_CLASS, this);

        Mockito.verify(testField).set(this, Mockito.any());
        Mockito.verify(testField).setAccessible(true);
    }

    @Test
    public void testProcess_instanceIsNull() {
        Mockito.when(testField.isAnnotationPresent(Spy.class)).thenReturn(true);
        Mockito.when(testField.get(this)).thenReturn(null);

        assertThrows(MockitoException.class, () -> systemUnderTest.process(TEST_CLASS, this));

        Mockito.verify(testField).setAccessible(true);
    }

    @Test
    public void testAssertNoAnnotations() {
        Mockito.when(testField.isAnnotationPresent(Spy.class)).thenReturn(true);
        Mockito.when(testField.isAnnotationPresent(Mockito.Mock.class)).thenReturn(true);

        systemUnderTest.assertNoAnnotations(Spy.class, testField, Mockito.Mock.class);

        Mockito.verifyNoInteractions(new Reporter());
    }

    @Test
    public void testAssertNoAnnotations_annotationPresent() {
        Mockito.when(testField.isAnnotationPresent(Spy.class)).thenReturn(true);
        Mockito.when(testField.isAnnotationPresent(Mockito.Mock.class)).thenReturn(true);

        assertThrows(MockitoException.class, () -> systemUnderTest.assertNoAnnotations(Spy.class, testField, Mockito.Mock.class));

        Mockito.verify(new Reporter()).unsupportedCombinationOfAnnotations("Spy", "Mock");
    }
}