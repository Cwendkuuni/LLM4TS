package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import org.junit.Assert;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.Class;
import java.lang.ArrayStoreException;

public class IFXObjectTest {

 private static final String TABSTOP = "  ";

 @Test
 public void testEquals() throws Exception {
 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = new IFXObject();
 IFXObject obj3 = new IFXObject();

 Assert.assertTrue(obj1.equals(obj1));
 Assert.assertFalse(obj1.equals(obj2));
 Assert.assertEquals(obj1.hashCode(), obj1.hashCode()); // hashCode should be consistent with equals

 obj1.setField("testValue1");
 obj2.setField("testValue2");
 obj3.setField("testValue1");

 Assert.assertFalse(obj1.equals(obj2));
 Assert.assertTrue(obj1.equals(obj3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualsNullObject() {
 IFXObject obj = new IFXObject();
 Assert.assertFalse(obj.equals(null));
 }

 @Test
 public void testToString() throws Exception {
 IFXObject obj = new IFXObject();
 obj.setField("testValue");

 String expected = obj.getClass().getName() + " = {\n" +
                TABSTOP + "testField = testValue\n" +
                "}";

 Assert.assertEquals(expected, obj.toString());
 }

 @Test
 public void testToStringNestedArray() throws Exception {
 IFXObject obj1 = new IFXObject();
 obj1.setField("testValue1");

 IFXObject obj2 = new IFXObject();
 obj2.setField("testValue2");
 Object[] arr = new Object[] { obj1, obj2 };

 IFXObject obj = new IFXObject();
 obj.setField(arr);

 String expected = obj.getClass().getName() + " = {\n" +
                TABSTOP + "testField = [\n" +
                TABSTOP + "[" + TABSTOP + "] = " + obj1.getClass().getName() + " = {\n" +
                TABSTOP + TABSTOP + "testField = testValue1\n" +
                TABSTOP + "}\n" +
                TABSTOP + "[" + TABSTOP + "] = " + obj2.getClass().getName() + " = {\n" +
                TABSTOP + TABSTOP + "testField = testValue2\n" +
                TABSTOP + "}\n" +
                TABSTOP + "]\n" +
                "}";

 Assert.assertEquals(expected, obj.toString());
 }

 @Test
 public void testToStringNestedObject() throws Exception {
 IFXObject obj1 = new IFXObject();
 obj1.setField("testValue1");

 IFXObject obj2 = new IFXObject();
 obj2.setField("testValue2");
 IFXObject obj3 = new IFXObject();
 obj3.setField("testValue3");

 IFXObject obj = new IFXObject();
 obj.setField(obj1);
 obj.setField("secondField", obj2);
 obj.setField("thirdField", obj3);

 String expected = obj.getClass().getName() + " = {\n" +
                TABSTOP + "testField = " + obj1.getClass().getName() + " = {\n" +
                TABSTOP + TABSTOP + "testField = testValue1\n" +
                TABSTOP + "}\n" +
                TABSTOP + "secondField = " + obj2.getClass().getName() + " = {\n" +
                TABSTOP + TABSTOP + "testField = testValue2\n" +
                TABSTOP + "}\n" +
                TABSTOP + "thirdField = " + obj3.getClass().getName() + " = {\n" +
                TABSTOP + TABSTOP + "testField = testValue3\n" +
                TABSTOP + "}\n" +
                "}";

 Assert.assertEquals(expected, obj.toString());
 }

 @Test
 public void testEqualsRecursive() throws Exception {
 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = new IFXObject();
 Object[] arr1 = new Object[] { obj1 };
 Object[] arr2 = new Object[] { obj1 };
 IFXObject obj3 = new IFXObject();
 IFXObject obj4 = new IFXObject();
 Object[] arr3 = new Object[] { obj2, obj3, obj4 };
 Object[] arr4 = new Object[] { obj2, obj3, obj4 };

 obj1.setField("testValue1");
 obj2.setField("testValue2");
 obj3.setField("testValue3");
 obj4.setField("testValue4");

 Assert.assertTrue(obj1.equals(obj1));
 Assert.assertFalse(obj1.equals(obj2));

 obj1.setField(arr1);
 obj2.setField(arr2);
 Assert.assertTrue(obj1.equals(obj2));

 obj1.setField("secondField", obj3);
 obj2.setField("secondField", obj3);
 obj3.setField("thirdField", obj4);
 Assert.assertTrue(obj1.equals(obj2));

 obj3.setField("thirdField", new IFXObject());
 Assert.assertFalse(obj1.equals(obj2));

 obj1.setField(arr1);
 obj2.setField(arr4);
 Assert.assertFalse(obj1.equals(obj2));
 }

 @Test(expected = ArrayStoreException.class)
 public void testToStringArrayInvalidType() throws Exception {
 IFXObject obj = new IFXObject();
 obj.setField("testValue");

 String[] arr = new String[] { "testString" };
 Object[] invalidArr = new Object[] { obj, arr };

 IFXObject objWithInvalidArray = new IFXObject();
 objWithInvalidArray.setField(invalidArr);

 String expected = objWithInvalidArray.getClass().getName() + " = {\n" +
                 TABSTOP + "testField = [\n" +
                 TABSTOP + "[" + TABSTOP + "] = " + obj.getClass().getName() + " = {\n" +
                 TABSTOP + TABSTOP + "testField = testValue\n" +
                 TABSTOP + "}\n" +
                 TABSTOP + "[" + TABSTOP + "] = [Ljava.lang.String;@1b2d52d\n" +
                 "]";

 Assert.assertEquals(expected, objWithInvalidArray.toString());
 }

 @Test
 public void testToStringNull() throws Exception {
 IFXObject obj = new IFXObject();
 obj.setField(null);

 String expected = obj.getClass().getName() + " = {\n" +
                 TABSTOP + "testField = NULL\n" +
                 "}";

 Assert.assertEquals(expected, obj.toString());
 }

 @Test
 public void testToStringEmptyArray() throws Exception {
 IFXObject obj = new IFXObject();
 obj.setField(new Object[0]);

 String expected = obj.getClass().getName() + " = {\n" +
                 TABSTOP + "testField = []\n" +
                 "}";

 Assert.assertEquals(expected, obj.toString());
 }

 @Test
 public void testToStringEmptyObject() throws Exception {
 IFXObject obj = new IFXObject();

 String expected = obj.getClass().getName() + " = {\n" +
                 "}";

 Assert.assertEquals(expected, obj.toString());
 }

 private void setField(IFXObject obj, String fieldName, Object value) throws IllegalAccessException, InvocationTargetException {
 Field field = obj.getClass().getDeclaredField(fieldName);
 field.setAccessible(true);
 field.set(obj, value);
 }
}