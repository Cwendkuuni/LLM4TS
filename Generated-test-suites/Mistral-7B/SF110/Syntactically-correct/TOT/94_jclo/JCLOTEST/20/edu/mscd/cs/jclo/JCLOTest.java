package edu.mscd.cs.jclo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class JCLOTest {

 private JCLO jclo;

 @Test
 public void testConstructorWithObject() {
 jclo = new JCLO(new Object());
 Assert.assertNotNull(jclo.getValue("field1"));
 }

 @Test
 public void testConstructorWithAliases() {
 String[][] aliases = {{ "alias1", "field2" }, { "alias2", "field3" }};
 jclo = new JCLO(null, new Object(), aliases);
 Assert.assertEquals(jclo.getValue("alias1"), jclo.getValue("field2"));
 Assert.assertEquals(jclo.getValue("alias2"), jclo.getValue("field3"));
 }

 @Test
 public void testConstructorWithPrefix() {
 String prefix = "prefix_";
 jclo = new JCLO(prefix, new Object());
 Assert.assertEquals(jclo.getValue("prefix_field1"), jclo.getValue("field1"));
 }

 @Test
 public void testConstructorWithPrefixAndAliases() {
 String prefix = "prefix_";
 String[][] aliases = {{ "alias1", "field2" }, { "alias2", "field3" }};
 jclo = new JCLO(prefix, new Object(), aliases);
 Assert.assertEquals(jclo.getValue("prefix_alias1"), jclo.getValue("field2"));
 Assert.assertEquals(jclo.getValue("prefix_alias2"), jclo.getValue("field3"));
 }

 @Test(expected = IllegalAccessException.class)
 public void testGetObjectWithInaccessibleField() {
 jclo = new JCLO(null, new Object());
 jclo.getObject(new Field("inaccessibleField") {});
 }

 @Test
 public void testGetFieldWithNonExistentKey() {
 jclo = new JCLO(null, new Object());
 Assert.assertNull(jclo.getValue("nonExistentKey"));
 }

 @Test
 public void testGetValueWithNullObject() {
 jclo = new JCLO(null, null);
 Assert.assertNull(jclo.getValue("field1"));
 }

 @Test
 public void testGetValueWithBooleanField() {
 jclo = new JCLO(null, new Object());
 jclo.setObject(jclo.getField("booleanField"), true);
 Assert.assertTrue((boolean)jclo.getValue("booleanField"));
 }

 @Test
 public void testGetValueWithByteField() {
 jclo = new JCLO(null, new Object());
 jclo.setObject(jclo.getField("byteField"), (byte)10);
 Assert.assertEquals((byte)10, (byte)jclo.getValue("byteField"));
 }

 @Test
 public void testGetValueWithShortField() {
 jclo = new JCLO(null, new Object());
 jclo.setObject(jclo.getField("shortField"), (short)20);
 Assert.assertEquals((short)20, (short)jclo.getValue("shortField"));
 }

 @Test
 public void testGetValueWithIntField() {
 jclo = new JCLO(null, new Object());
 jclo.setObject(jclo.getField("intField"), 30);
 Assert.assertEquals(30, (int)jclo.getValue("intField"));
 }

 @Test
 public void testGetValueWithFloatField() {
 jclo = new JCLO(null, new Object());
 jclo.setObject(jclo.getField("floatField"), 40.0f);
 Assert.assertEquals(40.0f, (float)jclo.getValue("floatField"), 0.001f);
 }

 @Test
 public void testGetValueWithDoubleField() {
 jclo = new JCLO(null, new Object());
 jclo.setObject(jclo.getField("doubleField"), 50.0);
 Assert.assertEquals(50.0, (double)jclo.getValue("doubleField"), 0.00001);
 }

 @Test
 public void testGetValueWithLongField() {
 jclo = new JCLO(null, new Object());
 jclo.setObject(jclo.getField("longField"), 60L);
 Assert.assertEquals(60L, (long)jclo.getValue("longField"));
 }

 @Test
 public void testGetValueWithStringField() {
 jclo = new JCLO(null, new Object());
 jclo.setObject(jclo.getField("stringField"), "test");
 Assert.assertEquals("test", (String)jclo.getValue("stringField"));
 }

 @Test
 public void testGetValueWithArrayField() {
 jclo = new JCLO(null, new Object());
 jclo.setObject(jclo.getField("intArrayField"), new int[]{1, 2, 3});
 Assert.assertArrayEquals(new int[]{1, 2, 3}, (int[])jclo.getValue("intArrayField"));
 }

 @Test
 public void testUsage() {
 jclo = new JCLO(null, new Object());
 String usage = jclo.usage();
 Assert.assertTrue(usage.contains("boolean: --boolean =BOOLEAN"));
 Assert.assertTrue(usage.contains("byte: --byte =BYTE"));
 Assert.assertTrue(usage.contains("short: --short =SHORT"));
 Assert.assertTrue(usage.contains("int: --int =INT"));
 Assert.assertTrue(usage.contains("float: --float =FLOAT"));
 Assert.assertTrue(usage.contains("double: --double =DOUBLE"));
 Assert.assertTrue(usage.contains("long: --long =LONG"));
 Assert.assertTrue(usage.contains("java.lang.String: --string =STRING"));
 Assert.assertTrue(usage.contains("char: --char =CHAR"));
 }

}