package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine engine;

 @Mock
 private Object testClass;

 @Spy
 private Object spiedInstance;

 @Before
 public void setUp() {
 engine = new SpyAnnotationEngine();
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testCreateMockFor() {
 assertNull(engine.createMockFor(Mockito.mock(Annotation.class), null));
 }

 @Test(expected = MockitoException.class)
 public void testProcess_MissingInstance() {
 engine.process(SpyAnnotationEngine.class, testClass);
 engine.process(SpyAnnotationEngine.class, testClass);
 new MockitoException("Cannot create a @Spy for 'fieldName' field because the *instance* is missing\n" +
         "The instance must be created *before* initMocks();\n" +
         "Example of correct usage of @Spy:\n" +
         "   @Spy List mock = new LinkedList<>();\n" +
         "   //also, don't forget about MockitoAnnotations.initMocks();");
 }

 @Test
 public void testProcess_SpyFieldAlreadyMocked() {
 Object originalInstance = mock(Object.class);
 engine.process(SpyAnnotationEngine.class, testClass);
 engine.process(SpyAnnotationEngine.class, testClass);
 engine.process(SpyAnnotationEngine.class, testClass);

 Field field = null;
 try {
 field = testClass.getClass().getDeclaredField("fieldName");
 field.setAccessible(true);
 } catch (NoSuchFieldException e) {
 throw new RuntimeException("Field 'fieldName' not found", e);
 }

 Object fieldInstance = null;
 try {
 fieldInstance = field.get(testClass);
 } catch (IllegalAccessException e) {
 throw new RuntimeException("Failed to get 'fieldName' instance", e);
 }

 Mockito.verify(fieldInstance). equals(originalInstance); // verify mocked instance
 field.set(testClass, spiedInstance);

 engine.process(SpyAnnotationEngine.class, testClass);

 Mockito.reset(fieldInstance);
 Mockito.verify(fieldInstance). equals(spiedInstance); // verify spied instance
 }

 @Test(expected = MockitoException.class)
 public void testProcess_SpyFieldAlreadySpyed() {
 engine.process(SpyAnnotationEngine.class, testClass);
 engine.process(SpyAnnotationEngine.class, testClass);
 engine.process(SpyAnnotationEngine.class, testClass);

 Field field = null;
 try {
 field = testClass.getClass().getDeclaredField("fieldName");
 field.setAccessible(true);
 } catch (NoSuchFieldException e) {
 throw new RuntimeException("Field 'fieldName' not found", e);
 }

 Object fieldInstance = null;
 try {
 fieldInstance = field.get(testClass);
 } catch (IllegalAccessException e) {
 throw new RuntimeException("Failed to get 'fieldName' instance", e);
 }

 Mockito.spyInPlace(fieldInstance);

 engine.process(SpyAnnotationEngine.class, testClass);

 verify(engine).assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
 }

 @Test
 public void testProcess_SpyFieldAccessibility() {
 engine.process(SpyAnnotationEngine.class, testClass);
 engine.process(SpyAnnotationEngine.class, testClass);
 engine.process(SpyAnnotationEngine.class, testClass);

 Field field = null;
 try {
 field = testClass.getClass().getDeclaredField("fieldName");
 field.setAccessible(true);
 } catch (NoSuchFieldException e) {
 throw new RuntimeException("Field 'fieldName' not found", e);
 }

 Object fieldInstance = null;
 try {
 fieldInstance = field.get(testClass);
 } catch (IllegalAccessException e) {
 throw new RuntimeException("Failed to get 'fieldName' instance", e);
 }

 Mockito.verifyZeroInteractions(fieldInstance);
 field.setAccessible(false);
 engine.process(SpyAnnotationEngine.class, testClass);

 Mockito.verifyNoMoreInteractions(fieldInstance);
 }

 @Test
 public void testProcess_MultipleFields() {
 engine.process(SpyAnnotationEngine.class, testClass);
 engine.process(SpyAnnotationEngine.class, testClass);
 engine.process(SpyAnnotationEngine.class, testClass);

 Field field1 = null;
 try {
 field1 = testClass.getClass().getDeclaredField("fieldName1");
 field1.setAccessible(true);
 } catch (NoSuchFieldException e) {
 throw new RuntimeException("Field 'fieldName1' not found", e);
 }
 Object fieldInstance1 = null;
 try {
 fieldInstance1 = field1.get(testClass);
 } catch (IllegalAccessException e) {
 throw new RuntimeException("Failed to get 'fieldName1' instance", e);
 }

 Field field2 = null;
 try {
 field2 = testClass.getClass().getDeclaredField("fieldName2");
 field2.setAccessible(true);
 } catch (NoSuchFieldException e) {
 throw new RuntimeException("Field 'fieldName2' not found", e);
 }
 Object fieldInstance2 = null;
 try {
 fieldInstance2 = field2.get(testClass);
 } catch (IllegalAccessException e) {
 throw new RuntimeException("Failed to get 'fieldName2' instance", e);
 }

 ArgumentCaptor<Field> fieldCaptor = ArgumentCaptor.forClass(Field.class);
 ArgumentCaptor<Object> instanceCaptor = ArgumentCaptor.forClass(Object.class);

 verify(engine).process(eq(SpyAnnotationEngine.class), eq(testClass));
 verify(engine).process(eq(SpyAnnotationEngine.class), eq(testClass));
 verify(engine).process(eq(SpyAnnotationEngine.class), eq(testClass));
 verify(engine).assertNoAnnotations(eq(Spy.class), eq(field1), anyClass(), anyClass(), anyClass());
 verify(engine).assertNoAnnotations(eq(Spy.class), eq(field2), anyClass(), anyClass(), anyClass());
 verify(engine).process(eq(SpyAnnotationEngine.class), eq(testClass), argThat(fieldCaptor.capture()));
 verify(engine).process(eq(SpyAnnotationEngine.class), eq(testClass), argThat(fieldCaptor.capture()));
 verify(engine).process(eq(SpyAnnotationEngine.class), eq(testClass), argThat(fieldCaptor.capture()));

 Field firstField = fieldCaptor.getValue();
 assertEquals("fieldName1", firstField.getName());
 verifyZeroInteractions(fieldInstance1);

 Field secondField = fieldCaptor.getValue();
 assertEquals("fieldName2", secondField.getName());
 verifyNoMoreInteractions(fieldInstance2);
 }
}