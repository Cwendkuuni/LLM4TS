package edu.mscd.cs.jclo;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.Arrays;

public class JCLOTest {

 private JCLO jclo;

 @Test
 public void testJCLOConstructor() {
 Object object = new Object();
 JCLO jclo1 = new JCLO(object);
 JCLO jclo2 = new JCLO(object, new String[][] {{ "alias1", "value1" }, { "alias2", "value2" }});
 JCLO jclo3 = new JCLO("prefix", object);
 JCLO jclo4 = new JCLO("prefix", object, new String[][] {{ "alias1", "value1" }, { "alias2", "value2" }});

 assertNotNull(jclo1);
 assertNotNull(jclo2);
 assertNotNull(jclo3);
 assertNotNull(jclo4);
 }

 @Test
 public void testGetObject() {
 Object object = new Object();
 JCLO jclo = new JCLO(object);
 Field field = jclo.getClass().getDeclaredFields()[0];
 Object expectedValue = new String("expectedValue");
 try {
 field.set(jclo, expectedValue);
 } catch (IllegalAccessException e) {
 e.printStackTrace();
 }
 Object actualValue = jclo.getObject(field);
 assertEquals(expectedValue, actualValue);
 }

 @Test(expected = NullPointerException.class)
 public void testGetObjectNullField() {
 Object object = new Object();
 JCLO jclo = new JCLO(object);
 Field nullField = null;
 jclo.getObject(nullField);
 }

 @Test
 public void testGetField() {
 Object object = new Object() {
 String field1 = "field1";
 String field2 = "field2";
 };
 JCLO jclo = new JCLO(object);
 Field field1 = jclo.getField("field1");
 Field field2 = jclo.getField("field2");
 Field nullField = jclo.getField("nonExistentField");

 assertNotNull(field1);
 assertNotNull(field2);
 assertNull(nullField);
 }

 @Test(expected = NullPointerException.class)
 public void testGetFieldNullObject() {
 JCLO jclo = new JCLO(null);
 jclo.getField("field");
 }

 @Test
 public void testGetValue() {
 Object object = new Object() {
 String field1 = "field1";
 String field2 = "field2";
 String field3 = "field3";
 int field4 = 42;
 int[] field5 = { 1, 2, 3 };
 };
 JCLO jclo = new JCLO(object);

 String value1 = jclo.getValue("field1");
 String value2 = jclo.getValue("field2");
 String value3 = jclo.getValue("field3");
 int value4 = jclo.getIntValue("field4");
 int[] value5 = jclo.getIntsValue("field5");

 assertEquals("field1", value1);
 assertEquals("field2", value2);
 assertEquals("field3", value3);
 assertEquals(42, value4);
 assertArrayEquals(new int[] { 1, 2, 3 }, value5);
 }

 @Test(expected = NullPointerException.class)
 public void testGetValueNullKey() {
 Object object = new Object();
 JCLO jclo = new JCLO(object);
 jclo.getValue(null);
 }

 @Test
 public void testSetObject() {
 Object object = new Object();
 JCLO jclo = new JCLO(object);
 Field field = jclo.getClass().getDeclaredFields()[0];
 Object newValue = new String("newValue");
 try {
 jclo.setObject(field, newValue);
 } catch (IllegalAccessException e) {
 e.printStackTrace();
 }
 String actualValue = (String) jclo.getValue("field1");
 assertEquals(newValue, actualValue);
 }

