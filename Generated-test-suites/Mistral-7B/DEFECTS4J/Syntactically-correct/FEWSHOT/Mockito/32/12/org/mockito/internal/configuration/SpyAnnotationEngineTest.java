package org.mockito.internal.configuration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.exceptions.BaseMockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

/**
 * Test cases for {@link SpyAnnotationEngine} class.
 */
public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);

        assertNull(annotationEngine.createMockFor(annotation, field));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateMockForThrowsUOE() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();

        annotationEngine.createMockFor(mock(Annotation.class), mock(Field.class));
    }

    @Test
    public void testProcessNullContext() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Object testClass = mock(Object.class);

        annotationEngine.process(null, testClass);

        verifyZeroInteractions(testClass);
    }

    @Test
    public void testProcessNoSpyAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field field = mock(Field.class);

        annotationEngine.process(context, testClass);

        verifyZeroInteractions(field);
    }

    @Test
    public void testProcessSpyAnnotationWithMockInstance() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class context = mock(Class.class);
        Object testClass = mock(Object.class);
        Object instance = mock(Object.class);
        Field field = mock(Field.class);

        when(field.getName()).thenReturn("testField");
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.get(testClass)).thenReturn(instance);
        when(new MockUtil().isMock(instance)).thenReturn(true);

        annotationEngine.process(context, testClass);

        verify(instance).getClass();
        verify(instance).getClass(); // verify that getClass() is called twice
        verify(testClass).getClass();

        verifyZeroInteractions(field);
    }

    @Test
    public void testProcessSpyAnnotationWithNonMockInstance() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class context = mock(Class.class);
        Object testClass = mock(Object.class);
        Object instance = mock(Object.class);
        Field field = mock(Field.class);

        when(field.getName()).thenReturn("testField");
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.get(testClass)).thenReturn(instance);
        when(new MockUtil().isMock(instance)).thenReturn(false);

        annotationEngine.process(context, testClass);

        verify(instance).getClass();
        verify(instance).getClass(); // verify that getClass() is called twice
        verify(testClass).getClass();
        verify(field).set(testClass, argThat(isInstanceOf(Mockito.spy(instance).getClass())));
    }

    @Test(expected = MockitoException.class)
    public void testProcessWithMissingInstance() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field field = mock(Field.class);

        when(field.getName()).thenReturn("testField");
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);

        annotationEngine.process(context, testClass);

        verify(testClass).getClass();
        verify(testClass).getClass(); // verify that getClass() is called twice

        throw new MockitoException("Cannot create a @Spy for 'testField' field because the *instance* is missing\n"
                + "The instance must be created *before* initMocks();\n"
                + "Example of correct usage of @Spy:\n"
                + "   @Spy List mock = new LinkedList();\n"
                + "   //also, don't forget about MockitoAnnotations.initMocks();");
    }

    @Test
    public void testAssertNoAnnotationsWithNoAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Field field = mock(Field.class);

        annotationEngine.assertNoAnnotations(null, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);

        verifyZeroInteractions(field);
    }

    @Test
    public void testAssertNoAnnotationsWithSpyAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Field field = mock(Field.class);
        Field spyAnnotation = mock(Annotation.class);

        when(field.isAnnotationPresent(spyAnnotation.getClass())).thenReturn(true);

        annotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);

        verify(field).getClass();
        verify(spyAnnotation).getClass();
        verify(spyAnnotation).getClass(); // verify that getClass() is called twice
        verify(field).getClass();

        verify(new Reporter()).unsupportedCombinationOfAnnotations(eq("Spy"), eq(spyAnnotation.getClass()));
    }

    @Test
    public void testAssertNoAnnotationsWithMockAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Field field = mock(Field.class);
        Field mockAnnotation = mock(Annotation.class);

        when(field.isAnnotationPresent(mockAnnotation.getClass())).thenReturn(true);

        annotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);

        verify(field).getClass();
        verify(mockAnnotation).getClass();
        verify(mockAnnotation).getClass(); // verify that getClass() is called twice
        verify(field).getClass();

        verifyZeroInteractions(new Reporter());
    }

    @Test
    public void testAssertNoAnnotationsWithCaptorAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Field field = mock(Field.class);
        Field captorAnnotation = mock(Annotation.class);

        when(field.isAnnotationPresent(captorAnnotation.getClass())).thenReturn(true);

        annotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);

        verify(field).getClass();
        verify(captorAnnotation).getClass();
        verify(captorAnnotation).getClass(); // verify that getClass() is called twice
        verify(field).getClass();

        verifyZeroInteractions(new Reporter());
    }

    @Test
    public void testAssertNoAnnotationsWithMultipleAnnotations() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Field field = mock(Field.class);
        Field spyAnnotation = mock(Annotation.class);
        Field mockAnnotation = mock(Annotation.class);

        when(field.isAnnotationPresent(spyAnnotation.getClass())).thenReturn(true);
        when(field.isAnnotationPresent(mockAnnotation.getClass())).thenReturn(true);

        annotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);

        verify(field).getClass();
        verify(spyAnnotation).getClass();
        verify(spyAnnotation).getClass(); // verify that getClass() is called twice
        verify(field).getClass();
        verify(mockAnnotation).getClass();
        verify(mockAnnotation).getClass(); // verify that getClass() is called twice
        verify(field).getClass();

        verify(new Reporter()).unsupportedCombinationOfAnnotations(eq("Spy"), eq(spyAnnotation.getClass()));
    }

    @Test
    public void testProcessWithException() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field field = mock(Field.class);
        IllegalAccessException illegalAccessException = mock(IllegalAccessException.class);

        when(field.getName()).thenReturn("testField");
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.get(testClass)).thenThrow(illegalAccessException);

        try {
            annotationEngine.process(context, testClass);
        } catch (BaseMockitoException e) {
            verify(testClass).getClass();
            verify(testClass).getClass(); // verify that getClass() is called twice
            verify(field).setAccessible(true);
            verify(field).setAccessible(true); // verify that setAccessible() is called twice
            verify(field).getClass();
            verify(illegalAccessException).getMessage();
            verify(illegalAccessException).getCause();
            verifyNoMoreInteractions(testClass, field);
        }
    }
}