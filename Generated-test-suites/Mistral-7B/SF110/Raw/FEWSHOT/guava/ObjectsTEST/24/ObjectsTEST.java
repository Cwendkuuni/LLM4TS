package com.google.common.base;

import static com.google.common.base.Objects.toString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.ToStringHelper;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

@GwtCompatible
public class ObjectsTest {

    @Test
    public void testEqual() {
        assertSame("null equals null", Objects.equal(null, null));
        assertSame("null equals non-null", Objects.equal(null, ""));
        assertSame("non-null equals null", Objects.equal("", null));
        assertSame("non-null equals non-null and same object", Objects.equal(new Object(), new Object()));
        assertSame("non-null equals non-null and different objects", Objects.equal(new Object(), new Object()));
        assertFalse("non-null not equal null", Objects.equal(new Object(), null));
        assertFalse("non-null not equal non-null and different types", Objects.equal(new Object(), ""));
        assertFalse("non-null not equal non-null and different values", Objects.equal(new Object(), new Object()));
    }

    @Test
    public void testHashCodeWithNullObjects() {
        assertEquals("hashCode of empty array", Objects.hashCode(), Objects.hashCode(new Object[0]));
        assertEquals("hashCode of null object", Objects.hashCode(null), Objects.hashCode(new Object(){@Override public boolean equals(Object obj) { return false; }}));
        assertEquals("hashCode of array with null object", Objects.hashCode(new Object[]{null}), Objects.hashCode(new Object[]{new Object()}));
        assertEquals("hashCode of array with multiple null objects", Objects.hashCode(new Object[]{null, null}), Objects.hashCode(new Object[]{new Object(), null}));
        assertEquals("hashCode of array with multiple null and non-null objects", Objects.hashCode(new Object[]{null, "foo"}), Objects.hashCode(new Object[]{"foo", null}));
    }

    @Test
    public void testHashCodeWithPrimitiveTypes() {
        Integer[] integers = new Integer[]{1, 2, 3};
        long[] longs = new long[]{1L, 2L, 3L};
        byte[] bytes = new byte[]{1, 2, 3};
        short[] shorts = new short[]{1, 2, 3};
        char[] chars = new char[]{'a', 'b', 'c'};
        float[] floats = new float[]{1.0f, 2.0f, 3.0f};
        double[] doubles = new double[]{1.0, 2.0, 3.0};
        assertArrayEquals("hashCode of array of integers", Objects.hashCode(integers), Objects.hashCode(Arrays.copyOf(integers, integers.length)));
        assertArrayEquals("hashCode of array of longs", Objects.hashCode(longs), Objects.hashCode(Arrays.copyOf(longs, longs.length)));
        assertArrayEquals("hashCode of array of bytes", Objects.hashCode(bytes), Objects.hashCode(Arrays.copyOf(bytes, bytes.length)));
        assertArrayEquals("hashCode of array of shorts", Objects.hashCode(shorts), Objects.hashCode(Arrays.copyOf(shorts, shorts.length)));
        assertArrayEquals("hashCode of array of chars", Objects.hashCode(chars), Objects.hashCode(chars.clone()));
        assertArrayEquals("hashCode of array of floats", Objects.hashCode(floats), Objects.hashCode(Arrays.copyOf(floats, floats.length)));
        assertArrayEquals("hashCode of array of doubles", Objects.hashCode(doubles), Objects.hashCode(Arrays.copyOf(doubles, doubles.length)));
    }

    @Test
    public void testToStringHelper() {
        String name = "name";
        Object value = "value";
        ToStringHelper helper = Objects.toStringHelper("ClassName")
                .omitNullValues()
                .add("name", value);
        assertEquals("ClassName{name=value}", helper.toString());
        Objects.ToStringHelper nullValueHelper = Objects.toStringHelper("ClassName")
                .omitNullValues()
                .add("name", null);
        assertEquals("ClassName{}", nullValueHelper.toString());
    }

    @Test
    public void testToStringHelperWithMultipleValues() {
        String name1 = "name1";
        Object value1 = "value1";
        String name2 = "name2";
        Object value2 = "value2";
        ToStringHelper helper = Objects.toStringHelper("ClassName")
                .omitNullValues()
                .add(name1, value1)
                .add(name2, value2);
        if (value1 != null && value2 != null) {
            assertEquals("ClassName{name1=" + value1 + ", name2=" + value2 + '}', helper.toString());
        } else {
            assertEquals("ClassName{}", helper.toString());
        }
    }

