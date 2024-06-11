package org.mockito.internal.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.mockito.internal.configuration.SpyAnnotationEngine.*;

public class SpyAnnotationEngineTest {

 @Test
 public void testProcess_NoSpyAnnotation() {
     Class context =mock(Class.class);
     Object testClass =mock(Object.class);

     SpyAnnotationEngine engine =new SpyAnnotationEngine();
     engine.process(context, testClass);

     verify(context, times(1)).getDeclaredFields();
 }

 @Test
 public void testProcess_SpyAnnotationPresent_InstanceNull() {
     Class context =mock(Class.class);
     Object testClass =mock(Object.class);
     Field field =mock(Field.class);

     when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
     when(field.getName()).thenReturn("someFieldName");
     when(field.isAccessible()).thenReturn(false);

     SpyAnnotationEngine engine =new SpyAnnotationEngine();
     engine.process(context, testClass);

     verify(context, times(1)).getDeclaredFields();
     verify(field, times(1)).get(testClass);
     verify(field, times(1)).setAccessible(true);
     verify(field, times(1)).set(testClass, isA(Mockito.mock(Object.class)));
     verify(field, times(1)).setAccessible(equalTo(true));
 }

 @Test
 public void testProcess_SpyAnnotationPresent_InstanceNotMock() {
     Class context =mock(Class.class);
     Object testClass =mock(Object.class);
     Field field =mock(Field.class);
     Object instance =mock(Object.class);

     when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
     when(field.getName()).thenReturn("someFieldName");
     when(field.get(testClass)).thenReturn(instance);
     when(field.isAccessible()).thenReturn(false);

     SpyAnnotationEngine engine =new SpyAnnotationEngine();
     engine.process(context, testClass);

     verify(context, times(1)).getDeclaredFields();
     verify(field, times(1)).get(testClass);
     verify(field, times(1)).setAccessible(true);
     verify(instance, times(1)).getClass();
     verify(field, times(1)).set(testClass, argThat(isA(Mockito.spy(instance))));
     verify(field, times(1)).setAccessible(equalTo(true));
 }

 @Test
 public void testProcess_SpyAnnotationPresent_InstanceAlreadyMocked() {
     Class context =mock(Class.class);
     Object testClass =mock(Object.class);
     Field field =mock(Field.class);
     Object instance =mock(Object.class);

     when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
     when(field.getName()).thenReturn("someFieldName");
     when(field.get(testClass)).thenReturn(instance);
     when(field.isAccessible()).thenReturn(false);
     when(new MockUtil().isMock(instance)).thenReturn(true);

     SpyAnnotationEngine engine =new SpyAnnotationEngine();
     engine.process(context, testClass);

     verify(context, times(1)).getDeclaredFields();
     verify(field, times(1)).get(testClass);
     verify(field, times(1)).setAccessible(true);
     verify(instance, times(1)).getClass();
     verify(field, times(1)).set(testClass, argThat(isA(instance)));
     verify(field, times(1)).setAccessible(equalTo(true));
     verify(instance, times(1)).reset();
 }

 @Test(expected = MockitoException.class)
 public void testProcess_SpyAnnotationPresent_InstanceNullError() {
     Class context =mock(Class.class);
     Object testClass =mock(Object.class);
     Field field =mock(Field.class);

     when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
     when(field.getName()).thenReturn("someFieldName");
     when(field.isAccessible()).thenReturn(false);
     when(field.get(testClass)).thenReturn(null);

     SpyAnnotationEngine engine =new SpyAnnotationEngine();
     engine.process(context, testClass);

     fail("Should have thrown MockitoException with message: Cannot create a @Spy for 'someFieldName' field because the instance is missing");
 }

 @Test(expected = MockitoException.class)
 public void testProcess_SpyAnnotationPresent_AnotherAnnotationPresent() {
     Class context =mock(Class.class);
     Object testClass =mock(Object.class);
     Field field =mock(Field.class);

     when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
     when(field.getName()).thenReturn("someFieldName");
     when(field.isAccessible()).thenReturn(false);

     SpyAnnotationEngine engine =new SpyAnnotationEngine();
     engine.process(context, testClass);

     try {
         when(field.isAnnotationPresent(MockitoMock.class)).thenReturn(true);
     } catch (IllegalArgumentException e) {
         throw new AssertionError("Should not have thrown IllegalArgumentException", e);
     }

     new Reporter().unsupportedCombinationOfAnnotations("Spy", Spy.class.getName());
 }
}