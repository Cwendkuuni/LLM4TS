package org.jsoup.nodes;

import org.jsoup.Assert; // for Assert.assertEquals, Assert.assertNotNull, etc.
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testAttributes() {
        Attributes attributes = new Attributes();

        // Test get and put
        attributes.put("key1", "value1");
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value1", attributes.getIgnoreCase("key1"));

        // Test putIgnoreCase
        attributes.putIgnoreCase("Key1", "value2");
        Assert.assertEquals("value2", attributes.getIgnoreCase("Key1"));
        Assert.assertEquals("value2", attributes.get("Key1"));

        // Test size
        Assert.assertEquals(0, attributes.size());
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());

        // Test hasKey
        Assert.assertFalse(attributes.hasKey("key3"));
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));

        // Test remove
        attributes.remove("key1");
        Assert.assertFalse(attributes.hasKey("key1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertEquals(1, attributes.size());

        // Test put with boolean value
        attributes.put("key3", true);
        Assert.assertFalse(attributes.hasKey("key3")); // Case sensitive
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key3")); // Case insensitive

        // Test put with Attribute
        Attribute attribute = new Attribute("key4", "value4");
        attributes.put(attribute);
        Assert.assertEquals("value4", attributes.get("key4"));

        // Test putAll
        Attributes attributes2 = new Attributes();
        attributes2.put("key5", "value5");
        attributes2.put("key6", "value6");
        attributes.putAll(attributes2);
        Assert.assertEquals(6, attributes.size());

        // Test iterator
        List<Map.Entry<String, String>> entries = new ArrayList<>();
        for (Map.Entry<String, String> entry : attributes.asList().iterator()) {
            entries.add(entry);
        }
        Assert.assertEquals(6, entries.size());
        Assert.assertEquals("key1", entries.get(0).getKey());
        Assert.assertEquals("value2", entries.get(0).getValue());
        Assert.assertEquals("key3", entries.get(1).getKey());
        Assert.assertEquals(null, entries.get(1).getValue()); // boolean attribute
        Assert.assertEquals("key4", entries.get(2).getKey());
        Assert.assertEquals("value4", entries.get(2).getValue());
        Assert.assertEquals("key5", entries.get(3).getKey());
        Assert.assertEquals("value5", entries.get(3).getValue());
        Assert.assertEquals("key6", entries.get(4).getKey());
        Assert.assertEquals("value6", entries.get(4).getValue());

        // Test dataset
        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals(4, dataset.size());
        Assert.assertEquals("value1", dataset.get("key1"));
        Assert.assertEquals("value2", dataset.get("Key1"));
        Assert.assertEquals("value4", dataset.get("key4"));

        // Test html
        StringBuilder sb = new StringBuilder();
        try {
            attributes.html(sb, null);
        } catch (IOException e) {
            throw new AssertionError("IOException should not be thrown");
        }
        Assert.assertEquals("<key1=\"value1\" key2 key3=\"\" key4=\"value4\" key5=\"value5\" key6=\"value6\">", sb.toString());

        // Test equals and hashCode
        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value1");
        attributes3.put("key2", "value2");
        Assert.assertEquals(attributes, attributes);
        Assert.assertEquals(attributes, attributes3);
        Assert.assertEquals(attributes.hashCode(), attributes3.hashCode());

        // Test clone
        Attributes cloned = attributes.clone();
        Assert.assertEquals(attributes, cloned);

        // Test normalize
        attributes.normalize();
        Assert.assertEquals("key1", attributes.keys[0]);
        Assert.assertEquals("value1", attributes.vals[0]);
        Assert.assertEquals("key2", attributes.keys[1]);
        Assert.assertEquals("value2", attributes.vals[1]);
        Assert.assertEquals("key3", attributes.keys[2]);
        Assert.assertNull(attributes.vals[2]);
        Assert.assertEquals("key4", attributes.keys[3]);
        Assert.assertEquals("value4", attributes.vals[3]);
        Assert.assertEquals("key5", attributes.keys[4]);
        Assert.assertEquals("value5", attributes.vals[4]);
        Assert.assertEquals("key6", attributes.keys[5]);
        Assert.assertEquals("value6", attributes.vals[5]);
    }
}