package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class IFXObjectTest {

 private IFXObject createTestObject() {
 return new IFXObject();
 }

 @Test
 public void testEquals() {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = createTestObject();
 assertTrue(obj1.equals(obj1));
 assertTrue(obj1.equals(obj2));
 IFXObject obj3 = new Object(); // not an IFXObject, should return false
 assertFalse(obj1.equals(obj3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualsWithNull() {
 IFXObject obj1 = createTestObject();
 assertFalse(obj1.equals(null));
 }

 @Test
 public void testToString() {
 IFXObject obj = createTestObject();
 String expected = "IFXObject = {}";
 assertEquals(expected, obj.toString());
 }

 @Test
 public void testStringify() throws Exception {
 IFXObject obj = createTestObject();
 String expected = "IFXObject = {}";
 String result = obj.stringify(obj, 0);
 assertEquals(expected, result);
 }

 @Test
 public void testDoCheckEqualsWithNulls() {
 IFXObject obj1 = null;
 IFXObject obj2 = null;
 assertTrue(IFXObject.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsWithDifferentClasses() {
 IFXObject obj1 = createTestObject();
 Object obj2 = new Object();
 assertFalse(IFXObject.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsWithSingleMethodDifference() throws Exception {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = createTestObject();
 Method method1 = obj1.getClass().getMethod("getTest");
 method1.setAccessible(true);
 method1.invoke(obj1);
 Method method2 = obj2.getClass().getMethod("getTest");
 method2.setAccessible(true);
 method2.invoke(obj2);
 assertFalse(IFXObject.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsWithArrays() throws Exception {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = createTestObject();
 Method method1 = obj1.getClass().getMethod("getArray");
 method1.setAccessible(true);
 Object array1 = method1.invoke(obj1);
 Method method2 = obj2.getClass().getMethod("getArray");
 method2.setAccessible(true);
 Object array2 = method2.invoke(obj2);
 assertFalse(IFXObject.doCheckEquals(obj1, obj2));
 Object[] array1Copy = (Object[])array1.clone();
 Object[] array2Copy = (Object[])array2.clone();
 for (int i = 0; i < array1.length; i++) {
 Array.set(array1Copy, i, createTestObject());
 Array.set(array2Copy, i, createTestObject());
 }
 assertTrue(IFXObject.doCheckEquals(obj1, obj2));
 }

 @Test(expected = ArrayStoreException.class)
 public void testDoCheckEqualsWithArrayTypesMismatch() throws Exception {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = createTestObject();
 Method method1 = obj1.getClass().getMethod("getArray");
 method1.setAccessible(true);
 Object array1 = method1.invoke(obj1);
 Method method2 = obj2.getClass().getMethod("getArray");
 method2.setAccessible(true);
 Object array2 = method2.invoke(obj2);
 Class array1Class = array1.getClass().getComponentType();
 Class array2Class = array2.getClass().getComponentType();
 assertNotSame(array1Class, array2Class);
 assertFalse(IFXObject.doCheckEquals(obj1, obj2));
 }

 @Test(expected = InvocationTargetException.class)
 public void testToStringWithException() throws Exception {
 IFXObject obj = createTestObject();
 Method method = obj.getClass().getMethod("stringify", IFXObject.class, int.class);
 method.setAccessible(true);
 try {
 method.invoke(obj, new Object[] { null, 0 });
 fail("Expected InvocationTargetException");
 } catch (InvocationTargetException e) {
 assertTrue(true);
 }
 }

}