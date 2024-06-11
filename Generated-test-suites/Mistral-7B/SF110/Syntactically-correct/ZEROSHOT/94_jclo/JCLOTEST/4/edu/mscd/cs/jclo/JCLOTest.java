package edu.mscd.cs.jclo;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JCLOTest {

 private JCLO jclo;
 private Object testObject;

 @Before
 public void setUp() throws Exception {
 testObject = new TestClass();
 jclo = new JCLO(testObject);
 }

 @Test
 public void testConstructorWithoutArguments() {
 JCLO jcloWithoutPrefix = new JCLO(testObject);
 assertEquals(0, jcloWithoutPrefix.getFields().length);
 }

 @Test
 public void testConstructorWithObjectArgument() {
 JCLO jcloWithObjectArgument = new JCLO(testObject);
 assertEquals(testObject.getClass().getDeclaredFields().length, jcloWithObjectArgument.getFields().length);
 }

 @Test
 public void testConstructorWithPrefixAndObjectArgument() {
 JCLO jcloWithPrefixAndObjectArgument = new JCLO("prefix", testObject);
 assertNotSame(jcloWithObjectArgument, jcloWithPrefixAndObjectArgument);
 }

 @Test
 public void testConstructorWithPrefixObjectAndAliasesArgument() {
 String[][] aliases = {{ "@", "alias" }, { "a", "alias2" }};
 JCLO jcloWithPrefixObjectAndAliases = new JCLO("prefix", testObject, aliases);
 assertNotSame(jcloWithObjectArgument, jcloWithPrefixObjectAndAliases);
 }

 @Test
 public void testGetValue() {
 Field testField = testObject.getClass().getDeclaredField("testField");
 testField.setAccessible(true);
 Object testValue = new Integer(42);
 testField.set(testObject, testValue);
 JCLO jcloWithTestField = new JCLO(testObject);
 JCLO jcloWithTestFieldAndValue = new JCLO(testObject);
 jcloWithTestFieldAndValue.parse("--testField=43");
 assertEquals(testValue, jcloWithTestFieldAndValue.getValue("testField"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetValueWithNotFoundField() {
 JCLO jclo = new JCLO(testObject);
 assertNull(jclo.getValue("nonExistentField"));
 }

 @Test
 public void testSetObject() {
 Field testField = testObject.getClass().getDeclaredField("testField");
 testField.setAccessible(true);
 Object testValue = new Integer(42);
 JCLO jcloWithTestField = new JCLO(testObject);
 jcloWithTestField.parse("--testField=43");
 jcloWithTestField.setObject(testField, new Integer(45));
 assertEquals(new Integer(45), jcloWithTestField.getValue("testField"));
 }

 @Test
 public void testSetObjectWithNull() {
 Field testField = testObject.getClass().getDeclaredField("testField");
 testField.setAccessible(true);
 JCLO jcloWithTestField = new JCLO(testObject);
 try {
 jcloWithTestField.setObject(testField, null);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testToString() {
 JCLO jcloWithFields = new JCLO(testObject);
 String expectedToString = "java.lang.Integer: testField = 42";
 assertEquals(expectedToString, jcloWithFields.toString());
 }

 @Test
 public void testUsage() {
 JCLO jclo = new JCLO(testObject);
 String expectedUsage = "--testField=Integer testField: \n";
 assertEquals(expectedUsage, jclo.usage());
 }

 @Test
 public void testParse() {
 JCLO jclo = new JCLO(testObject);
 String[] args = {"--testField=43"};
 jclo.parse(args);
 assertEquals(new Integer(43), jclo.getValue("testField"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidArgument() {
 JCLO jclo = new JCLO(testObject);
 String[] args = {"--nonExistentField=value"};
 jclo.parse(args);
 fail();
 }

 @Test
 public void testParseWithAliases() {
 JCLO jclo = new JCLO(testObject, null, new String[][]{{"@", "alias"}, {"a", "alias2"}});
 String[] args = {"--@=value1", "--a=value2"};
 jclo.parse(args);
 assertEquals("value1", jclo.getValue("@"));
 assertEquals("value2", jclo.getValue("a"));
 }

 @Test
 public void testGetArrayType() {
 assertEquals("int[]", jclo.getArrayType(int[].class));
 }
}

class TestClass {
 private int testField;
}