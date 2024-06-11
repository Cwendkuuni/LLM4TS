package org.mockito.internal.configuration.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.stubs.MockSettings;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine injectionEngine;
 private Object testClassInstance;
 private Set<Object> mocks;
 private Set<Field> injectMocksFields;

 @Before
 public void setup() {
 injectionEngine = new DefaultInjectionEngine();
 testClassInstance = new TestClass();
 mocks = new HashSet<>();
 injectMocksFields = Arrays.asList(TestClass.class.getDeclaredFields())
 .stream()
 .filter(field -> field.isAnnotationPresent(InjectMocks.class))
 .collect(Collectors.toSet());
 }

 @Test
 public void testInjectMocksOnFields() {
 // given
 Object mock1 = Mockito.mock(MockType1.class);
 Object mock2 = Mockito.mock(MockType2.class);
 mocks.add(mock1);
 mocks.add(mock2);

 // when
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

 // then
 for (Field field : injectMocksFields) {
 Object injectedMock = field.get(testClassInstance);
 assertNotNull(injectedMock);
 assertEquals(mock1, injectedMock); // first injected mock should be the one with the closest type match
 }
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocksOnFieldsWithMultipleCandidates() {
 // given
 Object mock1 = Mockito.mock(MockType1.class);
 Object mock2 = Mockito.mock(MockType1.class);
 mocks.add(mock1);
 mocks.add(mock2);

 // when
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

 // then
 // MockitoException should be thrown since there are multiple candidate mocks
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocksOnFieldsWithNoCandidates() {
 // given
 Set<Object> emptyMocks = new HashSet<>();

 // when
 injectionEngine.injectMocksOnFields(injectMocksFields, emptyMocks, testClassInstance);

 // then
 // MockitoException should be thrown since there are no candidate mocks
 }

 @Test
 public void testInjectMockFieldWithPropertySetter() {
 // given
 Object mock = Mockito.mock(MockType.class);
 TestClass testClass = new TestClass();
 testClass.setMock(mock);
 mocks.add(mock);

 // when
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClass);

 // then
 Object injectedMock = testClass.getMock();
 assertEquals(mock, injectedMock);
 }

 @Test
 public void testInjectMockFieldWithFieldInjection() {
 // given
 Object mock = Mockito.mock(MockType.class);
 TestClass testClass = new TestClass();
 testClass.setMock(null);
 Field mockField = TestClass.class.getDeclaredField("mock");
 mockField.setAccessible(true);
 mockField.set(testClass, mock);
 mocks.add(mock);

 // when
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClass);

 // then
 Object injectedMock = testClass.getMock();
 assertEquals(mock, injectedMock);
 }

 @Test(expected = MockitoException.class)
 public void testInjectMockFieldWithNoSetterOrField() {
 // given
 Object mock = Mockito.mock(MockType.class);
 TestClass testClass = new TestClass();
 mocks.add(mock);

 // when
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClass);

 // then
 // MockitoException should be thrown since there is no setter or field to set the mock
 }

 @Test
 public void testInjectMockCandidateByType() {
 // given
 TestClass testClass = new TestClass();
 Object mock1 = Mockito.mock(MockType1.class);
 Object mock2 = Mockito.mock(MockType2.class);
 mocks.add(mock1);
 mocks.add(mock2);

 // when
 Class<?> awaitingInjectionClazz = TestClass.class;
 Field mockField = null;
 do {
 mockField = awaitingInjectionClazz.getDeclaredField("mockField");
 awaitingInjectionClazz = awaitingInjectionClazz.getSuperclass();
 } while (mockField == null && awaitingInjectionClazz != Object.class);

 if (mockField != null) {
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testClass);
 verify(mock1, times(1)).verifyNoMoreInteractions(); // verify that mock1 was injected
 }
 }

 @Test
 public void testInjectMockCandidateByName() {
 // given
 TestClass testClass = new TestClass();
 Object mock1 = Mockito.mock(MockType1.class);
 Object mock2 = Mockito.mock(MockType2.class);
 mocks.add(mock1);
 mocks.add(mock2);

 // when
 Class<?> awaitingInjectionClazz = TestClass.class;
 Field mockField1 = null, mockField2 = null;
 do {
 mockField1 = findMockFieldByName("mockField1", awaitingInjectionClazz);
 mockField2 = findMockFieldByName("mockField2", awaitingInjectionClazz);
 awaitingInjectionClazz = awaitingInjectionClazz.getSuperclass();
 } while (mockField1 == null && mockField2 == null && awaitingInjectionClazz != Object.class);

 if (mockField1 != null) {
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testClass);
 verify(mock1, times(1)).verifyNoMoreInteractions(); // verify that mock1 was injected
 }

 if (mockField2 != null) {
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testClass);
 verify(mock2, times(1)).verifyNoMoreInteractions(); // verify that mock2 was injected
 }
 }

 private Field findMockFieldByName(String fieldName, Class<?> clazz) {
 Field[] fields = clazz.getDeclaredFields();
 for (Field field : fields) {
 if (field.isAnnotationPresent(MockSettings.class) && field.getName().equals(fieldName)) {
 return field;
 }
 }
 return null;
 }
}

class TestClass {
 @InjectMocks
 private MockType mock;
 private MockType mockField;
 private MockType2 mockField2;
 private MockType3 mockField3;

 // getters and setters
}

interface MockType {}
interface MockType1 extends MockType {}
interface MockType2 extends MockType {}
interface MockType3 extends MockType {}