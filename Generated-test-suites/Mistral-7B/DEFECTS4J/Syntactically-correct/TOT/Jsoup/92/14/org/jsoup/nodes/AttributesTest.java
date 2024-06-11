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
    public void testGet() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals(EmptyString, attributes.get("emptyKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("value", attributes.getIgnoreCase("Key"));
        assertEquals(EmptyString, attributes.getIgnoreCase("emptyKey"));
    }

    @Test
    public void testPut() {
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));
        assertEquals(EmptyString, attributes.get("key2"));

        attributes.put("key2", "value2");
        assertEquals("value2", attributes.get("key2"));
        assertEquals("value2", attributes.getIgnoreCase("Key2"));
    }

    @Test
    public void testPutIgnoreCase() {
        attributes.put("key1", "value1");
        attributes.put("Key1", "value2");
        assertEquals("value2", attributes.get("key1"));
        assertEquals("value2", attributes.getIgnoreCase("Key1"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key", true);
        assertTrue(attributes.get("key").equals(EmptyString) || attributes.hasKey("key"));
        assertFalse(attributes.get("key").equals("false"));

        attributes.put("key", false);
        assertFalse(attributes.get("key").equals(EmptyString) || attributes.hasKey("key"));
    }

    @Test
    public void testRemove() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("Key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("Key1");
        assertFalse(attributes.hasKey("Key1"));
        assertEquals("value2", attributes.get("key2"));
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
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        attributes.addAll(attributes2);
        assertEquals(4, attributes.size());
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertTrue(attributes.hasKey("key3"));
        assertTrue(attributes.hasKey("key4"));
    }

    @Test
    public void testAsList() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> list = attributes.asList();
        assertEquals(2, list.size());
        assertTrue(list.get(0).getKey().equals("key1"));
        assertTrue(list.get(0).getValue().equals("value1"));
        assertTrue(list.get(1).getKey().equals("key2"));
        assertTrue(list.get(1).getValue().equals("value2"));
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> map = attributes.dataset();
        assertEquals(2, map.size());
        assertTrue(map.containsKey("data-key1"));
        assertTrue(map.containsValue("value1"));
        assertTrue(map.containsKey("data-key2"));
        assertTrue(map.containsValue("value2"));
    }

}