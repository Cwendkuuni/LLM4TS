package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.jsoup.nodes.Attributes.dataPrefix;
import static org.jsoup.nodes.Attributes.Empty;
import static org.jsoup.nodes.Attributes.EmptyString;
import static org.jsoup.nodes.Attributes.InitialCapacity;
import static org.jsoup.nodes.Attributes.GrowthFactor;
import static org.jsoup.nodes.Attributes.NotFound;

public class AttributesTest {

    @Test
    public void testEmptyAttributes() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
        Assert.assertArrayEquals(Empty, attributes.keys);
        Assert.assertArrayEquals(Empty, attributes.vals);
    }

    @Test
    public void testAddAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertEquals(1, attributes.size());
        Assert.assertArrayEquals(new String[]{"key"}, attributes.keys);
        Assert.assertArrayEquals(new String[]{"value"}, attributes.vals);
    }

    @Test
    public void testAddDuplicateAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        Assert.assertEquals(1, attributes.size());
        Assert.assertArrayEquals(new String[]{"key"}, attributes.keys);
        Assert.assertArrayEquals(new String[]{"value2"}, attributes.vals);
    }

    @Test
    public void testAddIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key", "Value");
        Assert.assertEquals(1, attributes.size());
        Assert.assertArrayEquals(new String[]{"Key"}, attributes.keys);
        Assert.assertArrayEquals(new String[]{"Value"}, attributes.vals);
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        Assert.assertEquals(0, attributes.size());
        Assert.assertArrayEquals(Empty, attributes.keys);
        Assert.assertArrayEquals(Empty, attributes.vals);
    }

    @Test
    public void testRemoveIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key", "Value");
        attributes.remove("key");
        Assert.assertEquals(0, attributes.size());
        Assert.assertArrayEquals(Empty, attributes.keys);
        Assert.assertArrayEquals(Empty, attributes.vals);
    }

    @Test
    public void testContainsAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
        Assert.assertFalse(attributes.hasKey("other"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("other"));
    }

    @Test
    public void testContainsIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("Key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
        Assert.assertFalse(attributes.hasKey("other"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("other"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        Assert.assertArrayEquals(new String[]{"key"}, attributes.keys);
        Assert.assertArrayEquals(new String[0], attributes.vals);
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
        Assert.assertFalse(attributes.hasKey("other"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("other"));
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testPutBooleanAttributeRemoveOnFalse() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        attributes.put("key", false);
        Assert.assertArrayEquals(new String[0], attributes.keys);
        Assert.assertArrayEquals(new String[0], attributes.vals);
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testGetAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertEquals("value", attributes.get("key"));
        Assert.assertEquals("value", attributes.getIgnoreCase("key"));
        Assert.assertEquals(EmptyString, attributes.get("other"));
        Assert.assertEquals(EmptyString, attributes.getIgnoreCase("other"));
        Assert.assertNull(attributes.get("key"));
        Assert.assertNull(attributes.getIgnoreCase("KEY"));
    }

    @Test
    public void testPutAttributes() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        Attributes attributes3 = new Attributes();
        attributes3.putAll(attributes1);
        attributes3.putAll(attributes2);
        Assert.assertEquals(2, attributes3.size());
        Assert.assertArrayEquals(new String[]{"key1", "key2"}, attributes3.keys);
        Assert.assertArrayEquals(new String[]{"value1", "value2"}, attributes3.vals);
        Assert.assertTrue(attributes3.hasKey("key1"));
        Assert.assertTrue(attributes3.hasKey("key2"));
        Assert.assertTrue(attributes3.hasKeyIgnoreCase("key1"));
        Assert.assertTrue(attributes3.hasKeyIgnoreCase("key2"));
        Assert.assertFalse(attributes3.hasKey("other"));
        Assert.assertFalse(attributes3.hasKeyIgnoreCase("other"));
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullAttributes() {
        Attributes attributes = new Attributes();
        attributes.putAll(null);
    }

    @Test
    public void testCloneAttributes() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = attributes1.clone();
        Assert.assertEquals(1, attributes2.size());
        Assert.assertArrayEquals(new String[]{"key1"}, attributes2.keys);
        Assert.assertArrayEquals(new String[]{"value1"}, attributes2.vals);
        Assert.assertTrue(attributes2.hasKey("key1"));
        Assert.assertTrue(attributes2.hasKeyIgnoreCase("key1"));
        Assert.assertFalse(attributes2.hasKey("other"));
        Assert.assertFalse(attributes2.hasKeyIgnoreCase("other"));
        Attributes attributes3 = new Attributes();
        attributes3.put("key2", "value2");
        attributes3.putAll(attributes1);
        Attributes attributes4 = attributes3.clone();
        Assert.assertEquals(2, attributes4.size());
        Assert.assertArrayEquals(new String[]{"key1", "key2"}, attributes4.keys);
        Assert.assertArrayEquals(new String[]{"value1", "value2"}, attributes4.vals);
        Assert.assertTrue(attributes4.hasKey("key1"));
        Assert.assertTrue(attributes4.hasKey("key2"));
        Assert.assertTrue(attributes4.hasKeyIgnoreCase("key1"));
        Assert.assertTrue(attributes4.hasKeyIgnoreCase("key2"));
        Assert.assertFalse(attributes4.hasKey("other"));
        Assert.assertFalse(attributes4.hasKeyIgnoreCase("other"));
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        String result = attributes.toString();
        Assert.assertTrue(result.matches(".*\\s+key1=\\s+[\"']value1[\\]\\s+.*\\s+key2=\\s+[\"']value2[\\].*"));
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("KEY1", "value1");
        attributes.normalize();
        Assert.assertEquals(1, attributes.size());
        Assert.assertArrayEquals(new String[]{"key1"}, attributes.keys);
        Assert.assertArrayEquals(new String[]{"value1"}, attributes.vals);
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
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        StringBuilder sb = new StringBuilder();
        attributes.html(sb, null);
        String result = sb.toString();
        Assert.assertTrue(result.matches(".*\\s+key1=\\s+[\"']value1[\\]"));
    }
}