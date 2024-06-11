
package com.example.test;

import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {
    @Test
    public void testEqual() {
        // Test with null objects
        assertFalse(Objects.equal(null, null)); // true according to method definition, but this is not the expected behavior for equals method
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));

        // Test with same object
        Object obj1 = new Object();
        assertTrue(Objects.equal(obj1, obj1));

        // Test with non-null and equal objects
        Object obj2 = new Object();
        assertTrue(Objects.equal(obj1, obj2));
        assertTrue(Objects.equal(obj2, obj1));

        // Test with non-null but not equal objects
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
        Object a = new Object();
        Object b = new Object();
        Object c = new Object();

        // Test with empty array
        assertEquals(0, Objects.hashCode());

        // Test with single object
        assertEquals(Objects.hashCode(a), Objects.hashCode(a));

        // Test with multiple objects
        assertEquals(Objects.hashCode(a, b), Objects.hashCode(b, a)); // Different order
        assertEquals(Objects.hashCode(a, b), Objects.hashCode(a, c) ^ Objects.hashCode(c, b)); // Different objects

        // Test with null object
        assertNotEquals(Objects.hashCode(a, null), Objects.hashCode(a, b));
    }

    @Test
    public void testToStringHelper() {
        String className = "com.example.MyClass";
        Objects.ToStringHelper stringHelper = Objects.toStringHelper(className);

        // Test adding different types of values
        stringHelper.add("name", "John Doe");
        stringHelper.add("age", 30);
        stringHelper.add("isMarried", true);
        stringHelper.add("isStudent", false);

        // Test omitting null values
        Objects.ToStringHelper stringHelper2 = stringHelper.omitNullValues();
        stringHelper2.add("address", null);

        // Test toString method
        String expectedString = String.format("%s{name=John Doe, age=30, isMarried=true, isStudent=false}");
        String actualString = stringHelper.toString();
        assertEquals(expectedString, actualString);

        String expectedString2 = String.format("%s{name=John Doe, age=30, isMarried=true}");
        String actualString2 = stringHelper2.toString();
        assertEquals(expectedString2, actualString2);
    }
}