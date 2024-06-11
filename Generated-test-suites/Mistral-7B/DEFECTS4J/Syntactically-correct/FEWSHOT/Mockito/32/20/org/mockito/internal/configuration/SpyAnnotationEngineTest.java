package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import java.lang.reflect.Field;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class SpyAnnotationEngineTest {

 @Mock
 private AnnotationEngine annotationEngine;
 @Spy
 private SpyAnnotationEngine spyAnnotationEngine;
 @Captor
 private FieldCaptor fieldCaptor;

 @Before
 public void setUp() {
 Mockito.when(annotationEngine.createMockFor(ArgumentMatchers.any(Annotation.class), ArgumentMatchers.any(Field.class)))
         .thenReturn(null);
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockForNullAnnotation() {
 spyAnnotationEngine.process(Mockito.mock(Class.class), mock(Object.class));
 spyAnnotationEngine.createMockFor(null, null);
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockForNullField() {
 spyAnnotationEngine.process(Mockito.mock(Class.class), mock(Object.class));
 spyAnnotationEngine.createMockFor(Spy.class, null);
 }

 @Test
 public void testCreateMockForExistingMock() throws IllegalAccessException {
 Object testInstance = mock(Object.class);
 Object fieldInstance = mock(Object.class);
 Object spyInstance = spy(fieldInstance);
 Object testClass = mock(Object.class);
 Field field = Mockito.spy(new Field() {
 @Override public Object get(Object obj) { return fieldInstance; }
 @Override public void setAccessible(boolean b) {}
 @Override public Class<?> getType() { return Object.class; }
 @Override public String getName() { return "fieldName"; }
 }).getDeclaredField("fieldName");
 field.setAccessible(true);
 field.set(testClass, testInstance);

 spyAnnotationEngine.process(Mockito.mock(Class.class), testClass);
 spyAnnotationEngine.createMockFor(Spy.class, field);

 verify(testInstance).getClass();
 verify(fieldInstance).getClass();
 verify(spyInstance).getClass();
 assertNotNull(fieldInstance);
 assertNotNull(spyInstance);
 }

 @Test
 public void testCreateSpyForNullInstance() throws IllegalAccessException {
 Object testClass = mock(Object.class);
 Field field = Mockito.spy(new Field() {
 @Override public Object get(Object obj) { return null; }
 @Override public void setAccessible(boolean b) {}
 @Override public Class<?> getType() { return Object.class; }
 @Override public String getName() { return "fieldName"; }
 }).getDeclaredField("fieldName");
 field.setAccessible(true);
 field.set(testClass, null);

 spyAnnotationEngine.process(Mockito.mock(Class.class), testClass);
 spyAnnotationEngine.createMockFor(Spy.class, field);

 assertNull(field.get(testClass));
 }

 @Test
 public void testCreateSpyForNonAccessibleInstance() throws IllegalAccessException {
 Object testClass = mock(Object.class);
 Field field = Mockito.spy(new Field() {
 @Override public Object get(Object obj) { return null; }
 @Override public void setAccessible(boolean b) {}
 @Override public Class<?> getType() { return Object.class; }
 @Override public String getName() { return "fieldName"; }
 }).getDeclaredField("fieldName");
 field.setAccessible(false);
 field.set(testClass, null);

 spyAnnotationEngine.process(Mockito.mock(Class.class), testClass);
 spyAnnotationEngine.createMockFor(Spy.class, field);

 assertNull(field.get(testClass));
 }

 @Test
 public void testCreateSpyForNonSpyedInstance() throws IllegalAccessException {
 Object testClass = mock(Object.class);
 Object fieldInstance = mock(Object.class);
 Field field = Mockito.spy(new Field() {
 @Override public Object get(Object obj) { return fieldInstance; }
 @Override public void setAccessible(boolean b) {}
 @Override public Class<?> getType() { return Object.class; }
 @Override public String getName() { return "fieldName"; }
 }).getDeclaredField("fieldName");
 field.setAccessible(true);
 field.set(testClass, fieldInstance);

 spyAnnotationEngine.process(Mockito.mock(Class.class), testClass);
 spyAnnotationEngine.createMockFor(Spy.class, field);

 assertNotNull(field.get(testClass));
 }

 @Test
 public void testCreateSpyForExistingSpy() throws IllegalAccessException {
 Object testInstance = mock(Object.class);
 Object fieldInstance = mock(Object.class);
 Object spyInstance = spy(fieldInstance);
 Object testClass = mock(Object.class);
 Field field = Mockito.spy(new Field() {
 @Override public Object get(Object obj) { return fieldInstance; }
 @Override public void setAccessible(boolean b) {}
 @Override public Class<?> getType() { return Object.class; }
 @Override public String getName() { return "fieldName"; }
 }).getDeclaredField("fieldName");
 field.setAccessible(true);
 field.set(testClass, spyInstance);

 spyAnnotationEngine.process(Mockito.mock(Class.class), testClass);
 spyAnnotationEngine.createMockFor(Spy.class, field);

 assertSame(spyInstance, field.get(testClass));
 }

 @Test
 public void testAssertNoAnnotationsForAnnotationWithEmptyArray() {
 spyAnnotationEngine.assertNoAnnotations(Spy.class, fieldCaptor.capture(), Mock.class);
 assertNotNull(fieldCaptor.getValue());
 }

 @Test(expected = org.mockito.exceptions.base.MockitoException.class)
 public void testAssertNoAnnotationsForAnnotationWithUndesiredAnnotation() {
 spyAnnotationEngine.assertNoAnnotations(Spy.class, fieldCaptor.capture(), Mockito.MockitoAnnotations.Mock.class);
 }

 @Test(expected = org.mockito.exceptions.base.MockitoException.class)
 public void testAssertNoAnnotationsForAnnotationWithUndesiredAnnotationOnField() {
 Field field = Mockito.spy(new Field() {
 @Override public Object get(Object obj) { return null; }
 @Override public void setAccessible(boolean b) {}
 @Override public Class<?> getType() { return Object.class; }
 @Override public String getName() { return "fieldName"; }
 }).getDeclaredField("fieldName");
 spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mockito.MockitoAnnotations.Mock.class);
 }
}