    @Test
    public void testToStringHelperWithNullValues() {
        String name1 = "name1";
        Object value1 = null;
        String name2 = "name2";
        Object value2 = "value2";
        ToStringHelper helper = Objects.toStringHelper("ClassName")
                .omitNullValues()
                .add(name1, value1)
                .add(name2, value2);
        if (value1 == null) {
            if (value2 != null) {
                assertEquals("ClassName{name2=value2}", helper.toString());
            } else {
                assertEquals("ClassName{}", helper.toString());
            }
        } else {
            assertSame("toStringHelper should not add null values", helper.toString(), helper.toString());
        }
    }

    @Test
    public void testToStringHelperWithMultipleTypes() {
        String name1 = "name1";
        Object value1 = "value1";
        String name2 = "name2";
        Number value2 = 2;
        ToStringHelper helper = Objects.toStringHelper("ClassName")
                .add(name1, value1)
                .add(name2, value2);
        assertEquals("ClassName{name1=value1, name2=2}", helper.toString());
    }

    @Test
    public void testToStringHelperWithEmptyString() {
        ToStringHelper helper = Objects.toStringHelper("ClassName")
                .add("name", "");
        assertEquals("ClassName{name=}", helper.toString());
    }

    @Test
    public void testToStringHelperWithEmptyToString() {
        Object value = new Object(){@Override public String toString() { return ""; }};
        ToStringHelper helper = Objects.toStringHelper("ClassName")
                .add("name", value);
        assertEquals("ClassName{name=}", helper.toString());
    }

    @Test
    public void testToStringHelperWithCustomToString() {
        Object obj = new Object(){
            private static final long serialVersionUID = 1L;
            @Override
            public String toString() {
                return "CustomValue";
            }
        };
        ToStringHelper helper = Objects.toStringHelper("ClassName")
                .add("name", obj);
        assertEquals("ClassName{name=CustomValue}", helper.toString());
    }

    @Test
    public void testToStringHelperWithoutOmitNullValues() {
        String name = "name";
        Object value = null;
        ToStringHelper helper = Objects.toStringHelper("ClassName")
                .add(name, value);
        assertEquals("ClassName{name=null}", helper.toString());
    }

    @Test
    public void testToStringHelperAddMultipleValuesWithSameName() {
        String name = "name";
        Object value1 = "value1";
        Object value2 = "value2";
        ToStringHelper helper = Objects.toStringHelper("ClassName")
                .add(name, value1)
                .add(name, value2);
        if (value1 != null && value2 != null) {
            assertEquals("ClassName{name=" + value1 + ", name=" + value2 + '}', helper.toString());
        } else {
            assertEquals("ClassName{" + (value1 != null ? name + "=" + value1 : "") + (value2 != null ? ", " + name + "=" + value2 : "") + '}', helper.toString());
        }
    }

    @Test
    public void testToStringHelperAddValueWithSameName() {
        String name = "name";
        Object value1 = "value1";
        Object value2 = "value2";
        ToStringHelper helper = Objects.toStringHelper("ClassName")
                .add(name, value1)
                .addValue(value2);
        if (value1 != null) {
            assertEquals("ClassName{name=" + value1 + ", name=" + value2 + '}', helper.toString());
        } else {
            assertEquals("ClassName{name=" + value2 + '}', helper.toString());
        }
    }

    @Test
    public void testToStringHelperAddValueWithSameNameAndNullValue() {
        String name = "name";
        Object value1 = "value1";
        Object value2 = null;
        ToStringHelper helper = Objects.toStringHelper("ClassName")
                .add(name, value1)
                .addValue(value2);
        if (value1 != null) {
            assertEquals("ClassName{name=" + value1 + '}', helper.toString());
        } else {
            assertEquals("ClassName{name=}", helper.toString());
        }
    }

    @Test
    public void testToStringHelperWithEmptyArray() {
        String name = "name";
        ToStringHelper helper = Objects.toStringHelper("ClassName")
                .add(name, (Object[])new Object[0]);
        assertEquals("ClassName{name=[]}", helper.toString());
    }

    @Test
    public void testToStringHelperWithEmptyList() {
        String name = "name";
        List<Object> list = Collections.emptyList();
        ToStringHelper helper = Objects.toStringHelper("ClassName")
                .add(name, list);
        assertEquals("ClassName{name=[]}", helper.toString());
    }

    @Test
    public void testToStringHelperWithEmptyMap() {
        String name = "name";
        Map<String, Object> map = Collections.emptyMap();
        ToStringHelper helper = Objects.toStringHelper("ClassName")
                .add(name, map);
        assertEquals("ClassName{name={}}", helper.toString());
    }

    @Test
    public void testToStringHelperWithNullMap() {
        String name = "name";
        Map<String, Object> map = null;
        ToStringHelper helper = Objects.toStringHelper("ClassName")
                .add(name, map);
        assertEquals("ClassName{name=}", helper.toString());
    }
}