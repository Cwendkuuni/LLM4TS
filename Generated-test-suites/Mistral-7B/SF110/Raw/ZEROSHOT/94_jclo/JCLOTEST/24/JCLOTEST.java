package edu.mscd.cs.jclo;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.Test;

public class JCLOTest {

 private Object createTestObject() {
 Object testObject = new TestObject();
 return new JCLO(testObject);
 }

 @Test
 public void testConstructorNoArgs() {
 JCLO jclo = new JCLO(null);
 assertNull(jclo.getPrefix());
 assertNull(jclo.getAliases());
 assertNotNull(jclo.getFields());
 assertFalse(jclo.hasEquals());
 assertFalse(jclo.doubleDashes());
 }

 @Test
 public void testConstructorObject() {
 JCLO jclo = new JCLO(new TestObject());
 assertNull(jclo.getPrefix());
 assertNull(jclo.getAliases());
 assertNotNull(jclo.getFields());
 assertFalse(jclo.hasEquals());
 assertFalse(jclo.doubleDashes());
 }

 @Test
 public void testConstructorPrefixObject() {
 JCLO jclo = new JCLO("prefix", new TestObject());
 assertEquals("prefix", jclo.getPrefix());
 assertNull(jclo.getAliases());
 assertNotNull(jclo.getFields());
 assertFalse(jclo.hasEquals());
 assertFalse(jclo.doubleDashes());
 }

 @Test
 public void testConstructorPrefixObjectAliases() {
 JCLO jclo = new JCLO("prefix", new TestObject(), new String[][]{{"a", "b"}, {"c", "d"}});
 assertEquals("prefix", jclo.getPrefix());
 assertArrayEquals(new String[][]{{"a", "b"}, {"c", "d"}}, jclo.getAliases());
 assertNotNull(jclo.getFields());
 assertFalse(jclo.hasEquals());
 assertFalse(jclo.doubleDashes());
 }

 @Test
 public void testGetObject() throws Exception {
 JCLO jclo = createTestObject();
 Field testField = TestObject.class.getDeclaredField("testField");
 jclo.parse(new String[]{"--testField=value"});
 Object value = jclo.getObject(testField);
 assertEquals("value", value.toString());
 }

 @Test(expected = IllegalAccessException.class)
 public void testGetObjectWithIllegalAccess() throws Exception {
 JCLO jclo = createTestObject();
 Field testField = TestObject.class.getDeclaredField("testField");
 jclo.parse(new String[]{"--testField=value"});
 jclo.setObject(testField, "not an object");
 }

 @Test
 public void testGetField() {
 JCLO jclo = createTestObject();
 assertEquals("testField", jclo.getField("testField").getName());
 }

