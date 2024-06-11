package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    public void testGetAttributeIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        String actual = attributes.getIgnoreCase("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetAttributeNotFound() {
        Attributes attributes = new Attributes();
        String actual = attributes.get("key");
        Assert.assertEquals(Attributes.EmptyString, actual);
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);

        attributes.put("key", "newValue");
        actual = attributes.get("key");
        expected = "newValue";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutAttributeWithSameKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        String actual = attributes.get("key");
        String expected = "value2";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        boolean actual = attributes.get("key").equals(Attributes.EmptyString);
        boolean expected = false;
        Assert.assertFalse(actual);

        attributes.put("key", false);
        actual = attributes.get("key").equals(Attributes.EmptyString);
        expected = true;
        Assert.assertTrue(actual);

        attributes.put("key", null);
        actual = attributes.hasKey("key");
        expected = false;
        Assert.assertFalse(actual);
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "value");
        String actual = attributes.getIgnoreCase("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutIgnoreCaseWithSameKey() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "value1");
        attributes.putIgnoreCase("key", "value2");
        String actual = attributes.getIgnoreCase("key");
        String expected = "value2";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutInvalidKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.put("", "value");
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        String actual = attributes.get("key");
        Assert.assertEquals(Attributes.EmptyString, actual);
    }

    @Test
    public void testRemoveAttributeNotFound() {
        Attributes attributes = new Attributes();
        String actual = attributes.get("key");
        Assert.assertEquals(Attributes.EmptyString, actual);
        attributes.remove("key");
        actual = attributes.get("key");
        Assert.assertEquals(Attributes.EmptyString, actual);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKey("key"));

        attributes.put("key", "value");
        Assert.assertTrue(attributes.hasKey("key"));

        attributes.remove("key");
        Assert.assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));

        attributes.put("KEY", "value");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));

        attributes.remove("KEY");
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());

        attributes.remove("key1");
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key2", "value2");

        Attributes result = new Attributes();
        result.addAll(attributes1);
        result.addAll(attributes2);

        List<String> keys = Arrays.asList("key1", "key2");
        List<String> values = Arrays.asList("value1", "value2");

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = values.get(i);
            String actual = result.get(key);
            String expected = value;
            Assert.assertEquals(expected, actual);
        }
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Iterator<Map.Entry<String, String>> iterator = attributes.iterator();
        List<Map.Entry<String, String>> expected = Arrays.asList(
                new AbstractMap.SimpleEntry<>("key1", "value1"),
                new AbstractMap.SimpleEntry<>("key2", "value2")
        );

        int i = 0;
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            Map.Entry<String, String> expectedEntry = expected.get(i++);
            String key = entry.getKey();
            String value = entry.getValue();

            String expectedKey = expectedEntry.getKey();
            String expectedValue = expectedEntry.getValue();

            Assert.assertEquals(expectedKey, key);
            Assert.assertEquals(expectedValue, value);
        }
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        List<Map.Entry<String, String>> expected = Arrays.asList(
                new AbstractMap.SimpleEntry<>("data-key1", "value1"),
                new AbstractMap.SimpleEntry<>("data-key2", "value2")
        );

        for (int i = 0; i < expected.size(); i++) {
            Map.Entry<String, String> entry = expected.get(i);
            String key = entry.getKey();
            String value = entry.getValue();

            String actualKey = dataset.keySet().iterator().next();
            String actualValue = dataset.get(actualKey);

            Assert.assertEquals(key, actualKey);
            Assert.assertEquals(value, actualValue);
        }
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        StringBuilder expected = new StringBuilder();
        expected.append(" key1=\"value1\" key2=\"value2\"");

        StringBuilder actual = new StringBuilder();
        attributes.html(actual, null);

        Assert.assertEquals(expected.toString(), actual.toString());
    }
}