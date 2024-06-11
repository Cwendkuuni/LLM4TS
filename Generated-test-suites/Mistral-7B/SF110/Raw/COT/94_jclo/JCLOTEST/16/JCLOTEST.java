package edu.mscd.cs.jclo;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.Test;

public class JCLOTest {

 private Object object;
 private JCLO jclo;

 @Before
 public void setup() {
 object = new Object() {
 private int i = 42;
 private String s = "test";
 };
 jclo = new JCLO(object);
 }

 @Test
 public void testJCLOConstructorWithObject() {
 JCLO jclo = new JCLO(object);
 assertEquals(object.getClass(), jclo.fields[0].getType());
 }

 @Test
 public void testJCLOConstructorWithObjectAndAliases() {
 String[][] aliases = new String[1][2];
 aliases[0][0] = "i";
 aliases[0][1] = "num";
 JCLO jclo = new JCLO(object, aliases);
 Field field = jclo.getField("num");
 assertEquals(int.class, field.getType());
 }

 @Test
 public void testJCLOConstructorWithPrefixAndObject() {
 JCLO jclo = new JCLO("prefix_", object);
 assertEquals("prefix_i", jclo.getField("i").getName());
 }

 @Test
 public void testJCLOConstructorWithPrefixAndObjectAndAliases() {
 String[][] aliases = new String[1][2];
 aliases[0][0] = "i";
 aliases[0][1] = "num";
 JCLO jclo = new JCLO("prefix_", object, aliases);
 Field field = jclo.getField("num");
 assertEquals(int.class, field.getType());
 }

 @Test
 public void testGetObject() {
 Field field = jclo.getField("i");
 Object value = jclo.getObject(field);
 assertEquals(42, value);
 }

 @Test(expected = IllegalAccessException.class)
 public void testGetObjectWithInaccessibleField() {
 Field field = jclo.getClass().getDeclaredFields()[0];
 field.setAccessible(true);
 Object value = jclo.getObject(field);
 fail("IllegalAccessException expected, but not thrown");
 }

 @Test
 public void testGetField() {
 Field field = jclo.getField("i");
 assertEquals("i", field.getName());
 }

 @Test
 public void testGetFieldWithNonExistentKey() {
 Field field = jclo.getField("nonExistentKey");
 assertNull(field);
 }

 @Test
 public void testgetValue() {
 int expected = 42;
 Object value = jclo.getValue("i");
 assertEquals(expected, value);
 }

 @Test
 public void testgetValueWithNonExistentKey() {
 Object value = jclo.getValue("nonExistentKey");
 assertNull(value);
 }

 @Test
 public void testGetUsageType() {
 Class intType = int.class;
 assertEquals(" int: i = ", jclo.getUsageType(intType));
 Class booleanType = boolean.class;
 assertEquals(" [=boolean]: boolean: b = ", jclo.getUsageType(booleanType));
 Class arrayIntType = int[].class;
 assertEquals(" int...: ints = ", jclo.getUsageType(arrayIntType));
 }

 @Test
 public void testUsage() {
 String expected = "int: i = \n" +
                   "boolean: b = ";
 String usage = jclo.usage();
 assertEquals(expected, usage);
 }

 @Test
 public void testParseAdditional() {
 String[] args = {"--additional", "arg1", "arg2"};
 jclo.parse(args);
 Object additional = jclo.getObject(jclo.getField("additional"));
 assertArrayEquals(new String[]{"arg1", "arg2"}, (String[])additional);
 }

 @Test
 public void testAddToArray() {
 Field intsField = jclo.getField("ints");
 Object ints = jclo.getObject(intsField);
 jclo.addToArray(intsField, new Integer(43));
 Object newInts = jclo.getObject(intsField);
 assertArrayEquals(new Integer[]{42, 43}, (Integer[])newInts);
 }

 @Test
 public void testGetKey() {
 String key = "i_";
 String expected = "_1";
 assertEquals(expected, jclo.getKey(key));
 }

 @Test
 public void testGetBooleanValue() {
 String arg = "hasFlag=true";
 String expected = "true";
 assertEquals(expected, jclo.getBooleanValue(arg));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetBooleanValueWithInvalidArg() {
 String arg = "hasFlag=";
 }

 @Test
 public void testMakeObject() {
 String booleanValue = "true";
 Class booleanType = boolean.class;
 Object expected = new Boolean(booleanValue);
 Object actual = jclo.makeObject(booleanType, booleanValue);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMakeObjectWithInvalidType() {
 String stringValue = "test";
 Class longType = long.class;
 }

 @Test
 public void testParse() {
 String[] args = {"--doubleDashes", "--hasEquals", "-i=42", "-b=true", "-s=test"};
 jclo.parse(args);
 assertEquals(true, jclo.getBoolean("hasEquals"));
 assertEquals(42, jclo.getByte("i"));
 assertEquals("test", jclo.getString("s"));
 assertEquals(true, jclo.getBoolean("b"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidArg() {
 String[] args = {"--doubleDashes", "--hasEquals", "-i=42=", "-b=true"};
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithMissingValue() {
 String[] args = {"--doubleDashes", "--hasEquals", "-i="};
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidType() {
 String[] args = {"--doubleDashes", "--hasEquals", "-i=abc", "-b=true"};
 }
}