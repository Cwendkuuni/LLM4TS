package edu.mscd.cs.jclo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class JCLOTest {

 private Object testObject;
 private JCLO jclo;

 @Before
 public void setUp() throws InstantiationException, IllegalAccessException {
 testObject = new TestClass();
 jclo = new JCLO(testObject);
 }

 @After
 public void tearDown() {
 jclo = null;
 testObject = null;
 }

 @Test
 public void testConstructor_Object() {
 JCLO jclo1 = new JCLO(testObject);
 assertEquals(1, jclo1.getFields().length);
 }

 @Test
 public void testConstructor_Object_Aliases() {
 String[][] aliases = new String[1][2];
 aliases[0][0] = "alias1";
 aliases[0][1] = "alias2";
 JCLO jclo2 = new JCLO(testObject, aliases);
 assertEquals(1, jclo2.getFields().length);
 assertTrue(Arrays.deepEquals(jclo2.getAliases(), aliases));
 }

 @Test
 public void testConstructor_Prefix_Object() {
 String prefix = "prefix_";
 JCLO jclo3 = new JCLO(prefix, testObject);
 assertEquals(prefix, jclo3.getPrefix());
 }

 @Test
 public void testConstructor_Prefix_Object_Aliases() {
 String prefix = "prefix_";
 String[][] aliases = new String[1][2];
 aliases[0][0] = "alias1";
 aliases[0][1] = "alias2";
 JCLO jclo4 = new JCLO(prefix, testObject, aliases);
 assertEquals(prefix, jclo4.getPrefix());
 assertTrue(Arrays.deepEquals(jclo4.getAliases(), aliases));
 }

 @Test
 public void testGetField() {
 String key = "field1";
 Field field = testObject.getClass().getDeclaredField(key);
 Field jcloField = jclo.getField(key);
 assertEquals(field, jcloField);
 }

 @Test(expected = NullPointerException.class)
 public void testGetField_NullKey() {
 jclo.getValue(null);
 }

 @Test
 public void testGetValue() {
 String key = "field1";
 Object value = new Object();
 Field field = testObject.getClass().getDeclaredField(key);
 field.set(testObject, value);
 Object jcloValue = jclo.getValue(key);
 assertEquals(value, jcloValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetValue_NullObject() {
 jclo.getValue(null);
 }

 @Test
 public void testGetValue_NullKey_NullObject() {
 jclo.getValue(null, "key");
 }

 @Test
 public void testSetObject() throws IllegalAccessException {
 String key = "field1";
 Object newValue = new Object();
 Field field = testObject.getClass().getDeclaredField(key);
 jclo.setObject(field, newValue);
 assertEquals(newValue, field.get(testObject));
 }

 @Test(expected = IllegalAccessException.class)
 public void testSetObject_IllegalAccess() {
 Field field = testObject.getClass().getDeclaredField("privateField");
 jclo.setObject(field, "value");
 }

 @Test
 public void testGetArrayType() {
 Class intArrayType = int[].class;
 assertEquals("int[]", jclo.getArrayType(intArrayType));
 }

 @Test
 public void testToString() {
 StringBuilder expected = new StringBuilder();
 expected.append("int[]: field1 = [1, 2, 3]\n");
 expected.append("boolean: field2 = true\n");
 String jcloToString = jclo.toString();
 assertEquals(expected.toString(), jcloToString);
 }

 @Test
 public void testUsage() {
 String expected = "--int field1 = INT_VALUE\n" +
 "--boolean field2 = BOOLEAN_VALUE\n";
 String jcloUsage = jclo.usage();
 assertEquals(expected, jcloUsage);
 }

 @Test
 public void testParse_Boolean() {
 String[] args = {"--boolean", "true"};
 jclo.parse(args);
 assertTrue(jclo.getBoolean("field2"));
 }

 @Test
 public void testParse_Byte() {
 String[] args = {"--byte", "127"};
 jclo.parse(args);
 assertEquals((byte)127, jclo.getByte("field1"));
 }

 @Test
 public void testParse_Char() {
 String[] args = {"--char", "A"};
 jclo.parse(args);
 assertEquals((char)'A', jclo.getChar("field1"));
 }

 @Test
 public void testParse_Short() {
 String[] args = {"--short", "32767"};
 jclo.parse(args);
 assertEquals((short)32767, jclo.getShort("field1"));
 }

 @Test
 public void testParse_Int() {
 String[] args = {"--int", "2147483647"};
 jclo.parse(args);
 assertEquals(2147483647, jclo.getInt("field1"));
 }

 @Test
 public void testParse_Float() {
 String[] args = {"--float", "3.14159265"};
 jclo.parse(args);
 assertEquals(3.14159265f, jclo.getFloat("field1"), 0.0001f);
 }

 @Test
 public void testParse_Double() {
 String[] args = {"--double", "1.79769313486231E308"};
 jclo.parse(args);
 assertEquals(1.79769313486231E308d, jclo.getDouble("field1"), 0.0);
 }

 @Test
 public void testParse_Long() {
 String[] args = {"--long", "9223372036854775807"};
 jclo.parse(args);
 assertEquals(9223372036854775807L, jclo.getLong("field1"));
 }

 @Test
 public void testParse_String() {
 String[] args = {"--string", "test"};
 jclo.parse(args);
 assertEquals("test", jclo.getString("field1"));
 }

 @Test
 public void testParse_Array_Byte() {
 byte[] byteArray = {1, 2, 3};
 String[] args = {"--byte[]", "1,2,3"};
 jclo.parse(args);
 assertArrayEquals(byteArray, (byte[])jclo.getBytes("field1"));
 }

 @Test
 public void testParse_Array_Char() {
 char[] charArray = {'A', 'B', 'C'};
 String[] args = {"--char[]", "'A','B','C'"};
 jclo.parse(args);
 assertArrayEquals(charArray, (char[])jclo.getChars("field1"));
 }

 @Test
 public void testParse_Array_Short() {
 short[] shortArray = {1, 2, 3};
 String[] args = {"--short[]", "1,2,3"};
 jclo.parse(args);
 assertArrayEquals(shortArray, (short[])jclo.getShorts("field1"));
 }

 @Test
 public void testParse_Array_Int() {
 int[] intArray = {1, 2, 3};
 String[] args = {"--int[]", "1,2,3"};
 jclo.parse(args);
 assertArrayEquals(intArray, (int[])jclo.getInts("field1"));
 }

 @Test
 public void testParse_Array_Float() {
 float[] floatArray = {1.0f, 2.0f, 3.0f};
 String[] args = {"--float[]", "1.0,2.0,3.0"};
 jclo.parse(args);
 assertArrayEquals(floatArray, (float[])jclo.getFloats("field1"), 0.001f);
 }

 @Test
 public void testParse_Array_Double() {
 double[] doubleArray = {1.0, 2.0, 3.0};
 String[] args = {"--double[]", "1.0,2.0,3.0"};
 jclo.parse(args);
 assertArrayEquals(doubleArray, (double[])jclo.getDoubles("field1"), 0.0);
 }

 @Test
 public void testParse_Array_Long() {
 long[] longArray = {1, 2, 3};
 String[] args = {"--long[]", "1,2,3"};
 jclo.parse(args);
 assertArrayEquals(longArray, (long[])jclo.getLongs("field1"));
 }

 @Test
 public void testParse_Array_String() {
 String[] stringArray = {"test1", "test2", "test3"};
 String[] args = {"--string[]", "test1,test2,test3"};
 jclo.parse(args);
 assertEquals(Arrays.asList(stringArray), Arrays.asList((String[])jclo.getStrings("field1")));
 }

 @Test
 public void testParse_EqualsValue() {
 String[] args = {"--int", "field1=5"};
 jclo.parse(args);
 assertEquals(5, jclo.getInt("field1"));
 }

 @Test
 public void testParse_EqualsValue_Multiple() {
 String[] args = {"--int", "field1=5,field2=10"};
 jclo.parse(args);
 assertEquals(5, jclo.getInt("field1"));
 assertEquals(10, jclo.getInt("field2"));
 }

 @Test
 public void testParse_MultipleFields() {
 String[] args = {"--int", "field1=5", "--int", "field2=10", "--int", "field3=15"};
 jclo.parse(args);
 assertEquals(5, jclo.getInt("field1"));
 assertEquals(10, jclo.getInt("field2"));
 assertEquals(15, jclo.getInt("field3"));
 }

 @Test
 public void testParse_Boolean_Prefix() {
 String prefix = "prefix_";
 String[] args = {"--boolean", "prefix_field1=true"};
 JCLO jcloPrefix = new JCLO(prefix, testObject);
 jcloPrefix.parse(args);
 assertTrue(jcloPrefix.getBoolean("field1"));
 }

 @Test
 public void testParse_MultipleFields_Prefix() {
 String prefix = "prefix_";
 String[] args = {"--boolean", "prefix_field1=true", "--int", "prefix_field2=5"};
 JCLO jcloPrefix = new JCLO(prefix, testObject);
 jcloPrefix.parse(args);
 assertTrue(jcloPrefix.getBoolean("field1"));
 assertEquals(5, jcloPrefix.getInt("field2"));
 }

 @Test
 public void testParse_Aliases() {
 String[][] aliases = new String[1][2];
 aliases[0][0] = "alias1";
 aliases[0][1] = "alias2";
 JCLO jcloAliases = new JCLO(null, testObject, aliases);
 String[] args = {"--alias1", "5"};
 jcloAliases.parse(args);
 assertEquals(5, jcloAliases.getInt("field1"));
 }

 @Test
 public void testParse_Aliases_Multiple() {
 String[][] aliases = new String[2][2];
 aliases[0][0] = "alias1";
 aliases[0][1] = "alias2";
 aliases[1][0] = "alias3";
 aliases[1][1] = "alias4";
 JCLO jcloAliases = new JCLO(null, testObject, aliases);
 String[] args = {"--alias1", "5", "--alias3", "10"};
 jcloAliases.parse(args);
 assertEquals(5, jcloAliases.getInt("field1"));
 assertEquals(10, jcloAliases.getInt("field3"));
 }
}

 class TestClass {
 private int field1;
 private byte field2;
 private char field3;
 private short field4;
 private int field5;
 private float field6;
 private double field7;
 private long field8;
 private String field9;
 private int[] field10;
 private char[] field11;
 private short[] field12;
 private int[] field13;
 private float[] field14;
 private double[] field15;
 private long[] field16;
 private String[] field17;
 }