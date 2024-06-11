package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class JoinerTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testJoiner_basic() {
        Joiner joiner = Joiner.on(":");
        StringWriter sw = new StringWriter();
        try {
            joiner.appendTo(sw, Arrays.asList("foo", "bar", "baz"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("foo:bar:baz", sw.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testJoiner_nullSafe() {
        Joiner joiner = Joiner.on(":").useForNull("N/A");
        StringWriter sw = new StringWriter();
        try {
            joiner.appendTo(sw, Arrays.asList("foo", null, "bar", "baz"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("foo:N/A:bar:baz", sw.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testJoiner_appendTo() {
        Joiner joiner = Joiner.on(":");
        StringWriter sw = new StringWriter();
        joiner.appendTo(sw, "foo", "bar", "baz");
        assertEquals("foo:bar:baz", sw.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testJoiner_appendTo_iterator() {
        Joiner joiner = Joiner.on(":");
        StringWriter sw = new StringWriter();
        Iterator<String> it = Arrays.asList("foo", "bar", "baz").iterator();
        joiner.appendTo(sw, it);
        assertEquals("foo:bar:baz", sw.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testJoiner_appendTo_array() {
        Joiner joiner = Joiner.on(":");
        StringWriter sw = new StringWriter();
        joiner.appendTo(sw, new Object[]{"foo", "bar", "baz"});
        assertEquals("foo:bar:baz", sw.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testJoiner_appendTo_map() {
        Joiner joiner = Joiner.on("=");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        StringWriter sw = new StringWriter();
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        try {
            mapJoiner.appendTo(sw, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("key1=value1=key2=value2", sw.toString());
    }

    @Test
    public void testJoin_emptyIterable() {
        Joiner joiner = Joiner.on(":");
        assertEquals("", joiner.join(Collections.emptyList()));
    }

    @Test
    public void testJoin_emptyArray() {
        Joiner joiner = Joiner.on(":");
        assertEquals("", joiner.join(new Object[0]));
    }

    @Test
    public void testJoin_null() {
        Joiner joiner = Joiner.on(":");
        assertNull(joiner.join(null));
    }

    @Test
    public void testJoin_singleElement() {
        Joiner joiner = Joiner.on(":");
        assertEquals("foo", joiner.join(Arrays.asList("foo")));
    }

    @Test
    public void testJoin_twoElements() {
        Joiner joiner = Joiner.on(":");
        assertEquals("foo:bar", joiner.join(Arrays.asList("foo", "bar")));
    }

    @Test
    public void testJoin_threeElements() {
        Joiner joiner = Joiner.on(":");
        assertEquals("foo:bar:baz", joiner.join(Arrays.asList("foo", "bar", "baz")));
    }

    @Test
    public void testJoin_fourElements() {
        Joiner joiner = Joiner.on(":");
        assertEquals("foo:bar:baz:qux", joiner.join(Arrays.asList("foo", "bar", "baz", "qux")));
    }

    @Test
    public void testJoin_fiveElements() {
        Joiner joiner = Joiner.on(":");
        assertEquals("foo:bar:baz:qux:corge", joiner.join(Arrays.asList("foo", "bar", "baz", "qux", "corge")));
    }

    @Test
    public void testJoin_map() {
        Joiner joiner = Joiner.on("=");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        assertEquals("key1=value1=key2=value2", joiner.join(map.entrySet()));
    }

    @Test
    public void testJoin_map_nullKey() {
        Joiner joiner = Joiner.on("=");
        Map<String, String> map = new HashMap<>();
        map.put(null, "value");
        assertNull(joiner.join(map.entrySet()));
    }

    @Test
    public void testJoin_map_nullValue() {
        Joiner joiner = Joiner.on("=");
        Map<String, String> map = new HashMap<>();
        map.put("key", null);
        assertNull(joiner.join(map.entrySet()));
    }

    @Test
    public void testJoin_map_empty() {
        Joiner joiner = Joiner.on("=");
        Map<String, String> map = new HashMap<>();
        assertEquals("", joiner.join(map.entrySet()));
    }

    @Test
    public void testJoin_set() {
        Joiner joiner = Joiner.on(",");
        Set<String> set = new HashSet<>(Arrays.asList("foo", "bar", "baz"));
        assertEquals("foo,bar,baz", joiner.join(set));
    }

    @Test
    public void testJoin_list() {
        Joiner joiner = Joiner.on(";");
        List<String> list = Arrays.asList("foo", "bar", "baz");
        assertEquals("foo;bar;baz", joiner.join(list));
    }

    @Test
    public void testJoin_iterator() {
        Joiner joiner = Joiner.on(",");
        Iterator<String> it = Arrays.asList("foo", "bar", "baz").iterator();
        assertEquals("foo,bar,baz", joiner.join(it));
    }

    @Test
    public void testJoin_array() {
        Joiner joiner = Joiner.on(",");
        Object[] arr = new Object[]{"foo", "bar", "baz"};
        assertEquals("foo,bar,baz", joiner.join(arr));
    }

    @Test
    public void testJoin_useForNull_empty() {
        Joiner joiner = Joiner.on(":").useForNull("");
        assertEquals("", joiner.join(Collections.emptyList()));
    }

    @Test
    public void testJoin_useForNull_singleElement() {
        Joiner joiner = Joiner.on(":").useForNull("");
        assertEquals("foo", joiner.join(Arrays.asList("foo")));
    }

    @Test
    public void testJoin_useForNull_multipleElements() {
        Joiner joiner = Joiner.on(":").useForNull("");
        assertEquals("foo:N/A:bar:N/A:baz", joiner.join(Arrays.asList("foo", null, "bar", null, "baz")));
    }

    @Test
    public void testJoin_useForNull_map() {
        Joiner joiner = Joiner.on(":").useForNull("N/A");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put(null, null);
        map.put("key3", "value3");
        assertEquals("key1:value1:N/A:N/A:key3", joiner.join(map.entrySet()));
    }

    @Test
    public void testJoin_skipNulls_singleNull() {
        Joiner joiner = Joiner.on(":").skipNulls();
        assertEquals("foo:bar", joiner.join(Arrays.asList("foo", null, "bar")));
    }

    @Test
    public void testJoin_skipNulls_multipleNull() {
        Joiner joiner = Joiner.on(":").skipNulls();
        assertEquals("foo:bar:baz", joiner.join(Arrays.asList("foo", null, null, "baz")));
    }
}