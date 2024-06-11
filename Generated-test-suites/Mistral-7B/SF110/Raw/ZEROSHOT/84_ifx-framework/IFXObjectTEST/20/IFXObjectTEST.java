package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import org.junit.Assert;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

public class IFXObjectTest {

 IFXObject obj1;
 IFXObject obj2;
 IFXObject obj3;
 IFXObject obj4;
 IFXObject obj5;
 IFXObject[] objArray;
 IBaseType baseType;

 @Before
 public void setUp() {
 obj1 = new IFXObject();
 obj2 = new IFXObject();
 obj3 = new IFXObject();
 obj4 = new IFXObject();
 obj5 = new IFXObject();
 baseType = new MyBaseType("Test String");
 objArray = new IFXObject[] { obj1, obj2, obj3 };
 }

 @Test
 public void testEquals() {
 testEquals(obj1, obj1);
 testEquals(obj1, obj2);
 testEquals(obj1, obj3);
 testEquals(obj1, obj4);
 testEquals(obj1, obj5);
 testEquals(obj1, baseType);
 testEquals(null, obj1);
 testEquals(obj1, null);
 testEquals(this, obj1);
 testEquals(objArray, objArray);
 testEquals(objArray, null);
 testEquals(null, objArray);
 testEquals(obj1, obj2);
 testEquals(obj2, obj1);
 testEquals(new IFXObject(), new IFXObject());
 testEquals(new IFXObject(), null);
 testEquals(null, new IFXObject());
 }

