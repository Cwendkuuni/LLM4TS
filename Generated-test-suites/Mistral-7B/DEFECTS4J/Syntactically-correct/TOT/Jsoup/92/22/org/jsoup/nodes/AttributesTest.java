package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testGetAndPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Assert.assertEquals("value1", attributes.get("key1"));

        attributes.put("key2", "value2");
        Assert.assertEquals("value2", attributes.get("key2"));

        attributes.put("key1", "value1_updated");
        Assert.assertEquals("value1_updated", attributes.get("key1"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        Assert.assertEquals("value1", attributes.getIgnoreCase("key1"));
        Assert.assertEquals("value1", attributes.getIgnoreCase("Key1"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key1", "value1");
        Assert.assertEquals("value1", attributes.get("Key1"));
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value1", attributes.getIgnoreCase("Key1"));
        Assert.assertEquals("value1", attributes.getIgnoreCase("key1"));
    }

    @Test
    public void testPutAndRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Assert.assertEquals("value1", attributes.get("key1"));

        attributes.remove("key1");
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");

        Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        Assert.assertEquals(1, attributes.size());

        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());

        attributes.remove("key1");
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");

        Attributes result = new Attributes();
        result.addAll(attributes1);
        result.addAll(attributes2);

        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.hasKey("key1"));
        Assert.assertTrue(result.hasKey("key2"));
        Assert.assertEquals("value1", result.get("key1"));
        Assert.assertEquals("value2", result.get("key2"));
    }

    @Test
    public void testIterable() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> expectedList = new HashMap<>() {{
            put(new AbstractMap.SimpleEntry("key1", "value1"), null);
            put(new AbstractMap.SimpleEntry("key2", "value2"), null);
        }};

        List<Map.Entry<String, String>> actualList = new ArrayList<>(attributes);

        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "data-value1");
        attributes.put("data-key2", "data-value2");

        Map<String, String> dataset = attributes.dataset();

        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals("data-value1", dataset.get("data-key1"));
        Assert.assertEquals("data-value2", dataset.get("data-key2"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes attributesWithData = new Attributes();
        attributesWithData.put("data-key1", "data-value1");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key1=\"value1\"");

        StringBuilder actualHtmlAttributes = new StringBuilder();
        StringBuilder actualHtmlAttributesWithData = new StringBuilder();

        try {
            attributes.html(actualHtmlAttributes, new Document.OutputSettings());
            attributesWithData.html(actualHtmlAttributesWithData, new Document.OutputSettings());
            expectedHtml.append(actualHtmlAttributes);
            expectedHtml.append(actualHtmlAttributesWithData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Assert.assertEquals(expectedHtml.toString(), attributes.html());
        Assert.assertEquals(expectedHtml.toString(), attributesWithData.html());
    }
}