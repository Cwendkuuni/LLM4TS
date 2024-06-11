package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
        attributes.put("Key", "Value");
        String actual = attributes.getIgnoreCase("key");
        String expected = "Value";
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
    public void testPutAttributeIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "Value");
        String actual = attributes.getIgnoreCase("key");
        String expected = "Value";
        Assert.assertEquals(expected, actual);
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

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        String actual = attributes.get("booleanKey");
        Assert.assertEquals(Attributes.EmptyString, actual);
        boolean hasBooleanKey = attributes.hasKey("booleanKey");
        Assert.assertTrue(hasBooleanKey);
    }

    @Test
    public void testPutBooleanAttributeRemove() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        attributes.remove("booleanKey");
        boolean hasBooleanKey = attributes.hasKey("booleanKey");
        Assert.assertFalse(hasBooleanKey);
    }

    @Test
    public void testPutAttributeWithInitialCapacity() {
        Attributes attributes = new Attributes(5);
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");
        attributes.put("key4", "value4");
        attributes.put("key5", "value5");
        int actualSize = attributes.size();
        int expectedSize = 5;
        Assert.assertEquals(expectedSize, actualSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithSizeOverflow() {
        Attributes attributes = new Attributes();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            attributes.put("key", "value");
        }
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean actual = attributes.hasKey("key");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "value");
        boolean actual = attributes.hasKeyIgnoreCase("key");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyNotFound() {
        Attributes attributes = new Attributes();
        boolean actual = attributes.hasKey("notFound");
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        boolean actualHasKey = attributes.hasKey("key");
        boolean expectedHasKey = false;
        Assert.assertEquals(expectedHasKey, actualHasKey);
    }

    @Test
    public void testRemoveAttributeIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "value");
        attributes.remove("key");
        boolean actualHasKey = attributes.hasKeyIgnoreCase("key");
        boolean expectedHasKey = false;
        Assert.assertEquals(expectedHasKey, actualHasKey);
    }

    @Test
    public void testRemoveAttributeNotExist() {
        Attributes attributes = new Attributes();
        boolean actualHasKey = attributes.hasKey("notFound");
        boolean expectedHasKey = false;
        attributes.remove("notFound");
        Assert.assertEquals(expectedHasKey, actualHasKey);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        int actualSize = attributes.size();
        int expectedSize = 0;
        Assert.assertEquals(expectedSize, actualSize);

        attributes.put("key1", "value1");
        actualSize = attributes.size();
        expectedSize = 1;
        Assert.assertEquals(expectedSize, actualSize);

        attributes.put("key2", "value2");
        actualSize = attributes.size();
        expectedSize = 2;
        Assert.assertEquals(expectedSize, actualSize);
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

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("key1", "value1");
        expectedMap.put("key2", "value2");

        for (Map.Entry<String, String> entry : expectedMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String actualKey = result.get(key);
            String actualValue = result.getIgnoreCase(key);

            Assert.assertEquals(key, actualKey);
            Assert.assertEquals(value, actualValue);
        }
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> expectedList = Arrays.asList(
                new Attribute("key1", "value1"),
                new Attribute("key2", "value2")
        );

        List<Attribute> actualList = attributes.asList();

        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("data-key1", "value1");
        expectedMap.put("data-key2", "value2");

        Map<String, String> actualMap = attributes.dataset();

        Assert.assertEquals(expectedMap, actualMap);
    }
}