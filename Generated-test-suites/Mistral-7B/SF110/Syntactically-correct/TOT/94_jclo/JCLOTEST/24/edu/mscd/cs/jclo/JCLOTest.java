package edu.mscd.cs.jclo;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.lang.reflect.Field;

public class JCLOTest {

 private JCLO jclo;

 @Before
 public void setUp() {
 jclo = new JCLO(new Object());
 }

 @Test
 public void testGetValue_String() {
 String key = "field1";
 Object expectedValue = new Integer(10);
 Field field = jclo.getClass().getDeclaredField(key);
 try {
 field.set(jclo, expectedValue);
 } catch (IllegalAccessException e) {
 e.printStackTrace();
 }
 Object actualValue = jclo.getValue(key);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testGetValue_NonExistentField() {
 String key = "nonExistentField";
 assertNull(jclo.getValue(key));
 }

 @Test
 public void testGetValue_NullObject() {
 jclo = new JCLO(null);
 String key = "field1";
 assertNull(jclo.getValue(key));
 }

 @Test
 public void testParse_ValidArguments() {
 String[] args = {"--boolean=true", "--byte=-128", "--short=-32768", "--int=-2147483648", "--float=-3.14", "--double=-1.23456789"};
 JCLO jclo = new JCLO(args);
 assertTrue(jclo.getBoolean("boolean"));
 assertEquals(-128, jclo.getByte("byte"));
 assertEquals(-32768, jclo.getShort("short"));
 assertEquals(-2147483648, jclo.getInt("int"));
 assertEquals(-3.14f, jclo.getFloat("float"), 0.01f);
 assertEquals(-1.23456789d, jclo.getDouble("double"), 0.0000001d);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_InvalidArgument1() {
 String[] args = {"--boolean=true=", "--byte=-128="};
 new JCLO(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_InvalidArgument2() {
 String[] args = {"--boolean=true", "--byte=-128=128"};
 new JCLO(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_InvalidArgument3() {
 String[] args = {"--boolean=true", "--byte=-128", "--int=123"};
 new JCLO(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_InvalidArgument4() {
 String[] args = {"--boolean=true", "--byte=-128", "--int=123", "--float"};
 new JCLO(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_InvalidArgument5() {
 String[] args = {"--boolean=true", "--byte=-128", "--int=123", "--float=3.14", "--string"};
 new JCLO(args);
 }
}