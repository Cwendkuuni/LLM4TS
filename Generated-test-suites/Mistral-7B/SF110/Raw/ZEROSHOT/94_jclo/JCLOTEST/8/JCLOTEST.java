package edu.mscd.cs.jclo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;

public class JCLOTest {

 private Object testObject;
 private JCLO jclo;

 @Before
 public void setUp() throws Exception {
 testObject = new TestClass();
 jclo = new JCLO(testObject);
 }

 @After
 public void tearDown() throws Exception {
 // Add tear-down code here, if necessary
 }

 @Test
 public void testConstructorWithObject() {
 JCLO jclo = new JCLO(testObject);
 assertNotNull(jclo);
 }

 @Test
 public void testConstructorWithObjectAndAliases() {
 String[][] aliases = new String[][] {{ "k1", "v1" }, { "k2", "v2" }};
 JCLO jclo = new JCLO(testObject, aliases);
 assertNotNull(jclo);
 }

 @Test
 public void testConstructorWithPrefixAndObject() {
 String prefix = "prefix_";
 JCLO jclo = new JCLO(prefix, testObject);
 assertNotNull(jclo);
 }

 @Test
 public void testConstructorWithPrefixAndObjectAndAliases() {
 String prefix = "prefix_";
 String[][] aliases = new String[][] {{ "k1", "v1" }, { "k2", "v2" }};
 JCLO jclo = new JCLO(prefix, testObject, aliases);
 assertNotNull(jclo);
 }

 @Test
 public void testInitialization() {
 Field[] fields = testObject.getClass().getDeclaredFields();
 JCLO jclo = new JCLO(testObject);
 assertEquals(fields.length, jclo.fields.length);
 for (int i = 0; i < fields.length; i++) {
 Field field = fields[i];
 field.setAccessible(true);
 jclo.fields[i].setAccessible(true);
 assertSame(field, jclo.fields[i]);
 }
 }

 @Test
 public void testGetObject() throws IllegalAccessException {
 Field field = testObject.getClass().getDeclaredField("field1");
 Object value = testObject.getClass().newInstance();
 field.set(testObject, value);
 jclo = new JCLO(testObject);
 Object jcloValue = jclo.getObject(jclo.getField("field1"));
 assertEquals(value, jcloValue);
 }

 @Test(expected = IllegalAccessException.class)
 public void testGetObjectWithIllegalAccess() throws Exception {
 JCLO jclo = new JCLO(testObject);
 jclo.getObject(jclo.getField("nonexistentField"));
 }

 @Test
 public void testGetField() {
 Field field = testObject.getClass().getDeclaredField("field1");
 JCLO jclo = new JCLO(testObject);
 Field jcloField = jclo.getField("field1");
 assertSame(field, jcloField);
 }

 @Test
 public void testGetFieldWithNullKey() {
 JCLO jclo = new JCLO(testObject);
 assertNull(jclo.getField(null));
 }

 @Test
 public void testGetValue() {
 Field field = testObject.getClass().getDeclaredField("field1");
 field.set(testObject, new Integer(42));
 JCLO jclo = new JCLO(testObject);
 assertEquals(42, jclo.getValue("field1").intValue());
 }

 @Test
 public void testGetValueWithNullKey() {
 JCLO jclo = new JCLO(testObject);
 assertNull(jclo.getValue(null));
 }

 @Test
 public void testGetValueWithNonExistentKey() {
 JCLO jclo = new JCLO(testObject);
 assertNull(jclo.getValue("nonexistentField"));
 }

 @Test
 public void testSetObject() throws IllegalAccessException {
 Field field = testObject.getClass().getDeclaredField("field1");
 Object value = new Integer(42);
 JCLO jclo = new JCLO(testObject);
 jclo.setObject(jclo.getField("field1"), value);
 assertEquals(value, field.get(testObject));
 }

 @Test(expected = IllegalAccessException.class)
 public void testSetObjectWithIllegalAccess() throws Exception {
 JCLO jclo = new JCLO(testObject);
 jclo.setObject(jclo.getField("nonexistentField"), new Integer(42));
 }

 @Test
 public void testGetArrayType() {
 Class type = int[].class;
 assertEquals("int[]", jclo.getArrayType(type));
 }

 @Test
 public void testToString() {
 TestClass testClass = new TestClass();
 testClass.field1 = new Integer(42);
 testClass.field2 = new String[] { "hello", "world" };
 JCLO jclo = new JCLO(testClass);
 assertEquals(
 "int: field1 = 42\n" +
 "java.lang.String[]: field2 = [hello, world]\n",
 jclo.toString());
 }

 @Test
 public void testUsage() {
 TestClass testClass = new TestClass();
 JCLO jclo = new JCLO(testClass, null, null);
 String usage = jclo.usage();
 String expected =
 "--field1=int:\n" +
 "--field2=java.lang.String...:\n";
 assertEquals(expected, usage);
 }

