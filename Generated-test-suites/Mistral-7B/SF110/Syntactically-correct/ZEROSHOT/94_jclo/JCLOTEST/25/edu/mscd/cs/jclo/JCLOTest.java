package edu.mscd.cs.jclo;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JCLOTest {

 private Object testObject;
 private JCLO jclo;

 @Before
 public void setUp() {
 testObject = new TestClass();
 jclo = new JCLO(testObject);
 }

 @After
 public void tearDown() {
 jclo = null;
 testObject = null;
 }

 @Test
 public void testConstructorWithObject() {
 JCLO jcloWithObject = new JCLO(testObject);
 assertEquals(testObject.getClass(), jcloWithObject.object.getClass());
 }

 @Test
 public void testConstructorWithAliases() {
 String[][] aliases = new String[][]{{"a", "aliasA"}, {"b", "aliasB"}};
 JCLO jcloWithAliases = new JCLO(testObject, aliases);
 assertEquals(testObject.getClass(), jcloWithAliases.object.getClass());
 assertNotNull(jcloWithAliases.aliases);
 assertEquals(2, jcloWithAliases.aliases.length);
 }

 @Test
 public void testConstructorWithPrefix() {
 String prefix = "prefix_";
 JCLO jcloWithPrefix = new JCLO(prefix, testObject);
 assertEquals(prefix, jcloWithPrefix.prefix);
 }

 @Test
 public void testConstructorWithPrefixAndAliases() {
 String prefix = "prefix_";
 String[][] aliases = new String[][]{{"a", "aliasA"}, {"b", "aliasB"}};
 JCLO jcloWithPrefixAndAliases = new JCLO(prefix, testObject, aliases);
 assertEquals(prefix, jcloWithPrefixAndAliases.prefix);
 assertNotNull(jcloWithPrefixAndAliases.aliases);
 assertEquals(2, jcloWithPrefixAndAliases.aliases.length);
 }

 @Test(expected = IllegalAccessException.class)
 public void testGetObjectWithInaccessibleField() throws IllegalAccessException {
 Field inaccessibleField = testObject.getClass().getDeclaredField("inaccessibleField");
 inaccessibleField.setAccessible(true);
 jclo.getObject(inaccessibleField);
 }

 @Test
 public void testGetField() {
 String fieldName = "field1";
 Field field = jclo.getField(fieldName);
 assertNotNull(field);
 assertEquals(testObject.getClass().getDeclaredField(fieldName), field);
 }

 @Test
 public void testGetValue() {
 String fieldName = "field1";
 Object expectedValue = "expectedValue";
 testObject.field1 = expectedValue;
 Object actualValue = jclo.getValue(fieldName);
 assertEquals(expectedValue, actualValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetValueWithUnknownField() {
 String unknownFieldName = "unknownField";
 jclo.getValue(unknownFieldName);
 }

 @Test
 public void testSetObject() {
 String fieldName = "field1";
 Object newValue = "newValue";
 Object oldValue = testObject.field1;
 Field field = jclo.getField(fieldName);
 jclo.setObject(field, newValue);
 assertEquals(newValue, testObject.field1);
 assertEquals(oldValue, jclo.getValue(fieldName));
 }

 @Test(expected = IllegalAccessException.class)
 public void testSetObjectWithInaccessibleField() throws IllegalAccessException {
 Field inaccessibleField = testObject.getClass().getDeclaredField("inaccessibleField");
 inaccessibleField.setAccessible(true);
 jclo.setObject(inaccessibleField, "newValue");
 }

 @Test
 public void testToString() {
 String expectedToString = "int: field1 = 1\n"
 + "String: field2 = testObject\n";
 String actualToString = jclo.toString();
 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testUsage() {
 String expectedUsage = "-field1: [=int]\n"
 + "-field2: -\n"
 + "-additional: =\n";
 String actualUsage = jclo.usage();
 assertEquals(expectedUsage, actualUsage);
 }

 @Test
 public void testParseWithNoArguments() {
 String[] args = {};
 jclo.parse(args);
 assertFalse(jclo.hasEquals);
 assertFalse(jclo.doubleDashes);
 assertNull(jclo.getField("additional"));
 }

 @Test
 public void testParseWithDoubleDashes() {
 String[] args = {"--doubleDashes"};
 jclo.parse(args);
 assertTrue(jclo.doubleDashes);
 assertFalse(jclo.hasEquals);
 assertNull(jclo.getField("additional"));
 }

 @Test
 public void testParseWithEquals() {
 String[] args = {"--field1=5"};
 jclo.parse(args);
 assertTrue(jclo.hasEquals);
 assertFalse(jclo.doubleDashes);
 assertEquals(5, jclo.getInt("field1"));
 }

 @Test
 public void testParseWithDoubleDashesAndEquals() {
 String[] args = {"--doubleDashes--field1=5"};
 jclo.parse(args);
 assertTrue(jclo.doubleDashes);
 assertTrue(jclo.hasEquals);
 assertEquals(5, jclo.getInt("field1"));
 }

 @Test
 public void testParseWithAdditional() {
 String[] args = {"--additional=value1,value2"};
 jclo.parse(args);
 Object[] expectedAdditional = new Object[]{"value1", "value2"};
 assertNotNull(jclo.getField("additional"));
 assertArrayEquals(expectedAdditional, (Object[])jclo.getValue("additional"));
 }

 @Test
 public void testParseWithArray() {
 String[] args = {"--arrayField=1,2,3"};
 jclo.parse(args);
 Object[] expectedArray = new Object[]{1, 2, 3};
 assertArrayEquals(expectedArray, (Object[])jclo.getValue("arrayField"));
 }

 @Test
 public void testParseWithBoolean() {
 String[] args = {"--booleanField=true"};
 jclo.parse(args);
 assertTrue(jclo.getBoolean("booleanField"));
 }

 @Test
 public void testParseWithChar() {
 String[] args = {"--charField=a"};
 jclo.parse(args);
 assertEquals('a', jclo.getChar("charField"));
 }

 @Test
 public void testParseWithShort() {
 String[] args = {"--shortField=5"};
 jclo.parse(args);
 assertEquals((short)5, jclo.getShort("shortField"));
 }

 @Test
 public void testParseWithInt() {
 String[] args = {"--intField=10"};
 jclo.parse(args);
 assertEquals(10, jclo.getInt("intField"));
 }

 @Test
 public void testParseWithFloat() {
 String[] args = {"--floatField=3.14"};
 jclo.parse(args);
 assertEquals(3.14f, jclo.getFloat("floatField"), 0.0f);
 }

 @Test
 public void testParseWithDouble() {
 String[] args = {"--doubleField=3.14159"};
 jclo.parse(args);
 assertEquals(3.14159, jclo.getDouble("doubleField"), 0.0);
 }

 @Test
 public void testParseWithLong() {
 String[] args = {"--longField=1000000"};
 jclo.parse(args);
 assertEquals(1000000, jclo.getLong("longField"));
 }

 @Test
 public void testParseWithString() {
 String[] args = {"--stringField=Hello, World!"};
 jclo.parse(args);
 assertEquals("Hello, World!", jclo.getString("stringField"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithUnknownField() {
 String[] args = {"--unknownField=value"};
 jclo.parse(args);
 }
}

 class TestClass {
 private int field1;
 private String field2;
 private int[] arrayField;
 private boolean booleanField;
 private char charField;
 private short shortField;
 private byte byteField;
 private long longField;
 private float floatField;
 private double doubleField;
 }