package edu.mscd.cs.jclo;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.Test;

public class JCLOTest {

 private Object testObject;
 private JCLO jclo;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 testObject = new Object() {
 };
 jclo = new JCLO(testObject);
 }

 @Test
 public void testJCLOConstructorWithObject() {
 jclo = new JCLO(testObject);
 assertNull(jclo.prefix);
 assertSame(testObject, jclo.object);
 assertNull(jclo.aliases);
 assertEquals(testObject.getClass().getDeclaredFields().length, jclo.fields.length);
 }

 @Test
 public void testJCLOConstructorWithObjectAndAliases() {
 String[][] aliases = { { "a", "aliasA" }, { "b", "aliasB" } };
 jclo = new JCLO(testObject, aliases);
 assertNull(jclo.prefix);
 assertSame(testObject, jclo.object);
 assertSame(aliases, jclo.aliases);
 assertEquals(testObject.getClass().getDeclaredFields().length, jclo.fields.length);
 }

 @Test
 public void testJCLOConstructorWithPrefixAndObject() {
 String prefix = "prefix";
 jclo = new JCLO(prefix, testObject);
 assertEquals(prefix, jclo.prefix);
 assertSame(testObject, jclo.object);
 assertNull(jclo.aliases);
 }

 @Test
 public void testJCLOConstructorWithPrefixAndObjectAndAliases() {
 String prefix = "prefix";
 String[][] aliases = { { "a", "aliasA" }, { "b", "aliasB" } };
 jclo = new JCLO(prefix, testObject, aliases);
 assertEquals(prefix, jclo.prefix);
 assertSame(testObject, jclo.object);
 assertSame(aliases, jclo.aliases);
 }

 @Test
 public void testGetField() {
 Field testField = null;
 try {
 testField = testObject.getClass().getDeclaredField("testField");
 } catch (NoSuchFieldException e) {
 e.printStackTrace();
 }
 jclo.getField("testField");
 assertSame(testField, jclo.getField("testField"));
 }

 @Test(expected = NullPointerException.class)
 public void testGetFieldNullKey() {
 jclo.getField(null);
 }

 @Test(expected = NoSuchFieldException.class)
 public void testGetFieldNonExistentField() {
 jclo.getField("nonExistentField");
 }

 @Test
 public void testGetValue() {
 String testKey = "testField";
 Field testField = null;
 try {
 testField = testObject.getClass().getDeclaredField(testKey);
 testField.set(testObject, "testValue");
 } catch (NoSuchFieldException | IllegalAccessException e) {
 e.printStackTrace();
 }
 Object testValue = jclo.getValue(testKey);
 assertSame("testValue", testValue);
 }

 @Test(expected = NullPointerException.class)
 public void testGetValueNullKey() {
 jclo.getValue(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetValueNonExistentField() {
 jclo.getValue("nonExistentField");
 }

 @Test
 public void testSetObject() {
 Field testField = null;
 try {
 testField = testObject.getClass().getDeclaredField("testField");
 testField.set(testObject, "testValue");
 } catch (NoSuchFieldException | IllegalAccessException e) {
 e.printStackTrace();
 }
 Object testValue = new Object() {
 };
 jclo.setObject(testField, testValue);
 assertEquals(testValue, jclo.getValue("testField"));
 }

 @Test(expected = NullPointerException.class)
 public void testSetObjectNullField() {
 Field nullField = null;
 jclo.setObject(nullField, new Object() {
 });
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetObjectNonExistentField() {
 Field nonExistentField = null;
 try {
 nonExistentField = testObject.getClass().getDeclaredField("nonExistentField");
 } catch (NoSuchFieldException e) {
 e.printStackTrace();
 }
 jclo.setObject(nonExistentField, new Object() {
 });
 }

 @Test
 public void testGetUsageTypeBoolean() {
 assertEquals("[=boolean]", jclo.getUsageType(boolean.class));
 }

 @Test
 public void testGetUsageTypeArray() {
 assertEquals("byte...", jclo.getUsageType(byte[].class));
 }

 @Test
 public void testUsage() {
 String expectedUsage = "--testField:boolean--testField2:byte--testField3:java.lang.String\n";
 assertEquals(expectedUsage, jclo.usage());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidArgument() {
 String[] args = { "--testField--" };
 jclo.parse(args);
 }

 @Test
 public void testParseWithBoolean() {
 String[] args = { "--testField=true" };
 jclo.parse(args);
 assertTrue(jclo.getBoolean("testField"));
 }

 @Test
 public void testParseWithByte() {
 String[] args = { "--testField=1" };
 jclo.parse(args);
 assertEquals((byte)1, jclo.getByte("testField"));
 }

 @Test
 public void testParseWithShort() {
 String[] args = { "--testField=123" };
 jclo.parse(args);
 assertEquals((short)123, jclo.getShort("testField"));
 }

 @Test
 public void testParseWithInt() {
 String[] args = { "--testField=12345" };
 jclo.parse(args);
 assertEquals(12345, jclo.getInt("testField"));
 }

 @Test
 public void testParseWithFloat() {
 String[] args = { "--testField=123.45" };
 jclo.parse(args);
 assertEquals(123.45f, jclo.getFloat("testField"), 0.01f);
 }

 @Test
 public void testParseWithDouble() {
 String[] args = { "--testField=123.456" };
 jclo.parse(args);
 assertEquals(123.456, jclo.getDouble("testField"), 0.001);
 }

 @Test
 public void testParseWithLong() {
 String[] args = { "--testField=123456789012345" };
 jclo.parse(args);
 assertEquals(123456789012345L, jclo.getLong("testField"));
 }

 @Test
 public void testParseWithString() {
 String[] args = { "--testField=testString" };
 jclo.parse(args);
 assertEquals("testString", jclo.getString("testField"));
 }

 @Test
 public void testParseWithStringArray() {
 String[] args = { "--testField=testString1,testString2" };
 jclo.parse(args);
 String[] testStringArray = (String[])jclo.getValue("testField");
 assertArrayEquals(new String[] { "testString1", "testString2" }, testStringArray);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidType() {
 String[] args = { "--testField=invalidType" };
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithMissingValue() {
 String[] args = { "--testField" };
 jclo.parse(args);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testParseWithMissingValueAndHasArray() {
 String[] args = { "--testField--" };
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidAlias() {
 String[] args = { "--testField__alias--" };
 jclo.parse(args);
 }
}