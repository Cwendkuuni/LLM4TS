package org.mockito.internal.configuration;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito. ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine annotationEngine;
 @Mock private Object testClass;
 @Spy private Object spiedInstance;

 @Before
 public void setup() {
 annotationEngine = new SpyAnnotationEngine();
 }

 @Test(expected = MockitoException.class)
 public void testSpyAnnotationOnNullInstance() {
 Field field = getField("spiedField", testClass);
 annotationEngine.process(SpyAnnotationEngine.class, testClass);
 field.set(testClass, null);
 annotationEngine.process(SpyAnnotationEngine.class, testClass);
 }

 @Test(expected = MockitoException.class)
 public void testSpyAnnotationOnMissingInstance() {
 Field field = getField("spiedField", testClass);
 annotationEngine.process(SpyAnnotationEngine.class, testClass);
 }

 @Test
 public void testSpyAnnotationOnExistingMock() {
 Field field = getField("spiedField", testClass);
 field.set(testClass, Mockito.mock(Object.class));
 annotationEngine.process(SpyAnnotationEngine.class, testClass);
 assertEquals(testClass, field.get(testClass));
 }

 @Test
 public void testSpyAnnotationOnExistingSpy() {
 Field field = getField("spiedField", testClass);
 field.set(testClass, spiedInstance);
 annotationEngine.process(SpyAnnotationEngine.class, testClass);
 assertEquals(spiedInstance, field.get(testClass));
 }

 @Test(expected = MockitoException.class)
 public void testSpyAnnotationWithMockAnnotation() {
 Field field = getField("spiedField", testClass);
 field.set(testClass, Mockito.mock(Object.class));
 annotationEngine.process(SpyAnnotationEngine.class, testClass);
 field.set(testClass, Mockito.mock(Object.class));
 }

 @Test(expected = MockitoException.class)
 public void testSpyAnnotationWithMockitoAnnotationsMockAnnotation() {
 Field field = getField("spiedField", testClass);
 field.set(testClass, Mockito.mock(Object.class));
 MockitoAnnotations.initMocks(testClass);
 annotationEngine.process(SpyAnnotationEngine.class, testClass);
 field.set(testClass, Mockito.mock(Object.class));
 }

 @Test(expected = MockitoException.class)
 public void testSpyAnnotationWithCaptorAnnotation() {
 Field field = getField("spiedField", testClass);
 field.set(testClass, Mockito.mock(Object.class));
 ArgumentCaptor<Object> captor = Mockito.captor(Object.class);
 annotationEngine.process(SpyAnnotationEngine.class, testClass);
 }

 @Test
 public void testSpyAnnotationWithNoUndesiredAnnotation() {
 Field field = getField("spiedField", testClass);
 annotationEngine.process(SpyAnnotationEngine.class, testClass);
 }

 @Test(expected = org.mockito.exceptions.base.MockitoException.class)
 public void testDuplicateSpyAnnotation() {
 Field field = getField("spiedField", testClass);
 field.set(testClass, spiedInstance);
 annotationEngine.process(SpyAnnotationEngine.class, testClass);
 field.set(testClass, spiedInstance);
 }

 private Field getField(String name, Object object) {
 try {
 Field field = object.getClass().getDeclaredField(name);
 field.setAccessible(true);
 return field;
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }
}