package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import org.junit.Assert.*;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class IFXObjectTest {

 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = new IFXObject();
 IFXObject obj3 = new IFXObject();
 IFXObject obj4 = new IFXObject();
 IFXObject[] objArray = new IFXObject[2];
 IBaseType baseType = new Integer(1);

 @Test
 public void testToString() {
 System.out.println("Testing toString method:");
 assertEquals("IFXObject = {getClass = class org.sourceforge.ifx.basetypes.IFXObject, get = null, get = NULL}", obj1.toString());
 obj1.setField("Test1", baseType);
 obj1.setField("Test2", obj2);
 obj1.setField("Test3", objArray);
 obj1.setField("Test4", new String[]{"Test1", "Test2"});
 assertEquals("IFXObject = {getClass = class org.sourceforge.ifx.basetypes.IFXObject, getTest1 = Test1, getTest2 = IFXObject = {getClass = class org.sourceforge.ifx.basetypes.IFXObject, getTest1 = null, getTest2 = null, getTest3 = [IFXObject = {getClass = class org.sourceforge.ifx.basetypes.IFXObject, getTest1 = null, getTest2 = null, getTest3 = null, getTest4 = [Test1, Test2]}, getTest3 = IFXObject = {getClass = class org.sourceforge.ifx.basetypes.IFXObject, getTest1 = null, getTest2 = null, getTest3 = null, getTest4 = [null, null]}, getTest4 = [Test1, Test2]}", obj1.toString());
 }

 @Test
 public void testEquals() {
 System.out.println("Testing equals method:");
 assertFalse(obj1.equals(obj2));
 obj2 = obj1;
 assertTrue(obj1.equals(obj2));
 obj2.setField("Test1", new Integer(2));
 assertFalse(obj1.equals(obj2));
 objArray[0] = obj1;
 objArray[1] = obj2;
 obj1.setField("Test3", objArray);
 obj2.setField("Test3", objArray);
 assertTrue(obj1.equals(obj2));
 obj2.setField("Test1", new Integer(3));
 assertFalse(obj1.equals(obj2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStringifyWithNullObject() {
 System.out.println("Testing stringify method with null object:");
 try {
 IFXObject nullObj = null;
 IFXObject.stringify(nullObj, 0);
 fail("Expected IllegalArgumentException but got none");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testStringifyWithPrimitiveType() {
 System.out.println("Testing stringify method with primitive type:");
 obj1.setField("Test1", baseType);
 assertEquals("IFXObject = {getClass = class org.sourceforge.ifx.basetypes.IFXObject, getTest1 = 1}", obj1.toString());
 }

 @Test
 public void testStringifyWithStringArray() {
 System.out.println("Testing stringify method with String array:");
 obj1.setField("Test4", new String[]{"Test1", "Test2"});
 assertEquals("IFXObject = {getClass = class org.sourceforge.ifx.basetypes.IFXObject, getTest1 = null, getTest2 = null, getTest3 = null, getTest4 = [Test1, Test2]}", obj1.toString());
 }

 @Test
 public void testStringifyWithArray() {
 System.out.println("Testing stringify method with array:");
 objArray[0] = obj1;
 objArray[1] = obj2;
 obj1.setField("Test3", objArray);
 obj2.setField("Test3", objArray);
 assertEquals("IFXObject = {getClass = class org.sourceforge.ifx.basetypes.IFXObject, getTest1 = null, getTest2 = null, getTest3 = [IFXObject = {getClass = class org.sourceforge.ifx.basetypes.IFXObject, getTest1 = null, getTest2 = null, getTest3 = null, getTest4 = [Test1, Test2]}, IFXObject = {getClass = class org.sourceforge.ifx.basetypes.IFXObject, getTest1 = null, getTest2 = null, getTest3 = null, getTest4 = [Test1, Test2]}]", obj1.toString());
 }

 @Test
 public void testDeepEquals() throws IllegalAccessException, InvocationTargetException {
 System.out.println("Testing deep equals method:");
 IFXObject obj5 = new IFXObject();
 obj5.setField("Test1", baseType);
 obj5.setField("Test2", baseType);
 obj5.setField("Test3", objArray);
 objArray[0] = obj1;
 objArray[1] = obj2;
 obj1.setField("Test3", objArray);
 obj2.setField("Test3", objArray);
 assertTrue(IFXObject.doCheckEquals(obj1, obj5));
 obj2.setField("Test1", new Integer(3));
 assertFalse(IFXObject.doCheckEquals(obj1, obj2));
 }

 private void setField(final String fieldName, final Object value) throws IllegalAccessException {
 final Field field = IFXObject.class.getDeclaredField(fieldName);
 field.setAccessible(true);
 field.set(this, value);
 }

}