package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import static org.junit.Assert.*;

public class IFXObjectTest {

 IFXObject obj1 = new IFXObject(); // empty object
 IFXObject obj2 = new IFXObject(); // empty object
 IFXObject obj3 = new IFXObject(); // object with some fields
 IFXObject obj4 = new IFXObject(); // object with some fields

 @Test
 public void testEquals() {
 // test equals with null
 assertFalse(obj1.equals(null));
 // test equals with different classes
 assertFalse(obj1.equals(new Object()));
 // test equals with same instance
 assertTrue(obj1.equals(obj1));
 // test equals with same object but different references
 IFXObject obj5 = obj1;
 assertTrue(obj1.equals(obj5));
 // test equals with different objects of the same class
 assertFalse(obj1.equals(obj2));
 // test equals with different objects of the same class but with the same fields
 IFXObject obj6 = new IFXObject();
 obj6.setField1(obj1.getField1());
 obj6.setField2(obj1.getField2());
 // test equals with different objects of the same class but with different fields
 IFXObject obj7 = new IFXObject();
 obj7.setField1(obj1.getField1());
 obj7.setField2(new Object());
 assertFalse(obj1.equals(obj7));
 // test equals with different objects of different classes
 IFXObject obj8 = new IFXObject();
 obj8.setField1(new Object());
 assertFalse(obj1.equals(obj8));
 // test equals with reflections
 try {
 Object field1 = obj3.getClass().getField("field1");
 Object field2 = obj3.getClass().getField("field2");
 IFXObject obj9 = new IFXObject();
 Field[] fields = obj3.getClass().getFields();
 for (Field f : fields) {
 if (f.getName().equals("field1")) {
 f.set(obj9, field1.get(obj3));
 }
 else if (f.getName().equals("field2")) {
 f.set(obj9, field2.get(obj3));
 }
 }
 assertEquals(obj3, obj9);
 }
 catch (NoSuchFieldException | IllegalAccessException e) {
 e.printStackTrace();
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testToString() {
 // test toString with empty object
 assertEquals("org.sourceforge.ifx.basetypes.IFXObject = {}", obj1.toString());
 // test toString with object with some fields
 IFXObject obj10 = new IFXObject();
 obj10.setField1("field1");
 obj10.setField2(new Object[] { new Object(), new Object() });
 assertEquals("org.sourceforge.ifx.basetypes.IFXObject = {" +
 "field1 = field1,\n" +
 "[] = [NULL,\n" +
 "NULL]\n" +
 "}", obj10.toString());
 }

 @Test(expected = Exception.class)
 public void testToStringException() {
 // test toString with exception
 new IFXObject().toString();
 }

 @Test
 public void testStringify() {
 // test stringify with empty object
 assertEquals("org.sourceforge.ifx.basetypes.IFXObject = {}", obj1.stringify(obj1, 0));
 // test stringify with object with some fields
 IFXObject obj11 = new IFXObject();
 obj11.setField1("field1");
 obj11.setField2(new Object[] { new Object(), new Object() });
 assertEquals(obj11.toString(), obj11.stringify(obj11, 0));
 }

 @Test(expected = Exception.class)
 public void testStringifyException() {
 // test stringify with exception
 new IFXObject().stringify(new Object(), 0);
 }

 @Test
 public void testDoCheckEquals() {
 // test doCheckEquals with null
 assertFalse(obj1.doCheckEquals(null, obj1));
 // test doCheckEquals with different classes
 assertFalse(obj1.doCheckEquals(new Object(), obj1));
 // test doCheckEquals with same instance
 assertTrue(obj1.doCheckEquals(obj1, obj1));
 // test doCheckEquals with same object but different references
 IFXObject obj12 = obj1;
 assertTrue(obj1.doCheckEquals(obj1, obj12));
 // test doCheckEquals with different objects of the same class and different fields
 IFXObject obj13 = new IFXObject();
 obj13.setField1("field1");
 obj13.setField2("field2");
 assertFalse(obj1.doCheckEquals(obj1, obj13));
 // test doCheckEquals with different objects of the same class but with the same fields
 IFXObject obj14 = new IFXObject();
 obj14.setField1(obj1.getField1());
 obj14.setField2(obj1.getField2());
 assertTrue(obj1.doCheckEquals(obj1, obj14));
 // test doCheckEquals with different objects of the same class but with different fields
 IFXObject obj15 = new IFXObject();
 obj15.setField1(obj1.getField1());
 obj15.setField2(new Object());
 assertFalse(obj1.doCheckEquals(obj1, obj15));
 // test doCheckEquals with different objects of different classes
 IFXObject obj16 = new IFXObject();
 obj16.setField1(new Object());
 assertFalse(obj1.doCheckEquals(obj1, obj16));
 // test doCheckEquals with reflections
 try {
 Object field1 = obj3.getClass().getField("field1");
 Object field2 = obj3.getClass().getField("field2");
 IFXObject obj17 = new IFXObject();
 Field[] fields = obj3.getClass().getFields();
 for (Field f : fields) {
 if (f.getName().equals("field1")) {
 f.set(obj17, field1.get(obj3));
 }
 else if (f.getName().equals("field2")) {
 f.set(obj17, field2.get(obj3));
 }
 }
 assertTrue(obj1.doCheckEquals(obj1, obj17));
 }
 catch (NoSuchFieldException | IllegalAccessException e) {
 e.printStackTrace();
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testTab() {
 // test tab with different levels
 assertEquals("", IFXObject.this.tab(0));
 assertEquals("  ", IFXObject.this.tab(1));
 assertEquals("  ", IFXObject.this.tab(2));
 }
}