 @Test
 public void testGetValue() {
 JCLO jclo = createTestObject();
 jclo.parse(new String[]{"--testField=value"});
 assertEquals("value", jclo.getValue("testField").toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetValueWithNullField() {
 JCLO jclo = createTestObject();
 assertEquals(null, jclo.getValue(null));
 }

 @Test
 public void testGetUsageType() {
 JCLO jclo = createTestObject();
 assertEquals("boolean[]", jclo.getUsageType(boolean[].class));
 }

 @Test
 public void testUsage() {
 JCLO jclo = new JCLO(new TestObject(), new String[][]{{"a", "b"}, {"c", "d"}});
 assertEquals("-testField=[=boolean]\n-testField2=value1\n-testField3=value2\n--a=b\n--c=d\n", jclo.usage());
 }

 @Test
 public void testParseAdditional() {
 JCLO jclo = new JCLO(new TestObject());
 String[] args = {"--additional=one"};
 jclo.parse(args);
 Object additional = jclo.getObject(JCLO.class.getDeclaredField("additional"));
 assertNotNull(additional);
 assertArrayEquals(new String[]{"one"}, (String[])additional);
 }

 @Test
 public void testAddToArray() {
 JCLO jclo = new JCLO(new TestObject());
 Field testField = TestObject.class.getDeclaredField("testFieldArray");
 jclo.parse(new String[]{"--testFieldArray=value1"});
 Object value1 = jclo.makeObject(int[].class, "value1");
 jclo.setObject(testField, value1);
 jclo.parse(new String[]{"--testFieldArray=value2"});
 Object value2 = jclo.makeObject(int[].class, "value2");
 Object result = jclo.getObject(testField);
 assertArrayEquals(new int[]{value1, value2}, (int[])result);
 }

 @Test
 public void testGetKey() {
 JCLO jclo = new JCLO(new TestObject());
 assertEquals("testField", jclo.getKey("testField"));
 }

 @Test
 public void testGetBooleanValue() {
 JCLO jclo = new JCLO(new TestObject());
 assertEquals("true", jclo.getBooleanValue("--testField=true"));
 assertEquals("false", jclo.getBooleanValue("--testField=false"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetBooleanValueWithInvalidArg() {
 JCLO jclo = new JCLO(new TestObject());
 assertEquals(null, jclo.getBooleanValue("--testField"));
 }

 @Test
 public void testMakeObject() {
 JCLO jclo = new JCLO(new TestObject());
 assertEquals(new Boolean(false), jclo.makeObject(boolean.class, "false"));
 assertEquals(new Byte((byte)1), jclo.makeObject(byte.class, "1"));
 assertEquals(new Short((short)1), jclo.makeObject(short.class, "1"));
 assertEquals(new Integer(1), jclo.makeObject(int.class, "1"));
 assertEquals(new Float(1.0f), jclo.makeObject(float.class, "1.0"));
 assertEquals(new Double(1.0), jclo.makeObject(double.class, "1.0"));
 assertEquals(new Long(1L), jclo.makeObject(long.class, "1"));
 assertEquals("testString", jclo.makeObject(String.class, "testString"));
 assertEquals(new Character('a'), jclo.makeObject(char.class, "a"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMakeObjectWithInvalidType() {
 JCLO jclo = new JCLO(new TestObject());
 jclo.makeObject(int[].class, "testString");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidArg() {
 JCLO jclo = new JCLO(new TestObject());
 jclo.parse(new String[]{"--testField=value=value"});
 }

 @Test
 public void testParse() {
 JCLO jclo = new JCLO(new TestObject());
 String[] args = {"--testField=value1", "--testField2=value2"};
 jclo.parse(args);
 assertEquals("value1", jclo.getValue("testField").toString());
 assertEquals("value2", jclo.getValue("testField2").toString());
 }

 @Test
 public void testGetters() {
 JCLO jclo = createTestObject();
 jclo.parse(new String[]{"--testField=value"});
 assertTrue(jclo.getBoolean("getBoolean"));
 byte testByte = (byte) 1;
 assertEquals(testByte, jclo.getByte("getByte"));
 char testChar = 'a';
 assertEquals(testChar, jclo.getChar("getChar"));
 short testShort = (short) 1;
 assertEquals(testShort, jclo.getShort("getShort"));
 int testInt = 1;
 assertEquals(testInt, jclo.getInt("getInt"));
 float testFloat = 1.0f;
 assertEquals(testFloat, jclo.getFloat("getFloat"));
 double testDouble = 1.0;
 assertEquals(testDouble, jclo.getDouble("getDouble"));
 long testLong = 1L;
 assertEquals(testLong, jclo.getLong("getLong"));
 String testString = "testString";
 assertEquals(testString, jclo.getString("getString"));
 byte[] testBytes = {1};
 assertEquals(testBytes, jclo.getBytes("getBytes"));
 char[] testChars = {'a'};
 assertEquals(testChars, jclo.getChars("getChars"));
 short[] testShorts = {1};
 assertEquals(testShorts, jclo.getShorts("getShorts"));
 int[] testInts = {1};
 assertEquals(testInts, jclo.getInts("getInts"));
 float[] testFloats = {1.0f};
 assertEquals(testFloats, jclo.getFloats("getFloats"));
 double[] testDoubles = {1.0};
 assertEquals(testDoubles, jclo.getDoubles("getDoubles"));
 long[] testLongs = {1L};
 assertEquals(testLongs, jclo.getLongs("getLongs"));
 String[] testStrings = {"testString"};
 assertEquals(testStrings, jclo.getStrings("getStrings"));
 }
}

class TestObject {
 private int testField;
 private int[] testFieldArray;

 @SuppressWarnings("unused")
 private static final Field testFieldField;

 static {
 try {
 testFieldField = TestObject.class.getDeclaredField("testField");
 testFieldField.setAccessible(true);
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }
}