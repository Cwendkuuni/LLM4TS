package org.mockito.internal.configuration.injection.test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.mock.MockType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Collection<Object> mocks;
 private Object fieldInstance;
 private Field field;

 @Rule
 public MockitoRule mockitoRule = MockitoJUnit.rule();

 @Before
 public void setUp() throws Exception {
 mocks = Arrays.asList(mock(MockedClass1.class, MockType.FIELD), mock(MockedClass2.class, MockType.FIELD));
 fieldInstance = new FakeClass();
 field = FieldReflectionUtils.getField(FakeClass.class, "field");
 filter = new FinalMockCandidateFilter();
 }

 @Test
 public void testFilterCandidate_SingleMock_PropertySetterSuccess() {
 Object mock = mocks.iterator().next();
 final Object expectedMock = mock;

 when(field.isAccessible()).thenReturn(true);
 when(fieldSetter.set(fieldInstance, field, expectedMock)).thenReturn(null);

 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertSame(expectedMock, injecter.getInjected());
 }

 @Test(expected = MockitoException.class)
 public void testFilterCandidate_SingleMock_PropertySetterFailure() {
 Object mock = mocks.iterator().next();
 when(field.isAccessible()).thenReturn(true);
 when(fieldSetter.set(fieldInstance, field, mock)).thenThrow(new IllegalAccessException());

 filter.filterCandidate(mocks, field, fieldInstance);
 }

 @Test
 public void testFilterCandidate_MultipleMocks_NoInject() {
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertFalse(injecter.thenInject());
 assertNull(injecter.getInjected());
 }

 @Test
 public void testFilterCandidate_SingleMock_FieldAccessSuccess() {
 Object mock = mocks.iterator().next();
 Object expectedMock = mock;
 when(field.isAccessible()).thenReturn(true);
 when(field.get(fieldInstance)).thenReturn(null);
 when(fieldSetter.set(fieldInstance, field, expectedMock)).thenReturn(null);
 when(field.set(fieldInstance, expectedMock)).thenReturn(null);

 filter.filterCandidate(mocks, field, fieldInstance);
 assertSame(expectedMock, filter.getLastInjected());
 }

 @Test(expected = MockitoException.class)
 public void testFilterCandidate_SingleMock_FieldAccessFailure() {
 Object mock = mocks.iterator().next();
 when(field.isAccessible()).thenReturn(true);
 when(field.get(fieldInstance)).thenReturn(null);
 when(fieldSetter.set(fieldInstance, field, mock)).thenReturn(null);
 when(field.set(fieldInstance, mock)).thenThrow(new IllegalAccessException());

 filter.filterCandidate(mocks, field, fieldInstance);
 }

 @Test
 public void testFilterCandidate_MultipleMocks_InjectFirst() {
 Object firstMock = mock(MockedClass1.class, MockType.FIELD);
 Object secondMock = mock(MockedClass2.class, MockType.FIELD);
 Object expectedFirstMock = firstMock;
 Object expectedSecondMock = secondMock;

 when(field.isAccessible()).thenReturn(true);
 when(fieldSetter.set(fieldInstance, field, expectedFirstMock)).thenReturn(null);
 when(fieldSetter.set(fieldInstance, field, expectedSecondMock)).thenThrow(new IllegalAccessException());

 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertSame(expectedFirstMock, injecter.getInjected());
 }
}

class MockedClass1 {}
class MockedClass2 {}
class FakeClass {
 private Object field;

 public Object getField() {
 return field;
 }

 public void setField(Object field) {
 this.field = field;
 }
}

class FieldReflectionUtils {
 public static <T> Field getField(Class<T> clazz, String fieldName) throws NoSuchFieldException {
 return clazz.getDeclaredField(fieldName);
 }
}

class FieldSetter {
 private Field field;
 private Object instance;

 public FieldSetter(Object instance, Field field) {
 this.field = field;
 this.instance = instance;
 }

 public Object set(Field field, Object value) throws IllegalAccessException {
 field.setAccessible(true);
 return field.set(instance, value);
 }
}