package edu.mscd.cs.jclo;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.Test;

public class JCLOTest {

 private JCLO jclo;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 jclo = new JCLO(new Object());
 }

 @Test
 public void testConstructorWithObject() {
 jclo = new JCLO(new Object());
 assertNotNull(jclo.getField("object"));
 }

 @Test
 public void testConstructorWithObjectAndAliases() {
 jclo = new JCLO(new Object(), new String[][]{{"alias1", "value1"}, {"alias2", "value2"}});
 assertNotNull(jclo.getField("alias1"));
 assertNotNull(jclo.getField("alias2"));
 assertEquals("value1", jclo.getValue("alias1"));
 assertEquals("value2", jclo.getValue("alias2"));
 }

 @Test
 public void testConstructorWithPrefixAndObject() {
 jclo = new JCLO("prefix", new Object());
 assertNotNull(jclo.getField("prefix"));
 assertEquals("prefix", jclo.getValue("prefix"));
 }

 @Test
 public void testConstructorWithPrefixAndObjectAndAliases() {
 jclo = new JCLO("prefix", new Object(), new String[][]{{"alias1", "value1"}, {"alias2", "value2"}});
 assertNotNull(jclo.getField("prefix"));
 assertEquals("prefix", jclo.getValue("prefix"));
 assertNotNull(jclo.getField("alias1"));
 assertNotNull(jclo.getField("alias2"));
 assertEquals("value1", jclo.getValue("alias1"));
 assertEquals("value2", jclo.getValue("alias2"));
 }

 @Test
 public void testGetObject() {
 final Field field = jclo.getField("object");
 Object original = jclo.getObject(field);
 jclo.setObject(field, new Object());
 Object newObject = jclo.getObject(field);
 assertNotSame(original, newObject);
 }

 @Test
 public void testGetField() {
 String key = "field1";
 Field expectedField = jclo.getClass().getDeclaredField(key);
 Field actualField = jclo.getField(key);
 assertEquals(expectedField, actualField);
 }

 @Test(expected = NullPointerException.class)
 public void testGetFieldWithNullKey() {
 jclo.getField(null);
 }

 @Test
 public void testGetValueWithExistingKey() {
 String key = "object";
 Object expectedValue = new Object();
 Object actualValue = jclo.getValue(key);
 assertSame(expectedValue, actualValue);
 }

 @Test
 public void testGetValueWithNonExistingKey() {
 String key = "nonExistingKey";
 Object actualValue = jclo.getValue(key);
 assertNull(actualValue);
 }

 @Test
 public void testSetObject() {
 final Field field = jclo.getField("object");
 Object original = jclo.getObject(field);
 jclo.setObject(field, new Object());
 Object newObject = jclo.getObject(field);
 assertNotSame(original, newObject);
 }

 @Test
 public void testGetArrayType() {
 Class intArrayType = int[].class;
 assertEquals("int[]", jclo.getArrayType(intArrayType));
 }

 @Test
 public void testToString() {
 String expectedToString = "class edu.mscd.cs.jclo.JCLO{\nobject: java.lang.Object = [Ljava.lang.Object;: \n}";
 String actualToString = jclo.toString();
 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testUsage() {
 String expectedUsage = "";
 // Add test cases for each field in the JCLO class
 String testUsage = jclo.usage();
 assertEquals(expectedUsage, testUsage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithUnknownOption() {
 String[] args = new String[]{"--unknownOption"};
 jclo.parse(args);
 }

 @Test
 public void testParseWithBooleanOption() {
 String[] args = new String[]{"--booleanOption"};
 jclo.parse(args);
 assertEquals(true, jclo.getBoolean("booleanOption"));
 }

 @Test
 public void testParseWithByteOption() {
 String[] args = new String[]{"--byteOption=123"};
 jclo.parse(args);
 assertEquals((byte)123, jclo.getByte("byteOption"));
 }

 @Test
 public void testParseWithCharOption() {
 String[] args = new String[]{"--charOption=a"};
 jclo.parse(args);
 assertEquals((char)'a', jclo.getChar("charOption"));
 }

 @Test
 public void testParseWithShortOption() {
 String[] args = new String[]{"--shortOption=1234"};
 jclo.parse(args);
 assertEquals((short)1234, jclo.getShort("shortOption"));
 }

 @Test
 public void testParseWithIntOption() {
 String[] args = new String[]{"--intOption=12345"};
 jclo.parse(args);
 assertEquals(12345, jclo.getInt("intOption"));
 }

 @Test
 public void testParseWithFloatOption() {
 String[] args = new String[]{"--floatOption=123.45"};
 jclo.parse(args);
 assertEquals(123.45f, jclo.getFloat("floatOption"), 0.001f);
 }

 @Test
 public void testParseWithDoubleOption() {
 String[] args = new String[]{"--doubleOption=123.456"};
 jclo.parse(args);
 assertEquals(123.456, jclo.getDouble("doubleOption"), 0.0001);
 }

 @Test
 public void testParseWithLongOption() {
 String[] args = new String[]{"--longOption=1234567890"};
 jclo.parse(args);
 assertEquals(1234567890L, jclo.getLong("longOption"));
 }

 @Test
 public void testParseWithStringOption() {
 String[] args = new String[]{"--stringOption=Hello World"};
 jclo.parse(args);
 assertEquals("Hello World", jclo.getString("stringOption"));
 }

 @Test
 public void testParseWithBooleanOptionAndValue() {
 String[] args = new String[]{"--booleanOption=true"};
 jclo.parse(args);
 assertEquals(true, jclo.getBoolean("booleanOption"));
 }

 @Test
 public void testParseWithByteOptionAndValue() {
 String[] args = new String[]{"--byteOption=123=10"};
 jclo.parse(args);
 assertEquals((byte)10, jclo.getByte("byteOption"));
 }

 @Test
 public void testParseWithCharOptionAndValue() {
 String[] args = new String[]{"--charOption=a=z"};
 jclo.parse(args);
 assertEquals((char)'z', jclo.getChar("charOption"));
 }

 @Test
 public void testParseWithShortOptionAndValue() {
 String[] args = new String[]{"--shortOption=1234=300"};
 jclo.parse(args);
 assertEquals((short)300, jclo.getShort("shortOption"));
 }

 @Test
 public void testParseWithIntOptionAndValue() {
 String[] args = new String[]{"--intOption=12345=5000"};
 jclo.parse(args);
 assertEquals(5000, jclo.getInt("intOption"));
 }

 @Test
 public void testParseWithFloatOptionAndValue() {
 String[] args = new String[]{"--floatOption=123.45=3.14159"};
 jclo.parse(args);
 assertEquals(3.14159f, jclo.getFloat("floatOption"), 0.001f);
 }

 @Test
 public void testParseWithDoubleOptionAndValue() {
 String[] args = new String[]{"--doubleOption=123.456=3.1415926536"};
 jclo.parse(args);
 assertEquals(3.1415926536d, jclo.getDouble("doubleOption"), 0.00001);
 }

 @Test
 public void testParseWithLongOptionAndValue() {
 String[] args = new String[]{"--longOption=1234567890=9876543210"};
 jclo.parse(args);
 assertEquals(9876543210L, jclo.getLong("longOption"));
 }

 @Test
 public void testParseWithStringOptionAndValue() {
 String[] args = new String[]{"--stringOption=Hello World=Goodbye Universe"};
 jclo.parse(args);
 assertEquals("Goodbye Universe", jclo.getString("stringOption"));
 }

 @Test
 public void testParseWithMultipleOptions() {
 String[] args = new String[]{"--booleanOption", "--byteOption=123", "--charOption=a", "--shortOption=1234", "--intOption=12345", "--floatOption=123.45", "--doubleOption=123.456", "--longOption=1234567890"};
 jclo.parse(args);
 assertEquals(true, jclo.getBoolean("booleanOption"));
 assertEquals((byte)123, jclo.getByte("byteOption"));
 assertEquals((char)'a', jclo.getChar("charOption"));
 assertEquals((short)1234, jclo.getShort("shortOption"));
 assertEquals(12345, jclo.getInt("intOption"));
 assertEquals(123.45f, jclo.getFloat("floatOption"), 0.001f);
 assertEquals(123.456, jclo.getDouble("doubleOption"), 0.0001);
 assertEquals(1234567890L, jclo.getLong("longOption"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithUnknownValueType() {
 String[] args = new String[]{"--unknownOption=unknownValue"};
 jclo.parse(args);
 }

 @Test
 public void testParseWithIncorrectNumberOfArguments() {
 String[] args = new String[]{"--booleanOption=true", "--intOption=12345"};
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithMissingEqualsSignForBooleanOption() {
 String[] args = new String[]{"--booleanOption=trueOption"};
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithMissingEqualsSignForValueOption() {
 String[] args = new String[]{"--intOption=intOption=5"};
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithMissingValueForBooleanOption() {
 String[] args = new String[]{"--booleanOption="};
 jclo.parse(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithMissingValueForValueOption() {
 String[] args = new String[]{"--intOption="};
 jclo.parse(args);
 }

 @Test
 public void testParseWithAliases() {
 jclo = new JCLO(new Object(), new String[][]{{"alias1", "alias2"}, {"alias3", "alias4"}});
 String[] args = new String[]{"--alias1=value1", "--alias2=value2", "--alias3=value3", "--alias4=value4"};
 jclo.parse(args);
 assertEquals("value1", jclo.getValue("alias1"));
 assertEquals("value2", jclo.getValue("alias2"));
 assertEquals("value3", jclo.getValue("alias3"));
 assertEquals("value4", jclo.getValue("alias4"));
 }
}