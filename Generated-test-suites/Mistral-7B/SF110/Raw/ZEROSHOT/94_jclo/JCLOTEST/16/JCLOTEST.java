package edu.mscd.cs.jclo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class JCLOTest {

 private JCLO testObject;
 private Field[] fields;
 private Object[] testValues;

 @Before
 public void setUp() {
 testObject = new JCLO(new Object());
 testValues = new Object[]{"test1", 1, 'c', (short)2, 3.0f, 4.0d, 5, "test string", new char[]{'a', 'b'}, new short[]{1, 2}, new int[]{1, 2}, new float[]{1.0f, 2.0f}, new double[]{1.0d, 2.0d}, new long[]{1, 2}, new String[]{"test1", "test2"}};
 fields = testObject.getClass().getDeclaredFields();
 for (Field field : fields) {
 field.setAccessible(true);
 }
 }

 @After
 public void tearDown() {
 testObject = null;
 }

 @Test
 public void testConstructor() {
 Object obj = new Object();
 JCLO jclo = new JCLO(obj);
 assertSame(obj, jclo.getObject());
 }

 @Test
 public void testConstructorWithPrefix() {
 String prefix = "testPrefix";
 Object obj = new Object();
 JCLO jclo = new JCLO(prefix, obj);
 assertEquals(prefix, jclo.getPrefix());
 }

 @Test
 public void testConstructorWithPrefixAndAliases() {
 String prefix = "testPrefix";
 Object obj = new Object();
 String[][] aliases = {{ "alias1", "alias2" }, {"alias3", "alias4"}};
 JCLO jclo = new JCLO(prefix, obj, aliases);
 assertEquals(prefix, jclo.getPrefix());
 assertSame(aliases, jclo.getAliases());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConstructor() {
 String[][] aliases = {{ "alias1", "alias2" }, {"alias3", "alias4"}};
 try {
 JCLO jclo = new JCLO(null, null, aliases);
 } catch (IllegalArgumentException e) {
 assertEquals("Null object not allowed", e.getMessage());
 }
 }

 @Test
 public void testGetValue() {
 for (int i = 0; i < fields.length; i++) {
 Field field = fields[i];
 Object value = testValues[i];
 Object testValue = testObject.getValue(field.getName());
 assertEquals(value, testValue);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetValueInvalidKey() {
 String invalidKey = "invalidKey";
 assertNull(testObject.getValue(invalidKey));
 }

 @Test
 public void testSetObject() {
 Field field = fields[0];
 Object value = testValues[0];
 testObject.setObject(field, value);
 Object testValue = testObject.getValue(field.getName());
 assertEquals(value, testValue);
 }

 @Test
 public void testGetUsageType() {
 for (int i = 0; i < fields.length; i++) {
 Field field = fields[i];
 Class type = field.getType();
 String usageType = testObject.getUsageType(type);
 String expectedUsageType = "";
 if (type.isArray()) {
 expectedUsageType = "[" + getArrayType(type) + "]";
 } else {
 expectedUsageType = type.getName().replaceFirst("class java.lang.", "");
 }
 assertEquals(expectedUsageType, usageType);
 }
 }

 @Test
 public void testToString() {
 StringBuilder sb = new StringBuilder();
 for (int i = 0; i < fields.length; i++) {
 Field field = fields[i];
 Object value = testValues[i];
 if (i != 0) {
 sb.append("\n");
 }
 if (field.getType().isArray()) {
 sb.append(getArrayType(field.getType()) + "[]: ");
 sb.append(field.getName()).append(" = ");
 sb.append("[");
 Object[] arrayValue = (Object[])testObject.getValue(field.getName());
 for (int j = 0; j < Array.getLength(arrayValue); j++) {
 if (j != 0) {
 sb.append(", ");
 }
 sb.append(Array.get(arrayValue, j));
 }
 sb.append("]");
 } else {
 sb.append(field.getType().toString().replaceFirst("class java.lang.", ""));
 sb.append(": ").append(field.getName()).append(" = ").append(value);
 }
 }
 String expectedToString = sb.toString();
 assertEquals(expectedToString, testObject.toString());
 }

 @Test
 public void testParseAdditional() {
 String[] args = {"--additional", "arg1", "arg2"};
 testObject.parse(args);
 Object additional = testObject.getValue("additional");
 assertArrayEquals(new String[]{"arg1", "arg2"}, (String[])additional);
 }

 @Test
 public void testParseAdditionalNull() {
 String[] args = {"--additional"};
 testObject.parse(args);
 assertNull(testObject.getValue("additional"));
 }

 @Test
 public void testParse() {
 String[] args = {"--double-dashes", "--boolean=true", "--byte=1", "--char=c", "--short=2", "--int=3", "--float=3.0", "--double=4.0", "--long=5", "--string=test string", "--alias1=aliasValue1", "--alias2=aliasValue2"};
 testObject.parse(args);
 testBoolean("doubleDashes", true);
 testByte("byte", (byte)1);
 testChar("char", 'c');
 testShort("short", (short)2);
 testInt("int", 3);
 testFloat("float", 3.0f);
 testDouble("double", 4.0d);
 testLong("long", 5);
 testString("string", "test string");
 testAlias("alias1", "aliasValue1");
 testAlias("alias2", "aliasValue2");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidOption() {
 String[] args = {"--invalidOption"};
 testObject.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidValue() {
 String[] args = {"--byte=invalidValue"};
 testObject.parse(args);
 }

 @Test
 public void testUsage() {
 String expectedUsage = "";
 for (int i = 0; i < fields.length; i++) {
 Field field = fields[i];
 String key = field.getName();
 if (!key.equals("additional")) {
 if (testObject.prefix != null) {
 if (!key.startsWith(testObject.prefix)) {
 continue;
 }
 key = key.replaceFirst("^" + testObject.prefix, "");
 }
 if (!Modifier.isFinal(field.getModifiers())) {
 key = key.replaceFirst("^_([0-9])", "$1");
 key = key.replaceAll("__", "-");
 key = key.replaceAll("_\\$", ".");
 expectedUsage += (testObject.doubleDashes ? "--" : "-") + key + testObject.getUsageType(field.getType()) + "\n";
 }
 }
 }
 String usage = testObject.usage();
 assertEquals(expectedUsage, usage);
 }

 private String getArrayType(Class type) {
 return type.getComponentType().toString().replaceFirst("class.*\\.", "");
 }

 private void testBoolean(String key, boolean expectedValue) {
 assertTrue(testObject.getBoolean(key) == expectedValue);
 }

 private void testByte(String key, byte expectedValue) {
 assertEquals(expectedValue, testObject.getByte(key));
 }

 private void testChar(String key, char expectedValue) {
 assertEquals(expectedValue, testObject.getChar(key));
 }

 private void testShort(String key, short expectedValue) {
 assertEquals(expectedValue, testObject.getShort(key));
 }

 private void testInt(String key, int expectedValue) {
 assertEquals(expectedValue, testObject.getInt(key));
 }

 private void testFloat(String key, float expectedValue) {
 assertEquals(expectedValue, testObject.getFloat(key), 0.001f);
 }

 private void testDouble(String key, double expectedValue) {
 assertEquals(expectedValue, testObject.getDouble(key), 0.001d);
 }

 private void testLong(String key, long expectedValue) {
 assertEquals(expectedValue, testObject.getLong(key));
 }

 private void testString(String key, String expectedValue) {
 assertEquals(expectedValue, testObject.getString(key));
 }

 private void testAlias(String key, String expectedValue) {
 assertEquals(expectedValue, testObject.getValue(key).toString());
 }
}