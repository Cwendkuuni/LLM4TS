package com.google.common.base;

import static org.junit.Assert.*;

import com.google.common.base.Objects;
import org.junit.Test;

import java.util.Arrays;

public class ObjectsTest {
    @Test
    public void testEqual() {
        assertEquals(true, Objects.equal(null, null));
        assertEquals(true, Objects.equal(null, new Object()));
        assertEquals(true, Objects.equal(new Object(), new Object()));
        assertEquals(true, Objects.equal(new Object(), new Object()));
        assertEquals(false, Objects.equal(null, new Object()));
        assertEquals(false, Objects.equal(new Object(), null));
        assertEquals(true, Objects.equal("foo", "foo"));
        assertEquals(false, Objects.equal("foo", "bar"));
    }

    @Test
    public void testHashCode() {
        Object[] array = new Object[10];
        for (int i = 0; i < array.length; i++) {
            array[i] = Integer.valueOf(i);
        }
        assertEquals(Arrays.hashCode(array), Objects.hashCode(array));
        assertEquals(Arrays.hashCode(new Object[0]), Objects.hashCode((Object[]) null));
    }

    @Test
    public void testToStringHelper_omitNullValues_defaultFalse() {
        Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
        helper.add("name1", "value1");
        helper.add("name2", null);
        helper.add("name3", "value3");
        assertEquals("Object{name1=value1, name3=value3}", helper.toString());
    }

    @Test
    public void testToStringHelper_omitNullValues_true() {
        Objects.ToStringHelper helper = Objects.toStringHelper(new Object()).omitNullValues();
        helper.add("name1", "value1");
        helper.add("name2", null);
        helper.add("name3", "value3");
        assertEquals("Object{name1=value1, name3=value3}", helper.toString());
    }

    @Test
    public void testToStringHelper_addValues() {
        Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
        helper.add("name1", "value1");
        helper.addValue("value2");
        helper.addValue(123);
        helper.addValue(123L);
        helper.addValue(123.0);
        helper.addValue(123.0f);
        helper.addValue(true);
        helper.addValue(!true);
        helper.addValue('a');
        helper.addValue('\u0000');
        assertEquals("Object{name1=value1, value2, 123, 123, 123.0, 123.0, true, false, a, \u0000}", helper.toString());
    }

    @Test
    public void testToStringHelper_addValuesWithName() {
        Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
        helper.add("name1", "value1");
        helper.add("name2", "value2");
        helper.add("name3", "value3");
        helper.add("name4", null);
        helper.addValue(123);
        helper.addValue(123L);
        helper.addValue(123.0);
        helper.addValue(123.0f);
        helper.addValue(true);
        helper.addValue(!true);
        helper.addValue('a');
        helper.addValue('\u0000');
        assertEquals("Object{name1=value1, name2=value2, name3=value3, value123=123, value123L=123, value123D=123.0, value123DF=123.0, value123B=true, value123BN=false, value123C=a, value123CC=\u0000}", helper.toString());
    }
}