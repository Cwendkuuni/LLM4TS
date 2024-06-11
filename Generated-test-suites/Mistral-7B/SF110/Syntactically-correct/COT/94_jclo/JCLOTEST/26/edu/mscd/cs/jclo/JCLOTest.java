package edu.mscd.cs.jclo;

import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

public class JCLOTest {

 private JCLO jclo;

 @Test
 public void testJCLOConstructorWithObject() {
 jclo = new JCLO(new Object());
 assertNotNull(jclo.object);
 }

 @Test
 public void testJCLOConstructorWithObjectAndAliases() {
 jclo = new JCLO(new Object(), new String[][]{{"a", "b"}});
 assertNotNull(jclo.aliases);
 assertEquals(1, jclo.aliases.length);
 assertEquals(2, jclo.aliases[0].length);
 }

 @Test
 public void testJCLOConstructorWithPrefixAndObject() {
 jclo = new JCLO("prefix", new Object());
 assertNotNull(jclo.prefix);
 assertEquals("prefix", jclo.prefix);
 }

 @Test
 public void testJCLOConstructorWithPrefixAndObjectAndAliases() {
 jclo = new JCLO("prefix", new Object(), new String[][]{{"a", "b"}});
 assertNotNull(jclo.prefix);
 assertEquals("prefix", jclo.prefix);
 assertNotNull(jclo.aliases);
 assertEquals(1, jclo.aliases.length);
 assertEquals(2, jclo.aliases[0].length);
 }

 @Test
 public void testGetObject() {
 final Field field = new Field() {{
 name = "testField";
 type = Integer.TYPE;
 setAccessible(true);
 }};
 jclo = new JCLO(new Object(), new String[][]{{"testField", "fieldValue"}});
 jclo.setObject(field, 1);
 int value = (int) jclo.getObject(field);
 assertEquals(1, value);
 }

 @Test(expected = NullPointerException.class)
 public void testGetObjectNullField() {
 final Field field = new Field() {{
 name = "nullField";
 type = Integer.TYPE;
 setAccessible(true);
 }};
 jclo = new JCLO(new Object(), new String[][]{{"testField", "fieldValue"}});
 int value = (int) jclo.getObject(field);
 fail("Should have thrown NullPointerException");
 }

 @Test(expected = IllegalAccessException.class)
 public void testGetObjectIllegalAccess() {
 final Field field = new Field() {{
 name = "illegalField";
 type = Integer.TYPE;
 setAccessible(false);
 }};
 jclo = new JCLO(new Object(), new String[][]{{"testField", "fieldValue"}});
 try {
 jclo.getObject(field);
 fail("Should have thrown IllegalAccessException");
 } catch (IllegalAccessException e) {
 // Expected
 }
 }

 @Test
 public void testGetField() {
 jclo = new JCLO(new Object(), new String[][]{{"testField", "fieldValue"}});
 Field field = jclo.getField("testField");
 assertNotNull(field);
 }

 @Test
 public void testGetFieldNullKey() {
 jclo = new JCLO(new Object(), new String[][]{{"testField", "fieldValue"}});
 Field field = jclo.getField(null);
 assertNull(field);
 }

 @Test
 public void testGetValue() {
 jclo = new JCLO(new Object(), new String[][]{{"testField", "fieldValue"}});
 Field testField = jclo.getField("testField");
 Object value = jclo.getValue("testField");
 assertEquals(jclo.getObject(testField), value);
 }

 @Test
 public void testValueNullKey() {
 jclo = new JCLO(new Object(), new String[][]{{"testField", "fieldValue"}});
 Object value = jclo.getValue(null);
 assertNull(value);
 }

 @Test
 public void testGetArrayType() {
 assertEquals("int[]", JCLO.getArrayType(int[].class));
 }

 @Test
 public void testToString() {
 jclo = new JCLO(new Object(), new String[][]{{"field1", "value1"}, {"field2", "value2"}});
 String expected = "int[]: field1 = [1, 2, 3]\nboolean: field2 = false\n";
 assertEquals(expected, jclo.toString());
 }

