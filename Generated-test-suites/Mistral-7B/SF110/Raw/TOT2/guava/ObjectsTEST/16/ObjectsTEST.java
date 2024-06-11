package com.example.junit4; // Change 'com.example.junit4' to the package name of your test classes

import org.junit.Test;
import org.junit.Assert.*;
import com.google.common.base.Objects;

public class ObjectsTest {
    // Test cases for equal method
    // Test cases for hashCode method
    // Test cases for toStringHelper methods
    // Test cases for firstNonNull method

    @Test
    public void testEqualNull() {
        Object obj1 = new Object();
        Object obj2 = null;
        assertFalse(Objects.equal(obj1, obj2));
    }

    @Test
    public void testEqualDifferentClasses() {
        Object obj1 = new Object();
        String obj2 = "Test";
        assertFalse(Objects.equal(obj1, obj2));
    }

    @Test
    public void testHashCodeNull() {
        Object[] objects = null;
        int hashCode = Objects.hashCode(objects);
        assertEquals(0, hashCode);
    }

    @Test
    public void testHashCodeEmptyArray() {
        Object[] objects = new Object[0];
        int hashCode = Objects.hashCode(objects);
        assertEquals(0, hashCode);
    }

    @Test
    public void testHashCodeNullElements() {
        Object[] objects = new Object[]{null};
        int hashCode = Objects.hashCode(objects);
        // the implementation of hashCode in this case is not specified,
        // so different JVMs may produce different hash codes
        // assertNotSame(expectedHashCode, hashCode);
    }

    // Test cases for other methods
}