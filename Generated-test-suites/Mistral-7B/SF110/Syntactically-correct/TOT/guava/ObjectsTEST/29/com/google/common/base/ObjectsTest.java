package com.example.junit4;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {
    @Test
    public void testEqual() {
        // Test with null objects
        assertEquals(true, Objects.equal(null, null));
        assertEquals(false, Objects.equal(null, new Object()));
        assertEquals(false, Objects.equal(new Object(), null));

        // Test with same object
        Object obj1 = new Object();
        assertEquals(true, Objects.equal(obj1, obj1));

        // Test with equal objects
        Object obj2 = new Object();
        assertEquals(true, Objects.equal(obj1, obj2));
        assertEquals(true, Objects.equal(obj2, obj1));

        // Test with different objects
        Object obj3 = new Object();
        assertEquals(false, Objects.equal(obj1, obj3));
        assertEquals(false, Objects.equal(obj3, obj1));
    }

    @Test
    public void testHashCode() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();

        // Test with null array
        assertNull(Objects.hashCode(null));

        // Test with empty array
        assertEquals(0, Objects.hashCode());

        // Test with one object
        assertEquals(Objects.hashCode(obj1), Objects.hashCode(new Object[]{obj1}));

        // Test with two same objects
        assertEquals(Objects.hashCode(obj1, obj1), Objects.hashCode(new Object[]{obj1, obj1}));

        // Test with two different objects
        assertEquals(Objects.hashCode(obj1, obj2), Objects.hashCode(new Object[]{obj1, obj2}));

        // Test for hash collisions with same values but different objects
        Object obj4 = new Object();
        assertEquals(Objects.hashCode(obj1, obj3), Objects.hashCode(new Object[]{obj1, obj4}));

        // Test for hash collisions with different values but same object
        assertEquals(Objects.hashCode(obj1, obj3), Objects.hashCode(new Object[]{obj3, obj1}));
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");

        // Test constructors
        helper.omitNullValues();
        assertEquals("ClassName{}", helper.toString());

        // Test adding different types of values
        helper.add("name", "value1");
        helper.add("anotherName", 1);
        helper.add("anotherName2", 1.0);
        helper.addValue(true);

        // Test toString method
        assertEquals("ClassName{name=value1, anotherName=1, anotherName2=1.0, omitNullValues=true}", helper.toString());
    }
}