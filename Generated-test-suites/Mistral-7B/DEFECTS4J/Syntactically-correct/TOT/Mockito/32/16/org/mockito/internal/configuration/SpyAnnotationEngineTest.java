import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.FieldUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine annotationEngine;
 private Object testClass;
 private Field field;

 @Before
 public void setup() {
 testClass = new Object() {};
 field = TestUtils.getDeclaredField(testClass, "spiedField");
 annotationEngine = new SpyAnnotationEngine();
 }

 @Test
 public void testCreateMockForNullAnnotation() {
 assertNull(annotationEngine.createMockFor(null, field));
 }

 @Test
 public void testCreateMockForSpyAnnotation() throws IllegalAccessException {
 Object instance = new Object();
 field.set(testClass, instance);
 annotationEngine.process(testClass.getClass(), testClass);
 assertNotNull(Mockito.isSpy(field.get(testClass)));
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockForSpyAnnotationWhenInstanceIsMissing() throws IllegalAccessException {
 annotationEngine.process(testClass.getClass(), testClass);
 field.set(testClass, null);
 field.setAccessible(true);
 field.set(testClass, mock(Object.class));
 annotationEngine.process(testClass.getClass(), testClass);
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockForSpyAnnotationWhenInstanceIsAlreadyAMock() throws IllegalAccessException {
 Object mock = Mockito.mock(Object.class);
 field.set(testClass, mock);
 annotationEngine.process(testClass.getClass(), testClass);
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockForSpyAnnotationWithUndesiredAnnotation() throws IllegalAccessException {
 Mockito.mock(Mock.class);
 field.setAnnotation(new MockAnnotation());
 annotationEngine.process(testClass.getClass(), testClass);
 }

 @Test
 public void testResetSpyInstance() throws IllegalAccessException {
 Object instance = new Object();
 field.set(testClass, instance);
 annotationEngine.process(testClass.getClass(), testClass);
 Mockito.reset(field.get(testClass));
 }

 @Test
 public void testProcessClassWithMultipleSpyAnnotations() throws IllegalAccessException {
 Object instance1 = new Object();
 Object instance2 = new Object();
 field.set(testClass, instance1);
 FieldUtils.setDeclaredField(testClass, "spiedField2", instance2);
 annotationEngine.process(testClass.getClass(), testClass);
 assertNotNull(Mockito.isSpy(field.get(testClass)));
 assertNotNull(Mockito.isSpy(FieldUtils.getDeclaredFieldValue(testClass, "spiedField2")));
 }

 @Test(expected = MockitoException.class)
 public void testProcessClassWithMockAnnotation() throws IllegalAccessException {
 field.setAnnotation(new MockAnnotation());
 annotationEngine.process(testClass.getClass(), testClass);
 }

 @Test(expected = MockitoException.class)
 public void testProcessClassWithCaptorAnnotation() throws IllegalAccessException {
 field.setAnnotation(new CaptorAnnotation());
 annotationEngine.process(testClass.getClass(), testClass);
 }

 @Test(expected = MockitoException.class)
 public void testProcessClassWithMultipleUndesiredAnnotations() throws IllegalAccessException {
 field.setAnnotation(new MockAnnotation());
 Mockito.mock(Captor.class);
 annotationEngine.process(testClass.getClass(), testClass);
 }

 @Test
 public void testProcessClassWithoutSpyAnnotation() throws IllegalAccessException {
 field.setAccessible(true);
 field.set(testClass, mock(Object.class));
 annotationEngine.process(testClass.getClass(), testClass);
 assertNull(field.get(testClass));
 }

 @Test(expected = MockitoException.class)
 public void testProcessClassWithNullTestClass() throws IllegalAccessException {
 annotationEngine.process(null, testClass);
 }

 @Test(expected = MockitoException.class)
 public void testProcessClassWithNullContext() throws IllegalAccessException {
 annotationEngine.process(testClass.getClass(), null);
 }
}

class TestUtils {
 static <T> Field getDeclaredField(Object obj, String fieldName) throws IllegalAccessException, NoSuchFieldException {
 Field field = obj.getClass().getDeclaredField(fieldName);
 field.setAccessible(true);
 return field;
 }
}

class MockAnnotation {
}

class CaptorAnnotation {
}