package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Method;

public class IFXObjectTest {

 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = new IFXObject();
 IFXObject obj3 = new IFXObject();
 IFXObject obj4 = new IFXObject();
 IBaseType baseType1 = new BaseTypeInt(10);
 IBaseType baseType2 = new BaseTypeInt(20);
 IBaseType baseType3 = new BaseTypeString("test");
 IBaseType baseType4 = new BaseTypeString("test");

 @Test
 public void testEquals() {
 System.out.println("Testing equals method");
 // Test case for null objects
 assertEquals(null, null);
 assertFalse(obj1 == null);
 assertFalse(obj1.equals(null));
 // Test case for same object
 assertTrue(obj1.equals(obj1));
 // Test case for different objects of same class
 assertFalse(obj1.equals(obj2));
 // Test case for different objects of different classes
 assertFalse(obj1.equals(baseType1));
 // Test case for objects with equal fields
 obj1.setField1(baseType1);
 obj2.setField1(baseType1);
 assertTrue(obj1.equals(obj2));
 // Test case for objects with different fields
 obj1.setField1(baseType1);
 obj2.setField1(baseType2);
 assertFalse(obj1.equals(obj2));
 // Test case for objects with nested arrays
 IFXObject obj5 = new IFXObject();
 obj5.setField1(new BaseTypeInt[1]);
 IFXObject obj6 = new IFXObject();
 obj6.setField1(new BaseTypeInt[1]);
 Arrays.setAll(obj5.getField1(), new BaseTypeInt[]{baseType1});
 Arrays.setAll(obj6.getField1(), new BaseTypeInt[]{baseType2});
 assertFalse(obj5.equals(obj6));
 // Test case for objects with nested objects
 IFXObject obj7 = new IFXObject();
 obj7.setField1(new IFXObject());
 IFXObject obj8 = new IFXObject();
 obj8.setField1(new IFXObject());
 obj7.getField1().setField1(baseType1);
 obj8.getField1().setField1(baseType2);
 assertFalse(obj7.equals(obj8));
 // Test case for objects with nested BaseType objects
 obj7.getField1().setField1(baseType3);
 obj8.getField1().setField1(baseType4);
 assertFalse(obj7.equals(obj8));
 // Test case for objects with nested BaseType objects with equal strings
 obj7.getField1().setField1(baseType4);
 assertTrue(obj7.equals(obj8));
 }

 @Test
 public void testToString() {
 System.out.println("Testing toString method");
 // Test case for empty object
 assertEquals("org.sourceforge.ifx.basetypes.IFXObject = {}", obj1.toString());
 // Test case for object with single field
 obj1.setField1(baseType1);
 assertEquals("org.sourceforge.ifx.basetypes.IFXObject = {field1 = BaseTypeInt = Int = 10}", obj1.toString());
 // Test case for object with nested array
 obj1.setField1(new BaseTypeInt[1]);
 Arrays.setAll(obj1.getField1(), new BaseTypeInt[]{baseType1});
 assertEquals("org.sourceforge.ifx.basetypes.IFXObject = {field1 = [BaseTypeInt] = [Int = 10]}", obj1.toString());
 // Test case for object with nested object
 obj1.setField1(new IFXObject());
 obj1.getField1().setField1(baseType1);
 assertEquals("org.sourceforge.ifx.basetypes.IFXObject = {field1 = IFXObject = {field1 = BaseTypeInt = Int = 10}}", obj1.toString());
 }

 private void setField1(IFXObject obj, IBaseType value) {
 try {
 Method method = obj.getClass().getDeclaredMethod("setField1", IBaseType.class);
 method.setAccessible(true);
 method.invoke(obj, value);
 } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
 e.printStackTrace();
 }
}

 private IFXObject getField1(IFXObject obj) {
 try {
 Method method = obj.getClass().getDeclaredMethod("getField1");
 method.setAccessible(true);
 return (IFXObject)method.invoke(obj);
 } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
 e.printStackTrace();
 }
 return null;
}

}