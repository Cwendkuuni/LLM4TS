package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class IFXObjectTest {

 private IFXObject createTestObject() {
 return new IFXObject() {
 // Overriding equals method to make testing easier
 @Override
 public boolean equals(Object obj) {
 return super.equals(obj);
 }
 };
 }

 @Test
 public void testEquals() {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = createTestObject();
 IFXObject obj3 = new IFXObject();

 // Test equals with null object
 assertFalse(obj1.equals(null));

 // Test equals with same object
 assertTrue(obj1.equals(obj1));

 // Test equals with different classes
 assertFalse(obj1.equals(new Object()));

 // Test equals with different objects of the same class
 assertFalse(obj1.equals(obj2));

 // Test equals with same objects of different classes
 assertFalse(obj1.equals(obj3));

 // Test equals with objects having the same fields
 IFXObject obj4 = new IFXObject() {
 // Overriding toString method to make testing output more readable
 @Override
 public String toString() {
 return "IFXObject[field1=1, field2=\"test\"]";
 }
 };
 obj1.setField1(1);
 obj1.setField2("test");
 IFXObject obj5 = new IFXObject() {
 @Override
 public String toString() {
 return "IFXObject[field1=1, field2=\"test\"]";
 }
 };
 obj5.setField1(1);
 obj5.setField2("test");
 assertTrue(obj1.equals(obj5));
 }

 @Test
 public void testToString() {
 IFXObject obj = createTestObject();
 String expected = "IFXObject = {\n field1 = NULL\n field2 = NULL\n}";
 String actual = obj.toString();
 assertEquals(expected, actual);
 }

 @Test(expected = Exception.class)
 public void testToStringWithException() {
 IFXObject obj = createTestObject();
 obj.toString(); // Should throw an exception
 }

 @Test
 public void testStringifyGetMethod() throws IllegalAccessException, InvocationTargetException {
 IFXObject obj = createTestObject();
 Method getField1Method = IFXObject.class.getMethod("getField1");
 Object field1Value = getField1Method.invoke(obj);
 String expected = "field1 = NULL";
 String actual = obj.stringify(obj, 0);
 assertTrue(actual.contains(expected));
 }

 @Test
 public void testStringifyGetArrayMethod() throws IllegalAccessException, InvocationTargetException {
 IFXObject obj = createTestObject();
 Method getField1ArrayMethod = IFXObject.class.getMethod("getField1Array");
 Object field1ArrayValue = getField1ArrayMethod.invoke(obj);
 int arrayLength = Array.getLength(field1ArrayValue);
 String expected = "field1 = [\n";
 String actual = obj.stringify(obj, 0);
 for (int i = 0; i < arrayLength; ++i) {
 IFXObject arrayElement = (IFXObject)Array.get(field1ArrayValue, i);
 expected += "\n " + "[".concat(Integer.toString(i)).concat("]:");
 actual += System.lineSeparator() + obj.tab(1).concat("[").concat(Integer.toString(i)).concat("]:");
 expected += testStringify(arrayElement);
 }
 expected += "\n]";
 assertTrue(actual.contains(expected));
 }

 @Test
 public void testStringifyGetIBaseTypeMethod() throws IllegalAccessException, InvocationTargetException {
 IFXObject obj = createTestObject();
 Method getField1IBaseTypeMethod = IFXObject.class.getMethod("getField1IBaseType");
 IBaseType field1IBaseTypeValue = (IBaseType)getField1IBaseTypeMethod.invoke(obj);
 String expected = "field1 = IFXString[String=\"test\"]";
 String actual = obj.stringify(obj, 0);
 assertTrue(actual.contains(expected));
 }

 @Test
 public void testStringifyOtherObject() throws IllegalAccessException, InvocationTargetException {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = new IFXObject() {};
 String expected = "IFXObject = {\n field1 = IFXObject = {\n field1 = NULL\n field2 = NULL\n}\n field2 = NULL\n}";
 String actual = obj1.stringify(obj2, 0);
 assertTrue(actual.contains(expected));
 }

 @Test
 public void testDoCheckEqualsSameObjects() {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = obj1;
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsDifferentObjects() {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = new IFXObject() {};
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsSameClasses() {
 IFXObject obj1 = new IFXObject() {};
 IFXObject obj2 = new IFXObject() {};
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsDifferentClasses() {
 IFXObject obj1 = new IFXObject() {};
 Object obj2 = new Object() {};
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsDifferentMethods() {
 IFXObject obj1 = new IFXObject() {
 @Override
 public String toString() {
 return "IFXObject[field1=1, field2=\"test\"]";
 }
 };
 IFXObject obj2 = new IFXObject() {
 @Override
 public String toString() {
 return "IFXObject[field1=2, field2=\"test2\"]";
 }
 };
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsNullObj1() {
 IFXObject obj1 = null;
 IFXObject obj2 = new IFXObject() {};
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsNullObj2() {
 IFXObject obj1 = new IFXObject() {};
 IFXObject obj2 = null;
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test(expected = NullPointerException.class)
 public void testDoCheckEqualsNullFields() {
 IFXObject obj1 = new IFXObject() {
 @Override
 public String toString() {
 return "IFXObject[field1=NULL, field2=\"test\"]";
 }
 };
 IFXObject obj2 = new IFXObject() {
 @Override
 public String toString() {
 return "IFXObject[field1=NULL, field2=NULL]";
 }
 };
 obj1.setField2(null);
 obj2.setField1(null);
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsDifferentFieldValues() {
 IFXObject obj1 = new IFXObject() {
 @Override
 public String toString() {
 return "IFXObject[field1=1, field2=\"test1\"]";
 }
 };
 IFXObject obj2 = new IFXObject() {
 @Override
 public String toString() {
 return "IFXObject[field1=2, field2=\"test2\"]";
 }
 };
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsSameFieldValues() {
 IFXObject obj1 = new IFXObject() {
 @Override
 public String toString() {
 return "IFXObject[field1=1, field2=\"test\"]";
 }
 };
 IFXObject obj2 = new IFXObject() {
 @Override
 public String toString() {
 return "IFXObject[field1=1, field2=\"test\"]";
 }
 };
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 }

 private String testStringify(IFXObject obj) {
 return obj.toString();
 }
}