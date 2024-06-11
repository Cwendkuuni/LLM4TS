package org.mockito.internal.configuration.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine injectionEngine;
 private Object testClassInstance;
 private Set<Field> injectMocksFields;
 private Set<Object> mocks;

 @Before
 public void setUp() {
 injectionEngine = new DefaultInjectionEngine();
 testClassInstance = new TestClass();
 injectMocksFields = Arrays.asList(TestClass.class.getDeclaredFields())
 .stream()
 .filter(field -> field.isAnnotationPresent(org.mockito.InjectMocks.class))
 .collect(Collectors.toSet());
 mocks = new HashSet<>();
 }

 @Test
 public void testInjectMocksOnEmptyFields() {
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 assertTrue(mocks.isEmpty());
 }

 @Test
 public void testInjectMocksOnSingleField() throws IllegalAccessException {
 Object mock = mock(SingleMock.class);
 mocks.add(mock);
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 Field injectMocksField = TestClass.class.getDeclaredField("injectMocksField");
 injectMocksField.setAccessible(true);
 assertNotNull(injectMocksField.get(testClassInstance));
 assertEquals(mock, injectMocksField.get(testClassInstance));
 }

 @Test
 public void testInjectMocksOnMultipleFieldsWithSameType() throws IllegalAccessException {
 Object mock = mock(SameTypeMock.class);
 mocks.add(mock);
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 Field field1 = TestClass.class.getDeclaredField("field1");
 Field field2 = TestClass.class.getDeclaredField("field2");
 assertNotNull(field1.get(testClassInstance));
 assertNotNull(field2.get(testClassInstance));
 assertEquals(mock, field1.get(testClassInstance));
 assertEquals(mock, field2.get(testClassInstance));
 }

 @Test
 public void testInjectMocksOnMultipleFieldsWithDifferentTypes() throws IllegalAccessException {
 Object mock1 = mock(DifferentTypeMock1.class);
 Object mock2 = mock(DifferentTypeMock2.class);
 mocks.add(mock1);
 mocks.add(mock2);
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 Field field1 = TestClass.class.getDeclaredField("field1");
 Field field2 = TestClass.class.getDeclaredField("field2");
 assertNotNull(field1.get(testClassInstance));
 assertNotNull(field2.get(testClassInstance));
 assertNotSame(mock1, field2.get(testClassInstance));
 assertNotSame(mock2, field1.get(testClassInstance));
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocksOnFieldsWithNoMockCandidate() throws IllegalAccessException {
 injectionEngine.injectMocksOnFields(Arrays.asList(TestClassNoMock.class.getDeclaredField("field1")), mocks, testClassInstance);
 }

 @Test
 public void testInjectMocksWithSpy() throws IllegalAccessException {
 Object spy = spy(Spy.class);
 mocks.add(spy);
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 Field injectMocksField = TestClass.class.getDeclaredField("injectMocksField");
 injectMocksField.setAccessible(true);
 assertNotNull(injectMocksField.get(testClassInstance));
 assertTrue(InjectMocks.class.isAssignableFrom(injectMocksField.getType()));
 assertEquals(spy, injectMocksField.get(testClassInstance));
 }

 @Test
 public void testInjectMocksWithNullParameter() {
 try {
 injectionEngine.injectMocksOnFields(null, mocks, testClassInstance);
 } catch (NullPointerException e) {
 // expected
 }
 }
}

class TestClass {
 @InjectMocks
 private SingleMock injectMocksField;
 private SingleMock field1;
 private SameTypeMock field2;
 private DifferentTypeMock1 field3;
 private DifferentTypeMock2 field4;
 private TestClassNoMock noMockField;

 static class SingleMock {}
 static class SameTypeMock extends SingleMock {}
 static class DifferentTypeMock1 {}
 static class DifferentTypeMock2 {}
 static class TestClassNoMock {}
}

class TestClassNoMock {
 @InjectMocks
 private TestClassNoMock noMockField;
}

class Spy {
 @InjectMocks
 private Spy injectMocksField;
}