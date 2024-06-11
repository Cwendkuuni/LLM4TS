package org.mockito.internal.configuration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.Spy;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

 private static final Class<SpyAnnotationEngine> CLASS_UNDER_TEST = SpyAnnotationEngine.class;
 private static final String FIELD_NAME = "fieldToBeSpyed";
 private static final Class<Spy> SPY_ANNOTATION = Spy.class;

 @SuppressWarnings("unchecked")
 @Test
 public void testProcess_whenFieldAnnotatedWithSpyAnnotation_thenInstanceIsSpyed() throws Exception {
 SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
 Class<?> testClass = MockitoJUnitRunner.class;
 Object testInstance = new Object();

 // Set up the test class with a field annotated with @Spy
 Field field = testClass.getDeclaredField(FIELD_NAME);
 field.setAccessible(true);
 field.set(testInstance, testInstance);
 field.setAnnotation(new SpyAnnotation()); // Mockito does not support creating SpyAnnotation instance, so we mock it

 // Initialize mocks
 MockitoAnnotations.initMocks(testInstance);

 // Spy engine processing
 annotationEngine.process(testClass, testInstance);

 // Check that the field has been spied
 verify(testInstance, times(1)).getClass(); // Check that testInstance is not a mock
 assertNotNull(field.get(testInstance)); // Check that the field instance is not null
 assertTrue(isSpy(field.get(testInstance))); // Check that the field instance is a spy
 }

 @SuppressWarnings("unchecked")
 @Test(expected = MockitoException.class)
 public void testProcess_whenFieldAnnotatedWithSpyAnnotation_andInstanceIsMissing_thenThrowsException() throws Exception {
 SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
 Class<?> testClass = MockitoJUnitRunner.class;
 Field field = testClass.getDeclaredField(FIELD_NAME);
 field.setAccessible(true);

 // Spy engine processing
 annotationEngine.process(testClass, null);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testProcess_whenFieldAnnotatedWithSpyAnnotation_andInstanceIsAlreadyMocked_thenInstanceIsReset() throws Exception {
 SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
 Class<?> testClass = MockitoJUnitRunner.class;
 Object testInstance = mock(Object.class);

 // Set up the test class with a field annotated with @Spy
 Field field = testClass.getDeclaredField(FIELD_NAME);
 field.setAccessible(true);
 field.set(testInstance, testInstance);
 field.setAnnotation(new SpyAnnotation());

 // Initialize mocks and mock the instance
 MockitoAnnotations.initMocks(testInstance);
 when(testInstance.getClass()).thenReturn(testClass);

 // Spy engine processing
 annotationEngine.process(testClass, testInstance);

 // Check that the mock has been reset
 verify(testInstance).getClass();
 verifyNoMoreInteractions(testInstance);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = IllegalArgumentException.class)
 public void testProcess_whenFieldAnnotatedWithMultipleAnnotations_thenThrowsException() throws Exception {
 SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
 Class<?> testClass = MockitoJUnitRunner.class;
 Object testInstance = new Object();

 // Set up the test class with a field annotated with @Spy and @Mock annotations
 Field field = testClass.getDeclaredField(FIELD_NAME);
 field.setAccessible(true);
 field.set(testInstance, testInstance);
 field.setAnnotation(new SpyAnnotation());
 field.setAnnotation(Mockito.mock(Annotation.class));

 // Spy engine processing
 annotationEngine.process(testClass, testInstance);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = IllegalArgumentException.class)
 public void testProcess_whenFieldAnnotatedWithMultipleCaptureAnnotations_thenThrowsException() throws Exception {
 SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
 Class<?> testClass = MockitoJUnitRunner.class;
 Object testInstance = new Object();

 // Set up the test class with a field annotated with multiple @Captor annotations
 Field field = testClass.getDeclaredField(FIELD_NAME);
 field.setAccessible(true);
 field.set(testInstance, testInstance);
 field.setAnnotation(new CaptorAnnotation());
 field.setAnnotation(new CaptorAnnotation());

 // Spy engine processing
 annotationEngine.process(testClass, testInstance);
 }

 private static boolean isSpy(Object instance) {
 return instance instanceof org.mockito.Spy;
}

 private static class SpyAnnotation implements Annotation {}
 private static class CaptorAnnotation implements Annotation {}
}