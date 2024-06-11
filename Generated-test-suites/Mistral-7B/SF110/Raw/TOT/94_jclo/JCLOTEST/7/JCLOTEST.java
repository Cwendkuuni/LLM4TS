package edu.mscd.cs.jclo;

import org.junit.Test;
import org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.Arrays;

public class JCLOTest {

 private JCLO jclo;

 @Test
 public void testConstructorWithObject() {
 jclo = new JCLO(new Object());
 Assert.assertNotNull(jclo.object);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullObject() {
 jclo = new JCLO(null);
 }

 @Test
 public void testConstructorWithObjectAndAliases() {
 jclo = new JCLO(new Object(), new String[][] {{ "alias1", "value1" }, { "alias2", "value2" }});
 Assert.assertNotNull(jclo.object);
 Assert.assertArrayEquals(new String[] { "value1", "value2" }, (String[]) jclo.getValue("alias1"));
 }

 @Test
 public void testConstructorWithPrefixAndObject() {
 jclo = new JCLO("prefix", new Object());
 Assert.assertEquals("prefix", jclo.prefix);
 }

 @Test
 public void testConstructorWithPrefixAndObjectAndAliases() {
 jclo = new JCLO("prefix", new Object(), new String[][] {{ "alias1", "value1" }, { "alias2", "value2" }});
 Assert.assertEquals("prefix", jclo.prefix);
 Assert.assertArrayEquals(new String[] { "value1", "value2" }, (String[]) jclo.getValue("alias1"));
 }

 @Test
 public void testGetValue() {
 jclo = new JCLO(new String("prefix"), new Object(), new String[][] {{ "alias1", "value1" }, { "alias2", "value2" }});
 Assert.assertEquals("value1", jclo.getValue("alias1"));
 Assert.assertEquals("value2", jclo.getValue("alias2"));
 }

 @Test
 public void testSetValue() {
 jclo = new JCLO();
 jclo.setObject(jclo.getField("field1"), new String[] { "value1", "value2" });
 Assert.assertArrayEquals(new String[] { "value1", "value2" }, (String[]) jclo.getValue("field1"));
 }

 @Test
 public void testUsage() {
 jclo = new JCLO(new Object());
 Assert.assertEquals("", jclo.usage());
 }

 @Test
 public void testUsageWithAliases() {
 jclo = new JCLO(new Object(), new String[][] {{ "alias1", "value1" }, { "alias2", "value2" }});
 Assert.assertEquals("alias1 = value1\nalias2 = value2\n", jclo.usage());
 }

 @Test
 public void testUsageWithPrefix() {
 jclo = new JCLO("prefix", new Object());
 Assert.assertEquals("prefixfield1 = \n", jclo.usage());
 }

 @Test
 public void testUsageWithPrefixAndAliases() {
 jclo = new JCLO("prefix", new Object(), new String[][] {{ "alias1", "value1" }, { "alias2", "value2" }});
 Assert.assertEquals("prefixfield1 = \nprefixalias1 = value1\nprefixalias2 = value2\n", jclo.usage());
 }

 @Test
 public void testParse() {
 jclo = new JCLO();
 jclo.parse(new String[] { "--doubleDashes", "--hasEquals=true", "--prefix=prefix", "--alias1=value1", "--alias2=value2" });
 Assert.assertTrue(jclo.doubleDashes);
 Assert.assertTrue(jclo.hasEquals);
 Assert.assertEquals("prefix", jclo.prefix);
 Assert.assertArrayEquals(new String[] { "value1", "value2" }, (String[]) jclo.getValue("alias1"));
 }

 @Test
 public void testParseWithInvalidOption() {
 jclo = new JCLO();
 try {
 jclo.parse(new String[] { "--invalidOption=value" });
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseWithInvalidValueType() {
 jclo = new JCLO();
 try {
 jclo.parse(new String[] { "--boolean=invalidValue" });
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}