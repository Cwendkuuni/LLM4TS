package edu.mscd.cs.jclo;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

public class JCLOTest {

 private JCLO jclo;

 @Test
 public void testConstructorWithObject() {
 jclo = new JCLO(new Object());
 Assert.assertNotNull(jclo.getValue("field1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidKey() {
 jclo = new JCLO(new Object(), new String[][]{{"field1", "alias1"}});
 jclo.getField("invalidKey");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidArgument() {
 jclo = new JCLO("prefix", new Object());
 jclo.parse(new String[]{"--invalid=value"});
 }

 @Test
 public void testGetValueWithValidKey() {
 jclo = new JCLO("prefix", new Object(), new String[][]{{"field1", "alias1"}});
 jclo.parse(new String[]{"--prefix=prefix1", "--field1=value1"});
 Assert.assertEquals("value1", jclo.getValue("field1"));
 Assert.assertEquals("prefix1", jclo.getValue("prefix"));
 }

 @Test
 public void testGetValueWithAlias() {
 jclo = new JCLO("prefix", new Object(), new String[][]{{"field1", "alias1"}});
 jclo.parse(new String[]{"--prefix=prefix1", "--alias1=value1"});
 Assert.assertEquals("value1", jclo.getValue("field1"));
 }

 @Test
 public void testGetValueWithLongKey() {
 jclo = new JCLO("prefix", new Object(), new String[][]{{"field1", "alias1"}}) {
 private static final int FIELD_COUNT = 100;

 @Override
 synchronized Field[] getDeclaredFields() {
 Field[] fields = new Field[FIELD_COUNT + 1];
 for (int i = 0; i < FIELD_COUNT; ++i) {
 fields[i] = super.getDeclaredFields()[i];
 }
 fields[FIELD_COUNT] = new Field(this.getClass(), "longField");
 return fields;
 }
 };
 jclo.parse(new String[]{"--prefix=prefix1", "--longField=value1"});
 Assert.assertEquals("value1", jclo.getValue("longField"));
 }

 @Test
 public void testSetValue() {
 jclo = new JCLO("prefix", new Object());
 jclo.parse(new String[]{"--prefix=prefix1", "--field1=value1"});
 jclo.setValue("field1", "newValue");
 Assert.assertEquals("newValue", jclo.getValue("field1"));
 }

 @Test(expected = IllegalAccessException.class)
 public void testSetValueWithPrivateField() throws IllegalAccessException {
 jclo = new JCLO("prefix", new Object(), new String[][]{{"field1", "alias1"}});
 jclo.parse(new String[]{"--prefix=prefix1", "--field1=value1"});
 final Field privateField = jclo.getClass().getDeclaredField("privateField");
 privateField.setAccessible(true);
 jclo.setValue("privateField", "newValue");
 }

 @Test
 public void testToString() {
 jclo = new JCLO("prefix", new Object(), new String[][]{{"field1", "alias1"}});
 jclo.parse(new String[]{"--prefix=prefix1", "--field1=value1"});
 final String expectedOutput = "boolean: prefix = prefix1\n" +
 "boolean: field1 = value1\n";
 Assert.assertEquals(expectedOutput, jclo.toString());
 }

 @Test
 public void testUsage() {
 jclo = new JCLO("prefix", new Object(), new String[][]{{"field1", "alias1"}});
 jclo.parse(new String[]{"--prefix=prefix1", "--field1=value1"});
 final String expectedUsage = "--prefix=prefix\n" +
 "--field1=value1\n";
 Assert.assertEquals(expectedUsage, jclo.usage());
 }

 @Test
 public void testParseWithAdditionalArgument() {
 jclo = new JCLO("prefix", new Object());
 jclo.parse(new String[]{"--prefix=prefix1", "--", "--additional=arg1", "--additional=arg2"});
 final String[] expectedAdditional = {"arg1", "arg2"};
 Assert.assertArrayEquals(expectedAdditional, (String[])jclo.getValue("additional"));
 }
}