package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testGetAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAttributeNullKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get(null);
    }

    @Test
    public void testGetAttributeIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.getIgnoreCase("key");
        String expected = "value";
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAttributeIgnoreCaseNullKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.getIgnoreCase(null);
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        String key1 = "key1";
        String value1 = "value1";
        String key2 = "key2";
        String value2 = "value2";

        attributes.put(key1, value1);
        attributes.put(key2, value2);

        String actual1 = attributes.get(key1);
        String expected1 = value1;
        assertEquals(expected1, actual1);

        String actual2 = attributes.get(key2);
        String expected2 = value2;
        assertEquals(expected2, actual2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutAttributeNullKey() {
        Attributes attributes = new Attributes();
        String value = "value";
        attributes.put(null, value);
    }

    @Test
    public void testPutAttributeNullValue() {
        Attributes attributes = new Attributes();
        String key = "key";
        attributes.put(key, null);
        String actual = attributes.get(key);
        assertEquals("", actual);
    }

    @Test
    public void testPutAttributeSameKey() {
        Attributes attributes = new Attributes();
        String key = "key";
        String value1 = "value1";
        String value2 = "value2";

        attributes.put(key, value1);
        attributes.put(key, value2);

        String actual = attributes.get(key);
        String expected = value2; // last value overwrites the previous one
        assertEquals(expected, actual);
    }

    @Test
    public void testPutIgnoreCaseSameKey() {
        Attributes attributes = new Attributes();
        String key1 = "key1";
        String value1 = "value1";
        String key2 = "Key1";
        String value2 = "value2";

        attributes.put(key1, value1);
        attributes.put(key2, value2);

        String actual = attributes.get(key1);
        String expected = value2; // case insensitive comparison, last value overwrites the previous one
        assertEquals(expected, actual);
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        String key = "key";
        boolean value = true;

        attributes.put(key, value);
        boolean actual = attributes.getBoolean(key);
        assertTrue(actual);

        boolean expected = false;
        attributes.put(key, expected);
        actual = attributes.getBoolean(key);
        assertFalse(actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPutBooleanAttributeNullKey() {
        Attributes attributes = new Attributes();
        boolean value = true;
        attributes.put(null, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutBooleanAttributeInvalidValue() {
        Attributes attributes = new Attributes();
        String key = "key";
        int value = 1;
        attributes.put(key, value);
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        String key = "key";
        String value = "value";

        attributes.put(key, value);
        attributes.remove(key);
        String actual = attributes.get(key);
        assertEquals("", actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveAttributeNullKey() {
        Attributes attributes = new Attributes();
        attributes.remove(null);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        String key = "key";
        boolean expected = false;

        assertFalse(attributes.hasKey(key));

        attributes.put(key, "value");
        expected = true;

        assertTrue(attributes.hasKey(key));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        String key1 = "key1";
        String key2 = "Key1";

        attributes.put(key1, "value1");

        assertTrue(attributes.hasKeyIgnoreCase(key1));
        assertTrue(attributes.hasKeyIgnoreCase(key2));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        int expected = 0;

        assertEquals(expected, attributes.size());

        attributes.put("key1", "value1");
        expected = 1;

        assertEquals(expected, attributes.size());

        attributes.put("key2", "value2");
        expected = 2;

        assertEquals(expected, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        String key = "key";
        String value = "value";

        attributes1.put(key, value);

        attributes2.put("key2", "value2");
        attributes2.put("key3", "value3");

        attributes1.addAll(attributes2);

        int expectedSize = 3;
        assertEquals(expectedSize, attributes1.size());

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("key", value);
        expectedMap.put("key2", "value2");
        expectedMap.put("key3", "value3");

        Map<String, String> actualMap = attributes1.asMap();

        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        String key1 = "key1";
        String value1 = "value1";
        String key2 = "key2";
        String value2 = "value2";

        attributes.put(key1, value1);
        attributes.put(key2, value2);

        List<Attribute> expectedList = Arrays.asList(
                new Attribute(key1, value1, attributes),
                new Attribute(key2, value2, attributes)
        );

        List<Attribute> actualList = attributes.asList();

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        String key1 = "data-key1";
        String value1 = "value1";
        String key2 = "data-key2";
        String value2 = "value2";

        attributes.put(key1, value1);
        attributes.put(key2, value2);

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put(key1, value1);
        expectedMap.put(key2, value2);

        Map<String, String> actualMap = attributes.dataset();

        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        String key1 = "key1";
        String value1 = "value1";
        String key2 = "key2";
        String value2 = "value2";

        attributes.put(key1, value1);
        attributes.put(key2, value2);

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append("<attribues key1=\"value1\" key2=\"value2\"/>");

        StringBuilder actualHtml = new StringBuilder();
        attributes.html(actualHtml, null);

        assertEquals(expectedHtml.toString(), actualHtml.toString());
    }
}