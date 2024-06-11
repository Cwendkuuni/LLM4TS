package com.google.common.base;

import com.google.common.annotations.NonNull;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Arrays;
import com.google.common.primitives.Longs;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ObjectsTest {
    private Objects.ToStringHelper helper;

    @Before
    public void setUp() {
        helper = new Objects.ToStringHelper("TestClass")
                .add("field1", "value1")
                .add("field2", 2)
                .add("field3", null)
                .omitNullValues();
    }

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal(new Object(), new Object()));
        assertTrue(Objects.equal("foo", "foo"));
        assertFalse(Objects.equal(null, "foo"));
        assertFalse(Objects.equal(new Object(), "foo"));
    }

    @Test
    public void testHashCode() {
        List<Object> list = Arrays.asList(1, 2, 3, 4, 5);
        int hashCode = Objects.hashCode(list.toArray());
        int expectedHashCode = Longs.combine(Arrays.hashCode(new int[]{1, 2, 3, 4, 5}), 0x51);
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testToStringHelper() {
        assertEquals("TestClass{field1=value1, field2=2}", helper.toString());
    }

    @Test
    public void testToStringHelperOmittingNullValues() {
        assertEquals("TestClass{field1=value1, field2=2}", helper.toString());
        helper = new Objects.ToStringHelper("TestClass")
                .add("field1", "value1")
                .add("field2", null)
                .omitNullValues();
        assertEquals("TestClass{field1=value1}", helper.toString());
    }

    @Test
    public void testToStringHelperAddingDifferentTypes() {
        helper = new Objects.ToStringHelper("TestClass")
                .add("field1", 1)
                .add("field2", "two")
                .add("field3", new Object())
                .omitNullValues();
        assertEquals("TestClass{field1=1, field2=two}", helper.toString());
    }

    @Test
    public void testToStringHelperAddingEmptyArray() {
        helper = new Objects.ToStringHelper("TestClass")
                .addValue(new int[0]);
        assertEquals("TestClass{}", helper.toString());
    }

    @Test
    public void testToStringHelperAddingNullArray() {
        helper = new Objects.ToStringHelper("TestClass")
                .addValue(null);
        assertEquals("TestClass{}", helper.toString());
    }
}