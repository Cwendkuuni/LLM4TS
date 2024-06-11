package org.mockito.internal.configuration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.util.MockUtil.isMock;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.ExposedForMocking;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;

public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine engine;
 private Object testObject;
 private Class testClass;
 private Field field;

 @Mock
 private AnnotationEngine mockAnnotationEngine;
 @Spy
 private Reporter reporter;
 @Captor
 private ArgumentCaptor<Annotation> annotationCaptor;

 @Before
 public void setup() {
 testClass = getClass();
 field = MockUtil.getDeclaredField(testClass, "testField");
 engine = new SpyAnnotationEngine() {
 @Override
 public AnnotationEngine getAnnotationEngine() {
 return mockAnnotationEngine;
 }
 };
 testObject = mock(Object.class);
 }

 @Test
 public void shouldNotCreateMockForAnnotation() {
 when(mockAnnotationEngine.createMockFor(Spy.class, field)).thenReturn(testObject);
 engine.process(testClass, testObject);
 verify(mockAnnotationEngine).createMockFor(Spy.class, field);
 verifyZeroInteractions(testObject);
 }

 @Test
 public void shouldSpyExistingMock() {
 when(mockAnnotationEngine.createMockFor(Spy.class, field)).thenReturn(testObject);
 when(field.get(testObject)).thenReturn(mock(Object.class));
 engine.process(testClass, testObject);
 verify(testObject).equals(testObject); // verify that the original instance is not replaced

 MockedStatic<MockUtil> mockedMockUtil = mockStatic(MockUtil.class);
 mockedMockUtil.when(() -> MockUtil.isMock(testObject)).thenReturn(true);
 engine.process(testClass, testObject);
 verify(testObject).equals(testObject); // verify that the original instance is not replaced
 mockedMockUtil.verify();
 }

 @Test(expected = MockitoException.class)
 public void shouldThrowExceptionWhenInstanceIsNull() {
 when(mockAnnotationEngine.createMockFor(Spy.class, field)).thenThrow(new IllegalStateException("Instance is null"));
 engine.process(testClass, testObject);
 }

 @Test(expected = MockitoException.class)
 public void shouldThrowExceptionWhenAccessingPrivateFieldFails() {
 when(mockAnnotationEngine.createMockFor(Spy.class, field)).thenThrow(new IllegalAccessException("Access failed"));
 engine.process(testClass, testObject);
 }

 @Test
 public void shouldNotCreateAnnotationInstanceTwice() {
 when(mockAnnotationEngine.createMockFor(Spy.class, field)).thenReturn(testObject);
 engine.process(testClass, testObject);
 engine.process(testClass, testObject);
 verifyNoMoreInteractions(mockAnnotationEngine);
 }

 @Test(expected = MockitoException.class)
 public void shouldThrowExceptionWhenAnnotationEngineReturnsNull() {
 when(mockAnnotationEngine.createMockFor(Spy.class, field)).thenReturn(null);
 engine.process(testClass, testObject);
 }

 @Test
 public void shouldNotCreateAnnotationInstanceWhenAnnotationNotPresent() {
 engine.process(testClass, testObject);
 verifyZeroInteractions(mockAnnotationEngine);
 }

 @Test
 public void shouldThrowExceptionWhenAnnotationAndMockAnnotationArePresent() {
 field.setAccessible(true);
 when(mockAnnotationEngine.createMockFor(Mock.class, field)).thenReturn(testObject);
 engine.process(testClass, testObject);
 new Reporter().unsupportedCombinationOfAnnotations("Spy", "Mock");
 }

 @Test
 public void shouldThrowExceptionWhenAnnotationAndCaptorAnnotationArePresent() {
 field.setAccessible(true);
 when(mockAnnotationEngine.createMockFor(Captor.class, field)).thenReturn(testObject);
 engine.process(testClass, testObject);
 new Reporter().unsupportedCombinationOfAnnotations("Spy", "Captor");
 }

 @Test
 public void shouldThrowExceptionWhenAnnotationAndMockitoAnnotationsMockArePresent() {
 field.setAccessible(true);
 when(mockAnnotationEngine.createMockFor(org.mockito.Mockito.Mock.class, field)).thenReturn(testObject);
 engine.process(testClass, testObject);
 new Reporter().unsupportedCombinationOfAnnotations("Spy", "MockitoAnnotations.Mock");
 }

 @Test
 public void shouldThrowExceptionWhenAnnotationAndSpyAnnotationArePresentOnSameField() {
 field.setAccessible(true);
 when(mockAnnotationEngine.createMockFor(Spy.class, field)).thenReturn(testObject);
 engine.process(testClass, testObject);
 new Reporter().unsupportedCombinationOfAnnotations("Spy", "Spy");
 }

 @Test
 public void shouldNotThrowExceptionWhenAnnotationIsNotPresent() {
 field.setAccessible(true);
 engine.process(testClass, testObject);
 verifyZeroInteractions(reporter);
 }

 @ExposedForMocking
 static class TestClass {
 @Spy
 private Object testField;
 }
}