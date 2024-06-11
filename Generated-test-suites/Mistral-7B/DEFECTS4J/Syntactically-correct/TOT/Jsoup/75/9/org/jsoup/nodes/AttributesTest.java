package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import static org.jsoup.nodes.Attributes.dataPrefix;

public class AttributesTest {

    @Test
    public void testGetAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        String actual = attributes.getIgnoreCase("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetAttributeNotFound() {
        Attributes attributes = new Attributes();
        String actual = attributes.get("notFound");
        Assert.assertEquals(Attributes.EmptyString, actual);
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "value");
        String actual = attributes.getIgnoreCase("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        boolean actual = attributes.getBoolean("booleanKey");
        Assert.assertTrue(actual);
    }

    @Test
    public void testPutBooleanAttributeRemove() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        attributes.remove("booleanKey");
        boolean actual = attributes.getBoolean("booleanKey");
        Assert.assertFalse(actual);
    }

    @Test
    public void testPutAttributeOverwrite() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        String actual = attributes.get("key");
        String expected = "value2";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullAttributeKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullAttributeValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean actual = attributes.hasKey("key");
        Assert.assertTrue(actual);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "value");
        boolean actual = attributes.hasKeyIgnoreCase("key");
        Assert.assertTrue(actual);
    }

    @Test
    public void testHasKeyNotFound() {
        Attributes attributes = new Attributes();
        boolean actual = attributes.hasKey("notFound");
        Assert.assertFalse(actual);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int actual = attributes.size();
        int expected = 2;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        Attributes expected = new Attributes();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        attributes1.addAll(attributes2);
        Assert.assertEquals(expected, attributes1);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        Map<String, String> actual = attributes.dataset();
        Map<String, String> expected = new HashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String expected = " key=\"value\"";
        String actual = attributes.html();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String expected = " key=\"value\"";
        String actual = attributes.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        boolean actual = attributes1.equals(attributes2);
        Assert.assertTrue(actual);
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        int actual1 = attributes1.hashCode();
        int actual2 = attributes2.hashCode();
        Assert.assertEquals(actual1, actual2);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Attributes clonedAttributes = attributes.clone();
        String actual = clonedAttributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }
}