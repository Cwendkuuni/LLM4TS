package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertEquals(true, Objects.equal(null, null));
        assertEquals(true, Objects.equal(new Object(), new Object()));
        assertEquals(true, Objects.equal("foo", "foo"));
        assertEquals(false, Objects.equal(new Object(), "foo"));
        assertEquals(false, Objects.equal("foo", new Object()));
        assertEquals(false, Objects.equal(null, new Object()));
    }

    @Test
    public void testHashCode() {
        Object[] objects = new Object[10];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = Integer.valueOf(i);
        }
        int hashCode = Objects.hashCode(objects);
        Objects objects2 = new Object[10];
        for (int i = 0; i < objects2.length; i++) {
            objects2[i] = Integer.valueOf(i + 10);
        }
        int hashCode2 = Objects.hashCode(objects2);
        assertEquals(hashCode, Arrays.hashCode(objects));
        assertNotSame(hashCode, hashCode2);
    }

    @Test(expected = NullPointerException.class)
    public void testHashCodeNullArray() {
        Objects.hashCode(null);
    }

    @Test
    public void testToStringHelper() {
        String s = Objects.toStringHelper(new Object()).toString();
        assertEquals("Object{}", s);

        String s1 = Objects.toStringHelper(new Object()).omitNullValues().add("name", "value").toString();
        assertEquals("Object{name=value}", s1);

        String s2 = Objects.toStringHelper(new Object()).add("name1", null).add("name2", "value").toString();
        assertEquals("Object{name1=null, name2=value}", s2);
    }

    @Test
    public void testToStringHelperOmmitNullValues() {
        String s = Objects.toStringHelper(new Object()).omitNullValues().add("name", null).toString();
        assertEquals("Object{}", s);
    }

    @Test
    public void testToStringHelperAddValue() {
        String s = Objects.toStringHelper(new Object()).addValue("value").toString();
        assertEquals("Object{value=value}", s);
    }

    @Test
    public void testToStringHelperAddValueBoolean() {
        String s = Objects.toStringHelper(new Object()).addValue(true).toString();
        assertEquals("Object{value=true}", s);
    }

    @Test
    public void testToStringHelperAddValueChar() {
        String s = Objects.toStringHelper(new Object()).addValue('c').toString();
        assertEquals("Object{value=c}", s);
    }

    @Test
    public void testToStringHelperAddValueDouble() {
        String s = Objects.toStringHelper(new Object()).addValue(3.14).toString();
        assertEquals("Object{value=3.14}", s);
    }

    @Test
    public void testToStringHelperAddValueFloat() {
        String s = Objects.toStringHelper(new Object()).addValue(3.14f).toString();
        assertEquals("Object{value=3.14}", s);
    }

    @Test
    public void testToStringHelperAddValueInt() {
        String s = Objects.toStringHelper(new Object()).addValue(3).toString();
        assertEquals("Object{value=3}", s);
    }

    @Test
    public void testToStringHelperAddValueLong() {
        String s = Objects.toStringHelper(new Object()).addValue(3L).toString();
        assertEquals("Object{value=3}", s);
    }

    @Test
    public void testToStringHelperAdd() {
        String s = Objects.toStringHelper(new Object()).add("name", "value").toString();
        assertEquals("Object{name=value}", s);

        String s1 = Objects.toStringHelper(new Object()).add("name1", null).add("name2", "value").toString();
        assertEquals("Object{name1=null, name2=value}", s1);
    }

    @Test
    public void testToStringHelperAddBoolean() {
        String s = Objects.toStringHelper(new Object()).add("name", true).toString();
        assertEquals("Object{name=true}", s);
    }

    @Test
    public void testToStringHelperAddChar() {
        String s = Objects.toStringHelper(new Object()).add("name", 'c').toString();
        assertEquals("Object{name=c}", s);
    }

    @Test
    public void testToStringHelperAddDouble() {
        String s = Objects.toStringHelper(new Object()).add("name", 3.14).toString();
        assertEquals("Object{name=3.14}", s);
    }

    @Test
    public void testToStringHelperAddFloat() {
        String s = Objects.toStringHelper(new Object()).add("name", 3.14f).toString();
        assertEquals("Object{name=3.14}", s);
    }

    @Test
    public void testToStringHelperAddInt() {
        String s = Objects.toStringHelper(new Object()).add("name", 3).toString();
        assertEquals("Object{name=3}", s);
    }

    @Test
    public void testToStringHelperAddLong() {
        String s = Objects.toStringHelper(new Object()).add("name", 3L).toString();
        assertEquals("Object{name=3}", s);
    }

    @Test
    public void testToStringHelperAddValueAndName() {
        String s = Objects.toStringHelper(new Object()).addValue("value").add("name", "value2").toString();
        assertEquals("Object{name=value2, value=value}", s);
    }
}