 @Test(expected = NullPointerException.class)
 public void testSetObjectNullField() {
 Object object = new Object();
 JCLO jclo = new JCLO(object);
 Field nullField = null;
 try {
 jclo.setObject(nullField, new Object());
 } catch (IllegalAccessException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testParse() {
 String[] args = new String[] { "--double-dash", "-myBoolean=true", "-myByte=-127", "-myShort=-32767",
 "-myInt=-2147483647", "-myLong=-9223372036854775807", "-myFloat=-3.14", "-myDouble=-1.0",
 "-myString=testString", "-myChar=c", "-myArray=1,2,3", "-alias1=value1", "-alias2=value2",
 "-prefix_field1=prefixValue1", "--prefix_field2=prefixValue2", "--version" };

 JCLO jclo = new JCLO(new Object());
 jclo.parse(args);

 boolean myBoolean = jclo.getBoolean("myBoolean");
 byte myByte = jclo.getByte("myByte");
 short myShort = jclo.getShort("myShort");
 int myInt = jclo.getInt("myInt");
 long myLong = jclo.getLong("myLong");
 float myFloat = jclo.getFloat("myFloat");
 double myDouble = jclo.getDouble("myDouble");
 String myString = jclo.getString("myString");
 char myChar = jclo.getChar("myChar");
 Object[] myArray = jclo.getObject("myArray");
 assertArrayEquals(new Integer[] { -127, -32767, -2147483647, -9223372036854775807, -3, -1, null }, myArray);
 String[] myAlias = jclo.getString("alias1");
 assertEquals("value1", myAlias[0]);
 String[] myPrefixField = jclo.getString("prefix_field1");
 assertEquals("prefixValue1", myPrefixField[0]);
 assertFalse(jclo.hasEquals());
 assertFalse(jclo.doubleDashes());
 }

 @Test
 public void testParseWithEquals() {
 String[] args = new String[] { "--double-dash=myBoolean:true", "-myByte:=-127", "-myShort:=-32767",
 "-myInt:=-2147483647", "-myLong:=-9223372036854775807", "-myFloat:=-3.14", "-myDouble:=-1.0",
 "-myString:=testString", "-myChar:=c", "-myArray:=1,2,3", "-alias1:=value1", "-alias2:=value2",
 "-prefix_field1:=prefixValue1", "--prefix_field2:=prefixValue2", "--version" };

 JCLO jclo = new JCLO(new Object());
 jclo.parse(args);

 boolean myBoolean = jclo.getBoolean("myBoolean");
 byte myByte = jclo.getByte("myByte");
 short myShort = jclo.getShort("myShort");
 int myInt = jclo.getInt("myInt");
 long myLong = jclo.getLong("myLong");
 float myFloat = jclo.getFloat("myFloat");
 double myDouble = jclo.getDouble("myDouble");
 String myString = jclo.getString("myString");
 char myChar = jclo.getChar("myChar");
 Object[] myArray = jclo.getObject("myArray");
 assertArrayEquals(new Integer[] { -127, -32767, -2147483647, -9223372036854775807, -3, -1, null }, myArray);
 String[] myAlias = jclo.getString("alias1");
 assertEquals("value1", myAlias[0]);
 String[] myPrefixField = jclo.getString("prefix_field1");
 assertEquals("prefixValue1", myPrefixField[0]);
 assertTrue(jclo.hasEquals());
 assertFalse(jclo.doubleDashes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidOption() {
 String[] args = new String[] { "--double-dash=myBoolean:true", "-myBoolean:=true", "-myBoolean=true",
 "-myBoolean:true=", "myBoolean:true" };

 JCLO jclo = new JCLO(new Object());
 jclo.parse(args);
 }

 @Test(expected = NullPointerException.class)
 public void testGetValueNullObject() {
 Object object = null;
 JCLO jclo = new JCLO(object);
 jclo.getValue("field");
 }

 @Test
 public void testToString() {
 Object object = new Object() {
 String field1 = "field1";
 String field2 = "field2";
 String field3 = "field3";
 int field4 = 42;
 int[] field5 = { 1, 2, 3 };
 };
 JCLO jclo = new JCLO(object);
 String expectedToString = "class java.lang.Object{\nfield1 = field1 = java.lang.String: field1 = test\nfield2 = field2 = java.lang.String: field2 = test2\nfield3 = field3 = java.lang.String: field3 = test3\nfield4 = field4 = int: field4 = 42\nfield5 = field5 = int[]: field5 = [1, 2, 3]\n}";
 String actualToString = jclo.toString();
 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testUsage() {
 Object object = new Object();
 JCLO jclo = new JCLO(object);
 String expectedUsage = "--prefix_myPrefix_field1=myPrefixValue1: myPrefix_field1 (myPrefixValue1)\n" +
 "--prefix_myPrefix_field2=myPrefixValue2: myPrefix_field2 (myPrefixValue2)\n" +
 "--myBoolean: myBoolean (boolean)\n" +
 "--myByte: myByte (byte)\n" +
 "--myChar: myChar (char)\n" +
 "--myShort: myShort (short)\n" +
 "--myInt: myInt (int)\n" +
 "--myLong: myLong (long)\n" +
 "--myFloat: myFloat (float)\n" +
 "--myDouble: myDouble (double)\n" +
 "--myString: myString (java.lang.String)\n" +
 "--myArray: myArray (int[])\n" +
 "--alias1: alias1 (value1)\n" +
 "--alias2: alias2 (value2)\n";
 String actualUsage = jclo.usage();
 assertEquals(expectedUsage, actualUsage);
 }
}