package com.example.junit;

import static org.junit.Assert.*;
import org.junit.Test;

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

        // Test with equal objects
        Object obj2 = new Object();
        assertTrue(Objects.equal(obj1, obj2));
        assertTrue(Objects.equal(obj2, obj1));

        // Test with different objects
        Object obj3 = new Object() {
            @Override
            public boolean equals(Object o) {
                return false;
            }
        };
        assertFalse(Objects.equal(obj1, obj3));
        assertFalse(Objects.equal(obj3, obj1));

        // Test with different classes
        Object obj1String = "test";
        Object obj2Integer = 1;
        assertFalse(Objects.equal(obj1String, obj2Integer));
    }

    @Test
    public void testHashCode() {
        Object[] objects = new Object[10];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = new Object() {
                @Override
                public int hashCode() {
                    return i;
                }
            };
        }

        int hashCode = Objects.hashCode(objects);
        int expectedHashCode = 0;
        for (int i = 0; i < objects.length; i++) {
            expectedHashCode = 31 * expectedHashCode + i;
        }
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testToStringHelper() {
        Object obj = new Object() {
            @Override
            public String toString() {
                return "Test Object";
            }
        };

        String className = Objects.class.getName();
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper(obj);
        toStringHelper.add("name", "Test Name");
        toStringHelper.add("value", 123);

        String expectedToString = String.format("%s{name=Test Name, value=123}", className);
        String toString = toStringHelper.toString();
        assertEquals(expectedToString, toString);

        toStringHelper.omitNullValues();
        toStringHelper.add("value", null);
        toString = toStringHelper.toString();
        assertTrue(toString.contains("name=") && toString.contains("value=") && toString.contains("omitNullValues=false"));
    }
}