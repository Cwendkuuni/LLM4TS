package com.google.common.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ObjectsTest {

    @Test
    public void testEqualNullNull() {
        assertTrue(Objects.equal(null, null));
    }

    @Test
    public void testEqualNullObject() {
        assertFalse(Objects.equal(null, new Object()));
    }

    @Test
    public void testEqualObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertTrue(Objects.equal(obj1, obj2));
    }

    @Test(expected = NullPointerException.class)
    public void testEqualNullObjectNull() {
        Object obj1 = new Object();
        Object obj2 = null;
        Objects.equal(obj1, obj2);
    }

    @Test
    public void testHashCodeNullObjects() {
        int hashCode = Objects.hashCode(null);
        assertTrue(hashCode == 0);
    }

    @Test
    public void testHashCodeObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        int hashCode = Objects.hashCode(obj1, obj2);
        int hashCodeOfObjects = Arrays.hashCode(new Object[] { obj1, obj2 });
        assertEquals(hashCode, hashCodeOfObjects);
    }

    @Test(expected = NullPointerException.class)
    public void testHashCodeNullObject() {
        Object obj1 = new Object();
        Object[] objects = { obj1, null };
        Objects.hashCode(objects);
    }

    @Test
    public void testToStringHelperDefaultConstruction() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("className");
        assertNotNull(helper);
    }

    @Test
    public void testToStringHelperOmitNullValues() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("className").omitNullValues();
        assertNotNull(helper);
    }

    @Test
    public void testToStringHelperAddStringValue() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("className");
        helper.add("name", "value");
        String toString = helper.toString();
        assertTrue(toString.contains("name=value"));
    }

    @Test
    public void testToStringHelperAddNullValue() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("className");
        helper.add("name", null);
        String toString = helper.toString();
        assertTrue(toString.contains("name"));
        assertTrue(toString.contains("null"));
    }

    @Test
    public void testToStringHelperAddBooleanValue() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("className");
        helper.add("name", true);
        String toString = helper.toString();
        assertTrue(toString.contains("name=true"));
    }

    @Test
    public void testToStringHelperAddCharValue() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("className");
        helper.add("name", 'a');
        String toString = helper.toString();
        assertTrue(toString.contains("name='a'"));
    }

    @Test
    public void testToStringHelperAddDoubleValue() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("className");
        helper.add("name", 3.14);
        String toString = helper.toString();
        assertTrue(toString.contains("name=3.14"));
    }

    @Test
    public void testToStringHelperAddFloatValue() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("className");
        helper.add("name", 3.14f);
        String toString = helper.toString();
        assertTrue(toString.contains("name=3.14"));
    }

    @Test
    public void testToStringHelperAddIntValue() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("className");
        helper.add("name", 123);
        String toString = helper.toString();
        assertTrue(toString.contains("name=123"));
    }

    @Test
    public void testToStringHelperAddLongValue() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("className");
        helper.add("name", 123L);
        String toString = helper.toString();
        assertTrue(toString.contains("name=123"));
    }

    @Test
    public void testToStringHelperAddNullObject() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("className");
        helper.add("name", null);
        String toString = helper.toString();
        assertTrue(toString.contains("name"));
        assertTrue(toString.contains("null"));
    }

    @Test
    public void testToStringHelperAddObjects() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("className");
        helper.add("name1", new Object());
        helper.add("name2", new Object());
        String toString = helper.toString();
        assertTrue(toString.contains("name1"));
        assertTrue(toString.contains("name2"));
    }

    @Test
    public void testToStringHelperToString() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("className");
        helper.add("name1", new Object());
        helper.add("name2", new Object());
        String toString = helper.toString();
        assertTrue(toString.contains("className{name1=, name2="));
    }
}