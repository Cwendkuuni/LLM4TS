package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.BaseMockitoException;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine injectionEngine;
 private Set<Object> mocks;
 private Object testClassInstance;

 @Before
 public void setUp() {
 injectionEngine = new DefaultInjectionEngine();
 mocks = new HashSet<>();
 testClassInstance = mock(TestClass.class);
 }

 @Test
 public void testInjectMocksOnFields() {
 // Given
 Set<Field> injectMocksFields = Arrays.asList(
 new FieldTestClass1().getDeclaredFields(),
 new FieldTestClass2().getDeclaredFields()
 ).stream().filter(f -> f.isAnnotationPresent(InjectMocks.class)).collect(Collectors.toUnmodifiableSet());
 mocks.add(mock(Dependency1.class));
 mocks.add(mock(Dependency2.class));

 // When
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

 // Then
 verify(testClassInstance).setDependency1(mocks.iterator().next());
 verify(testClassInstance).setDependency2(mocks.iterator().next());

 // Test error scenario when injection fails due to no candidate found
 Field fieldWithoutCandidate = FieldTestClass2.class.getDeclaredField("nonExistentField");
 fieldWithoutCandidate.setAccessible(true);
 mocks.clear();
 mocks.add(mock(Dependency1.class));
 mocks.add(mock(Dependency2.class));
 injectionEngine.injectMocksOnFields(Collections.singleton(fieldWithoutCandidate), mocks, testClassInstance);
 Whitebox.getInternalState(injectionEngine, "reporter").reportErrorException(new BaseMockitoException("Error injecting mock"));
 // Verify error report was shown
 verify(testClassInstance, Mockito.never()).setDependency1(Mockito.any());
 verify(testClassInstance, Mockito.never()).setDependency2(Mockito.any());
 }

 private static class FieldTestClass1 {
 @InjectMocks private Dependency1 dependency1;
 }

 private static class FieldTestClass2 {
 @InjectMocks private Dependency1 dependency1;
 @InjectMocks private Dependency2 dependency2;
 }

 private static class Dependency1 {}
 private static class Dependency2 {}
}