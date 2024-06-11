package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import org.junit.Assert;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Array;

public class IFXObjectTest {

 private static final Class<IFXObject> IFXObjectClass = IFXObject.class;

 @Test
 public void testEquals() {
 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = new IFXObject();
 IFXObject obj3 = new IFXObject();

 Assert.assertTrue(obj1.equals(obj1));
 Assert.assertFalse(obj1.equals(obj2));
 Assert.assertFalse(obj1.equals(obj3));

 obj2 = obj1;
 Assert.assertTrue(obj1.equals(obj2));
 Assert.assertTrue(obj2.equals(obj1));

 IFXObject[] objArray = new IFXObject[2];
 objArray[0] = obj1;
 objArray[1] = obj2;

 IFXObject arrayObj = new IFXObject();
 arrayObj.getClass().getDeclaredMethod("setValue", new Class[] { int.class, Object.class }).invoke(arrayObj, new Object[] { 0, obj1 });

 IFXObject objWithArray = new IFXObject();
 setValue(objWithArray, "array", arrayObj);

 Assert.assertFalse(obj1.equals(objWithArray));
 Assert.assertEquals(obj1, obj1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualsNull() {
 IFXObject obj = new IFXObject();
 Assert.assertFalse(obj.equals(null));
 }

 @Test
 public void testToString() {
 IFXObject obj = new IFXObject();
 Assert.assertNotNull(obj.toString());
 }

 @Test
 public void testToStringComplexObject() {
 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = new IFXObject();
 IFXObject obj3 = new IFXObject();
 IFXObject obj4 = new IFXObject();
 IFXObject obj5 = new IFXObject();

 setValue(obj1, "prop1", "value1");
 setValue(obj1, "prop2", obj2);
 setValue(obj2, "prop1", "value2");
 setValue(obj2, "prop2", obj3);
 setValue(obj3, "prop1", "value3");
 setValue(obj3, "prop2", obj4);
 setValue(obj4, "prop1", "value4");
 setValue(obj4, "prop2", obj5);

 String expected = "IFXObject = {\nprop1 = value1\nprop2 = IFXObject = {\nprop1 = value2\nprop2 = IFXObject = {\nprop1 = value3\nprop2 = IFXObject = {\nprop1 = value4\nprop2 = NULL\n}\n}";

 String actual = obj1.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testToStringArray() {
 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = new IFXObject();
 IFXObject[] arrayObj = new IFXObject[2];

 setValue(obj1, "prop1", obj2);
 setValue(obj2, "prop1", new int[0]);
 setValue(obj1, "prop2", arrayObj);
 setValue(arrayObj[0], "prop1", "value1");
 setValue(arrayObj[1], "prop1", "value2");

 String expected = "IFXObject = {\nprop1 = IFXObject = {\nprop1 = NULL\nprop2 = [{\nprop1 = value1\n}, {\nprop1 = value2\n}]\n}";

 String actual = obj1.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testToStringBaseType() {
 IFXObject obj = new IFXObject();
 IBaseType baseType = new BaseTypeInt(1);
 setValue(obj, "prop1", baseType);

 String expected = "IFXObject = {\nprop1 = IBaseType = {value = 1}\n}";

 String actual = obj.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToStringNull() {
 IFXObject obj = new IFXObject();
 obj.getClass().getDeclaredMethod("setValue", new Class[] { String.class, Object.class }).invoke(obj, new Object[] { "prop1", null });
 }

 @Test
 public void testDoCheckEquals() throws Exception {
 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = new IFXObject();
 IFXObject obj3 = new IFXObject();
 IFXObject obj4 = new IFXObject();
 IFXObject obj5 = new IFXObject();

 setValue(obj1, "prop1", "value1");
 setValue(obj1, "prop2", obj2);
 setValue(obj2, "prop1", "value2");
 setValue(obj2, "prop2", obj3);
 setValue(obj3, "prop1", "value3");
 setValue(obj3, "prop2", obj4);
 setValue(obj4, "prop1", "value4");
 setValue(obj4, "prop2", obj5);

 setValue(obj5, "prop1", "value5");

 Assert.assertTrue(obj1.doCheckEquals(obj1, obj1));
 Assert.assertFalse(obj1.doCheckEquals(obj1, obj2));
 Assert.assertFalse(obj1.doCheckEquals(obj1, obj3));
 Assert.assertFalse(obj1.doCheckEquals(obj1, obj4));
 Assert.assertFalse(obj1.doCheckEquals(obj1, obj5));
 Assert.assertFalse(obj1.doCheckEquals(obj2, obj3));
 Assert.assertFalse(obj1.doCheckEquals(obj2, obj4));
 Assert.assertFalse(obj1.doCheckEquals(obj2, obj5));
 Assert.assertFalse(obj1.doCheckEquals(obj3, obj4));
 Assert.assertFalse(obj1.doCheckEquals(obj3, obj5));
 Assert.assertFalse(obj1.doCheckEquals(obj4, obj5));

 setValue(obj2, "prop2", obj1);
 Assert.assertTrue(obj1.doCheckEquals(obj1, obj2));
 Assert.assertTrue(obj2.doCheckEquals(obj1, obj2));

 setValue(obj3, "prop1", "value3_new");
 Assert.assertFalse(obj1.doCheckEquals(obj1, obj3));

 IFXObject arrayObj1 = new IFXObject();
 setValue(arrayObj1, "prop1", new int[0]);
 IFXObject arrayObj2 = new IFXObject();
 setValue(arrayObj2, "prop1", new int[0]);
 setValue(obj1, "prop3", arrayObj1);
 setValue(obj2, "prop3", arrayObj2);

 Object[] objArray1 = Array.newInstance(IFXObjectClass, 2);
 Object[] objArray2 = Array.newInstance(IFXObjectClass, 2);
 setValue(objArray1[0], "prop1", obj1);
 setValue(objArray1[1], "prop1", obj2);
 setValue(objArray2[0], "prop1", obj2);
 setValue(objArray2[1], "prop1", obj1);

 setValue(obj1, "prop3", objArray1);
 setValue(obj2, "prop3", objArray2);

 Assert.assertTrue(obj1.doCheckEquals(obj1, obj2));
 Assert.assertTrue(obj2.doCheckEquals(obj1, obj2));

 Object oneDimArray = new int[1];
 setValue(obj1, "prop3", oneDimArray);
 setValue(obj2, "prop3", oneDimArray);

 Assert.assertTrue(obj1.doCheckEquals(obj1, obj2));
 Assert.assertTrue(obj2.doCheckEquals(obj1, obj2));

 setValue(oneDimArray, 0, 1);
 Assert.assertFalse(obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsNull() {
 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = null;

 Assert.assertFalse(obj1.doCheckEquals(obj1, obj2));
 Assert.assertFalse(obj2.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testDoCheckEqualsDifferentClasses() {
 IFXObject obj1 = new IFXObject();
 String obj2 = new String("value");

 Assert.assertFalse(obj1.doCheckEquals(obj1, obj2));
 Assert.assertFalse(obj2.doCheckEquals(obj1, obj2));
 }

 private void setValue(Object obj, String key, Object value) throws InvocationTargetException, IllegalAccessException {
 Method method = obj.getClass().getDeclaredMethod("setValue", new Class[] { String.class, Object.class });
 method.setAccessible(true);
 method.invoke(obj, new Object[] { key, value });
 }

}