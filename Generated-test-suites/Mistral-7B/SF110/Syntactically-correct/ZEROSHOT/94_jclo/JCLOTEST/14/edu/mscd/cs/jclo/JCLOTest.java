package edu.mscd.cs.jclo;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.Test;

public class JCLOTest {

 private Object createTestObject() {
 final JCLO jclo = new JCLO(new Object());
 return jclo;
 }

 @Test
 public void testJCLOCreation() {
 JCLO jclo = createTestObject();
 assertNotNull(jclo);
 }

 @Test
 public void testJCLOCreationWithPrefix() {
 JCLO jclo = new JCLO("prefix_", new Object());
 assertNotNull(jclo);
 }

 @Test
 public void testJCLOCreationWithPrefixAndAliases() {
 String[][] aliases = { { "a", "aliasA" }, { "b", "aliasB" } };
 JCLO jclo = new JCLO("prefix_", new Object(), aliases);
 assertNotNull(jclo);
 }

 @Test
 public void testGetObject() {
 JCLO jclo = createTestObject();
 Field field = jclo.getClass().getDeclaredFields()[0];
 Object value = jclo.getObject(field);
 assertNotNull(value);
 }

 @Test(expected = IllegalAccessException.class)
 public void testGetObjectWithIllegalAccess() {
 JCLO jclo = createTestObject();
 Field field = jclo.getClass().getDeclaredFields()[0];
 field.setAccessible(false);
 jclo.getObject(field);
 }

 @Test
 public void testGetField() {
 JCLO jclo = createTestObject();
 String key = "field1";
 Field field = jclo.getField(key);
 assertNotNull(field);
 }

 @Test
 public void testGetFieldWithNullKey() {
 JCLO jclo = createTestObject();
 Field field = jclo.getField(null);
 assertNull(field);
 }

 @Test
 public void testGetValue() {
 JCLO jclo = createTestObject();
 String key = "field1";
 Field field = jclo.getClass().getDeclaredFields()[0];
 Object value = jclo.getValue(key);
 assertNotNull(value);
 assertSame(value, jclo.getObject(field));
 }

 @Test
 public void testGetValueWithNullKey() {
 JCLO jclo = createTestObject();
 String key = null;
 Object value = jclo.getValue(key);
 assertNull(value);
 }

 @Test
 public void testGetValueWithNonExistentField() {
 JCLO jclo = createTestObject();
 String key = "nonExistentField";
 Object value = jclo.getValue(key);
 assertNull(value);
 }

