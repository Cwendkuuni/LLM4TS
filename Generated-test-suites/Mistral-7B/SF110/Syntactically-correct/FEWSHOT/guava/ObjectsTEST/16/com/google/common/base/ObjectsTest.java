package com.google.common.base;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Objects.hashCode;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertEquals(true, equal(null, null));
        assertEquals(true, equal(new Object(), new Object()));
        assertEquals(true, equal("foo", "foo"));
        assertEquals(false, equal(null, new Object()));
        assertEquals(false, equal("foo", "bar"));
        assertEquals(false, equal(1, "1"));
        assertEquals(false, equal(1.0, 1));
    }

    @Test
    public void testHashCode() {
        Object[] objects = new Object[10];
        Arrays.fill(objects, new Object());
        assertEquals(10, hashCode(objects));

        Object obj1 = new Object();
        Object obj2 = new Object();
        assertEquals(obj1.hashCode(), obj1.hashCode());
        assertEquals(obj1.hashCode(), hashCode(obj1));
        assertEquals(obj2.hashCode(), obj2.hashCode());
        assertEquals(obj2.hashCode(), hashCode(obj2));
        assertEquals(hashCode(obj1), hashCode(obj2));

        String str1 = "foo";
        String str2 = "foo";
        assertEquals(str1.hashCode(), str1.hashCode());
        assertEquals(str1.hashCode(), hashCode(str1));
        assertEquals(str2.hashCode(), str2.hashCode());
        assertEquals(str2.hashCode(), hashCode(str2));
        assertEquals(hashCode(str1), hashCode(str2));

        int i = 1;
        assertEquals(i, i.hashCode());
        assertEquals(i, hashCode(i));

        long l = 1L;
        assertEquals(l, l.hashCode());
        assertEquals(l, hashCode(l));
    }

    @Test
    public void testToStringHelper_omitNullValuesFalse() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("TestClass")
                .add("name", "value")
                .add("anotherName", null);
        assertEquals("TestClass{name=value, anotherName=}", helper.toString());
    }

    @Test
    public void testToStringHelper_omitNullValuesTrue() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("TestClass")
                .omitNullValues()
                .add("name", "value")
                .add("anotherName", null);
        assertEquals("TestClass{name=value}", helper.toString());
    }

    @Test
    public void testToStringHelper_addingVariousTypes() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("TestClass")
                .add("intValue", 1)
                .add("doubleValue", 1.0)
                .add("booleanValue", true)
                .add("charValue", 'A')
                .add("stringValue", "value")
                .add("anotherStringValue", null);
        assertEquals("TestClass{intValue=1, doubleValue=1.0, booleanValue=true, charValue=A, stringValue=value}", helper.toString());
    }

    @Test
    public void testToStringHelper_addingVariousTypesWithOmitNullValues() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("TestClass")
                .omitNullValues()
                .add("intValue", 1)
                .add("doubleValue", 1.0)
                .add("booleanValue", true)
                .add("charValue", 'A')
                .add("stringValue", "value")
                .add("anotherStringValue", null);
        assertEquals("TestClass{intValue=1, doubleValue=1.0, booleanValue=true, charValue=A, stringValue=value}", helper.toString());
    }

    @Test
    public void testToStringHelper_addingArray() {
        Object[] array = new Object[] {1, "value", true};
        Objects.ToStringHelper helper = new Objects.ToStringHelper("TestClass")
                .add("array", array);
        assertEquals("TestClass{array=[1, value, true]}", helper.toString());
    }
}