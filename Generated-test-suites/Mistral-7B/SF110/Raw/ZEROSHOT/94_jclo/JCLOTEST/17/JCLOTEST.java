package edu.mscd.cs.jclo;

import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;

public class JCLOTest {

 private Object testObject;
 private JCLO jclo;

 @Before
 public void setup() {
 testObject = new TestClass();
 jclo = new JCLO(testObject);
 }

 @Test
 public void testConstructorWithObject() {
 JCLO jclo = new JCLO(testObject);
 assertEquals(testObject.getClass(), jclo.getClass());
 assertEquals(testObject, jclo.object);
 assertNull(jclo.prefix);
 assertNull(jclo.aliases);
 assertNotNull(jclo.fields);
 }

 @Test
 public void testConstructorWithObjectAndAliases() {
 String[][] aliases = {{"name", "n"}, {"age", "a"}};
 JCLO jclo = new JCLO(testObject, aliases);
 assertEquals(testObject.getClass(), jclo.getClass());
 assertEquals(testObject, jclo.object);
 assertEquals(aliases, jclo.aliases);
 assertNull(jclo.prefix);
 assertNotNull(jclo.fields);
 }

 @Test
 public void testConstructorWithPrefixAndObject() {
 String prefix = "prefix_";
 JCLO jclo = new JCLO(prefix, testObject);
 assertEquals(prefix, jclo.prefix);
 assertEquals(testObject, jclo.object);
 assertNull(jclo.aliases);
 assertNotNull(jclo.fields);
 }

 @Test
 public void testConstructorWithPrefixAndObjectAndAliases() {
 String prefix = "prefix_";
 String[][] aliases = {{"name", "n"}, {"age", "a"}};
 JCLO jclo = new JCLO(prefix, testObject, aliases);
 assertEquals(prefix, jclo.prefix);
 assertEquals(testObject, jclo.object);
 assertEquals(aliases, jclo.aliases);
 assertNotNull(jclo.fields);
 }

 @Test
 public void testGetObject() {
 Field field = getField("name");
 Object name = testObject.getClass().getDeclaredField("name").get(testObject);
 Object jcloName = jclo.getObject(field);
 assertEquals(name, jcloName);
 }

 @Test
 public void testGetField() {
 String key = "name";
 Field jcloField = jclo.getField(key);
 Field testField = testObject.getClass().getDeclaredField(key);
 assertEquals(testField, jcloField);
 }

 @Test
 public void testgetValue() {
 String key = "name";
 Object expected = testObject.getName();
 Object result = jclo.getValue(key);
 assertEquals(expected, result);
 }

 @Test
 public void testValueForNonExistentField() {
 String key = "nonExistentField";
 assertNull(jclo.getValue(key));
 }

 @Test
 public void testSetObject() {
 Field field = getField("name");
 String value = "testValue";
 jclo.setObject(field, value);
 String result = (String) jclo.getObject(field);
 assertEquals(value, result);
 }

 @Test
 public void testGetArrayType() {
 Class type = int[].class;
 String expected = "int[]";
 String result = jclo.getArrayType(type);
 assertEquals(expected, result);
 }

 @Test
 public void testToString() {
 String expected = "int: name = testValue\nboolean: age = false\n";
 String result = jclo.toString();
 assertEquals(expected, result);
 }

 @Test
 public void testUsage() {
 String expected = "--name name\n--age age\n";
 String result = jclo.usage();
 assertEquals(expected, result);
 }

 @Test
 public void testParseAdditional() {
 String[] args = {"--additional", "test1", "test2"};
 jclo.parse(args);
 String[] expected = {"test1", "test2"};
 Object result = (String[]) jclo.getObject(jclo.getField("additional"));
 assertArrayEquals(expected, (String[]) result);
 }

