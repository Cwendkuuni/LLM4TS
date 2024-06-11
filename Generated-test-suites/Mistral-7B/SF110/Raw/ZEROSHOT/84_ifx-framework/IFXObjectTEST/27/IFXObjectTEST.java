package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

public class IFXObjectTest {

 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = new IFXObject();
 IFXObject obj3 = new IFXObject();
 IFXObject obj4 = new IFXObject();
 IBaseType baseType1 = new BaseType();
 IBaseType baseType2 = new BaseType();

 @Test
 public void testEquals() {
 // Test equals with null object
 assertFalse(obj1.equals(null));

 // Test equals with same object
 assertTrue(obj1.equals(obj1));

 // Test equals with different classes
 assertFalse(obj1.equals(new Object()));

 // Test equals with different objects of the same class
 assertFalse(obj1.equals(obj2));

 // Test equals with same objects of the same class
 obj1.setField1("value1");
 obj2.setField1("value1");
 assertTrue(obj1.equals(obj2));

 // Test equals with same objects of different classes but with same values
 obj3.setField1(baseType1);
 obj4.setField1(baseType2);
 baseType1.setValue("value1");
 baseType2.setValue("value1");
 assertTrue(obj3.equals(obj4));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualsWithIllegalArgument() {
 // Test equals with incorrect argument type
 obj1.equals("not an object");
 }

 @Test
 public void testToString() {
 // Test toString with empty object
 assertEquals("org.sourceforge.ifx.basetypes.IFXObject = {}", obj1.toString());

 // Test toString with field values
 obj1.setField1("field1 value");
 obj1.setField2(baseType1);
 baseType1.setValue("baseType1 value");
 assertEquals("org.sourceforge.ifx.basetypes.IFXObject = {\nfield1 value = value1\nbaseType1 value = value1\n}", obj1.toString());

 // Test toString with nested objects
 IFXObject nestedObj = new IFXObject();
 nestedObj.setField1(baseType1);
 baseType1.setValue("baseType1 value");
 obj1.setField3(nestedObj);
 assertEquals("org.sourceforge.ifx.basetypes.IFXObject = {\nfield1 value = value1\nbaseType1 value = value1\nfield3 = {\norg.sourceforge.ifx.basetypes.IFXObject = {\nfield1 value = value1\n}\n}", obj1.toString());
 }

 @Test
 public void testStringify() throws Exception {
 // Test stringify with empty object
 assertEquals("org.sourceforge.ifx.basetypes.IFXObject = {}", IFXObject.stringify(obj1, 0));

 // Test stringify with field values
 obj1.setField1("field1 value");
 obj1.setField2(baseType1);
 baseType1.setValue("baseType1 value");
 assertEquals("org.sourceforge.ifx.basetypes.IFXObject = {\nfield1 value = value1\nbaseType1 value = value1\n}", IFXObject.stringify(obj1, 0));

 // Test stringify with nested objects
 IFXObject nestedObj = new IFXObject();
 nestedObj.setField1(baseType1);
 baseType1.setValue("baseType1 value");
 obj1.setField3(nestedObj);
 assertEquals("org.sourceforge.ifx.basetypes.IFXObject = {\nfield1 value = value1\nbaseType1 value = value1\nfield3 = {\norg.sourceforge.ifx.basetypes.IFXObject = {\nfield1 value = value1\n}\n}", IFXObject.stringify(obj1, 0));
 }

 @Test
 public void testDoCheckEquals() throws Exception {
 // Test doCheckEquals with null objects
 assertFalse(IFXObject.doCheckEquals(null, null));

 // Test doCheckEquals with same objects
 assertTrue(IFXObject.doCheckEquals(obj1, obj1));

 // Test doCheckEquals with different classes
 assertFalse(IFXObject.doCheckEquals(obj1, new Object()));

 // Test doCheckEquals with different objects of the same class
 IFXObject obj5 = new IFXObject();
 obj5.setField1("value1");
 assertFalse(IFXObject.doCheckEquals(obj1, obj5));

 // Test doCheckEquals with same objects of the same class
 obj1.setField1("value1");
 obj5.setField1("value1");
 assertTrue(IFXObject.doCheckEquals(obj1, obj5));

 // Test doCheckEquals with same objects of different classes but with same values
 IFXObject obj6 = new IFXObject();
 obj6.setField1(baseType1);
 IFXObject obj7 = new IFXObject();
 obj7.setField1(baseType2);
 baseType1.setValue("value1");
 baseType2.setValue("value1");
 assertTrue(IFXObject.doCheckEquals(obj6, obj7));
 }

 @Test
 public void testTab() {
 // Test tab with different levels
 assertEquals("", IFXObject.tab(0));
 assertEquals("  ", IFXObject.tab(1));
 assertEquals("  ", IFXObject.tab(2));
 }
}