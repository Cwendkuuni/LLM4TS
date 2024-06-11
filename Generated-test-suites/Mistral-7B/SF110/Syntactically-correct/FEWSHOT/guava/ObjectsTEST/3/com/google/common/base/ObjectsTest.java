package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertFalse(Objects.equal(null, null));
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), new Object()));
        assertTrue(Objects.equal(new Object(), new Object()));
        assertTrue(Objects.equal("foo", "foo"));
        assertTrue(Objects.equal(1, 1));
        assertFalse(Objects.equal("foo", "bar"));
        assertFalse(Objects.equal(1, 2));
    }

    @Test
    public void testHashCode() {
        Object[] objects = new Object[10];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = new Object();
        }
        int hashCode = Objects.hashCode(objects);
        assertNotSame(-1, hashCode);
        Objects array = new Object[0];
        int hashCodeZero = Objects.hashCode(array);
        assertEquals(hashCodeZero, Objects.hashCode());
    }

    @Test(expected = NullPointerException.class)
    public void testToStringHelperNullSelf() {
        Objects.toStringHelper(null);
    }

    @Test
    public void testToStringHelperStringClassName() {
        String result = Objects.toStringHelper("ClassName").add("field1", "value1").add("field2", "value2").toString();
        assertEquals("ClassName{field1=value1, field2=value2}", result);
    }

    @Test
    public void testToStringHelperClassClassName() {
        String result = Objects.toStringHelper(ClassName.class).add("field1", "value1").add("field2", "value2").toString();
        assertEquals("ClassName{field1=value1, field2=value2}", result);
    }

    @Test
    public void testToStringHelperStringClassNameOmitNullValues() {
        String result = Objects.toStringHelper("ClassName").omitNullValues().add("field1", "value1").add("field2", null).toString();
        assertEquals("ClassName{}", result);
    }

    @Test
    public void testToStringHelperClassClassNameOmitNullValues() {
        String result = Objects.toStringHelper(ClassName.class).omitNullValues().add("field1", "value1").add("field2", null).toString();
        assertEquals("ClassName{}", result);
    }

    @Test
    public void testToStringHelperAdd() {
        String result = Objects.toStringHelper("ClassName").add("field1", "value1").add("field2", "value2").toString();
        assertEquals("ClassName{field1=value1, field2=value2}", result);
    }

    @Test
    public void testToStringHelperAddBoolean() {
        String result = Objects.toStringHelper("ClassName").add("field1", true).toString();
        assertEquals("ClassName{field1=true}", result);
    }

    @Test
    public void testToStringHelperAddChar() {
        String result = Objects.toStringHelper("ClassName").add("field1", 'a').toString();
        assertEquals("ClassName{field1=a}", result);
    }

    @Test
    public void testToStringHelperAddDouble() {
        String result = Objects.toStringHelper("ClassName").add("field1", 1.23).toString();
        assertEquals("ClassName{field1=1.23}", result);
    }

    @Test
    public void testToStringHelperAddFloat() {
        String result = Objects.toStringHelper("ClassName").add("field1", 1.23f).toString();
        assertEquals("ClassName{field1=1.23}", result);
    }

    @Test
    public void testToStringHelperAddInt() {
        String result = Objects.toStringHelper("ClassName").add("field1", 1).toString();
        assertEquals("ClassName{field1=1}", result);
    }

    @Test
    public void testToStringHelperAddLong() {
        String result = Objects.toStringHelper("ClassName").add("field1", 1L).toString();
        assertEquals("ClassName{field1=1}", result);
    }

    @Test
    public void testToStringHelperAddValue() {
        String result = Objects.toStringHelper("ClassName").addValue("value1").toString();
        assertEquals("ClassName{field1=value1}", result);
    }

    @Test
    public void testToStringHelperAddValueBoolean() {
        String result = Objects.toStringHelper("ClassName").addValue(true).toString();
        assertEquals("ClassName{field1=true}", result);
    }

    @Test
    public void testToStringHelperAddValueChar() {
        String result = Objects.toStringHelper("ClassName").addValue('a').toString();
        assertEquals("ClassName{field1=a}", result);
    }

    @Test
    public void testToStringHelperAddValueDouble() {
        String result = Objects.toStringHelper("ClassName").addValue(1.23).toString();
        assertEquals("ClassName{field1=1.23}", result);
    }

    @Test
    public void testToStringHelperAddValueFloat() {
        String result = Objects.toStringHelper("ClassName").addValue(1.23f).toString();
        assertEquals("ClassName{field1=1.23}", result);
    }

    @Test
    public void testToStringHelperAddValueInt() {
        String result = Objects.toStringHelper("ClassName").addValue(1).toString();
        assertEquals("ClassName{field1=1}", result);
    }

    @Test
    public void testToStringHelperAddValueLong() {
        String result = Objects.toStringHelper("ClassName").addValue(1L).toString();
        assertEquals("ClassName{field1=1}", result);
    }
}