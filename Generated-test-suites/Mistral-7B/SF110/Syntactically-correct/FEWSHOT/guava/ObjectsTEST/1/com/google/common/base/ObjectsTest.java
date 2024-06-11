package com.google.common.base;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal(1, 1));
        assertTrue(Objects.equal("foo", "foo"));
        assertFalse(Objects.equal(null, "foo"));
        assertFalse(Objects.equal(new Object(), new Object()));
        assertFalse(Objects.equal(1, "1"));
    }

    @Test
    public void testHashCode() {
        int hashCode = Objects.hashCode();
        assertEquals(hashCode, Objects.hashCode());
        assertEquals(hashCode, Objects.hashCode(new Object()));
        assertEquals(1, Objects.hashCode(1));
        assertEquals("foo".hashCode(), Objects.hashCode("foo"));
        assertEquals(1 * 31 + "foo".hashCode(), Objects.hashCode(1, "foo"));
        assertEquals(1 * 31 + "foo".hashCode(), Objects.hashCode("foo", 1));
        assertEquals(1 * 31 + "foo".hashCode() * 31 + 2, Objects.hashCode(1, "foo", 2));
        assertEquals(1 * 31 + "foo".hashCode() * 31 + 2 * 31 + "bar".hashCode(), Objects.hashCode(1, "foo", 2, "bar"));
    }

    @Test(expected = NullPointerException.class)
    public void testHashCodeNull() {
        Objects.hashCode(null);
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        assertEquals("TestClass{}", helper.toString());

        helper = helper.add("field1", 1);
        assertEquals("TestClass{field1=1}", helper.toString());

        helper = helper.add("field2", "foo");
        assertEquals("TestClass{field1=1, field2=foo}", helper.toString());

        helper = helper.omitNullValues();
        helper = helper.add("field3", null);
        assertEquals("TestClass{field1=1, field2=foo}", helper.toString());
    }

    @Test
    public void testToStringHelperAddValues() {
        Objects.ToStringHelper helper = Objects.toStringHelper(String.class);
        helper = helper.addValue("value1");
        assertEquals("String[value1]", helper.toString());

        helper = helper.addValue(true);
        assertEquals("String[value1, true]", helper.toString());

        helper = helper.addValue(1);
        assertEquals("String[value1, true, 1]", helper.toString());

        helper = helper.addValue(1.0);
        assertEquals("String[value1, true, 1, 1.0]", helper.toString());

        helper = helper.addValue('a');
        assertEquals("String[value1, true, 1, 1.0, a]", helper.toString());
    }

    @Test
    public void testToStringHelperAddString() {
        Objects.ToStringHelper helper = Objects.toStringHelper(String.class);
        helper = helper.add("field1", "value1");
        assertEquals("String[field1=value1]", helper.toString());
    }

    @Test
    public void testToStringHelperAddBoolean() {
        Objects.ToStringHelper helper = Objects.toStringHelper(String.class);
        helper = helper.add("field1", true);
        assertEquals("String[field1=true]", helper.toString());
    }

    @Test
    public void testToStringHelperAddChar() {
        Objects.ToStringHelper helper = Objects.toStringHelper(String.class);
        helper = helper.add("field1", 'a');
        assertEquals("String[field1=a]", helper.toString());
    }

    @Test
    public void testToStringHelperAddDouble() {
        Objects.ToStringHelper helper = Objects.toStringHelper(String.class);
        helper = helper.add("field1", 1.0);
        assertEquals("String[field1=1.0]", helper.toString());
    }

    @Test
    public void testToStringHelperAddFloat() {
        Objects.ToStringHelper helper = Objects.toStringHelper(String.class);
        helper = helper.add("field1", 1.0f);
        assertEquals("String[field1=1.0]", helper.toString());
    }

    @Test
    public void testToStringHelperAddInt() {
        Objects.ToStringHelper helper = Objects.toStringHelper(String.class);
        helper = helper.add("field1", 1);
        assertEquals("String[field1=1]", helper.toString());
    }

    @Test
    public void testToStringHelperAddLong() {
        Objects.ToStringHelper helper = Objects.toStringHelper(String.class);
        helper = helper.add("field1", 1L);
        assertEquals("String[field1=1]", helper.toString());
    }
}