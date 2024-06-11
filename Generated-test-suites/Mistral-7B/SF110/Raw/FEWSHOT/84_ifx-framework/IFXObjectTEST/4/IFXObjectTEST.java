package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.*;

import org.junit.Test;

public class IFXObjectTest {

 IFXObject createTestObject() {
 return new IFXObject() {
 // Add any necessary fields or methods here to create an instance of IFXObject for testing
 };
 }

 @Test
 public void testEquals() {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = createTestObject();
 assertTrue(obj1.equals(obj1));
 assertTrue(obj2.equals(obj2));
 assertFalse(obj1.equals(obj2));
 }

 @Test
 public void testToString() {
 IFXObject obj = createTestObject();
 String expected = "IFXObject = {" +
 "class java.lang.String = NULL" +
 "class java.lang.Integer = NULL" +
 "class org.sourceforge.ifx.basetypes.IFXObject = {" +
 "class java.lang.String = NULL" +
 "class java.lang.Integer = NULL" +
 "}";
 String actual = obj.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testStringify_nullObject() {
 IFXObject obj = null;
 String expected = "IFXObject = {null}";
 String actual = obj.stringify(obj, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testStringify_simpleObject() {
 IFXObject obj = new IFXObject() {
 String strField = "test string";
 };
 String expected = "IFXObject = {" +
 "class java.lang.String = test string" +
 "}";
 String actual = obj.stringify(obj, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testStringify_arrayObject() {
 IFXObject obj = new IFXObject() {
 String[] strArrayField = {"test string 1", "test string 2"};
 };
 String expected = "IFXObject = {" +
 "class java.lang.String = [test string 1,test string 2]" +
 "}";
 String actual = obj.stringify(obj, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testStringify_nestedObject() {
 IFXObject outerObj = new IFXObject() {
 IFXObject innerObj = new IFXObject() {
 String strField = "test string";
 };
 };
 String expected = "IFXObject = {" +
 "class org.sourceforge.ifx.basetypes.IFXObject = {" +
 "class java.lang.String = test string" +
 "}" +
 "}";
 String actual = outerObj.stringify(outerObj, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testDoCheckEquals_nullObjects() {
 IFXObject obj1 = null;
 IFXObject obj2 = null;
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEquals_sameObjects() {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = obj1;
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEquals_differentClasses() {
 IFXObject obj1 = createTestObject();
 Object obj2 = new Object();
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEquals_differentFields() {
 IFXObject obj1 = new IFXObject() {
 String strField1 = "test string 1";
 };
 IFXObject obj2 = new IFXObject() {
 String strField2 = "test string 2";
 };
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEquals_sameFields_differentValues() {
 IFXObject obj1 = new IFXObject() {
 String strField = "test string 1";
 };
 IFXObject obj2 = new IFXObject() {
 String strField = "test string 2";
 };
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEquals_sameFields_sameValues() {
 IFXObject obj1 = new IFXObject() {
 String strField = "test string";
 };
 IFXObject obj2 = new IFXObject() {
 String strField = "test string";
 };
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEquals_arrayFields_sameValues() {
 IFXObject obj1 = new IFXObject() {
 String[] strArrayField = {"test string 1", "test string 2"};
 };
 IFXObject obj2 = new IFXObject() {
 String[] strArrayField = {"test string 1", "test string 2"};
 };
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEquals_arrayFields_differentValues() {
 IFXObject obj1 = new IFXObject() {
 String[] strArrayField = {"test string 1", "test string 2"};
 };
 IFXObject obj2 = new IFXObject() {
 String[] strArrayField = {"test string 2", "test string 1"};
 };
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEquals_nestedObjects_sameValues() {
 IFXObject outerObj1 = new IFXObject() {
 IFXObject innerObj1 = new IFXObject() {
 String strField = "test string";
 };
 };
 IFXObject outerObj2 = new IFXObject() {
 IFXObject innerObj2 = new IFXObject() {
 String strField = "test string";
 };
 };
 assertTrue(outerObj1.doCheckEquals(outerObj1, outerObj2));
 }

 @Test
 public void testDoCheckEquals_nestedObjects_differentValues() {
 IFXObject outerObj1 = new IFXObject() {
 IFXObject innerObj1 = new IFXObject() {
 String strField = "test string 1";
 };
 };
 IFXObject outerObj2 = new IFXObject() {
 IFXObject innerObj2 = new IFXObject() {
 String strField = "test string 2";
 };
 };
 assertFalse(outerObj1.doCheckEquals(outerObj1, outerObj2));
 }

}