package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Assert.assertEquals("test", attributes.get("class"));
        Assert.assertEquals("", attributes.get("nonExistent"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Assert.assertEquals("test", attributes.getIgnoreCase("class"));
        Assert.assertEquals("", attributes.getIgnoreCase("ClAsS"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Assert.assertEquals("test", attributes.get("class"));

        attributes.put("title", "Test Title");
        Assert.assertEquals("Test Title", attributes.get("title"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("data-checked", true);
        Assert.assertTrue(attributes.hasKey("data-checked"));
        Assert.assertFalse(attributes.hasKey("data-unchecked"));
        Assert.assertEquals("", attributes.get("data-unchecked"));

        attributes.put("data-checked", false);
        Assert.assertFalse(attributes.hasKey("data-checked"));
        Assert.assertFalse(attributes.hasKey("data-unchecked"));

        attributes.put("data-unchecked", true);
        Assert.assertFalse(attributes.hasKey("data-checked"));
        Assert.assertTrue(attributes.hasKey("data-unchecked"));
        Assert.assertEquals("", attributes.get("data-checked"));
    }

    @Test
    public void testPutExists() {
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        attributes.put("class", "newTest");
        Assert.assertEquals("newTest", attributes.get("class"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        attributes.remove("class");
        Assert.assertEquals("", attributes.get("class"));

        attributes.put("class", "test");
        attributes.removeIgnoreCase("class");
        Assert.assertEquals("", attributes.get("class"));
    }

    @Test
    public void testRemoveNonExistent() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKey("nonexistent"));
        attributes.remove("nonexistent");
        Assert.assertFalse(attributes.hasKey("nonexistent"));
    }

    @Test
    public void testRemoveIgnoreCaseNonExistent() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKeyIgnoreCase("nonexistent"));
        attributes.removeIgnoreCase("nonexistent");
        Assert.assertFalse(attributes.hasKeyIgnoreCase("nonexistent"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKey("nonexistent"));

        attributes.put("class", "test");
        Assert.assertTrue(attributes.hasKey("class"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("class"));
        Assert.assertFalse(attributes.hasKey("CLASS"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());

        attributes.put("class", "test");
        Assert.assertEquals(1, attributes.size());

        attributes.put("title", "Test Title");
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("class", "test1");
        Attributes attributes2 = new Attributes();
        attributes2.put("class", "test2");
        attributes1.addAll(attributes2);
        Assert.assertEquals("test1 test2", attributes1.html());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-test", "value");
        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals("value", dataset.get("data-test"));
        Assert.assertEquals(1, dataset.size());
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Document document = new Document("");
        try {
            attributes.html(document.appendBody().htmlOutput(), document.outputSettings());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals("<html><body class=\"test\"></body></html>", document.html());
    }
}