package edu.mscd.cs.jclo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

public class JCLOTest {

 private JCLO jclo;

 @Test
 public void testConstructorWithObject() {
 jclo = new JCLO(new Object());
 assertEquals(1, jclo.getFields().length);
 }

 @Test
 public void testConstructorWithObjectAndAliases() {
 String[][] aliases = new String[][] {{"field1", "alias1"}, {"field2", "alias2"}};
 jclo = new JCLO(new Object(), aliases);
 assertEquals(2, jclo.getFields().length);
 Field field1 = jclo.getField("field1");
 assertEquals("alias1", field1.getName());
 }

 @Test
 public void testConstructorWithPrefixAndObject() {
 String prefix = "prefix";
 jclo = new JCLO(prefix, new Object());
 assertEquals(prefix, jclo.getPrefix());
 }

 @Test
 public void testConstructorWithPrefixAndObjectAndAliases() {
 String prefix = "prefix";
 String[][] aliases = new String[][] {{"field1", "alias1"}, {"field2", "alias2"}};
 jclo = new JCLO(prefix, new Object(), aliases);
 assertEquals(prefix, jclo.getPrefix());
 Field field1 = jclo.getField("field1");
 assertEquals("alias1", field1.getName());
 }

 @Test(expected = IllegalAccessException.class)
 public void testGetObjectWithInaccessibleField() throws Exception {
 jclo = new JCLO(new Object());
 Field inaccessibleField = jclo.getClass().getDeclaredField("inaccessibleField");
 inaccessibleField.setAccessible(true);
 assertNull(jclo.getObject(inaccessibleField));
 }

 @Test(expected = NullPointerException.class)
 public void testGetObjectWithNullField() {
 jclo = new JCLO(new Object());
 assertNull(jclo.getObject(null));
 }

 @Test
 public void testGetValueWithExistingField() {
 jclo = new JCLO("prefix", new Object(), new String[][] {{"field1", "alias1"}});
 jclo.setObject(jclo.getField("field1"), "value1");
 assertEquals("value1", jclo.getValue("field1"));
 assertEquals("alias1", jclo.getValue("alias1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetValueWithNonExistentField() {
 jclo = new JCLO("prefix", new Object(), new String[][] {{"field1", "alias1"}});
 assertNull(jclo.getValue("nonExistentField"));
 }

 @Test
 public void testSetObjectWithExistingField() {
 jclo = new JCLO("prefix", new Object(), new String[][] {{"field1", "alias1"}});
 jclo.setObject(jclo.getField("field1"), "value1");
 assertEquals("value1", jclo.getValue("field1"));
 }

 @Test(expected = NullPointerException.class)
 public void testSetObjectWithNullField() {
 jclo = new JCLO("prefix", new Object(), new String[][] {{"field1", "alias1"}});
 jclo.setObject(null, "value1");
 }

 @Test
 public void testToStringWithNoFields() {
 jclo = new JCLO(new Object());
 assertEquals("", jclo.toString());
 }

 @Test
 public void testToStringWithOneField() {
 jclo = new JCLO("prefix", new Object(), new String[][] {{"field1", "alias1"}});
 jclo.setObject(jclo.getField("field1"), new int[] {1, 2, 3});
 String expected = prefix + "\nint[]: field1 = [1, 2, 3]\n";
 assertEquals(expected, jclo.toString());
 }

 @Test
 public void testToStringWithMultipleFields() {
 jclo = new JCLO("prefix", new Object(), new String[][] {{"field1", "alias1"}, {"field2", "alias2"}});
 jclo.setObject(jclo.getField("field1"), new int[] {1, 2, 3});
 jclo.setObject(jclo.getField("field2"), new String[] {"hello", "world"});
 String expected = prefix + "\nint[]: field1 = [1, 2, 3]\nString[]: field2 = [hello, world]\n";
 assertEquals(expected, jclo.toString());
 }

 @Test
 public void testGetUsageTypeWithBoolean() {
 assertEquals("[=boolean]", jclo.getUsageType(boolean.class));
 }

 @Test
 public void testGetUsageTypeWithArrayOfBytes() {
 assertEquals("=byte[]", jclo.getUsageType(byte[].class));
 }

 @Test
 public void testUsageWithNoAliases() {
 jclo = new JCLO("prefix", new Object());
 String expected = "int: field1 = \nchar: field2 = \nshort: field3 = \nfloat: field4 = \ndouble: field5 = \nlong: field6 = \njava.lang.String: field7 = \n";
 assertEquals(expected, jclo.usage());
 }

 @Test
 public void testUsageWithAliases() {
 jclo = new JCLO("prefix", new Object(), new String[][] {{"field1", "alias1"}, {"field2", "alias2"}});
 String expected = "int: field1 = \nString: alias1 = \nchar: field2 = \nString: alias2 = \n";
 assertEquals(expected, jclo.usage());
 }

 @Test
 public void testParseWithNoArguments() {
 jclo = new JCLO("prefix", new Object());
 jclo.parse(new String[0]);
 assertArrayEquals(new String[0], (String[])jclo.getObject("additional"));
 }

 @Test
 public void testParseWithBooleanArgument() {
 jclo = new JCLO("prefix", new Object());
 jclo.parse(new String[] {"--boolean", "true"});
 assertTrue((boolean)jclo.getObject("booleanField"));
 }

 @Test
 public void testParseWithByteArrayArgument() {
 jclo = new JCLO("prefix", new Object());
 jclo.parse(new String[] {"--byte-array", "1,2,3"});
 assertArrayEquals(new byte[] {1, 2, 3}, (byte[])jclo.getObject("byteArrayField"));
 }

 @Test
 public void testParseWithMultipleArguments() {
 jclo = new JCLO("prefix", new Object());
 jclo.parse(new String[] {"--boolean", "true", "--byte-array", "1,2,3", "--string", "hello"});
 assertTrue((boolean)jclo.getObject("booleanField"));
 assertArrayEquals(new byte[] {1, 2, 3}, (byte[])jclo.getObject("byteArrayField"));
 assertEquals("hello", jclo.getObject("stringField"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidBooleanArgument() {
 jclo = new JCLO("prefix", new Object());
 jclo.parse(new String[] {"--boolean=false"});
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testParseWithInvalidByteArrayArgument() {
 jclo = new JCLO("prefix", new Object());
 jclo.parse(new String[] {"--byte-array=1,2,3,4"});
 }
}