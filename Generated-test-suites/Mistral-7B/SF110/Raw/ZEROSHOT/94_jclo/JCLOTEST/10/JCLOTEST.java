package edu.mscd.cs.jclo;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

public class JCLOTest {

 private Object testObject;
 private JCLO jclo;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 testObject = new TestClass();
 jclo = new JCLO(testObject);
 }

 @Test
 public void testConstructorWithObject() {
 JCLO jclo1 = new JCLO(testObject);
 Assert.assertNotSame(jclo, jclo1);
 }

 @Test
 public void testConstructorWithObjectAndAliases() {
 JCLO jclo2 = new JCLO(testObject, new String[][]{{"a", "aliasA"}, {"b", "aliasB"}});
 Assert.assertNotSame(jclo, jclo2);
 }

 @Test
 public void testConstructorWithPrefixAndObject() {
 JCLO jclo3 = new JCLO("prefix", testObject);
 JCLO jclo4 = new JCLO("prefix", testObject, new String[][]{{"a", "aliasA"}, {"b", "aliasB"}});
 Assert.assertNotSame(jclo3, jclo4);
 }

 @Test
 public void testConstructorWithPrefixAndObjectAndAliases() {
 JCLO jclo5 = new JCLO("prefix", testObject, new String[][]{{"a", "aliasA"}, {"b", "aliasB"}});
 Assert.assertEquals("prefix", jclo5.getPrefix());
 }

 @Test
 public void testGetObject() {
 Field field = null;
 try {
 field = testObject.getClass().getDeclaredField("field1");
 } catch (NoSuchFieldException e) {
 e.printStackTrace();
 }
 Assert.assertNotNull(field);
 Object fieldValue = jclo.getObject(field);
 Assert.assertEquals(testObject, jclo.getObject(field));
 }

 @Test
 public void testGetField() {
 Field field = null;
 try {
 field = testObject.getClass().getDeclaredField("field1");
 } catch (NoSuchFieldException e) {
 e.printStackTrace();
 }
 Assert.assertNotNull(field);
 Field jcloField = jclo.getField("field1");
 Assert.assertEquals(field, jcloField);
 }

 @Test
 public void testGetValue() {
 Field field = null;
 try {
 field = testObject.getClass().getDeclaredField("field1");
 } catch (NoSuchFieldException e) {
 e.printStackTrace();
 }
 Assert.assertNotNull(field);
 Object fieldValue = jclo.getValue("field1");
 Assert.assertEquals(fieldValue, jclo.getObject(field));
 }

 @Test
 public void testSetObject() {
 Field field = null;
 try {
 field = testObject.getClass().getDeclaredField("field1");
 } catch (NoSuchFieldException e) {
 e.printStackTrace();
 }
 Assert.assertNotNull(field);
 Object newValue = new Object();
 jclo.setObject(field, newValue);
 Assert.assertEquals(newValue, jclo.getObject(field));
 }

 @Test
 public void testGetArrayType() {
 Class testClass = testObject.getClass();
 Class arrayClass = testClass.getComponentType();
 String arrayType = jclo.getArrayType(arrayClass);
 Assert.assertEquals(arrayType, "Object");
 }

 @Test
 public void testToString() {
 String expected = "Object[]: field1 = [Ljava.lang.Object;@66266262\n";
 String actual = jclo.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testUsage() {
 String expected = "-field1:\n";
 String actual = jclo.usage();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testParseAdditional() {
 String[] args = new String[]{"--additional", "arg1", "arg2"};
 jclo.parse(args);
 Object additional = jclo.getValue("additional");
 Assert.assertNotNull(additional);
 Assert.assertEquals(Arrays.asList("arg1", "arg2"), Arrays.asList((Object[])additional));
 }

 @Test
 public void testAddToArray() {
 Field field = null;
 try {
 field = testObject.getClass().getDeclaredField("field1");
 } catch (NoSuchFieldException e) {
 e.printStackTrace();
 }
 Object originalArray = jclo.getObject(field);
 Object newElement = new Object();
 jclo.addToArray(field, newElement);
 Object updatedArray = jclo.getObject(field);
 int length = Array.getLength(originalArray);
 Assert.assertEquals(length + 1, Array.getLength(updatedArray));
 Assert.assertEquals(updatedArray, Array.newInstance(field.getType().getComponentType(), length + 1));
 }

 @Test
 public void testGetKey() {
 String arg = "--field1=value";
 String expected = "field1";
 String actual = jclo.getKey(arg);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testGetBooleanValue() {
 String arg = "--boolean_flag";
 String expected = "true";
 String actual = jclo.getBooleanValue(arg);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMakeObject() {
 String stringValue = "true";
 String intValue = "123";
 String floatValue = "123.456";
 String longValue = "1234567890123456789";
 String stringClassValue = "String";
 String booleanClassValue = "boolean";
 Object booleanObject = jclo.makeObject(booleanClassValue, stringValue);
 Assert.assertTrue(booleanObject instanceof Boolean);
 Object intObject = jclo.makeObject("int", intValue);
 Assert.assertTrue(intObject instanceof Integer);
 Object floatObject = jclo.makeObject("float", floatValue);
 Assert.assertTrue(floatObject instanceof Float);
 Object longObject = jclo.makeObject("long", longValue);
 Assert.assertTrue(longObject instanceof Long);
 Object stringObject = jclo.makeObject(stringClassValue, "");
 Assert.assertTrue(stringObject instanceof String);
 }

 @Test
 public void testGetEqualsValue() {
 String arg = "--boolean_flag=true";
 String expected = "true";
 String actual = jclo.getEqualsValue(arg);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidArgumentFormat() {
 String[] args = new String[]{"--field1", "value"};
 jclo.parse(args);
 }

 @Test
 public void testParseWithBooleanArgument() {
 String[] args = new String[]{"--boolean_flag"};
 jclo.parse(args);
 Assert.assertTrue(jclo.getBoolean("boolean_flag"));
 }

 @Test
 public void testParseWithStringArgument() {
 String[] args = new String[]{"--string_field", "value"};
 jclo.parse(args);
 Assert.assertEquals("value", jclo.getString("string_field"));
 }

 @Test
 public void testParseWithIntegerArgument() {
 String[] args = new String[]{"--int_field", "123"};
 jclo.parse(args);
 Assert.assertEquals(123, jclo.getInt("int_field"));
 }

 @Test
 public void testParseWithFloatArgument() {
 String[] args = new String[]{"--float_field", "123.456"};
 jclo.parse(args);
 Assert.assertEquals(123.456F, jclo.getFloat("float_field"), 0.001F);
 }

 @Test
 public void testParseWithDoubleArgument() {
 String[] args = new String[]{"--double_field", "123.456"};
 jclo.parse(args);
 Assert.assertEquals(123.456D, jclo.getDouble("double_field"), 0.001D);
 }

 @Test
 public void testParseWithLongArgument() {
 String[] args = new String[]{"--long_field", "1234567890123456789"};
 jclo.parse(args);
 Assert.assertEquals(1234567890123456789L, jclo.getLong("long_field"));
 }

 @Test
 public void testParseWithArrayArgument() {
 String[] args = new String[]{"--array_field", "1,2,3"};
 jclo.parse(args);
 Object[] expectedArray = new Object[]{1, 2, 3};
 Object[] actualArray = (Object[])jclo.getValue("array_field");
 Assert.assertArrayEquals(expectedArray, actualArray);
 }

 @Test
 public void testParseWithAliases() {
 String[] args = new String[]{"--aliasA", "valueA"};
 jclo.parse(args);
 String expectedValueA = "valueA";
 String actualValueA = (String)jclo.getValue("field1");
 Assert.assertEquals(expectedValueA, actualValueA);
 }

 @Test
 public void testParseWithPrefix() {
 String prefix = "prefix_";
 JCLO jcloPrefix = new JCLO(prefix, testObject);
 jcloPrefix.parse(new String[]{"--prefix_field1", "value1"});
 String expectedValue1 = "value1";
 String actualValue1 = (String)jcloPrefix.getValue("field1");
 Assert.assertEquals(expectedValue1, actualValue1);
 }
}

 class TestClass {
 private Object field1;
 }