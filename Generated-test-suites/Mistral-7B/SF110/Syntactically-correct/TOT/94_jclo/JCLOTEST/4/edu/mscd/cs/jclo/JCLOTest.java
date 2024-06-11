package edu.mscd.cs.jclo;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JCLOTest {

 private Object object;
 private JCLO jclo;

 @Before
 public void setUp() {
 object = new Object() {
 Field testField = new Field("testField", int.class);
 };
 jclo = new JCLO(object);
 }

 @Test
 public void testGetValue() {
 jclo.setObject(object.getClass().getDeclaredField("testField"), 42);
 assertEquals(42, jclo.getValue("testField"));
 }

 @Test(expected = NullPointerException.class)
 public void testGetValueNullField() {
 jclo.setValue("nonExistentField", "expectedValue");
 assertNull(jclo.getValue("nonExistentField"));
 }

 @Test
 public void testGetString() {
 jclo.setObject(object.getClass().getDeclaredField("testField"), "testString");
 assertEquals("testString", jclo.getValue("testField"));
 }

 @Test
 public void testGetBoolean() {
 jclo.setObject(object.getClass().getDeclaredField("testField"), true);
 assertTrue(jclo.getBoolean("testField"));
 }

 @Test
 public void testGetByte() {
 jclo.setObject(object.getClass().getDeclaredField("testField"), (byte) 10);
 assertEquals((byte) 10, jclo.getByte("testField"));
 }

 @Test
 public void testGetShort() {
 jclo.setObject(object.getClass().getDeclaredField("testField"), (short) 20);
 assertEquals((short) 20, jclo.getShort("testField"));
 }

 @Test
 public void testGetInt() {
 jclo.setObject(object.getClass().getDeclaredField("testField"), 30);
 assertEquals(30, jclo.getInt("testField"));
 }

 @Test
 public void testGetFloat() {
 jclo.setObject(object.getClass().getDeclaredField("testField"), (float) 3.14f);
 assertEquals(3.14f, jclo.getFloat("testField"), 0.01f);
 }

 @Test
 public void testGetDouble() {
 jclo.setObject(object.getClass().getDeclaredField("testField"), 3.14);
 assertEquals(3.14, jclo.getDouble("testField"), 0.001);
 }

 @Test
 public void testGetLong() {
 jclo.setObject(object.getClass().getDeclaredField("testField"), 42L);
 assertEquals(42L, jclo.getLong("testField"));
 }

 @Test
 public void testGetArray() {
 int[] testArray = {1, 2, 3};
 jclo.setObject(object.getClass().getDeclaredField("testField"), testArray);
 assertArrayEquals(testArray, (int[]) jclo.getObject(jclo.getField("testField")));
 }

 @Test
 public void testAliases() {
 jclo = new JCLO("prefix_", object);
 jclo.setValue("prefix_testField", 42);
 assertEquals(42, jclo.getValue("testField"));
 }

 @Test
 public void testParse() {
 String[] args = {"--double-dashes", "--has-equals=testField=true", "--testField=42"};
 jclo = new JCLO(null, object, null);
 jclo.parse(args);
 assertTrue(jclo.getBoolean("hasEquals"));
 assertEquals(true, jclo.getBoolean("testField"));
 assertEquals(42, jclo.getInt("testField"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidOption() {
 String[] args = {"--invalidOption=42"};
 jclo = new JCLO(null, object, null);
 jclo.parse(args);
 fail("Expected IllegalArgumentException");
 }
}