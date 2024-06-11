package org.mockito.internal.configuration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.AnnotationEngine;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.junit.Runners;
import org.mockito.util.configuration.plugins.Plugins;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Plugins(SpyAnnotationEngine.class)
@Runners(Runners.Version.JUnit_4_12)
public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;

    @Before
    public void setUp() {
        spyAnnotationEngine = new SpyAnnotationEngine();
    }

    @Test
    public void testCreateMockForReturnsNull() {
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);

        assertNull(spyAnnotationEngine.createMockFor(annotation, field));
    }

    @Test
    public void testProcess_NoSpyAnnotation_DoesNothing() throws IllegalAccessException {
        Class<?> context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field field = mock(Field.class);

        field.setAccessible(true);
        Object instance = mock(Object.class);
        field.set(testClass, instance);

        spyAnnotationEngine.process(context, testClass);

        verifyZeroInteractions(instance);
    }

    @Test
    public void testProcess_SpyAnnotationPresent_SpyExistingInstance() throws IllegalAccessException {
        Class<?> context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field field = mock(Field.class);
        Object instance = mock(Object.class);
        field.setAccessible(true);
        field.set(testClass, instance);

        when(instance.getClass()).thenReturn(String.class);
        when(new MockUtil().isMock(instance)).thenReturn(true);

        spyAnnotationEngine.process(context, testClass);

        verify(instance).reset();
    }

    @Test
    public void testProcess_SpyAnnotationPresent_SpyNullInstance() throws IllegalAccessException {
        Class<?> context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field field = mock(Field.class);

        field.setAccessible(true);
        field.set(testClass, null);

        assertThrows(MockitoException.class, () -> spyAnnotationEngine.process(context, testClass));
    }

    @Test
    public void testProcess_SpyAnnotationPresent_SpyNewInstance() throws IllegalAccessException {
        Class<?> context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field field = mock(Field.class);
        Object instance = mock(Object.class);
        field.setAccessible(true);
        field.set(testClass, null);

        when(field.get(testClass)).thenReturn(null);

        Object spyInstance = spy(instance);
        field.set(testClass, spyInstance);

        spyAnnotationEngine.process(context, testClass);

        verify(instance).reset();
        verify(spyInstance).reset();
        verify(spyInstance).answerQuestionsAboutItself(eq(testClass), any());
    }

    @Test
    public void testProcess_SpyAnnotationPresent_SpyMultipleInstances() throws IllegalAccessException {
        Class<?> context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);
        Object instance1 = mock(Object.class);
        Object instance2 = mock(Object.class);
        field1.setAccessible(true);
        field2.setAccessible(true);
        field1.set(testClass, instance1);
        field2.set(testClass, instance2);

        when(instance1.getClass()).thenReturn(String.class);
        when(new MockUtil().isMock(instance1)).thenReturn(false);

        when(instance2.getClass()).thenReturn(Integer.class);
        when(new MockUtil().isMock(instance2)).thenReturn(false);

        spyAnnotationEngine.process(context, testClass);

        verify(instance1).reset();
        verify(instance2).reset();
    }

    @Test
    public void testAssertNoAnnotations() {
        Field field = mock(Field.class);
        Class<?> annotation = mock(Annotation.class);
        Class<?> undesiredAnnotation1 = mock(Annotation.class);
        Class<?> undesiredAnnotation2 = mock(Annotation.class);

        spyAnnotationEngine.assertNoAnnotations(annotation, field, undesiredAnnotation1, undesiredAnnotation2);

        verifyZeroInteractions(new Reporter());
    }

    @Test
    public void testAssertNoAnnotations_AnnotationPresent() {
        Field field = mock(Field.class);
        Class<?> annotation = mock(Annotation.class);
        Class<?> undesiredAnnotation = mock(Annotation.class);

        when(field.isAnnotationPresent(undesiredAnnotation)).thenReturn(true);

        assertThrows(org.mockito.exceptions.base.MockitoException.class, () -> spyAnnotationEngine.assertNoAnnotations(annotation, field, undesiredAnnotation));
    }
}