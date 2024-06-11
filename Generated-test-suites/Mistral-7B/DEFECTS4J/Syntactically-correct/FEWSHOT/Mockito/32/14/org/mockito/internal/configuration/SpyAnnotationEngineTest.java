package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.configuration.AnnotationEngine;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.reflect.ClassPath;
import org.mockito.reflect.FieldUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine annotationEngine;
    private Object testObject;

    @Before
    public void setup() {
        annotationEngine = new SpyAnnotationEngine();
        testObject = new Object();
    }

    @Test
    public void testCreateMockFor() {
        assertNotNull(annotationEngine);
        Annotation annotation = Mockito.mock(Annotation.class);
        Field field = Mockito.spy(Mockito.mock(Field.class));
        assertNull(annotationEngine.createMockFor(annotation, field));
    }

    @Test
    public void testProcess_NoSpyAnnotation() {
        Class<?> context = Mockito.mock(Class.class);
        annotationEngine.process(context, testObject);
        Field[] fields = context.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Spy.class)) {
                FieldUtils.makeAccessible(field);
                assertNull(field.get(testObject));
            }
        }
    }

    @Test(expected = MockitoException.class)
    public void testProcess_MissingInstance() {
        Class<?> context = Mockito.mock(Class.class);
        Field field = Mockito.spy(Mockito.mock(Field.class));
        field.set(testObject, null);
        annotationEngine.process(context, testObject);
    }

    @Test
    public void testProcess_AlreadySpyedInstance() {
        Class<? extends Annotation> mockAnnotation = Mockito.mock(Annotation.class);
        Field field = Mockito.spy(Mockito.mock(Field.class));
        Object instance = mock(Object.class);
        field.set(testObject, instance);
        FieldUtils.makeAccessible(field);
        annotationEngine.process(ClassPath.class, testObject);
        verifyNoMoreInteractions(instance);
    }

    @Test
    public void testProcess_NewlySpyedInstance() {
        Class<? extends Annotation> mockAnnotation = Mockito.mock(Annotation.class);
        Field field = Mockito.spy(Mockito.mock(Field.class));
        Object instance = mock(Object.class);
        field.set(testObject, instance);
        FieldUtils.makeAccessible(field);
        annotationEngine.process(ClassPath.class, testObject);
        Mockito.verify(instance).toString();
    }

    @Test
    public void testAssertNoAnnotations() {
        Field field = Mockito.spy(Mockito.mock(Field.class));
        Class<Annotation> mockAnnotation = Mockito.mock(Annotation.class);
        annotationEngine.assertNoAnnotations(mockAnnotation.getClass(), field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAssertNoAnnotations_AnnotationPresent() {
        Field field = Mockito.spy(Mockito.mock(Field.class));
        Class<Annotation> mockAnnotation = Mockito.mock(Annotation.class);
        FieldUtils.makeAccessible(field);
        field.setAnnotation(mockAnnotation);
        annotationEngine.assertNoAnnotations(mockAnnotation.getClass(), field, Mockito.MockitoAnnotations.Mock.class);
    }
}