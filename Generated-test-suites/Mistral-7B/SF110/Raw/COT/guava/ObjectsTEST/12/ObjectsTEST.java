package com.example.junit.tests.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.Objects;

public class ObjectsTest {

    // Equal method tests

    @Test
    public void testEqualNullNull() {
        assertTrue(Objects.equal(null, null));
    }

    @Test
    public void testEqualNullObject() {
        assertFalse(Objects.equal(null, new Object()));
    }

    @Test
    public void testEqualSameObject() {
        Object obj = new Object();
        assertTrue(Objects.equal(obj, obj));
    }

    @Test
    public void testEqualDifferentObjectsWithSameValues() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        obj1 = "same value";
        obj2 = "same value";
        assertTrue(Objects.equal(obj1, obj2));
    }

    @Test
    public void testEqualDifferentObjectsWithDifferentValues() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        obj1 = "different value";
        assertFalse(Objects.equal(obj1, obj2));
    }

    // hashCode method tests

    @Test
    public void testHashCodeNullArray() {
        assertEquals(0, Objects.hashCode(null));
    }

    @Test
    public void testHashCodeEmptyArray() {
        Object[] emptyArray = new Object[0];
        assertEquals(0, Objects.hashCode(emptyArray));
    }

    @Test
    public void testHashCodeSingleObject() {
        Object obj = new Object();
        long expectedHashCode = obj.hashCode();
        long actualHashCode = Objects.hashCode(obj);
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testHashCodeMultipleObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        long expectedHashCode = obj1.hashCode() ^ obj2.hashCode();
        long actualHashCode = Objects.hashCode(obj1, obj2);
        assertEquals(expectedHashCode, actualHashCode);
    }

    // toStringHelper deprecated methods tests

    @Test(expected = DeprecationException.class)
    public void testToStringHelperObjectDeprecated() {
        Objects.toStringHelper(new Object());
    }

    @Test(expected = DeprecationException.class)
    public void testToStringHelperClassDeprecated() {
        Objects.toStringHelper(Object.class);
    }

    @Test(expected = DeprecationException.class)
    public void testToStringHelperStringDeprecated() {
        Objects.toStringHelper("ClassName");
    }

    // toStringHelper add tests

    @Test
    public void testToStringHelperAddStringString() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");
        helper.add("name1", "value1");
        assertEquals("ClassName{name1=value1}", helper.toString());
    }

    @Test
    public void testToStringHelperAddStringBoolean() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");
        helper.add("name1", true);
        assertEquals("ClassName{name1=true}", helper.toString());
    }

    @Test
    public void testToStringHelperAddStringChar() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");
        helper.add("name1", 'c');
        assertEquals("ClassName{name1=c}", helper.toString());
    }

    @Test
    public void testToStringHelperAddStringDouble() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");
        helper.add("name1", 1.2);
        assertEquals("ClassName{name1=1.2}", helper.toString());
    }

    @Test
    public void testToStringHelperAddStringFloat() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");
        helper.add("name1", 1.2f);
        assertEquals("ClassName{name1=1.2}", helper.toString());
    }

    @Test
    public void testToStringHelperAddStringInt() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");
        helper.add("name1", 1);
        assertEquals("ClassName{name1=1}", helper.toString());
    }

    @Test
    public void testToStringHelperAddStringLong() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");
        helper.add("name1", 1L);
        assertEquals("ClassName{name1=1}", helper.toString());
    }

    @Test
    public void testToStringHelperAddObject() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");
        Object obj = new Object();
        helper.addValue(obj);
        assertEquals("ClassName{name1=" + obj.toString() + "}", helper.toString());
    }

    @Test
    public void testToStringHelperAddBoolean() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");
        helper.omitNullValues();
        helper.add("name1", true);
        assertEquals("ClassName{name1=true}", helper.toString());
    }

    @Test
    public void testToStringHelperAddNullValue() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");
        helper.add("name1", null);
        assertEquals("ClassName{name1=null}", helper.toString());
    }
}