package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setup() {
        attributes = new Attributes();
        attributes.put("class", "test-class");
        attributes.put("data-test", "test-data");
    }

    @Test
    public void testGet() {
        assertEquals("test-class", attributes.get("class"));
        assertEquals("test-data", attributes.get("data-test"));
        assertEquals("", attributes.get("unknown"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("test-class", attributes.getIgnoreCase("class"));
        assertEquals("test-data", attributes.getIgnoreCase("DATA-TEST"));
        assertEquals("", attributes.getIgnoreCase("unknown"));
    }

    @Test
    public void testPut() {
        attributes.put("new-key", "new-value");
        assertEquals("new-value", attributes.get("new-key"));
    }

    @Test
    public void testPutIgnoreCase() {
        attributes.putIgnoreCase("new-key-ignore-case", "new-value-ignore-case");
        assertEquals("new-value-ignore-case", attributes.get("new-key-ignore-case"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("test", true);
        assertTrue(attributes.hasKey("test"));
        assertFalse(attributes.get("test").isEmpty());
        assertFalse(attributes.get("test").equals("test"));
    }

    @Test
    public void testPutBooleanAndRemove() {
        attributes.put("test", true);
        assertTrue(attributes.hasKey("test"));
        attributes.remove("test");
        assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testPutAndAddAll() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("other-key", "other-value");
        attributes.addAll(otherAttributes);
        assertEquals(2, attributes.size());
        assertTrue(attributes.hasKey("class"));
        assertTrue(attributes.hasKey("other-key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithNullKey() {
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithNullValue() {
        attributes.put("key", null);
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("class"));
        assertTrue(attributes.hasKey("data-test"));
        assertFalse(attributes.hasKey("unknown"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKeyIgnoreCase("class"));
        assertTrue(attributes.hasKeyIgnoreCase("DATA-TEST"));
        assertFalse(attributes.hasKeyIgnoreCase("unknown"));
    }

    @Test
    public void testSize() {
        assertEquals(2, attributes.size());
    }

    @Test
    public void testRemove() {
        attributes.remove("class");
        assertFalse(attributes.hasKey("class"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.removeIgnoreCase("class");
        assertFalse(attributes.hasKey("class"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testDataset() {
        Map<String, String> dataset = attributes.dataset();
        assertEquals("test-class", dataset.get("class"));
        assertEquals("test-data", dataset.get("data-test"));
        assertNull(dataset.get("unknown"));
    }

    @Test
    public void testToString() {
        assertEquals("class=\"test-class\" data-test=\"test-data\"", attributes.toString());
    }

    @Test
    public void testEquals() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("class", "test-class");
        otherAttributes.put("data-test", "test-data");
        assertTrue(attributes.equals(attributes));
        assertTrue(attributes.equals(otherAttributes));
        assertFalse(attributes.equals(new Object()));
    }
}