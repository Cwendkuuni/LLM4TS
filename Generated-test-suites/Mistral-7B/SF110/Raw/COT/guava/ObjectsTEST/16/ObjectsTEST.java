package com.example.junit;

import com.google.common.base.Objects;
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        // Basic test case
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal(new Object(), new Object()));
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal("abc", "def"));

        // Edge cases and exception handling
        try {
            Objects.equal(null, new Object());
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }

        // Test with arrays
        Object[] array1 = new Object[0];
        Object[] array2 = new Object[0];
        assertTrue(Objects.equal(array1, array2));
        Object[] array3 = {new Object(), new Object()};
        Object[] array4 = {new Object(), new Object()};
        assertTrue(Objects.equal(array3, array4));
        Object[] array5 = {null, new Object()};
        Object[] array6 = {new Object(), null};
        assertFalse(Objects.equal(array5, array6));
    }

    @Test
    public void testHashCode() {
        // Basic test case
        Object[] objects1 = new Object[0];
        Object[] objects2 = new Object[0];
        assertEquals(Objects.hashCode(objects1), Objects.hashCode(objects2));
        Object[] objects3 = {new Object(), new Object()};
        Object[] objects4 = {new Object(), new Object()};
        assertEquals(Objects.hashCode(objects3), Objects.hashCode(objects4));

        // Edge cases and exception handling
        try {
            Objects.hashCode(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }

        // Test with different types
        assertEquals(Objects.hashCode("abc"), 3416454611L);
        assertEquals(Objects.hashCode(123), 123);
        assertEquals(Objects.hashCode(123.0), 732536571L);
        assertEquals(Objects.hashCode(true), 1);
    }

    @Test
    public void testToStringHelper_omitNullValues_default() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Classname");
        helper.add("field1", "value1");
        helper.add("field2", null);
        assertEquals("Classname{field1=value1, field2=}", helper.toString());
    }

    @Test
    public void testToStringHelper_omitNullValues_true() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Classname").omitNullValues();
        helper.add("field1", "value1");
        helper.add("field2", null);
        assertEquals("Classname{field1=value1}", helper.toString());
    }
}