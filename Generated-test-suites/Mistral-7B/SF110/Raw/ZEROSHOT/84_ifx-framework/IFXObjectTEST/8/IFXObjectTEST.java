package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import static org.junit.Assert.*;

public class IFXObjectTest {

 IFXObject testObj1 = new IFXObject();
 IFXObject testObj2 = new IFXObject();
 IFXObject testObj3 = new IFXObject();
 IFXObject testObj4 = new IFXObject();
 IBaseType testBaseType1 = new BaseTypeInt(1);
 IBaseType testBaseType2 = new BaseTypeInt(2);
 IBaseType testBaseType3 = new BaseTypeDouble(3.5);
 IBaseType testBaseType4 = new BaseTypeDouble(3.5);

 @Test
 public void testEquals() {
 // Testing null objects
 assertEquals(testObj1, testObj1);
 assertEquals(testObj1, testObj1);
 assertNotEquals(testObj1, null);
 assertNotEquals(null, testObj1);

 // Testing same object
 assertEquals(testObj1, testObj1);

 // Testing objects with the same class and same values
 testObj2.setField("testValue");
 testObj3.setField("testValue");
 assertEquals(testObj2, testObj3);

 // Testing objects with the same class and different values
 testObj2.setField("testValue1");
 assertNotEquals(testObj2, testObj3);

 // Testing objects with different classes
 testObj4 = new OtherClass();
 assertNotEquals(testObj1, testObj4);

 // Testing toString()
 assertEquals("IFXObject = {field=testValue,}", testObj2.toString());
 }

 @Test
 public void testToString() {
 IFXObject testObj5 = new IFXObject() {{
 setField("testValue");
 setNestedObject(new IFXObject() {{
 setField("testNestedValue");
 }});
 }};
 String expectedToString = "IFXObject = {field=testValue,\n nestedObject = {field=testNestedValue,\n}}";
 assertEquals(expectedToString, testObj5.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToStringException() {
 IFXObject testObj6 = new IFXObject();
 testObj6.getClass(); // Invoking getClass() method to trigger exception
 }

 @Test
 public void testDoCheckEquals() {
 IFXObject obj1 = new IFXObject() {{
 setField("testValue");
 setNestedObject(new IFXObject() {{
 setField("testNestedValue");
 }});
 }};
 IFXObject obj2 = new IFXObject() {{
 setField("testValue");
 setNestedObject(new IFXObject() {{
 setField("testNestedValue");
 }});
 }};
 IFXObject obj3 = new IFXObject() {{
 setField("testValue");
 }};
 IFXObject obj4 = new IFXObject() {{
 setField("testValue2");
 }};
 IFXObject obj5 = new OtherClass();

 // Testing equals method internally (doCheckEquals)
 assertTrue(obj1.doCheckEquals(obj1, obj1));
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 assertFalse(obj1.doCheckEquals(obj1, obj3));
 assertFalse(obj1.doCheckEquals(obj1, obj4));
 assertFalse(obj1.doCheckEquals(obj1, obj5));
 }

 @Test
 public void testGetString() {
 // Testing base types
 assertEquals("1", testBaseType1.getString());
 assertEquals("3.5", testBaseType3.getString());

 // Testing IFXObject with base types
 IFXObject testObjBaseType1 = new IFXObject() {{
 setField(testBaseType1);
 }};
 assertEquals("IFXObject = {field=1}", testObjBaseType1.toString());

 // Testing IFXObject with nested IFXObjects
 IFXObject testObjNested1 = new IFXObject() {{
 setField("testValue");
 setNestedObject(testObjBaseType1);
 }};
 IFXObject testObjNested2 = new IFXObject() {{
 setField("testValue2");
 setNestedObject(new IFXObject() {{
 setField("testValue3");
 }});
 }};
 IFXObject testObjNested3 = new IFXObject() {{
 setField("testValue");
 setNestedObject(testObjNested2);
 }};
 String expectedToStringNested = "IFXObject = {field=testValue,\n nestedObject = {field=testValue2,\n nestedObject = {field=testValue3,\n}}";
 assertEquals(expectedToStringNested, testObjNested3.toString());
 }
}