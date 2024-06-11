package edu.mscd.cs.jclo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;

public class JCLOTest {

 private JCLO jclo;

 @Before
 public void setUp() {
 jclo = new JCLO(new Object());
 }

 @Test
 public void testGetValue_String() {
 String key = "field1";
 Object expectedValue = new Integer(1);
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
 public void testGetValue_PrefixField() {
 String prefix = "prefix_";
 String key = "field1";
 Object expectedValue = new Integer(2);
 try {
 Field field = jclo.getClass().getDeclaredField(key);
 field.setAccessible(true);
 field.set(jclo, expectedValue);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 e.printStackTrace();
 }
 Object actualValue = jclo.getValue(prefix + key);
 assertEquals(expectedValue, actualValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetValue_NoSuchOption() {
 String key = "noSuchOption";
 jclo.getValue(key);
 }

 @Test
 public void testUsage() {
 assertEquals("", jclo.usage());
 jclo = new JCLO("prefix_", new Object());
 assertEquals(
 "boolean[]: prefix_boolean\n" +
 "int: prefix_field1\n" +
 "int[]: prefix_field2\n" +
 "java.lang.String: prefix_field3\n", jclo.usage());
 }

 @Test
 public void testParse() {
 String[] args = new String[]{"--boolean", "prefix_boolean=true", "--int", "prefix_field1=1", "--ints", "prefix_field2=1,2,3"};
 jclo = new JCLO(null, args);
 JCLO expectedJclo = new JCLO(true, null);
 assertTrue(expectedJclo.getBoolean("prefix_boolean"));
 assertEquals(1, expectedJclo.getInt("prefix_field1"));
 assertArrayEquals(new int[]{1, 2, 3}, (int[])expectedJclo.getInts("prefix_field2"));
 assertEquals(expectedJclo, jclo);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_InvalidBoolean() {
 String[] args = new String[]{"--boolean=invalidBoolean"};
 jclo = new JCLO(null, args);
 }

 @Test
 public void testParse_InvalidInt() {
 String[] args = new String[]{"--int=invalidInt"};
 jclo = new JCLO(null, args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_InvalidInts() {
 String[] args = new String[]{"--ints=-1,-2"};
 jclo = new JCLO(null, args);
 }

 @After
 public void tearDown() {
 jclo = null;
 }

}