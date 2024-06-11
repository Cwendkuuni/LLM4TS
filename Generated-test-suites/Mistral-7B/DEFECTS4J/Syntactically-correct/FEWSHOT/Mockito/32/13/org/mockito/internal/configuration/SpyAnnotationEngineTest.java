package org.mockito.internal.configuration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;

public class SpyAnnotationEngineTest {

 AnnotationEngine engine = new SpyAnnotationEngine();
 Class testClass = mock(Class.class);

 @Test
 public void testCreateMockFor() {
 assertNull(engine.createMockFor(mock(Annotation.class), mock(Field.class)));
 }

 @Test(expected = MockitoException.class)
 public void testProcessNullContext() {
 engine.process(null, testClass);
 }

 @Test
 public void testProcessNoSpyAnnotation() {
 engine.process(testClass, testClass);
 verifyNoInteractions(testClass);
 }

 @Test
 public void testProcessSpyAnnotationWithNullField() {
 engine.process(testClass, testClass);
 Field nullField = mock(Field.class);
 try {
 engine.process(testClass, testClass);
 engine.process(testClass, testClass, nullField);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testProcessSpyAnnotationWithFieldWithoutInstance() {
 Field field = mock(Field.class);
 doReturn(null).when(field).get(any(Object.class));
 engine.process(testClass, testClass, field);
 verifyZeroInteractions(field);
 }

 @Test
 public void testProcessSpyAnnotationWithFieldThatIsAlreadyAMock() {
 Object instance = mock(Object.class);
 Field field = mock(Field.class);
 doReturn(instance).when(field).get(any(Object.class));
 doReturn(true).when(new MockUtil()).isMock(instance);
 engine.process(testClass, testClass, field);
 verify(field).set(any(Object.class), argThat(isA(Mockito.spy(instance).getClass())));
 }

 @Test
 public void testProcessSpyAnnotationWithFieldThatIsNotAMock() {
 Object instance = mock(Object.class);
 Field field = mock(Field.class);
 doReturn(instance).when(field).get(any(Object.class));
 doReturn(false).when(new MockUtil()).isMock(instance);
 engine.process(testClass, testClass, field);
 verify(field).set(any(Object.class), argThat(isA(instance.getClass())));
 }

 @Test(expected = MockitoException.class)
 public void testProcessSpyAnnotationWithAnnotationPresentTwice() {
 Field field = mock(Field.class);
 doReturn(instance).when(field).get(any(Object.class));
 engine.process(testClass, testClass, field);
 engine.process(testClass, testClass, field);
 }

 @Test
 public void testProcessSpyAnnotationWithOtherAnnotationsPresent() {
 Field field = mock(Field.class);
 doReturn(instance).when(field).get(any(Object.class));
 engine.process(testClass, testClass, field);
 Field mockAnnotationField = mock(Field.class);
 Field captorAnnotationField = mock(Field.class);
 Field mockitoAnnotationField = mock(Field.class);
 doReturn(field).when(field).getAnnotation(Spy.class);
 doReturn(mockAnnotationField).when(field).getAnnotation(Mock.class);
 doReturn(captorAnnotationField).when(field).getAnnotation(Captor.class);
 doReturn(mockitoAnnotationField).when(field).getAnnotation(Mockito.MockitoAnnotations.Mock.class);
 assertFalse(engine.process(testClass, testClass, field));
 new Reporter().unsupportedCombinationOfAnnotations("Spy", "MockitoAnnotations.Mock");
 }
}