 @Test
 public void testParseAdditional() {
 String[] args = new String[] { "--additional", "arg1", "arg2" };
 JCLO jclo = new JCLO(null, null, null);
 jclo.parse(args);
 String[] expectedAdditional = new String[] { "arg1", "arg2" };
 assertArrayEquals(expectedAdditional, jclo.getField("additional").get(jclo.object));
 }

 @Test
 public void testParseWithSingleParameter() {
 String[] args = new String[] { "--field1", "value" };
 JCLO jclo = new JCLO(null, null, null);
 jclo.parse(args);
 assertEquals(42, jclo.getValue("field1").intValue());
 }

 @Test
 public void testParseWithDoubleDashesAndEqualsSign() {
 String[] args = new String[] { "--field1=value" };
 JCLO jclo = new JCLO(null, null, null);
 jclo.parse(args);
 assertEquals(42, jclo.getValue("field1").intValue());
 }

 @Test
 public void testParseWithDoubleDashesAndNoEqualsSign() {
 String[] args = new String[] { "--field1" };
 JCLO jclo = new JCLO(null, null, null);
 jclo.parse(args);
 assertNull(jclo.getValue("field1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithMissingEqualsSign() {
 String[] args = new String[] { "--field1value" };
 JCLO jclo = new JCLO(null, null, null);
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithMissingHyphenBeforeEqualsSign() {
 String[] args = new String[] { "--field1=value" };
 JCLO jclo = new JCLO(null, null, null);
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithMissingPrefix() {
 String[] args = new String[] { "field1=value" };
 JCLO jclo = new JCLO(null, null, null);
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidPrefix() {
 String[] args = new String[] { "--invalidprefixfield1=value" };
 JCLO jclo = new JCLO(null, null, null);
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNonExistentField() {
 String[] args = new String[] { "--nonexistentfield=value" };
 JCLO jclo = new JCLO(null, null, null);
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidType() {
 String[] args = new String[] { "--field1=invalidType=value" };
 JCLO jclo = new JCLO(null, null, null);
 jclo.parse(args);
 }

 @Test
 public void testGetBooleanValue() {
 String arg = "--field1=true";
 String booleanValue = jclo.getBooleanValue(arg);
 assertEquals("true", booleanValue);
 }

 @Test
 public void testGetBooleanValueWithYes() {
 String arg = "--field1=yes";
 String booleanValue = jclo.getBooleanValue(arg);
 assertEquals("true", booleanValue);
 }

 @Test
 public void testMakeObjectWithBoolean() {
 String arg = "--field1=true";
 Object booleanValue = jclo.makeObject("boolean", arg);
 assertTrue(booleanValue instanceof Boolean);
 assertEquals(true, ((Boolean)booleanValue).booleanValue());
 }

 @Test
 public void testMakeObjectWithByte() {
 String arg = "--field1=123";
 Object byteValue = jclo.makeObject("byte", arg);
 assertTrue(byteValue instanceof Byte);
 assertEquals((byte)123, ((Byte)byteValue).byteValue());
 }

 @Test
 public void testMakeObjectWithShort() {
 String arg = "--field1=1234";
 Object shortValue = jclo.makeObject("short", arg);
 assertTrue(shortValue instanceof Short);
 assertEquals((short)1234, ((Short)shortValue).shortValue());
 }

 @Test
 public void testMakeObjectWithInt() {
 String arg = "--field1=12345";
 Object intValue = jclo.makeObject("int", arg);
 assertTrue(intValue instanceof Integer);
 assertEquals(12345, ((Integer)intValue).intValue());
 }

 @Test
 public void testMakeObjectWithFloat() {
 String arg = "--field1=123.45";
 Object floatValue = jclo.makeObject("float", arg);
 assertTrue(floatValue instanceof Float);
 assertEquals(123.45f, ((Float)floatValue).floatValue(), 0.001f);
 }

 @Test
 public void testMakeObjectWithDouble() {
 String arg = "--field1=123.456";
 Object doubleValue = jclo.makeObject("double", arg);
 assertTrue(doubleValue instanceof Double);
 assertEquals(123.456, ((Double)doubleValue).doubleValue(), 0.0001);
 }

 @Test
 public void testMakeObjectWithLong() {
 String arg = "--field1=1234567890";
 Object longValue = jclo.makeObject("long", arg);
 assertTrue(longValue instanceof Long);
 assertEquals(1234567890L, ((Long)longValue).longValue());
 }

 @Test
 public void testMakeObjectWithString() {
 String arg = "--field1=hello world";
 Object stringValue = jclo.makeObject("java.lang.String", arg);
 assertTrue(stringValue instanceof String);
 assertEquals("hello world", (String)stringValue);
 }

 @Test
 public void testMakeObjectWithChar() {
 String arg = "--field1=h";
 Object charValue = jclo.makeObject("char", arg);
 assertTrue(charValue instanceof Character);
 assertEquals('h', ((Character)charValue).charValue());
 }
}

 class TestClass {
 int field1;
 Integer field2;
 String field3;
 String[] field4;
 }