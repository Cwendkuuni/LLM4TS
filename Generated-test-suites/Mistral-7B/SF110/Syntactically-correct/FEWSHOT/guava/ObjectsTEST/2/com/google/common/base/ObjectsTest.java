package com.google.common.base;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertFalse(Objects.equal(null, 1));
        assertFalse(Objects.equal(1, null));
        assertFalse(Objects.equal(null, null));
        assertTrue(Objects.equal(1, 1));
        assertTrue(Objects.equal("foo", "foo"));
        assertTrue(Objects.equal(new Object(), new Object()));
    }

    @Test
    public void testHashCode() {
        Object[] objects = new Object[10];
        Arrays.fill(objects, "foo");
        int hashCode = Objects.hashCode(objects);
        assertNotSame(hashCode, Objects.hashCode(Objects.EMPTY_STRING_ARRAY));
        assertNotSame(hashCode, Objects.hashCode(new Object[0]));
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("MyClass");
        helper.add("field1", 1);
        helper.add("field2", "bar");
        assertEquals("MyClass{field1=1, field2=bar}", helper.toString());

        Objects.ToStringHelper helper2 = Objects.toStringHelper("MyClass").omitNullValues();
        helper2.add("field1", null);
        helper2.add("field2", "bar");
        assertEquals("MyClass{field2=bar}", helper2.toString());
    }

    @Test
    public void testToStringHelperAddValue() {
        Objects.ToStringHelper helper = Objects.toStringHelper("MyClass");
        helper.addValue(1);
        helper.addValue(true);
        helper.addValue("foo");
        assertEquals("MyClass{1, true, foo}", helper.toString());
    }

    @Test
    public void testToStringHelperAdd() {
        Objects.ToStringHelper helper = Objects.toStringHelper("MyClass");
        helper.add("field1", "foo");
        helper.add("field2", 1);
        assertEquals("MyClass{field1=foo, field2=1}", helper.toString());
    }

    @Test
    public void testToStringHelperAddNull() {
        Objects.ToStringHelper helper = Objects.toStringHelper("MyClass");
        helper.add("field1", null);
        helper.add("field2", "bar");
        assertEquals("MyClass{field2=bar}", helper.toString());
    }
}