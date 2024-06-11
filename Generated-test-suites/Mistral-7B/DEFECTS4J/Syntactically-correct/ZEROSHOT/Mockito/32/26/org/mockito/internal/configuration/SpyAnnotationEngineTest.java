package org.mockito.internal.configuration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.junit.MockitoJUnitRunner;

@SuppressWarnings({"unchecked"})
@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

 private AnnotationEngine annotationEngine;

 @Mock
 private Annotation annotation;

 @Mock
 private Field field;

 @Mock
 private Class<?> context;

 @Mock
 private Object testClass;

 @Spy
 private Object instanceToSpy;

 @Captor
 private ArgumentCaptor<Annotation> annotationCaptor;

 @Captor
 private ArgumentCaptor<Field> fieldCaptor;

 @Before
 public void setUp() {
 annotationEngine = new SpyAnnotationEngine();
 }

 @Test
 public void testCreateMockForReturnsNull() {
 assertNull(annotationEngine.createMockFor(annotation, field));
 }

 @Test
 public void testProcessDoesNotSpyNullInstance() {
 when(field.get(testClass)).thenReturn(null);
 annotationEngine.process(context, testClass);
 verifyZeroInteractions(context, testClass, field, instanceToSpy);
 }

 @Test
 public void testProcessSpyExistingMock() {
 when(field.get(testClass)).thenReturn(mock(Object.class));
 annotationEngine.process(context, testClass);
 verify(field).set(testClass, argThat(isA(Mockito.class)));
 }

 @Test
 public void testProcessSetsSpyOnAccessibleField() {
 when(field.isAccessible()).thenReturn(false);
 when(field.getName()).thenReturn("testField");
 when(context.getDeclaredFields()).thenReturn(new Field[]{field});
 when(field.get(testClass)).thenReturn(instanceToSpy);
 annotationEngine.process(context, testClass);
 verify(field).setAccessible(true);
 verify(field).set(testClass, argThat(isA(instanceToSpy.getClass())));
 verifyZeroInteractions(context, testClass, field, instanceToSpy);
 }

 @Test(expected = MockitoException.class)
 public void testProcessThrowsExceptionWhenInstanceMissing() {
 when(field.getName()).thenReturn("testField");
 when(context.getDeclaredFields()).thenReturn(new Field[]{field});
 when(field.get(testClass)).thenReturn(null);
 annotationEngine.process(context, testClass);
 fail("Expected MockitoException");
 }

 @Test(expected = MockitoException.class)
 public void testProcessThrowsExceptionWhenInstanceIsAMock() {
 when(field.getName()).thenReturn("testField");
 when(context.getDeclaredFields()).thenReturn(new Field[]{field});
 when(field.get(testClass)).thenReturn(mock(Object.class));
 annotationEngine.process(context, testClass);
 annotationEngine.process(context, testClass);
 fail("Expected MockitoException");
 }

 @Test
 public void testAssertNoAnnotations() {
 annotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
 }

 @Test(expected = org.mockito.exceptions.base.MockitoException.class)
 public void testAssertNoAnnotationsThrowsExceptionWhenSpyAnnotationPresentAndAnotherAnnotationPresent() {
 when(field.isAnnotationPresent(MockitoAnnotations.Mock.class)).thenReturn(true);
 annotationEngine.assertNoAnnotations(Spy.class, field, MockitoAnnotations.Mock.class);
 fail("Expected MockitoException");
 }
}