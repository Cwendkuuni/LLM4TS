package com.google.common.base;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Objects.hashCode;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertFalse(Objects.equal(null, null));
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));
        assertFalse(Objects.equal(new Object(), new Object()));
        assertTrue(Objects.equal(new Object(), new Object())); // same instance
        assertTrue(Objects.equal(new Object(), new Object())); // equal values
    }

    @Test
    public void testHashCode() {
        Object[] objects = new Object[5];
        Arrays.fill(objects, new Object());
        int hashCode = Objects.hashCode(objects);
        int hashCode2 = Objects.hashCode(Objects.concat(objects, new Object[1]));
        assertEquals(hashCode, Objects.hashCode(objects));
        assertEquals(hashCode2, Objects.hashCode(Objects.concat(objects, new Object[1])));
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("TestClass")
                .add("name", "John Doe")
                .add("age", 30)
                .addValue(3.14)
                .omitNullValues();
        assertEquals("TestClass{name=John Doe, age=30, }", helper.toString());
    }

    @Test
    public void testAddVariousTypes() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("TestClass")
                .add("name", "John Doe")
                .add("age", 30)
                .addValue(3.14)
                .addValue(true)
                .addValue('A')
                .addValue(new Object());
        assertEquals("TestClass{name=John Doe, age=30, 3.14=, .=true, A=, =,}", helper.toString());
    }

    @Test
    public void testToStringHelperNullValues() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("TestClass")
                .add("name", "John Doe")
                .add("age", null)
                .omitNullValues();
        assertEquals("TestClass{name=John Doe}", helper.toString());
    }

    @Test
    public void testToStringHelperEmpty() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("TestClass");
        assertEquals("TestClass{}", helper.toString());
    }
}