package org.mockito.internal.configuration.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Field;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine injectionEngine;
 private Set<Object> mocks;
 private Object testClassInstance;

 @Before
 public void setUp() {
 injectionEngine = new DefaultInjectionEngine();
 mocks = new HashSet<>();
 testClassInstance = new TestClass();
 initMocks(testClassInstance);
 }

 @Test
 public void testInjectMocksOnFieldsHierarchyWithOneMock() {
 // given
 Class<?> fieldClass = TestClassInner.class;
 Object fieldInstance = mock(fieldClass);
 mocks.add(fieldInstance);
 Field innerField = TestClass.class.getDeclaredField("inner");

 // when
 injectionEngine.injectMocksOnFields(Arrays.asList(innerField), mocks, testClassInstance);

 // then
 assertNotNull(Whitebox.getInternalState(testClassInstance, "inner"));
 assertEquals(fieldInstance, Whitebox.getInternalState(testClassInstance, "inner"));
 assertFalse(mocks.contains(fieldInstance));
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocksOnFieldsHierarchyWithMultipleMocks() {
 // given
 Class<?> fieldClass1 = TestClassInner1.class;
 Object fieldInstance1 = mock(fieldClass1);
 mocks.add(fieldInstance1);
 Class<?> fieldClass2 = TestClassInner2.class;
 Object fieldInstance2 = mock(fieldClass2);
 mocks.add(fieldInstance2);
 Field innerField = TestClass.class.getDeclaredField("inner");

 // when
 injectionEngine.injectMocksOnFields(Arrays.asList(innerField), mocks, testClassInstance);

 // then
 assertNotNull(Whitebox.getInternalState(testClassInstance, "inner"));
 // fail, user should provide dependencies
 }

 @Test
 public void testInjectMockCandidateByType() {
 // given
 Class<?> awaitingInjectionClazz = TestClassWithMockAsField.class;
 Object testClassInstance = new TestClassWithMockAsField();
 Object mock = mock(TestClassWithMockAsFieldInner.class);
 mocks.add(mock);
 Set<Object> mocksToBeInjected = new HashSet<>(mocks);
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocksToBeInjected, testClassInstance);

 // then
 assertNotNull(Whitebox.getInternalState(testClassInstance, "testField"));
 assertEquals(mock, Whitebox.getInternalState(testClassInstance, "testField"));
 assertFalse(mocksToBeInjected.contains(mock));
 }

 @Test(expected = MockitoException.class)
 public void testInjectMockCandidateByNameWithMultipleMocks() {
 // given
 Class<?> awaitingInjectionClazz = TestClassWithMockByName.class;
 Object testClassInstance = new TestClassWithMockByName();
 Object mock1 = mock(TestClassWithMockByNameInner1.class);
 Object mock2 = mock(TestClassWithMockByNameInner2.class);
 mocks.add(mock1);
 mocks.add(mock2);
 Set<Object> mocksToBeInjected = new HashSet<>(mocks);
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocksToBeInjected, testClassInstance);

 // then
 assertNotNull(Whitebox.getInternalState(testClassInstance, "testField"));
 // fail, user should provide dependencies
 }

 @Test
 public void testInjectMockCandidateByNameWithNoMocks() {
 // given
 Class<?> awaitingInjectionClazz = TestClassWithMockByName.class;
 Object testClassInstance = new TestClassWithMockByName();
 Set<Object> mocksToBeInjected = new HashSet<>();
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocksToBeInjected, testClassInstance);

 // then
 assertNull(Whitebox.getInternalState(testClassInstance, "testField"));
 }

 @Test
 public void testFailWhenMockCannotBeInjectedByTypeOrName() {
 // given
 Class<?> awaitingInjectionClazz = TestClassWithFailingMock.class;
 Object testClassInstance = new TestClassWithFailingMock();
 Set<Object> mocksToBeInjected = new HashSet<>();
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocksToBeInjected, testClassInstance);

 // then
 assertNull(Whitebox.getInternalState(testClassInstance, "testField"));
 }

 private static class TestClass {
 private TestClassInner inner;

 public TestClass() {}

 @InjectMocks
 public TestClassInner getInner() {
 return inner;
 }
 }

 private static class TestClassInner {
 // no annotations
 }

 private static class TestClassWithMockAsField {
 @InjectMocks
 private TestClassWithMockAsFieldInner testField;
 }

 private static class TestClassWithMockAsFieldInner {
 // no annotations
 }

 private static class TestClassWithMockByName {
 @InjectMocks
 private TestClassWithMockByNameInner testField;
 }

 private static class TestClassWithMockByNameInner1 {
 // with @Mock annotation
 }

 private static class TestClassWithMockByNameInner2 {
 // with @Mock annotation
 }

 private static class TestClassWithFailingMock {
 @InjectMocks
 private FailClass testField;
 }

 private static class FailClass {
 // cannot be mocked
 }
}