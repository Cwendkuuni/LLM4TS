package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

public class IFXObjectTest {

 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = new IFXObject();
 IFXObject obj3 = new IFXObject();
 String expectedString = "class org.sourceforge.ifx.basetypes.IFXObject = {\ngetClassName = class org.sourceforge.ifx.basetypes.IFXObject\n}";

 @Test
 public void testEquals() {
 IFXObject obj4 = obj1;
 // test equals with null
 assertFalse(obj1.equals(null));
 // test equals with same object
 assertTrue(obj1.equals(obj4));
 // test equals with different class
 assertFalse(obj1.equals("test"));
 // test equals with different object
 assertFalse(obj1.equals(obj2));
 // test equals with different class and different objects
 assertFalse(obj1.equals(new Object()));
 // test equals with same class and different objects
 assertFalse(obj1.equals(obj3));
 // test equals with same class and same objects but different values
 IFXObject obj5 = new IFXObject();
 obj5.setSomeField("test");
 obj3.setSomeField("test");
 assertFalse(obj1.equals(obj5));
 // test equals with same class and same objects but same values
 obj5 = new IFXObject();
 obj5.setSomeField("test");
 obj3.setSomeField("test");
 obj3 = obj5;
 assertTrue(obj1.equals(obj3));
 }

 @Test
 public void testToString() {
 // test toString with empty object
 assertEquals("class org.sourceforge.ifx.basetypes.IFXObject = {\ngetClassName = class org.sourceforge.ifx.basetypes.IFXObject\n}", obj1.toString());
 // test toString with object containing primitive types
 obj1.setSomeField(1);
 obj1.setAnotherField(true);
 obj1.setThirdField('a');
 assertEquals(expectedString, obj1.toString());
 // test toString with object containing array
 IFXObject objArray = new IFXObject();
 objArray.setAnArrayField(new int[] { 1, 2, 3 });
 expectedString = "class org.sourceforge.ifx.basetypes.IFXObject = {\ngetClassName = class org.sourceforge.ifx.basetypes.IFXObject\n\nanArrayField = [1,2,3]\n}";
 assertEquals(expectedString, objArray.toString());
 // test toString with object containing nested array
 IFXObject nestedArrayObj = new IFXObject();
 nestedArrayObj.setAnArrayField(new IFXObject[] { objArray });
 expectedString = "class org.sourceforge.ifx.basetypes.IFXObject = {\ngetClassName = class org.sourceforge.ifx.basetypes.IFXObject\n\nanArrayField = [\nclass org.sourceforge.ifx.basetypes.IFXObject = {\ngetClassName = class org.sourceforge.ifx.basetypes.IFXObject\nanArrayField = [1,2,3]\n}\n]";
 assertEquals(expectedString, nestedArrayObj.toString());
 // test toString with object containing nested objects
 IFXObject nestedObj1 = new IFXObject();
 nestedObj1.setNestedField1(new IFXObject());
 expectedString = "class org.sourceforge.ifx.basetypes.IFXObject = {\ngetClassName = class org.sourceforge.ifx.basetypes.IFXObject\nnestedField1 = {\ngetClassName = class org.sourceforge.ifx.basetypes.IFXObject\n}";
 IFXObject nestedObj2 = new IFXObject();
 nestedObj2.setNestedField1(nestedObj1);
 assertEquals(expectedString, nestedObj2.toString());
 }

 @Test
 public void testDoCheckEquals() {
 // test equals with null
 assertFalse(obj1.doCheckEquals(null, obj1));
 // test equals with same object
 assertTrue(obj1.doCheckEquals(obj1, obj1));
 // test equals with different class
 assertFalse(obj1.doCheckEquals(new Object(), obj1));
 // test equals with different object but same class
 IFXObject obj6 = new IFXObject();
 assertFalse(obj1.doCheckEquals(obj6, obj1));
 // test equals with same class and same objects but different values
 obj6.setSomeField("test");
 assertFalse(obj1.doCheckEquals(obj6, obj1));
 // test equals with same class and same objects but same values
 obj6.setSomeField("test");
 obj6 = obj1;
 assertTrue(obj1.doCheckEquals(obj6, obj6));
 // test equals with same class and same objects but different values but same length and type arrays
 IFXObject obj7 = new IFXObject();
 obj7.setSomeArrayField(new int[] { 1, 2, 3 });
 IFXObject obj8 = new IFXObject();
 obj8.setSomeArrayField(new int[] { 1, 2, 3 });
 // test equals with same class and same objects but different values but different length arrays
 IFXObject obj9 = new IFXObject();
 obj9.setSomeArrayField(new int[] { 1, 2, 3, 4 });
 assertFalse(obj7.doCheckEquals(obj8, obj9));
 // test equals with same class and same objects but different values but different types arrays
 IFXObject obj10 = new IFXObject();
 obj10.setSomeArrayField(new String[] { "1", "2", "3" });
 assertFalse(obj7.doCheckEquals(obj8, obj10));
 // test equals with same class and same objects but different values but same length and same type nested arrays
 IFXObject nestedObj3 = new IFXObject();
 nestedObj3.setNestedArrayField(new IFXObject[] { objArray });
 IFXObject nestedObj4 = new IFXObject();
 nestedObj4.setNestedArrayField(new IFXObject[] { objArray });
 assertTrue(obj7.doCheckEquals(obj8, nestedObj3));
 // test equals with same class and same objects but different values but same length and different type nested arrays
 IFXObject nestedObj5 = new IFXObject();
 nestedObj5.setNestedArrayField(new IFXObject[] { new IFXObject() });
 assertFalse(obj7.doCheckEquals(obj8, nestedObj5));
 }

 private void setSomeField(IFXObject obj, int value) throws IllegalAccessException, InvocationTargetException {
 Method method = obj.getClass().getDeclaredMethod("setSomeField", int.class);
 method.setAccessible(true);
 method.invoke(obj, value);
 }

 private void setSomeField(IFXObject obj, String value) throws IllegalAccessException, InvocationTargetException {
 Method method = obj.getClass().getDeclaredMethod("setSomeField", String.class);
 method.setAccessible(true);
 method.invoke(obj, value);
 }

 private void setAnotherField(IFXObject obj, boolean value) throws IllegalAccessException, InvocationTargetException {
 Method method = obj.getClass().getDeclaredMethod("setAnotherField", boolean.class);
 method.setAccessible(true);
 method.invoke(obj, value);
 }

 private void setThirdField(IFXObject obj, char value) throws IllegalAccessException, InvocationTargetException {
 Method method = obj.getClass().getDeclaredMethod("setThirdField", char.class);
 method.setAccessible(true);
 method.invoke(obj, value);
 }

 private void setSomeArrayField(IFXObject obj, int[] value) throws IllegalAccessException, InvocationTargetException {
 Method method = obj.getClass().getDeclaredMethod("setSomeArrayField", int[].class);
 method.setAccessible(true);
 method.invoke(obj, value);
 }

 private void setNestedField1(IFXObject obj, IFXObject value) throws IllegalAccessException, InvocationTargetException {
 Method method = obj.getClass().getDeclaredMethod("setNestedField1", IFXObject.class);
 method.setAccessible(true);
 method.invoke(obj, value);
 }

 private void setNestedArrayField(IFXObject obj, IFXObject[] value) throws IllegalAccessException, InvocationTargetException {
 Method method = obj.getClass().getDeclaredMethod("setNestedArrayField", IFXObject[].class);
 method.setAccessible(true);
 method.invoke(obj, value);
 }
}