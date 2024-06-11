package edu.mscd.cs.jclo;

import org.junit.Test;
import static org.junit.Assert.*;

public class JCLOTest {

 private Object testObject;
 private JCLO jclo;

 @Before
 public void setUp() {
 testObject = new Object() {
 };
 jclo = new JCLO(testObject);
 }

 @Test
 public void testConstructorWithObject() {
 JCLO jcloWithObject = new JCLO(testObject);
 assertEquals(testObject, jcloWithObject.getObject("object"));
 }

 @Test
 public void testConstructorWithAliases() {
 String[][] aliases = new String[][]{{"a", "aliasA"}, {"b", "aliasB"}};
 JCLO jcloWithAliases = new JCLO(testObject, aliases);
 assertEquals(testObject, jcloWithAliases.getObject("object"));
 assertEquals("aliasA", jcloWithAliases.getValue("a"));
 assertEquals("aliasB", jcloWithAliases.getValue("b"));
 }

 @Test
 public void testConstructorWithPrefix() {
 String prefix = "prefix_";
 JCLO jcloWithPrefix = new JCLO(prefix, testObject);
 assertEquals(testObject, jcloWithPrefix.getObject("object"));
 assertEquals("prefix_object", jcloWithPrefix.getValue("object"));
 }

 @Test
 public void testConstructorWithPrefixAndAliases() {
 String prefix = "prefix_";
 String[][] aliases = new String[][]{{"a", "aliasA"}, {"b", "aliasB"}};
 JCLO jcloWithPrefixAndAliases = new JCLO(prefix, testObject, aliases);
 assertEquals(testObject, jcloWithPrefixAndAliases.getObject("object"));
 assertEquals("prefix_object", jcloWithPrefixAndAliases.getValue("object"));
 assertEquals("aliasA", jcloWithPrefixAndAliases.getValue("a"));
 assertEquals("aliasB", jcloWithPrefixAndAliases.getValue("b"));
 }

 @Test(expected = IllegalAccessException.class)
 public void testGetObjectWithInaccessibleField() {
 Field inaccessibleField = testObject.getClass().getDeclaredFields()[0];
 inaccessibleField.setAccessible(false);
 JCLO jclo = new JCLO(testObject);
 jclo.getObject(inaccessibleField);
 }

 @Test
 public void testGetFieldWithUnknownKey() {
 JCLO jclo = new JCLO(testObject);
 assertNull(jclo.getValue("unknownKey"));
 }

 @Test
 public void testGetValueWithBooleanField() {
 JCLO jclo = new JCLO(testObject);
 jclo.setObject(jclo.getField("booleanField"), true);
 assertTrue(jclo.getBoolean("booleanField"));
 }

 @Test
 public void testGetValueWithByteField() {
 JCLO jclo = new JCLO(testObject);
 jclo.setObject(jclo.getField("byteField"), (byte) 5);
 assertEquals((byte) 5, jclo.getByte("byteField"));
 }

 @Test
 public void testGetValueWithCharField() {
 JCLO jclo = new JCLO(testObject);
 jclo.setObject(jclo.getField("charField"), 'a');
 assertEquals('a', jclo.getChar("charField"));
 }

 @Test
 public void testGetValueWithShortField() {
 JCLO jclo = new JCLO(testObject);
 jclo.setObject(jclo.getField("shortField"), (short) 10);
 assertEquals((short) 10, jclo.getShort("shortField"));
 }

 @Test
 public void testGetValueWithIntField() {
 JCLO jclo = new JCLO(testObject);
 jclo.setObject(jclo.getField("intField"), 10);
 assertEquals(10, jclo.getInt("intField"));
 }

 @Test
 public void testGetValueWithFloatField() {
 JCLO jclo = new JCLO(testObject);
 jclo.setObject(jclo.getField("floatField"), 3.14f);
 assertEquals(3.14f, jclo.getFloat("floatField"), 0.01f);
 }

 @Test
 public void testGetValueWithDoubleField() {
 JCLO jclo = new JCLO(testObject);
 jclo.setObject(jclo.getField("doubleField"), 3.14);
 assertEquals(3.14, jclo.getDouble("doubleField"), 0.001);
 }

 @Test
 public void testGetValueWithLongField() {
 JCLO jclo = new JCLO(testObject);
 jclo.setObject(jclo.getField("longField"), 10L);
 assertEquals(10L, jclo.getLong("longField"));
 }

 @Test
 public void testGetValueWithStringField() {
 JCLO jclo = new JCLO(testObject);
 jclo.setObject(jclo.getField("stringField"), "test");
 assertEquals("test", jclo.getString("stringField"));
 }

 @Test
 public void testGetValueWithArrayField() {
 JCLO jclo = new JCLO(testObject);
 jclo.setObject(jclo.getField("arrayField"), new int[]{1, 2, 3});
 assertArrayEquals(new int[]{1, 2, 3}, jclo.getInts("arrayField"));
 }

 @Test
 public void testGetStringUsage() {
 JCLO jclo = new JCLO(testObject);
 jclo.setObject(jclo.getField("usageString"), "testUsageString");
 assertEquals("testUsageString", jclo.getValue("usageString"));
 assertEquals("--testUsageString", jclo.usage().split("\n")[0]);
 }

 @Test
 public void testParseWithBooleanFlag() {
 String[] args = new String[]{"--booleanFlag"};
 JCLO jclo = new JCLO(testObject);
 jclo.parse(args);
 assertTrue(jclo.getBoolean("booleanFlag"));
 }

 @Test
 public void testParseWithBooleanArrayFlag() {
 String[] args = new String[]{"--booleanArrayFlag=true", "--booleanArrayFlag=false"};
 JCLO jclo = new JCLO(testObject);
 jclo.parse(args);
 assertTrue(jclo.getBoolean("booleanArrayFlag[0]"));
 assertFalse(jclo.getBoolean("booleanArrayFlag[1]"));
 }

 @Test
 public void testParseWithStringFlag() {
 String[] args = new String[]{"--stringFlag=testString"};
 JCLO jclo = new JCLO(testObject);
 jclo.parse(args);
 assertEquals("testString", jclo.getValue("stringFlag"));
 }

 @Test
 public void testParseWithStringArrayFlag() {
 String[] args = new String[]{"--stringArrayFlag=test1", "--stringArrayFlag=test2"};
 JCLO jclo = new JCLO(testObject);
 jclo.parse(args);
 String[] result = jclo.getStrings("stringArrayFlag");
 assertEquals(2, result.length);
 assertEquals("test1", result[0]);
 assertEquals("test2", result[1]);
 }

 @Test
 public void testParseWithInvalidFlag() {
 String[] args = new String[]{"--invalidFlag"};
 JCLO jclo = new JCLO(testObject);
 try {
 jclo.parse(args);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}