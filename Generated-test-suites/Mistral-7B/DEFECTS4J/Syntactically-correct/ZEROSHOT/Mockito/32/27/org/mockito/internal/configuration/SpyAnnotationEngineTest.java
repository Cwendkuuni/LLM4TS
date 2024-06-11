package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import static org.mockito.Mockito.fieldDoesNotHaveAnnotation;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine engine;
 @Rule
 public final MockitoAnnotations annotations = initMocks(this);
 @Mock
 private Object testClass;
 @Spy
 private Object spiedObject;

 @Before
 public void setUp() {
 engine = new SpyAnnotationEngine();
 }

 @Test
 public void testCreateMockForNullAnnotation() {
 engine.createMockFor(null, null);
 assertNull(engine.createMockFor(null, null));
 }

 @Test
 public void testProcessNoSpyAnnotation() {
 engine.process(testClass.getClass(), testClass);
 verify(testClass, Mockito.never()).set(Mockito.anyField(), Mockito.any());
 }

 @Test
 public void testProcessExistingSpyAnnotation() {
 Field existingSpyField = Mockito.spy(testClass.getClass()).getDeclaredField("existingSpyField");
 existingSpyField.setAccessible(true);
 existingSpyField.set(testClass, spiedObject);
 engine.process(testClass.getClass(), testClass);
 assertEquals(spiedObject, existingSpyField.get(testClass));
 }

 @Test
 public void testProcessNullInstance() {
 assertThrows(MockitoException.class, () -> engine.process(testClass.getClass(), null));
 }

 @Test
 public void testProcessMockInstance() {
 Field mockField = Mockito.mock(Field.class);
 mockField.setAccessible(true);
 mockField.set(testClass, Mockito.mock(Object.class));
 engine.process(testClass.getClass(), testClass);
 Mockito.verifyNoInteractions(mockField);
 }

 @Test
 public void testProcessCaptorAnnotation() {
 Field captorField = Mockito.spy(testClass.getClass()).getDeclaredField("captorField");
 captorField.setAccessible(true);
 captorField.set(testClass, Mockito.mock(org.mockito.ArgumentCaptor.class));
 engine.process(testClass.getClass(), testClass);
 verify(testClass, Mockito.never()).set(Mockito.anyField(), Mockito.any(org.mockito.ArgumentCaptor.class));
 }

 @Test(expected = MockitoException.class)
 public void testProcessAnnotationWithOtherAnnotation() throws NoSuchFieldException, IllegalAccessException {
 Field fieldWithAnnotation = testClass.getClass().getDeclaredField("fieldWithAnnotationAndMockAnnotation");
 fieldWithAnnotation.setAccessible(true);
 fieldWithAnnotation.set(testClass, mock(Object.class));
 engine.process(testClass.getClass(), testClass);
 }

 @Test(expected = MockitoException.class)
 public void testProcessAnnotationWithSpyAndMockAnnotation() throws NoSuchFieldException, IllegalAccessException {
 Field fieldWithAnnotation = testClass.getClass().getDeclaredField("fieldWithSpyAndMockAnnotation");
 fieldWithAnnotation.setAccessible(true);
 fieldWithAnnotation.set(testClass, mock(Object.class));
 engine.process(testClass.getClass(), testClass);
 }

 @Test(expected = MockitoException.class)
 public void testProcessAnnotationWithSpyAndCaptorAnnotation() throws NoSuchFieldException, IllegalAccessException {
 Field fieldWithAnnotation = testClass.getClass().getDeclaredField("fieldWithSpyAndCaptorAnnotation");
 fieldWithAnnotation.setAccessible(true);
 fieldWithAnnotation.set(testClass, mock(org.mockito.ArgumentCaptor.class));
 engine.process(testClass.getClass(), testClass);
 }

 @Test
 public void testProcessMultipleSpyAnnotation() throws NoSuchFieldException, IllegalAccessException {
 Field field1 = testClass.getClass().getDeclaredField("field1");
 field1.setAccessible(true);
 Field field2 = testClass.getClass().getDeclaredField("field2");
 field2.setAccessible(true);
 Object instance = new Object() {};
 field1.set(instance, new Object());
 field2.set(instance, new Object());
 engine.process(testClass.getClass(), instance);
 assertEquals(new Object(), field1.get(instance));
 assertEquals(new Object(), field2.get(instance));
 }

 @Test(expected = MockitoException.class)
 public void testProcessFieldWithoutAnnotationAccess() throws NoSuchFieldException, IllegalAccessException {
 Field notAccessibleField = testClass.getClass().getDeclaredField("notAccessibleField");
 notAccessibleField.setAccessible(false);
 engine.process(testClass.getClass(), testClass);
 }

 @Test(expected = MockitoException.class)
 public void testProcessFieldWithAnnotationAccessButNoSpyAnnotation() throws NoSuchFieldException, IllegalAccessException {
 Field fieldWithAnnotation = testClass.getClass().getDeclaredField("fieldWithAnnotationButNoSpyAnnotation");
 fieldWithAnnotation.setAccessible(true);
 engine.process(testClass.getClass(), testClass);
 }

 @Test
 public void testProcessFieldWithAnnotationAndMockAnnotation() throws NoSuchFieldException, IllegalAccessException {
 Field fieldWithAnnotationAndMockAnnotation = testClass.getClass().getDeclaredField("fieldWithAnnotationAndMockAnnotation");
 fieldWithAnnotationAndMockAnnotation.setAccessible(true);
 engine.process(testClass.getClass(), testClass);
 assertThrows(MockitoException.class, () -> fieldDoesNotHaveAnnotation(Spy.class, fieldWithAnnotationAndMockAnnotation));
 }

 @Test
 public void testProcessFieldWithAnnotationAndCaptorAnnotation() throws NoSuchFieldException, IllegalAccessException {
 Field fieldWithAnnotationAndCaptorAnnotation = testClass.getClass().getDeclaredField("fieldWithAnnotationAndCaptorAnnotation");
 fieldWithAnnotationAndCaptorAnnotation.setAccessible(true);
 engine.process(testClass.getClass(), testClass);
 assertThrows(MockitoException.class, () -> fieldDoesNotHaveAnnotation(Spy.class, fieldWithAnnotationAndCaptorAnnotation));
 }

 @Test
 public void testProcessFieldWithAnnotationAndNoInstance() throws NoSuchFieldException, IllegalAccessException {
 Field fieldWithAnnotation = testClass.getClass().getDeclaredField("fieldWithAnnotationAndNoInstance");
 fieldWithAnnotation.setAccessible(true);
 engine.process(testClass.getClass(), null);
 }

 @Test
 public void testProcessFieldWithAnnotationAndNullInstanceInside() throws NoSuchFieldException, IllegalAccessException {
 Field fieldWithAnnotation = testClass.getClass().getDeclaredField("fieldWithAnnotationAndNullInstanceInside");
 fieldWithAnnotation.setAccessible(true);
 Object nestedObject = new Object() {};
 fieldWithAnnotation.set(new Object() {}, nestedObject);
 engine.process(testClass.getClass(), null);
 assertNull(fieldWithAnnotation.get(new Object() {}));
 }

 @Test
 public void testProcessFieldWithAnnotationAndNullInstanceInsideWithAccess() throws NoSuchFieldException, IllegalAccessException {
 Field fieldWithAnnotation = testClass.getClass().getDeclaredField("fieldWithAnnotationAndNullInstanceInsideWithAccess");
 fieldWithAnnotation.setAccessible(true);
 Object nestedObject = new Object() {};
 fieldWithAnnotation.set(testClass, nestedObject);
 engine.process(testClass.getClass(), testClass);
 assertNull(fieldWithAnnotation.get(testClass));
 }

 @Test
 public void testProcessFieldWithAnnotationAndNullInstanceInsideWithNoAccess() throws NoSuchFieldException, IllegalAccessException {
 Field fieldWithAnnotation = testClass.getClass().getDeclaredField("fieldWithAnnotationAndNullInstanceInsideWithNoAccess");
 engine.process(testClass.getClass(), testClass);
 assertThrows(IllegalAccessException.class, () -> fieldWithAnnotation.get(testClass));
 }
}