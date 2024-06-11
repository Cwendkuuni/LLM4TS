package com.google.common.base;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Objects.hashCode;
import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertEquals(true, Objects.equal(null, null));
        assertEquals(true, Objects.equal(1, 1));
        assertEquals(true, Objects.equal("foo", "foo"));
        assertEquals(true, Objects.equal(new Object(), new Object()));
        assertEquals(false, Objects.equal(1, "1"));
        assertEquals(false, Objects.equal(null, ""));
        assertEquals(false, Objects.equal("foo", "bar"));
    }

    @Test
    public void testHashCode() {
        assertArrayEquals(new int[]{0}, Objects.hashCode(new Object[0]));
        assertArrayEquals(new int[]{1}, Objects.hashCode(new Object[]{null}));
        assertArrayEquals(new int[]{1, 2}, Objects.hashCode(new Object[]{new Object(), new Object()}));
        assertArrayEquals(new int[]{1, 2, 3}, Objects.hashCode(new Object[]{new Object(), new Object(), new Object()}));
        assertArrayEquals(new int[]{1, "foo".hashCode()}, Objects.hashCode(new Object[]{new Object(), "foo"}));
        assertArrayEquals(new int[]{1, 2, "foo".hashCode()}, Objects.hashCode(new Object[]{new Object(), new Object(), "foo"}));
    }

    @Test(expected = NullPointerException.class)
    public void testHashCodeNull() {
        Objects.hashCode((Object) null);
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("TestClass") {
            @Override
            public String toString() {
                return super.toString() + " with additional text";
            }
        };

        String result = helper.omitNullValues().add("name1", 1).add("name2", "value2").toString();
        assertEquals("TestClass{name1=1, name2=value2}", result);

        result = helper.omitNullValues().add("name1", null).add("name2", "value2").toString();
        assertEquals("TestClass{name2=value2}", result);
    }

    @Test
    public void testAddValues() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("TestClass");
        String result = helper.addValue(1).addValue("value2").toString();
        assertEquals("TestClass{1=1, value2=value2}", result);
    }

    @Test
    public void testAddValuesWithNullValues() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("TestClass").omitNullValues();
        helper.add("name1", null).add("name2", null).add("name3", "value3");

        String result = helper.toString();
        assertEquals("TestClass{}", result);
    }

    @Test
    public void testAddWithNullObject() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("TestClass");
        helper.add(null);

        String result = helper.toString();
        assertEquals("TestClass{}", result);
    }

    @Test
    public void testAddWithNullName() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("TestClass");
        helper.add(null, "value");

        String result = helper.toString();
        assertEquals("TestClass{}", result);
    }

    @Test
    public void testAddWithNullValue() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("TestClass");
        helper.add("name", null);

        String result = helper.toString();
        assertEquals("TestClass{name=null}", result);
    }
}