 @Test
 public void testAddToArray() {
 Field field = getField("numbers");
 Object[] initialArray = {};
 Object newElement = 1;
 jclo.setObject(field, initialArray);
 Object expandedArray = jclo.addToArray(field, newElement);
 Object[] expected = {newElement};
 Object[] result = (Object[]) jclo.getObject(field);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testParse() {
 String[] args = {"--name", "testName", "--age", "25", "--numbers", "1", "2"};
 jclo.parse(args);
 String name = (String) jclo.getObject(jclo.getField("name"));
 int age = (int) jclo.getObject(jclo.getField("age"));
 Object[] numbers = (Object[]) jclo.getObject(jclo.getField("numbers"));
 assertEquals("testName", name);
 assertEquals(25, age);
 assertArrayEquals(new Object[]{1, 2}, numbers);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidOption() {
 String[] args = {"--invalidOption"};
 jclo.parse(args);
 }

 @Test
 public void testParseWithBooleanOption() {
 String[] args = {"--boolean", "true"};
 jclo.parse(args);
 assertTrue((Boolean) jclo.getObject(jclo.getField("boolean")));
 }

 @Test
 public void testParseWithBooleanArrayOption() {
 String[] args = {"--booleans", "true", "false"};
 jclo.parse(args);
 Object[] booleans = (Object[]) jclo.getObject(jclo.getField("booleans"));
 assertTrue((Boolean) booleans[0]);
 assertTrue((Boolean) booleans[1]);
 }

 @Test
 public void testParseWithStringOption() {
 String[] args = {"--string", "testString"};
 jclo.parse(args);
 assertEquals("testString", jclo.getObject(jclo.getField("string")));
 }

 @Test
 public void testParseWithStringArrayOption() {
 String[] args = {"--strings", "testString1", "testString2"};
 jclo.parse(args);
 String[] strings = (String[]) jclo.getObject(jclo.getField("strings"));
 assertEquals("testString1", strings[0]);
 assertEquals("testString2", strings[1]);
 }

 @Test
 public void testParseWithIntOption() {
 String[] args = {"--int", "5"};
 jclo.parse(args);
 assertEquals(5, (int) jclo.getObject(jclo.getField("int")));
 }

 @Test
 public void testParseWithIntArrayOption() {
 String[] args = {"--ints", "1", "2"};
 jclo.parse(args);
 int[] ints = (int[]) jclo.getObject(jclo.getField("ints"));
 assertEquals(1, ints[0]);
 assertEquals(2, ints[1]);
 }

 @Test
 public void testParseWithFloatOption() {
 String[] args = {"--float", "3.14"};
 jclo.parse(args);
 assertEquals(3.14f, (Float) jclo.getObject(jclo.getField("float")));
 }

 @Test
 public void testParseWithFloatArrayOption() {
 String[] args = {"--floats", "1.1", "2.2"};
 jclo.parse(args);
 float[] floats = (float[]) jclo.getObject(jclo.getField("floats"));
 assertEquals(1.1f, floats[0]);
 assertEquals(2.2f, floats[1]);
 }

 @Test
 public void testParseWithDoubleOption() {
 String[] args = {"--double", "4.25"};
 jclo.parse(args);
 assertEquals(4.25, (Double) jclo.getObject(jclo.getField("double")));
 }

 @Test
 public void testParseWithDoubleArrayOption() {
 String[] args = {"--doubles", "1.1", "2.2"};
 jclo.parse(args);
 double[] doubles = (double[]) jclo.getObject(jclo.getField("doubles"));
 assertEquals(1.1, doubles[0]);
 assertEquals(2.2, doubles[1]);
 }

 @Test
 public void testParseWithLongOption() {
 String[] args = {"--long", "5L"};
 jclo.parse(args);
 assertEquals(5L, (Long) jclo.getObject(jclo.getField("long")));
 }

 @Test
 public void testParseWithLongArrayOption() {
 String[] args = {"--longs", "1", "2"};
 jclo.parse(args);
 long[] longs = (long[]) jclo.getObject(jclo.getField("longs"));
 assertEquals(1L, longs[0]);
 assertEquals(2L, longs[1]);
 }

 private Field getField(String key) {
 return jclo.getField(key);
 }
}