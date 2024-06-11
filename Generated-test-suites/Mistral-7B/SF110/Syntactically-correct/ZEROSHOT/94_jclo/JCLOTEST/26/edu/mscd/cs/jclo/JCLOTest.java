package edu.mscd.cs.jclo;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class JCLOTest {

 private Object object;
 private JCLO jclo;

 @Before
 public void setup() {
 object = new TestClass();
 jclo = new JCLO(object);
 }

 @Test
 public void testConstructorWithObject() {
 JCLO jclo1 = new JCLO(object);
 assertEquals(object.getClass(), jclo1.getClass());
 assertEquals(object, jclo1.getObject("object"));
 }

 @Test
 public void testConstructorWithObjectAndAliases() {
 String[][] aliases = new String[][] {{ "alias1", "alias2" }, { "alias3", "alias4" }};
 JCLO jclo2 = new JCLO(object, aliases);
 assertEquals(aliases, jclo2.getAliases());
 }

 @Test
 public void testConstructorWithPrefixAndObject() {
 String prefix = "prefix_";
 JCLO jclo3 = new JCLO(prefix, object);
 String key = "prefix_object";
 assertEquals(key, jclo3.getField(key).getName());
 }

 @Test
 public void testConstructorWithPrefixAndObjectAndAliases() {
 String prefix = "prefix_";
 String[][] aliases = new String[][] {{ "alias1", "alias2" }, { "alias3", "alias4" }};
 JCLO jclo4 = new JCLO(prefix, object, aliases);
 String key = "prefix_object";
 assertEquals(key, jclo4.getField(key).getName());
 assertEquals(aliases, jclo4.getAliases());
 }

 @Test
 public void testGetValue() {
 Field field = null;
 try {
 field = object.getClass().getDeclaredField("booleanField");
 } catch (NoSuchFieldException e) {
 fail("Field not found");
 }
 field.setAccessible(true);
 jclo.parse(new String[] { "-booleanField=true" });
 assertTrue(jclo.getBoolean("booleanField"));
 }

 @Test
 public void testGetValueWithAlias() {
 String[][] aliases = new String[][] {{ "b", "booleanField" }, { "i", "integerField" }};
 JCLO jclo5 = new JCLO("", object, aliases);
 jclo5.parse(new String[] { "--booleanField=true" });
 assertTrue(jclo5.getBoolean("b"));
 }

 @Test
 public void testGetValueWithPrefix() {
 String prefix = "prefix_";
 JCLO jclo6 = new JCLO(prefix, object);
 jclo6.parse(new String[] { prefix + "booleanField=true" });
 assertTrue(jclo6.getBoolean("booleanField"));
 }

 @Test
 public void testGetValueWithPrefixAndAlias() {
 String prefix = "prefix_";
 String[][] aliases = new String[][] {{ "b", "booleanField" }, { "i", "integerField" }};
 JCLO jclo7 = new JCLO(prefix, object, aliases);
 jclo7.parse(new String[] { prefix + "booleanField=true" });
 assertTrue(jclo7.getBoolean("b"));
 }

 @Test
 public void testGetValueWithArray() {
 Field field = null;
 try {
 field = object.getClass().getDeclaredField("intArrayField");
 } catch (NoSuchFieldException e) {
 fail("Field not found");
 }
 field.setAccessible(true);
 int[] expected = new int[] { 1, 2, 3 };
 jclo.parse(new String[] { "-intArrayField=1,2,3" });
 assertArrayEquals(expected, (int[])jclo.getObject(field));
 }

 @Test
 public void testUsage() {
 String expected = "-integerField: int\n" +
                "-booleanField: boolean\n" +
                "-stringField: java.lang.String\n" +
                "--doubleField: double\n" +
                "--intArrayField: int...\n" +
                "--stringArrayField: java.lang.String...\n";
 assertEquals(expected, jclo.usage());
 }

 @Test
 public void testParse() {
 final String[] args = new String[] { "-booleanField=true",
                                       "-integerField=1",
                                       "-stringField=test",
                                       "--doubleField=1.5",
                                       "--intArrayField=2,3,4,5",
                                       "--stringArrayField=test1,test2,test3" };
 jclo.parse(args);
 assertTrue(jclo.getBoolean("booleanField"));
 assertEquals(1, jclo.getInt("integerField"));
 assertEquals("test", jclo.getString("stringField"));
 assertEquals(1.5, jclo.getDouble("doubleField"), 0.001);
 int[] intArray = (int[])jclo.getObject(jclo.getField("intArrayField"));
 assertArrayEquals(new int[] { 2, 3, 4, 5 }, intArray);
 String[] stringArray = (String[])jclo.getObject(jclo.getField("stringArrayField"));
 assertArrayEquals(new String[] { "test1", "test2", "test3" }, stringArray);
 }

 @Test
 public void testParseWithPrefix() {
 final String[] args = new String[] { "--prefix_booleanField=true",
                                       "--prefix_integerField=1",
                                       "--prefix_stringField=test",
                                       "--prefix_doubleField=1.5",
                                       "--prefix_intArrayField=2,3,4,5",
                                       "--prefix_stringArrayField=test1,test2,test3" };
 JCLO jclo8 = new JCLO("prefix_", object);
 jclo8.parse(args);
 assertTrue(jclo8.getBoolean("booleanField"));
 assertEquals(1, jclo8.getInt("integerField"));
 assertEquals("test", jclo8.getString("stringField"));
 assertEquals(1.5, jclo8.getDouble("doubleField"), 0.001);
 int[] intArray = (int[])jclo8.getObject(jclo8.getField("intArrayField"));
 assertArrayEquals(new int[] { 2, 3, 4, 5 }, intArray);
 String[] stringArray = (String[])jclo8.getObject(jclo8.getField("stringArrayField"));
 assertArrayEquals(new String[] { "test1", "test2", "test3" }, stringArray);
 }

 @Test
 public void testParseWithAliases() {
 final String[] args = new String[] { "-b=true",
                                       "-i=1",
                                       "-s=test",
                                       "--d=1.5",
                                       "--ia=2,3,4,5",
                                       "--sa=test1,test2,test3" };
 JCLO jclo9 = new JCLO("", object, new String[][] { { "b", "booleanField" }, { "i", "integerField" }, { "s", "stringField" }, { "d", "doubleField" }, { "ia", "intArrayField" }, { "sa", "stringArrayField" } });
 jclo9.parse(args);
 assertTrue(jclo9.getBoolean("booleanField"));
 assertEquals(1, jclo9.getInt("integerField"));
 assertEquals("test", jclo9.getString("stringField"));
 assertEquals(1.5, jclo9.getDouble("doubleField"), 0.001);
 int[] intArray = (int[])jclo9.getObject(jclo9.getField("intArrayField"));
 assertArrayEquals(new int[] { 2, 3, 4, 5 }, intArray);
 String[] stringArray = (String[])jclo9.getObject(jclo9.getField("stringArrayField"));
 assertArrayEquals(new String[] { "test1", "test2", "test3" }, stringArray);
 }
}

class TestClass {
 boolean booleanField = false;
 int intField = 1;
 String stringField = "test";
 double doubleField = 1.5;
 int[] intArrayField = new int[] { 1, 2, 3 };
 String[] stringArrayField = new String[] { "test1", "test2", "test3" };
}