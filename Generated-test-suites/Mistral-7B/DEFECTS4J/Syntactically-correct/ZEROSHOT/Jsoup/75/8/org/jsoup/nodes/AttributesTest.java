package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.jsoup.nodes.Attributes.dataPrefix;
import static org.jsoup.nodes.Attributes.Empty;
import static org.jsoup.nodes.Attributes.InitialCapacity;
import static org.jsoup.nodes.Attributes.NotFound;

public class AttributesTest {

    @Test
    public void testInitialState() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        assertEquals(Empty, attributes.get(null));
        assertEquals(Empty, attributes.getIgnoreCase(null));
        assertEquals(Empty, attributes.get(EmptyString));
        assertEquals(Empty, attributes.getIgnoreCase(EmptyString));
    }

    @Test
    public void testPutGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("value", attributes.getIgnoreCase("Key"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testPutGetMultiple() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value1", attributes.getIgnoreCase("Key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertEquals("value2", attributes.getIgnoreCase("Key2"));
        assertEquals(2, attributes.size());
    }

    @Test
    public void testPutOverwrite() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        assertEquals("value2", attributes.get("key"));
        assertEquals("value2", attributes.getIgnoreCase("key"));
        assertEquals("value2", attributes.getIgnoreCase("Key"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testPutDataAttribute() {
        Attributes attributes = new Attributes();
        String dataKey = dataPrefix + "dataKey";
        attributes.put(dataKey, "value");
        assertEquals("value", attributes.getIgnoreCase(dataKey));
        assertEquals("value", attributes.get(dataKey));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", false);
        assertTrue(attributes.hasKey("key"));
        assertNull(attributes.get("key"));
        assertEquals(0, attributes.size());
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.removeIgnoreCase("Key1");
        assertFalse(attributes.hasKey("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testRemoveNonExistent() {
        Attributes attributes = new Attributes();
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertEquals(0, attributes.size());
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testClear() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.clear();
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
        assertEquals(0, attributes.size());
    }

    @Test
    public void testCloning() {
        Attributes original = new Attributes();
        original.put("key1", "value1");
        original.put("key2", "value2");
        Attributes cloned = (Attributes) original.clone();
        assertEquals(original.size(), cloned.size());
        assertArrayEquals(original.keys, cloned.keys);
        assertArrayEquals(original.vals, cloned.vals);
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("KEY1", "VALUE1");
        attributes.normalize();
        assertEquals("value1", attributes.get("key1"));
        assertTrue(attributes.hasKey("key1"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        assertEquals("value2", attributes1.get("key2"));
        assertTrue(attributes1.hasKey("key2"));
        assertEquals(2, attributes1.size());
    }

    @Test
    public void testIterating() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Map.Entry<String, String>> list = Arrays.asList(new AbstractMap.SimpleEntry<>("key1", "value1"),
                new AbstractMap.SimpleEntry<>("key2", "value2"));
        Iterator<Map.Entry<String, String>> iterator = attributes.asList().iterator();
        List<Map.Entry<String, String>> expectedList = Collections.unmodifiableList(attributes.asList());
        for (Map.Entry<String, String> entry : expectedList) {
            assertEquals(entry.getKey(), iterator.next().getKey());
            assertEquals(entry.getValue(), iterator.next().getValue());
        }
        assertEquals(expectedList.size(), iterator.nextIndex());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "dataValue1");
        attributes.put("data-key2", "dataValue2");
        Map<String, String> dataset = attributes.dataset();
        Iterator<Map.Entry<String, String>> datasetIterator = dataset.entrySet().iterator();
        for (Map.Entry<String, String> entry : attributes.asList()) {
            if (entry.getKey().startsWith(dataPrefix)) {
                assertEquals(entry.getKey(), datasetIterator.next().getKey());
                assertEquals(entry.getValue(), datasetIterator.next().getValue());
            }
        }
        assertEquals(attributes.size(), dataset.size());
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals("key1=\"value1\" key2=\"value2\"", attributes.toString());
    }
}