package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class IFXObjectTest {

 private static final Class<IFXObject> IFX_OBJECT_CLASS = IFXObject.class;

 @Test
 public void testEquals_sameInstance() {
     final IFXObject obj = new IFXObject();
     assertTrue(obj.equals(obj));
 }

 @Test
 public void testEquals_null() {
     final IFXObject obj = new IFXObject();
     assertFalse(obj.equals(null));
 }

 @Test
 public void testEquals_differentTypes() {
     final IFXObject obj1 = new IFXObject();
     final Object obj2 = new Object();
     assertFalse(obj1.equals(obj2));
 }

 @Test
 public void testEquals_differentClasses() {
     final IFXObject obj1 = new IFXObject();
     final IFXObject obj2 = new String("test");
     assertFalse(obj1.equals(obj2));
 }

 @Test
 public void testEquals_unequalObjects() {
     final IFXObject obj1 = new IFXObject();
     final IFXObject obj2 = new IFXObject();
     assertFalse(obj1.equals(obj2));
 }

 @Test
 public void testEquals_equalObjectsWithSameClass() {
     final IFXObject obj1 = new IFXObject();
     final IFXObject obj2 = new IFXObject();
     assertTrue(obj1.equals(obj2));
 }

 @Test
 public void testEquals_equalObjectsWithDifferentMethods() {
     final IFXObject obj1 = new IFXObject();
     final IFXObject obj2 = new IFXObject() {
         @Override
         public String toString() {
             return "test";
         }
     };
     assertTrue(obj1.equals(obj2));
 }

 @Test
 public void testToString_nullObject() {
     assertNull(new IFXObject().toString(null));
 }

 @Test
 public void testToString_emptyObject() {
     final IFXObject obj = new IFXObject();
     assertEquals("", obj.toString(obj));
 }

 @Test
 public void testToString_arrayObject() throws IllegalAccessException, InvocationTargetException {
     final IFXObject obj1 = new IFXObject();
     final IFXObject[] objArray = {obj1, obj1};
     final String expected = "IFXObject = {getClass = class [LIFXObject;]\nget = IFXObject\nget = IFXObject\n}";
     assertEquals(expected, obj1.toString(objArray));
 }

 @Test
 public void testToString_baseTypeObject() {
     final int intValue = 42;
     final IBaseType intObj = new Integer(intValue);
     final String expected = Integer.toString(intValue);
     assertEquals(expected, new IFXObject().toString(intObj));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToString_invalidObject() {
     new IFXObject().toString(new Object() {
         @Override
         public Method[] getMethods() {
             throw new RuntimeException();
         }
     });
 }

 @Test
 public void testDoCheckEquals_nullObjects() {
     assertFalse(IFXObject.doCheckEquals(null, null));
 }

 @Test
 public void testDoCheckEquals_sameObjects() {
     final IFXObject obj1 = new IFXObject();
     final IFXObject obj2 = obj1;
     assertTrue(IFXObject.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEquals_equalObjectsWithSameClass() {
     final IFXObject obj1 = new IFXObject();
     final IFXObject obj2 = new IFXObject();
     assertTrue(IFXObject.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEquals_unequalObjectsWithDifferentClasses() {
     final IFXObject obj1 = new IFXObject();
     final String obj2 = new String("test");
     assertFalse(IFXObject.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEquals_unequalObjectsWithArrays() throws IllegalAccessException, InvocationTargetException {
     final IFXObject obj1 = new IFXObject();
     final IFXObject obj2 = new IFXObject();
     final Object[] array1 = new Object[] {obj1, obj1};
     final Object[] array2 = new Object[] {obj2, obj2};
     final Method getClassMethod = IFX_OBJECT_CLASS.getMethod("getClass");
     final Method getMethod = IFX_OBJECT_CLASS.getMethod("get");
     final Object[] methods1 = getMethods(obj1);
     final Object[] methods2 = getMethods(obj2);
     final boolean isMethodArrayEqual = Array.equals(methods1, methods2);
     final boolean isClassesEqual = Array.equals(new Object[] {getClassMethod, getMethod}, getMethods(array1));
     final boolean isExpectedResult = isMethodArrayEqual && isClassesEqual;
     assertTrue(IFXObject.doCheckEquals(array1, array2) == isExpectedResult);
 }

 @Test
 public void testDoCheckEquals_unequalObjectsWithArraysDifferentLength() throws IllegalAccessException, InvocationTargetException {
     final IFXObject obj1 = new IFXObject();
     final IFXObject obj2 = new IFXObject();
     final Object[] array1 = new Object[] {obj1, obj1};
     final Object[] array2 = new Object[] {obj1};
     final Method getClassMethod = IFX_OBJECT_CLASS.getMethod("getClass");
     final Method getMethod = IFX_OBJECT_CLASS.getMethod("get");
     final Object[] methods1 = getMethods(obj1);
     final Object[] methods2 = getMethods(obj2);
     final boolean isMethodArrayEqual = Array.equals(methods1, methods2);
     final boolean isClassesEqual = Array.equals(new Object[] {getClassMethod, getMethod}, getMethods(array1));
     final boolean isExpectedResult = !isMethodArrayEqual && isClassesEqual;
     assertTrue(IFXObject.doCheckCheckEquals(array1, array2) == isExpectedResult);
 }

 @Test
 public void testDoCheckEquals_unequalObjectsWithArraysDifferentClasses() throws IllegalAccessException, InvocationTargetException {
     final IFXObject obj1 = new IFXObject();
     final String obj2 = new String("test");
     final Object[] array1 = new Object[] {obj1, obj1};
     final Object[] array2 = new Object[] {obj2, obj2};
     final Method getClassMethod = IFX_OBJECT_CLASS.getMethod("getClass");
     final Method getMethod = IFX_OBJECT_CLASS.getMethod("get");
     final Object[] methods1 = getMethods(obj1);
     final Object[] methods2 = getMethods(obj2);
     final boolean isMethodArrayEqual = Array.equals(methods1, methods2);
     final boolean isClassesEqual = Array.equals(new Object[] {getClassMethod, getMethod}, getMethods(array1));
     final boolean isExpectedResult = !isMethodArrayEqual && !isClassesEqual;
     assertTrue(IFXObject.doCheckEquals(array1, array2) == isExpectedResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoCheckEquals_nullArray() {
     IFXObject.doCheckEquals(null, new Object[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoCheckEquals_invalidArray() {
     final IFXObject obj1 = new IFXObject();
     final int[] intArray = {1, 2, 3};
     IFXObject.doCheckEquals(new Object[] {obj1}, intArray);
 }

 @Test(expected = InvocationTargetException.class)
 public void testDoCheckEquals_throwException() throws IllegalAccessException, InvocationTargetException {
     final IFXObject obj1 = new IFXObject();
     final IFXObject obj2 = new IFXObject() {
         @Override
         public Method[] getMethods() throws Exception {
             throw new RuntimeException();
         }
     };
     assertThrows(InvocationTargetException.class, () -> IFXObject.doCheckEquals(new Object[] {obj1}, getMethods(obj2)));
 }

 private Object[] getMethods(final Object obj) throws IllegalAccessException, InvocationTargetException {
     final Method getMethodsMethod = IFX_OBJECT_CLASS.getDeclaredMethod("getMethods", (Class[])null);
     getMethodsMethod.setAccessible(true);
     return (Object[])getMethodsMethod.invoke(obj);
 }

}