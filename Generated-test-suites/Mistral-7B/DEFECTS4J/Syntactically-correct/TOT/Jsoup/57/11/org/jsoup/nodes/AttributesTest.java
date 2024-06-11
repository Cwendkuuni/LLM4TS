package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.AttributesImpl;

public class AttributesTest {

    @Test
    public void testGetAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String expected = "value";
        String actual = attributes.get("key");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        String expected = "Value";
        String actual = attributes.getIgnoreCase("key");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean expected = true;
        boolean actual = attributes.hasKey("key");
        Assert.assertEquals(expected, actual);

        Attributes attributesNoKey = new Attributes();
        boolean actualNoKey = attributes.hasKey("key");
        Assert.assertEquals(expected, !actualNoKey);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        boolean expected = true;
        boolean actual = attributes.hasKeyIgnoreCase("key");
        Assert.assertEquals(expected, actual);

        Attributes attributesNoKey = new Attributes();
        boolean actualNoKey = attributes.hasKeyIgnoreCase("key");
        Assert.assertEquals(expected, !actualNoKey);
    }

    @Test
    public void testPutGetAttribute() {
        Attributes attributes = new Attributes();
        String key = "key";
        String value1 = "value1";
        String value2 = "value2";

        // Put an attribute
        attributes.put(key, value1);
        String expectedValue1 = value1;
        String actualValue1 = attributes.get(key);
        Assert.assertEquals(expectedValue1, actualValue1);

        // Put the same attribute again
        attributes.put(key, value2);
        String expectedValue2 = value2;
        String actualValue2 = attributes.get(key);
        Assert.assertEquals(expectedValue2, actualValue2);

        // Get the attribute value
        String actualValue = attributes.get(key);
        Assert.assertEquals(expectedValue2, actualValue);
    }

    @Test
    public void testPutGetBooleanAttribute() {
        Attributes attributes = new Attributes();
        String key = "key";

        // Put a boolean attribute
        attributes.put(key, true);
        boolean expected = true;
        boolean actual = attributes.hasKey(key);
        Assert.assertEquals(expected, actual);

        // Get the boolean attribute value
        boolean actualValue = attributes.getBoolean(key);
        Assert.assertEquals(expected, actualValue);

        // Put a false boolean attribute
        attributes.put(key, false);
        expected = false;
        actual = attributes.hasKey(key);
        Assert.assertEquals(expected, actual);

        // Get the boolean attribute value
        actualValue = attributes.getBoolean(key);
        Assert.assertEquals(expected, !actualValue);
    }

    @Test
    public void testPutGetAttributeWithSameKey() {
        Attributes attributes = new Attributes();
        String key = "key";
        String value1 = "value1";
        String value2 = "value2";

        // Put an attribute
        attributes.put(key, value1);
        String expectedValue1 = value1;
        String actualValue1 = attributes.get(key);
        Assert.assertEquals(expectedValue1, actualValue1);

        // Put the same key with a different value
        attributes.put(key, value2);
        String expectedValue2 = value2;
        String actualValue2 = attributes.get(key);
        Assert.assertEquals(expectedValue2, actualValue2);

        // Get the attribute value
        String actualValue = attributes.get(key);
        Assert.assertEquals(expectedValue2, actualValue);
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        String key = "key";
        String value = "value";

        // Put an attribute
        attributes.put(key, value);
        boolean hasKey = attributes.hasKey(key);
        Assert.assertTrue(hasKey);

        // Remove the attribute
        attributes.remove(key);
        hasKey = attributes.hasKey(key);
        Assert.assertFalse(hasKey);
    }

    @Test
    public void testRemoveIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        String key = "Key";
        String value = "value";

        // Put an attribute
        attributes.put(key, value);
        boolean hasKey = attributes.hasKeyIgnoreCase("key");
        Assert.assertTrue(hasKey);

        // Remove the attribute ignoring the case
        attributes.removeIgnoreCase("key");
        hasKey = attributes.hasKeyIgnoreCase("key");
        Assert.assertFalse(hasKey);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKeyAttribute() {
        Attributes attributes = new Attributes();
        String nullKey = null;
        String value = "value";

        // Try to put a null key attribute
        attributes.put(nullKey, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullValueAttribute() {
        Attributes attributes = new Attributes();
        String key = "key";
        String nullValue = null;

        // Try to put a null value attribute
        attributes.put(key, nullValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutEmptyStringKeyAttribute() {
        Attributes attributes = new Attributes();
        String emptyStringKey = "";
        String value = "value";

        // Try to put an empty string key attribute
        attributes.put(emptyStringKey, value);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        int expectedSize = 0;
        int actualSize = attributes.size();
        Assert.assertEquals(expectedSize, actualSize);

        // Add some attributes
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        expectedSize = 3;
        actualSize = attributes.size();
        Assert.assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testAddAllAttributes() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes expectedAttributes = new Attributes();
        Attributes actualAttributes = new Attributes();

        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        attributes2.put("key2", "value3");
        attributes2.put("key3", "value3");

        expectedAttributes.addAll(attributes2);
        expectedAttributes.put("key1", "value1");
        expectedAttributes.put("key2", "value3");
        expectedAttributes.put("key3", "value3");

        attributes1.addAll(attributes2);
        actualAttributes = attributes1;

        Assert.assertEquals(expectedAttributes.size(), actualAttributes.size());
        for (Map.Entry<String, String> entry : expectedAttributes.entrySet()) {
            String key = entry.getKey();
            String expectedValue = entry.getValue();
            String actualValue = attributes1.get(key);
            Assert.assertEquals(expectedValue, actualValue);
        }
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
        attributes.put("key2", "value2");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key1=\"value1\" key2=\"value2\"");

        StringBuilder actualHtml = new StringBuilder();
        attributes.html(actualHtml, new Document.OutputSettings());

        Assert.assertEquals(expectedHtml.toString(), actualHtml.toString());
    }
}