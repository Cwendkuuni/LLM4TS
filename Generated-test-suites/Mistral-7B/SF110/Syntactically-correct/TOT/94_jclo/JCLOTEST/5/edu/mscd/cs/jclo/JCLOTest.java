package edu.mscd.cs.jclo;

import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

public class JCLOTest {

 private static final String CLASS_NAME = JCLO.class.getName();

 @Test
 public void testJCLOConstruction() throws Exception {
 JCLO jclo = new JCLO(new Object());
 assertEquals(1, jclo.getFields().length);
 assertEquals("Object", jclo.fields[0].getType().getName());

 JCLO jcloWithAliases = new JCLO(new Object(), new String[][]{{"alias1", "aliasValue1"}, {"alias2", "aliasValue2"}});
 assertEquals(3, jcloWithAliases.getFields().length);
 assertEquals("String[][]", jcloWithAliases.getClass().getDeclaredField("aliases").getType().getTypeName());
 }

 @Test
 public void testGetValue() throws Exception {
 JCLO jclo = new JCLO("prefix", new Object(), new String[][]{{"alias1", "aliasValue1"}, {"alias2", "aliasValue2"}});
 Field field = jclo.getClass().getDeclaredField("prefix");
 field.setAccessible(true);
 jclo.setObject(field, "newPrefix");

 assertEquals("newPrefix", jclo.getValue("prefix"));
 assertEquals("Object", jclo.getValue("nonExistentField").getClass().getName());
 }

 @Test(expected = IllegalAccessException.class)
 public void testIllegalAccessException() throws Exception {
 JCLO jclo = new JCLO(new Object());
 Field field = jclo.getClass().getDeclaredField("object");
 field.setAccessible(true);
 field.set(jclo, null);
 jclo.getValue("object");
 }

 @Test
 public void testToString() throws Exception {
 JCLO jclo = new JCLO("prefix", new Object(), new String[][]{{"alias1", "aliasValue1"}, {"alias2", "aliasValue2"}});
 String toString = jclo.toString();
 assertTrue(toString.contains("prefix"));
 assertTrue(toString.contains("Object"));
 assertTrue(toString.contains("alias1"));
 assertTrue(toString.contains("aliasValue1"));
 }

 @Test
 public void testUsage() throws Exception {
 JCLO jclo = new JCLO(new Object());
 String usage = jclo.usage();
 assertTrue(usage.contains("boolean[]: prefix_boolean"));
 assertTrue(usage.contains("Object: object"));
 }

 @Test
 public void testParse() throws Exception {
 String[] args = new String[]{"--doubleDashes", "--prefix=testPrefix", "--additional=array[0]=value1,array[1]=value2"};
 JCLO jclo = new JCLO(new Object());
 jclo.parse(args);

 assertEquals("testPrefix", jclo.getObject(jclo.getField("prefix")));
 Field field = jclo.getField("additional");
 Object additional = jclo.getObject(field);
 assertArrayEquals(new Object[]{new String("value1"), new String("value2")}, additional);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentException() throws Exception {
 String[] args = new String[]{"--invalidOption"};
 JCLO jclo = new JCLO(new Object());
 jclo.parse(args);
 }
}