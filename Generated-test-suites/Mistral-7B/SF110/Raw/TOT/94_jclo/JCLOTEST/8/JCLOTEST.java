package edu.mscd.cs.jclo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Field;
import java.util.Arrays;

@RunWith(JUnit4.class)
public class JCLOTest {

 private static final String PREFIX = "prefix_";
 private static final String[][] ALIASES = new String[][] {{"prefix2", "alias2"}, {"prefix3", "alias3"}};

 @Test
 public void testConstructorWithObject() {
 JCLO jclo = new JCLO(new Object());
 Assert.assertNotNull(jclo);
 }

 @Test
 public void testConstructorWithObjectAndAliases() {
 JCLO jclo = new JCLO(new Object(), ALIASES);
 Assert.assertNotNull(jclo);
 }

 @Test
 public void testConstructorWithPrefixAndObject() {
 JCLO jclo = new JCLO(PREFIX, new Object());
 Assert.assertNotNull(jclo);
 }

 @Test
 public void testConstructorWithPrefixObjectAndAliases() {
 JCLO jclo = new JCLO(PREFIX, new Object(), ALIASES);
 Assert.assertNotNull(jclo);
 }

 @Test
 public void testGetValueWithValidKey() {
 JCLO jclo = new JCLO(PREFIX, new Object(), ALIASES);
 jclo.setObject(jclo.getField("field1"), "value1");
 String value = (String) jclo.getValue("field1");
 Assert.assertEquals("value1", value);
 }

 @Test
 public void testGetValueWithPrefixKey() {
 JCLO jclo = new JCLO(PREFIX, new Object(), ALIASES);
 jclo.setObject(jclo.getField("field1"), "value1");
 String value = (String) jclo.getValue("prefix_field1");
 Assert.assertEquals("value1", value);
 }

 @Test
 public void testGetValueWithInvalidKey() {
 JCLO jclo = new JCLO(PREFIX, new Object(), ALIASES);
 jclo.setObject(jclo.getField("field1"), "value1");
 String value = (String) jclo.getValue("invalid_key");
 Assert.assertNull(value);
 }

 @Test
 public void testSetValueWithValidKeyAndValue() {
 JCLO jclo = new JCLO(PREFIX, new Object(), ALIASES);
 String key = "field1";
 String value = "new_value";
 jclo.setValue(key, value);
 Assert.assertEquals(value, jclo.getObject(jclo.getField(key)).toString());
 }

 @Test
 public void testSetValueWithPrefixKeyAndValue() {
 JCLO jclo = new JCLO(PREFIX, new Object(), ALIASES);
 String key = "prefix_field1";
 String value = "new_value";
 jclo.setValue(key, value);
 Assert.assertEquals(value, jclo.getObject(jclo.getField("field1")).toString());
 }

 @Test
 public void testSetValueWithInvalidKey() {
 JCLO jclo = new JCLO(PREFIX, new Object(), ALIASES);
 String key = "invalid_key";
 String value = "new_value";
 jclo.setValue(key, value);
 Assert.assertNull(jclo.getObject(jclo.getField(key)));
 }

 @Test
 public void testToString() {
 JCLO jclo = new JCLO(PREFIX, new Object(), ALIASES);
 jclo.setObject(jclo.getField("field1"), "value1");
 jclo.setObject(jclo.getField("field2"), new int[0]);
 String expected = PREFIX + "field1: String = value1\n"
 + PREFIX + "field2: int[] = []";
 String result = jclo.toString();
 Assert.assertEquals(expected, result);
 }

 @Test(expected = IllegalAccessException.class)
 public void testGetObjectThrowsIllegalAccessException() {
 JCLO jclo = new JCLO(PREFIX, new Object(), ALIASES);
 Field field = jclo.getField("field1");
 jclo.setAccessible(true);
 jclo.getObject(field);
 }

 @Test(expected = NullPointerException.class)
 public void testGetObjectThrowsNullPointerException() {
 JCLO jclo = new JCLO(PREFIX, new Object(), ALIASES);
 Field field = null;
 jclo.getObject(field);
 }
}