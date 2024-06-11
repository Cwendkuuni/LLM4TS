package edu.mscd.cs.jclo;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

public class JCLOTest {

 private static final String CLASS_NAME = JCLO.class.getName();

 @Test
 public void testJCLOInstanceCreation() {
 JCLO jclo = new JCLO(new Object());
 Assert.assertNotNull(jclo.getObject(null));
 Assert.assertEquals(1, jclo.getFields().length);
 }

 @Test
 public void testJCLOInstanceCreationWithPrefix() {
 JCLO jclo = new JCLO("prefix", new Object());
 Assert.assertNotNull(jclo.getObject(null));
 Assert.assertEquals(1, jclo.getFields().length);
 Assert.assertEquals("prefix", jclo.getPrefix());
 }

 @Test
 public void testJCLOInstanceCreationWithAliases() {
 JCLO jclo = new JCLO(null, new Object(), new String[][]{{"a", "aliasA"}, {"b", "aliasB"}});
 Assert.assertNotNull(jclo.getObject(null));
 Assert.assertEquals(2, jclo.getFields().length);
 Assert.assertNotNull(jclo.getField("a"));
 Assert.assertNotNull(jclo.getField("aliasA"));
 Assert.assertEquals("aliasA", jclo.aliases[0][1]);
 }

 @Test(expected = IllegalAccessException.class)
 public void testGetObjectIllegalAccessException() {
 JCLO jclo = new JCLO(null, new Object());
 Field field = jclo.getField("notAField");
 jclo.getObject(field);
 }

 @Test
 public void testGetField() {
 JCLO jclo = new JCLO(null, new Object());
 Field field = jclo.getField("fieldName");
 Assert.assertNull(field);
 }

 @Test
 public void testGetValue() {
 JCLO jclo = new JCLO(null, new Object());
 Field field = jclo.getField("object");
 Object value = jclo.getValue("object");
 Assert.assertNotNull(value);
 Assert.assertEquals(jclo.getObject(field), value);
 }

 @Test
 public void testSetObject() {
 JCLO jclo = new JCLO(null, new Object());
 Field field = jclo.getField("object");
 Object valueToSet = new Object();
 jclo.setObject(field, valueToSet);
 Assert.assertEquals(valueToSet, jclo.getObject(field));
 }

 @Test
 public void testGetArrayType() {
 Class type = int[].class;
 Assert.assertEquals("int[]", JCLO.getArrayType(type));
 }

 @Test
 public void testToString() {
 JCLO jclo = new JCLO(null, new Object());
 Object[] fieldsValues = {new Integer(1), new Byte((byte) 2), "three"};
 for (int i = 0; i < jclo.getFields().length; i++) {
 JCLO.setObject(jclo.getFields()[i], fieldsValues[i]);
 }
 String expected = "int: object = [1]\nbyte: object = 2\njava.lang.String: object = three\n";
 String actual = jclo.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testUsage() {
 JCLO jclo = new JCLO(null, new Object());
 String expected = "";
 String actual = jclo.usage();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testParseAdditional() {
 JCLO jclo = new JCLO(null, new Object());
 String[] args = {"--additional", "arg1", "arg2", "arg3"};
 jclo.parse(args);
 Object[] expectedAdditional = {arg1, arg2, arg3};
 Object[] actualAdditional = (Object[]) jclo.getObject(jclo.getField("additional"));
 Assert.assertArrayEquals(expectedAdditional, actualAdditional);
 }

 @Test
 public void testParseAddToArray() {
 JCLO jclo = new JCLO(null, new Object());
 String[] args = {"--arrayField", "arg1", "--arrayField", "arg2"};
 jclo.parse(args);
 Object[] expectedArray = {arg1, arg2};
 Object[] actualArray = (Object[]) jclo.getObject(jclo.getField("arrayField"));
 Assert.assertArrayEquals(expectedArray, actualArray);
 }

 @Test
 public void testParseBooleanValue() {
 JCLO jclo = new JCLO(null, new Object());
 String[] args = {"--booleanField", "true"};
 jclo.parse(args);
 boolean expectedBoolean = true;
 boolean actualBoolean = jclo.getBoolean("booleanField");
 Assert.assertEquals(expectedBoolean, actualBoolean);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseIllegalArgumentException() {
 JCLO jclo = new JCLO(null, new Object());
 String[] args = {"--booleanField=value"};
 jclo.parse(args);
 }

 @Test
 public void testParseEqualsValue() {
 JCLO jclo = new JCLO(null, new Object());
 String[] args = {"--equalsField=value"};
 jclo.parse(args);
 String expectedEqualsValue = "value";
 String actualEqualsValue = jclo.getEqualsValue("equalsField");
 Assert.assertEquals(expectedEqualsValue, actualEqualsValue);
 }

 @Test
 public void testMakeObjectByte() {
 Object expected = Byte.valueOf((byte) 1);
 Object actual = JCLO.makeObject("byte", "1");
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMakeObjectBoolean() {
 Object expected = Boolean.TRUE;
 Object actual = JCLO.makeObject("boolean", "true");
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMakeObjectInt() {
 Object expected = Integer.valueOf(1);
 Object actual = JCLO.makeObject("int", "1");
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMakeObjectString() {
 Object expected = "testString";
 Object actual = JCLO.makeObject("java.lang.String", "testString");
 Assert.assertEquals(expected, actual);
 }
}