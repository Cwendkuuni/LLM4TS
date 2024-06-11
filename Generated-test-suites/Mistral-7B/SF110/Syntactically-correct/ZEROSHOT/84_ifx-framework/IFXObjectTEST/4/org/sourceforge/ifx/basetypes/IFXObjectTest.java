package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

public class IFXObjectTest {

 IFXObject obj1, obj2, obj3;

 @Before
 public void setUp() {
 obj1 = new IFXObject();
 obj2 = new IFXObject();
 obj3 = new IFXObject();
 }

 @Test
 public void testEquals() {
 // test null case
 assertFalse(obj1.equals(null));
 // test self-comparison
 assertTrue(obj1.equals(obj1));
 // test different classes
 assertFalse(obj1.equals(new Object()));
 // test same class but different instances
 assertFalse(obj1.equals(obj2));
 // test equals method in subclass
 IFXObject subObj1 = new MySubclass();
 IFXObject subObj2 = new MySubclass();
 assertTrue(subObj1.equals(subObj2));
 // test equals method with same objects
 obj1.setField("test");
 obj2.setField("test");
 assertTrue(obj1.equals(obj2));
 // test equals method with different objects
 obj1.setField("test1");
 obj2.setField("test2");
 assertFalse(obj1.equals(obj2));
 }

 @Test
 public void testToString() {
 // test empty class
 assertEquals("IFXObject = {}", obj1.toString());
 // test class with fields
 obj1.setField("test");
 obj2.setField("test2");
 obj3.setField("test3");
 IFXArray intArray = new IFXArray(new int[]{1, 2, 3});
 IFXArray intArray2 = new IFXArray(new int[]{1, 2, 3});
 obj1.setField("intArray", intArray);
 obj2.setField("intArray2", intArray2);
 String expected = "IFXObject = {" +
 "intArray = [[" +
 "0:1," +
 "1:2," +
 "2:3" +
 "],\n" +
 "field = test" +
 "}";
 assertEquals(expected, obj1.toString());
 }

 @Test
 public void testDoCheckEquals() {
 // test null case
 assertFalse(IFXObject.doCheckEquals(null, obj1));
 // test self-comparison
 assertTrue(IFXObject.doCheckEquals(obj1, obj1));
 // test different classes
 assertFalse(IFXObject.doCheckEquals(obj1, new Object()));
 // test equals method in subclass
 MySubclass subObj1 = new MySubclass();
 MySubclass subObj2 = new MySubclass();
 assertTrue(IFXObject.doCheckEquals(subObj1, subObj2));
 // test equals method with same objects
 obj1.setField("test");
 obj2.setField("test");
 assertTrue(IFXObject.doCheckEquals(obj1, obj2));
 // test equals method with different objects
 obj1.setField("test1");
 obj2.setField("test2");
 assertFalse(IFXObject.doCheckEquals(obj1, obj2));
 // test equals method with arrays
 IFXArray intArray = new IFXArray(new int[]{1, 2, 3});
 IFXArray intArray2 = new IFXArray(new int[]{1, 2, 3});
 IFXArray intArray3 = new IFXArray(new int[]{1, 2, 4});
 obj1.setField("intArray", intArray);
 obj2.setField("intArray2", intArray2);
 obj3.setField("intArray3", intArray3);
 assertTrue(IFXObject.doCheckEquals(obj1, obj2));
 assertFalse(IFXObject.doCheckEquals(obj1, obj3));
 }

 @Test
 public void testGetString() {
 // test empty class
 assertEquals("", ((IBaseType)obj1).getString());
 // test class with fields
 obj1.setField("test");
 obj2.setField("test2");
 obj3.setField("test3");
 IFXArray intArray = new IFXArray(new int[]{1, 2, 3});
 IFXArray intArray2 = new IFXArray(new int[]{1, 2, 3});
 obj1.setField("intArray", intArray);
 obj2.setField("intArray2", intArray2);
 String expected = "test" +
 "\nintArray = [\n" +
 "0:1,\n" +
 "1:2,\n" +
 "2:3\n" +
 "]";
 assertEquals(expected, ((IBaseType)obj1).getString());
 }

 @Test
 public void testStringify() throws Exception {
 // test empty class
 String expected = "IFXObject = {}\n";
 assertEquals(expected, IFXObject.stringify(obj1, 0));
 // test class with fields
 obj1.setField("test");
 obj2.setField("test2");
 obj3.setField("test3");
 IFXArray intArray = new IFXArray(new int[]{1, 2, 3});
 IFXArray intArray2 = new IFXArray(new int[]{1, 2, 3});
 obj1.setField("intArray", intArray);
 obj2.setField("intArray2", intArray2);
 IFXObject subObj1 = new MySubclass();
 IFXObject subObj2 = new MySubclass();
 subObj1.setField("subField", "testSub");
 subObj2.setField("subField", "testSub2");
 obj1.setField("subObj", subObj1);
 obj2.setField("subObj2", subObj2);
 String expectedTest = "IFXObject = {\n" +
 "field = test,\n" +
 "intArray = [\n" +
 "0:1,\n" +
 "1:2,\n" +
 "2:3\n" +
 "],\n" +
 "subObj = IFXObject = {subField = testSub},\n" +
 "}\n";
 String expectedSub = "IFXObject = {\n" +
 "subField = testSub,\n" +
 "}\n";
 assertEquals(expectedTest, IFXObject.stringify(obj1, 0));
 assertEquals(expectedSub, IFXObject.stringify(subObj1, 0));
 }

 @Test(expected = Exception.class)
 public void testStringifyException() throws Exception {
 // test exception case
 IFXObject obj4 = new IFXObject();
 Method method = obj4.getClass().getMethod("getField", new Class[0]);
 method.setAccessible(true);
 method.invoke(obj4, new Object[0]);
 IFXObject.stringify(obj4, 0);
 }

 // MySubclass is assumed to be a subclass of IFXObject
 public static class MySubclass extends IFXObject {
 private String subField;

 public String getSubField() {
 return subField;
 }

 public void setSubField(String subField) {
 this.subField = subField;
 }
 }

}