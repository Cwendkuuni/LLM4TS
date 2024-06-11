package org.mockito.internal.configuration.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.configuration.AnnotationEngine;
import org.mockito.internal.configuration.MockitoAnnotation;
import org.mockito.internal.configuration.Spy;
import org.mockito.internal.configuration.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine engine;
 private Object testClass;

 @Before
 public void setup() {
 testClass = new Object() {};
 engine = new SpyAnnotationEngine();
 }

 @Test
 public void testCreateMockFor() {
 assertNull(engine.createMockFor(null, null));
 assertNull(engine.createMockFor(new MockitoAnnotation(), null));
 assertNull(engine.createMockFor(new MockitoAnnotation(), new Field(this.getClass(), "testField", Integer.TYPE)));
 assertNull(engine.createMockFor(new Spy(), new Field(this.getClass(), "testField", String.class)));
 }

 @Test
 public void testProcess_FieldIsNull() {
 engine.process(this.getClass(), testClass);
 assertFalse(testClass.getClass().getDeclaredFields()[0].isAnnotationPresent(Spy.class));
 }

 @Test
 public void testProcess_FieldIsAnnotatedWithSpy_InstanceIsNotNull() {
 @Spy Object mock = new Object() {};
 MockitoAnnotations.initMocks(testClass);
 engine.process(this.getClass(), testClass);
 ArgumentCaptor<Object> argumentCaptor = argumentCaptor(Object.class);
 verify(testClass, times(1)).setField(argumentCaptor.capture(any(Object.class)));
 Object capturedObject = argumentCaptor.getValue();
 assertEquals(mock, capturedObject);
 }

 @Test
 public void testProcess_FieldIsAnnotatedWithSpy_InstanceIsAlreadyAMock() {
 @Mock Object mock = mock(Object.class);
 MockitoAnnotations.initMocks(testClass);
 engine.process(this.getClass(), testClass);
 Mockito.reset(mock);
 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testProcess_FieldIsAnnotatedWithSpy_InstanceIsNull() {
 @Spy Object mock = null;
 assertThrows(MockitoException.class, () -> engine.process(this.getClass(), testClass));
 }

 @Test
 public void testProcess_FieldIsAnnotatedWithSpy_AccessFailed() {
 @Spy Object mock = new Object() {};
 testClass.getClass().getDeclaredField("testField").setAccessible(false);
 assertThrows(IllegalAccessException.class, () -> engine.process(this.getClass(), testClass));
 }

 @Test
 public void testProcess_FieldIsAnnotatedWithMultipleAnnotations() {
 @Spy @Mock Object mock = new Object() {};
 engine.process(this.getClass(), testClass);
 assertFalse(testClass.getClass().getDeclaredFields()[0].isAnnotationPresent(Captor.class));
 }

 @Test
 public void testProcess_FieldIsAnnotatedWithSpy_SameFieldAnnotatedWithDifferentAnnotations() {
 @Spy Object mock1 = new Object() {};
 @Spy Object mock2 = new Object() {};
 Field field1 = this.getClass().getDeclaredField("testField1");
 Field field2 = this.getClass().getDeclaredField("testField2");
 MockitoAnnotations.initMocks(testClass);
 engine.process(this.getClass(), testClass);
 ArgumentCaptor<Object> argumentCaptor1 = argumentCaptor(Object.class);
 ArgumentCaptor<Object> argumentCaptor2 = argumentCaptor(Object.class);
 verify(testClass, times(1)).setField1(argumentCaptor1.capture(any(Object.class)));
 Object capturedObject1 = argumentCaptor1.getValue();
 assertEquals(mock1, capturedObject1);
 verify(testClass, times(1)).setField2(argumentCaptor2.capture(any(Object.class)));
 Object capturedObject2 = argumentCaptor2.getValue();
 assertEquals(mock2, capturedObject2);
 }

 @Test
 public void testAssertNoAnnotations() {
 engine.assertNoAnnotations(Spy.class, new Field(this.getClass(), "testField", String.class), MockitoAnnotation.class, MockitoAnnotations.Mock.class, Captor.class);
 }

 @Test
 public void testAssertNoAnnotations_AnnotationPresent() {
 engine.assertNoAnnotations(Spy.class, new Field(this.getClass(), "testField", String.class), MockitoAnnotation.class);
 }
}