package com.example.test;

import org.junit.Assert;
import static org.junit.Assert.*;

public class ObjectsTest {
    @Test
    public void testEqual() {
        // Test with null objects
        assertFalse(Objects.equal(null, null));
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));

        // Test with equal objects
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertTrue(Objects.equal(obj1, obj2));

        // Test with non-equal objects
        Object obj3 = new Object();
        assertFalse(Objects.equal(obj1, obj3));

        // Test with objects of different classes
        String str1 = "test";
        String str2 = "anotherTest";
        assertFalse(Objects.equal(str1, str2));

        // Test with objects having equal values
        Integer int1 = 1;
        Integer int2 = 1;
        assertTrue(Objects.equal(int1, int2));
    }

    @Test
    public void testHashCode() {
        // Test with an empty array
        assertNotNull(Objects.hashCode(new Object[0]));

        // Test with arrays containing different objects
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object[] array1 = {obj1, new Object(), obj2};
        Object[] array2 = {new Object(), obj1, obj2};
        assertNotEquals(Objects.hashCode(array1), Objects.hashCode(array2));

        // Test with arrays containing the same objects
        Object[] array3 = {obj1, obj1, obj1};
        assertEquals(Objects.hashCode(array3), Objects.hashCode(array3));

        // Test with arrays containing null values
        Object[] array4 = {null, obj1, null};
        assertNotEquals(Objects.hashCode(array4), Objects.hashCode(array1));
    }

    @Test
    public void testToStringHelper() {
        // Test toStringHelper with an empty ToStringHelper
        String emptyToStringHelper = Objects.toStringHelper(null).toString();
        assertEquals("", emptyToStringHelper);

        // Test toStringHelper with a simple String value
        String str = "test";
        StringToStringHelper strToStringHelper = Objects.toStringHelper(str);
        assertEquals("test{}", strToStringHelper.toString());

        // Test toStringHelper with multiple values
        StringToStringHelper multipleValuesToStringHelper = strToStringHelper.add("name", "value");
        assertEquals("test{name=value}", multipleValuesToStringHelper.toString());

        // Test toStringHelper with null values
        StringToStringHelper nullValuesToStringHelper = strToStringHelper.omitNullValues().add("name", null);
        assertEquals("test{name=value}", nullValuesToStringHelper.toString());
    }
}