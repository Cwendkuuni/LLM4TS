package org.mockito.internal.configuration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.AnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.exceptions.Reporter;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

 private static final String FIELD_NAME = "fieldName";
 private static final Class<SpyAnnotationEngine> CLASS_NAME = SpyAnnotationEngine.class;

 @SuppressWarnings("unchecked")
 @Mock
 private AnnotationEngine annotationEngine;
 @Mock
 private Reporter reporter;
 @Spy
 private SpyAnnotationEngine systemUnderTest;
 @Captor
 private ArgumentCaptor<Annotation> annotationCaptor;

 @Test(expected = MockitoException.class)
 public void testCreateMockForAnnotationIsNull() {
     systemUnderTest.createMockFor(null, null);
 }

 @Test
 public void testCreateMockForAnnotationPresentSpyAndFieldIsAccessible() throws IllegalAccessException {
     Class context = getClass();
     Object testInstance = mock(Object.class);
     Field field = getField(context, FIELD_NAME, Object.class);
     when(field.get(testInstance)).thenReturn("test value");
     systemUnderTest.process(context, testInstance);
     verify(annotationEngine).process(context, testInstance);
     Object spiedInstance = field.get(testInstance);
     verify(annotationEngine).createMockFor(Spy.class, field);
     verify(reporter).unsupportedCombinationOfAnnotations("Spy", "AnnotationEngine");
     verify(systemUnderTest).assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
     verifyZeroInteractions(spiedInstance);
 }

 @Test
 public void testCreateMockForAnnotationPresentSpyAndFieldIsNotAccessible() throws IllegalAccessException {
     Class context = getClass();
     Object testInstance = mock(Object.class);
     Field field = getField(context, FIELD_NAME, Object.class);
     when(field.get(testInstance)).thenReturn("test value");
     systemUnderTest.process(context, testInstance);
     verify(annotationEngine).process(context, testInstance);
     doThrow(new IllegalAccessException()).when(field).setAccessible(true);
     try {
         systemUnderTest.process(context, testInstance);
     } catch (IllegalAccessException e) {
         // expected
     } finally {
         field.setAccessible(true);
     }
     verify(reporter).unsupportedCombinationOfAnnotations("Spy", "AnnotationEngine");
     verifyZeroInteractions(field);
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockForAnnotationPresentSpyAndInstanceIsNull() throws IllegalAccessException {
     Class context = getClass();
     Object testInstance = mock(Object.class);
     Field field = getField(context, FIELD_NAME, Object.class);
     when(field.get(testInstance)).thenReturn(null);
     systemUnderTest.process(context, testInstance);
     verify(annotationEngine).process(context, testInstance);
     verify(reporter).unsupportedCombinationOfAnnotations("Spy", "AnnotationEngine");
     verifyZeroInteractions(field);
     verify(field).setAccessible(true);
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockForAnnotationPresentSpyAndInstanceIsAMock() throws IllegalAccessException {
     Class context = getClass();
     Object testInstance = mock(Object.class);
     Field field = getField(context, FIELD_NAME, Object.class);
     when(field.get(testInstance)).thenReturn(mock(Object.class));
     systemUnderTest.process(context, testInstance);
     verify(annotationEngine).process(context, testInstance);
     verify(field).set(testInstance, mock(Object.class));
     verify(reporter).unsupportedCombinationOfAnnotations("Spy", "Spy");
     verify(testInstance).getClass();
 }

 @Test
 public void testCreateMockForAnnotationPresentSpyAndInstanceIsNotAMock() throws IllegalAccessException {
     Class context = getClass();
     Object testInstance = new Object();
     Field field = getField(context, FIELD_NAME, Object.class);
     systemUnderTest.process(context, testInstance);
     verify(annotationEngine).process(context, testInstance);
     Object spiedInstance = field.get(testInstance);
     verify(annotationEngine).createMockFor(Spy.class, field);
     verify(reporter).unsupportedCombinationOfAnnotations("Spy", "AnnotationEngine");
     verify(systemUnderTest).assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
     verify(spiedInstance).getClass();
 }

 @Test
 public void testCreateMockForAnnotationPresentSpyAndInstanceIsAlreadySpyed() throws IllegalAccessException {
     Class context = getClass();
     Object testInstance = new Object();
     Field field = getField(context, FIELD_NAME, Object.class);
     Object spiedInstance = spy(testInstance);
     field.set(testInstance, spiedInstance);
     systemUnderTest.process(context, testInstance);
     verify(annotationEngine).process(context, testInstance);
     verifyZeroInteractions(annotationEngine);
     verifyZeroInteractions(field);
 }

 @Test
 public void testProcessAnnotationEngineIsNull() {
     systemUnderTest.process(null, null);
 }

 @Test
 public void testProcessNoAnnotationPresent() throws IllegalAccessException {
     Class context = getClass();
     Object testInstance = new Object();
     systemUnderTest.process(context, testInstance);
     verifyZeroInteractions(annotationEngine);
 }

 @Test(expected = IllegalAccessException.class)
 public void testProcessFieldIsNotAccessible() throws IllegalAccessException {
     Class context = getClass();
     Object testInstance = new Object();
     Field field = getField(context, FIELD_NAME, Object.class);
     doThrow(new IllegalAccessException()).when(field).setAccessible(true);
     systemUnderTest.process(context, testInstance);
 }

 @Test
 public void testAssertNoAnnotationsIsCalledWithCorrectArguments() {
     Field field = getField(getClass(), "fieldName", Object.class);
     systemUnderTest.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
 }

 @Test
 public void testAssertNoAnnotationsWhenAnnotationPresent() {
     Field field = getField(getClass(), "fieldName", Object.class);
     when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
     systemUnderTest.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAssertNoAnnotationsWhenFieldIsNull() {
     systemUnderTest.assertNoAnnotations(Spy.class, null, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAssertNoAnnotationsWhenAnnotationIsNull() {
     Field field = getField(getClass(), "fieldName", Object.class);
     systemUnderTest.assertNoAnnotations(null, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAssertNoAnnotationsWhenAnnotationIsNotInstanceofAnnotation() {
     Field field = getField(getClass(), "fieldName", Object.class);
     systemUnderTest.assertNoAnnotations(Integer.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
 }

 @Test
 public void testAssertNoAnnotationsWhenAnnotationIsPresentButNotUndesired() {
     Field field = getField(getClass(), "fieldName", Object.class);
     when(field.isAnnotationPresent(Captor.class)).thenReturn(true);
     systemUnderTest.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
 }

 @Test
 public void testAssertNoAnnotationsWhenAnnotationsAreAllPresent() {
     Field field = getField(getClass(), "fieldName", Object.class);
     when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
     when(field.isAnnotationPresent(Mock.class)).thenReturn(true);
     when(field.isAnnotationPresent(org.mockito.MockitoAnnotations.Mock.class)).thenReturn(true);
     when(field.isAnnotationPresent(Captor.class)).thenReturn(true);
     systemUnderTest.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
 }

 @Test(expected = MockitoException.class)
 public void testAssertNoAnnotationsWhenAnnotationIsPresentAndUndesired() {
     Field field = getField(getClass(), "fieldName", Object.class);
     when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
     when(field.isAnnotationPresent(Mock.class)).thenReturn(true);
     systemUnderTest.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
 }

 @Test
 public void testReset() throws IllegalAccessException {
     Class context = getClass();
     Object testInstance = mock(Object.class);
     Field field = getField(context, FIELD_NAME, Object.class);
     Object spiedInstance = spy(mock(Object.class));
     field.set(testInstance, spiedInstance);
     when(field.get(testInstance)).thenReturn(spiedInstance);
     systemUnderTest.process(context, testInstance);
     when(field.get(testInstance)).thenReturn(spiedInstance);
     systemUnderTest.process(context, testInstance);
     verify(spiedInstance).getClass();
     verify(spiedInstance).getClass();
 }

 private Field getField(Class clazz, String fieldName, Class<?> fieldType) throws IllegalAccessException {
     Field field = clazz.getDeclaredField(fieldName);
     field.setAccessible(true);
     return field;
 }
}