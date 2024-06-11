package com.google.common.base;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal(new Object(), new Object()));
        assertTrue(Objects.equal(new Integer(5), new Integer(5)));
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), new Integer(5)));
    }

    @Test
    public void testHashCode() {
        Object[] objects = new Object[10];
        Arrays.fill(objects, new Object());
        int hashCode = Objects.hashCode(objects);
        for (int i = 0; i < objects.length; i++) {
            Object obj = objects[i];
            Objects.hashCode(new Object[] { obj });
            assertEquals(hashCode, Objects.hashCode(new Object[] { obj }));
        }
    }

    @Test
    public void testToStringHelper_omitNullValues_false() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName")
                .add("name1", "value1")
                .add("name2", null)
                .add("name3", "value3");
        assertEquals("ClassName{name1=value1, name2=, name3=value3}", helper.toString());
    }

    @Test
    public void testToStringHelper_omitNullValues_true() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName")
                .add("name1", "value1")
                .add("name2", null)
                .omitNullValues()
                .add("name3", "value3");
        assertEquals("ClassName{name1=value1, name3=value3}", helper.toString());
    }

    @Test
    public void testToStringHelper_addVariousTypes() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName")
                .add("name1", 1)
                .add("name2", "two")
                .add("name3", 3.14)
                .add("name4", true)
                .add("name5", false)
                .add("name6", null);
        assertEquals("ClassName{name1=1, name2=two, name3=3.14, name4=true, name5=false, name6=}", helper.toString());
    }

    @Test
    public void testToStringHelper_addValue() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName")
                .addValue("value1")
                .addValue(1)
                .addValue(3.14)
                .addValue(true)
                .addValue(false);
        assertEquals("ClassName{value1=value1, 1=1, 3.14=3.14, true=true, false=false}", helper.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testToStringHelper_nullName() {
        Objects.ToStringHelper helper = Objects.newToStringHelper(null).add("name", "value1");
    }

    @Test(expected = NullPointerException.class)
    public void testToStringHelper_nullValue() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName")
                .add("name1", "value1")
                .add("name2", null);
    }
}