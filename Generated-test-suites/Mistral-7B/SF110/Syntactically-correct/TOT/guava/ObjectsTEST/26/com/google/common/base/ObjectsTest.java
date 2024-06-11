package com.google.common.base;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
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
        assertTrue(Objects.equal(obj1, obj2));
        assertTrue(Objects.equal(obj2, obj1));

        // Test with different objects
        Object obj3 = new Object();
        assertFalse(Objects.equal(obj1, obj3));
        assertFalse(Objects.equal(obj3, obj1));

        // Test with null and non-null objects
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));
        assertFalse(Objects.equal(null, "hello"));
        assertFalse(Objects.equal("hello", null));
    }

    @Test
    public void testHashCode() {
        Object a = new Object();
        Object b = new Object();
        Object c = new Object();

        // Test with empty array
        int hashCodeEmpty = Objects.hashCode();
        int hashCodeArray = Objects.hashCode(new Object[0]);
        assertEquals(hashCodeEmpty, hashCodeArray);

        // Test with single object
        int hashCodeSingleA = Objects.hashCode(a);
        int hashCodeSingleB = Objects.hashCode(b);
        assertNotEquals(hashCodeSingleA, hashCodeSingleB);

        // Test with two objects
        int hashCodeTwo = Objects.hashCode(a, b);
        int hashCodeTwoSwapped = Objects.hashCode(b, a);
        assertEquals(hashCodeTwo, hashCodeTwoSwapped);

        // Test with same object multiple times
        int hashCodeA = Objects.hashCode(a);
        int hashCodeAA = Objects.hashCode(a, a);
        assertEquals(hashCodeAA, hashCodeA);

        // Test with different objects
        int hashCodeAB = Objects.hashCode(a, b);
        int hashCodeBC = Objects.hashCode(b, c);
        assertNotEquals(hashCodeAB, hashCodeBC);

        // Test with null object
        int hashCodeNull = Objects.hashCode(a, null);
        int hashCodeNullSwapped = Objects.hashCode(null, a);
        assertEquals(hashCodeNull, hashCodeNullSwapped);
    }

    @Test
    public void testFirstNonNull() {
        Object obj1 = new Object();
        Object obj2 = null;
        Object obj3 = new Object();

        // Test with null first object
        assertEquals(obj1, Objects.firstNonNull(null, obj1));
        assertEquals(obj1, Objects.firstNonNull(obj1, null));

        // Test with null second object
        assertEquals(obj1, Objects.firstNonNull(obj1, obj2));

        // Test with non-null first object
        assertEquals(obj1, Objects.firstNonNull(obj1, obj3));

        // Test with non-null second object
        assertEquals(obj3, Objects.firstNonNull(obj2, obj3));
    }
}