package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

public class IFXObjectTest {

 private IFXObject createTestObject() {
 return new IFXObject();
 }

 @Test
 public void testEquals() {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = createTestObject();
 IFXObject obj3 = createTestObject();

 assertTrue(obj1.equals(obj1));
 assertFalse(obj1.equals(obj2));
 assertFalse(obj1.equals(obj3));

 obj2 = obj1;
 assertTrue(obj1.equals(obj2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualsWithNull() {
 IFXObject obj1 = createTestObject();
 assertFalse(obj1.equals(null));
 }

 @Test
 public void testToString() {
 IFXObject obj = createTestObject();
 String expected = obj.getClass().getName() + " = {\n" +
 "\tgetclass = " + obj.getClass().getName() + "\n" +
 "}";
 assertEquals(expected, obj.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToStringWithNull() {
 IFXObject obj = createTestObject();
 assertNotNull(obj.toString());
 obj = null;
 throw new IllegalArgumentException("Expected null argument to toString");
 }

 @Test
 public void testStringify() throws Exception {
 IFXObject obj = createTestObject();
 String expected = obj.getClass().getName() + " = {\n\tgetclass = " + obj.getClass().getName() + "\n}";
 String actual = IFXObject.stringify(obj, 0);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStringifyWithNull() {
 IFXObject obj = createTestObject();
 IFXObject nullObj = null;
 assertThrows(IllegalArgumentException.class, () -> IFXObject.stringify(nullObj, 0));
 }

 @Test
 public void testDoCheckEquals() throws Exception {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = createTestObject();
 IFXObject obj3 = createTestObject();

 assertTrue(obj1.doCheckEquals(obj1, obj1));
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 assertFalse(obj1.doCheckEquals(obj1, obj3));

 obj2 = obj1;
 assertTrue(obj1.doCheckEquals(obj1, obj2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoCheckEqualsWithNull() {
 IFXObject obj1 = createTestObject();
 IFXObject obj2 = null;
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 assertThrows(IllegalArgumentException.class, () -> obj1.doCheckEquals(obj1, obj2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoCheckEqualsWithDifferentTypes() throws Exception {
 IFXObject obj1 = createTestObject();
 Object obj2 = new Object();
 assertFalse(obj1.doCheckEquals(obj1, obj2));
 assertThrows(IllegalArgumentException.class, () -> obj1.doCheckEquals(obj1, obj2));
 }

 @Test
 public void testStringifyGetArray() throws Exception {
 IFXObject[] array = new IFXObject[3];
 array[0] = createTestObject();
 array[1] = createTestObject();
 array[2] = createTestObject();

 IFXObject obj = array[0];
 String expected = obj.getClass().getName() + " = {\n\tgetclass = " + obj.getClass().getName() + "\n\t[0]:\n\t\t" + obj.getClass().getName() + " = {\n\t\t\tgetclass = " + obj.getClass().getName() + "\n\t\t}";
 String actual = IFXObject.stringify(obj, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testStringifyGetArrayWithNullElement() throws Exception {
 IFXObject[] array = new IFXObject[3];
 array[0] = createTestObject();
 array[1] = null;
 array[2] = createTestObject();

 IFXObject obj = array[0];
 String expected = obj.getClass().getName() + " = {\n\tgetclass = " + obj.getClass().getName() + "\n\t[0]:\n\t\t" + obj.getClass().getName() + " = {\n\t\t\tgetclass = " + obj.getClass().getName() + "\n\t\t}\n\t[1]: NULL\n\t[2]:\n\t\t" + obj.getClass().getName() + " = {\n\t\t\tgetclass = " + obj.getClass().getName() + "\n\t\t}";
 String actual = IFXObject.stringify(obj, 0);
 assertEquals(expected, actual);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testStringifyGetArrayWithInvalidIndex() throws Exception {
 IFXObject[] array = new IFXObject[3];
 array[0] = createTestObject();
 array[1] = createTestObject();
 array[2] = createTestObject();

 IFXObject obj = array;
 String expected = "Exception! ArrayIndexOutOfBoundsException: get length: 3";
 String actual = "";
 try {
 actual = IFXObject.stringify(obj, 3);
 } catch (Exception e) {
 actual = e.toString();
 }
 assertEquals(expected, actual);
 }

 @Test
 public void testStringifyGetObjectWithNullReturn() throws Exception {
 IFXObject obj = createTestObject();
 Method getMethod = obj.getClass().getMethod("getNullMethod");
 Object nullRetVal = getMethod.invoke(obj);
 String expected = obj.getClass().getName() + " = {\n\tgetclass = " + obj.getClass().getName() + "\n\tgetNullMethod = NULL\n}";
 String actual = IFXObject.stringify(obj, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testStringifyGetObjectWithEmptyStringReturn() throws Exception {
 IFXObject obj = createTestObject();
 Method getMethod = obj.getClass().getMethod("getEmptyStringMethod");
 String emptyRetVal = (String)getMethod.invoke(obj);
 String expected = obj.getClass().getName() + " = {\n\tgetclass = " + obj.getClass().getName() + "\n\tgetEmptyStringMethod = ''" + emptyRetVal + "\''\n}";
 String actual = IFXObject.stringify(obj, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testStringifyGetObjectWithNonEmptyStringReturn() throws Exception {
 IFXObject obj = createTestObject();
 Method getMethod = obj.getClass().getMethod("getNonEmptyStringMethod");
 String nonEmptyRetVal = (String)getMethod.invoke(obj);
 String expected = obj.getClass().getName() + " = {\n\tgetclass = " + obj.getClass().getName() + "\n\tgetNonEmptyStringMethod = '" + nonEmptyRetVal + "\'}\n}";
 String actual = IFXObject.stringify(obj, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testStringifyGetObjectWithArrayReturn() throws Exception {
 IFXObject obj = createTestObject();
 Method getMethod = obj.getClass().getMethod("getArrayMethod");
 IFXObject[] arrayRetVal = (IFXObject[])getMethod.invoke(obj);
 String expected = obj.getClass().getName() + " = {\n\tgetclass = " + obj.getClass().getName() + "\n\tgetArrayMethod = [\n\t\t" + arrayRetVal[0].getClass().getName() + " = {\n\t\t\tgetclass = " + arrayRetVal[0].getClass().getName() + "\n\t\t}\n\t\t" + arrayRetVal[1].getClass().getName() + " = {\n\t\t\tgetclass = " + arrayRetVal[1].getClass().getName() + "\n\t\t}\n\t]";
 String actual = IFXObject.stringify(obj, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testStringifyGetObjectWithNullArrayReturn() throws Exception {
 IFXObject obj = createTestObject();
 Method getMethod = obj.getClass().getMethod("getNullArrayMethod");
 IFXObject[] nullArrayRetVal = (IFXObject[])getMethod.invoke(obj);
 String expected = obj.getClass().getName() + " = {\n\tgetclass = " + obj.getClass().getName() + "\n\tgetNullArrayMethod = NULL\n}";
 String actual = IFXObject.stringify(obj, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testStringifyGetObjectWithIBaseTypeReturn() throws Exception {
 IFXObject obj = createTestObject();
 Method getMethod = obj.getClass().getMethod("getIBaseTypeMethod");
 IBaseType iBaseTypeRetVal = (IBaseType)getMethod.invoke(obj);
 String expected = obj.getClass().getName() + " = {\n\tgetclass = " + obj.getClass().getName() + "\n\tgetIBaseTypeMethod = " + iBaseTypeRetVal.getString() + "\n}";
 String actual = IFXObject.stringify(obj, 0);
 assertEquals(expected, actual);
 }

 @Test(expected = NoSuchMethodException.class)
 public void testStringifyGetNonExistentMethod() throws Exception {
 IFXObject obj = createTestObject();
 String expected = "Exception! NoSuchMethodException: getNonExistentMethod";
 String actual = "";
 try {
 actual = IFXObject.stringify(obj, 0);
 Method getMethod = obj.getClass().getMethod("getNonExistentMethod");
 } catch (Exception e) {
 actual = e.toString();
 }
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalAccessException.class)
 public void testStringifyMethodWithPrivateAccess() throws Exception {
 IFXObject obj = createTestObject();
 String expected = "Exception! IllegalAccessException: class org.sourceforge.ifx.basetypes.IFXObject in module ifx-basetypes cannot be accessed: module does not 'opens org.sourceforge.ifx.basetypes' to module";
 String actual = "";
 try {
 actual = IFXObject.stringify(obj, 0);
 Method privateMethod = obj.getClass().getDeclaredMethod("privateMethod");
 privateMethod.setAccessible(true);
 } catch (Exception e) {
 actual = e.toString();
 }
 assertEquals(expected, actual);
 }

 @Test(expected = InvocationTargetException.class)
 public void testStringifyMethodWithRuntimeException() throws Exception {
 IFXObject obj = createTestObject();
 String expected = "Exception! RuntimeException: java.lang.RuntimeException: Runtime exception";
 String actual = "";
 try {
 actual = IFXObject.stringify(obj, 0);
 Method runtimeExceptionMethod = obj.getClass().getMethod("runtimeExceptionMethod");
 runtimeExceptionMethod.setAccessible(true);
 runtimeExceptionMethod.invoke(obj);
 } catch (Exception e) {
 actual = e.toString();
 }
 assertEquals(expected, actual);
 }

 @Test(expected = Error.class)
 public void testStringifyMethodWithError() throws Exception {
 IFXObject obj = createTestObject();
 String expected = "Exception! Error: java.lang.Error: Error occurred";
 String actual = "";
 try {
 actual = IFXObject.stringify(obj, 0);
 Method errorMethod = obj.getClass().getMethod("errorMethod");
 errorMethod.setAccessible(true);
 errorMethod.invoke(obj);
 } catch (Exception e) {
 actual = e.toString();
 }
 assertEquals(expected, actual);
 }

}