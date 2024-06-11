package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.*;

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
 IFXObject obj3 = createTestObject();

 assertTrue(obj1.equals(obj1));
 assertFalse(obj1.equals(null));
 assertFalse(obj1.equals(obj2));
 assertFalse(obj1.equals(obj3));

 obj2 = obj1;
 assertTrue(obj1.equals(obj2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualsWithNullObj() {
 IFXObject obj1 = createTestObject();
 assertFalse(obj1.equals(null));
 }

 @Test
 public void testToString() {
 IFXObject obj = createTestObject();
 assertEquals("IFXObject = {...}", obj.toString());
 }

 @Test
 public void testStringify() throws IllegalAccessException, InvocationTargetException {
 IFXObject obj = createTestObject();
 String stringified = obj.stringify(obj, 0);
 assertEquals("IFXObject = {...}", stringified);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStringifyWithNullObj() throws IllegalAccessException, InvocationTargetException {
 IFXObject obj = createTestObject();
 String stringified = obj.stringify(null, 0);
 fail();
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testStringifyWithArrayIndexOutOfBounds() throws IllegalAccessException, InvocationTargetException {
 IFXObject obj = createTestObject();
 Object[] array = new Object[0];
 array[0] = obj;
 String stringified = obj.stringify(array, 0);
 fail();
 }

 @Test
 public void testDoCheckEqualsWithSameObjects() {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = obj1;
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsWithNullObj1() {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = createTestObject();
 assertFalse(obj1.doCheckEquals(null, obj2));
 }

 @Test
 public void testDoCheckEqualsWithNullObj2() {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = createTestObject();
 assertFalse(obj1.doCheckEquals(obj1, null));
 }

 @Test
 public void testDoCheckEqualsWithDifferentClasses() {
 IFXObject obj1 = createTestObject();
 String obj2 = new String("");
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsWithDifferentNumberOfMethods() throws Exception {
 IFXObject obj1 = createTestObject();
 Object obj2 = new Object(){};
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testDoCheckEqualsWithArrayIndexOutOfBounds() throws Exception {
 IFXObject obj1 = createTestObject();
 Object obj2 = new Object(){};
 Method[] methods1 = obj1.getClass().getMethods();
 Method[] methods2 = obj2.getClass().getMethods();
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 int length = methods1.length + 1;
 Method method = new Method("getInvalidMethodName", new Class[]{});
 methods2 = new Method[length];
 methods2[methods2.length - 1] = method;
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test(expected = InvocationTargetException.class)
 public void testDoCheckEqualsWithException() throws Exception {
 IFXObject obj1 = createTestObject();
 Object obj2 = new Object(){
 @Override
 public String toString() {
 throw new Exception("Test Exception");
 }
 };
 Method[] methods1 = obj1.getClass().getMethods();
 Method method = methods1[0];
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

}