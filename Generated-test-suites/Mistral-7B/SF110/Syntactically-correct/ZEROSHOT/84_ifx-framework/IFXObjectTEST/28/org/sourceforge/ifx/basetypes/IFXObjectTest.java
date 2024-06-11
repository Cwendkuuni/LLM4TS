package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import org.junit.Assert.*;

import java.lang.reflect.*;

public class IFXObjectTest {

 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = new IFXObject();
 IFXObject obj3 = new IFXObject();
 IFXObject objArray1 = new IFXObject[3];
 IFXObject objArray2 = new IFXObject[3];
 IBaseType baseType1 = new BaseType("TestString1");
 IBaseType baseType2 = new BaseType("TestString2");

 @Test
 public void testEquals() {
 // Test equals method when both objects are null
 Assert.assertTrue(obj1.equals(obj1));
 Assert.assertTrue(obj1.equals(obj1));
 Assert.assertTrue(obj1 == obj1);

 Assert.assertFalse(obj1.equals(null));
 Assert.assertFalse(null.equals(obj1));

 // Test equals method when both objects are the same instance
 Assert.assertTrue(obj1.equals(obj1));

 // Test equals method when one object is null
 Assert.assertFalse(obj1.equals(null));

 // Test equals method when both objects have different classes
 Assert.assertFalse(obj1.equals(new Object()));

 // Test equals method when comparing same instance with different class
 Assert.assertFalse(obj1.equals("TestString"));

 // Test equals method when comparing different instances of the same class
 Assert.assertFalse(obj1.equals(obj2));

 // Test equals method when comparing instances with same values
 obj1.setField("TestField1", baseType1);
 obj2.setField("TestField1", baseType1);

 Assert.assertTrue(obj1.equals(obj2));
 Assert.assertTrue(obj2.equals(obj1));

 // Test equals method when comparing instances with different values
 obj1.setField("TestField1", baseType1);
 obj2.setField("TestField1", baseType2);

 Assert.assertFalse(obj1.equals(obj2));
 Assert.assertFalse(obj2.equals(obj1));

 // Test equals method when comparing array instances
 objArray1[0] = obj1;
 objArray1[1] = obj2;
 objArray2[0] = obj1;
 objArray2[1] = obj3;

 Assert.assertFalse(objArray1.equals(objArray2));
 Assert.assertFalse(objArray2.equals(objArray1));

 // Test equals method when comparing array instances with the same elements
 objArray1[2] = obj1;
 objArray2[2] = obj1;

 Assert.assertArrayEquals(objArray1, objArray2);
 Assert.assertArrayEquals(objArray2, objArray1);
 }

 @Test
 public void testToString() {
 // Test toString method with empty object
 Assert.assertEquals("IFXObject = {}\n", obj1.toString());

 // Test toString method with object having fields
 obj1.setField("TestField1", baseType1);
 obj1.setField("TestField2", baseType2);

 Assert.assertEquals("IFXObject = {\nTestField1 = TestString1\nTestField2 = TestString2\n}", obj1.toString());
 }

 @Test
 public void testStringify() {
 // Test stringify method with empty object
 Assert.assertEquals("IFXObject = {}\n", obj1.stringify(obj1, 0));

 // Test stringify method with object having fields
 obj1.setField("TestField1", baseType1);
 obj1.setField("TestField2", baseType2);

 Assert.assertEquals("IFXObject = {\nTestField1 = TestString1\nTestField2 = TestString2\n}", obj1.stringify(obj1, 0));
 }

 @Test
 public void testDoCheckEquals() {
 // Test doCheckEquals method when both objects are null
 Assert.assertTrue(obj1.doCheckEquals(obj1, obj1));

 // Test doCheckEquals method when both objects are the same instance
 Assert.assertTrue(obj1.doCheckEquals(obj1, obj1));

 // Test doCheckEquals method when comparing same instance with different class
 Assert.assertFalse(obj1.doCheckEquals(obj1, "TestString"));

 // Test doCheckEquals method when comparing different instances of the same class
 assertFalse(obj1.doCheckEquals(obj1, obj2));

 // Test doCheckEquals method when comparing instances with same values
 obj1.setField("TestField1", baseType1);
 obj2.setField("TestField1", baseType1);

 Assert.assertTrue(obj1.doCheckEquals(obj1, obj2));
 Assert.assertTrue(obj2.doCheckEquals(obj1, obj1));

 // Test doCheckEquals method when comparing instances with different values
 obj1.setField("TestField1", baseType1);
 obj2.setField("TestField1", baseType2);

 Assert.assertFalse(obj1.doCheckEquals(obj1, obj2));
 Assert.assertFalse(obj2.doCheckEquals(obj1, obj1));

 // Test doCheckEquals method when comparing array instances
 objArray1[0] = obj1;
 objArray1[1] = obj2;
 objArray2[0] = obj1;
 objArray2[1] = obj3;

 Assert.assertFalse(obj1.doCheckEquals(objArray1, objArray2));
 Assert.assertFalse(objArray2.doCheckEquals(objArray1));

 // Test doCheckEquals method when comparing array instances with the same elements
 objArray1[2] = obj1;
 objArray2[2] = obj1;

 Assert.assertTrue(obj1.doCheckEquals(objArray1, objArray2));
 Assert.assertTrue(objArray2.doCheckEquals(objArray1));
 }

 // Helper method to set field values in the IFXObject
 private void setField(IFXObject obj, String fieldName, IBaseType value) {
 try {
 Field field = obj.getClass().getDeclaredField(fieldName);
 field.setAccessible(true);
 field.set(obj, value);
 }
 catch (NoSuchFieldException | IllegalAccessException e) {
 e.printStackTrace();
 }
 }
}