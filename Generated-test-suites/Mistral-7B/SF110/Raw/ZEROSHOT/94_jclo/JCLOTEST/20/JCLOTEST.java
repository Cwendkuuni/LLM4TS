package edu.mscd.cs.jclo;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JCLOTest {

 @Test
 public void testJCLOConstruction() {
 JCLO jclo = new JCLO(new Object());
 assertNotNull(jclo);
 assertEquals(0, jclo.fields.length);

 JCLO jclo1 = new JCLO(new Object(), new String[][] {{"test", "value"}});
 assertNotNull(jclo1);
 assertEquals(1, jclo1.fields.length);

 JCLO jclo2 = new JCLO("prefix", new Object());
 assertNotNull(jclo2);
 assertEquals("", jclo2.prefix);

 JCLO jclo3 = new JCLO("prefix", new Object(), new String[][] {{"test", "value"}});
 assertNotNull(jclo3);
 assertEquals("prefix", jclo3.prefix);
 }

 @Test
 public void testGetObject() {
 JCLO jclo = new JCLO(new Object(), new String[][] {{"test", "value"}});
 Field testField = jclo.getField("test");
 assertNotNull(testField);
 Object testObject = jclo.getObject(testField);
 assertEquals("value", testObject);
 }

 @Test
 public void testGetField() {
 JCLO jclo = new JCLO(new Object(), new String[][] {{"test", "value"}});
 Field testField = jclo.getField("test");
 assertSame(testField, jclo.getField("test"));
 }

 @Test(expected = NullPointerException.class)
 public void testGetFieldNull() {
 JCLO jclo = new JCLO(new Object());
 Field testField = jclo.getField("test");
 assertNull(testField);
 }

 @Test
 public void testGetValue() {
 JCLO jclo = new JCLO(new Object(), new String[][] {{"test", "value"}});
 Field testField = jclo.getField("test");
 Object testObject = jclo.getObject(testField);
 assertEquals("value", jclo.getValue("test"));
 }

 @Test
 public void testGetValueNull() {
 JCLO jclo = new JCLO(new Object());
 assertEquals(null, jclo.getValue("test"));
 }

 @Test
 public void testGetValueNonExistentField() {
 JCLO jclo = new JCLO(new Object());
 assertEquals(null, jclo.getValue("test"));
 }

 @Test
 public void testToString() {
 JCLO jclo = new JCLO(new Object(), new String[][] {{"test", new int[] {1, 2, 3}}});
 assertEquals("int[]: test = [1, 2, 3]\n", jclo.toString());
 }

 @Test
 public void testUsage() {
 JCLO jclo = new JCLO(new Object());
 String usage = jclo.usage();
 String expectedUsage = "--test=<value>\n";
 assertEquals(expectedUsage, usage);
 }

 @Test
 public void testParseAdditional() {
 JCLO jclo = new JCLO(new Object(), new String[][] {{"additional", new String[] {"arg1", "arg2"}}});
 String[] additional = jclo.getObject(jclo.getField("additional"));
 assertNotNull(additional);
 assertArrayEquals(new String[] {"arg1", "arg2"}, additional);

 JCLO jclo1 = new JCLO(new Object(), new String[][] {{"additional", new int[] {1, 2, 3}}});
 fail("parseAdditional should not be able to parse integers for 'additional'");
 }

 @Test
 public void testParseAdditionalStringArray() {
 JCLO jclo = new JCLO(new Object(), new String[][] {{"test", new String[] {"arg1", "arg2"}}});
 jclo.parse(new String[] {"--test=arg1"});
 List<String> testList = (List<String>) jclo.getObject(jclo.getField("test"));
 assertNotNull(testList);
 assertFalse(testList.isEmpty());
 assertEquals("arg1", testList.get(0));
 }

 @Test
 public void testParseAdditionalIntegerArray() {
 JCLO jclo = new JCLO(new Object(), new String[][] {{"test", new int[] {1, 2, 3}}});
 fail("parseAdditional should not be able to parse integers for 'test'");
 }

 @Test
 public void testParse() {
 JCLO jclo = new JCLO(new Object(), new String[][] {});
 jclo.parse(new String[] {"--test=value"});
 assertEquals("value", jclo.getValue("test"));

 JCLO jclo1 = new JCLO(new Object(), new String[][] {{"--test=value", "--test2=anotherValue"}});
 List<String> testList = (List<String>) jclo1.getObject(jclo1.getField("test"));
 List<String> testList2 = (List<String>) jclo1.getObject(jclo1.getField("test2"));
 assertNotNull(testList);
 assertFalse(testList.isEmpty());
 assertEquals("value", testList.get(0));
 assertNotNull(testList2);
 assertFalse(testList2.isEmpty());
 assertEquals("anotherValue", testList2.get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidOption() {
 JCLO jclo = new JCLO(new Object(), new String[][] {});
 jclo.parse(new String[] {"--test=value=value"});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingValue() {
 JCLO jclo = new JCLO(new Object(), new String[][] {{"--test="}});
 jclo.parse(new String[] {"--test"});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingOption() {
 JCLO jclo = new JCLO(new Object(), new String[][] {{"--nonexistent="}});
 jclo.parse(new String[] {"--nonexistent"});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingDoubleDashes() {
 JCLO jclo = new JCLO(new Object(), new String[][] {{"--test"}});
 jclo.parse(new String[] {"--test=value"});
 }

 @Test
 public void testParseDoubleDashes() {
 JCLO jclo = new JCLO(new Object(), new String[][] {{"--test="}});
 jclo.parse(new String[] {"--test=--value"});
 assertEquals(true, jclo.hasEquals);
 }

 @Test
 public void testParseEquals() {
 JCLO jclo = new JCLO(new Object(), new String[][] {{"--test="}});
 jclo.parse(new String[] {"--test=value"});
 assertEquals("value", jclo.getValue("test"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidEquals() {
 JCLO jclo = new JCLO(new Object(), new String[][] {{"--test="}});
 jclo.parse(new String[] {"--test=value="});
 }

 @Test
 public void testParseWithAliases() {
 JCLO jclo = new JCLO(new Object(), new String[][] {{"--alias1=test1", "--alias2=test2"}});
 jclo.parse(new String[] {"--test1=value1", "--test2=value2"});
 assertEquals("value1", jclo.getValue("test1"));
 assertEquals("value2", jclo.getValue("test2"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidAlias() {
 JCLO jclo = new JCLO(new Object(), new String[][] {{"--test1=test", "--alias2=test3"}});
 jclo.parse(new String[] {"--test1=value1", "--test3=value3"});
 }

 @Test
 public void testGetters() {
 JCLO jclo = new JCLO(new Object(), new String[][] {{"test", "value"}});
 assertEquals("value", jclo.getString("test"));
 assertEquals(1, jclo.getInt("intTest"));
 assertEquals(1.0f, jclo.getFloat("floatTest"), 0.01f);
 assertEquals(1.0, jclo.getDouble("doubleTest"), 0.01);
 assertEquals(1, jclo.getByte("byteTest").byteValue());
 assertEquals('a', jclo.getChar("charTest").charValue());
 assertEquals(1, jclo.getShort("shortTest").shortValue());
 assertEquals(1, jclo.getInts("intsTest").length);
 assertEquals(1.0f, jclo.getFloats("floatsTest")[0], 0.01f);
 assertEquals(1.0, jclo.getDoubles("doublesTest")[0], 0.01);
 assertEquals(1, jclo.getLongs("longsTest").length);
 assertEquals("[1, 2, 3]", Arrays.toString(jclo.getStrings("stringsTest")));
 }

 @Test
 public void testGettersNull() {
 JCLO jclo = new JCLO(new Object());
 assertEquals(null, jclo.getString("test"));
 assertEquals(0, jclo.getInt("intTest"));
 assertEquals(0f, jclo.getFloat("floatTest"), 0.01f);
 assertEquals(0d, jclo.getDouble("doubleTest"), 0.01);
 assertEquals(0, jclo.getByte("byteTest").byteValue());
 assertEquals('\u0000', jclo.getChar("charTest").charValue());
 assertEquals(0, jclo.getShort("shortTest").shortValue());
 assertEquals(new int[0], jclo.getInts("intsTest"));
 assertEquals(new float[0], jclo.getFloats("floatsTest"));
 assertEquals(new double[0], jclo.getDoubles("doublesTest"));
 assertEquals(new long[0], jclo.getLongs("longsTest"));
 assertEquals(Collections.emptyList(), jclo.getStrings("stringsTest"));
 }

 @Test
 public void testMain() {
 String[] args = new String[1];
 args[0] = "--version";
 JCLO.main(args);
 }

}