package edu.mscd.cs.jclo;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JCLOTest {

    

    @Test
    public void testJCLOConstructorWithObject() {
        JCLO jclo = new JCLO(new Object());
        assertNotNull(jclo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJCLOConstructorWithNullObject() {
        new JCLO(null);
    }

    @Test
    public void testJCLOConstructorWithObjectAndAliases() {
        String[][] aliases = new String[][]{{"a", "aliasA"}, {"b", "aliasB"}};
        JCLO jclo = new JCLO(new Object(), aliases);
        assertNotNull(jclo);
    }

    @Test
    public void testJCLOConstructorWithPrefixAndObject() {
        String prefix = "prefix";
        JCLO jclo = new JCLO(prefix, new Object());
        assertNotNull(jclo);
        assertEquals(prefix, jclo.prefix);
    }

    @Test
    public void testJCLOConstructorWithPrefixAndObjectAndAliases() {
        String prefix = "prefix";
        String[][] aliases = new String[][]{{"a", "aliasA"}, {"b", "aliasB"}};
        JCLO jclo = new JCLO(prefix, new Object(), aliases);
        assertNotNull(jclo);
        assertEquals(prefix, jclo.prefix);
    }

    @Test
    public void testGetObject() {
        JCLO jclo = new JCLO(new Object());
        Field field = jclo.getClass().getDeclaredFields()[0];
        Object object = new Object();
        field.set(jclo, object);
        assertNotNull(jclo.getObject(field));
        assertEquals(object, jclo.getObject(field));
    }

    @Test
    public void testGetField() {
        JCLO jclo = new JCLO(new Object());
        String key = "field";
        Field expectedField = new Object().getClass().getDeclaredField(key);
        Field actualField = jclo.getField(key);
        assertNotNull(actualField);
        assertEquals(expectedField, actualField);
    }

    @Test
    public void testGetValue() {
        JCLO jclo = new JCLO(new Object());
        Field field = new Object().getClass().getDeclaredField("field");
        Object value = new Object();
        try {
            field.set(new Object(), value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        jclo.setObject(field, value);
        assertEquals(value, jclo.getValue("field"));
    }

    @Test
    public void testGetValueWithNullKey() {
        JCLO jclo = new JCLO(new Object());
        assertNull(jclo.getValue(null));
    }

    @Test
    public void testGetValueWithNonExistentKey() {
        JCLO jclo = new JCLO(new Object());
        assertNull(jclo.getValue("nonExistentKey"));
    }

    @Test
    public void testSetObject() {
        JCLO jclo = new JCLO(new Object());
        Field field = new Object().getClass().getDeclaredField("field");
        Object value = new Object();
        jclo.setObject(field, value);
        assertEquals(value, jclo.getObject(field));
    }

    @Test
    public void testGetArrayType() {
        Class intArrayClass = int[].class;
        String arrayType = JCLO.getArrayType(intArrayClass);
        assertEquals("int[]", arrayType);
    }

    @Test
    public void testToString() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        StringBuilder expectedToString = new StringBuilder();
        expectedToString.append("int: field1 = ");
        expectedToString.append(Arrays.toString((int[])jclo.getObject(jclo.getField("field1"))));
        expectedToString.append("\n");
        String actualToString = jclo.toString();
        assertTrue(expectedToString.toString().equals(actualToString));
    }

    @Test
    public void testUsage() {
        JCLO jclo = new JCLO(new Object());
        String usage = jclo.usage();
        assertTrue(usage.contains("--field1="));
        assertTrue(usage.contains("--field1=" + "int..."));
        assertTrue(usage.contains("--field2="));
        assertTrue(usage.contains("--field2=" + "int"));
    }

    @Test
    public void testParse() {
        String[] args = {"--field1=1,2,3", "--field2=true"};
        JCLO jclo = new JCLO(new Object());
        jclo.parse(args);
        int[] expectedField1 = {1, 2, 3};
        assertArrayEquals(expectedField1, (int[])jclo.getObject(jclo.getField("field1")));
        assertTrue(jclo.getBoolean("field2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNonExistentKey() {
        String[] args = {"--nonExistentKey=1,2,3"};
        JCLO jclo = new JCLO(new Object());
        jclo.parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidType() {
        String[] args = {"--field1=hello"};
        JCLO jclo = new JCLO(new Object());
        jclo.parse(args);
    }

    
}