package edu.mscd.cs.jclo;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

public class JCLOTest {

 private JCLO jclo;

 @Before
 public void setup() {
 Object obj = new Object() {
 Field testField = new Field("testField", int.class);
 };
 jclo = new JCLO(obj);
 }

 @Test
 public void testConstructorWithObject() {
 JCLO jclo1 = new JCLO(new Object());
 assertEquals(0, jclo1.getFields().length);
 }

 @Test
 public void testConstructorWithObjectAndAliases() {
 String[][] aliases = new String[1][2];
 aliases[0][0] = "alias1";
 aliases[0][1] = "alias2";
 JCLO jclo2 = new JCLO(new Object(), aliases);
 assertEquals(0, jclo2.getFields().length);
 assertEquals(aliases, jclo2.getAliases());
 }

 @Test
 public void testConstructorWithPrefixAndObject() {
 String prefix = "test";
 JCLO jclo3 = new JCLO(prefix, new Object());
 assertEquals("", jclo3.getPrefix());
 }

 @Test
 public void testConstructorWithPrefixAndObjectAndAliases() {
 String prefix = "test";
 String[][] aliases = new String[1][2];
 aliases[0][0] = "alias1";
 aliases[0][1] = "alias2";
 JCLO jclo4 = new JCLO(prefix, new Object(), aliases);
 assertEquals(prefix, jclo4.getPrefix());
 assertEquals(aliases, jclo4.getAliases());
 }

 @Test
 public void testGetObject() {
 Field testField = jclo.getField("testField");
 assertEquals(0, testField.get(jclo.object).hashCode());
 Object testObject = new Integer(42);
 jclo.setObject(testField, testObject);
 assertEquals(42, testObject.hashCode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetFieldWithNullKey() {
 jclo.getValue(null);
 }

 @Test
 public void testgetValueWithValidKey() {
 Field testField = jclo.getField("testField");
 assertEquals(0, testField.get(jclo.object).hashCode());
 Object testObject = new Integer(42);
 jclo.setObject(testField, testObject);
 assertEquals(42, jclo.getValue("testField").hashCode());
 }

 @Test
 public void testGetFieldWithInvalidKey() {
 assertEquals(null, jclo.getField("invalidKey"));
 }

 @Test(expected = IllegalAccessException.class)
 public void testSetObjectWithInaccessibleField() {
 Field inaccessibleField = new Field("inaccessibleField", int.class) {
 @Override
 public boolean isAccessible() {
 return false;
 }
 };
 jclo.setObject(inaccessibleField, new Integer(42));
 }

 @Test
 public void testToString() {
 StringBuilder expected = new StringBuilder();
 expected.append("int[]: testField = [0]");
 assertEquals(expected.toString(), jclo.toString());
 }

 @Test
 public void testGetUsageTypeWithBoolean() {
 assertEquals("[=boolean]", jclo.getUsageType(boolean.class));
 }

 @Test
 public void testGetUsageTypeWithIntArray() {
 assertEquals(" int...", jclo.getUsageType(int[].class));
 }

 @Test
 public void testUsage() {
 String[][] aliases = new String[1][2];
 aliases[0][0] = "alias1";
 aliases[0][1] = "alias2";
 jclo.parse(new String[]{"--prefix=test", "--alias1=testField", "--testField=42"});
 String[] usage = jclo.usage().split("\n");
 assertEquals(3, usage.length);
 assertEquals("testField = int", usage[0]);
 assertEquals("testField = int...", usage[1]);
 assertEquals("alias1 = testField", usage[2]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullArg() {
 jclo.parse(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidOption() {
 jclo.parse(new String[]{"--invalid"});
 }

 @Test
 public void testParseWithBooleanOption() {
 jclo.parse(new String[]{"--double-dashes", "--prefix=test", "--testField=true"});
 assertEquals(true, jclo.getBoolean("testField"));
 }

 @Test
 public void testParseWithByteOption() {
 jclo.parse(new String[]{"--double-dashes", "--prefix=test", "--testField=127"});
 assertEquals((byte)127, jclo.getByte("testField"));
 }

 @Test(expected = NumberFormatException.class)
 public void testParseWithInvalidByteOption() {
 jclo.parse(new String[]{"--double-dashes", "--prefix=test", "--testField=257"});
 }

 @Test
 public void testParseWithCharOption() {
 jclo.parse(new String[]{"--double-dashes", "--prefix=test", "--testField=A"});
 assertEquals('A', jclo.getChar("testField"));
 }

 @Test(expected = NumberFormatException.class)
 public void testParseWithInvalidCharOption() {
 jclo.parse(new String[]{"--double-dashes", "--prefix=test", "--testField=Z9"});
 }

 @Test
 public void testParseWithShortOption() {
 jclo.parse(new String[]{"--double-dashes", "--prefix=test", "--testField=-32768"});
 assertEquals((short)-32768, jclo.getShort("testField"));
 }

 @Test(expected = NumberFormatException.class)
 public void testParseWithInvalidShortOption() {
 jclo.parse(new String[]{"--double-dashes", "--prefix=test", "--testField=32768"});
 }

 @Test
 public void testParseWithIntOption() {
 jclo.parse(new String[]{"--double-dashes", "--prefix=test", "--testField=42"});
 assertEquals(42, jclo.getInt("testField"));
 }

 @Test
 public void testParseWithFloatOption() {
 jclo.parse(new String[]{"--double-dashes", "--prefix=test", "--testField=3.1415"});
 assertEquals(3.1415f, jclo.getFloat("testField"), 0.0001f);
 }

 @Test
 public void testParseWithDoubleOption() {
 jclo.parse(new String[]{"--double-dashes", "--prefix=test", "--testField=3.14159265358979323846"});
 assertEquals(3.14159265358979323846d, jclo.getDouble("testField"), 0.000000000000000001d);
 }

 @Test
 public void testParseWithLongOption() {
 jclo.parse(new String[]{"--double-dashes", "--prefix=test", "--testField=4294967295"});
 assertEquals(4294967295L, jclo.getLong("testField"));
 }

 @Test
 public void testParseWithStringOption() {
 jclo.parse(new String[]{"--double-dashes", "--prefix=test", "--testField=Hello World"});
 assertEquals("Hello World", jclo.getString("testField"));
 }

 @Test
 public void testParseWithArrayOption() {
 String[] testArray = new String[]{"A", "B", "C"};
 jclo.parse(new String[]{"--double-dashes", "--prefix=test", "--testField=--array:String:testArray"});
 assertEquals(testArray, jclo.getStrings("testField"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidArrayOption() {
 jclo.parse(new String[]{"--double-dashes", "--prefix=test", "--testField=--array:Integer:testArray"});
 }

 @Test
 public void testParseWithAliases() {
 String[] testAliases = new String[]{"alias1=testField1", "alias2=testField2"};
 jclo.parse(new String[]{"--double-dashes", "--prefix=test", "--testField=--alias:testAliases"});
 Field[] fields = jclo.getFields();
 assertEquals(2, fields.length);
 assertEquals("testField1", fields[0].getName());
 assertEquals("testField2", fields[1].getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidAliases() {
 jclo.parse(new String[]{"--double-dashes", "--prefix=test", "--testField=--alias:testAliases:invalid"});
 }
}