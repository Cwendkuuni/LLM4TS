package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));
        assertFalse(Objects.equal(new Object(), new Object()));

        Object obj1 = new Object();
        Object obj2 = new Object();
        assertTrue(Objects.equal(obj1, obj2));

        Object obj3 = new Object();
        obj3 = obj1;
        assertTrue(Objects.equal(obj1, obj3));
        assertTrue(Objects.equal(obj3, obj1));

        Object obj4 = new Object();
        obj4 = obj2;
        assertFalse(Objects.equal(obj1, obj4));
        assertFalse(Objects.equal(obj4, obj1));
    }

    @Test
    public void testHashCodeWithNullObjects() {
        int hashCode = Objects.hashCode();
        assertEquals(hashCode, Objects.hashCode());

        hashCode = Objects.hashCode(null);
        assertEquals(hashCode, Objects.hashCode(null));

        hashCode = Objects.hashCode(null, new Object());
        int expectedHashCode = Integer.hashCode(0) ^ Objects.hashCode(new Object());
        assertEquals(expectedHashCode, hashCode);

        hashCode = Objects.hashCode(new Object(), null);
        expectedHashCode = Objects.hashCode(new Object()) ^ Integer.hashCode(0);
        assertEquals(expectedHashCode, hashCode);

        hashCode = Objects.hashCode(new Object(), new Object());
        assertEquals(Objects.hashCode(new Object(), new Object()), Objects.hashCode(new Object(), new Object()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeWithInvalidArgument() {
        Objects.hashCode(new int[0]);
    }

    @Test
    public void testToStringHelperBasicUsage() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("ClassName")
                .add("name1", "value1")
                .add("name2", 2)
                .add("name3", 3.0);
        assertEquals("ClassName{name1=value1, name2=2, name3=3.0}", helper.toString());
    }

    @Test
    public void testToStringHelperOmitNullValues() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("ClassName")
                .omitNullValues()
                .add("name1", "value1")
                .add("name2", null)
                .add("name3", null)
                .add("name4", 4);
        assertEquals("ClassName{name1=value1, name4=4}", helper.toString());
    }

    @Test
    public void testToStringHelperAddVariousTypes() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("ClassName")
                .add("name1", "value1")
                .add("name2", false)
                .add("name3", '\u0001')
                .add("name4", 4.0)
                .add("name5", 5)
                .add("name6", 5L)
                .add("name7", new Object());
        assertEquals("ClassName{name1=value1, name2=false, name3=\u0001, name4=4.0, name5=5, name6=5, name7=ClassName@4d5674f2}", helper.toString());
    }

    @Test
    public void testToStringHelperAddVariousTypesWithNulls() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("ClassName")
                .omitNullValues()
                .add("name1", "value1")
                .add("name2", null)
                .add("name3", null)
                .add("name4", 4)
                .add("name5", null)
                .add("name6", null);
        assertEquals("ClassName{name1=value1, name4=4}", helper.toString());
    }
}