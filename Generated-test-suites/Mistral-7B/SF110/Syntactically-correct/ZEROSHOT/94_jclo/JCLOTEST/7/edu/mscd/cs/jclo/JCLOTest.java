package edu.mscd.cs.jclo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class JCLOTest {

 private Object testObject;
 private JCLO jclo;

 @Before
 public void setUp() throws IllegalAccessException, InstantiationException {
 testObject = new TestObject();
 jclo = new JCLO(testObject);
 }

 @Test
 public void testConstructorWithObjectOnly() {
 JCLO jclo1 = new JCLO(testObject);
 assertEquals(testObject.getClass(), jclo1.getObjectClass());
 assertEquals(0, jclo1.getFields().length);
 }

 @Test
 public void testConstructorWithObjectAndAliases() {
 String[][] aliases = new String[1][2];
 aliases[0][0] = "alias1";
 aliases[0][1] = "alias2";
 JCLO jclo2 = new JCLO(testObject, aliases);
 assertEquals(testObject.getClass(), jclo2.getObjectClass());
 assertEquals(2, jclo2.getFields().length);
 assertEquals("alias1", jclo2.getAlias("alias1"));
 assertEquals("alias2", jclo2.getAlias("alias2"));
 }

 @Test
 public void testConstructorWithPrefixAndObject() {
 String prefix = "prefix_";
 JCLO jclo3 = new JCLO(prefix, testObject);
 assertEquals(prefix, jclo3.getPrefix());
 assertEquals(testObject.getClass(), jclo3.getObjectClass());
 assertEquals(testObject.getClass().getDeclaredFields().length, jclo3.getFields().length);
 }

 @Test
 public void testConstructorWithPrefixAndObjectAndAliases() {
 String prefix = "prefix_";
 String[][] aliases = new String[1][2];
 aliases[0][0] = "alias1";
 aliases[0][1] = "alias2";
 JCLO jclo4 = new JCLO(prefix, testObject, aliases);
 assertEquals(prefix, jclo4.getPrefix());
 assertEquals(testObject.getClass(), jclo4.getObjectClass());
 assertEquals(testObject.getClass().getDeclaredFields().length, jclo4.getFields().length);
 assertEquals("alias1", jclo4.getAlias("alias1"));
 assertEquals("alias2", jclo4.getValue("alias2"));
 }

 @Test
 public void testGetObject() throws IllegalAccessException {
 Field field = testObject.getClass().getDeclaredField("field1");
 field.setAccessible(true);
 Object fieldValue = field.get(testObject);
 JCLO jclo5 = new JCLO(testObject);
 assertEquals(fieldValue, jclo5.getValue("field1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetObjectWithInvalidKey() {
 JCLO jclo6 = new JCLO(testObject);
 jclo6.getValue("invalidKey");
 }

 @Test
 public void testSetObject() throws IllegalAccessException {
 Field field = testObject.getClass().getDeclaredField("field1");
 field.setAccessible(true);
 Object fieldValue = new Integer(5);
 JCLO jclo7 = new JCLO(testObject);
 jclo7.setObject(field, fieldValue);
 assertEquals(fieldValue, jclo7.getValue("field1"));
 }

 @Test
 public void testToString() {
 JCLO jclo8 = new JCLO(testObject);
 String toString = jclo8.toString();
 String expectedToString = "class java.lang.Object:\n" +
         "field1 = 0\n";
 assertTrue(toString.contains(expectedToString));
 }

 @Test
 public void testUsage() {
 JCLO jclo9 = new JCLO(testObject);
 String usage = jclo9.usage();
 String expectedUsage = "--prefix=prefix_--field1=--field2=--\n" +
         "--field1=VALUE1\n" +
         "--field2=VALUE2\n";
 assertTrue(usage.contains(expectedUsage));
 }

 @Test
 public void testParse() {
 String[] args = new String[]{"--prefix=prefix_", "--field1=VALUE1"};
 JCLO jclo10 = new JCLO(testObject);
 jclo10.parse(args);
 assertEquals("VALUE1", jclo10.getValue("field1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidOption() {
 String[] args = new String[]{"--prefix=prefix_", "--invalidOption=VALUE"};
 JCLO jclo11 = new JCLO(testObject);
 jclo11.parse(args);
 }

 @Test
 public void testGetArrayType() {
 JCLO jclo12 = new JCLO(testObject);
 assertEquals("int[]", jclo12.getArrayType(int[].class));
 }

 @After
 public void tearDown() {
 testObject = null;
 jclo = null;
 }
}

class TestObject {
 private int field1;
 private String field2;

 private static final int field3 = 5;
 private static final String field4 = "test";

 private TestObject() {}
}