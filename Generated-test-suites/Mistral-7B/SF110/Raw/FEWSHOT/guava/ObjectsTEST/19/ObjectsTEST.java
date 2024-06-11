package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertFalse(Objects.equal(null, null));
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));
        assertFalse(Objects.equal(new Object(), new Object()));
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = obj1;
        assertTrue(Objects.equal(obj1, obj3));
        assertTrue(Objects.equal(obj1, obj1));
        assertTrue(Objects.equal(obj1, obj2)); // Objects are equal by default
        assertFalse(Objects.equal(obj1, new Object())); // Different objects
    }

    @Test
    public void testHashCode() {
        Object[] objArray = new Object[10];
        Arrays.fill(objArray, new Object());
        int expectedHashCode = Arrays.hashCode(objArray);
        int actualHashCode = Objects.hashCode(objArray);
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test(expected = NullPointerException.class)
    public void testHashCodeNull() {
        Objects.hashCode(null);
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper("Class");
        String str = toStringHelper.omitNullValues().add("name", "value").toString();
        assertEquals("Class{name=value}", str);

        toStringHelper = Objects.toStringHelper("Class").omitNullValues();
        str = toStringHelper.add("name", null).toString();
        assertEquals("Class{}", str);

        toStringHelper = Objects.toStringHelper("Class").omitNullValues();
        str = toStringHelper.add("name", null).add("anotherName", "value").toString();
        assertEquals("Class{anotherName=value}", str);
    }

    @Test
    public void testToStringHelperAddValue() {
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper(new Object());
        String str = toStringHelper.addValue(true).toString();
        assertEquals("Object{}", str);

        toStringHelper = Objects.toStringHelper(new Object()).omitNullValues();
        str = toStringHelper.addValue(null).toString();
        assertEquals("Object{}", str);

        toStringHelper = Objects.toStringHelper(new Object()).omitNullValues();
        str = toStringHelper.addValue(true).addValue(1).toString();
        assertEquals("Object{true=1}", str);
    }

    @Test
    public void testToStringHelperAdd() {
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper(new Object());
        toStringHelper = toStringHelper.add("name", "value");
        String str = toStringHelper.toString();
        assertEquals("Object{name=value}", str);

        toStringHelper = Objects.toStringHelper(new Object()).omitNullValues();
        toStringHelper = toStringHelper.add("name", null);
        str = toStringHelper.toString();
        assertEquals("Object{}", str);

        toStringHelper = Objects.toStringHelper(new Object()).omitNullValues();
        toStringHelper = toStringHelper.add("name", null).add("anotherName", "value");
        str = toStringHelper.toString();
        assertEquals("Object{anotherName=value}", str);
    }

    @Test
    public void testToStringHelperValueHolder() {
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper("Class");
        toStringHelper = toStringHelper.addHolder(new Object());
        String str = toStringHelper.toString();
        assertEquals("Class{}", str);

        toStringHelper = Objects.toStringHelper("Class").omitNullValues();
        toStringHelper = toStringHelper.addHolder(null);
        str = toStringHelper.toString();
        assertEquals("Class{}", str);

        toStringHelper = Objects.toStringHelper("Class").omitNullValues();
        toStringHelper = toStringHelper.addHolder("name", new Object()).addHolder("anotherName", "value");
        str = toStringHelper.toString();
        assertEquals("Class{anotherName=value, name=}", str);
    }
}