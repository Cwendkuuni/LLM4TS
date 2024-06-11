package org.sourceforge.ifx.basetypes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.sourceforge.ifx.basetypes.IFXObject;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class IFXObjectTest {

 private static final String TABSTOP = "  ";

 @Test
 public void testEquals() {
 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = new IFXObject();
 IFXObject obj3 = new IFXObject();
 Object obj4 = new Object();

 assertTrue(obj1.equals(obj1));
 assertFalse(obj1.equals(obj2));
 assertFalse(obj1.equals(obj3));
 assertFalse(obj1.equals(obj4));

 obj1.setValue("testValue", "testValue1");
 obj2.setValue("testValue", "testValue1");
 assertTrue(obj1.equals(obj2));

 obj2.setValue("testValue", "testValue2");
 assertFalse(obj1.equals(obj2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualsInvalidType() {
 IFXObject obj1 = new IFXObject();
 assertFalse(obj1.equals(new Integer(0)));
 }

 @Test
 public void testToString() {
 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = new IFXObject();
 obj1.setValue("testValue1", "value1");
 obj2.setValue("testValue1", "value2");
 obj2.setValue("testValue2", "value3");

 String expected = "IFXObject = {" +
         "\ntestValue1 = value1" +
         "\ntestValue2 = [{" +
         "\n\ttestValue = value3" +
         "\n}]" +
         "}";
 String actual = obj1.toString();
 assertEquals(expected, actual);

 actual = obj2.toString();
 expected = "IFXObject = {" +
         "\ntestValue1 = value1" +
         "\ntestValue2 = {" +
         "\n\ttestValue = value3" +
         "\n}" +
         "}";
 assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testToStringNullObject() {
 IFXObject obj1 = null;
 obj1.toString();
 }

 @Test
 public void testStringify() throws Exception {
 IFXObject obj1 = new IFXObject();
 obj1.setValue("testValue1", new Integer(1));
 IFXObject obj2 = new IFXObject();
 obj2.setValue("testValue1", new Integer[2]);

 String expected = "IFXObject = {" +
         "\ntestValue1 = [1, 1]" +
         "}";
 String actual = obj1.stringify(obj1, 0);
 assertEquals(expected, actual);

 expected = "IFXObject = {" +
         "\ntestValue1 = [1, 1]\n" +
         "\ntestValue1 = [" +
         "\n\t\tIFXObject = {" +
         "\ntestValue = 1" +
         "\n}" +
         "\n,\n" +
         "\t\tIFXObject = {" +
         "\ntestValue = 1" +
         "\n}" +
         "]\n" +
         "}";
 String actual2 = obj1.stringify(obj2, 0);
 assertEquals(expected, actual2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStringifyInvalidObject() throws Exception {
 IFXObject obj1 = new IFXObject();
 String actual = obj1.stringify(new Integer(0), 0);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testDoCheckEquals() throws Exception {
 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = new IFXObject();
 IFXObject obj3 = new IFXObject();
 IFXObject obj4 = new IFXObject();
 IFXObject obj5 = new IFXObject();

 obj1.setValue("testValue1", "value1");
 obj2.setValue("testValue1", "value1");
 obj3.setValue("testValue1", "value2");
 obj4.setValue("testValue1", "value1");
 obj5.setValue("testValue1", "value3");

 assertTrue(obj1.doCheckEquals(obj1, obj1));
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 assertFalse(obj1.doCheckEquals(obj1, obj3));
 assertFalse(obj1.doCheckEquals(obj1, obj4));
 assertFalse(obj1.doCheckEquals(obj1, obj5));

 Class<IFXObject> clazz = IFXObject.class;
 Method[] methods = clazz.getMethods();
 Method getTestValueMethod = null;
 for (Method method : methods) {
 if (method.getName().equals("setValue")) {
 getTestValueMethod = method;
 break;
 }
 }
 Object[] arguments = new Object[2];
 arguments[0] = "testValue1";
 arguments[1] = "value1";

 Object value1 = getTestValueMethod.invoke(obj1, arguments);
 Object value2 = getTestValueMethod.invoke(obj2, arguments);
 Object value3 = getTestValueMethod.invoke(obj3, arguments);
 Object value4 = getTestValueMethod.invoke(obj4, arguments);
 Object value5 = getTestValueMethod.invoke(obj5, arguments);

 assertTrue(obj1.doCheckEquals(obj1, obj1));
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 assertFalse(obj1.doCheckEquals(obj1, obj3));
 assertFalse(obj1.doCheckEquals(obj1, obj4));
 assertFalse(obj1.doCheckEquals(obj1, obj5));

 Class<?> valueClass = value1.getClass();
 if (valueClass.isArray()) {
 IFXObject[] arrays = new IFXObject[2];
 arrays[0] = new IFXObject();
 arrays[1] = new IFXObject();
 Object[] arrayValues = new Object[2];
 arrayValues[0] = value1;
 arrayValues[1] = value2;

 assertTrue(obj1.doCheckEquals(obj1, obj1));
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 assertFalse(obj1.doCheckEquals(obj1, arrays[0]));
 assertFalse(obj1.doCheckEquals(obj1, arrays[1]));

 Method arrayGetValueMethod = null;
 for (Method method : arrays[0].getClass().getMethods()) {
 if (method.getName().equals("getValue")) {
 arrayGetValueMethod = method;
 break;
 }
 }
 Object arrayValue1 = arrayGetValueMethod.invoke(arrays[0], (Object[])null);
 Object arrayValue2 = arrayGetValueMethod.invoke(arrays[1], (Object[])null);

 assertFalse(obj1.doCheckEquals(obj1, arrayValue1));
 assertFalse(obj1.doCheckEquals(obj1, arrayValue2));
 }
 else if (valueClass instanceof IBaseType) {
 String value1AsString = ((IBaseType)value1).getString();
 String value2AsString = ((IBaseType)value2).getString();

 assertTrue(obj1.doCheckEquals(obj1, obj1));
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 assertFalse(obj1.doCheckEquals(obj1, value1AsString));
 assertFalse(obj1.doCheckEquals(obj1, value2AsString));
 }
 else {
 IFXObject value1Obj = (IFXObject)value1;
 IFXObject value2Obj = (IFXObject)value2;

 assertTrue(obj1.doCheckEquals(obj1, obj1));
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 assertFalse(obj1.doCheckEquals(obj1, value1Obj));
 assertFalse(obj1.doCheckEquals(obj1, value2Obj));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoCheckEqualsInvalidType() {
 IFXObject obj1 = new IFXObject();
 Object obj2 = new Object();
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testDoCheckEqualsArrayIndexOutOfBounds() throws Exception {
 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = new IFXObject();
 obj1.setValue("testValue1", new Integer[2]);
 obj2.setValue("testValue1", new Integer[1]);

 Method getValueMethod = null;
 for (Method method : obj1.getClass().getMethods()) {
 if (method.getName().equals("getValue")) {
 getValueMethod = method;
 break;
 }
 }
 Object[] arrayValues = new Object[2];
 arrayValues[0] = getValueMethod.invoke(obj1, (Object[])null);
 Object arrayValue1 = Array.get(arrayValues, 0);
 Object arrayValue2 = Array.get(arrayValues, 1);

 assertFalse(obj1.doCheckEquals(obj1, obj2));
 assertFalse(obj1.doCheckEquals(obj1, arrayValue1));
 assertFalse(obj1.doCheckEquals(obj1, arrayValue2));
 }
}