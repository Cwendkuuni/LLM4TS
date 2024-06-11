package org.mockito.internal.configuration.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.Spy;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine annotationEngine;
 private Object testClass;
 private Field testField;

 @Before
 public void setup() {
 annotationEngine = new SpyAnnotationEngine();
 testClass = new Object() {};
 testField = Mockito.spy(new Object()).getClass().getDeclaredField("testField");
 }

 @Test
 public void testCreateMockForAnnotatedField() {
 // Test case for createMockFor method
 testField.setAccessible(true);
 Object originalInstance = new Object();
 testField.set(testClass, originalInstance);
 Mockito.initMocks(testClass); // Init mocks to make sure the field is accessible

 annotationEngine.process(testClass.getClass(), testClass);

 ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);

 verify(annotationEngine).createMockFor(Spy.class, testField);

 // Verify that the original instance is spied and returned
 Object spiedInstance = testField.get(testClass);
 assertTrue(MockUtil.isMock(spiedInstance));

 // Verify that the original instance was reset
 testField.set(testClass, originalInstance);
 Mockito.reset(originalInstance);
 verify(originalInstance).reset();
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockForNullInstance() {
 // Test case for createMockFor method with null instance
 testField.setAccessible(true);
 testField.set(testClass, null);
 annotationEngine.process(testClass.getClass(), testClass);
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockForAnnotatedFieldTwice() {
 // Test case for createMockFor method called twice on the same annotated field
 testField.setAccessible(true);
 Object originalInstance = new Object();
 testField.set(testClass, originalInstance);
 annotationEngine.process(testClass.getClass(), testClass);
 annotationEngine.process(testClass.getClass(), testClass);
 }

 @Test
 @Spy
 public void testProcessAnnotatedFieldWithExistingSpy() {
 // Test case for process method with an existing spy
 Object existingSpy = mock(Object.class);
 testField.setAccessible(true);
 testField.set(testClass, existingSpy);
 annotationEngine.process(testClass.getClass(), testClass);

 // Verify no further changes were made to the existing spy
 assertEquals(existingSpy, testField.get(testClass));
 }

 @Test
 public void testProcessAnnotatedFieldWithExistingMock() {
 // Test case for process method with an existing mock
 Object existingMock = mock(Object.class);
 testField.setAccessible(true);
 testField.set(testClass, existingMock);
 annotationEngine.process(testClass.getClass(), testClass);

 // Verify that the existing mock was reset
 verify(existingMock).reset();
 }

 @Test(expected = MockitoException.class)
 public void testProcessAnnotatedFieldWithMultipleAnnotations() {
 // Test case for process method with a field annotated with both @Spy and @Mock
 testField.setAccessible(true);
 testField.set(testClass, new Object());
 annotationEngine.process(testClass.getClass(), testClass);
 }
}