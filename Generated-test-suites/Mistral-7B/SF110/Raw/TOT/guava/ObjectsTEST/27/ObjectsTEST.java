package com.example.junit4;

import org.junit.Test;
import static org.junit.Assert.*;
import static com.google.common.base.Objects.*;

public class ObjectsTest {
    @Test
    public void testEqual() {
        // Test with null objects
        assertFalse(Objects.equal(null, null));
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));

        // Test with same object
        Object obj1 = new Object();
        assertTrue(Objects.equal(obj1, obj1));

        // Test with non-null and equal objects
        Object obj2 = new Object();
        assertTrue(Objects.equal(obj1, obj2));
        assertTrue(Objects.equal(obj2, obj1));

        // Test with non-null and different objects
        Object obj3 = new Object() {
            @Override
            public boolean equals(Object obj) {
                return false;
            }
        };
        assertFalse(Objects.equal(obj1, obj3));
        assertFalse(Objects.equal(obj3, obj1));
    }

    @Test
    public void testHashCode() {
        Object[] objects = {new Object(), new Object(), new Object()};
        int expectedHashCode = Arrays.hashCode(objects);
        int actualHashCode = Objects.hashCode(objects);
        assertEquals(expectedHashCode, actualHashCode);

        Objects.hashCode(new Object[0]); // Test with empty array
        Objects.hashCode(new Object()); // Test with single object
        Objects.hashCode(null); // Test with null
        Objects.hashCode(new Object(), new Object()); // Test with two objects
    }

    @Test
    public void testToStringHelper() {
        String className = "com.example.MyClass";

        String expectedToString = "MyClass{foo=bar, baz=qux}";
        ToStringHelper helper = new ToStringHelper(className)
            .add("foo", "bar")
            .add("baz", "qux");
        String actualToString = helper.toString();
        assertEquals(expectedToString, actualToString);

        helper = new ToStringHelper(className)
            .add("foo", "bar")
            .add("baz")
            .omitNullValues();
        actualToString = helper.toString();
        assertEquals("MyClass{foo=bar}", actualToString);

        helper = new ToStringHelper(className)
            .add("foo", null)
            .add("baz", "qux");
        actualToString = helper.toString();
        assertEquals("MyClass{baz=qux}", actualToString);

        helper = new ToStringHelper(className)
            .add("foo", null)
            .add("baz", null)
            .omitNullValues();
        actualToString = helper.toString();
        assertEquals(className, actualToString);
    }
}