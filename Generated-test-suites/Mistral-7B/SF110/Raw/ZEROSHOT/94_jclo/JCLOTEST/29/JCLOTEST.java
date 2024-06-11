import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;

public class JCLOTest {

 private Object testObject;
 private JCLO jclo;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 testObject = new TestClass();
 jclo = new JCLO(testObject);
 }

 @Test
 public void testConstructorWithObjectOnly() {
 JCLO jcloWithPrefix = new JCLO(testObject, new String[][]{{"prefix1", "alias1"}});
 Assert.assertNotNull(jcloWithPrefix);
 }

 @Test
 public void testConstructorWithObjectAndAliases() {
 JCLO jcloWithPrefixAndAliases = new JCLO("prefix1", testObject, new String[][]{{"alias1", "alias2"}});
 Assert.assertNotNull(jcloWithPrefixAndAliases);
 }

 @Test
 public void testConstructorWithPrefixAndObject() {
 JCLO jcloWithPrefix = new JCLO("prefix1", testObject);
 Assert.assertNotNull(jcloWithPrefix);
 }

 @Test
 public void testConstructorWithPrefixObjectAndAliases() {
 JCLO jcloWithPrefixAndAliases = new JCLO("prefix1", testObject, new String[][]{{"alias1", "alias2"}});
 Assert.assertNotNull(jcloWithPrefixAndAliases);
 }

 @Test
 public void testGetObjectWithAccessibleField() throws IllegalAccessException {
 Field testField = testObject.getClass().getDeclaredField("testField");
 testField.setAccessible(true);
 Object testValue = testObject.getClass().getDeclaredField("testField").get(testObject);
 Object jcloValue = jclo.getValue("testField");
 assertEquals(testValue, jcloValue);
 }

 @Test(expected = IllegalAccessException.class)
 public void testGetObjectWithInaccessibleField() throws NoSuchFieldException, IllegalAccessException {
 Field inaccessibleField = testObject.getClass().getDeclaredField("inaccessibleField");
 inaccessibleField.setAccessible(false);
 jclo.getValue("inaccessibleField");
 }

 @Test(expected = NullPointerException.class)
 public void testGetObjectWithNullField() {
 jclo.getValue(null);
 }

 @Test
 public void testGetFieldWithExistingField() {
 Field testField = testObject.getClass().getDeclaredField("testField");
 JCLO.Field jcloField = jclo.getField("testField");
 assertEquals(testField, jcloField);
 }

 @Test
 public void testGetFieldWithNonExistingField() {
 Field nonExistingField = null;
 JCLO.Field jcloField = jclo.getField("nonExistingField");
 assertNull(jcloField);
 }

 @Test
 public void testGetValueWithExistingFieldAndAccessibleObject() {
 Field testField = testObject.getClass().getDeclaredField("testField");
 Object testValue = new Integer(42);
 testField.set(testObject, testValue);
 Object jcloValue = jclo.getValue("testField");
 assertEquals(testValue, jcloValue);
 }

 @Test
 public void testValueWithNonExistingField() {
 Object jcloValue = jclo.getValue("nonExistingField");
 assertNull(jcloValue);
 }

 @Test
 public void testSetObjectWithAccessibleField() throws IllegalAccessException {
 Field testField = testObject.getClass().getDeclaredField("testField");
 testField.setAccessible(true);
 Object testValue = new Integer(7);
 jclo.setObject(testField, testValue);
 Object jcloSetValue = jclo.getValue("testField");
 assertEquals(testValue, jcloSetValue);
 }

 @Test(expected = IllegalAccessException.class)
 public void testSetObjectWithInaccessibleField() {
 Field inaccessibleField = testObject.getClass().getDeclaredField("inaccessibleField");
 inaccessibleField.setAccessible(false);
 jclo.setObject(inaccessibleField, new Integer(5));
 }

 @Test(expected = NullPointerException.class)
 public void testSetObjectWithNullField() {
 jclo.setObject(null, new Integer(5));
 }

 @Test
 public void testToStringWithEmptyObject() {
 String expected = "";
 String actual = jclo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringWithObjectWithFields() {
 String expected = "boolean: testBoolean = false\nint: testInt = 42\njava.lang.String: testString = test\n";
 String actual = jclo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetUsageTypeWithBoolean() {
 String expected = "[=boolean]";
 String actual = jclo.getUsageType(Boolean.TYPE);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetUsageTypeWithIntegerArray() {
 String expected = "int...";
 String actual = jclo.getUsageType(int[].class);
 assertEquals(expected, actual);
 }

 @Test
 public void testUsageWithEmptyObject() {
 String expected = "";
 String actual = jclo.usage();
 assertEquals(expected, actual);
 }

 @Test
 public void testUsageWithObjectWithFields() {
 String expected = "--testBoolean:\n--testInt:\n--testString:\n";
 String actual = jclo.usage();
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWithEmptyArgsArray() {
 jclo.parse(new String[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithIllegalArg() {
 String[] args = {"--illegalArg"};
 jclo.parse(args);
 }

 @Test
 public void testParseWithBooleanOptionTrue() {
 String[] args = {"--testBoolean=true"};
 jclo.parse(args);
 assertTrue(jclo.getBoolean("testBoolean"));
 }

 @Test
 public void testParseWithBooleanOptionFalse() {
 String[] args = {"--testBoolean"};
 jclo.parse(args);
 assertFalse(jclo.getBoolean("testBoolean"));
 }

 @Test
 public void testParseWithIntegerOption() {
 String[] args = {"--testInt=42"};
 jclo.parse(args);
 assertEquals(42, jclo.getInt("testInt"));
 }

 @Test
 public void testParseWithStringOption() {
 String[] args = {"--testString=test"};
 jclo.parse(args);
 assertEquals("test", jclo.getString("testString"));
 }

 @Test
 public void testParseWithArrayOption() {
 String[] args = {"--testArray=1"};
 jclo.parse(args);
 assertEquals(new int[]{1}, jclo.getInts("testArray"));
 }

 @Test
 public void testParseWithArrayOptionAndMultipleValues() {
 String[] args = {"--testArray=1,2,3"};
 jclo.parse(args);
 assertArrayEquals(new int[]{1, 2, 3}, jclo.getInts("testArray"));
 }

 @Test
 public void testParseWithPrefixAndAliases() {
 String[] args = {"--prefix1=--testBoolean:testBoolean=true", "--prefix1=--testInt:testInt=42", "--prefix1=--testString:testString=test"};
 jclo.parse(args);
 assertTrue(jclo.getBoolean("testBoolean"));
 assertEquals(42, jclo.getInt("testInt"));
 assertEquals("test", jclo.getString("testString"));
 }

 @Test
 public void testParseWithPrefixAndAliasesAndAdditional() {
 String[] args = {"--prefix1=--testBoolean:testBoolean=true", "--prefix1=--testInt:testInt=42", "--prefix1=--testString:testString=test", "--prefix1=--additional:array=1,2,3"};
 JCLO jcloWithAdditional = new JCLO("prefix1", testObject, new String[][]{{"alias1", "alias2"}, {"additional", "array"}});
 jcloWithAdditional.parse(args);
 assertEquals(new int[]{1, 2, 3}, jcloWithAdditional.getInts("additional"));
 }
}

 class TestClass {
 private boolean testBoolean;
 private int testInt;
 private String testString;

 private static final int inaccessibleField;

 static {
 try {
 inaccessibleField = JCLO.getField("inaccessibleField").getInt(null);
 } catch (Exception e) {
 inaccessibleField = 0;
 }
 }
 }