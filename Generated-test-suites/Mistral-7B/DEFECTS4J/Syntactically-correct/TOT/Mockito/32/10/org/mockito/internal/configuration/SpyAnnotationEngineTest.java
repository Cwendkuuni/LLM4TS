package org.mockito.internal.configuration.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.configuration.annotation.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine engine;
 private Object testClass;

 @Before
 public void setup() {
 testClass = new Object() {};
 engine = new SpyAnnotationEngine();
 }

 @Test
 public void testCreateMockFor() {
 // Test error scenario: Invoking createMockFor with annotation and field when class is not annotated with AnnotationEngine
 assertEquals(null, engine.createMockFor(null, null));

 // Test error scenario: Invoking createMockFor with null annotation
 assertEquals(null, engine.createMockFor(null, new Field(SpyAnnotationEngine.class, "testField") {}));

 // Test error scenario: Invoking createMockFor with null field
 assertEquals(null, engine.createMockFor(new Annotation() {}, null));

 // Test normal scenario: Invoking createMockFor with valid annotation and field
 @Spy
 Object spyObject = new Object() {};
 Field field = new Field(testClass.getClass(), "testField") {};
 engine.process(testClass.getClass(), testClass);
 Object result = engine.createMockFor(Spy.class, field);
 assertNotNull(result);
 assertEquals(spyObject, result);
 }

 @Test
 public void testProcessWithMock() {
 // Test scenario: Invoking process with a field that is already a mock
 @Mock List mockList = mock(List.class);
 Field field = new Field(testClass.getClass(), "testField") {};
 field.set(testClass, mockList);
 engine.process(testClass.getClass(), testClass);
 assertNotNull(mockList);
 }

 @Test
 public void testProcessWithSpy() {
 // Test scenario: Invoking process with a null field instance
 engine.process(testClass.getClass(), testClass);
 Field field = new Field(testClass.getClass(), "testField") {};
 assertNull(field.get(testClass));

 // Test scenario: Invoking process with a non-null field instance
 Object target = new ArrayList<>();
 Field targetField = new Field(testClass.getClass(), "testField") {};
 targetField.set(testClass, target);
 engine.process(testClass.getClass(), testClass);
 Object spiedTarget = engine.createMockFor(Spy.class, targetField);
 assertNotNull(spiedTarget);
 assertEquals(target, spiedTarget);
 }

 @Test(expected = MockitoException.class)
 public void testProcessWithMissingInstance() {
 // Test error scenario: Invoking process with a missing field instance
 engine.process(testClass.getClass(), testClass);
 Field field = new Field(testClass.getClass(), "testField") {};
 engine.createMockFor(Spy.class, field);
 }
}