 @Test
 public void testToString() {
 JCLO jclo = createTestObject();
 String expected = "class java.lang.Object[]: field1 = [Ljava.lang.Object;";
 String actual = jclo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testUsage() {
 JCLO jclo = createTestObject();
 String expected = "--field1:\n";
 String actual = jclo.usage();
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAdditional() {
 JCLO jclo = createTestObject();
 String[] args = { "--additional=one,two,three" };
 jclo.parse(args);
 String[] actual = (String[])jclo.getObject(jclo.getField("additional"));
 String[] expected = { "one", "two", "three" };
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testParseInvalidAdditional() {
 JCLO jclo = createTestObject();
 String[] args = { "--additional=one,two" };
 jclo.parse(args);
 try {
 jclo.getObject(jclo.getField("additional"));
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testParseBoolean() {
 JCLO jclo = createTestObject();
 String[] args = { "--boolean=true" };
 jclo.parse(args);
 assertTrue(jclo.getBoolean("boolean"));
 }

 @Test
 public void testParseByte() {
 JCLO jclo = createTestObject();
 String[] args = { "--byte=127" };
 jclo.parse(args);
 assertEquals((byte)127, jclo.getByte("byte"));
 }

 @Test
 public void testParseChar() {
 JCLO jclo = createTestObject();
 String[] args = { "--char=A" };
 jclo.parse(args);
 assertEquals((char)'A', jclo.getChar("char"));
 }

 @Test
 public void testParseShort() {
 JCLO jclo = createTestObject();
 String[] args = { "--short=32767" };
 jclo.parse(args);
 assertEquals((short)32767, jclo.getShort("short"));
 }

 @Test
 public void testParseInt() {
 JCLO jclo = createTestObject();
 String[] args = { "--int=2147483647" };
 jclo.parse(args);
 assertEquals(2147483647, jclo.getInt("int"));
 }

 @Test
 public void testParseFloat() {
 JCLO jclo = createTestObject();
 String[] args = { "--float=3.14159265" };
 jclo.parse(args);
 assertEquals(3.14159265f, jclo.getFloat("float"), 0.0001f);
 }

 @Test
 public void testParseDouble() {
 JCLO jclo = createTestObject();
 String[] args = { "--double=6.28318530718225E+01" };
 jclo.parse(args);
 assertEquals(6.28318530718225E+01, jclo.getDouble("double"), 0.00000001);
 }

 @Test
 public void testParseLong() {
 JCLO jclo = createTestObject();
 String[] args = { "--long=9223372036854775807L" };
 jclo.parse(args);
 assertEquals(9223372036854775807L, jclo.getLong("long"));
 }

 @Test
 public void testParseString() {
 JCLO jclo = createTestObject();
 String[] args = { "--string=testString" };
 jclo.parse(args);
 assertEquals("testString", jclo.getString("string"));
 }

 @Test
 public void testParseArrayOfBoolean() {
 JCLO jclo = createTestObject();
 String[] args = { "--booleanArray=true,true,true" };
 jclo.parse(args);
 assertTrue((boolean[])jclo.getObject(jclo.getField("booleanArray"))[0]);
 assertTrue((boolean[])jclo.getObject(jclo.getField("booleanArray"))[1]);
 assertTrue((boolean[])jclo.getObject(jclo.getField("booleanArray"))[2]);
 }

 @Test
 public void testParseArrayOfByte() {
 JCLO jclo = createTestObject();
 String[] args = { "--byteArray=1,2,3" };
 jclo.parse(args);
 byte[] expected = { 1, 2, 3 };
 byte[] actual = (byte[])jclo.getObject(jclo.getField("byteArray"));
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testParseArrayOfChar() {
 JCLO jclo = createTestObject();
 String[] args = { "--charArray=A,B,C" };
 jclo.parse(args);
 char[] expected = { 'A', 'B', 'C' };
 char[] actual = (char[])jclo.getObject(jclo.getField("charArray"));
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testParseArrayOfShort() {
 JCLO jclo = createTestObject();
 String[] args = { "--shortArray=10,20,30" };
 jclo.parse(args);
 short[] expected = { 10, 20, 30 };
 short[] actual = (short[])jclo.getObject(jclo.getField("shortArray"));
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testParseArrayOfInt() {
 JCLO jclo = createTestObject();
 String[] args = { "--intArray=100,200,300" };
 jclo.parse(args);
 int[] expected = { 100, 200, 300 };
 int[] actual = (int[])jclo.getObject(jclo.getField("intArray"));
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testParseArrayOfFloat() {
 JCLO jclo = createTestObject();
 String[] args = { "--floatArray=1.0,2.0,3.0" };
 jclo.parse(args);
 float[] expected = { 1.0f, 2.0f, 3.0f };
 float[] actual = (float[])jclo.getObject(jclo.getField("floatArray"));
 assertArrayEquals(expected, actual, 0.0001f);
 }

 @Test
 public void testParseArrayOfDouble() {
 JCLO jclo = createTestObject();
 String[] args = { "--doubleArray=1.0D,2.0D,3.0D" };
 jclo.parse(args);
 double[] expected = { 1.0D, 2.0D, 3.0D };
 double[] actual = (double[])jclo.getObject(jclo.getField("doubleArray"));
 assertArrayEquals(expected, actual, 0.00000001);
 }

 @Test
 public void testParseArrayOfLong() {
 JCLO jclo = createTestObject();
 String[] args = { "--longArray=10L,20L,30L" };
 jclo.parse(args);
 long[] expected = { 10L, 20L, 30L };
 long[] actual = (long[])jclo.getObject(jclo.getField("longArray"));
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testParseArrayOfString() {
 JCLO jclo = createTestObject();
 String[] args = { "--stringArray=test1,test2,test3" };
 jclo.parse(args);
 String[] expected = { "test1", "test2", "test3" };
 String[] actual = (String[])jclo.getObject(jclo.getField("stringArray"));
 assertArrayEquals(expected, actual);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testParseWithInvalidArgument() {
 JCLO jclo = createTestObject();
 String[] args = { "--unknownOption=value" };
 jclo.parse(args);
 }
}