package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGetEmpty() {
        assertNull(attributes.get(""));
    }

    @Test
    public void testGetIgnoreCaseEmpty() {
        assertNull(attributes.getIgnoreCase(""));
    }

    @Test
    public void testGetNullAttributes() {
        assertNull(attributes.get("key"));
    }

    @Test
    public void testGet() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals("", attributes.get("otherKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("", attributes.getIgnoreCase("OtherKey"));
    }

    @Test
    public void testPut() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testPutIgnoreCase() {
        attributes.put("key1", "value1");
        attributes.put("KEY2", "value2");

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.getIgnoreCase("KEY2"));
    }

    @Test
    public void testPutWithSameKey() {
        attributes.put("key", "value1");
        attributes.put("key", "value2");

        assertEquals("value2", attributes.get("key"));
        assertEquals("", attributes.get("otherKey"));
    }

    @Test
    public void testPutBooleanTrue() {
        attributes.put("key", false);
        attributes.put("key", true);

        assertFalse(attributes.getBoolean("key"));
    }

    @Test
    public void testPutBooleanFalse() {
        attributes.put("key", true);
        attributes.put("key", false);

        assertFalse(attributes.getBoolean("key"));
    }

    @Test
    public void testPutNullValue() {
        attributes.put("key", null);

        assertNull(attributes.get("key"));
    }

    @Test
    public void testPutNullValueIgnoreCase() {
        attributes.put("KEY", null);

        assertNull(attributes.getIgnoreCase("key"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key3"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("key1", "value1");
        attributes.put("KEY2", "value2");

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY2"));
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.size());
    }

    @Test
    public void testSizeEmpty() {
        assertEquals(0, attributes.size());
    }

    @Test
    public void testAddAllNull() {
        attributes.addAll(null);

        assertEquals(0, attributes.size());
    }

    @Test
    public void testAddAllEmpty() {
        Attributes empty = new Attributes();
        attributes.addAll(empty);

        assertEquals(0, attributes.size());
    }

    @Test
    public void testAddAllOne() {
        Attributes other = new Attributes();
        other.put("key", "value");

        attributes.addAll(other);

        assertEquals("value", attributes.get("key"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAllMultiple() {
        Attributes other1 = new Attributes();
        other1.put("key1", "value1");
        other1.put("key2", "value2");

        Attributes other2 = new Attributes();
        other2.put("key3", "value3");

        attributes.addAll(other1);
        attributes.addAll(other2);

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("value3", attributes.get("key3"));
        assertEquals(3, attributes.size());
    }

    @Test
    public void testIterator() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<String> keys = new ArrayList<>();
        for (String key : attributes) {
            keys.add(key);
        }

        assertEquals(Arrays.asList("key1", "key2"), keys);
    }

    @Test
    public void testAsList() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> list = attributes.asList();

        assertEquals(2, list.size());
        assertEquals("value1", list.get(0).getValue());
        assertEquals("value2", list.get(1).getValue());
    }

    @Test
    public void testDatasetEmpty() {
        Map<String, String> dataset = attributes.dataset();

        assertEquals(0, dataset.size());
    }

    @Test
    public void testDatasetOne() {
        attributes.put("data-key", "value");

        Map<String, String> dataset = attributes.dataset();

        assertEquals("value", dataset.get("key"));
        assertEquals(1, dataset.size());
    }

    @Test
    public void testDatasetMultiple() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals("value1", dataset.get("key1"));
        assertEquals("value2", dataset.get("key2"));
        assertEquals(2, dataset.size());
    }

    @Test
    public void testHtmlEmpty() {
        assertEquals("", attributes.html());
    }

    @Test
    public void testHtmlOne() {
        attributes.put("key", "value");

        assertEquals(" key=\"value\"", attributes.html());
    }

    @Test
    public void testHtmlMultiple() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(" key1=\"value1\" key2=\"value2\"", attributes.html());
    }

    @Test
    public void testToString() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals("key1=\"value1\" key2=\"value2\"", attributes.toString());
    }

    @Test
    public void testEqualsSame() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes(attributes1);

        assertTrue(attributes1.equals(attributes2));
    }

    @Test
    public void testEqualsDifferent() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes2.put("key", "value");

        assertFalse(attributes1.equals(attributes2));
    }

    @Test
    public void testHashCodeSame() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();

        assertEquals(attributes1.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testHashCodeDifferent() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key", "value");
        Attributes attributes2 = new Attributes();

        assertNotEquals(attributes1.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testCloneEmpty() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = attributes1.clone();

        assertEquals(attributes1, attributes2);
    }

    @Test
    public void testCloneWithAttributes() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = attributes1.clone();

        assertEquals(attributes1, attributes2);
        assertNotSame(attributes1, attributes2);
        assertEquals(2, attributes2.size());
        assertEquals("value1", attributes2.get("key1"));
        assertEquals("value2", attributes2.get("key2"));
    }
}