 private void testEquals(Object obj1, Object obj2) {
 try {
 Assert.assertEquals(obj1.equals(obj2), this.doCheckEquals(obj1, obj2));
 } catch (Exception e) {
 e.printStackTrace();
 Assert.fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testToString() {
 String expected = "class org.sourceforge.ifx.basetypes.IFXObject = {getClassName = org.sourceforge.ifx.basetypes.IFXObject, getClass = class org.sourceforge.ifx.basetypes.IFXObject}";
 Assert.assertEquals(expected, obj1.toString());
 }

 @Test(expected = Exception.class)
 public void testToStringException() {
 // This test should throw an exception, as the toString() method is not supposed to throw any
 try {
 obj1.toString();
 } catch (Exception e) {
 Assert.fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testDoCheckEquals() throws Exception {
 testDoCheckEquals(obj1, obj1);
 testDoCheckEquals(obj1, obj2);
 testDoCheckEquals(obj1, obj3);
 testDoCheckEquals(obj1, obj4);
 testDoCheckEquals(obj1, obj5);
 testDoCheckEquals(obj1, baseType);
 testDoCheckEquals(null, obj1);
 testDoCheckEquals(obj1, null);
 testDoCheckEquals(this, obj1);
 testDoCheckEquals(objArray, objArray);
 testDoCheckEquals(objArray, null);
 testDoCheckEquals(null, objArray);
 testDoCheckEquals(obj1, obj2);
 testDoCheckEquals(obj2, obj1);
 testDoCheckEquals(new IFXObject(), new IFXObject());
 testDoCheckEquals(new IFXObject(), null);
 testDoCheckEquals(null, new IFXObject());
 }

 private void testDoCheckEquals(Object obj1, Object obj2) throws Exception {
 Method method = IFXObject.class.getDeclaredMethod("doCheckEquals", IFXObject.class);
 method.setAccessible(true);
 boolean result = (boolean)method.invoke(obj1, obj2);
 boolean expected = equals(obj1, obj2);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testStringify() {
 // This test assumes that the implementation of stringify() in IFXObject correctly prints out the toString() method call for all nested objects
 String expected = "class org.sourceforge.ifx.basetypes.IFXObject = {getClassName = org.sourceforge.ifx.basetypes.IFXObject, getClass = class org.sourceforge.ifx.basetypes.IFXObject, getgetClassName = class org.sourceforge.ifx.basetypes.MyBaseType, getgetClass = class org.sourceforge.ifx.basetypes.IFXObject, get[] = [class org.sourceforge.ifx.basetypes.IFXObject, class org.sourceforge.ifx.basetypes.IFXObject, class org.sourceforge.ifx.basetypes.IFXObject], getmyBaseType = Test String}";
 String result = obj1.stringify(obj1, 0);
 Assert.assertEquals(expected, result);
 }

 @Test(expected = Exception.class)
 public void testStringifyException() {
 // This test should throw an exception, as the stringify() method is not supposed to throw any
 try {
 obj1.stringify(null, 0);
 } catch (Exception e) {
 Assert.fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testGetString() {
 IBaseType baseType = new MyBaseType("Test String");
 IFXObject obj = new IFXObject();
 obj.setMyBaseType(baseType);
 String expected = "class org.sourceforge.ifx.basetypes.IFXObject = {getClassName = org.sourceforge.ifx.basetypes.IFXObject, getClass = class org.sourceforge.ifx.basetypes.IFXObject, getmyBaseType = Test String}";
 String result = obj.toString();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testGetClass() {
 Class<?> expected = IFXObject.class;
 Class<?> result = obj1.getClass();
 Assert.assertEquals(expected, result);
 }

 private void setMyBaseType(IFXObject obj, IBaseType baseType) {
 try {
 Method method = IFXObject.class.getDeclaredMethod("setMyBaseType", IBaseType.class);
 method.setAccessible(true);
 method.invoke(obj, baseType);
 } catch (NoSuchMethodException e) {
 e.printStackTrace();
 Assert.fail("Unexpected exception: " + e.getMessage());
 } catch (IllegalAccessException e) {
 e.printStackTrace();
 Assert.fail("Unexpected exception: " + e.getMessage());
 } catch (InvocationTargetException e) {
 e.printStackTrace();
 Assert.fail("Unexpected exception: " + e.getCause().getMessage());
 }
 }

 @Test
 public void testSetMyBaseType() {
 setMyBaseType(obj1, baseType);
 assert baseType.equals(obj1.getMyBaseType());
 }

 @Test
 public void testGetArrayElement() {
 IFXObject[] array = new IFXObject[3];
 array[0] = obj1;
 array[1] = obj2;
 array[2] = obj3;
 IFXObject expected = obj1;
 IFXObject result = (IFXObject)Array.get(array, 0);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testArrayEquals() {
 IFXObject[] array1 = new IFXObject[3];
 array1[0] = obj1;
 array1[1] = obj2;
 array1[2] = obj3;
 IFXObject[] array2 = new IFXObject[3];
 array2[0] = obj1;
 array2[1] = obj2;
 array2[2] = obj3;
 IFXObject[] array3 = new IFXObject[2];
 array3[0] = obj1;
 array3[1] = obj2;
 IFXObject[] array4 = null;
 IFXObject[] array5 = new IFXObject[3];
 Object[] array6 = new Object[3];
 IFXObject[] array7 = { obj1, obj2, obj3 };
 Assert.assertEquals(array1, array1);
 Assert.assertEquals(array1, array2);
 Assert.assertNotEquals(array1, array3);
 Assert.assertNotEquals(array1, array4);
 Assert.assertNotEquals(array1, array5);
 Assert.assertNotEquals(array1, array6);
 Assert.assertNotEquals(array1, array7);
 Assert.assertNotEquals(array7, array1);
 }

 @Test
 public void testArrayToString() {
 String expected = "[class org.sourceforge.ifx.basetypes.IFXObject, class org.sourceforge.ifx.basetypes.IFXObject, class org.sourceforge.ifx.basetypes.IFXObject]";
 String result = objArray.toString();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testArrayToStringArray() {
 String[] expected = { "class org.sourceforge.ifx.basetypes.IFXObject", "class org.sourceforge.ifx.basetypes.IFXObject", "class org.sourceforge.ifx.basetypes.IFXObject" };
 String[] result = (String[])objArray.getClass().getMethod("toString").invoke(objArray);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testArrayStringify() {
 String expected = "[class org.sourceforge.ifx.basetypes.IFXObject = {getClassName = class org.sourceforge.ifx.basetypes.IFXObject, getClass = class org.sourceforge.ifx.basetypes.IFXObject}, class org.sourceforge.ifx.basetypes.IFXObject = {getClassName = class org.sourceforge.ifx.basetypes.IFXObject, getClass = class org.sourceforge.ifx.basetypes.IFXObject}, class org.sourceforge.ifx.basetypes.IFXObject = {getClassName = class org.sourceforge.ifx.basetypes.IFXObject, getClass = class org.sourceforge.ifx.basetypes.IFXObject}]";
 String result = objArray.stringify(objArray, 0);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testArrayStringifyArray() {
 String[] expected = { "class org.sourceforge.ifx.basetypes.IFXObject = {getClassName = class org.sourceforge.ifx.basetypes.IFXObject, getClass = class org.sourceforge.ifx.basetypes.IFXObject}",
 "class org.sourceforge.ifx.basetypes.IFXObject = {getClassName = class org.sourceforge.ifx.basetypes.IFXObject, getClass = class org.sourceforge.ifx.basetypes.IFXObject}",
 "class org.sourceforge.ifx.basetypes.IFXObject = {getClassName = class org.sourceforge.ifx.basetypes.IFXObject, getClass = class org.sourceforge.ifx.basetypes.IFXObject}" };
 String[] result = (String[])objArray.getClass().getMethod("stringify", int.class).invoke(objArray, 0);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testArrayGetMethod() {
 Method getMethod = IFXObject[].class.getMethod("get", int.class);
 Object result = getMethod.invoke(objArray, 0);
 Assert.assertEquals(obj1, result);
 }

 @Test
 public void testArraySetMethod() {
 IFXObject newObj = new IFXObject();
 Method setMethod = IFXObject[].class.getMethod("set", int.class, IFXObject.class);
 setMethod.invoke(objArray, 0, newObj);
 Assert.assertEquals(newObj, objArray[0]);
 }

 @After
 public void tearDown() {
 // Release any allocated resources here
 }
}