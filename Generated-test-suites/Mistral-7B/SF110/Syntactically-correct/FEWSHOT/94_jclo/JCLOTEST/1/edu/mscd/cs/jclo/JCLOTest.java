package edu.mscd.cs.jclo;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.junit.Test;

public class JCLOTest {

    @Test
    public void testJCLOConstructor() {
        JCLO jclo = new JCLO(new Object());
        assertNotNull(jclo.getField("object"));
        assertEquals(jclo.getClass(), jclo.getClass());
        assertFalse(jclo.hasEquals());
        assertFalse(jclo.doubleDashes());
        assertNull(jclo.prefix);
        assertNull(jclo.aliases);
    }

    @Test
    public void testJCLOConstructorWithPrefix() {
        JCLO jclo = new JCLO("prefix", new Object());
        assertEquals("prefix", jclo.prefix);
        assertNull(jclo.aliases);
    }

    @Test
    public void testJCLOConstructorWithAliases() {
        String[][] aliases = new String[][]{{"a", "b"}, {"c", "d"}};
        JCLO jclo = new JCLO(new Object(), aliases);
        assertEquals(aliases, jclo.aliases);
        assertNull(jclo.prefix);
    }

    @Test
    public void testJCLOConstructorWithPrefixAndAliases() {
        String[][] aliases = new String[][]{{"a", "b"}, {"c", "d"}};
        JCLO jclo = new JCLO("prefix", new Object(), aliases);
        assertEquals("prefix", jclo.prefix);
        assertEquals(aliases, jclo.aliases);
    }

    @Test
    public void testGetObject() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        Field field = new Field("test", Integer.TYPE);
        jclo.setObject(field, 1);
        assertEquals(1, jclo.getObject(field));
    }

    @Test
    public void testGetField() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        Field field = new Field("test", Integer.TYPE);
        jclo.setField(field, "testFieldName");
        Field testField = jclo.getField("testFieldName");
        assertEquals(field, testField);
    }

    @Test
    public void testGetValue() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        Field field = new Field("test", Integer.TYPE);
        jclo.setObject(field, 1);
        assertEquals(1, jclo.getValue("test"));
    }

    @Test
    public void testSetObject() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        Field field = new Field("test", Integer.TYPE);
        jclo.setObject(field, 1);
        assertEquals(1, jclo.getObject(field).intValue());
    }

    @Test
    public void testGetArrayType() {
        Class intArrayClass = int[].class;
        assertEquals("int[]", JCLO.getArrayType(intArrayClass));
    }

    @Test
    public void testToString() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        Field field1 = new Field("test1", Integer.TYPE);
        Field field2 = new Field("test2", String.class);
        jclo.setObject(field1, 1);
        jclo.setObject(field2, "testString");
        String expected = "int: test1 = 1\njava.lang.String: test2 = testString";
        assertEquals(expected, jclo.toString());
    }

    @Test
    public void testUsage() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        Field field1 = new Field("test1", Integer.TYPE);
        Field field2 = new Field("test2", String.class);
        jclo.setObject(field1, 1);
        jclo.setObject(field2, "testString");
        String expected = "--test1 <int>\n--test2 <string>\n";
        assertEquals(expected, jclo.usage());
    }

    @Test
    public void testParseAdditional() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        String[] args = {"--additional", "arg1", "arg2"};
        jclo.parseAdditional(args, 0);
        Field field = jclo.getField("additional");
        assertNotNull(field);
        Object additional = jclo.getObject(field);
        assertArrayEquals(new String[]{"arg1", "arg2"}, (String[])additional);
    }

    @Test
    public void testParseAddToArray() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        Field field = new Field("test", Integer[].class);
        jclo.parseAdditional(new String[]{"--test", "1"}, 0);
        jclo.parseAdditional(new String[]{"--test", "2"}, 0);
        Object test = jclo.getObject(field);
        assertEquals(new Integer[]{1, 2}, test);
    }

    @Test
    public void testParseGetKey() {
        String arg = "--test_1__2__3=value";
        String expected = "test_1_2_3";
        assertEquals(expected, JCLO.getKey(arg));
    }

    @Test
    public void testParseGetBooleanValue() {
        String arg = "--test=true";
        String expected = "true";
        assertEquals(expected, JCLO.getBooleanValue(arg));
    }

    @Test
    public void testParseMakeObject() {
        String type = "boolean";
        String val = "true";
        Object expected = Boolean.TRUE;
        Object result = JCLO.makeObject(type, val);
        assertEquals(expected, result);
    }

    @Test
    public void testParseGetEqualsValue() {
        String arg = "--test=value";
        String expected = "value";
        assertEquals(expected, JCLO.getEqualsValue(arg));
    }

    @Test
    public void testParseIllegalArgumentException() {
        String arg = "--test=value=";
        assertThrows(IllegalArgumentException.class, () -> JCLO.getEqualsValue(arg));
    }

    @Test
    public void testParse() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        String[] args = {"--test1", "1", "--test2=value2", "--test3=3"};
        jclo.parse(args);
        Field field1 = jclo.getField("test1");
        assertEquals(1, jclo.getObject(field1).intValue());
        Field field2 = jclo.getField("test2");
        assertEquals("value2", jclo.getObject(field2));
        Field field3 = jclo.getField("test3");
        assertEquals(3, jclo.getObject(field3).intValue());
    }

    @Test
    public void testParseBoolean() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        String[] args = {"--test", "true"};
        jclo.parse(args);
        Field field = jclo.getField("test");
        assertTrue(jclo.getBoolean("test"));
    }

    @Test
    public void testParseBooleanFalse() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        String[] args = {"--test"};
        jclo.parse(args);
        Field field = jclo.getField("test");
        assertFalse(jclo.getBoolean("test"));
    }

    @Test
    public void testParseNull() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        String[] args = {};
        jclo.parse(args);
        Field field = jclo.getField("test");
        assertNull(jclo.getObject(field));
    }

    @Test
    public void testParseNonExistentField() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        String[] args = {"--nonExistentField"};
        jclo.parse(args);
        assertThrows(IllegalArgumentException.class, () -> jclo.getValue("nonExistentField"));
    }

    @Test
    public void testMain() {
        String[] args = {"--version"};
        JCLO.main(args);
        assertEquals("1.3.4", System.out.getHistory(0).getLine().trim());
    }
}