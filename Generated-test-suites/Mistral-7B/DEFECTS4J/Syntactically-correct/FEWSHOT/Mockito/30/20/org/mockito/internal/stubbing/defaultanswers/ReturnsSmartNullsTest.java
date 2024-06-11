package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private Object dummyMock;

 @Before
 public void setup() {
 dummyMock = mock(Object.class);
 returnsSmartNulls = new ReturnsSmartNulls();
 }

 @Test
 public void testReturnsNull() {
 assertNull(returnsSmartNulls.answer(ArgumentMatchers.any(InvocationOnMock.class)));
 }

 @Test
 public void testReturnsSmartNullForString() {
 String result = (String) returnsSmartNulls.answer(mockMethodWithReturnTypeString());
 assertEquals("SmartNull returned by unstubbed method on mock", result);
 }

 @Test
 public void testReturnsSmartNullForNumber() {
 Number result = (Number) returnsSmartNulls.answer(mockMethodWithReturnTypeNumber());
 assertEquals("SmartNull returned by unstubbed method on mock", result);
 }

 @Test
 public void testReturnsSmartNullForCustomClass() {
 CustomClass result = (CustomClass) returnsSmartNulls.answer(mockMethodWithReturnTypeCustomClass());
 assertEquals("SmartNull returned by unstubbed method on mock", result);
 }

 @Test
 public void testReturnsDefaultValue() {
 when(dummyMock.foo()).thenAnswer(new ReturnsSmartNulls());
 Object result = dummyMock.foo();
 verify(dummyMock).foo();
 assertEquals(Integer.valueOf(42), result);
 }

 @Test
 public void testReturnsSmartNullForUnmockedStaticMethod() {
 when(ReturnsSmartNulls.class).thenAnswer(new ReturnsSmartNulls());
 Object result = ReturnsSmartNulls.class.getDeclaredMethod("testStripLeadingHyphens").invoke(null);
 assertNull(result);
 }

 @Test
 public void testReturnsSmartNullForArray() {
 Object[] result = (Object[]) returnsSmartNulls.answer(mockMethodWithReturnTypeArray());
 assertEquals("[SmartNull]", Arrays.toString(result));
 }

 @Test
 public void testReturnsSmartNullForCollection() {
 Object result = returnsSmartNulls.answer(mockMethodWithReturnTypeCollection());
 assertEquals("SmartCollection [SmartNull]", result.toString());
 }

 @Test
 public void testReturnsSmartNullForMap() {
 Object result = returnsSmartNulls.answer(mockMethodWithReturnTypeMap());
 assertEquals("SmartMap{}[SmartNull=SmartNull]", result.toString());
 }

 private Method mockMethodWithReturnTypeString() {
 return Mockito.spy(String.class).getDeclaredMethod("valueOf", String.class);
 }

 private Method mockMethodWithReturnTypeNumber() {
 return Mockito.spy(Integer.class).getDeclaredMethod("valueOf", int.class);
 }

 private Method mockMethodWithReturnTypeCustomClass() {
 CustomClass customClass = Mockito.mock(CustomClass.class);
 return spy(CustomClass.class).getDeclaredMethod("newInstance", CustomClass.class);
 }

 private Method mockMethodWithReturnTypeArray() {
 return Mockito.spy(Object[].class).getDeclaredMethod("newInstance", int.class);
 }

 private Method mockMethodWithReturnTypeCollection() {
 return Mockito.spy(ArrayList.class).getDeclaredConstructor(int.class);
 }

 private Method mockMethodWithReturnTypeMap() {
 return Mockito.spy(HashMap.class).getDeclaredConstructor(int.class, int.class);
 }

 private static class CustomClass implements Serializable {
 public CustomClass() {
 }

 public static CustomClass newInstance(CustomClass customClass) {
 throw new UnsupportedOperationException();
 }
 }
}