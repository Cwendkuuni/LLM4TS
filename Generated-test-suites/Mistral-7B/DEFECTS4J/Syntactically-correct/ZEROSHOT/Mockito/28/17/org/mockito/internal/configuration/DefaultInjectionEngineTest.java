package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine defaultInjectionEngine;
 private Object testClassInstance;
 private Set<Object> mocks;
 private Set<Field> injectMocksFields;

 @Before
 public void setUp() {
 mocks = new HashSet<>();
 testClassInstance = new Object() {};
 injectMocksFields = Arrays.asList(new Field[] { new Field() {}, new Field() {}, new Field() {}, new Field() {} }).stream().map(Field::getClass).map(Class::getDeclaredFields).flatMap(Field::stream).collect(Collectors.toSet());
 }

 @Test
 public void testInjectMocksOnFields() {
 defaultInjectionEngine = new DefaultInjectionEngine();
 defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

 // Test if all mocks were injected
 verify(mocks).size();

 // Test if all mocks were injected exactly once
 for (Object mock : mocks) {
 verify(mock).getClass().isInstance(defaultInjectionEngine.mockCandidateFilter.findMockCandidate(mock.getClass(), mocks, testClassInstance).get());
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testFailureWhenMultipleCandidatesForSameType() {
 defaultInjectionEngine = new DefaultInjectionEngine();
 // Add two mocks of the same type to the mocks set
 mocks.add(mock(Class1.class));
 mocks.add(mock(Class1.class));

 // Set up the test class instance with two fields of the same type annotated with @InjectMocks
 Field field1 = new Field() {}.getClass().getDeclaredField("field1");
 field1.setAccessible(true);
 field1.setAnnotation(MockitoAnnotations.InjectMocks.class);
 testClassInstance.getClass().getDeclaredField("field1").set(testClassInstance, field1);
 Field field2 = new Field() {}.getClass().getDeclaredField("field2");
 field2.setAccessible(true);
 field2.setAnnotation(MockitoAnnotations.InjectMocks.class);
 testClassInstance.getClass().getDeclaredField("field2").set(testClassInstance, field2);

 // Call injectMocksOnFields method
 defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 }

 @Test(expected = IllegalStateException.class)
 public void testFailureWhenNoMockCandidateForFieldType() {
 defaultInjectionEngine = new DefaultInjectionEngine();
 // Setup the test class instance with a field of an unsupported type annotated with @InjectMocks
 Field field = new Field() {}.getClass().getDeclaredField("field");
 field.setAccessible(true);
 field.setAnnotation(MockitoAnnotations.InjectMocks.class);
 testClassInstance.getClass().getDeclaredField("field").set(testClassInstance, field);

 // Call injectMocksOnFields method
 defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 }

 @Test
 public void testSuccessWhenNoMockCandidateForFieldTypeButFieldIsProvided() {
 defaultInjectionEngine = new DefaultInjectionEngine();
 // Setup the test class instance with a field of an unsupported type annotated with @InjectMocks but provide a dependency
 Field field = new Field() {}.getClass().getDeclaredField("field");
 field.setAccessible(true);
 field.setAnnotation(MockitoAnnotations.InjectMocks.class);
 testClassInstance.getClass().getDeclaredField("field").set(testClassInstance, field);
 Object dependency = mock(DependencyClass.class);
 testClassInstance.getClass().getDeclaredField("field").set(testClassInstance, dependency);

 // Call injectMocksOnFields method
 defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 }
}

class Field {
}

class Class1 {}
class DependencyClass {}