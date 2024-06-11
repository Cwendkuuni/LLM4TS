package org.mockito.internal.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.exceptions.Reporter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

 private static final String FIELD_NAME = "testField";

 @Mock
 private Object testClass;

 @Spy
 private Object spiedObject;

 @Test
 public void testCreateMockFor() {
     // The createMockFor method is empty, so no tests are needed for it
 }

 @Test(expected = MockitoException.class)
 public void testProcess_MissingInstance() {
     SpyAnnotationEngine engine = new SpyAnnotationEngine();
     engine.process(SpyAnnotationEngine.class, testClass);

     // The testClass field is null, which should throw an exception
     Field field = spy(SpyAnnotationEngine.class).getDeclaredField(FIELD_NAME);
     assertNull(field.get(testClass));

     engine.process(SpyAnnotationEngine.class, testClass);
 }

 @Test(expected = MockitoException.class)
 public void testProcess_AlreadySpyed() {
     SpyAnnotationEngine engine = new SpyAnnotationEngine();

     // Set the field value to a mocked object
     Object mock = mock(Object.class);
     Field field = spy(SpyAnnotationEngine.class).getDeclaredField(FIELD_NAME);
     field.setAccessible(true);
     field.set(testClass, mock);

     engine.process(SpyAnnotationEngine.class, testClass);

     assertNotNull(field.get(testClass));
     assertEquals(mock, field.get(testClass));

     // Try to spy the same object again
     engine.process(SpyAnnotationEngine.class, testClass);
 }

 @Test(expected = MockitoException.class)
 public void testProcess_InvalidAnnotationCombination() {
     SpyAnnotationEngine engine = new SpyAnnotationEngine();

     // Set the field value to an object annotated with Mockito's Mock annotation
     Object mock = mock(Object.class);
     Field field = spy(SpyAnnotationEngine.class).getDeclaredField(FIELD_NAME);
     field.setAccessible(true);
     field.set(testClass, mock);

     engine.process(SpyAnnotationEngine.class, testClass);

     // Set the field value to the same object, but now annotated with Spy
     Object spy = spy(mock);
     field.set(testClass, spy);

     engine.process(SpyAnnotationEngine.class, testClass);
 }

 @Test
 public void testProcess() {
     SpyAnnotationEngine engine = new SpyAnnotationEngine();

     Object originalObject = new Object();
     Field field = spy(SpyAnnotationEngine.class).getDeclaredField(FIELD_NAME);
     field.setAccessible(true);
     field.set(testClass, originalObject);

     engine.process(SpyAnnotationEngine.class, testClass);

     // The object should be spied, but not mocked
     assertSame(originalObject, field.get(testClass));
     assertFalse(isMock(field.get(testClass)));
     assertFalse(field.get(testClass).getClass().isAnnotationPresent(Spy.class));

     // Now let's spy the spied object
     ArgumentCaptor<Object> captor = argumentCaptor(Object.class);
     Object spiedObject = spy(field.get(testClass));
     verify(engine, times(1)).process(SpyAnnotationEngine.class, testClass);
     verify(engine, times(1)).process(SpyAnnotationEngine.class, any(Class.class));
     verify(engine, times(1)).process(SpyAnnotationEngine.class, eq(testClass.getClass()));
     verify(engine, times(1)).process(any(Field.class), eq(field));
     verifyZeroInteractions(engine);

     field.set(testClass, spiedObject);

     engine.process(SpyAnnotationEngine.class, testClass);

     // The object should now be spied
     assertSame(spiedObject, field.get(testClass));
     assertTrue(isMock(field.get(testClass).getClass()));
     assertTrue(field.get(testClass).getClass().isAnnotationPresent(Spy.class));
 }

 private boolean isMock(Object object) {
     try {
         Class<?> clazz = Class.forName("org.mockito.Mockito$1");
         return clazz.isInstance(Mockito.mock(Object.class).getClass());
     } catch (ClassNotFoundException e) {
         throw new RuntimeException("Mockito not found", e);
     }
 }
}