package com.example.junit4;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.Objects;

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

        // Test with same non-null objects
        Object obj2 = new Object();
        assertTrue(Objects.equal(obj1, obj2) || Objects.equal(obj2, obj1));

        // Test with non-equal objects
        Object obj3 = new Object() {
            @Override
            public boolean equals(Object o) {
                return false;
            }
        };
        assertFalse(Objects.equal(obj1, obj3));
        assertFalse(Objects.equal(obj3, obj1));
    }

    @Test
    public void testHashCode() {
        Object[] objects = new Object[5];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = new Object();
        }

        int hashCode = Objects.hashCode(objects);
        int expectedHashCode = Arrays.hashCode(objects);
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper stringHelper = new Objects.ToStringHelper("ClassName")
                .add("name1", "value1")
                .add("name2", 2)
                .add("name3", null)
                .omitNullValues();

        String expectedString = "ClassName{" +
                "name1=value1," +
                "name2=2" +
                '}';

        String actualString = stringHelper.toString();
        assertEquals(expectedString, actualString);
    }
}