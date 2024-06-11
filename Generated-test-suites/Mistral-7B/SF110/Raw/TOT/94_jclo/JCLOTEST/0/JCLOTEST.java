package edu.mscd.cs.jclo;

import org.junit.After;
import org.junit.Before;
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

 @After
 public void tearDown() {
 jclo = null;
 testObject = null;
 }

 @Test
 public void testGetValueWithValidKeyAndAccessibleField() {
 final String key = "field1";
 final String expectedValue = "expectedValue";
 Field field = ReflectionUtils.findField(JCLO.class, "fields", 0);
 ReflectionUtils.setFieldValue(jclo, field, new Object() {
 @SuppressWarnings("unused")
 private String value = expectedValue;
 });
 Object actualValue = jclo.getValue(key);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testGetValueWithInvalidKey() {
 assertNull(jclo.getValue("invalidKey"));
 }

 @Test
 public void testGetValueWithInaccessibleField() {
 final String key = "nonExistentField";
 assertNull(jclo.getValue(key));
 }

 @Test
 public void testGetValueWithPrefix() {
 final String prefix = "test_";
 final String key = "prefixField";
 final String expectedValue = "expectedValue";
 Field field = ReflectionUtils.findField(JCLO.class, "fields", 1);
 ReflectionUtils.setFieldValue(jclo, field, new Object() {
 @SuppressWarnings("unused")
 private String value = expectedValue;
 });
 jclo = new JCLO(prefix, testObject);
 Object actualValue = jclo.getValue(key);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testGetValueWithAliases() {
 final String prefix = "test_";
 final String key = "alias1";
 final String[][] aliases = new String[][] { { "alias2", "alias3" } };
 final String expectedValue = "expectedValue";
 Field field = ReflectionUtils.findField(JCLO.class, "fields", 2);
 ReflectionUtils.setFieldValue(jclo, field, new Object() {
 @SuppressWarnings("unused")
 private String value = expectedValue;
 });
 jclo = new JCLO(prefix, testObject, aliases);
 Object actualValue = jclo.getValue(key);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testGetStringValue() {
 final String key = "stringField";
 final String expectedValue = "expectedValue";
 Field field = ReflectionUtils.findField(JCLO.class, "fields", 3);
 ReflectionUtils.setFieldValue(jclo, field, "expectedValue");
 Object actualValue = jclo.getValue(key);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testGetByteValue() {
 final String key = "byteField";
 final byte expectedValue = (byte) 127;
 Field field = ReflectionUtils.findField(JCLO.class, "fields", 4);
 ReflectionUtils.setFieldValue(jclo, field, (byte) 127);
 Object actualValue = jclo.getValue(key);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testGetCharValue() {
 final String key = "charField";
 final char expectedValue = 'A';
 Field field = ReflectionUtils.findField(JCLO.class, "fields", 5);
 ReflectionUtils.setFieldValue(jclo, field, 'A');
 Object actualValue = jclo.getValue(key);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testGetShortValue() {
 final String key = "shortField";
 final short expectedValue = 32767;
 Field field = ReflectionUtils.findField(JCLO.class, "fields", 6);
 ReflectionUtils.setFieldValue(jclo, field, (short) 32767);
 Object actualValue = jclo.getValue(key);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testgetIntValue() {
 final String key = "intField";
 final int expectedValue = Integer.MAX_VALUE;
 Field field = ReflectionUtils.findField(JCLO.class, "fields", 7);
 ReflectionUtils.setFieldValue(jclo, field, Integer.MAX_VALUE);
 Object actualValue = jclo.getValue(key);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testGetFloatValue() {
 final String key = "floatField";
 final float expectedValue = Float.POSITIVE_INFINITY;
 Field field = ReflectionUtils.findField(JCLO.class, "fields", 8);
 ReflectionUtils.setFieldValue(jclo, field, Float.POSITIVE_INFINITY);
 Object actualValue = jclo.getValue(key);
 assertEquals(expectedValue, actualValue, 0.0f);
 }

 @Test
 public void testGetDoubleValue() {
 final String key = "doubleField";
 final double expectedValue = Double.POSITIVE_INFINITY;
 Field field = ReflectionUtils.findField(JCLO.class, "fields", 9);
 ReflectionUtils.setFieldValue(jclo, field, Double.POSITIVE_INFINITY);
 Object actualValue = jclo.getValue(key);
 assertEquals(expectedValue, actualValue, 0.0);
 }

 @Test
 public void testGetLongValue() {
 final String key = "longField";
 final long expectedValue = Long.MAX_VALUE;
 Field field = ReflectionUtils.findField(JCLO.class, "fields", 10);
 ReflectionUtils.setFieldValue(jclo, field, Long.MAX_VALUE);
 Object actualValue = jclo.getValue(key);
 assertEquals(expectedValue, actualValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetNonExistentField() {
 jclo.getValue("nonExistentField");
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullObject() {
 JCLO jclo = null;
 assertEquals(null, jclo.getValue("field1"));
 }
}