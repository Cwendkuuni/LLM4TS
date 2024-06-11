package edu.mscd.cs.jclo;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.junit.Test;

public class JCLOTest {

 private static final String TEST_CLASS_NAME = "JCLO";
 private static final String TEST_OBJECT_CLASS_NAME = "java.lang.String";

 @Test
 public void testJCLOConstructorWithObject() {
 JCLO jclosure = new JCLO(new Object());
 assertEquals(1, jclosure.getFields().length);
 }

 @Test
 public void testJCLOConstructorWithObjectAndAliases() {
 String[][] aliases = new String[1][2];
 aliases[0] = new String[]{"alias1", "alias2"};
 JCLO jclosure = new JCLO(new Object(), aliases);
 assertEquals(1, jclosure.getFields().length);
 }

 @Test
 public void testJCLOConstructorWithPrefixAndObject() {
 JCLO jclosure = new JCLO("prefix", new Object());
 assertEquals(1, jclosure.getFields().length);
 }

 @Test
 public void testJCLOConstructorWithPrefixAndObjectAndAliases() {
 String[][] aliases = new String[1][2];
 aliases[0] = new String[]{"alias1", "alias2"};
 JCLO jclosure = new JCLO("prefix", new Object(), aliases);
 assertEquals(1, jclosure.getFields().length);
 }

 @Test
 public void testGetObject() {
 Object testObject = new Object();
 JCLO jclosure = new JCLO(testObject);
 Field field = jclosure.getFields()[0];
 Object getValue = jclosure.getObject(field);
 assertEquals(testObject, getValue);
 }

 @Test
 public void testGetField() {
 JCLO jclosure = new JCLO(new Object());
 String key = "field1";
 Field expectedField = new Field("field1", Integer.TYPE, Modifier.PRIVATE, null, null);
 Field actualField = jclosure.getField(key);
 assertEquals(expectedField, actualField);
 }

 @Test(expected = NullPointerException.class)
 public void testGetFieldWithNullKey() {
 JCLO jclosure = new JCLO(new Object());
 Field actualField = jclosure.getField(null);
 fail("Expected NullPointerException, but got: " + actualField);
 }

