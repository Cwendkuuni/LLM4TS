package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class AttributesTest {

    @Test
    public void testGetAndPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        attributes.put("key2", "value2");
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertEquals("value1", attributes.getIgnoreCase("Key1"));
    }

    @Test
    public void testPutWithExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        assertTrue(attributes.hasKey("booleanKey"));
        assertFalse(attributes.get("booleanKey").isEmpty());
        assertFalse(attributes.hasKey("BooleanKey"));
        assertFalse(attributes.getIgnoreCase("BooleanKey").isEmpty());
        attributes.put("booleanKey", false);
        assertFalse(attributes.hasKey("booleanKey"));
        assertFalse(attributes.get("booleanKey").isEmpty());
        assertFalse(attributes.getIgnoreCase("BooleanKey").isEmpty());
        attributes.remove("booleanKey");
        assertFalse(attributes.hasKey("booleanKey"));
        assertFalse(attributes.get("booleanKey").isEmpty());
        assertFalse(attributes.getIgnoreCase("BooleanKey").isEmpty());
    }

    @Test
    public void testPutAttributeWithExistingKeyAndValue() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));
        assertEquals(Arrays.asList("value1", "value2"), attributes.asList().stream().map(Attribute::getKey).toList());
    }

    @Test
    public void testPutSameKeyMultipleTimes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        attributes.put("key1", "value3");
        assertEquals("value3", attributes.get("key1"));
        assertEquals(Arrays.asList("value1", "value2", "value3"), attributes.asList().stream().map(Attribute::getKey).toList());
    }

    @Test
    public void testPutEmptyValue() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "");
        assertEquals("", attributes.get("key1"));
    }

    @Test
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key1", null);
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.get("key1").isEmpty());
        assertEquals(null, attributes.get("key1"));
    }

    @Test
    public void testSizeAndCapacity() {
        Attributes attributes = new Attributes();
        for (int i = 0; i < 100; i++) {
            attributes.put("key" + i, "value" + i);
        }
        assertEquals(100, attributes.size());
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("KEY2"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveInvalidIndex() {
        Attributes attributes = new Attributes();
        attributes.remove(10);
    }

    @Test
    public void testRemoveWithExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testRemoveWithMissingKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testContainsNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key1", null);
        assertTrue(attributes.containsValue(null));
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);
        assertEquals(Arrays.asList("key1", "key2"), attributes3.asList().stream().map(Attribute::getKey).toList());
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals(Arrays.asList("key1=value1"), attributes.asList());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        assertEquals(Arrays.asList(new AbstractMap.SimpleEntry<>("key1", "value1")), attributes.dataset().entrySet().stream().toList());
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("<key1=value1>", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        Attributes attributes3 = new Attributes();
        attributes3.put("key2", "value2");
        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes clonedAttributes = attributes.clone();
        assertEquals("value1", clonedAttributes.get("key1"));
        attributes.put("key2", "value2");
        assertFalse(clonedAttributes.equals(attributes));
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.normalize();
        assertEquals("key1", attributes.get("key1"));
    }
}