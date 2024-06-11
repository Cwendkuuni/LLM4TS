package com.google.common.base;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertFalse(Objects.equal(null, "foo"));
        assertFalse(Objects.equal("foo", null));
        assertFalse(Objects.equal(null, null));
        assertTrue(Objects.equal("foo", "foo"));
        assertTrue(Objects.equal(new Object(), new Object()));
        assertTrue(Objects.equal(new StringBuilder("foo"), new StringBuilder("foo")));
    }

    @Test
    public void testHashCode() {
        assertNotNull(Objects.hashCode(new Object[0]));
        assertArrayEquals(new int[]{1, 2, 3}, Objects.hashCode(new Object[]{new Object(), new Object(), new Object()}));
        assertArrayEquals(new int[]{1, "foo".hashCode(), 3.0f.hashCode()}, Objects.hashCode(new Object[]{new Object(), "foo", 3.0f}));
    }

    @Test
    public void testToStringHelper_omitNullValues_default() {
        assertToString("Foo{name=bar, value=hello}", () -> Objects.toStringHelper("Foo").add("name", "bar").addValue("hello"));
        assertToString("Foo{name=bar}", () -> Objects.toStringHelper("Foo").add("name", "bar"));
        assertToString("Foo{name=bar, value=null}", () -> Objects.toStringHelper("Foo").add("name", "bar").addValue(null));
    }

    @Test
    public void testToStringHelper_omitNullValues_true() {
        assertToString("Foo{name=bar}", () -> Objects.toStringHelper("Foo").omitNullValues().add("name", "bar"));
        assertToString("Foo{}", () -> Objects.toStringHelper("Foo").omitNullValues().addValue(null));
    }

    @Test
    public void testToStringHelper_addAllValues() {
        assertToString("Foo{name1=value1, name2=value2, name3=value3}", () -> Objects.toStringHelper("Foo")
                .add("name1", "value1")
                .add("name2", "value2")
                .add("name3", "value3"));
    }

    @Test
    public void testToStringHelper_addBooleanValue() {
        assertToString("Foo{name=true}", () -> Objects.toStringHelper("Foo").add("name", true));
    }

    @Test
    public void testToStringHelper_addCharValue() {
        assertToString("Foo{name='a'", () -> Objects.toStringHelper("Foo").add("name", 'a'));
    }

    @Test
    public void testToStringHelper_addDoubleValue() {
        assertToString("Foo{name=3.14}", () -> Objects.toStringHelper("Foo").add("name", 3.14));
    }

    @Test
    public void testToStringHelper_addFloatValue() {
        assertToString("Foo{name=1.5}", () -> Objects.toStringHelper("Foo").add("name", 1.5f));
    }

    @Test
    public void testToStringHelper_addIntValue() {
        assertToString("Foo{name=1}", () -> Objects.toStringHelper("Foo").add("name", 1));
    }

    @Test
    public void testToStringHelper_addLongValue() {
        assertToString("Foo{name=100}", () -> Objects.toStringHelper("Foo").add("name", 100L));
    }

    @Test
    public void testToStringHelper_addNullValue() {
        assertToString("Foo{name=null}", () -> Objects.toStringHelper("Foo").add("name", null));
    }

    private String assertToString(String expected, Runnable toStringHelperBuilder) {
        String result = toStringHelperBuilder.run().toString();
        assertEquals(expected, result);
        return result;
    }

    private void assertArrayEquals(int[] expected, int... actual) {
        assertArrayEquals(expected, (Object[]) actual);
    }

    private void assertArrayEquals(Object[] expected, Object... actual) {
        assertArrayEquals(expected, 0, actual.length, actual);
    }

    private void assertArrayEquals(Object[] expected, int index, int length, Object[] actual) {
        Arrays.sort(expected);
        Arrays.sort(actual, 0, length);
        for (int i = index; i < index + length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}