package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Annotations;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class SpyAnnotationEngineTest {

 @Rule
 public MockitoRule mockitoRule = MockitoJUnit.rule();

 @Mock
 private Object testClass;

 @Spy
 private Object spyInstance;

 @Before
 public void setUp() throws Exception {
 Annotations.initMocks(this);
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockForNullAnnotation() {
 new SpyAnnotationEngine().createMockFor(null, null);
 }

 @Test
 public void testCreateMockForNoAnnotation() {
 new SpyAnnotationEngine().createMockFor(new Object() {}.getClass(), null);
 assertNull(spyInstance);
 }

 @Test
 public void testProcessNoSpyAnnotation() {
 new SpyAnnotationEngine().process(SpyAnnotationEngine.class, testClass);
 }

 @Test
 public void testProcessNonAccessibleField() throws Exception {
 Field nonAccessibleField = SpyAnnotationEngine.class.getDeclaredField("privateField");
 nonAccessibleField.setAccessible(true);
 Object nonAccessibleInstance = mock(Object.class);
 nonAccessibleField.set(testClass, nonAccessibleInstance);
 new SpyAnnotationEngine().process(SpyAnnotationEngine.class, testClass);
 assertFalse(nonAccessibleField.isAccessible());
 }

 @Test
 public void testProcessSpyAnnotation() throws Exception {
 Object originalInstance = new Object();
 Field field = SpyAnnotationEngine.class.getDeclaredField("fieldToSpy");
 field.setAccessible(true);
 field.set(testClass, originalInstance);
 new SpyAnnotationEngine().process(SpyAnnotationEngine.class, testClass);
 assertNotNull(spyInstance);
 assertNotNull(field.get(testClass));
 assertSame(originalInstance, field.get(testClass));
 assertNotNull(Mockito.mockingDetails(field.get(testClass)));
 }

 @Test(expected = MockitoException.class)
 public void testProcessSpyAnnotationTwice() throws Exception {
 Object originalInstance = new Object();
 Field field = SpyAnnotationEngine.class.getDeclaredField("fieldToSpy");
 field.setAccessible(true);
 field.set(testClass, originalInstance);
 new SpyAnnotationEngine().process(SpyAnnotationEngine.class, testClass);
 new SpyAnnotationEngine().process(SpyAnnotationEngine.class, testClass);
 }

 @Test
 public void testProcessWithCaptorAnnotation() throws Exception {
 ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
 Field captorField = SpyAnnotationEngine.class.getDeclaredField("captorField");
 captorField.setAccessible(true);
 captorField.set(testClass, mock(ArgumentCaptor.class));
 new SpyAnnotationEngine().process(SpyAnnotationEngine.class, testClass);
 assertNull(captorField.get(testClass));
 }

 @Test(expected = MockitoException.class)
 public void testProcessWithMockAnnotation() throws Exception {
 Field mockField = SpyAnnotationEngine.class.getDeclaredField("mockField");
 mockField.setAccessible(true);
 mockField.set(testClass, mock(Object.class));
 new SpyAnnotationEngine().process(SpyAnnotationEngine.class, testClass);
 }

 @Test(expected = MockitoException.class)
 public void testProcessWithBothMockAndSpyAnnotation() throws Exception {
 Field bothAnnotationField = SpyAnnotationEngine.class.getDeclaredField("bothAnnotationField");
 bothAnnotationField.setAccessible(true);
 bothAnnotationField.set(testClass, new Object() {
 @Mock
 @Spy
 public Object both() {}
 });
 new SpyAnnotationEngine().process(SpyAnnotationEngine.class, testClass);
 }

 @Test
 public void testProcessWithMultipleSpyAnnotations() throws Exception {
 Field field1 = SpyAnnotationEngine.class.getDeclaredField("field1ToSpy");
 Field field2 = SpyAnnotationEngine.class.getDeclaredField("field2ToSpy");
 field1.setAccessible(true);
 field2.setAccessible(true);
 field1.set(testClass, new Object());
 field2.set(testClass, new Object());
 new SpyAnnotationEngine().process(SpyAnnotationEngine.class, testClass);
 assertNotNull(field1.get(testClass));
 assertNotNull(field2.get(testClass));
 assertNotNull(Mockito.mockingDetails(field1.get(testClass)));
 assertNotNull(Mockito.mockingDetails(field2.get(testClass)));
 }
}