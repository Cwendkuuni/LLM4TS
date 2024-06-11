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
 public void testEquals() throws Exception {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = createTestObject();
 assertTrue(obj1.equals(obj1));
 assertTrue(obj1.equals(obj2));
 obj2.getClass().getMethods()[0].invoke(obj2); // invoke any method to change obj2
 assertFalse(obj1.equals(obj2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualsWithNull() {
 IFXObject obj1 = createTestObject();
 assertFalse(obj1.equals(null));
 }

 @Test
 public void testToString() throws Exception {
 IFXObject obj = createTestObject();
 String expected = "IFXObject = {}";
 assertEquals(expected, obj.toString());
 }

 @Test(expected = ReflectiveOperationException.class)
 public void testToStringWithException() throws Exception {
 IFXObject obj = createTestObject();
 throw new ReflectiveOperationException("Test exception", new Exception());
 }

 @Test
 public void testStringifyGetClass() throws Exception {
 IFXObject obj = createTestObject();
 Method getClassMethod = obj.getClass().getMethod("getClass");
 String expected = "java.lang.Class = [class java.lang.Class]";
 assertEquals(expected, obj.stringify(obj, 0).substring(0, expected.length()));
 }

 @Test
 public void testStringifyGetArray() throws Exception {
 IFXObject obj = createTestObject();
 Object array = new Object[1];
 Method getArrayMethod = IFXObject.class.getMethod("getStringify", IFXObject.class, int.class);
 Object arrayObj = Array.newInstance(IFXObject.class, 1);
 Array.set(array, 0, arrayObj);
 Method getArrayMethodForArrayObj = arrayObj.getClass().getMethod("getClass");
 String expected = "[java.lang.Object] = [{\n  IFXObject = {}\n}]";
 assertEquals(expected, getArrayMethod.invoke(obj, array).toString());
 }

 @Test
 public void testStringifyGetIBaseType() throws Exception {
 IFXObject obj = createTestObject();
 IBaseType iBaseType = new MyIBaseType();
 Method getStringMethod = iBaseType.getClass().getMethod("getString");
 String expected = "MyIBaseType = Test";
 Method getMethod = obj.getClass().getMethod("getStringify", IFXObject.class, int.class);
 assertEquals(expected, getMethod.invoke(obj, iBaseType).toString());
 }

 @Test
 public void testStringifyComplexObject() throws Exception {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = createTestObject();
 Object array = new Object[2];
 Array.set(array, 0, obj1);
 Array.set(array, 1, obj2);
 Method getArrayMethod = IFXObject.class.getMethod("getStringify", IFXObject.class, int.class);
 String expected = "{IFXObject = {}\n  IFXObject = {}\n}";
 assertEquals(expected, getArrayMethod.invoke(obj1, array).toString());
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testStringifyArrayIndexOutOfBounds() throws Exception {
 IFXObject obj = createTestObject();
 Object array = new Object[1];
 Array.set(array, 0, obj);
 Method getArrayMethod = IFXObject.class.getMethod("getStringify", IFXObject.class, int.class);
 Method getMethodForArrayObj = obj.getClass().getMethod("getClass");
 Object arrayObj = Array.newInstance(IFXObject.class, 0);
 Array.set(array, 1, arrayObj);
 assertArrayEquals(new Object[]{"[java.lang.Object] = [{\n  IFXObject = {}\n}]"}, getArrayMethod.invoke(obj, array).toString().split("\n"));
 throw new ArrayIndexOutOfBoundsException("Index out of bounds: 1");
 }

 @Test(expected = ClassCastException.class)
 public void testStringifyClassCastException() throws Exception {
 IFXObject obj = createTestObject();
 String str = "Test";
 Method setStringMethod = IBaseType.class.getMethod("setString", String.class);
 IBaseType iBaseType = new MyIBaseType();
 setStringMethod.invoke(iBaseType, str);
 Method getStringMethod = iBaseType.getClass().getMethod("getString");
 Object array = new Object[1];
 Array.set(array, 0, obj);
 Method getArrayMethod = IFXObject.class.getMethod("getStringify", IFXObject.class, int.class);
 assertEquals(new String[]{"IFXObject = {}\n  MyIBaseType = Test"}, getArrayMethod.invoke(obj, array).toString().split("\n"));
 Method getMethodForIBaseType = obj.getClass().getMethod("getStringify", IFXObject.class, int.class);
 assertFalse(getMethodForIBaseType.isAccessible(obj));
 getMethodForIBaseType.setAccessible(true);
 try {
 getMethodForIBaseType.invoke(obj, iBaseType);
 fail("Expected ClassCastException");
 } catch (ClassCastException e) {
 // Expected
 }
 }

 @Test
 public void testDoCheckEqualsReflexivity() throws Exception {
 IFXObject obj1 = createTestObject();
 assertTrue(obj1.doCheckEquals(obj1, obj1));
 }

 @Test
 public void testDoCheckEqualsSymmetry() throws Exception {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = createTestObject();
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 assertTrue(obj2.doCheckEquals(obj2, obj1));
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsTransitivity() throws Exception {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = createTestObject();
 IFXObject obj3 = createTestObject();
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 assertTrue(obj2.doCheckEquals(obj2, obj3));
 assertTrue(obj1.doCheckEquals(obj1, obj3));
 }

 @Test
 public void testDoCheckEqualsDifferentClasses() throws Exception {
 IFXObject obj1 = createTestObject();
 Object obj2 = new Object();
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsNullObject() throws Exception {
 IFXObject obj1 = createTestObject();
 assertFalse(obj1.doCheckEquals(obj1, null));
 }

 @Test
 public void testDoCheckEqualsSameClassDifferentInstances() throws Exception {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = new IFXObject();
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsDifferentClassesDifferentInstances() throws Exception {
 IFXObject obj1 = createTestObject();
 Object obj2 = new Object();
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test(expected = StackOverflowError.class)
 public void testDoCheckEqualsStackOverflow() throws Exception {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = createTestObject();
 Method getMethodsMethod = obj1.getClass().getMethod("getMethods");
 getMethodsMethod.setAccessible(true);
 Object methods = getMethodsMethod.invoke(obj1);
 Method doCheckEqualsMethod = IFXObject.class.getMethod("doCheckEquals", IFXObject.class, IFXObject.class);
 doCheckEqualsMethod.invoke(obj1, obj2);
 doCheckEqualsMethod.invoke(obj2, obj1);
 doCheckEqualsMethod.invoke(obj1, obj2);
 doCheckEqualsMethod.invoke(obj2, obj1);
 }

}