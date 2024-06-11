package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class IFXObjectTest {

 private IFXObject createTestObject() {
 return new IFXObject() {
 // Add any necessary instance variables and methods here
 };
 }

 @Test
 public void testEquals() throws Exception {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = createTestObject();
 IFXObject obj3 = createTestObject();

 assertEquals(obj1, obj1);
 assertFalse(obj1.equals(null));
 assertFalse(obj1.equals(obj2));
 assertFalse(obj1.equals(obj3));

 obj1.getClass().getMethod("setX", int.class).invoke(obj1, 1);
 obj2.getClass().getMethod("setX", int.class).invoke(obj2, 1);
 obj3.getClass().getMethod("setX", int.class).invoke(obj3, 2);

 assertEquals(obj1, obj1);
 assertEquals(obj1, obj2);
 assertFalse(obj1.equals(obj3));
 }

 @Test
 public void testToString() throws Exception {
 IFXObject obj = createTestObject();
 obj.getClass().getMethod("setX", int.class).invoke(obj, 1);
 obj.getClass().getMethod("setY", int.class).invoke(obj, 2);
 obj.getClass().getMethod("setZ", int.class).invoke(obj, 3);

 String expected = "IFXObject = {\n x = 1\n y = 2\n z = 3\n}";
 String actual = obj.toString();

 assertTrue(actual.startsWith(expected));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToStringWithNullObject() {
 IFXObject obj = null;
 obj.toString();
 }

 @Test
 public void testStringify() throws Exception {
 IFXObject obj = createTestObject();
 obj.getClass().getMethod("setX", int.class).invoke(obj, 1);
 obj.getClass().getMethod("setY", int.class).invoke(obj, 2);
 obj.getClass().getMethod("setZ", int.class).invoke(obj, 3);

 String expected = "IFXObject = {\n x = 1\n y = 2\n z = 3\n}";
 String actual = obj.stringify(obj, 0);

 assertTrue(actual.startsWith(expected));
 }

 @Test
 public void testDoCheckEquals() throws Exception {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = createTestObject();
 IFXObject obj3 = createTestObject();
 IFXObject obj4 = createTestObject();

 obj1.getClass().getMethod("setX", int.class).invoke(obj1, 1);
 obj2.getClass().getMethod("setX", int.class).invoke(obj2, 1);
 obj3.getClass().getMethod("setX", int.class).invoke(obj3, 2);
 obj4.getClass().getMethod("setX", int.class).invoke(obj4, 1);

 obj1.getClass().getMethod("setY", int.class).invoke(obj1, 2);
 obj2.getClass().getMethod("setY", int.class).invoke(obj2, 3);
 obj3.getClass().getMethod("setY", int.class).invoke(obj3, 2);
 obj4.getClass().getMethod("setY", int.class).invoke(obj4, 2);

 obj1.getClass().getMethod("setZ", int[].class).invoke(obj1, new Object[]{ new int[]{1, 2, 3} });
 obj2.getClass().getMethod("setZ", int[].class).invoke(obj2, new Object[]{ new int[]{1, 2, 3} });
 obj3.getClass().getMethod("setZ", int[].class).invoke(obj3, new Object[]{ new int[]{1, 2, 4} });
 obj4.getClass().getMethod("setZ", int[].class).invoke(obj4, new Object[]{ new int[]{1, 2, 3} });

 assertTrue(obj1.doCheckEquals(obj1, obj1));
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 assertFalse(obj1.doCheckEquals(obj1, obj3));
 assertFalse(obj1.doCheckEquals(obj1, obj4));
 }

 @Test
 public void testDoCheckEqualsWithDifferentClasses() throws Exception {
 IFXObject obj1 = createTestObject();
 String obj2 = "Test";

 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsWithNullObject() throws Exception {
 IFXObject obj1 = createTestObject();
 assertFalse(obj1.doCheckEquals(obj1, null));
 }

 @Test
 public void testDoCheckEqualsWithDifferentNumberOfMethods() throws Exception {
 IFXObject obj1 = new IFXObject() {
 };
 IFXObject obj2 = new IFXObject() {
 int x;
 int y;
 int z;
 int w;
 };

 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsWithDifferentMethodNames() throws Exception {
 IFXObject obj1 = new IFXObject() {
 int x;
 int y;
 int z;
 };
 IFXObject obj2 = new IFXObject() {
 int x;
 int y;
 int w;
 };

 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsWithDifferentMethodReturnTypes() throws Exception {
 IFXObject obj1 = new IFXObject() {
 int x;
 int y;
 int z;
 };
 IFXObject obj2 = new IFXObject() {
 int x;
 String y;
 int z;
 };

 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsWithArrayMethodsWithDifferentSizes() throws Exception {
 IFXObject obj1 = new IFXObject() {
 int x;
 int y;
 int[] z;
 };
 IFXObject obj2 = new IFXObject() {
 int x;
 int y;
 int[] z;
 int[] w;
 };

 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsWithArrayMethodsWithNullValues() throws Exception {
 IFXObject obj1 = new IFXObject() {
 int x;
 int y;
 int[] z;
 };
 IFXObject obj2 = new IFXObject() {
 int x;
 int y;
 int[] z;
 int[] w;
 };

 obj1.getClass().getMethod("setZ", int[].class).invoke(obj1, new Object[]{ null });
 obj2.getClass().getMethod("setW", int[].class).invoke(obj2, new Object[]{ null });

 assertTrue(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsWithBaseTypeMethods() throws Exception {
 IFXObject obj1 = new IFXObject() {
 int x;
 String y;
 };
 IFXObject obj2 = new IFXObject() {
 String x;
 int y;
 };

 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsWithComplexObjects() throws Exception {
 IFXObject obj1 = new IFXObject() {
 int x;
 String y;
 IFXObject z;
 };
 IFXObject obj2 = new IFXObject() {
 int x;
 String y;
 IFXObject z;
 };
 IFXObject obj3 = new IFXObject() {
 int x;
 String y;
 IFXObject z;
 };

 obj1.getClass().getMethod("setX", int.class).invoke(obj1, 1);
 obj1.getClass().getMethod("setY", String.class).invoke(obj1, "Test");
 obj1.getClass().getMethod("setZ", IFXObject.class).invoke(obj1, obj2);
 obj2.getClass().getMethod("setX", int.class).invoke(obj2, 1);
 obj2.getClass().getMethod("setY", String.class).invoke(obj2, "Test");
 obj3.getClass().getMethod("setX", int.class).invoke(obj3, 1);
 obj3.getClass().getMethod("setY", String.class).invoke(obj3, "Test");

 obj1.getClass().getMethod("setZ", IFXObject.class).invoke(obj1, obj3);

 assertTrue(obj1.doCheckEquals(obj1, obj1));
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 assertFalse(obj1.doCheckEquals(obj1, obj3));
 }

 @Test(expected = InvocationTargetException.class)
 public void testDoCheckEqualsWithException() throws Exception {
 IFXObject obj1 = new IFXObject() {
 int x;
 };
 IFXObject obj2 = new IFXObject() {
 int x;
 };
 obj1.getClass().getMethod("setX", int.class).invoke(obj1, new Object[]{ new RuntimeException("Test") });

 assertTrue(obj1.doCheckEquals(obj1, obj2));
 }
}