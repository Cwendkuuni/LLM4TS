package org.mockito.internal.configuration. DefaultInjectionEngineTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.BaseReflectionFailure;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.FieldInitializer;
import org.mockito.internal.util.reflection.FieldUtil;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.MockitoConfiguration;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine injectionEngine;
 private Field mock1Field;
 private Field mock2Field;
 private Field mock3Field;
 private Field nonMockField;
 private Object testObject;
 private Set<Field> injectMocksFields;
 private Set<Object> mocks;

 @Before
 public void setup() {
 injectionEngine = new DefaultInjectionEngine();
 testObject = new TestClass();
 mock1Field = Mockito.spy(TestClass.class).getDeclaredField("mock1");
 mock1Field.setAccessible(true);
 mock2Field = Mockito.spy(TestClass.class).getDeclaredField("mock2");
 mock2Field.setAccessible(true);
 mock3Field = Mockito.spy(TestClass.class).getDeclaredField("mock3");
 mock3Field.setAccessible(true);
 nonMockField = Mockito.spy(TestClass.class).getDeclaredField("nonMock");
 nonMockField.setAccessible(true);
 injectMocksFields = new HashSet<>(Arrays.asList(mock1Field, mock2Field, mock3Field));
 mocks = new HashSet<>();
 mocks.add(mock(MockType1.class));
 mocks.add(mock(MockType2.class));
 mocks.add(mock(MockType3.class));
 }

 @Test
 public void testInjectMocksOnFields() {
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testObject);

 // Verify mocks were injected in the correct order
 verify(mock1Field).set(testObject, mocks.iterator().next());
 verifyNoMoreInteractions(mocks.iterator().next());
 verify(mock2Field).set(testObject, mocks.iterator().next());
 verifyNoMoreInteractions(mocks.iterator().next());
 verify(mock3Field).set(testObject, mocks.iterator().next());
 verifyNoMoreInteractions(mocks.iterator().next());

 // Verify non-mock field was not injected
 verifyZeroInteractions(nonMockField);
 }

 @Test(expected = BaseReflectionFailure.class)
 public void testInjectMocksOnFieldsWithDuplicateTypes() {
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testObject);
 mocks.add(mock(MockType1.class)); // add duplicate mock
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testObject);
 }

 @Test(expected = BaseReflectionFailure.class)
 public void testInjectMocksOnFieldsWithNonExistingType() {
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testObject);
 mock(NonExistingType.class); // add non-existing mock type
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testObject);
 }

 @Test
 public void testInjectMockCandidate() {
 Class<?> awaitingInjectionClazz = TestClass.class;
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testObject);

 // Verify mocks were injected in the correct order
 verify(mock1Field).set(testObject, mocks.iterator().next());
 verifyNoMoreInteractions(mocks.iterator().next());
 verify(mock2Field).set(testObject, mocks.iterator().next());
 verifyNoMoreInteractions(mocks.iterator().next());
 verify(mock3Field).set(testObject, mocks.iterator().next());
 verifyNoMoreInteractions(mocks.iterator().next());
 }

 @Test
 public void testOrderedInstanceFieldsFrom() {
 Class<?> awaitingInjectionClazz = TestClass.class;
 Field[] orderedInstanceFields = injectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);

 // Verify fields are ordered from sub-type to super-type
 for (int i = 0; i < orderedInstanceFields.length - 1; i++) {
 Field field1 = orderedInstanceFields[i];
 Field field2 = orderedInstanceFields[i + 1];

 // Sub-type is before super-type
 assertTrue(field1.getType().isAssignableFrom(field2.getType()));
 }
 }
}

class TestClass {
 @InjectMocks
 private MockType1 mock1;
 @InjectMocks
 private MockType2 mock2;
 @InjectMocks
 private MockType3 mock3;
 private MockTypeNonInjected nonMock;
}

interface MockType1 {
}

interface MockType2 {
}

interface MockType3 {
}

interface MockTypeNonInjected {
}

class NonExistingType {
}