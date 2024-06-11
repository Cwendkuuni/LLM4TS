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
 assertTrue(obj1.equals(obj1));
 assertTrue(obj1.equals(obj2)); // different instance but same class
 IFXObject obj3 = new Object();
 assertFalse(obj1.equals(obj3)); // different classes
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualsNull() {
 IFXObject obj1 = createIFXObject();
 assertFalse(obj1.equals(null));
 }

 @Test
 public void testToString() {
 IFXObject obj = createIFXObject();
 assertEquals("IFXObject = {...}", obj.toString());
 }

 @Test
 public void testToStringWithNestedArray() {
 IFXObject obj1 = createIFXObject();
 IFXObject obj2 = createIFXObject();
 Object[] array1 = { obj1 };
 Object[] array2 = { obj2 };
 Object[] array3 = { array1 };
 IFXObject obj = new IFXObject() {
 @Override
 public Object[] getClass() {
 return (Object[])getClass().getDeclaredField("class").get(this);
 }
 };
 obj.getClass()[0] = "CustomClass";
 obj.getClass()[1] = new Class[] { IFXObject.class, Object[].class };
 try {
 Field classField = IFXObject.class.getDeclaredField("class");
 classField.setAccessible(true);
 obj.getClass()[0] = classField.get(obj);
 } catch (IllegalAccessException e) {
 e.printStackTrace();
 }
 obj.getClass()[2] = array3;
 assertEquals("CustomClass = {IFXObject = {...}, [IFXObject = {...}]}", obj.toString());
 }

 @Test
 public void testToStringWithNestedObjects() {
 IFXObject obj1 = createIFXObject();
 IFXObject obj2 = createIFXObject();
 IFXObject obj3 = createIFXObject();
 Object[] array1 = { obj1, obj2 };
 IFXObject obj = new IFXObject() {
 @Override
 public Object[] getClass() {
 return (Object[])getClass().getDeclaredField("class").get(this);
 }
 };
 obj.getClass()[0] = "CustomClass";
 obj.getClass()[1] = new Class[] { IFXObject.class, Object[].class };
 try {
 Field classField = IFXObject.class.getDeclaredField("class");
 classField.setAccessible(true);
 obj.getClass()[0] = classField.get(obj);
 } catch (IllegalAccessException e) {
 e.printStackTrace();
 }
 obj.getClass()[2] = new Object[] { obj3, array1 };
 assertEquals("CustomClass = {IFXObject = {...}, IFXObject = {...}, [IFXObject = {...}, IFXObject = {...}]}", obj.toString());
 }

 @Test
 public void testToStringWithNullValues() {
 IFXObject obj1 = createIFXObject();
 IFXObject obj2 = createIFXObject();
 Object[] array1 = { obj1, null };
 IFXObject obj = new IFXObject() {
 @Override
 public Object[] getClass() {
 return (Object[])getClass().getDeclaredField("class").get(this);
 }
 };
 obj.getClass()[0] = "CustomClass";
 obj.getClass()[1] = new Class[] { IFXObject.class, Object[].class };
 try {
 Field classField = IFXObject.class.getDeclaredField("class");
 classField.setAccessible(true);
 obj.getClass()[0] = classField.get(obj);
 } catch (IllegalAccessException e) {
 e.printStackTrace();
 }
 obj.getClass()[2] = array1;
 assertEquals("CustomClass = {IFXObject = {...}, [IFXObject = {...}, NULL]}", obj.toString());
 }

 @Test(expected = Exception.class)
 public void testToStringWithException() {
 IFXObject obj = createIFXObject();
 throw new RuntimeException("Test Exception");
 }

 @Test
 public void testDoCheckEquals() {
 IFXObject obj1 = createIFXObject();
 IFXObject obj2 = createIFXObject();
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsDifferentClasses() {
 IFXObject obj1 = createIFXObject();
 Object obj2 = new Object();
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsDifferentMethods() {
 IFXObject obj1 = createIFXObject();
 IFXObject obj2 = createIFXObject();
 IFXObject obj3 = createIFXObject();
 Method method1 = IFXObject.class.getMethod("getString");
 Method method2 = IFXObject.class.getMethod("getClass");
 Object[] array1 = { obj1 };
 Object[] array2 = { obj2 };
 Object[] array3 = { obj3 };
 IFXObject obj = new IFXObject() {
 @Override
 public Object[] getClass() {
 return (Object[])getClass().getDeclaredField("class").get(this);
 }
 };
 obj.getClass()[0] = "CustomClass";
 obj.getClass()[1] = new Class[] { IFXObject.class, Object[].class };
 try {
 Field classField = IFXObject.class.getDeclaredField("class");
 classField.setAccessible(true);
 obj.getClass()[0] = classField.get(obj);
 } catch (IllegalAccessException e) {
 e.printStackTrace();
 }
 obj.getClass()[2] = array1;
 try {
 Object retVal1 = method1.invoke(obj1, (Object[])null);
 Object retVal2 = method1.invoke(obj2, (Object[])null);
 assertEquals(retVal1, retVal2);
 Object retVal3 = method2.invoke(obj3, (Object[])null);
 assertFalse(obj.doCheckEquals(obj, new Object() {
 @Override
 public Object[] getClass() {
 return new Object[] { obj.getClass(), retVal3.getClass() };
 }
 }));
 } catch (IllegalAccessException | InvocationTargetException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testDoCheckEqualsNullObjects() {
 IFXObject obj1 = null;
 IFXObject obj2 = null;
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsNullObjectAndNotNull() {
 IFXObject obj1 = createIFXObject();
 IFXObject obj2 = null;
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsDifferentClassesWithSameMethods() {
 IFXObject obj1 = createIFXObject();
 String obj2 = "Test String";
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsWithArrays() {
 IFXObject obj1 = createIFXObject();
 IFXObject obj2 = createIFXObject();
 Object[] array1 = { obj1 };
 Object[] array2 = { obj2 };
 IFXObject obj = new IFXObject() {
 @Override
 public Object[] getClass() {
 return (Object[])getClass().getDeclaredField("class").get(this);
 }
 };
 obj.getClass()[0] = "CustomClass";
 obj.getClass()[1] = new Class[] { IFXObject.class, Object[].class };
 try {
 Field classField = IFXObject.class.getDeclaredField("class");
 classField.setAccessible(true);
 obj.getClass()[0] = classField.get(obj);
 } catch (IllegalAccessException e) {
 e.printStackTrace();
 }
 obj.getClass()[2] = array1;
 IFXObject obj3 = createIFXObject();
 IFXObject[] array3 = { obj3 };
 try {
 Object retVal1 = array1[0];
 Object retVal2 = array2[0];
 assertEquals(retVal1, retVal2);
 Object retVal3 = array3[0];
 assertFalse(obj.doCheckEquals(obj, new Object() {
 @Override
 public Object[] getClass() {
 return new Object[] { obj.getClass(), array3.getClass() };
 }
 }));
 } catch (IllegalAccessException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testDoCheckEqualsWithNullArrays() {
 IFXObject obj1 = createIFXObject();
 IFXObject obj2 = createIFXObject();
 Object[] array1 = null;
 Object[] array2 = null;
 IFXObject obj = new IFXObject() {
 @Override
 public Object[] getClass() {
 return (Object[])getClass().getDeclaredField("class").get(this);
 }
 };
 obj.getClass()[0] = "CustomClass";
 obj.getClass()[1] = new Class[] { IFXObject.class, Object[].class };
 try {
 Field classField = IFXObject.class.getDeclaredField("class");
 classField.setAccessible(true);
 obj.getClass()[0] = classField.get(obj);
 } catch (IllegalAccessException e) {
 e.printStackTrace();
 }
 obj.getClass()[2] = array1;
 IFXObject obj3 = createIFXObject();
 IFXObject[] array3 = null;
 try {
 Object retVal1 = array1[0];
 Object retVal2 = array2[0];
 assertEquals(retVal1, retVal2);
 assertFalse(obj.doCheckEquals(obj, new Object() {
 @Override
 public Object[] getClass() {
 return new Object[] { obj.getClass(), array3.getClass() };
 }
 }));
 } catch (IllegalAccessException e) {
 e.printStackTrace();
 }
 }
}