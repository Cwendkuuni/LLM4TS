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
 public void setUp() {
 testObject = new TestObject();
 jclo = new JCLO(testObject);
 }

 @After
 public void tearDown() {
 jclo = null;
 testObject = null;
 }

 @Test
 public void testJCLOWithNoArguments() {
 JCLO jclo1 = new JCLO(testObject);
 assertEquals("", jclo1.getPrefix());
 assertNull(jclo1.getAliases());
 }

 @Test
 public void testJCLOWithPrefix() {
 JCLO jclo2 = new JCLO("prefix", testObject);
 assertEquals("prefix", jclo2.getPrefix());
 assertNull(jclo2.getAliases());
 }

 @Test
 public void testJCLOWithAliases() {
 String[][] aliases = new String[][]{{"a", "b"}, {"c", "d"}};
 JCLO jclo3 = new JCLO(testObject, aliases);
 assertEquals("", jclo3.getPrefix());
 assertEquals(aliases, jclo3.getAliases());
 }

 @Test
 public void testJCLOWithPrefixAndAliases() {
 String[][] aliases = new String[][]{{"a", "b"}, {"c", "d"}};
 JCLO jclo4 = new JCLO("prefix", testObject, aliases);
 assertEquals("prefix", jclo4.getPrefix());
 assertEquals(aliases, jclo4.getAliases());
 }

 @Test
 public void testGetObject() {
 Field testField = null;
 try {
 testField = testObject.getClass().getDeclaredField("testField");
 testField.setAccessible(true);
 } catch (NoSuchFieldException e) {
 e.printStackTrace();
 }
 Object testValue = new Integer(1);
 jclo.setObject(testField, testValue);
 Object retrievedValue = jclo.getObject(testField);
 assertEquals(testValue, retrievedValue);
 }

 @Test(expected = IllegalAccessException.class)
 public void testGetObjectWithIllegalAccess() {
 Field testField = null;
 try {
 testField = testObject.getClass().getDeclaredField("privateField");
 testField.setAccessible(true);
 } catch (NoSuchFieldException e) {
 e.printStackTrace();
 }
 jclo.getObject(testField);
 }

 @Test
 public void testGetField() {
 Field testField = null;
 testField = testObject.getClass().getDeclaredField("testField");
 testField.setAccessible(true);
 String key = testField.getName();
 Field retrievedField = jclo.getField(key);
 assertEquals(testField, retrievedField);
 }

 @Test
 public void testGetFieldWithNullKey() {
 Field retrievedField = jclo.getField(null);
 assertNull(retrievedField);
 }

 @Test
 public void testGetValue() {
 Field testField = null;
 try {
 testField = testObject.getClass().getDeclaredField("testField");
 testField.setAccessible(true);
 jclo.setObject(testField, new Integer(1));
 String key = testField.getName();
 Object retrievedValue = jclo.getValue(key);
 assertEquals(1, retrievedValue);
 } catch (NoSuchFieldException e) {
 e.printStackTrace();
 } catch (IllegalAccessException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testValueWithNullKey() {
 String key = null;
 Object retrievedValue = jclo.getValue(key);
 assertNull(retrievedValue);
 }

 @Test
 public void testSetObject() {
 Field testField = null;
 try {
 testField = testObject.getClass().getDeclaredField("testField");
 testField.setAccessible(true);
 } catch (NoSuchFieldException e) {
 e.printStackTrace();
 }
 Object testValue = new Integer(2);
 jclo.setObject(testField, testValue);
 Object retrievedValue = jclo.getObject(testField);
 assertEquals(testValue, retrievedValue);
 }

 @Test
 public void testToString() {
 assertEquals("", jclo.toString());
 Field testField = null;
 try {
 testField = testObject.getClass().getDeclaredField("testField");
 testField.setAccessible(true);
 jclo.setObject(testField, new Integer(1));
 } catch (NoSuchFieldException e) {
 e.printStackTrace();
 } catch (IllegalAccessException e) {
 e.printStackTrace();
 }
 jclo.setPrefix("prefix_");
 String expectedOutput = "prefix_testField = 1\n";
 String actualOutput = jclo.toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testUsage() {
 String expectedOutput = "--testField int:\n";
 String actualOutput = jclo.usage();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testParse() {
 String[] args = {"--testField", "1"};
 jclo.parse(args);
 int testIntValue = (int) jclo.getValue("testField");
 assertEquals(1, testIntValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidOption() {
 String[] args = {"--invalidOption"};
 jclo.parse(args);
 }

 @Test
 public void testParseWithPrefix() {
 String[] args = {"--prefix_testField", "1"};
 jclo.parse(args);
 int testIntValue = (int) jclo.getValue("testField");
 assertEquals(1, testIntValue);
 }

 @Test
 public void testParseWithArray() {
 String[] args = {"--testArray", "1", "2", "3"};
 jclo.parse(args);
 int[] testIntArrayValue = (int[]) jclo.getObject(jclo.getField("testArray"));
 assertArrayEquals(new int[]{1, 2, 3}, testIntArrayValue);
 }

 @Test
 public void testParseWithPrefixAndArray() {
 String[] args = {"--prefix_testArray", "1", "2", "3"};
 jclo.parse(args);
 int[] testIntArrayValue = (int[]) jclo.getObject(jclo.getField("testArray"));
 assertArrayEquals(new int[]{1, 2, 3}, testIntArrayValue);
 }

 @Test
 public void testParseWithBoolean() {
 String[] args = {"--testBoolean", "true"};
 jclo.parse(args);
 boolean testBooleanValue = (boolean) jclo.getValue("testBoolean");
 assertTrue(testBooleanValue);
 }

 @Test
 public void testParseWithBooleanAndAliases() {
 String[] args = {"--aliasForTestBoolean", "true"};
 JCLO jcloWithAliases = new JCLO(testObject, new String[][]{{"aliasForTestBoolean", "testBoolean"}});
 jcloWithAliases.parse(args);
 boolean testBooleanValue = (boolean) jcloWithAliases.getValue("testBoolean");
 assertTrue(testBooleanValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithMissingValue() {
 String[] args = {"--testField"};
 jclo.parse(args);
 }
}