 @Test
 public void testUsage() {
 jclo = new JCLO(new Object(), new String[][]{{"field1", "value1"}, {"field2", "value2"}});
 String expected = "--field1 --field2\n";
 assertEquals(expected, jclo.usage());
 }

 @Test
 public void testParseAdditional() {
 String[] args = {"--additional", "arg1", "arg2"};
 JCLO jclo = new JCLO(new Object(), null);
 jclo.parseAdditional(args, 0);
 String[] additional = (String[]) jclo.getObject(jclo.getField("additional"));
 assertEquals(2, additional.length);
 assertEquals("arg1", additional[0]);
 assertEquals("arg2", additional[1]);
 }

 @Test
 public void testAddToArray() {
 Field field = new Field() {{
 name = "testField";
 type = int[].class;
 setAccessible(true);
 }};
 Object array = JCLO.addToArray(field, 1);
 int[] result = (int[]) array;
 assertEquals(1, result.length);
 assertEquals(1, result[0]);
 }

 @Test
 public void testGetKey() {
 assertEquals("testField", JCLO.getKey("test.field"));
 }

 @Test
 public void testGetBooleanValue() {
 assertEquals("true", JCLO.getBooleanValue("--yes"));
 assertEquals("true", JCLO.getBooleanValue("--true"));
 }

 @Test
 public void testMakeObject() {
 assertEquals(true, (boolean) JCLO.makeObject(Boolean.TYPE, "true"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullArgs() {
 JCLO jclo = new JCLO(new Object(), null);
 String[] args = null;
 try {
 jclo.parse(args);
 fail("Should have thrown IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingEquals() {
 String[] args = {"--test"};
 JCLO jclo = new JCLO(new Object(), null);
 try {
 jclo.parse(args);
 fail("Should have thrown IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingValue() {
 String[] args = {"--test="};
 JCLO jclo = new JCLO(new Object(), null);
 try {
 jclo.parse(args);
 fail("Should have thrown IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingKey() {
 String[] args = {"=value"};
 JCLO jclo = new JCLO(new Object(), null);
 try {
 jclo.parse(args);
 fail("Should have thrown IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseMultipleFields() {
 String[] args = {"--test1=value1", "--test2=value2"};
 JCLO jclo = new JCLO(new Object(), null);
 jclo.parse(args);
 Object test1Value = jclo.getValue("test1");
 assertEquals("value1", test1Value);
 Object test2Value = jclo.getValue("test2");
 assertEquals("value2", test2Value);
 }

 @Test
 public void testParseMultipleFieldsWithPrefix() {
 String[] args = {"--test1=value1", "--test2=value2"};
 JCLO jclo = new JCLO("prefix", new Object(), null);
 jclo.parse(args);
 Object test1Value = jclo.getValue("test1");
 assertEquals("prefix_test1", test1Value);
 Object test2Value = jclo.getValue("test2");
 assertEquals("prefix_test2", test2Value);
 }

 @Test
 public void testParseMultipleFieldsWithAliases() {
 String[] args = {"--alias1=value1", "--alias2=value2"};
 JCLO jclo = new JCLO(new Object(), new String[][]{{"alias1", "test1"}, {"alias2", "test2"}});
 jclo.parse(args);
 Object test1Value = jclo.getValue("test1");
 assertEquals("value1", test1Value);
 Object test2Value = jclo.getValue("test2");
 assertEquals("value2", test2Value);
 }

 @Test
 public void testParseMultipleFieldsWithAliasesAndPrefix() {
 String[] args = {"--alias1=value1", "--alias2=value2"};
 JCLO jclo = new JCLO("prefix", new Object(), new String[][]{{"alias1", "test1"}, {"alias2", "test2"}});
 jclo.parse(args);
 Object test1Value = jclo.getValue("test1");
 assertEquals("prefix_test1", test1Value);
 Object test2Value = jclo.getValue("test2");
 assertEquals("prefix_test2", test2Value);
 }

}