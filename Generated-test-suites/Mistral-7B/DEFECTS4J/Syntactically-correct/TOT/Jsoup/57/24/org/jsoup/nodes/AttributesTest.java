package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");

        String actual = attributes.getIgnoreCase("key");
        String expected = "value";
        assertEquals(expected, actual);

        String actualIgnoreCase = attributes.getIgnoreCase("KEY");
        assertEquals(expected, actualIgnoreCase);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);

        boolean actual = attributes.hasKey("key");
        assertTrue(actual);

        actual = attributes.hasKeyIgnoreCase("Key");
        assertTrue(actual);

        actual = attributes.get("key").equals("true");
        assertTrue(actual);

        attributes.put("key", false);
        actual = attributes.hasKey("key");
        assertFalse(actual);
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute newAttribute = new Attribute("key", "value");
        attributes.put(newAttribute);

        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        boolean actual = attributes.hasKey("key");
        assertTrue(actual);

        attributes.remove("key");

        actual = attributes.hasKey("key");
        assertFalse(actual);
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");

        boolean actual = attributes.hasKeyIgnoreCase("key");
        assertTrue(actual);

        attributes.removeIgnoreCase("Key");

        actual = attributes.hasKeyIgnoreCase("key");
        assertFalse(actual);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        boolean actual = attributes.hasKey("key");
        assertTrue(actual);

        actual = attributes.hasKeyIgnoreCase("Key");
        assertTrue(actual);

        actual = attributes.hasKey("otherKey");
        assertFalse(actual);
    }

    @Test
    public void testHasKeyIgnoreCaseWithEmptyAttributes() {
        Attributes attributes = new Attributes();

        boolean actual = attributes.hasKeyIgnoreCase("key");
        assertFalse(actual);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int actual = attributes.size();
        int expected = 2;
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithDuplicateKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value3");
        attributes2.put("key3", "value3");

        attributes1.addAll(attributes2);

        int actualSize = attributes1.size();
        int expectedSize = 3;
        assertEquals(expectedSize, actualSize);

        String actual1Value = attributes1.get("key1");
        String expected1Value = "value1";
        assertEquals(expected1Value, actual1Value);

        String actual2Value = attributes1.get("key2");
        String expected2Value = "value3";
        assertEquals(expected2Value, actual2Value);

        String actual3Value = attributes1.get("key3");
        String expected3Value = "value3";
        assertEquals(expected3Value, actual3Value);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        int datasetSize = dataset.size();
        int expectedDatasetSize = 2;
        assertEquals(expectedDatasetSize, datasetSize);

        String actual1Value = dataset.get("data-key1");
        String expected1Value = "value1";
        assertEquals(expected1Value, actual1Value);

        String actual2Value = dataset.get("data-key2");
        String expected2Value = "value2";
        assertEquals(expected2Value, actual2Value);
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        StringBuilder expected = new StringBuilder();
        expected.append(" key=\"value\"");

        StringBuilder actual = new StringBuilder();
        try {
            attributes.html(actual, new Document.OutputSettings());
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(expected.toString(), actual.toString());
    }
}