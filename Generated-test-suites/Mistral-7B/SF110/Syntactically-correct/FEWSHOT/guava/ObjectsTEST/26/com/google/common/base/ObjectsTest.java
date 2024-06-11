package com.google.common.base;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertEquals(true, Objects.equal(null, null));
        assertEquals(true, Objects.equal(Integer.valueOf(1), Integer.valueOf(1)));
        assertEquals(true, Objects.equal(new Object(), new Object()));
        assertEquals(false, Objects.equal(null, new Object()));
        assertEquals(false, Objects.equal(Integer.valueOf(1), "1"));
    }

    @Test
    public void testHashCode() {
        Object[] objects = new Object[10];
        Arrays.fill(objects, new Object());
        int hashCode = Objects.hashCode(objects);
        assertNotSame(hashCode, Objects.hashCode(new Object[10]));
    }

    @Test
    public void testToStringHelper_String() {
        Objects.ToStringHelper helper = Objects.toStringHelper("StringClass");
        helper.add("name", "value");
        assertEquals("StringClass{name=value}", helper.toString());
    }

    @Test
    public void testToStringHelper_Class() {
        Objects.ToStringHelper helper = Objects.toStringHelper(String.class);
        helper.add("name", "value");
        assertEquals("String{name=value}", helper.toString());
    }

    @Test
    public void testToStringHelper_omitNullValues() {
        Objects.ToStringHelper helper = Objects.toStringHelper("StringClass").omitNullValues();
        helper.add("name1", null);
        helper.add("name2", "value2");
        helper.add("name3", null);
        assertEquals("StringClass{name2=value2}", helper.toString());
    }

    @Test
    public void testToStringHelper_add() {
        Objects.ToStringHelper helper = Objects.toStringHelper("StringClass");
        helper.add("name1", "value1");
        helper.add("name2", "value2");
        assertEquals("StringClass{name1=value1, name2=value2}", helper.toString());
    }

    @Test
    public void testToStringHelper_addValue() {
        Objects.ToStringHelper helper = Objects.toStringHelper("StringClass");
        helper.addValue("value1");
        assertEquals("StringClass{value1=value1}", helper.toString());
    }

    @Test
    public void testToStringHelper_addValue_double() {
        Objects.ToStringHelper helper = Objects.toStringHelper("StringClass");
        helper.addValue(1.2);
        assertEquals("StringClass{value1=1.2}", helper.toString());
    }

    @Test
    public void testToStringHelper_addValue_int() {
        Objects.ToStringHelper helper = Objects.toStringHelper("StringClass");
        helper.addValue(1);
        assertEquals("StringClass{value1=1}", helper.toString());
    }

    @Test
    public void testToStringHelper_addValue_boolean() {
        Objects.ToStringHelper helper = Objects.toStringHelper("StringClass");
        helper.addValue(true);
        assertEquals("StringClass{value1=true}", helper.toString());
    }

    @Test
    public void testToStringHelper_addValue_char() {
        Objects.ToStringHelper helper = Objects.toStringHelper("StringClass");
        helper.addValue('c');
        assertEquals("StringClass{value1=c}", helper.toString());
    }

    @Test
    public void testToStringHelper_addValue_long() {
        Objects.ToStringHelper helper = Objects.toStringHelper("StringClass");
        helper.addValue(1L);
        assertEquals("StringClass{value1=1}", helper.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testToStringHelper_addNullName() {
        Objects.ToStringHelper helper = Objects.toStringHelper("StringClass");
        helper.add(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testToStringHelper_addNullValue() {
        Objects.ToStringHelper helper = Objects.toStringHelper("StringClass");
        helper.add("name", null);
    }
}