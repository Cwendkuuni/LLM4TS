package org.sourceforge.ifx.basetypes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class IFXObjectTest {

 private IFXObject obj1;
 private IFXObject obj2;
 private IFXObject obj3;
 private IFXObject obj4;

 @Before
 public void setUp() throws InstantiationException, IllegalAccessException {
 obj1 = new IFXObject();
 obj2 = new IFXObject();
 obj3 = new IFXObject();
 obj4 = new IFXObject();
 }

 @Test
 public void testEquals() {
 // Test case 1: this object is equal to itself
 assertTrue(obj1.equals(obj1));

 // Test case 2: this object is not equal to null
 assertFalse(obj1.equals(null));

 // Test case 3: this object is not equal to an instance of a different class
 assertFalse(obj1.equals(new Object()));

 // Test case 4: this object is not equal to another instance of the same class with different class name
 obj2 = Mockito.spy(new IFXObject());
 Mockito.doReturn("TestClass2").when(obj2).getClass().getName();
 assertFalse(obj1.equals(obj2));

 // Test case 5: this object is equal to another instance of the same class with the same fields
 obj3 = new IFXObject();
 Mockito.doReturn(new Integer(5)).when(obj3).getClass().getMethod("getIntValue").invoke(obj3);
 obj4 = new IFXObject();
 Mockito.doReturn(new Integer(5)).when(obj4).getClass().getMethod("getIntValue").invoke(obj4);
 assertTrue(obj3.equals(obj4));

 // Test case 6: this object is equal to another instance of the same class with arrays of the same elements
 obj3 = new IFXObject();
 final int[] array1 = new int[] {1, 2, 3};
 Mockito.doReturn(array1).when(obj3).getClass().getMethod("getIntArray").invoke(obj3);
 obj4 = new IFXObject();
 final int[] array2 = new int[] {1, 2, 3};
 Mockito.doReturn(array2).when(obj4).getClass().getMethod("getIntArray").invoke(obj4);
 assertTrue(obj3.equals(obj4));

 // Test case 7: this object is not equal to another instance of the same class with arrays of different lengths
 obj3 = new IFXObject();
 final int[] array1 = new int[] {1, 2, 3};
 Mockito.doReturn(array1).when(obj3).getClass().getMethod("getIntArray").invoke(obj3);
 obj4 = new IFXObject();
 final int[] array2 = new int[] {1, 2};
 Mockito.doReturn(array2).when(obj4).getClass().getMethod("getIntArray").invoke(obj4);
 assertFalse(obj3.equals(obj4));

 // Test case 8: this object is not equal to another instance of the same class with arrays of different elements
 obj3 = new IFXObject();
 final int[] array1 = new int[] {1, 2, 3};
 Mockito.doReturn(array1).when(obj3).getClass().getMethod("getIntArray").invoke(obj3);
 obj4 = new IFXObject();
 final int[] array2 = new int[] {1, 2, 4};
 Mockito.doReturn(array2).when(obj4).getClass().getMethod("getIntArray").invoke(obj4);
 assertFalse(obj3.equals(obj4));

 // Test case 9: this object is not equal to another instance of the same class with an IBaseType field of the same value
 obj3 = new IFXObject();
 Mockito.doReturn(new Integer(5)).when(obj3).getClass().getMethod("getIntValue").invoke(obj3);
 obj4 = new IFXObject();
 Mockito.doReturn(new Integer(5)).when(obj4).getClass().getMethod("getIntValue").invoke(obj4);
 Mockito.doReturn(new String("TestString")).when(obj4).getClass().getMethod("getStringValue").invoke(obj4);
 assertFalse(obj3.equals(obj4));

 // Test case 10: this object is equal to another instance of the same class with an IBaseType field of the same value and same class name
IFXObject obj5 = new IFXObject();
Mockito.doReturn(new Integer(5)).when(obj5).getClass().getMethod("getIntValue").invoke(obj5);
Mockito.doReturn(new String("TestString")).when(obj5).getClass().getMethod("getStringValue").invoke(obj5);
obj4 = new IFXObject();
Mockito.doReturn(new Integer(5)).when(obj4).getClass().getMethod("getIntValue").invoke(obj4);
Mockito.doReturn(new String("TestString")).when(obj4).getClass().getMethod("getStringValue").invoke(obj4);
assertTrue(obj3.equals(obj4));
 }

 @Test
 public void testToString() {
 // Test case 1: toString returns the correct class name and empty brackets when the object has no fields
 assertEquals("IFXObject = {}", obj1.toString());

 // Test case 2: toString returns the correct class name and fields with primitive types
 obj1 = new IFXObject();
 Mockito.doReturn(new Integer(5)).when(obj1).getClass().getMethod("getIntValue").invoke(obj1);
 assertEquals("IFXObject = {getIntValue = 5}", obj1.toString());

 // Test case 3: toString returns the correct class name and fields with arrays
 obj1 = new IFXObject();
 final int[] array1 = new int[] {1, 2, 3};
 Mockito.doReturn(array1).when(obj1).getClass().getMethod("getIntArray").invoke(obj1);
 assertEquals("IFXObject = {getIntArray=[1, 2, 3]}", obj1.toString());

 // Test case 4: toString returns the correct class name and nested fields
 obj1 = new IFXObject();
 final int[] array1 = new int[] {1, 2, 3};
 Mockito.doReturn(array1).when(obj1).getClass().getMethod("getIntArray").invoke(obj1);
 final IFXObject nestedObj1 = new IFXObject();
 Mockito.doReturn(new Integer(5)).when(nestedObj1).getClass().getMethod("getIntValue").invoke(nestedObj1);
 final int[] nestedArray1 = new int[] {1, 2, 3};
 Mockito.doReturn(nestedArray1).when(nestedObj1).getClass().getMethod("getIntArray").invoke(nestedObj1);
 Mockito.doReturn(nestedObj1).when(obj1).getClass().getMethod("getNestedObj").invoke(obj1);
 assertEquals("IFXObject = {getIntArray=[1, 2, 3], getNestedObj={getIntValue=5, getIntArray=[1, 2, 3]}}", obj1.toString());
 }
}