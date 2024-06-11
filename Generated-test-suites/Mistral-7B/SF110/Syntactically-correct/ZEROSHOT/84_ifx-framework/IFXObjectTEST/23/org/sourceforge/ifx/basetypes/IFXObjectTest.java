package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import org.junit.Assert;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Array;

public class IFXObjectTest {

 private static final String TABSTOP = "  ";

 @Test
 public void testEquals() {
 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = new IFXObject();
 IFXObject obj3 = new IFXObject();

 Assert.assertTrue(obj1.equals(obj1));
 Assert.assertFalse(obj1.equals(obj2));

 obj1.setField("test", "testValue");
 obj2.setField("test", "testValue");
 Assert.assertTrue(obj1.equals(obj2));

 obj3.setField("test", "differentValue");
 Assert.assertFalse(obj1.equals(obj3));

 Assert.assertFalse(obj1.equals(null));
 }

 @Test
 public void testToString() {
 IFXObject obj = new IFXObject();
 obj.setField("test", "testValue");

 String expected = "IFXObject = {\n test = testValue\n}";
 String actual = obj.toString();

 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToStringWithNullObject() {
 IFXObject obj = null;
 obj.toString();
 }

 @Test
 public void testStringify() throws IllegalAccessException, InvocationTargetException {
 IFXObject obj = new IFXObject();
 obj.setField("test", new IFXObject());

 Method stringifyMethod = IFXObject.class.getDeclaredMethod("stringify", IFXObject.class, int.class);
 stringifyMethod.setAccessible(true);

 String expected = "IFXObject = {\n test = IFXObject = {\n }\n}";
 String actual = (String) stringifyMethod.invoke(obj, obj, 0);

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testDoCheckEquals() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
 IFXObject obj1 = new IFXObject();
 IFXObject obj2 = new IFXObject();
 IFXObject obj3 = new IFXObject();
 IFXObject obj4 = new IFXObject();

 Method getTestMethod = IFXObject.class.getDeclaredMethod("getTest");
 getTestMethod.setAccessible(true);

 obj1.setField("test", "testValue1");
 obj2.setField("test", "testValue2");
 obj3.setField("test", "testValue1");
 obj4.setField("test", "testValue1");

 Boolean result = IFXObject.class.getDeclaredMethod("doCheckEquals", IFXObject.class, IFXObject.class).invoke(obj1, obj2);
 Assert.assertFalse(result);

 result = IFXObject.class.getDeclaredMethod("doCheckEquals", IFXObject.class, IFXObject.class).invoke(obj1, obj3);
 Assert.assertTrue(result);

 result = IFXObject.class.getDeclaredMethod("doCheckEquals", IFXObject.class, IFXObject.class).invoke(obj1, obj4);
 Assert.assertTrue(result);
 }

 private void setField(IFXObject obj, String fieldName, Object value) throws IllegalAccessException {
 Field field = IFXObject.class.getDeclaredField(fieldName);
 field.setAccessible(true);
 field.set(obj, value);
 }

}