package edu.mscd.cs.jclo;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.junit.Test;

public class JCLOTest {

    @Test
    public void testJCLOConstructorWithObject() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        assertEquals(obj.getClass(), jclo.object.getClass());
        assertEquals(0, jclo.fields.length);
    }

    @Test
    public void testJCLOConstructorWithStringPrefixAndObject() {
        Object obj = new Object();
        JCLO jclo = new JCLO("prefix", obj);
        assertEquals("prefix", jclo.prefix);
        assertEquals(obj.getClass(), jclo.object.getClass());
        assertEquals(0, jclo.fields.length);
    }

    @Test
    public void testJCLOConstructorWithObjectAndAliases() {
        Object obj = new Object()[0];
        JCLO jclo = new JCLO(obj, new String[][] {{"key1", "alias1"}, {"key2", "alias2"}});
        assertEquals(obj.getClass(), jclo.object.getClass());
        assertEquals("key1", jclo.getField("alias1").getName());
        assertEquals("key2", jclo.getField("alias2").getName());
        assertEquals(2, jclo.fields.length);
    }

    @Test
    public void testGetObject() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        Field field = jclo.getClass().getDeclaredField("object");
        field.setAccessible(true);
        assertSame(obj, field.get(jclo));
    }

    @Test
    public void testGetField() {
        Object obj = new Object();
        Field field = new Field("key", Integer.TYPE, Modifier.PRIVATE, null, null);
        JCLO jclo = new JCLO(obj, new Field[] {field});
        assertEquals(field, jclo.getField("key"));
    }

    @Test
    public void testGetValue() {
        Object obj = new Object();
        Object value = new Object();
        Field field = new Field("key", Integer.TYPE, Modifier.PRIVATE, null, null);
        JCLO jclo = new JCLO(obj, new Field[] {field});
        field.set(obj, value);
        assertEquals(value, jclo.getValue("key"));
    }

    @Test
    public void testSetObject() {
        Object obj = new Object();
        Object newValue = new Object();
        JCLO jclo = new JCLO(obj);
        Field field = jclo.getClass().getDeclaredField("object");
        field.setAccessible(true);
        field.set(jclo, newValue);
        assertEquals(newValue, jclo.object);
    }

    @Test
    public void testToString() {
        Object obj = new Object() {
            int field1;
            int[] field2;
        };
        JCLO jclo = new JCLO(obj);
        String expected = "int: field1 = " + obj.field1 + "\n"
                + "int[]: field2 = [" + Arrays.toString((int[])obj.field2) + "]";
        assertEquals(expected, jclo.toString());
    }

    @Test
    public void testGetUsageType() {
        assertEquals("", JCLO.getUsageType(Boolean.TYPE));
        assertEquals("boolean...", JCLO.getUsageType(Boolean[].class));
        assertEquals("int", JCLO.getUsageType(Integer.TYPE));
        assertEquals("int...", JCLO.getUsageType(int[].class));
        assertEquals("java.lang.String", JCLO.getUsageType(String.class));
    }

    @Test
    public void testUsage() {
        Object obj = new Object() {
            boolean booleanField;
            byte byteField;
            short shortField;
            int intField;
            float floatField;
            double doubleField;
            long longField;
            char charField;
            String stringField;
            int[] intArrayField;
            String[] stringArrayField;
        };
        JCLO jclo = new JCLO(obj);
        String expected = "--boolean: \n"
                + "--byte: \n"
                + "--short: \n"
                + "--int: \n"
                + "--float: \n"
                + "--double: \n"
                + "--long: \n"
                + "--char: \n"
                + "--string: \n"
                + "--int-array:\n";
        assertEquals(expected, jclo.usage());
    }

    @Test
    public void testParseAdditional() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        String[] args = {"--additional", "arg1", "arg2"};
        jclo.parseAdditional(args, 0);
        Field additionalField = jclo.getField("additional");
        assertNotNull(additionalField);
        Object additional = jclo.getObject(additionalField);
        assertArrayEquals(new String[]{"arg1", "arg2"}, (String[])additional);
    }

    @Test
    public void testParse() {
        Object obj = new Object() {
            boolean booleanField;
            byte byteField;
            short shortField;
            int intField;
            float floatField;
            double doubleField;
            long longField;
            char charField;
            String stringField;
            int[] intArrayField;
            String[] stringArrayField;
        };
        JCLO jclo = new JCLO(obj);
        String[] args = {"--boolean", "true", "--byte", "1", "--short", "127", "--int", "12345",
                "--float", "3.14159", "--double", "3.14159265358979323846", "--long", "9223372036854775807",
                "--char", "A", "--string", "Hello, world!", "--int-array", "1", "2", "3", "--string-array", "a",
                "b", "c"};
        jclo.parse(args);
        assertTrue(jclo.getBoolean("booleanField"));
        assertEquals((byte)1, jclo.getByte("byteField"));
        assertEquals((short)127, jclo.getShort("shortField"));
        assertEquals(12345, jclo.getInt("intField"));
        assertEquals(3.14159f, jclo.getFloat("floatField"), 0.001f);
        assertEquals(3.14159265358979323846d, jclo.getDouble("doubleField"), 0.000000000000001d);
        assertEquals(9223372036854775807L, jclo.getLong("longField"));
        assertEquals('A', jclo.getChar("charField"));
        assertEquals("Hello, world!", jclo.getString("stringField"));
        assertArrayEquals(new int[]{1, 2, 3}, (int[])jclo.getObject(jclo.getField("intArrayField")));
        assertArrayEquals(new String[]{"a", "b", "c"}, (String[])jclo.getObject(jclo.getField("stringArrayField")));
    }

    @Test
    public void testGetBooleanValue() {
        assertEquals("true", JCLO.getBooleanValue("true"));
        assertEquals("true", JCLO.getBooleanValue("yes"));
        assertEquals("false", JCLO.getBooleanValue("false"));
        assertEquals("false", JCLO.getBooleanValue("no"));
        assertNull(JCLO.getBooleanValue(""));
        assertThrows(IllegalArgumentException.class, () -> JCLO.getBooleanValue("--verbose"));
    }

    @Test
    public void testMakeObject() {
        assertEquals(true, JCLO.makeObject(Boolean.TYPE, "true").booleanValue());
        assertEquals((byte)0, JCLO.makeObject(Byte.TYPE, "0").byteValue());
        assertEquals((short)1, JCLO.makeObject(Short.TYPE, "1").shortValue());
        assertEquals(12345, JCLO.makeObject(Integer.TYPE, "12345").intValue());
        assertEquals(3.14159f, JCLO.makeObject(Float.TYPE, "3.14159").floatValue(), 0.001f);
        assertEquals(3.14159265358979323846d, JCLO.makeObject(Double.TYPE, "3.14159265358979323846").doubleValue(), 0.000000000000001d);
        assertEquals(9223372036854775807L, JCLO.makeObject(Long.TYPE, "9223372036854775807").longValue());
        assertEquals('A', JCLO.makeObject(Character.TYPE, "A").charValue());
        assertEquals("Hello, world!", JCLO.makeObject(String.TYPE, "Hello, world!"));
        assertNull(JCLO.makeObject(String.TYPE, ""));
        assertNull(JCLO.makeObject(Integer.TYPE, ""));
    }

    @Test
    public void testGetEqualsValue() {
        assertEquals("value", JCLO.getEqualsValue("--key=value"));
        assertEquals("value", JCLO.getEqualsValue("--key value"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidOption() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        String[] args = {"--invalid-option"};
        jclo.parse(args);
    }
}