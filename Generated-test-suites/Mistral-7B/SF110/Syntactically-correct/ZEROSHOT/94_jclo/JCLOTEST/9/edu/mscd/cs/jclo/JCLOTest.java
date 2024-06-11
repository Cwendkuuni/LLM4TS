package edu.mscd.cs.jclo;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class JCLOTest {

 private JCLO jclo;
 private Object testObject;

 @Before
 public void setUp() {
 testObject = new TestObject();
 jclo = new JCLO(testObject);
 }

 @Test
 public void testConstructorWithObject() {
 JCLO jclo1 = new JCLO(testObject);
 Assert.assertNotNull(jclo1);
 }

 @Test
 public void testConstructorWithObjectAndAliases() {
 String[][] aliases = {{"alias1", "alias2"}, {"alias3", "alias4"}};
 JCLO jclo2 = new JCLO(testObject, aliases);
 Assert.assertNotNull(jclo2);
 }

 @Test
 public void testConstructorWithPrefixAndObject() {
 String prefix = "prefix_";
 JCLO jclo3 = new JCLO(prefix, testObject);
 Assert.assertNotNull(jclo3);
 }

 @Test
 public void testConstructorWithPrefixAndObjectAndAliases() {
 String prefix = "prefix_";
 String[][] aliases = {{"alias1", "alias2"}, {"alias3", "alias4"}};
 JCLO jclo4 = new JCLO(prefix, testObject, aliases);
 Assert.assertNotNull(jclo4);
 }

 @Test
 public void testGetObject() {
 Field field = null;
 try {
 field = testObject.getClass().getDeclaredField("privateField");
 } catch (NoSuchFieldException e) {
 e.printStackTrace();
 }
 Assert.assertNotNull(field);
 Object value = jclo.getValue("privateField");
 Assert.assertEquals(value, field.get(testObject));
 }

 @Test
 public void testGetField() {
 String key = "privateField";
 Field expectedField = testObject.getClass().getDeclaredField(key);
 Field actualField = jclo.getField(key);
 Assert.assertEquals(expectedField, actualField);
 }

 @Test(expected = NullPointerException.class)
 public void testGetValueWithNullKey() {
 jclo.getValue(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetValueWithInvalidKey() {
 String invalidKey = "nonExistentField";
 jclo.getValue(invalidKey);
 }

 @Test
 public void testSetObject() {
 Field field = null;
 try {
 field = testObject.getClass().getDeclaredField("privateField");
 } catch (NoSuchFieldException e) {
 e.printStackTrace();
 }
 Object valueToSet = new Object();
 jclo.setObject(field, valueToSet);
 Assert.assertEquals(valueToSet, field.get(testObject));
 }

 @Test
 public void testToString() {
 String expectedToString = "privateField: java.lang.Object = ...";
 String actualToString = jclo.toString();
 Assert.assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testUsage() {
 String expectedUsage = "-privateField: java.lang.Object\n";
 String actualUsage = jclo.usage();
 Assert.assertEquals(expectedUsage, actualUsage);
 }

 @Test
 public void testParse() {
 String[] args = {"--privateField=value1", "--privateField=value2"};
 jclo.parse(args);
 Object privateFieldValue1 = jclo.getValue("privateField");
 Assert.assertEquals("value1", privateFieldValue1.toString());
 Object privateFieldValue2 = jclo.getValue("privateField");
 Assert.assertEquals("value2", privateFieldValue2.toString());
 }

 @Test
 public void testGetBoolean() {
 boolean expectedBoolean = true;
 jclo.parse(new String[]{"--booleanField=true"});
 boolean booleanField = jclo.getBoolean("booleanField");
 Assert.assertEquals(expectedBoolean, booleanField);
 }

 @Test
 public void testGetByte() {
 byte expectedByte = 1;
 jclo.parse(new String[]{"--byteField=1"});
 byte byteField = jclo.getByte("byteField");
 Assert.assertEquals(expectedByte, byteField);
 }

 @Test
 public void testGetChar() {
 char expectedChar = 'a';
 jclo.parse(new String[]{"--charField=a"});
 char charField = jclo.getChar("charField");
 Assert.assertEquals(expectedChar, charField);
 }

 @Test
 public void testGetShort() {
 short expectedShort = 1;
 jclo.parse(new String[]{"--shortField=1"});
 short shortField = jclo.getShort("shortField");
 Assert.assertEquals(expectedShort, shortField);
 }

 @Test
 public void testGetInt() {
 int expectedInt = 1;
 jclo.parse(new String[]{"--intField=1"});
 int intField = jclo.getInt("intField");
 Assert.assertEquals(expectedInt, intField);
 }

 @Test
 public void testGetFloat() {
 float expectedFloat = 1.2f;
 jclo.parse(new String[]{"--floatField=1.2"});
 float floatField = jclo.getFloat("floatField");
 Assert.assertEquals(expectedFloat, floatField, 0.001f);
 }

 @Test
 public void testGetDouble() {
 double expectedDouble = 1.2;
 jclo.parse(new String[]{"--doubleField=1.2"});
 double doubleField = jclo.getDouble("doubleField");
 Assert.assertEquals(expectedDouble, doubleField, 0.001);
 }

 @Test
 public void testGetLong() {
 long expectedLong = 1;
 jclo.parse(new String[]{"--longField=1"});
 long longField = jclo.getLong("longField");
 Assert.assertEquals(expectedLong, longField);
 }

 @Test
 public void testGetString() {
 String expectedString = "test";
 jclo.parse(new String[]{"--stringField=test"});
 String stringField = jclo.getString("stringField");
 Assert.assertEquals(expectedString, stringField);
 }

 @Test
 public void testGetBytes() {
 byte[] expectedBytes = {1, 2, 3};
 jclo.parse(new String[]{"--bytesField=1,2,3"});
 byte[] bytesField = (byte[])jclo.getValue("bytesField");
 Assert.assertArrayEquals(expectedBytes, bytesField);
 }

 @Test
 public void testGetChars() {
 char[] expectedChars = {'a', 'b'};
 jclo.parse(new String[]{"--charsField=a,b"});
 char[] charsField = (char[])jclo.getValue("charsField");
 Assert.assertArrayEquals(expectedChars, charsField);
 }

 @Test
 public void testGetShorts() {
 short[] expectedShorts = {1, 2};
 jclo.parse(new String[]{"--shortsField=1,2"});
 short[] shortsField = (short[])jclo.getValue("shortsField");
 Assert.assertArrayEquals(expectedShorts, shortsField);
 }

 @Test
 public void testGetInts() {
 int[] expectedInts = {1, 2};
 jclo.parse(new String[]{"--intsField=1,2"});
 int[] intsField = (int[])jclo.getValue("intsField");
 Assert.assertArrayEquals(expectedInts, intsField);
 }

 @Test
 public void testGetFloats() {
 float[] expectedFloats = {1.2f, 2.3f};
 jclo.parse(new String[]{"--floatsField=1.2,2.3"});
 float[] floatsField = (float[])jclo.getValue("floatsField");
 Assert.assertArrayEquals(expectedFloats, floatsField, 0.001f);
 }

 @Test
 public void testGetDoubles() {
 double[] expectedDoubles = {1.2, 2.3};
 jclo.parse(new String[]{"--doublesField=1.2,2.3"});
 double[] doublesField = (double[])jclo.getValue("doublesField");
 Assert.assertArrayEquals(expectedDoubles, doublesField, 0.001);
 }

 @Test
 public void testGetLongs() {
 long[] expectedLongs = {1, 2};
 jclo.parse(new String[]{"--longsField=1,2"});
 long[] longsField = (long[])jclo.getValue("longsField");
 Assert.assertArrayEquals(expectedLongs, longsField);
 }

 @Test
 public void testGetStrings() {
 String[] expectedStrings = {"test1", "test2"};
 jclo.parse(new String[]{"--stringsField=test1,test2"});
 String[] stringsField = (String[])jclo.getValue("stringsField");
 Assert.assertArrayEquals(expectedStrings, stringsField);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetUnknownField() {
 jclo.getValue("unknownField");
 }
}

 class TestObject {
 private String privateField;
 private int privateIntField;
 private boolean booleanField;
 private byte byteField;
 private char charField;
 private short shortField;
 private int intField;
 private float floatField;
 private double doubleField;
 private long longField;
 private String stringField;
 private byte[] bytesField;
 private char[] charsField;
 private short[] shortsField;
 private int[] intsField;
 private float[] floatsField;
 private double[] doublesField;
 private long[] longsField;
 private String[] stringsField;
 }