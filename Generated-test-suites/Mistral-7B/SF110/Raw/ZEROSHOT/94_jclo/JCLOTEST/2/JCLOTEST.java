package edu.mscd.cs.jclo;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

public class JCLOTest {

 private JCLO jclo;
 private Object objectUnderTest;

 @Before
 public void setUp() {
 objectUnderTest = new MyTestClass();
 jclo = new JCLO(objectUnderTest);
 }

 @Test
 public void testConstructorWithObject() {
 JCLO jclo1 = new JCLO(objectUnderTest);
 assertEquals(objectUnderTest, jclo1.getObject());
 }

 @Test
 public void testConstructorWithObjectAndAliases() {
 String[][] aliases = new String[1][2];
 aliases[0] = new String[]{"alias1", "alias2"};
 JCLO jclo2 = new JCLO(objectUnderTest, aliases);
 assertArrayEquals(aliases, jclo2.getAliases());
 }

 @Test
 public void testConstructorWithPrefixAndObject() {
 String prefix = "prefix_";
 JCLO jclo3 = new JCLO(prefix, objectUnderTest);
 assertEquals(prefix, jclo3.getPrefix());
 }

 @Test
 public void testConstructorWithPrefixAndObjectAndAliases() {
 String prefix = "prefix_";
 String[][] aliases = new String[1][2];
 aliases[0] = new String[]{"alias1", "alias2"};
 JCLO jclo4 = new JCLO(prefix, objectUnderTest, aliases);
 assertEquals(prefix, jclo4.getPrefix());
 assertArrayEquals(aliases, jclo4.getAliases());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetValueWithNullKey() {
 jclo.setValue("nullKey", null); // Set null value before testing
 assertNull(jclo.getValue(null));
 }

 @Test
 public void testGetValue() {
 String key = "field1";
 Object expectedValue = "expectedValue";
 jclo.setValue(key, expectedValue);
 Object actualValue = jclo.getValue(key);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testSetValueAndGetValueForDifferentTypes() {
 String key = "field1";
 int expectedIntValue = 42;
 jclo.setValue(key, expectedIntValue);
 int actualIntValue = (int) jclo.getValue(key);
 assertEquals(expectedIntValue, actualIntValue);

 float expectedFloatValue = 3.14F;
 jclo.setValue(key, expectedFloatValue);
 float actualFloatValue = (float) jclo.getValue(key);
 assertEquals(expectedFloatValue, actualFloatValue, 0.001f);

 boolean expectedBooleanValue = true;
 jclo.setValue(key, expectedBooleanValue);
 boolean actualBooleanValue = (boolean) jclo.getValue(key);
 assertTrue(actualBooleanValue);
 }

 @Test
 public void testUsage() {
 String expectedUsage = "-prefix_field1: int = field1\n" +
 "-prefix_field2: float = field2\n" +
 "--field3: boolean[] = field3\n" +
 "--field4: String[] = field4\n";
 String actualUsage = jclo.usage();
 assertEquals(expectedUsage, actualUsage);
 }

 @Test
 public void testParse() {
 String[] args = new String[]{"--prefix", "-field1:1", "-field2:3.14", "-field3:true", "--field3:--boolean", "--field4:testString"};
 jclo.parse(args);
 assertArrayEquals(new String[]{"field1", "1"}, jclo.getFieldsWithValues());
 assertArrayEquals(new Object[]{1, 3.14F, true, new String[]{"boolean"}, new String[]{"testString"}}, jclo.getValues());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidArgument() {
 String[] args = new String[]{"--prefix", "-field1:1", "-field2:3.14", "--field3:invalidArgument"};
 jclo.parse(args);
 fail("An IllegalArgumentException should be thrown");
 }

 @Test
 public void testGetFieldsWithValues() {
 String[] expectedFieldsWithValues = new String[]{"field1:1", "field2:3.14", "field3:[true]", "field4:[testString]"};
 assertArrayEquals(expectedFieldsWithValues, jclo.getFieldsWithValues());
 }

 @Test
 public void testGetValues() {
 Object[] expectedValues = new Object[]{1, 3.14F, new boolean[0], new String[]{"boolean"}, new String[]{"testString"}};
 assertArrayEquals(expectedValues, jclo.getValues());
 }
}

 class MyTestClass {
 private int field1;
 private float field2;
 private boolean[] field3;
 private String[] field4;
}