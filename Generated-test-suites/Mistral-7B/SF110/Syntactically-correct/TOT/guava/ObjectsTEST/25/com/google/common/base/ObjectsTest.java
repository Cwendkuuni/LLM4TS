package com.google.common.base;

import static org.junit.Assert.*; // for Assert.assertEquals and Assert.assertFalse
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

        // Test with equal non-null objects
        Object obj2 = new Object();
        assertTrue(Objects.equal(obj1, obj2));
        assertTrue(Objects.equal(obj2, obj1));

        // Test with non-equal non-null objects
        Object obj3 = new Object() { /* different implementation */ };
        assertFalse(Objects.equal(obj1, obj3));
        assertFalse(Objects.equal(obj3, obj1));
    }

    @Test
    public void testHashCode() {
        Object[] objects = new Object[10];

        // Test with empty array
        assertEquals(0, Objects.hashCode(new Object[0]));

        // Test with single object
        Object obj1 = new Object();
        int hashCode1 = Objects.hashCode(obj1);
        objects[0] = obj1;

        // Test with multiple objects
        for (int i = 1; i < objects.length; i++) {
            Object obj2 = new Object() { /* different implementation */ };
            objects[i] = obj2;
            int hashCode2 = Objects.hashCode(objects);
            assertNotEquals(hashCode1, hashCode2);
        }

        // Test with same objects
        Object obj3 = objects[0];
        int hashCode3 = Objects.hashCode(objects);
        int hashCode4 = Objects.hashCode(obj3, objects[1], objects[2], objects[3], objects[4], objects[5], objects[6], objects[7], objects[8], objects[9]);
        assertEquals(hashCode3, hashCode4);
    }
}