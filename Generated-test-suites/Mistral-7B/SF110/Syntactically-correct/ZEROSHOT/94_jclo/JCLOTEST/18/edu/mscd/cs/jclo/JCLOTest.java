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

import org.junit.Before;
import org.junit.Test;

public class JCLOTest {

 private Object object;
 private JCLO jclo;

 @Before
 public void setUp() {
 object = new Object() {
 private String name = "John Doe";
 private int age = 30;
 private boolean isMarried = false;
 private String[] hobbies = { "football", "reading" };
 };
 jclo = new JCLO(object);
 }

 @Test
 public void testConstructorWithObject() {
 JCLO jclo1 = new JCLO(object);
 assertEquals(object, jclo1.getObject());
 }

 @Test
 public void testConstructorWithAliases() {
 String[][] aliases = { { "name", "Name" }, { "age", "Age" }, { "isMarried", "IsMarried" } };
 JCLO jclo1 = new JCLO(object, aliases);
 assertEquals(object, jclo1.getObject());
 Field nameField = jclo1.getField("Name");
 assertEquals("Name", nameField.getName());
 }

 @Test
 public void testConstructorWithPrefix() {
 String prefix = "prefix_";
 JCLO jclo1 = new JCLO(prefix, object);
 assertEquals(object, jclo1.getObject());
 Field nameField = jclo1.getField("prefix_name");
 assertEquals("prefix_name", nameField.getName());
 }

 @Test
 public void testConstructorWithPrefixAndAliases() {
 String prefix = "prefix_";
 String[][] aliases = { { "name", "Name" }, { "age", "Age" }, { "isMarried", "IsMarried" } };
 JCLO jclo1 = new JCLO(prefix, object, aliases);
 assertEquals(object, jclo1.getObject());
 Field nameField = jclo1.getField("prefix_Name");
 assertEquals("prefix_Name", nameField.getName());
 }

 @Test
 public void testGetValue() {
 assertEquals("John Doe", jclo.getValue("name"));
 assertEquals(30, jclo.getValue("age").intValue());
 assertFalse(jclo.getValue("isMarried"));
 assertNull(jclo.getValue("hobbies"));
 }

 @Test
 public void testGetValueWithAliases() {
 assertEquals("John Doe", jclo.getValue("Name"));
 assertEquals(30, jclo.getValue("Age").intValue());
 assertFalse(jclo.getValue("IsMarried"));
 assertNull(jclo.getValue("hobbies"));
 }

 @Test
 public void testGetValueWithPrefix() {
 String prefix = "prefix_";
 JCLO jclo1 = new JCLO(prefix, object);
 assertEquals("John Doe", jclo1.getValue("prefix_name"));
 assertEquals(30, jclo1.getValue("prefix_age").intValue());
 assertFalse(jclo1.getValue("prefix_isMarried"));
 assertNull(jclo1.getValue("prefix_hobbies"));
 }

 @Test
 public void testGetValueWithPrefixAndAliases() {
 String prefix = "prefix_";
 String[][] aliases = { { "name", "Name" }, { "age", "Age" }, { "isMarried", "IsMarried" } };
 JCLO jclo1 = new JCLO(prefix, object, aliases);
 assertEquals("John Doe", jclo1.getValue("prefix_Name"));
 assertEquals(30, jclo1.getValue("prefix_Age").intValue());
 assertFalse(jclo1.getValue("prefix_IsMarried"));
 assertNull(jclo1.getValue("prefix_hobbies"));
 }

 @Test
 public void testSetValue() {
 String newName = "Jane Doe";
 jclo.setObject(jclo.getField("name"), newName);
 assertEquals(newName, jclo.getValue("name"));
 }

 @Test
 public void testToString() {
 String expected = "byte[]: age = [30]\nchar: isMarried = false\n"
 + "java.lang.String: name = John Doe\nshort: []\n";
 assertEquals(expected, jclo.toString());
 }

 @Test
 public void testUsage() {
 String expected = "--age= Age\n--isMarried= IsMarried\n--name= Name\n";
 assertEquals(expected, jclo.usage());
 }

