package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*; // for importing all Mockito static methods

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine annotationEngine;
 @Mock private Object testClass;
 @Spy private Object spiedInstance;

 @Before
 public void setup() {
 annotationEngine = new SpyAnnotationEngine();
 try {
 fieldAccessible(testClass.getClass(), "spiedField", Object.class);
 } catch (NoSuchFieldException e) {
 // Create a field for testing purposes
 try {
 testClass.getClass().getDeclaredField("spiedField");
 } catch (NoSuchFieldException ex) {
 testClass.getClass().declareField("spiedField");
 }
 testClass.getClass().getDeclaredField("spiedField").setAccessible(true);
 }
 }

 @Test(expected = MockitoException.class)
 public void testSpyOnNullInstance() {
 annotationEngine.process(testClass.getClass(), testClass);
 assertNull(testClass.getClass().getDeclaredField("spiedField").get(testClass));
 annotationEngine.process(testClass.getClass(), testClass);
 // This should throw an exception since the instance is null
 annotationEngine.process(testClass.getClass(), testClass);
 }

 @Test
 public void testSpyOnExistingMock() {
 Object mockInstance = mock(Object.class);
 testClass.getClass().getDeclaredField("spiedField").set(testClass, mockInstance);
 annotationEngine.process(testClass.getClass(), testClass);
 verify(mockInstance).mockito_isMock();
 }

 @Test
 public void testSpyOnNonExistingField() {
 assertFalse(testClass.getClass().getDeclaredField("nonExistingField").isAnnotationPresent(Spy.class));
 annotationEngine.process(testClass.getClass(), testClass);
 }

 @Test
 public void testSpyOnFieldWithMultipleUndesiredAnnotations() {
 testClass.getClass().getDeclaredField("spiedField").set(testClass, spiedInstance);
 testClass.getClass().getDeclaredField("spiedField").setAccessible(true);
 testClass.getClass().getDeclaredField("spiedField").annotate(new MockAnnotation());
 testClass.getClass().getDeclaredField("spiedField").annotate(new CaptorAnnotation());

 try {
 annotationEngine.process(testClass.getClass(), testClass);
 fail("Expected UnsupportedCombinationOfAnnotations exception");
 } catch (MockitoException e) {
 // Expected exception
 }
 }

 @Test
 public void testSpyOnFieldWithOneUndesiredAnnotation() {
 testClass.getClass().getDeclaredField("spiedField").set(testClass, spiedInstance);
 testClass.getClass().getDeclaredField("spiedField").setAccessible(true);
 testClass.getClass().getDeclaredField("spiedField").annotate(new MockAnnotation());

 try {
 annotationEngine.process(testClass.getClass(), testClass);
 } catch (Exception e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testSpyOnFieldWithNoUndesiredAnnotations() {
 testClass.getClass().getDeclaredField("spiedField").set(testClass, spiedInstance);
 testClass.getClass().getDeclaredField("spiedField").setAccessible(true);

 annotationEngine.process(testClass.getClass(), testClass);
 verify(spiedInstance).mockito_isSpy();
 }

 @Test
 public void testSpyOnFieldWithSameAnnotation() {
 testClass.getClass().getDeclaredField("spiedField").set(testClass, spiedInstance);
 testClass.getClass().getDeclaredField("spiedField").setAccessible(true);
 testClass.getClass().getDeclaredField("spiedField").annotate(new SpyAnnotation());

 try {
 annotationEngine.process(testClass.getClass(), testClass);
 fail("Expected UnsupportedCombinationOfAnnotations exception");
 } catch (MockitoException e) {
 // Expected exception
 }
 }

 @Test
 public void testSpyOnFieldWithCaptorAnnotation() {
 ArgumentCaptor<Object> captor = argumentCaptor(Object.class);
 testClass.getClass().getDeclaredField("spiedField").set(testClass, spiedInstance);
 testClass.getClass().getDeclaredField("spiedField").setAccessible(true);
 testClass.getClass().getDeclaredField("spiedField").annotate(new CaptorAnnotation());

 try {
 annotationEngine.process(testClass.getClass(), testClass);
 fail("Expected UnsupportedCombinationOfAnnotations exception");
 } catch (MockitoException e) {
 // Expected exception
 }
 }

 private void fieldAccessible(Class clazz, String fieldName, Class type) throws NoSuchFieldException {
 Field field = clazz.getDeclaredField(fieldName);
 if (field == null) {
 throw new NoSuchFieldException("Field " + fieldName + " not found in class " + clazz.getName());
 }
 field.setAccessible(true);
}

 private static class MockAnnotation {}
 private static class CaptorAnnotation {}
 private static class SpyAnnotation extends Annotation {}
}