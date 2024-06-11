package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGetAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals(Attributes.EMPTY_STRING, attributes.get("nonexistent"));
    }

    @Test
    public void testGetAttributeIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals(Attributes.EMPTY_STRING, attributes.getIgnoreCase("NONEEXISTENT"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));
        attributes.put("key2", "value3");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value3", attributes.get("key2"));
    }

    @Test
    public void testPutIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("Key1"));
        assertEquals("value1", attributes.get("key1"));
        attributes.put("Key2", "value3");
        assertEquals("value3", attributes.get("Key2"));
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertTrue(attributes.get("key") == null);
        attributes.put("key", false);
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testPutAttributeWithNullValue() {
        Attributes attributes = new Attributes();
        assertNull(attributes.get("key"));
        attributes.put("key", null);
        assertNull(attributes.get("key"));
        assertEquals(0, attributes.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutAttributeWithEmptyKey() {
        Attributes attributes = new Attributes();
        attributes.put(Attributes.EMPTY_STRING, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutAttributeWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveAttributeWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.remove(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAttributeWithNonexistentKey() {
        Attributes attributes = new Attributes();
        attributes.remove("nonexistent");
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        assertEquals("value1", attributes1.get("key1"));
        assertEquals("value2", attributes1.get("key2"));
        assertEquals(2, attributes1.size());
    }

    @Test(expected = NullPointerException.class)
    public void testAddAllWithNullAttributes() {
        Attributes attributes = new Attributes();
        attributes.addAll(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAllWithEmptyAttributes() {
        Attributes attributes = new Attributes();
        attributes.addAll(new Attributes());
    }

    @Test
    public void testDataSet() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        Map<String, String> dataSet = attributes.dataset();
        assertEquals("value1", dataSet.get("data-key1"));
        assertEquals("value2", dataSet.get("data-key2"));
        assertEquals(2, dataSet.size());
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        StringBuilder sb = new StringBuilder();
        try {
            attributes.html(sb, new Document.OutputSettings());
        } catch (IOException e) {
        }
        assertEquals("key1=\"value1\" key2=\"value2\"", sb.toString());
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals("key1=\"value1\" key2=\"value2\"", attributes.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key1", "value2");
        attributes1.put("key2", "value2");
        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
        assertFalse(attributes1.equals(null));
        assertFalse(attributes1.equals(""));
        assertFalse(attributes1.equals(new Object()));
    }
}