 @Test
 public void testGetValue() {
 JCLO jclosure = new JCLO(new Object());
 Field field = jclosure.getFields()[0];
 Object expectedValue = new Integer(1);
 Object actualValue = jclosure.getValue(field.getName());
 assertEquals(expectedValue, actualValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetValueWithNonExistentField() {
 JCLO jclosure = new JCLO(new Object());
 String nonExistentFieldName = "nonExistentField";
 jclosure.getValue(nonExistentFieldName);
 fail("Expected IllegalArgumentException, but got: null");
 }

 @Test
 public void testSetObject() {
 JCLO jclosure = new JCLO(new Object());
 Field field = jclosure.getFields()[0];
 Integer originalValue = (Integer) jclosure.getValue(field.getName());
 Integer newValue = new Integer(2);
 jclosure.setObject(field, newValue);
 Integer newValueAfterSet = (Integer) jclosure.getValue(field.getName());
 assertEquals(newValue, newValueAfterSet);
 assertEquals(originalValue.intValue(), originalValue.intValue());
 }

 @Test(expected = IllegalAccessException.class)
 public void testSetObjectWithIllegalAccess() {
 JCLO jclosel = new JCLO(new Object());
 Field field = jclosel.getFields()[0];
 field.setAccessible(false);
 Integer newValue = new Integer(2);
 jclosel.setObject(field, newValue);
 fail("Expected IllegalAccessException, but got: null");
 }

 @Test
 public void testGetArrayType() {
 assertEquals("int[]", JCLO.getArrayType(int[].class));
 }

 @Test
 public void testToString() {
 JCLO jclosure = new JCLO(new Object());
 StringBuilder expectedStringBuilder = new StringBuilder();
 expectedStringBuilder.append("int: field1 = 1\n");
 String expectedString = expectedStringBuilder.toString();
 String actualString = jclosure.toString();
 assertEquals(expectedString, actualString);
 }

 @Test
 public void testUsage() {
 JCLO jclosure = new JCLO(new Object());
 StringBuilder expectedStringBuilder = new StringBuilder();
 expectedStringBuilder.append("--field1\n");
 expectedStringBuilder.append("--field1=[boolean]\n");
 expectedStringBuilder.append("--field1=[int]\n");
 expectedStringBuilder.append("--field1=[int...]\n");
 String expectedString = expectedStringBuilder.toString();
 String actualString = jclosure.usage();
 assertEquals(expectedString, actualString);
 }

 @Test
 public void testParseAdditional() {
 String[] args = new String[]{"--additional=arg1", "--additional=arg2"};
 JCLO jclosure = new JCLO(new Object());
 jclosure.parseAdditional(args, 0);
 Object additional = jclosure.getValue("additional");
 assertArrayEquals(new String[]{"arg1", "arg2"}, (String[]) additional);
 }

 @Test
 public void testParseWithoutAdditional() {
 String[] args = new String[0];
 JCLO jclosure = new JCLO(new Object());
 jclosure.parse(args);
 assertTrue(jclosure.getFields()[0].isAccessible());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithIllegalArgument() {
 String[] args = new String[]{"--illegalArgument"};
 JCLO jclosel = new JCLO(new Object());
 try {
 jclosel.parse(args);
 fail("Expected IllegalArgumentException, but got: null");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseWithBooleanOption() {
 String[] args = new String[]{"--doubleDashes", "--hasEquals", "--booleanOption=true"};
 JCLO jclosure = new JCLO(new Object());
 jclosure.parse(args);
 assertTrue(jclosure.getBoolean("booleanOption"));
 }

 @Test
 public void testParseWithByteOption() {
 String[] args = new String[]{"--doubleDashes", "--hasEquals", "--byteOption=10"};
 JCLO jclosure = new JCLO(new Object());
 jclosure.parse(args);
 assertEquals((byte) 10, jclosure.getByte("byteOption"));
 }

 @Test
 public void testParseWithCharOption() {
 String[] args = new String[]{"--doubleDashes", "--hasEquals", "--charOption=a"};
 JCLO jclosure = new JCLO(new Object());
 jclosure.parse(args);
 assertEquals('a', jclosure.getChar("charOption"));
 }

 @Test
 public void testParseWithShortOption() {
 String[] args = new String[]{"--doubleDashes", "--hasEquals", "--shortOption=20"};
 JCLO jclosure = new JCLO(new Object());
 jclosure.parse(args);
 assertEquals((short) 20, jclosure.getShort("shortOption"));
 }

 @Test
 public void testParseWithIntOption() {
 String[] args = new String[]{"--doubleDashes", "--hasEquals", "--intOption=30"};
 JCLO jclosure = new JCLO(new Object());
 jclosure.parse(args);
 assertEquals(30, jclosure.getInt("intOption"));
 }

 @Test
 public void testParseWithFloatOption() {
 String[] args = new String[]{"--doubleDashes", "--hasEquals", "--floatOption=30.5"};
 JCLO jclosure = new JCLO(new Object());
 jclosure.parse(args);
 assertEquals(30.5f, jclosure.getFloat("floatOption"), 0.01f);
 }

 @Test
 public void testParseWithDoubleOption() {
 String[] args = new String[]{"--doubleDashes", "--hasEquals", "--doubleOption=30.5"};
 JCLO jclosure = new JCLO(new Object());
 jclosure.parse(args);
 assertEquals(30.5, jclosure.getDouble("doubleOption"), 0.01);
 }

 @Test
 public void testParseWithLongOption() {
 String[] args = new String[]{"--doubleDashes", "--hasEquals", "--longOption=30L"};
 JCLO jclosure = new JCLO(new Object());
 jclosure.parse(args);
 assertEquals(30L, jclosure.getLong("longOption"));
 }

 @Test
 public void testParseWithStringOption() {
 String[] args = new String[]{"--doubleDashes", "--hasEquals", "--stringOption=test"};
 JCLO jclosure = new JCLO(new Object());
 jclosure.parse(args);
 assertEquals("test", jclosure.getString("stringOption"));
 }

 @Test
 public void testParseWithByteArrayOption() {
 String[] args = new String[]{"--doubleDashes", "--hasEquals", "--byteArrayOption=1,2,3"};
 JCLO jclosure = new JCLO(new Object());
 jclosure.parse(args);
 assertArrayEquals(new byte[]{(byte) 1, (byte) 2, (byte) 3}, (byte[]) jclosure.getValue("byteArrayOption"));
 }

 @Test
 public void testParseWithCharArrayOption() {
 String[] args = new String[]{"--doubleDashes", "--hasEquals", "--charArrayOption=a,b,c"};
 JCLO jclosure = new JCLO(new Object());
 jclosure.parse(args);
 assertArrayEquals(new char[]{'a', 'b', 'c'}, (char[]) jclosure.getValue("charArrayOption"));
 }

 @Test
 public void testParseWithShortArrayOption() {
 String[] args = new String[]{"--doubleDashes", "--hasEquals", "--shortArrayOption=1,2,3"};
 JCLO jclosure = new JCLO(new Object());
 jclosure.parse(args);
 assertArrayEquals(new short[]{(short) 1, (short) 2, (short) 3}, (short[]) jclosure.getValue("shortArrayOption"));
 }

 @Test
 public void testParseWithIntArrayOption() {
 String[] args = new String[]{"--doubleDashes", "--hasEquals", "--intArrayOption=1,2,3"};
 JCLO jclosure = new JCLO(new Object());
 jclosure.parse(args);
 assertArrayEquals(new int[]{1, 2, 3}, (int[]) jclosure.getValue("intArrayOption"));
 }

 @Test
 public void testParseWithFloatArrayOption() {
 String[] args = new String[]{"--doubleDashes", "--hasEquals", "--floatArrayOption=1.0,2.0,3.0"};
 JCLO jclosure = new JCLO(new Object());
 jclosure.parse(args);
 assertArrayEquals(new float[]{1.0f, 2.0f, 3.0f}, (float[]) jclosure.getValue("floatArrayOption"));
 }

 @Test
 public void testParseWithDoubleArrayOption() {
 String[] args = new String[]{"--doubleDashes", "--hasEquals", "--doubleArrayOption=1.0,2.0,3.0"};
 JCLO jclosure = new JCLO(new Object());
 jclosure.parse(args);
 assertArrayEquals(new double[]{1.0, 2.0, 3.0}, (double[]) jclosure.getValue("doubleArrayOption"));
 }

 @Test
 public void testParseWithLongArrayOption() {
 String[] args = new String[]{"--doubleDashes", "--hasEquals", "--longArrayOption=1L,2L,3L"};
 JCLO jclosure = new JCLO(new Object());
 jclosure.parse(args);
 assertArrayEquals(new long[]{1L, 2L, 3L}, (long[]) jclosure.getValue("longArrayOption"));
 }

 @Test
 public void testParseWithStringArrayOption() {
 String[] args = new String[]{"--doubleDashes", "--hasEquals", "--stringArrayOption=test1,test2,test3"};
 JCLO jclosure = new JCLO(new Object());
 jclosure.parse(args);
 assertArrayEquals(new String[]{"test1", "test2", "test3"}, (String[]) jclosure.getValue("stringArrayOption"));
 }

 @Test
 public void testMainWithVersionOption() {
 String[] args = new String[]{"--version"};
 JCLO.main(args);
 }

}