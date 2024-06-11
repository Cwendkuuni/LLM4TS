package edu.mscd.cs.jclo;

import org.junit.Test;
import org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

public class JCLOTest {
    // Basic tests

    @Test
    public void testJCLOConstructorWithObject() {
        JCLO jclo = new JCLO(new Object());
        assertNotNull(jclo);
    }

    @Test
    public void testJCLOConstructorWithObjectAndAliases() {
        String[][] aliases = new String[][]{{"a", "aliasA"}, {"b", "aliasB"}};
        JCLO jclo = new JCLO(new Object(), aliases);
        assertNotNull(jclo);
    }

    @Test
    public void testJCLOConstructorWithPrefixAndObject() {
        String prefix = "prefix_";
        JCLO jclo = new JCLO(prefix, new Object());
        assertNotNull(jclo);
        assertEquals(prefix, jclo.prefix);
    }

    @Test
    public void testJCLOConstructorWithPrefixAndObjectAndAliases() {
        String prefix = "prefix_";
        String[][] aliases = new String[][]{{"a", "aliasA"}, {"b", "aliasB"}};
        JCLO jclo = new JCLO(prefix, new Object(), aliases);
        assertNotNull(jclo);
        assertEquals(prefix, jclo.prefix);
    }

    @Test
    public void testGetValue() {
        JCLO jclo = new JCLO(new Object(), null);
        Field field = jclo.getClass().getDeclaredField("object");
        field.setAccessible(true);
        Object value = jclo.getValue("object");
        assertEquals(jclo.object, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValueWithNullKey() {
        JCLO jclo = new JCLO(new Object(), null);
        jclo.getValue(null);
    }

    @Test
    public void testGetValueWithNonExistentField() {
        JCLO jclo = new JCLO(new Object(), null);
        jclo.setValue("nonExistentField", new Object());
        String key = "nonExistentField";
        Object value = jclo.getValue(key);
        assertNull(value);
    }

    @Test
    public void testToString() {
        JCLO jclo = new JCLO(new Object(), null);
        Field field = jclo.getClass().getDeclaredField("object");
        field.setAccessible(true);
        field.set(jclo, new int[5]);
        String expected = "int[]: object = [I: 0 = [I: 0, 0, 0, 0, 0]\n";
        assertEquals(expected, jclo.toString());
    }

    // Edge cases and exceptions tests

    @Test
    public void testGetValueWithNullObject() {
        JCLO jclo = new JCLO(null, null);
        String key = "object";
        Object value = jclo.getValue(key);
        assertNull(value);
    }

    @Test(expected = NullPointerException.class)
    public void testGetValueWithNullField() {
        JCLO jclo = new JCLO(new Object(), null);
        Field nullField = null;
        Object value = jclo.getValue(nullField.getName());
    }

    @Test(expected = IllegalAccessException.class)
    public void testGetValueWithInaccessibleField() throws IllegalAccessException {
        JCLO jclo = new JCLO(new Object(), null);
        Field inaccessibleField = Reflections.getDeclaredField(JCLO.class, "aliases");
        inaccessibleField.setAccessible(false);
        String key = "aliases";
        Object value = jclo.getValue(key);
    }

    @Test
    public void testParseWithEmptyArgs() {
        String[] args = new String[0];
        JCLO jclo = new JCLO(null, null, null);
        try {
            jclo.parse(args);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseWithSingleArg() {
        String[] args = new String[]{"--key=value"};
        JCLO jclo = new JCLO(null, null, null);
        jclo.parse(args);
        Object keyValue = jclo.getValue("key");
        assertEquals(Arrays.asList("value"), Arrays.asList(keyValue));
    }

    @Test
    public void testParseWithDoubleDashes() {
        String[] args = new String[]{"--double-dashes=value"};
        JCLO jclo = new JCLO(null, null, null);
        jclo.parse(args);
        Object keyValue = jclo.getValue("double-dashes");
        assertEquals("value", keyValue);
    }

    @Test
    public void testParseWithDoubleDashesAndEquals() {
        String[] args = new String[]{"--double--dashes=value"};
        JCLO jclo = new JCLO(null, null, null);
        jclo.parse(args);
        Object keyValue = jclo.getValue("double--dashes");
        assertEquals("value", keyValue);
    }

    @Test
    public void testParseWithExistingAndAdditionalArgs() {
        String[] args = new String[]{"--key=value", "--key2=value2", "--additional=arg1,arg2"};
        JCLO jclo = new JCLO(null, null, null);
        jclo.parse(args);
        Object keyValue1 = jclo.getValue("key");
        assertEquals(Arrays.asList("value"), Arrays.asList(keyValue1));
        Object keyValue2 = jclo.getValue("key2");
        assertEquals(Arrays.asList("value2"), Arrays.asList(keyValue2));
        Object additionalValue = jclo.getValue("additional");
        assertArrayEquals(new String[]{"arg1", "arg2"}, (String[])additionalValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidOption() {
        String[] args = new String[]{"--invalidOption=value"};
        JCLO jclo = new JCLO(null, null, null);
        jclo.parse(args);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToArrayWithNullField() {
        JCLO jclo = new JCLO(null, null, null);
        Field nullField = null;
        Object value = jclo.addToArray(nullField, new Object());
    }

    @Test(expected = ArrayStoreException.class)
    public void testAddToArrayWithInvalidType() {
        JCLO jclo = new JCLO(null, null, null);
        Field field = jclo.getClass().getDeclaredField("booleanField");
        field.setAccessible(true);
        Object[] intArray = new Object[1];
        intArray[0] = new Integer(1);
        Object value = jclo.addToArray(field, new Boolean(true));
    }
}