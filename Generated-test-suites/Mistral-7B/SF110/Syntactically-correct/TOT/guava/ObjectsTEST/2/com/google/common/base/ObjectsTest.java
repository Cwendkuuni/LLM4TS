package com.example.junit4;

import com.google.common.base.Objects;
import org.junit.Test;
import static org.junit.Assert.*;

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
        obj1 = obj2;
        assertTrue(Objects.equal(obj1, obj2));

        // Test with non-equal objects
        Object obj3 = new Object() {
            @Override
            public boolean equals(Object o) {
                return false;
            }
        };
        assertFalse(Objects.equal(obj1, obj3));
    }

    @Test
    public void testHashCode() {
        Object[] objects = new Object[5];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = new Object();
        }

        // Test with empty array
        int hashCode = Objects.hashCode();
        int emptyArrayHashCode = Objects.hashCode();
        assertNotSame(hashCode, emptyArrayHashCode);

        // Test with single object
        int singleObjectHashCode = Objects.hashCode(objects[0]);
        assertEquals(singleObjectHashCode, Objects.hashCode(objects[0]));

        // Test with multiple objects
        int multipleObjectsHashCode = Objects.hashCode(objects);
        assertNotEquals(singleObjectHashCode, multipleObjectsHashCode);

        // Test with null object
        Object nullObject = null;
        int nullObjectHashCode = Objects.hashCode(nullObject);
        int hashCodeWithNull = Objects.hashCode(nullObject, objects[0]);
        assertNotEquals(nullObjectHashCode, hashCodeWithNull);
    }

    @Test
    public void testToStringHelper() {
        Object obj = new Object() {
            @Override
            public String toString() {
                return "Test Object";
            }
        };

        ToStringHelper helper = Objects.toStringHelper(obj);

        // Test with adding values
        helper.add("Name", "John Doe");
        helper.add("Age", 30);
        helper.omitNullValues();

        String expectedToString = "Test Object{Name=John Doe, Age=30}";
        assertEquals(expectedToString, helper.toString());

        // Test with adding null value
        helper.add("Height", null);
        helper.omitNullValues();

        expectedToString = "Test Object{Name=John Doe, Age=30}";
        assertEquals(expectedToString, helper.toString());

        // Test with adding multiple null values
        helper.add("Weight", null);
        helper.add("City", null);
        helper.omitNullValues();

        expectedToString = "Test Object{Name=John Doe, Age=30}";
        assertEquals(expectedToString, helper.toString());

        // Test with adding non-null value
        helper.addValue("Height", 180);
        expectedToString = "Test Object{Name=John Doe, Age=30, Height=180}";
        assertEquals(expectedToString, helper.toString());
    }
}