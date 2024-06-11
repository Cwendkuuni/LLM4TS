package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectsTest {

    

    // equal method test case
    @Test
    public void testEqual() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        Object obj3 = new Object();

        assertTrue(Objects.equal(obj1, obj2));
        assertFalse(Objects.equal(obj1, obj3));
        assertFalse(Objects.equal(null, obj1));
        assertFalse(Objects.equal(obj1, null));
    }

    // hashCode method test case
    @Test
    public void testHashCode() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        assertEquals(Objects.hashCode(obj1), Objects.hashCode(obj1));
        assertEquals(Objects.hashCode(obj1, obj2), Objects.hashCode(obj2, obj1));
        assertNotEquals(Objects.hashCode(obj1), Objects.hashCode(new Object()));
        assertArrayEquals(new int[]{Objects.hashCode(obj1), Objects.hashCode(obj2)}, new int[]{Objects.hashCode(obj1, obj2), Objects.hashCode(obj2, obj1)});
    }

    // toStringHelper deprecated methods test case
    @Test
    public void testToStringHelperDeprecatedMethods() {
        Object obj = new Object();

        assertSame(Objects.toStringHelper(obj).omitNullValues(), Objects.toStringHelper(obj).omitNullValues());
        assertSame(Objects.toStringHelper(obj.getClass()).omitNullValues(), Objects.toStringHelper(obj.getClass()).omitNullValues());
        assertSame(Objects.toStringHelper("ClassName").omitNullValues(), Objects.toStringHelper("ClassName").omitNullValues());

        Objects.toStringHelper(obj).add("name1", "value1");
        Objects.toStringHelper(obj.getClass()).add("name2", "value2");
        Objects.toStringHelper("ClassName").add("name3", "value3");

        assertEquals(Objects.toStringHelper(obj).toString(), "ClassName{name1=value1}");
        assertEquals(Objects.toStringHelper(obj.getClass()).toString(), "ClassName{name2=value2}");
        assertEquals(Objects.toStringHelper("ClassName").toString(), "ClassName{name3=value3}");
    }

    // firstNonNull method test case
    @Test
    public void testFirstNonNull() {
        Object obj1 = new Object();
        Object obj2 = null;
        String str1 = "String1";
        String str2 = "String2";

        assertEquals(Objects.firstNonNull(obj1, obj2), obj1);
        assertEquals(Objects.firstNonNull(obj1, str1), obj1);
        assertEquals(Objects.firstNonNull(obj1, str2), obj1);
        assertEquals(Objects.firstNonNull(null, obj2), obj2);
        assertEquals(Objects.firstNonNull(null, str1), str1);
        assertEquals(Objects.firstNonNull(null, str2), str2);
    }

    // ToStringHelper add methods test case
    @Test
    public void testToStringHelperAddMethods() {
        Object obj = new Object();

        Objects.ToStringHelper helper = Objects.toStringHelper(obj).omitNullValues();

        helper.add("name1", "value1");
        helper.add("name2", null);
        helper.add("name3", "value3");

        assertEquals(helper.toString(), "Object{name1=value1, name3=value3}");
    }

    
}