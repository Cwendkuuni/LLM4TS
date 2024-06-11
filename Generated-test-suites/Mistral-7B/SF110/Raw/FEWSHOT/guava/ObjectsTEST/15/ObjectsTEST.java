package com.google.common.base;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Objects.hashCode;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Arrays;
import com.google.common.primitives.Ints;

import org.junit.Test;

import java.util.Arrays;

@GwtCompatible
public class ObjectsTest {

    @Test
    public void testEqual() {
        assertFalse(Objects.equal(null, null));
        assertFalse(Objects.equal(null, new Object()));
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal(new Object(), new Object()));
        assertTrue(Objects.equal("foo", "foo"));
        assertFalse(Objects.equal("foo", "bar"));
        assertFalse(Objects.equal(new Object(), new Object() {
            @Override
            public String toString() {
                return "bar";
            }
        }));
        assertFalse(Objects.equal(Arrays.asList(1, 2), Arrays.asList(1, 2, 3)));
        assertFalse(Objects.equal(Arrays.asList(1, 2), Arrays.asList(2, 1)));
        assertTrue(Objects.equal(1, 1));
        assertTrue(Objects.equal(-1, -1));
        assertTrue(Objects.equal(1.0, 1.0));
        assertTrue(Objects.equal(1L, 1L));
        assertTrue(Objects.equal(new byte[]{}, new byte[]{}));
        assertTrue(Objects.equal(new byte[]{1}, new byte[]{1}));
        assertFalse(Objects.equal(new byte[]{1}, new byte[]{2}));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(0, Objects.hashCode(null));
        assertEquals(1, Objects.hashCode("foo"));
        assertEquals(1, Objects.hashCode('a'));
        assertEquals(1, Objects.hashCode(1));
        assertEquals(1, Objects.hashCode(1L));
        assertEquals(-1604588881, Objects.hashCode(1, 2));
        assertEquals(-1385815453, Objects.hashCode(1.0, 2.0));
        assertEquals(248124334, Objects.hashCode(1, 2, 3));
        assertEquals(1357921524, Objects.hashCode("foo", "bar"));
        assertEquals(445626664, Objects.hashCode("foo", 1, "bar"));
        assertEquals(-1182445351, Objects.hashCode(null, null, null));
        assertEquals(151, Objects.hashCode(new Object(), new Object()));
        assertEquals(151, Objects.hashCode(new Object(), new Object() {
            @Override
            public String toString() {
                return "bar";
            }
        }));
        assertEquals(1321244888, Objects.hashCode(Arrays.asList(1, 2), Arrays.asList(1, 2)));
        assertEquals(-2013642156, Objects.hashCode(Arrays.asList(1, 2), Arrays.asList(2, 1)));
        assertEquals(-1272637274, Objects.hashCode(1, 2, 3));
        assertEquals(-1563978166, Objects.hashCode("foo", 1, "bar"));
    }

    @Test
    public void testToStringHelper() {
        final String className = "com.google.common.base.ObjectsTest$TestToStringHelper";
        final Objects.ToStringHelper toStringHelper = Objects.toStringHelper(className);

        toStringHelper.add("foo", 1);
        toStringHelper.add("bar", "hello");
        toStringHelper.add("baz", null);

        assertEquals("com.google.common.base.ObjectsTest$TestToStringHelper{foo=1, bar=hello}", toStringHelper.toString());

        final Objects.ToStringHelper omitNullValuesToStringHelper = toStringHelper.omitNullValues();
        assertEquals("com.google.common.base.ObjectsTest$TestToStringHelper{foo=1, bar=hello}", omitNullValuesToStringHelper.toString());

        final Objects.ToStringHelper addNullValueToStringHelper = new Objects.ToStringHelper(className).omitNullValues();
        addNullValueToStringHelper.add("foo", null);
        assertEquals("com.google.common.base.ObjectsTest$TestToStringHelper{foo=}", addNullValueToStringHelper.toString());
    }

    @Test
    public void testToStringHelperAdd() {
        final String className = "com.google.common.base.ObjectsTest$TestToStringHelper";
        final Objects.ToStringHelper toStringHelper = Objects.toStringHelper(className);

        toStringHelper.add("foo", 1);
        toStringHelper.add("bar", "hello");
        toStringHelper.add("baz", null);
        toStringHelper.add("qux", 42);

        assertEquals("com.google.common.base.ObjectsTest$TestToStringHelper{foo=1, bar=hello, qux=42}", toStringHelper.toString());
    }

    @Test
    public void testToStringHelperAddValue() {
        final String className = "com.google.common.base.ObjectsTest$TestToStringHelper";
        final Objects.ToStringHelper toStringHelper = Objects.toStringHelper(className);

        toStringHelper.add("foo", 1);
        toStringHelper.addValue(2);

        assertEquals("com.google.common.base.ObjectsTest$TestToStringHelper{foo=1, 2}", toStringHelper.toString());
    }

    @Test
    public void testToStringHelperLambda() {
        final String className = "com.google.common.base.ObjectsTest$TestToStringHelperLambda";
        final Objects.ToStringHelper toStringHelper = Objects.toStringHelper(className);

        toStringHelper.add("foo", () -> "42");
        toStringHelper.add("bar", 1);

        assertEquals("com.google.common.base.ObjectsTest$TestToStringHelper{foo=42, bar=1}", toStringHelper.toString());
    }

    @Test
    public void testToStringHelperArray() {
        final String className = "com.google.common.base.ObjectsTest$TestToStringHelperArray";
        final Objects.ToStringHelper toStringHelper = Objects.toStringHelper(className);

        final int[] ints = {1, 2, 3};
        toStringHelper.add("ints", ints);

        assertEquals("com.google.common.base.ObjectsTest$TestToStringHelper{ints=[1, 2, 3]}", toStringHelper.toString());
    }

    @Test
    public void testToStringHelperList() {
        final String className = "com.google.common.base.ObjectsTest$TestToStringHelperList";
        final Objects.ToStringHelper toStringHelper = Objects.toStringHelper(className);

        final List<Integer> ints = Arrays.asList(1, 2, 3);
        toStringHelper.add("ints", ints);

        assertEquals("com.google.common.base.ObjectsTest$TestToStringHelper{ints=[1, 2, 3]}", toStringHelper.toString());
    }

    @Test
    public void testToStringHelperMap() {
        final String className = "com.google.common.base.ObjectsTest$TestToStringHelperMap";
        final Objects.ToStringHelper toStringHelper = Objects.toStringHelper(className);

        final Map<String, Integer> map = new HashMap<>();
        map.put("key1", 1);
        map.put("key2", 2);
        map.put("key3", null);

        toStringHelper.add("map", map);

        assertEquals("com.google.common.base.ObjectsTest$TestToStringHelper{map={key1=1, key2=2}}", toStringHelper.toString());
    }

    private static class TestToStringHelper {

    }

    private static class TestToStringHelperLambda {

    }
}