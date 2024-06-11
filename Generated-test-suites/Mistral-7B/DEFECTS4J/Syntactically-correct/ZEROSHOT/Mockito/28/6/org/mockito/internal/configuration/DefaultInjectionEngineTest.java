package org.mockito.internal.configuration.DefaultInjectionEngine;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.junit.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine defaultInjectionEngine;
 private Set<Field> injectMocksFields;
 private Set<Object> mocks;
 private Object testClassInstance;

 @Before
 public void setup() {
 defaultInjectionEngine = new DefaultInjectionEngine();
 injectMocksFields = new HashSet<>();
 mocks = new HashSet<>();
 testClassInstance = new Object() {};
 }

 @After
 public void teardown() {
 for (Object mock : mocks) {
 Mockito.unmock(mock);
 }
 mocks.clear();
 }

 @Test
 public void testInjectMocksOnFields() {
 // given
 initMocks(testClassInstance);
 mocks.add(Mockito.mock(String.class));
 mocks.add(Mockito.mock(Integer.class));

 // when
 defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

 // then
 verify(testClassInstance, times(1)).getClass();

 // check if mocks were injected
 for (Field field : injectMocksFields) {
 Class<?> fieldType = field.getType();
 Object fieldInstance = null;
 try {
 fieldInstance = new FieldInitializer(testClassInstance, field).initialize();
 } catch (MockitoException e) {
 // mock injection failed, skip field
 continue;
 }

 verify(fieldInstance, times(1)).getClass();
 assertNotSame(mocks.iterator().next(), fieldInstance);
 mocks.remove(fieldInstance);
 }

 // check that all mocks were injected
 assertEquals(0, mocks.size());
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocksOnFieldsWithDuplicateMocks() {
 // given
 initMocks(testClassInstance);
 mocks.add(Mockito.mock(String.class));
 mocks.add(Mockito.mock(String.class));

 // when
 try {
 defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 } catch (MockitoException e) {
 throw e;
 }
 }

 @Test
 public void testInjectMockCandidate() {
 // given
 Class<?> awaitingInjectionClazz = String.class;
 Field mockField = Mockito.spy(awaitingInjectionClazz).getClass().getDeclaredField("mockField");
 Object fieldInstance = Mockito.mock(Integer.class);
 Set<Object> mocksToBeInjected = new HashSet<>(mocks);

 // when
 defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocksToBeInjected, fieldInstance);

 // then
 assertEquals(1, mocksToBeInjected.size());
 verify(mockField, times(1)).set(testClassInstance, eq(fieldInstance));
 }

 @Test
 public void testOrderedInstanceFieldsFrom() {
 // given
 Class<?> awaitingInjectionClazz = String.class;
 Field field1 = new Field() {{
 setAccessible(true);
 setName("field1");
 setType(Integer.class);
 }};
 Field field2 = new Field() {{
 setAccessible(true);
 setName("field2");
 setType(String.class);
 }};
 Field field3 = new Field() {{
 setAccessible(true);
 setName("field3");
 setType(awaitingInjectionClazz);
 }};
 Field[] fields = {field3, field2, field1};

 // when
 Field[] orderedFields = defaultInjectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);

 // then
 assertArrayEquals(new Object[]{field3, field2, field1}, orderedFields);
 }
}