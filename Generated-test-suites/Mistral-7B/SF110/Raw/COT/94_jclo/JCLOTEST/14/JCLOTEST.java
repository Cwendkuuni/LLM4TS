package edu.mscd.cs.jclo;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.Test;

public class JCLOTest {

 private Object object;
 private JCLO jclo;

 @Before
 public void setup() {
 Object testObject = new TestObject();
 jclo = new JCLO(testObject, null);
 }

 @Test
 public void testJCLOConstructorWithObject() {
 jclo = new JCLO(new Object());
 assertNotNull(jclo.fields);
 assertNotNull(jclo.object);
 }

 @Test
 public void testJCLOConstructorWithObjectAndAliases() {
 jclo = new JCLO(new Object(), new String[][]{{"a", "aliasA"}, {"b", "aliasB"}});
 assertNotNull(jclo.fields);
 assertNotNull(jclo.object);
 assertNotNull(jclo.aliases);
 }

 @Test
 public void testJCLOConstructorWithPrefixAndObject() {
 jclo = new JCLO("prefix_", new Object());
 assertNotNull(jclo.prefix);
 assertNotNull(jclo.object);
 }

 @Test
 public void testJCLOConstructorWithPrefixAndObjectAndAliases() {
 jclo = new JCLO("prefix_", new Object(), new String[][]{{"a", "aliasA"}, {"b", "aliasB"}});
 assertNotNull(jclo.prefix);
 assertNotNull(jclo.object);
 assertNotNull(jclo.aliases);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJCLOConstructorWithInvalidPrefix() {
 jclo = new JCLO("prefix123_", new Object());
 fail("IllegalArgumentException not thrown");
 }

 @Test
 public void testGetValueWithValidKey() {
 Field testField = TestObject.class.getDeclaredField("testField");
 testField.setAccessible(true);
 Object testValue = new Object();
 jclo.setObject(testField, testValue);
 String key = "testField";
 Object expectedValue = testValue;
 Object actualValue = jclo.getValue(key);
 assertEquals(expectedValue, actualValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetValueWithInvalidKey() {
 String key = "invalidKey";
 Object actualValue = jclo.getValue(key);
 fail("IllegalArgumentException not thrown");
 }

 @Test
 public void testSetObjectWithValidFieldAndValue() {
 Field testField = TestObject.class.getDeclaredField("testField");
 testField.setAccessible(true);
 Object testValue = new Object();
 jclo.setObject(testField, testValue);
 Object expectedValue = testValue;
 Object actualValue = jclo.getValue(testField.getName());
 assertEquals(expectedValue, actualValue);
 }

 @Test(expected = IllegalAccessException.class)
 public void testGetObjectWithInvalidField() {
 String key = "invalidField";
 Object actualValue = jclo.getObject(jclo.getField(key));
 fail("IllegalAccessException not thrown");
 }

 @Test
 public void testGetUsageTypeWithBooleanType() {
 Class booleanType = boolean.class;
 String expectedUsageType = "[=boolean]";
 String actualUsageType = jclo.getUsageType(booleanType);
 assertEquals(expectedUsageType, actualUsageType);
 }

 @Test
 public void testGetUsageTypeWithArrayType() {
 Class intArrayType = int[].class;
 String expectedUsageType = "int...";
 String actualUsageType = jclo.getUsageType(intArrayType);
 assertEquals(expectedUsageType, actualUsageType);
 }

 @Test
 public void testToString() {
 TestObject testObject = new TestObject();
 jclo.setObject(TestObject.class.getDeclaredField("testField"), testObject);
 String expectedString = "int[]: testField = [TestObject@12345678]";
 String actualString = jclo.toString();
 assertEquals(expectedString, actualString);
 }

 @Test(expected = NullPointerException.class)
 public void testGetObjectWithNullField() {
 Field nullField = null;
 Object actualValue = jclo.getObject(nullField);
 fail("NullPointerException not thrown");
 }

 @Test
 public void testParseWithValidArguments() {
 String[] args = {"--prefix=prefix_", "--boolean=true", "--int=5", "--string=test"};
 jclo.parse(args);
 assertArrayEquals(new Object[]{Boolean.TRUE, 5, "test"}, jclo.getValues());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidArgument() {
 String[] args = {"--prefix=prefix_", "--boolean=true", "--int=5", "--invalid"};
 jclo.parse(args);
 fail("IllegalArgumentException not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithMissingValue() {
 String[] args = {"--prefix=prefix_", "--boolean=", "--int=5", "--string=test"};
 jclo.parse(args);
 fail("IllegalArgumentException not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidBooleanValue() {
 String[] args = {"--prefix=prefix_", "--boolean=false,true", "--int=5", "--string=test"};
 jclo.parse(args);
 fail("IllegalArgumentException not thrown");
 }

 @Test
 public void testParseWithAliases() {
 String[] args = {"--prefix=prefix_", "--boolean=--flag", "--flag=true", "--int=5", "--alias=test"};
 jclo.parse(args);
 assertArrayEquals(new Object[]{Boolean.TRUE, 5, "test"}, jclo.getValues());
 }

 @Test
 public void testParseWithAdditionalArguments() {
 String[] args = {"--prefix=prefix_", "--boolean=true", "--int=5", "--string=test", "--additional=value1,value2"};
 jclo.parse(args);
 assertArrayEquals(new Object[]{Boolean.TRUE, 5, "test"}, jclo.getValues());
 assertArrayEquals(new String[]{"value1", "value2"}, (String[])jclo.getValue("additional"));
 }

 @Test
 public void testParseWithLongArray() {
 String[] args = {"--prefix=prefix_", "--intArray=1,2,3,4,5"};
 jclo.parse(args);
 assertArrayEquals(new int[]{1, 2, 3, 4, 5}, (int[])jclo.getValue("intArray"));
 }

 @Test
 public void testParseWithFloatArray() {
 String[] args = {"--prefix=prefix_", "--floatArray=1.1,2.2,3.3,4.4,5.5"};
 jclo.parse(args);
 assertArrayEquals(new float[]{1.1f, 2.2f, 3.3f, 4.4f, 5.5f}, (float[])jclo.getValue("floatArray"));
 }

 @Test
 public void testParseWithDoubleArray() {
 String[] args = {"--prefix=prefix_", "--doubleArray=1.1,2.2,3.3,4.4,5.5"};
 jclo.parse(args);
 assertArrayEquals(new double[]{1.1, 2.2, 3.3, 4.4, 5.5}, (double[])jclo.getValue("doubleArray"));
 }

 @Test
 public void testParseWithLongArrayAndAdditionalArguments() {
 String[] args = {"--prefix=prefix_", "--intArray=1,2,3,4,5", "--additional=value1,value2"};
 jclo.parse(args);
 assertArrayEquals(new int[]{1, 2, 3, 4, 5}, (int[])jclo.getValue("intArray"));
 assertArrayEquals(new String[]{"value1", "value2"}, (String[])jclo.getValue("additional"));
 }

 @Test
 public void testParseWithFloatArrayAndAdditionalArguments() {
 String[] args = {"--prefix=prefix_", "--floatArray=1.1,2.2,3.3,4.4,5.5", "--additional=value1,value2"};
 jclo.parse(args);
 assertArrayEquals(new float[]{1.1f, 2.2f, 3.3f, 4.4f, 5.5f}, (float[])jclo.getValue("floatArray"));
 assertArrayEquals(new String[]{"value1", "value2"}, (String[])jclo.getValue("additional"));
 }

 @Test
 public void testParseWithDoubleArrayAndAdditionalArguments() {
 String[] args = {"--prefix=prefix_", "--doubleArray=1.1,2.2,3.3,4.4,5.5", "--additional=value1,value2"};
 jclo.parse(args);
 assertArrayEquals(new double[]{1.1, 2.2, 3.3, 4.4, 5.5}, (double[])jclo.getValue("doubleArray"));
 assertArrayEquals(new String[]{"value1", "value2"}, (String[])jclo.getValue("additional"));
 }

 private Object[] getValues() {
 Object[] values = new Object[jclo.fields.length];
 for (int i = 0; i < jclo.fields.length; ++i) {
 final Field field = jclo.fields[i];
 values[i] = jclo.getValue(field.getName());
 }
 return values;
 }
}