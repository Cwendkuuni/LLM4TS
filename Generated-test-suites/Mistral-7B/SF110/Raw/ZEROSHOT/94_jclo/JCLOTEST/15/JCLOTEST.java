package edu.mscd.cs.jclo;

import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;

public class JCLOTest {

 private static final String TEST_PREFIX = "test";
 private static final String TEST_CLASS = "edu.mscd.cs.jclo.JCLO";

 @Test
 public void testJCLOConstructorWithObject() {
 JCLO jclo = new JCLO(new Object());
 assertEquals(1, jclo.getFields().length);
 }

 @Test
 public void testJCLOConstructorWithObjectAndAliases() {
 String[][] aliases = new String[][] {{ "alias1", "alias2" }, { "alias3", "alias4" }};
 JCLO jclo = new JCLO(new Object(), aliases);
 assertEquals(2, jclo.getFields().length);
 }

 @Test
 public void testJCLOConstructorWithPrefixAndObject() {
 JCLO jclo = new JCLO("prefix", new Object());
 assertEquals(1, jclo.getFields().length);
 assertEquals("prefix", jclo.getPrefix());
 }

 @Test
 public void testJCLOConstructorWithPrefixAndObjectAndAliases() {
 String[][] aliases = new String[][] {{ "alias1", "alias2" }, { "alias3", "alias4" }};
 JCLO jclo = new JCLO("prefix", new Object(), aliases);
 assertEquals(2, jclo.getFields().length);
 assertEquals("prefix", jclo.getPrefix());
 }

 @Test
 public void testGetObject() {
 Object object = new Object();
 JCLO jclo = new JCLO(object);
 Field field = jclo.getField("field1");
 Object getValue = jclo.getObject(field);
 assertEquals(object, getValue);
 }

 @Test(expected = NullPointerException.class)
 public void testGetObjectNullField() {
 JCLO jclo = new JCLO(new Object());
 Field nullField = null;
 jclo.getObject(nullField);
 }

 @Test
 public void testGetField() {
 JCLO jclo = new JCLO("prefix", new Object());
 String key = "field1";
 Field getField = jclo.getField(key);
 assertNotNull(getField);
 assertEquals("field1", getField.getName());
 }

 @Test(expected = NullPointerException.class)
 public void testGetFieldNullKey() {
 JCLO jclo = new JCLO("prefix", new Object());
 Field nullField = jclo.getField(null);
 }

 @Test
 public void testGetValue() {
 JCLO jclo = new JCLO("prefix", new Object());
 Field field = jclo.getField("field1");
 Object value = jclo.getValue("field1");
 assertEquals(field.get(jclo.object), value);
 }

 @Test(expected = NullPointerException.class)
 public void testGetValueNullField() {
 JCLO jclo = new JCLO("prefix", new Object());
 Field nullField = null;
 jclo.getValue(nullField);
 }

 @Test
 public void testGetValueNullValue() {
 JCLO jclo = new JCLO("prefix", new Object());
 Field field = jclo.getField("field1");
 Object nullValue = null;
 assertEquals(nullValue, jclo.getValue("field2"));
 }

 @Test
 public void testSetObject() {
 JCLO jclo = new JCLO("prefix", new Object());
 Field field = jclo.getField("field1");
 Object setValue = new Object();
 jclo.setObject(field, setValue);
 assertEquals(setValue, field.get(jclo.object));
 }

 @Test
 public void testToString() {
 JCLO jclo = new JCLO("prefix", new Object());
 Field field = jclo.getField("field1");
 Object value = new Object();
 jclo.setObject(field, value);
 String toString = jclo.toString();
 assertTrue(toString.contains(value.toString()));
 }

 @Test
 public void testGetUsageType() {
 JCLO jclo = new JCLO("prefix", new Object());
 assertEquals("", jclo.getUsageType(boolean.class));
 assertEquals("boolean...", jclo.getUsageType(boolean[].class));
 assertEquals("int", jclo.getUsageType(int.class));
 assertEquals("java.lang.String", jclo.getUsageType(String.class));
 }

 @Test
 public void testUsage() {
 JCLO jclo = new JCLO("prefix", new Object());
 String usage = jclo.usage();
 assertTrue(usage.contains("--prefix"));
 assertTrue(usage.contains("--field1"));
 }

 @Test
 public void testParseAdditional() {
 String[] args = new String[] { "--additional=arg1", "--additional=arg2" };
 JCLO jclo = new JCLO("prefix", new Object());
 jclo.parse(args);
 Object[] additional = (Object[])jclo.getValue("additional");
 assertEquals(2, additional.length);
 assertEquals("arg1", additional[0].toString());
 assertEquals("arg2", additional[1].toString());
 }

 @Test
 public void testParseAddToArray() {
 String[] args = new String[] { "--field1=1", "--field1=2", "--field1=3" };
 JCLO jclo = new JCLO("prefix", new Object());
 jclo.parse(args);
 Object[] getValue = (Object[])jclo.getValue("field1");
 assertEquals(3, getValue.length);
 assertEquals(1, ((int[])getValue)[0]);
 assertEquals(2, ((int[])getValue)[1]);
 assertEquals(3, ((int[])getValue)[2]);
 }

 @Test
 public void testParseGetBooleanValue() {
 String[] args = new String[] { "--boolean=true" };
 JCLO jclo = new JCLO("prefix", new Object());
 jclo.parse(args);
 assertTrue(jclo.getBoolean("boolean"));
 }

 @Test
 public void testParseGetEqualsValue() {
 String[] args = new String[] { "--boolean=true=value" };
 JCLO jclo = new JCLO("prefix", new Object());
 jclo.parse(args);
 assertEquals("value", jclo.getValue("boolean"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidArgs() {
 String[] args = new String[] { "--boolean=value" };
 JCLO jclo = new JCLO("prefix", new Object());
 jclo.parse(args);
 }

 @Test
 public void testParse() {
 String[] args = new String[] { "--prefix=test", "--field1=1", "--field1=2", "--field1=3", "--boolean=true", "--boolean=false", "--alias1=alias2", "--alias2=test" };
 JCLO jclo = new JCLO(null, new Object(), new String[][] {{ "alias1", "alias2" }});
 jclo.parse(args);
 assertEquals("test:field1 = [1, 2, 3]", jclo.toString());
 assertEquals("true", jclo.getValue("boolean"));
 assertEquals("test", jclo.getValue("alias2"));
 }

 @Test
 public void testGetters() {
 JCLO jclo = new JCLO("prefix", new Object());
 assertEquals(null, jclo.getPrefix());
 assertEquals(null, jclo.getObject());
 assertFalse(jclo.isDoubleDashes());
 assertFalse(jclo.hasEquals());
 assertEquals(null, jclo.getAliases());
 JCLO jcloWithPrefix = new JCLO("prefix", new Object(), new String[][] {{ "alias1", "alias2" }});
 assertEquals("prefix", jcloWithPrefix.getPrefix());
 assertEquals(null, jcloWithPrefix.getObject());
 assertFalse(jcloWithPrefix.isDoubleDashes());
 assertFalse(jcloWithPrefix.hasEquals());
 assertEquals(new String[][] {{ "alias1", "alias2" }}, jcloWithPrefix.getAliases());
 }
}