package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import static org.junit.Assert.*;

public class IFXObjectTest {

 IFXObject testObj1 = new IFXObject();
 IFXObject testObj2 = new IFXObject();
 IFXObject testObj3 = new IFXObject();
 IBaseType testBaseType = new TestBaseType("Test Value");

 @Test
 public void testEquals() {
 // Test equals with null objects
 assertFalse(testObj1.equals(null));
 assertFalse(null.equals(testObj1));

 // Test equals with same object
 assertTrue(testObj1.equals(testObj1));

 // Test equals with different classes
 assertFalse(testObj1.equals(new Object()));

 // Test equals with different objects of the same class
 assertFalse(testObj1.equals(testObj2));

 // Test equals with same objects of the same class with identical fields
 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = new IFXObject();
 obj1.setField1("Test Value 1");
 obj2.setField1("Test Value 1");
 assertTrue(obj1.equals(obj2));

 // Test equals with same objects of the same class with different fields
 obj1.setField2("Test Value 2");
 assertFalse(obj1.equals(obj2));

 // Test equals with different objects of the same class with identical fields
 IFXObject obj3 = new IFXObject();
 obj3.setField1("Test Value 1");
 IFXObject obj4 = new IFXObject();
 obj4.setField1("Test Value 1");
 assertTrue(obj3.equals(obj4));

 // Test equals with different objects of the same class with different fields
 obj3.setField2("Test Value 3");
 assertFalse(obj3.equals(obj4));

 // Test equals with different objects of different classes
 assertFalse(testObj1.equals(testBaseType));

 // Test equals with arrays
 Object[] arr1 = new Object[] {testObj1, testBaseType};
 Object[] arr2 = new Object[] {testObj1, testBaseType};
 assertFalse(Arrays.deepEquals(arr1, arr2)); // Arrays are not equal, but the IFXObject elements are
 assertTrue(testObj1.equals(Arrays.stream(arr1).findFirst().get()));
 assertTrue(testBaseType.equals(Arrays.stream(arr1).findAny().get()));

 Object[] arr3 = new Object[] {testObj1, testObj2};
 assertFalse(testObj1.equals(arr3));
 assertFalse(testObj2.equals(arr3));

 // Test equals with nested objects
 IFXObject nestedObj1 = new IFXObject();
 IFXObject nestedObj2 = new IFXObject();
 testObj1.setField1(nestedObj1);
 testObj2.setField1(nestedObj2);
 nestedObj1.setField1(testBaseType);
 nestedObj2.setField1(testBaseType);
 assertTrue(testObj1.equals(testObj2));
 }

 @Test
 public void testToString() {
 // Test toString with empty object
 assertEquals("org.sourceforge.ifx.basetypes.IFXObject = {}\n", testObj1.toString());

 // Test toString with object having fields
 testObj1.setField1("Test Value 1");
 testObj1.setField2("Test Value 2");
 String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                  "getField1 = Test Value 1\n" +
                  "getField2 = Test Value 2\n" +
                  "}";
 assertEquals(expected, testObj1.toString());
 }

 private void setField1(IFXObject obj, String value) {
 try {
 Field field1 = obj.getClass().getDeclaredField("field1");
 field1.setAccessible(true);
 field1.set(obj, value);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 e.printStackTrace();
 }
}

 private void setField2(IFXObject obj, String value) {
 try {
 Field field2 = obj.getClass().getDeclaredField("field2");
 field2.setAccessible(true);
 field2.set(obj, value);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 e.printStackTrace();
 }
}
}