 @Test
 public void testParse() {
 String[] arguments = { "-age=31", "-isMarried=true" };
 jclo.parse(arguments);
 assertEquals(31, jclo.getInt("age"));
 assertTrue(jclo.getBoolean("isMarried"));
 }

 @Test
 public void testParseWithPrefix() {
 String prefix = "prefix_";
 jclo = new JCLO(prefix, object);
 String[] arguments = { "--prefix_age=31", "--prefix_isMarried=true" };
 jclo.parse(arguments);
 assertEquals(31, jclo.getInt("prefix_age"));
 assertTrue(jclo.getBoolean("prefix_isMarried"));
 }

 @Test
 public void testParseWithAliases() {
 String[][] aliases = { { "age", "Age" }, { "isMarried", "IsMarried" } };
 jclo = new JCLO(object, aliases);
 String[] arguments = { "--Age=31", "--IsMarried=true" };
 jclo.parse(arguments);
 assertEquals(31, jclo.getInt("age"));
 assertTrue(jclo.getBoolean("isMarried"));
 }

 @Test
 public void testParseWithPrefixAndAliases() {
 String prefix = "prefix_";
 String[][] aliases = { { "age", "Age" }, { "isMarried", "IsMarried" } };
 jclo = new JCLO(prefix, object, aliases);
 String[] arguments = { "--prefix_Age=31", "--prefix_IsMarried=true" };
 jclo.parse(arguments);
 assertEquals(31, jclo.getInt("prefix_age"));
 assertTrue(jclo.getBoolean("prefix_isMarried"));
 }

 @Test
 public void testParseWithInvalidArgument() {
 String[] arguments = { "--invalidOption=31" };
 assertThrows(IllegalArgumentException.class, () -> jclo.parse(arguments));
 }

 @Test
 public void testParseWithMissingValue() {
 String[] arguments = { "--age" };
 assertThrows(IllegalArgumentException.class, () -> jclo.parse(arguments));
 }

 @Test
 public void testParseWithInvalidType() {
 String[] arguments = { "--name=31" };
 assertThrows(IllegalArgumentException.class, () -> jclo.parse(arguments));
 }

 @Test
 public void testParseWithInvalidTypeForArray() {
 String[] arguments = { "--hobbies=apple" };
 assertThrows(IllegalArgumentException.class, () -> jclo.parse(arguments));
 }

 @Test
 public void testParseWithDuplicateKey() {
 String[] arguments = { "--age=31", "--age=32" };
 assertThrows(IllegalArgumentException.class, () -> jclo.parse(arguments));
 }

 @Test
 public void testParseWithNegativeNumber() {
 String[] arguments = { "--age=-1" };
 assertThrows(NumberFormatException.class, () -> jclo.parse(arguments));
 }

 @Test
 public void testParseWithBooleanValue() {
 String[] arguments = { "--isMarried=true" };
 jclo.parse(arguments);
 assertTrue(jclo.getBoolean("isMarried"));
 }

 @Test
 public void testParseWithBooleanValueFalse() {
 String[] arguments = { "--isMarried=false" };
 jclo.parse(arguments);
 assertFalse(jclo.getBoolean("isMarried"));
 }

 @Test
 public void testParseWithStringValue() {
 String[] arguments = { "--name=Jane Doe" };
 jclo.parse(arguments);
 assertEquals("Jane Doe", jclo.getValue("name"));
 }

 @Test
 public void testParseWithSimpleArray() {
 String[] arguments = { "--hobbies=football,reading" };
 jclo.parse(arguments);
 assertArrayEquals(new String[] { "football", "reading" }, (String[]) jclo.getValue("hobbies"));
 }

 @Test
 public void testParseWithComplexArray() {
 String[] arguments = { "--hobbies={football,reading},{tennis,swimming}" };
 jclo.parse(arguments);
 assertArrayEquals(new String[][] { { "football", "reading" }, { "tennis", "swimming" } }, (String[][]) jclo.getValue("hobbies"));
 }

}