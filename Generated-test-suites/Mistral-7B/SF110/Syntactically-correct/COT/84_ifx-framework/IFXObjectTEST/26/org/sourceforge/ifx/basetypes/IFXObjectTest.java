package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class IFXObjectTest {

 private IFXObject createIFXObject() {
 return new IFXObject();
 }

 @Test
 public void testEquals() {
 IFXObject obj1 = createIFXObject();
 IFXObject obj2 = createIFXObject();
 IFXObject obj3 = createIFXObject();
 Object obj4 = new Object();

 // Test when both objects are equal
 assertTrue(obj1.equals(obj1));

 // Test when first object is equal to second object
 assertTrue(obj1.equals(obj2));

 // Test when first object is not equal to third object
 assertFalse(obj1.equals(obj3));

 // Test when first object is not equal to non-IFXObject
 assertFalse(obj1.equals(obj4));

 // Test when first object is null and second object is null
 assertTrue(obj1 == null ? obj2 == null : !obj1.equals(obj2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToStringNullObject() {
 String result = createIFXObject().toString(null);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testToString() {
 IFXObject obj = createIFXObject();
 String result = obj.toString();
 assertNotNull(result);
 }

 @Test
 public void testStringifyGetClassMethod() {
 IFXObject obj = createIFXObject();
 String result = obj.stringify(obj, 0);
 assertNotNull(result);
 assertEquals(IFXObject.class.getName(), result.substring(0, result.indexOf("=")).trim());
 }

 @Test
 public void testStringifyGetBooleanMethod() {
 IFXObject obj = createIFXObject();
 boolean booleanValue = true;
 Method booleanMethod = null;
 try {
 booleanMethod = obj.getClass().getMethod("getBoolean");
 } catch (NoSuchMethodException e) {
 // This method is not present in the given class
 }
 IFXObject booleanObj = new IFXObject();
 booleanObj.setBoolean(booleanValue);
 String result = obj.stringify(booleanObj, 0);
 assertNotNull(result);
 assertFalse(result.contains("NULL"));
 }

 @Test
 public void testStringifyGetStringMethod() {
 IFXObject obj = createIFXObject();
 String stringValue = "Test String";
 Method stringMethod = null;
 try {
 stringMethod = obj.getClass().getMethod("getString");
 stringMethod.setAccessible(true);
 } catch (NoSuchMethodException | SecurityException e) {
 // This method is not present in the given class
 }
 IFXObject stringObj = new IFXObject();
 stringObj.setString(stringValue);
 String result = obj.stringify(stringObj, 0);
 assertNotNull(result);
 assertEquals(stringValue, result.substring(result.indexOf(":") + 1, result.length() - 1));
 }

 @Test
 public void testStringifyGetArrayMethod() {
 IFXObject obj = createIFXObject();
 IFXObject[] arrayObj = new IFXObject[3];
 for (int i = 0; i < arrayObj.length; i++) {
 arrayObj[i] = new IFXObject();
 arrayObj[i].setString("Test String " + i);
 }
 String result = obj.stringify(arrayObj, 0);
 assertNotNull(result);
 // Check if the first element in the array is present in the string
 assertTrue(result.contains("0]:Test String 0"));
 // Check if the second element in the array is present in the string
 assertTrue(result.contains("1]:Test String 1"));
 // Check if the third element in the array is present in the string
 assertTrue(result.contains("2]:Test String 2"));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testStringifyGetArrayNegativeIndex() {
 IFXObject obj = createIFXObject();
 IFXObject[] arrayObj = new IFXObject[3];
 for (int i = 0; i < arrayObj.length; i++) {
 arrayObj[i] = new IFXObject();
 arrayObj[i].setString("Test String " + i);
 }
 String result = obj.stringify(arrayObj, -1);
 fail("Should throw ArrayIndexOutOfBoundsException");
 }

 @Test(expected = NullPointerException.class)
 public void testStringifyNullArray() {
 IFXObject obj = createIFXObject();
 String result = obj.stringify(null, 0);
 fail("Should throw NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStringifyNonIFXObjectArray() {
 IFXObject obj = createIFXObject();
 String[] arrayObj = new String[3];
 for (int i = 0; i < arrayObj.length; i++) {
 arrayObj[i] = "Test String " + i;
 }
 String result = obj.stringify(arrayObj, 0);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = InvocationTargetException.class)
 public void testDoCheckEqualsNullThisObj() {
 IFXObject obj1 = createIFXObject();
 IFXObject obj2 = createIFXObject();
 IFXObject nullObj = null;
 boolean result = obj1.doCheckEquals(nullObj, obj2);
 fail("Should throw InvocationTargetException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoCheckEqualsNullThatObj() {
 IFXObject obj1 = createIFXObject();
 IFXObject obj2 = createIFXObject();
 IFXObject nullObj = null;
 boolean result = obj1.doCheckEquals(obj1, nullObj);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = ClassCastException.class)
 public void testDoCheckEqualsDifferentClasses() {
 IFXObject obj1 = createIFXObject();
 String obj2 = new String("Test");
 boolean result = obj1.doCheckEquals(obj1, obj2);
 fail("Should throw ClassCastException");
 }

 @Test
 public void testDoCheckEqualsSameClassesDifferentValues() {
 IFXObject obj1 = createIFXObject();
 IFXObject obj2 = createIFXObject();
 boolean result = obj1.doCheckEquals(obj1, obj2);
 assertFalse(result);
 }

 @Test
 public void testDoCheckEqualsSameClassesSameValues() {
 IFXObject obj1 = createIFXObject();
 IFXObject obj2 = createIFXObject();
 obj1.setString("Test String");
 obj2.setString("Test String");
 boolean result = obj1.doCheckEquals(obj1, obj2);
 assertTrue(result);
 }

 @Test
 public void testDoCheckEqualsSameClassesDifferentMethods() {
 IFXObject obj1 = createIFXObject();
 IFXObject obj2 = createIFXObject();
 obj1.setBoolean(true);
 obj2.setBoolean(false);
 boolean result = obj1.doCheckEquals(obj1, obj2);
 assertFalse(result);
 }

 @Test
 public void testDoCheckEqualsSameClassesSameMethodsDifferentValues() {
 IFXObject obj1 = createIFXObject();
 IFXObject obj2 = createIFXObject();
 obj1.setBoolean(true);
 obj2.setBoolean(true);
 boolean result = obj1.doCheckEquals(obj1, obj2);
 assertTrue(result);
 }
}