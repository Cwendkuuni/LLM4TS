package org.mockito.internal.configuration. DefaultInjectionEngineTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.mockito.internal.util.reflection.FieldAccessUtil.*;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine defaultInjectionEngine;
 private Set<Object> mocks;
 private Object testClassInstance;

 @Before
 public void setup() {
 mocks = new HashSet<>();
 testClassInstance = mock(TestClass.class);
 defaultInjectionEngine = new DefaultInjectionEngine();
 }

 @Test
 public void testInjectMocksOnFields() {
 Set<Field> injectMocksFields = getInjectMocksFields(TestClass.class);
 defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

 assertMockInjected(testClassInstance, "field1", "mock1");
 assertMockInjected(testClassInstance, "field2", "mock2");
 }

 private void assertMockInjected(Object testClassInstance, String fieldName, String mockName) {
 Object fieldValue = getFieldValue(testClassInstance, fieldName);
 assertNotNull(fieldValue);
 assertEquals(mock(mockName), fieldValue);
 }

 @Test(expected = MockitoException.class)
 public void testThrowsExceptionWhenCannotInitializeInjectedField() {
 Set<Field> injectMocksFields = getInjectMocksFields(TestClassWithException.class);
 defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 }

 @Test
 public void testDoesNotInjectDuplicateMock() {
 Set<Field> injectMocksFields = getInjectMocksFields(TestClassWithDuplicateMock.class);
 defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

 assertSize(mocks, 2);
 }

 @Test
 public void testInjectsMocksInHierarchy() {
 Set<Field> injectMocksFields = getInjectMocksFields(TestClassWithHierarchy.class);
 defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

 assertMockInjected(testClassInstance, "superClassField", "mock3");
 assertMockInjected(testClassInstance, "testClassField1", "mock4");
 assertMockInjected(testClassInstance, "testClassField2", "mock5");
 }

 @Test
 public void testDoesNotFailWhenNoMockCandidateFound() {
 Set<Field> injectMocksFields = getInjectMocksFields(TestClassWithNoMockCandidate.class);
 defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 }

 private Set<Field> getInjectMocksFields(Class<?> testClass) {
 Set<Field> fieldSet = new HashSet<>();
 for (Field field : testClass.getDeclaredFields()) {
 if (field.isAnnotationPresent(InjectMocks.class)) {
 fieldSet.add(field);
 }
 }
 return fieldSet;
}
}

class TestClass {
 
 @InjectMocks
 private Field1 field1;
 
 @InjectMocks
 private Field2 field2;
}

class TestClassWithException {
 
 @InjectMocks
 private ExceptionThrowingField exceptionThrowingField;
}

class TestClassWithDuplicateMock {
 
 @InjectMocks
 private Field1 field1;
 
 @InjectMocks
 @InjectMocks
 private Field1 field1Duplicate;
}

class TestClassWithHierarchy {
 
 @InjectMocks
 private SuperClass superClass;
 
 public class TestClass1 {
 @InjectMocks
 private Field1 field1;
 
 @InjectMocks
 private Field2 field2;
 }
 
 public class TestClass2 {
 @InjectMocks
 private Field1 field1;
 
 @InjectMocks
 private Field3 field3;
 }

 public class TestClass extends TestClass1 {}
}

class TestClassWithNoMockCandidate {
 
 @InjectMocks
 private UnsatisfiedField unsatisfiedField;
}

class ExceptionThrowingField {
 @javax.annotation.PostConstruct
 public void init() {
 throw new RuntimeException("Initialization failed.");
 }
}