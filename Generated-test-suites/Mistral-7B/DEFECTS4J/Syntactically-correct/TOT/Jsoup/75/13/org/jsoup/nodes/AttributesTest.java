package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGetEmptyAttributes() {
        assertEquals("", attributes.get(""));
        assertEquals("", attributes.getIgnoreCase(""));
        assertFalse(attributes.hasKey(""));
        assertFalse(attributes.hasKeyIgnoreCase(""));
        assertEquals(0, attributes.size());
    }

    @Test
    public void testGetSetAttributes() {
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertEquals(1, attributes.size());

        attributes.put("key2", "value2");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key2"));
        assertEquals(2, attributes.size());

        attributes.put("key1", "value3");
        assertEquals("value3", attributes.get("key1"));
        assertEquals("value3", attributes.getIgnoreCase("key1"));
        assertFalse(attributes.hasKey("value1"));
        assertFalse(attributes.hasKeyIgnoreCase("value1"));
        assertEquals(2, attributes.size());

        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
        assertEquals(1, attributes.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKey() {
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullValue() {
        attributes.put("key", null);
    }

    @Test
    public void testPutIgnoreCase() {
        attributes.putIgnoreCase("Key1", "value1");
        assertEquals("value1", attributes.get("Key1"));
        assertEquals("value1", attributes.getIgnoreCase("Key1"));
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertTrue(attributes.hasKey("Key1"));
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testPutBooleanAttribute() {
        attributes.put("booleanKey", true);
        assertTrue(attributes.hasKey("booleanKey"));
        assertFalse(attributes.get("booleanKey").isEmpty());

        attributes.put("booleanKey", false);
        assertFalse(attributes.hasKey("booleanKey"));
        assertTrue(attributes.get("booleanKey").isEmpty());

        attributes.putIgnoreCase("BOOLEANKEY", true);
        assertTrue(attributes.hasKey("booleanKey"));
        assertFalse(attributes.get("booleanKey").isEmpty());

        attributes.putIgnoreCase("BOOLEANKEY", false);
        assertFalse(attributes.hasKey("booleanKey"));
        assertTrue(attributes.get("booleanKey").isEmpty());
    }

    @Test
    public void testPutAttributeWithSameName() {
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        assertEquals("value2", attributes.get("key"));
        assertEquals("value2", attributes.getIgnoreCase("key"));
        assertTrue(attributes.hasKey("key"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testRemoveAttribute() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key2"));
        assertEquals(2, attributes.size());

        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
        assertEquals(1, attributes.size());

        attributes.remove("key2");
        assertFalse(attributes.hasKey("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
        assertEquals(0, attributes.size());
    }

    @Test
    public void testRemoveAttributeThatDoesNotExist() {
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        assertEquals(0, attributes.size());
    }

    @Test
    public void testRemoveAttributeByNameIgnoreCase() {
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value1", attributes.get("Key1"));
        assertEquals("value1", attributes.getIgnoreCase("Key1"));
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKey("Key1"));
        assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        assertEquals(1, attributes.size());

        attributes.remove("Key1");
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKey("Key1"));
        assertFalse(attributes.hasKeyIgnoreCase("Key1"));
        assertEquals(0, attributes.size());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        Map<String, String> dataset = attributes.dataset();
        assertEquals("value1", dataset.get("data-key1"));
        assertEquals("value1", dataset.getOrDefault("data-key1", ""));
        assertEquals("value2", dataset.get("data-key2"));
        assertEquals("value2", dataset.getOrDefault("data-key2", ""));
        assertFalse(dataset.containsKey("key1"));
        assertFalse(dataset.containsKey("key2"));
        assertEquals(2, dataset.size());
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        String html = attributes.html();
        String expected = " key1=\"value1\"";
        assertEquals(expected, html);

        attributes.remove("key1");
        html = attributes.html();
        expected = "";
        assertEquals(expected, html);
    }

    @Test
    public void testToString() {
        assertEquals("", attributes.toString());

        attributes.put("key1", "value1");
        assertEquals("{\"key1\":\"value1\"}", attributes.toString());

        attributes.put("key2", "value2");
        assertEquals("{\"key1\":\"value1\",\"key2\":\"value2\"}", attributes.toString());

        attributes.remove("key1");
        assertEquals("{\"key2\":\"value2\"}", attributes.toString());
    }

    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        Attributes clonedAttributes = attributes.clone();
        assertEquals("value1", clonedAttributes.get("key1"));
        assertEquals("value1", clonedAttributes.getIgnoreCase("key1"));
        assertTrue(clonedAttributes.hasKey("key1"));
        assertTrue(clonedAttributes.hasKeyIgnoreCase("key1"));
        assertEquals(1, clonedAttributes.size());

        attributes.remove("key1");
        assertEquals(0, clonedAttributes.size());
    }
}