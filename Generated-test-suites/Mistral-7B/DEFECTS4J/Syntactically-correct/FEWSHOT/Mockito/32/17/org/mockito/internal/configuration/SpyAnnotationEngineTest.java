package org.mockito.internal.configuration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.internal.configuration.AnnotationEngine;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtilTest;

public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine annotationEngine;
 private Field testField;
 private Object testObject;
 private Class testClass;

 @Mock
 private AnnotationEngine annotationEngineMock;
 @Captor
 private FieldsCaptor fieldsCaptor;

 @Before
 public void setUp() {
 annotationEngine = new SpyAnnotationEngine();
 testField = MockUtilTest.createTestField();
 testObject = new Object();
 testClass = testObject.getClass();
 }

 @Test
 public void testCreateMockFor() {
 assertNull(annotationEngine.createMockFor(Spy.class, testField));
 }

 @Test(expected = MockitoException.class)
 public void testProcessNullContext() {
 annotationEngine.process(null, testObject);
 }

 @Test
 public void testProcessNoSpyAnnotation() {
 annotationEngine.process(testClass, testObject);
 verifyZeroInteractions(annotationEngineMock);
 }

 @Test
 public void testProcessWithSpyAnnotation() {
 // given
 when(testField.get(testObject)).thenReturn(testObject);
 when(testField.isAnnotationPresent(Spy.class)).thenReturn(true);

 // when
 annotationEngine.process(testClass, testObject);

 // then
 assertFalse(testField.isAccessible());
 assertSame(spy(testObject), testField.get(testObject));
 verifyNoMoreInteractions(annotationEngineMock);
 }

 @Test(expected = MockitoException.class)
 public void testProcessWithNullTestObject() {
 annotationEngine.process(testClass, null);
 }

 @Test
 public void testProcessWithNonAccessibleField() {
 annotationEngine.process(testClass, testObject);
 verifyZeroInteractions(annotationEngineMock);
 }

 @Test
 public void testProcessWithInstanceNotMock() {
 // given
 when(testField.get(testObject)).thenReturn(testObject);
 when(testField.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(new MockUtil().isMock(testObject)).thenReturn(false);

 // when
 annotationEngine.process(testClass, testObject);

 // then
 Mockito.reset(testObject);
 assertSame(testObject, testField.get(testObject));
 verifyNoMoreInteractions(annotationEngineMock);
 }

 @Test(expected = IllegalAccessException.class)
 public void testProcessWithIllegalAccessException() {
 when(testField.get(testObject)).thenThrow(new IllegalAccessException());
 annotationEngine.process(testClass, testObject);
 }

 @Test
 public void testAssertNoAnnotations() {
 // given
 Field otherField = MockUtilTest.createTestField();
 testField.setAnnotation(new Spy());
 otherField.setAnnotation(new Mockito.MockitoAnnotations.Mock());
 when(testField.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(otherField.isAnnotationPresent(Mockito.MockitoAnnotations.Mock.class)).thenReturn(true);

 // when
 annotationEngine.process(testClass, testObject);

 // then
 verifyNoMoreInteractions(annotationEngineMock);
 }

 @Test(expected = MockitoException.class)
 public void testAssertNoAnnotationsWithCaptorAnnotation() {
 // given
 Field captorField = MockUtilTest.createTestField();
 testField.setAnnotation(new Spy());
 captorField.setAnnotation(new Captor());
 when(testField.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(captorField.isAnnotationPresent(Captor.class)).thenReturn(true);

 // when & then
 new Reporter().unsupportedCombinationOfAnnotations(Spy.class.getSimpleName(), "Captor");
 }

 @Test(expected = MockitoException.class)
 public void testAssertNoAnnotationsWithMockAnnotation() {
 // given
 Field mockField = MockUtilTest.createTestField();
 testField.setAnnotation(new Spy());
 mockField.setAnnotation(new Mockito.Mock());
 when(testField.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(mockField.isAnnotationPresent(Mockito.Mock.class)).thenReturn(true);

 // when & then
 new Reporter().unsupportedCombinationOfAnnotations(Spy.class.getSimpleName(), "Mock");
 }

 @Test(expected = MockitoException.class)
 public void testAssertNoAnnotationsWithMockitoAnnotationsMockAnnotation() {
 // given
 Field mockitoMockField = MockUtilTest.createTestField();
 testField.setAnnotation(new Spy());
 mockitoMockField.setAnnotation(
 org.mockito.MockitoAnnotations.Mock.class);
 when(testField.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(mockitoMockField.isAnnotationPresent(org.mockito.MockitoAnnotations.Mock.class)).thenReturn(true);

 // when & then
 new Reporter().unsupportedCombinationOfAnnotations(Spy.class.getSimpleName(), "MockitoAnnotations.Mock");
 }
}

class FieldsCaptor {
 final Field[] cachedFields;

 FieldsCaptor() {
 cachedFields = new Field[0];
 }
}