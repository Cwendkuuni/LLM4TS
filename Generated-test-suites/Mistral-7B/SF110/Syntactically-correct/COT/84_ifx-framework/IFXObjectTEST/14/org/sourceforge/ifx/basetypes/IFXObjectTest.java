package org.sourceforge.ifx.basetypes.test;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.sourceforge.ifx.basetypes.IFXObject;

public class IFXObjectTest {

 private IFXObject createTestObject() {
 return new IFXObject() {
 };
 }

 @Test
 public void testEquals_NullObjects() {
 IFXObject obj1 = null;
 IFXObject obj2 = null;
 assertTrue(obj1.equals(obj2));
 }

 @Test
 public void testEquals_SameObject() {
 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = obj1;
 assertTrue(obj1.equals(obj2));
 }

 @Test
 public void testEquals_DifferentClasses() {
 IFXObject obj1 = new IFXObject();
 String obj2 = "Test String";
 assertFalse(obj1.equals(obj2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEquals_InvalidArgument() {
 IFXObject obj1 = new IFXObject();
 Object obj2 = null; // or any non-IFXObject
 obj1.equals(obj2);
 }

 @Test
 public void testToString_EmptyObject() {
 IFXObject obj = new IFXObject();
 assertEquals("IFXObject = {}", obj.toString());
 }

 @Test
 public void testToString_ArrayObject() {
 IFXObject[] arrayObj = new IFXObject[3];
 for (int i = 0; i < arrayObj.length; ++i) {
 arrayObj[i] = new IFXObject();
 }
 assertEquals("IFXObject[] = {IFXObject = {}, IFXObject = {}, IFXObject = {}]", obj.toString(arrayObj, 0));
 }

 @Test
 public void testToString_NestedObject() {
 IFXObject outerObj = new IFXObject() {
 IFXObject innerObj = new IFXObject();
 };
 String expected = "IFXObject = {class org.sourceforge.ifx.basetypes.IFXObject = {class org.sourceforge.ifx.basetypes.IFXObject = {}}";
 assertEquals(expected, outerObj.toString(outerObj, 0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToString_InvalidArgument() {
 IFXObject obj = new IFXObject();
 obj.toString(null);
 }

 @Test
 public void testDoCheckEquals_SameObjects() {
 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = obj1;
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEquals_DifferentClasses() {
 IFXObject obj1 = new IFXObject();
 String obj2 = "Test String";
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEquals_NullObjects() {
 IFXObject obj1 = null;
 IFXObject obj2 = null;
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEquals_DifferentNumberOfMethods() {
 IFXObject obj1 = new IFXObject() {
 };
 IFXObject obj2 = new IFXObject() {
 int someField = 1;
 };
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEquals_DifferentMethodValues() {
 IFXObject obj1 = new IFXObject() {
 int someField = 1;
 };
 IFXObject obj2 = new IFXObject() {
 int someField = 2;
 };
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoCheckEquals_InvalidArguments() {
 IFXObject obj1 = new IFXObject();
 Object obj2 = null; // or any non-IFXObject
 obj1.doCheckEquals(obj1, obj2);
 }

 @Test
 public void testDoCheckEquals_ArrayObjects() {
 IFXObject obj1 = new IFXObject() {
 IFXObject[] someField;
 };
 IFXObject obj2 = new IFXObject() {
 IFXObject[] someField;
 };
 IFXObject[] obj1Array = new IFXObject[2];
 IFXObject[] obj2Array = new IFXObject[2];
 for (int i = 0; i < obj1Array.length; ++i) {
 obj1Array[i] = new IFXObject();
 }
 for (int i = 0; i < obj2Array.length; ++i) {
 obj2Array[i] = new IFXObject();
 }
 try {
 obj1.doCheckEquals(obj1, obj2);
 fail();
 } catch (AssertionError e) {
 // expected
 }
 IFXObject[] obj1MatchedArray = new IFXObject[2];
 IFXObject[] obj2MatchedArray = new IFXObject[2];
 for (int i = 0; i < obj1Array.length; ++i) {
 obj1MatchedArray[i] = obj1Array[i];
 obj2MatchedArray[i] = obj2Array[i];
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 }
 }
}