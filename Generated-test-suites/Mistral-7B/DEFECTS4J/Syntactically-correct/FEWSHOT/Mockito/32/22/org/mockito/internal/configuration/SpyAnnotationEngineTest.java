package org.mockito.internal.configuration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.initMocks;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.AnnotationEngine;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);
        assertNull(spyAnnotationEngine.createMockFor(annotation, field));
    }

    @Test
    public void testProcess_noSpyAnnotation() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field[] fields = new Field[0];

        spyAnnotationEngine.process(context, testClass);

        verifyNoInteractions(context, testClass, fields);
    }

    @Test
    public void testProcess_multipleFieldsWithSpyAnnotation() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);
        Field[] fields = new Field[] { field1, field2 };

        when(field1.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field1.getName()).thenReturn("field1");
        when(field2.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field2.getName()).thenReturn("field2");

        spyAnnotationEngine.process(context, testClass);

        verify(context).getDeclaredFields();
        verify(field1).setAccessible(true);
        verify(field1).isAccessible();
        verify(field1).setAccessible(true);
        verify(field2).setAccessible(true);
        verify(field2).isAccessible();
        verify(field1, times(2)).setAccessible(true);
        verify(field2, times(2)).setAccessible(true);

        Object instance1 = mock(Object.class);
        Object instance2 = mock(Object.class);

        when(field1.get(testClass)).thenReturn(instance1);
        when(field2.get(testClass)).thenReturn(instance2);

        verify(instance1, never()).reset();
        verifyZeroInteractions(Mockito.spy(instance1.getClass()));
        verify(instance2, never()).reset();
        verifyZeroInteractions(Mockito.spy(instance2.getClass()));

        assertTrue(spyAnnotationEngine.process(context, testClass));

        verify(field1).set(testClass, Mockito.spy(instance1));
        verify(field2).set(testClass, Mockito.spy(instance2));
    }

    @Test
    public void testProcess_fieldWithMockAnnotation() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field field = mock(Field.class);

        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAnnotationPresent(Mock.class)).thenReturn(true);

        spyAnnotationEngine.process(context, testClass);

        new Reporter().unsupportedCombinationOfAnnotations("Spy", "Mock");
    }

    @Test
    public void testProcess_fieldWithCaptorAnnotation() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field field = mock(Field.class);

        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAnnotationPresent(Captor.class)).thenReturn(true);

        spyAnnotationEngine.process(context, testClass);

        new Reporter().unsupportedCombinationOfAnnotations("Spy", "Captor");
    }

    @Test(expected = MockitoException.class)
    public void testProcess_instanceIsMissing() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field field = mock(Field.class);

        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.getName()).thenReturn("field");
        when(field.get(testClass)).thenReturn(null);

        spyAnnotationEngine.process(context, testClass);
    }

    @Test
    public void testProcess_instanceIsAMock() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field field = mock(Field.class);

        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.getName()).thenReturn("field");
        when(field.get(testClass)).thenReturn(mock(Object.class));

        spyAnnotationEngine.process(context, testClass);

        Mockito.reset(mock(Object.class).getClass());
        verify(field).set(testClass, Mockito.spy(Mockito.reset(mock(Object.class))));
    }

    @Test
    public void testAssertNoAnnotations() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class annotation = mock(Annotation.class);
        Field field = mock(Field.class);

        spyAnnotationEngine.assertNoAnnotations(annotation, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);

        verifyNoInteractions(annotation, field);
    }

    @Test
    public void testAssertNoAnnotations_duplicateAnnotation() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class annotation = mock(Annotation.class);
        Field field = mock(Field.class);

        when(field.isAnnotationPresent(annotation)).thenReturn(true);

        try {
            spyAnnotationEngine.assertNoAnnotations(annotation, field, annotation, Mock.class, Captor.class);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    private void verifyNoInteractions(Object... objects) {
        for (Object object : objects) {
            verifyZeroInteractions(object);
        }
    }

    private void verifyZeroInteractions(Object object) {
        verifyZeroInteractions(object.getClass());
    }

    private void verifyZeroInteractions(Class clazz) {
        verifyNoInteractions(clazz.getDeclaredMethods());
    }

    private void verifyInteractions(Object object, int times) {
        verify(object, times(times));
    }

    private void verifyInteractions(Object object, int times, Class clazz) {
        verify(object, times(times), clazz);
    }
}