package org.jsoup.nodes.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assertions.assertEquals("value", attributes.get("key"));
        Assertions.assertEquals("", attributes.get("notExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");
        Assertions.assertEquals("VALUE", attributes.getIgnoreCase("Key"));
        Assertions.assertEquals("", attributes.getIgnoreCase("NotExistentKey"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assertions.assertEquals("value", attributes.get("key"));

        attributes.put("key", "newValue");
        Assertions.assertEquals("newValue", attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        Assertions.assertTrue(attributes.hasKey("key"));
        Assertions.assertFalse(attributes.hasKey("key") + ": Expected false");
        Assertions.assertTrue(attributes.getBoolean("key"));
        Assertions.assertFalse(attributes.getBoolean("notExistentKey"));

        attributes.put("key", false);
        Assertions.assertFalse(attributes.getBoolean("key"));
        Assertions.assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testPutExisting() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        Assertions.assertEquals("value2", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        Assertions.assertEquals("", attributes.get("key"));
        Assertions.assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");
        attributes.removeIgnoreCase("Key");
        Assertions.assertEquals("", attributes.get("Key"));
        Assertions.assertFalse(attributes.hasKey("Key"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        Assertions.assertFalse(attributes.hasKey("key"));
        attributes.put("key", "value");
        Assertions.assertTrue(attributes.hasKey("key"));
        Assertions.assertFalse(attributes.hasKey("notExistentKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");
        Assertions.assertFalse(attributes.hasKeyIgnoreCase("key"));
        Assertions.assertTrue(attributes.hasKeyIgnoreCase("Key"));
        Assertions.assertFalse(attributes.hasKeyIgnoreCase("notExistentKey"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        Assertions.assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assertions.assertEquals(2, attributes.size());
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

        Assertions.assertEquals(2, attributes3.size());
        Assertions.assertEquals("value1", attributes3.get("key1"));
        Assertions.assertEquals("value2", attributes3.get("key2"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        Assertions.assertEquals(2, dataset.size());
        Assertions.assertEquals("value1", dataset.get("data-key1"));
        Assertions.assertEquals("value2", dataset.get("data-key2"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        StringBuilder expected = new StringBuilder();
        expected.append(" ");
        expected.append("key=\"value\"");

        StringBuilder actual = new StringBuilder();
        try {
            attributes.html(actual, new Document.OutputSettings());
        } catch (IOException e) {
            Assertions.fail("IOException: " + e.getMessage());
        }

        Assertions.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", new BooleanAttribute("booleanKey", true));

        Attributes clonedAttributes = attributes.clone();
        Assertions.assertEquals(attributes.size(), clonedAttributes.size());
        Assertions.assertEquals(attributes.get("key1"), clonedAttributes.get("key1"));
        Assertions.assertEquals(attributes.getBoolean("booleanKey"), clonedAttributes.getBoolean("booleanKey"));
    }

    @Test
    public void testCustomDataAttributeComparison() {
        Attributes attributes1 = new Attributes();
        attributes1.put("data-key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("data-Key1", "VALUE1");

        Assertions.assertEquals(attributes1, attributes2);
    }

    @Test
    public void testCustomDataAttributeRemoval() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.removeIgnoreCase("data-key1");
        Assertions.assertFalse(attributes.hasKey("data-key1"));
    }

    @Test
    public void testCustomDataAttributeIteration() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        List<Map.Entry<String, String>> dataset = new ArrayList<>();
        for (Map.Entry<String, Attribute> entry : attributes.asList()) {
            if (entry.getValue().isDataAttribute()) {
                dataset.add(new AbstractMap.SimpleEntry<>(entry.getKey().substring(6), entry.getValue().getValue()));
            }
        }

        Assertions.assertEquals(Arrays.asList(
                new AbstractMap.SimpleEntry<>("data-key1", "value1"),
                new AbstractMap.SimpleEntry<>("data-key2", "value2")),
                dataset);
    }
}