package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String value = attributes.get("key");
        Assert.assertEquals("value", value);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "Value");
        String value = attributes.getIgnoreCase("key");
        Assert.assertEquals("Value", value);
    }

    @Test
    public void testGetWhenNoAttribute() {
        Attributes attributes = new Attributes();
        String value = attributes.get("key");
        Assert.assertEquals("", value);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        Attribute attribute = attributes.get("key");
        Assert.assertTrue(attribute instanceof BooleanAttribute);
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.get("key").getValue().isEmpty());
    }

    @Test
    public void testPutOverwrite() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        Assert.assertEquals("value2", attributes.get("key"));
    }

    @Test
    public void testPutNull() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
        Assert.assertFalse(attributes.hasKey(null));
    }

    @Test
    public void testPutEmptyString() {
        Attributes attributes = new Attributes();
        attributes.put("", "value");
        Assert.assertFalse(attributes.hasKey(""));
    }

    @Test
    public void testPutSameKeyTwice() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        Assert.assertEquals("value2", attributes.get("key"));
    }

    @Test
    public void testPutEmptySet() {
        Attributes attributes = new Attributes();
        Attributes incoming = new Attributes();
        attributes.addAll(incoming);
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testPutNonEmptySet() {
        Attributes attributes = new Attributes();
        Attributes incoming = new Attributes();
        incoming.put("key1", "value1");
        incoming.put("key2", "value2");
        attributes.addAll(incoming);
        Assert.assertEquals(2, attributes.size());
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testPutSameKeyDifferentCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        Attributes incoming = new Attributes();
        incoming.put("Key", "value2");
        attributes.addAll(incoming);
        Assert.assertEquals("value2", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        Assert.assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.removeIgnoreCase("Key");
        Assert.assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveNonExistent() {
        Attributes attributes = new Attributes();
        attributes.remove("key");
        Assert.assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveMultiple() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("key1");
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveAll() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.clear();
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("key1"));
        Assert.assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKey("other"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("Key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("other"));
    }

    @Test
    public void testHasKeyNonExistent() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testSizeEmpty() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("data-key2", "value2");
        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals(1, dataset.size());
        Assert.assertEquals("value2", dataset.get("data-key2"));
    }

    @Test
    public void testDatasetEmpty() {
        Attributes attributes = new Attributes();
        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals(0, dataset.size());
    }

    @Test
    public void testHtml() throws IOException {
        Document doc = new Document();
        Element elem = doc.createElement("div");
        Attributes attributes = elem.attributes();
        attributes.put("key", "value");
        String html = elem.html();
        Assert.assertEquals("<div key=\"value\"></div>", html);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes cloned = attributes.clone();
        Assert.assertEquals("value1", cloned.get("key1"));
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = attributes1;
        Assert.assertNotEquals(attributes1, attributes2);
        Assert.assertEquals(attributes1, attributes3);

        attributes1.put("key", "value");
        attributes2.put("key", "value");
        Assert.assertEquals(attributes1, attributes2);

        attributes1.put("key2", "value2");
        Assert.assertNotEquals(attributes1, attributes2);

        Attributes attributes4 = new Attributes();
        attributes4.put("key3", "value3");
        Assert.assertNotEquals(attributes1, attributes4);
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = attributes1;
        Attributes attributes4 = new Attributes();
        attributes4.put("key", "value");

        Assert.assertEquals(attributes1.hashCode(), attributes1.hashCode());
        Assert.assertEquals(attributes1.hashCode(), attributes3.hashCode());
        Assert.assertNotEquals(attributes1.hashCode(), attributes2.hashCode());
        Assert.assertNotEquals(attributes1.hashCode(), attributes4.hashCode());

        attributes1.put("key2", "value2");
        Assert.assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
        Assert.assertNotEquals(attributes1.hashCode(), attributes4.hashCode());
    }
}