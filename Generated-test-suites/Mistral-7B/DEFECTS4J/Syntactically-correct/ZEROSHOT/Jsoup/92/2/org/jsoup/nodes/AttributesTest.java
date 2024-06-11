package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testAttributes() {
        Attributes attributes = new Attributes();

        // Test setting and getting attributes
        attributes.put("key1", "value1");
        Assert.assertEquals("value1", attributes.get("key1"));

        // Test setting the same attribute multiple times
        attributes.put("key1", "value2");
        Assert.assertEquals("value2", attributes.get("key1"));

        // Test setting and getting case-insensitive attributes
        attributes.putIgnoreCase("Key1", "Value1");
        Assert.assertEquals("Value1", attributes.getIgnoreCase("Key1"));

        // Test setting boolean attributes
        attributes.put("booleanKey", true);
        Assert.assertTrue(attributes.hasKey("booleanKey"));
        Assert.assertFalse(attributes.get("booleanKey").isEmpty());

        // Test removing attributes
        attributes.remove("key1");
        Assert.assertFalse(attributes.hasKey("key1"));

        // Test removing case-insensitive attributes
        attributes.removeIgnoreCase("Key1");
        Assert.assertFalse(attributes.hasKeyIgnoreCase("Key1"));

        // Test checking for existence of attributes
        Assert.assertTrue(attributes.hasKey("booleanKey"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("BooleanKey"));
        Assert.assertFalse(attributes.hasKey("nonexistentKey"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("nonexistentKey"));

        // Test size of attributes
        Assert.assertEquals(0, attributes.size());
        attributes.put("key1", "value1");
        Assert.assertEquals(1, attributes.size());

        // Test adding attributes from another Attributes object
        Attributes attributes1 = new Attributes();
        attributes1.put("key2", "value2");
        attributes.addAll(attributes1);
        Assert.assertEquals(2, attributes.size());

        // Test iterating through attributes
        Attributes.Iterator iterator = attributes.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            Assert.assertEquals("key" + i, attribute.getKey());
            Assert.assertEquals("value" + i, attribute.getValue());
            i++;
        }

        // Test dataset() method
        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals("key0", dataset.keySet().iterator().next());
        Assert.assertEquals("value0", dataset.get("key0"));
        Assert.assertEquals("key1", dataset.keySet().iterator().next());
        Assert.assertEquals("value1", dataset.get("key1"));
    }
}