package com.google.common.base.test;

import static org.junit.Assert.*;

import com.google.common.base.Objects;
import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        // Basic functionality test
        assertTrue(Objects.equal(null, null));
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertTrue(Objects.equal(obj1, obj2));
        assertFalse(Objects.equal(obj1, new Object()));

        // Null object test
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));

        // Object with equals method implementation test
        class TestObj {
            @Override
            public boolean equals(Object o) {
                return o != null && getClass() == o.getClass();
            }
        }

        TestObj obj3 = new TestObj();
        assertFalse(Objects.equal(obj1, obj3));
        assertFalse(Objects.equal(obj3, obj1));
        TestObj obj4 = new TestObj();
        assertTrue(Objects.equal(obj3, obj4));
        assertTrue(Objects.equal(obj4, obj3));
    }

    @Test
    public void testHashCode() {
        // Basic functionality test
        Object[] array = new Object[5];
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertEquals(Arrays.hashCode(new Object[0]), Objects.hashCode());
        assertEquals(Arrays.hashCode(new Object[]{obj1}), Objects.hashCode(obj1));
        assertEquals(Arrays.hashCode(new Object[]{obj1, obj2}), Objects.hashCode(obj1, obj2));

        // Test with null objects
        Object[] nullArray = new Object[5];
        nullArray[0] = null;
        assertEquals(Arrays.hashCode(new Object[]{null}), Objects.hashCode(null));
        assertEquals(Arrays.hashCode(new Object[]{obj1, null}), Objects.hashCode(obj1, null));
        assertEquals(Arrays.hashCode(new Object[]{null, obj1}), Objects.hashCode(null, obj1));
        assertEquals(Arrays.hashCode(new Object[]{null, null}), Objects.hashCode(null, null));
    }

    @Test(expected = DeprecationException.class)
    public void testDeprecatedToStringHelper() {
        Objects.toStringHelper(new Object());
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.newToStringHelper(Objects.class).omitNullValues();
        helper.add("name1", "value1");
        helper.add("name2", null);
        helper.add("name3", new Object());
        String result = helper.toString();
        String expected = "Objects{name1=value1, name3=Object@xxxxxx}";
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testToStringHelperNullName() {
        Objects.ToStringHelper helper = Objects.newToStringHelper(Objects.class);
        helper.add(null, "value1");
    }

    @Test(expected = NullPointerException.class)
    public void testToStringHelperNullValue() {
        Objects.ToStringHelper helper = Objects.newToStringHelper(Objects.class).omitNullValues();
        helper.add("name1", null);
    }
}