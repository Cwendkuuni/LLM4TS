package com.google.common.base;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));
        assertTrue(Objects.equal(new Object(), new Object()));
        assertTrue(Objects.equal(new Object(), new Object()));
    }

    @Test
    public void testHashCode() {
        Object[] objects = new Object[10];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = new Object();
        }
        int hashCode = Objects.hashCode(objects);
        Assert.assertNotSame(0, hashCode);
    }

    @Test(expected = NullPointerException.class)
    public void testHashCodeNull() {
        Objects.hashCode(null);
    }

    @Test
    public void testToStringHelperBasicUsage() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("ClassName")
                .add("name1", "value1")
                .add("name2", "value2");
        assertEquals("ClassName{name1=value1, name2=value2}", helper.toString());
    }

    @Test
    public void testToStringHelperOmitNullValues() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("ClassName")
                .add("name1", "value1")
                .add("name2", null)
                .omitNullValues();
        assertEquals("ClassName{name1=value1}", helper.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testToStringHelperNullClassName() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper(null);
    }

    @Test
    public void testToStringHelperAddNullValue() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("ClassName")
                .add("name1", "value1")
                .add("name2", null);
        assertEquals("ClassName{name1=value1, name2=null}", helper.toString());
    }

    @Test
    public void testToStringHelperAddNullClassName() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper(null);
        helper.add("name", "value");
        assertEquals("ClassName{name=value}", helper.toString());
    }
}