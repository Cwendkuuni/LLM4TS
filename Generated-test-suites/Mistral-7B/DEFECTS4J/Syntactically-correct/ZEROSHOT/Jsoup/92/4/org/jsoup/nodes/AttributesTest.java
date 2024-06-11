package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.jsoup.nodes.Attributes.InitialCapacity;
import static org.jsoup.nodes.Attributes.GrowthFactor;

public class AttributesTest {

    @Test
    public void testInitialCapacity() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(InitialCapacity, attributes.keys.length);
    }

    @Test
    public void testGrowthFactor() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int initialCapacity = attributes.keys.length;
        attributes.put("key3", "value3");

        Assert.assertEquals(initialCapacity * GrowthFactor, attributes.keys.length);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Assert.assertEquals("value1", attributes.get("key1"));

        attributes.put("key1", "newValue1");
        Assert.assertEquals("newValue1", attributes.get("key1"));

        attributes.put("key2", "value2");
        Assert.assertEquals("value2", attributes.get("key2"));
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
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key1", "value1");
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value1", attributes.getIgnoreCase("Key1"));
        Assert.assertEquals("value1", attributes.getIgnoreCase("KEY1"));
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("booleanAttribute", true);
        Assert.assertTrue(attributes.hasKey("booleanAttribute"));
        Assert.assertFalse(attributes.get("booleanAttribute").isEmpty());
        Assert.assertNull(attributes.get("booleanAttribute").getValue());
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);

        Assert.assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        attributes.remove("key1");
        Assert.assertFalse(attributes.hasKey("key1"));
        Assert.assertEquals(2, attributes.size());

        attributes.remove("key2");
        Assert.assertFalse(attributes.hasKey("key2"));
        Assert.assertEquals(0, attributes.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNonExistentKey() {
        Attributes attributes = new Attributes();
        attributes.remove("nonExistentKey");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullKey() {
        Attributes attributes = new Attributes();
        attributes.remove(null);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertTrue(attributes.hasKey("key2"));
        Assert.assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("Key2", "value2");

        Assert.assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        Assert.assertEquals(3, attributes.size());
    }

    @Test
    public void testIterable() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> list = new ArrayList<>(attributes);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals("value1", list.get(0).getValue());
        Assert.assertEquals("value2", list.get(1).getValue());

        for (Attribute attribute : attributes) {
            Assert.assertNotNull(attribute);
            Assert.assertNotNull(attribute.getKey());
            Assert.assertNotNull(attribute.getValue());
        }

        int i = 0;
        for (Attribute attribute : attributes) {
            Assert.assertEquals(attribute.getKey(), "key" + i);
            Assert.assertEquals(attribute.getValue(), "value" + i++);
        }
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> list = attributes.asList();
        Assert.assertEquals(2, list.size());
        Assert.assertEquals("value1", list.get(0).getValue());
        Assert.assertEquals("value2", list.get(1).getValue());

        for (Attribute attribute : list) {
            Assert.assertNotNull(attribute);
            Assert.assertNotNull(attribute.getKey());
            Assert.assertNotNull(attribute.getValue());
        }

        int i = 0;
        for (Attribute attribute : list) {
            Assert.assertEquals(attribute.getKey(), "key" + i);
            Assert.assertEquals(attribute.getValue(), "value" + i++);
        }
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("Key2", "value2");

        attributes.removeIgnoreCase("Key1");
        Assert.assertFalse(attributes.hasKey("Key1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals("value1", dataset.get("data-key1"));
        Assert.assertEquals("value2", dataset.get("data-key2"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        StringBuilder sb = new StringBuilder();
        attributes.html(sb);

        Assert.assertEquals(" key1=\"value1\" key2=\"value2\"", sb.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();

        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        attributes2.put("key1", "value1");
        attributes2.put("key3", "value3");

        attributes3.put("key1", "value1");
        attributes3.put("key1", "value1"); // duplicate key

        Assert.assertEquals(attributes1, attributes1);
        Assert.assertEquals(attributes1, attributes1);

        Assert.assertNotEquals(attributes1, attributes2);
        Assert.assertNotEquals(attributes1, attributes3);

        Attributes attributes4 = attributes1.clone();
        Assert.assertEquals(attributes1, attributes4);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clonedAttributes = attributes.clone();
        Assert.assertEquals(attributes.size(), clonedAttributes.size());

        for (int i = 0; i < attributes.size(); i++) {
            String key = attributes.keys[i];
            String value = attributes.vals[i];
            String clonedKey = clonedAttributes.keys[i];
            String clonedValue = clonedAttributes.vals[i];

            Assert.assertEquals(key, clonedKey);
            Assert.assertEquals(value, clonedValue);
        }
    }
}