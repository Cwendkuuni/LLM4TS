
package com.google.common.base;

import com.google.common.annotations.*;
import javax.annotation.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        // Test with null arguments
        assertFalse(Objects.equal(null, null));
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));

        // Test with equal non-null arguments
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertTrue(Objects.equal(obj1, obj2));

        // Test with non-equal arguments
        Object obj3 = new Object();
        assertFalse(Objects.equal(obj1, obj3));
    }

    @Test
    public void testHashCodeEmptyArray() {
        int hashCode = Objects.hashCode();
        assertEquals(0, hashCode);
    }

    @Test
    public void testHashCodeSingleElementArray() {
        Object obj = new Object();
        int hashCode = Objects.hashCode(obj);
        int expectedHashCode = obj.hashCode();
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testHashCodeMultipleElementsArray() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        int hashCode = Objects.hashCode(obj1, obj2);
        int expectedHashCode = obj1.hashCode() ^ obj2.hashCode();
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testHashCodeNullElementArray() {
        Object obj1 = new Object();
        Object obj2 = null;
        int hashCode = Objects.hashCode(obj1, obj2);
        int expectedHashCode = obj1.hashCode();
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testHashCodeDifferentTypesArray() {
        Object obj1 = new Object();
        Object obj2 = new String("test");
        int hashCode = Objects.hashCode(obj1, obj2);
        int expectedHashCode = obj1.hashCode() ^ obj2.hashCode();
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testToStringHelperDefaultUsage() {
        Objects.ToStringHelper stringHelper = Objects.toStringHelper(new Object());
        String expectedToString = "Object{}";
        String toString = stringHelper.toString();
        assertEquals(expectedToString, toString);
    }

    @Test
    public void testToStringHelperAddingValues() {
        Objects.ToStringHelper stringHelper = Objects.toStringHelper(new Object());
        stringHelper.add("name", "value");
        stringHelper.add("anotherName", 123);
        stringHelper.addValue(new Object());
        String expectedToString = "Object{name=value, anotherName=123, }";
        String toString = stringHelper.toString();
        assertEquals(expectedToString, toString);
    }

    @Test
    public void testToStringHelperOmitNullValues() {
        Objects.ToStringHelper stringHelper = Objects.toStringHelper(new Object());
        stringHelper.omitNullValues();
        String name = null;
        stringHelper.add("name", name);
        String expectedToString = "Object{}";
        String toString = stringHelper.toString();
        assertEquals(expectedToString, toString);
    }
}