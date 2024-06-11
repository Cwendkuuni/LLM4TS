package edu.mscd.cs.jclo;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JCLOTest {

 private JCLO jclo;

 @Before
 public void setUp() {
 jclo = new JCLO(new Object());
 }

 @Test
 public void testGetValueNullKey() {
 assertNull(jclo.getValue(null));
 }

 @Test
 public void testGetValueEmptyPrefix() {
 jclo = new JCLO("prefix", new Object(), null);
 assertNull(jclo.getValue("fieldName"));
 }

 @Test
 public void testGetValueEmptyPrefixValidKey() {
 jclo = new JCLO("prefix", new Object(), null);
 jclo.setObject(jclo.getField("fieldName"), new Object());
 assertEquals(new Object(), jclo.getValue("fieldName"));
 }

 @Test
 public void testGetValuePrefixValidKey() {
 jclo = new JCLO("prefix.", new Object(), null);
 jclo.setObject(jclo.getField("fieldName"), new Object());
 assertEquals("prefix.fieldName", jclo.getValue("fieldName"));
 }

 @Test
 public void testGetValueFieldDoesntExist() {
 jclo = new JCLO("prefix", new Object(), null);
 assertNull(jclo.getValue("nonExistentField"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetValueInvalidKey() {
 jclo = new JCLO("prefix", new Object(), null);
 jclo.setObject(jclo.getField("fieldName"), new Object());
 assertNull(jclo.getValue("invalidKey"));
 }

 @Test
 public void testToStringEmptyObject() {
 assertEquals("", jclo.toString());
 }

 @Test
 public void testToStringSimpleObject() {
 jclo = new JCLO(new int[3][], null);
 String expected = "int[][]: field0 = [0, 0, 0]\nint[][]: field1 = [0, 0, 0]\n";
 assertEquals(expected, jclo.toString());
 }

 @Test
 public void testToStringComplexObject() {
 jclo = new JCLO(new Object() {
 private int x = 5;
 private String y = "Hello World";
 private int[] z = new int[3];
 }, null);
 String expected = "int: x = 5\njava.lang.String: y = Hello World\nint[]: z = [0, 0, 0]\n";
 assertEquals(expected, jclo.toString());
 }

 @Test
 public void testUsageEmptyObject() {
 assertEquals("", jclo.usage());
 }

 @Test
 public void testUsageSimpleObject() {
 jclo = new JCLO(new Object(), null);
 final String[] expected = {"int: x = ", "int: y = ", "int: z = "};
 Arrays.sort(expected);
 assertEquals(Arrays.toString(expected), jclo.usage());
 }

 @Test
 public void testUsageComplexObject() {
 jclo = new JCLO("prefix", new Object() {
 private int x = 5;
 private String y = "Hello World";
 private int[] z = new int[3];
 }, null);
 String[] expected = {"--x = ", "--y = Hello World", "--z = "};
 Arrays.sort(expected);
 assertEquals(Arrays.toString(expected), jclo.usage());
 }

 @Test
 public void testParseValidArguments() {
 String[] args = {"--x=1", "--y=Hello World", "--z=1,2,3"};
 jclo = new JCLO(new Object(), null);
 jclo.parse(args);
 assertEquals(1, jclo.getInt("x"));
 assertEquals("Hello World", jclo.getString("y"));
 assertArrayEquals(new int[]{1, 2, 3}, (int[])jclo.getObject(jclo.getField("z")));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidKey() {
 String[] args = {"--invalidKey=1", "--x=1"};
 jclo = new JCLO(new Object(), null);
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidValue() {
 String[] args = {"--x=invalidValue", "--x=1"};
 jclo = new JCLO(new Object(), null);
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingValue() {
 String[] args = {"--x="};
 jclo = new JCLO(new Object(), null);
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingEquals() {
 String[] args = {"--x=invalidValue"};
 jclo = new JCLO(new Object(), null);
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidTypeBoolean() {
 String[] args = {"--invalidBoolean=true"};
 jclo = new JCLO(new Object(), null);
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidTypeByte() {
 String[] args = {"--invalidByte=abc"};
 jclo = new JCLO(new Object(), null);
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidTypeShort() {
 String[] args = {"--invalidShort=abc"};
 jclo = new JCLO(new Object(), null);
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidTypeInt() {
 String[] args = {"--invalidInt=abc"};
 jclo = new JCLO(new Object(), null);
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidTypeFloat() {
 String[] args = {"--invalidFloat=abc"};
 jclo = new JCLO(new Object(), null);
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidTypeDouble() {
 String[] args = {"--invalidDouble=abc"};
 jclo = new JCLO(new Object(), null);
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidTypeLong() {
 String[] args = {"--invalidLong=abc"};
 jclo = new JCLO(new Object(), null);
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidTypeString() {
 String[] args = {"--invalidString={}"};
 jclo = new JCLO(new Object(), null);
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidTypeChar() {
 String[] args = {"--invalidChar=abc"};
 jclo = new JCLO(new Object(), null);
 jclo.parse(args);
 }

 @Test
 public void testParseArray() {
 String[] args = {"--array=1,2,3"};
 jclo = new JCLO(new Object(), null);
 jclo.parse(args);
 int[] expected = {1, 2, 3};
 assertArrayEquals(expected, (int[])jclo.getObject(jclo.getField("array")));
 }
}