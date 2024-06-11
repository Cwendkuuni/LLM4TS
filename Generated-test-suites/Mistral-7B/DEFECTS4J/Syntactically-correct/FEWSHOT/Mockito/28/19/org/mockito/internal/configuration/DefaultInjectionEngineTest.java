package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Arrays;import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine injectionEngine;
 private Reporter reporter;
 private Set<Object> mocks;
 private Object testClassInstance;
 private static final Class<?> CLASS_WITH_MOCKED_FIELDS = TestClassWithMockedFields.class;

 @Before
 public void setup() {
 injectionEngine = new DefaultInjectionEngine();
 reporter = mock(Reporter.class);
 mocks = new HashSet<>();
 testClassInstance = new TestClassWithMockedFields();
 }

 @Test
 public void testInjectMocksOnFields() {
 Set<Field> injectMocksFields = Arrays.asList(TestClassWithMockedFields.class.getDeclaredFields())
 .stream()
 .filter(Field::isAnnotationPresent)
 .map(field -> (Field) field.getAnnotation(InjectMocks.class))
 .collect(Collectors.toUnmodifiableSet());
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

 assertFalse(injectMocksFields.isEmpty());

 for (Field field : injectMocksFields) {
 Object fieldInstance = null;
 try {
 fieldInstance = new FieldInitializer(testClassInstance, field).initialize();
 } catch (MockitoException e) {
 // ignore exception, as it is handled by DefaultInjectionEngine
 }
 assertNotNull(fieldInstance);

 Class<?> fieldClass = fieldInstance.getClass();
 while (fieldClass != Object.class) {
 Field mockField = findMockCandidate(mocks, fieldClass, fieldInstance.getClass().getName());
 if (mockField != null) {
 assertEquals(fieldInstance, Whitebox.getInternalState(testClassInstance, field));
 break;
 }
 fieldClass = fieldClass.getSuperclass();
 }
 }
 }

 @Test(expected = MockitoException.class)
 public void testCannotInitializeFieldAnnotatedWithInjectMocks() {
 Set<Field> injectMocksFields = Arrays.asList(TestClassWithMockedFields.class.getDeclaredFields())
 .stream()
 .filter(Field::isAnnotationPresent)
 .map(field -> (Field) field.getAnnotation(InjectMocks.class))
 .collect(Collectors.toUnmodifiableSet());
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

 Field fieldWithException = TestClassWithMockedFields.class.getDeclaredField("fieldWithException");
 fieldWithException.setAccessible(true);
 try {
 new FieldInitializer(testClassInstance, fieldWithException).initialize();
 } catch (IllegalAccessException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testEmptyMocksSet() {
 injectionEngine.injectMocksOnFields(Arrays.emptySet(), mocks, testClassInstance);
 assertFalse(mocks.isEmpty());
 }

 @Test
 public void testMockCandidateFilter() {
 Class<?> awaitingInjectionClazz = TestClassWithMockedFields.class;
 Set<Object> mocksCopy = new HashSet<>(mocks);

 Class<?> fieldType = String.class;
 Field mockField = findMockCandidate(mocksCopy, awaitingInjectionClazz, fieldType.getName());
 assertSame(mockField, mockCandidateFilter(mocksCopy, awaitingInjectionClazz.getField("fieldWithString"), fieldType, new NameBasedCandidateFilter()));
 assertSame(mockField, mockCandidateFilter(mocksCopy, awaitingInjectionClazz.getField("fieldWithString"), fieldType, new TypeBasedCandidateFilter(new FinalMockCandidateFilter())));
 assertSame(mockField, mockCandidateFilter(mocksCopy, awaitingInjectionClazz.getField("fieldWithString"), fieldType, new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()))));
 assertNull(mockCandidateFilter(mocksCopy, awaitingInjectionClazz, new Object().getClass()));
 assertNull(mockCandidateFilter(mocksCopy, awaitingInjectionClazz, null));
 assertNull(mockCandidateFilter(mocksCopy, awaitingInjectionClazz, String[].class));
 }

 private Field mockCandidateFilter(Set<Object> mocks, Field field, Class<?> fieldType, MockCandidateFilter mockCandidateFilter) {
 return mockCandidateFilter.filterCandidate(mocks, field, fieldType).getMock();
 }

 private Field findMockCandidate(Set<Object> mocks, Class<?> awaitingInjectionClazz, String fieldName) {
 return Arrays.stream(awaitingInjectionClazz.getDeclaredFields())
 .filter(field -> field.getName().equals(fieldName))
 .map(field -> injectionEngine.mockCandidateFilter(mocks, field, field.getType()).getMock())
 .findFirst()
 .orElse(null);
 }
}

class TestClassWithMockedFields {
 @InjectMocks
 private MockA mockA;
 private MockB fieldWithString;
 private MockC fieldWithException;

 public MockA getMockA() {
 return mockA;
 }

 public MockB getFieldWithString() {
 return fieldWithString;
 }

 public MockC getFieldWithException() {
 return fieldWithException;
 }
}

class MockA {
}

class MockB {
}

class MockC {
}