package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class AttributesTest {

    @Test
    public void testBasicGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
    }

    @Test
    public void testBasicPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
    }

    @Test
    public void testBasicPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        String actual = attributes.getIgnoreCase("key");
        String expected = "Value";
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testPutOverwrites() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        String actual = attributes.get("key");
        String expected = "value2";
        assertEquals(expected, actual);
    }

    @Test
    public void testPutIgnoreCaseOverwrites() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value1");
        attributes.put("key", "Value2");
        String actual = attributes.getIgnoreCase("key");
        String expected = "Value2";
        assertEquals(expected, actual);
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

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("key1");
        String actual = attributes.get("key1");
        String expected = "";
        assertEquals(expected, actual);
        String actual2 = attributes.get("key2");
        String expected2 = "value2";
        assertEquals(expected2, actual2);
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");
        attributes.put("key2", "value2");
        attributes.removeIgnoreCase("Key1");
        String actual = attributes.getIgnoreCase("Key1");
        String expected = "";
        assertEquals(expected, actual);
        String actual2 = attributes.getIgnoreCase("key2");
        String expected2 = "value2";
        assertEquals(expected2, actual2);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean actual = attributes.hasKey("key");
        boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        boolean actual = attributes.hasKeyIgnoreCase("key");
        boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyFalse() {
        Attributes attributes = new Attributes();
        boolean actual = attributes.hasKey("nonexistentkey");
        boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyIgnoreCaseFalse() {
        Attributes attributes = new Attributes();
        boolean actual = attributes.hasKeyIgnoreCase("nonexistentkey");
        boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveAll() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.addAll(new Attributes());
        attributes.remove("key1");
        attributes.remove("key2");
        int actual = attributes.size();
        int expected = 0;
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        String actual = attributes.toString();
        String expected = "Attributes{size=2, keys=[key1, key2], vals=[value1, value2]}";
        assertEquals(expected, actual);
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        StringBuilder sb = new StringBuilder();
        try {
            attributes.html(sb, null);
            String actual = sb.toString();
            String expected = " attributes=\"key='value'\"";
            assertEquals(expected, actual.substring(0, actual.length() - 1));
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
}