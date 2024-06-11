package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        Object obj3 = new Object();

        assertTrue(Objects.equal(obj1, obj2));
        assertTrue(Objects.equal(obj2, obj1));
        assertFalse(Objects.equal(obj1, obj3));
        assertFalse(Objects.equal(obj3, obj1));
        assertFalse(Objects.equal(null, obj1));
        assertFalse(Objects.equal(obj1, null));
    }

    @Test(expected = NullPointerException.class)
    public void testEqualNullPointerException() {
        Objects.equal(null, null);
    }

    @Test
    public void testHashCode() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();

        int hashCode1 = Objects.hashCode(obj1);
        int hashCode2 = Objects.hashCode(obj2);
        int hashCode3 = Objects.hashCode(obj3);
        int hashCodeObjects = Objects.hashCode(obj1, obj2, obj3);

        assertNotSame(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCodeObjects);
        assertNotSame(hashCode2, hashCodeObjects);
    }

    @Test
    public void testToStringHelperBasicUsage() {
        Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName")
                .add("name1", "value1")
                .add("name2", 2)
                .addValue(true)
                .addValue(3.14);

        assertEquals("ClassName{name1=value1, name2=2, omitNullValues=true}", toStringHelper.toString());
    }

    @Test
    public void testToStringHelperNullValue() {
        Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName")
                .add("name1", "value1")
                .add("name2", null);

        assertEquals("ClassName{name1=value1, name2=null}", toStringHelper.toString());
    }

    @Test
    public void testToStringHelperEmptyList() {
        Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName")
                .omitNullValues();

        assertEquals("ClassName{}", toStringHelper.toString());
    }
}

class Object {
    // empty class for JUnit test purposes
}