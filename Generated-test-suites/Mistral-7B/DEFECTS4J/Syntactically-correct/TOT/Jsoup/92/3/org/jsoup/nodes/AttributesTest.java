package org.jsoup.nodes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AttributesTest {

    @Test
    public void testEmptyAttributes() {
        Attributes attributes = new Attributes();
        Assertions.assertEquals(0, attributes.size());
        Assertions.assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testSettingAndGettingAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("test", "hello");
        Assertions.assertEquals("hello", attributes.get("test"));
        Assertions.assertTrue(attributes.hasKey("test"));
        Assertions.assertFalse(attributes.hasKeyIgnoreCase("Test"));
    }

    @Test
    public void testSettingAndGettingBooleanAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("test", false);
        Assertions.assertFalse(attributes.get("test").isEmpty());
        Assertions.assertFalse(attributes.get("test"));
        Assertions.assertTrue(attributes.hasKey("test"));
        attributes.put("test", true);
        Assertions.assertNull(attributes.get("test"));
        Assertions.assertTrue(attributes.hasKey("test"));
    }

    @Test
    public void testSettingAndGettingCaseInsensitiveAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("test", "hello");
        Assertions.assertEquals("hello", attributes.getIgnoreCase("test"));
        Assertions.assertEquals("hello", attributes.getIgnoreCase("TeSt"));
        Assertions.assertTrue(attributes.hasKeyIgnoreCase("test"));
        Assertions.assertTrue(attributes.hasKeyIgnoreCase("TeSt"));
    }

    @Test
    public void testSettingAndGettingDuplicateAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("test", "hello");
        attributes.put("test", "world");
        Assertions.assertEquals("world", attributes.get("test"));
        Assertions.assertFalse(attributes.get("test").isEmpty());
        Assertions.assertTrue(attributes.hasKey("test"));
        Assertions.assertFalse(attributes.hasKeyIgnoreCase("Test"));
    }

    @Test
    public void testSettingAndGettingAttributesWithPrefix() {
        Attributes attributes = new Attributes();
        attributes.put("data:test", "hello");
        Assertions.assertEquals("hello", attributes.get("data:test"));
        Assertions.assertTrue(attributes.hasKey("data:test"));
        Assertions.assertTrue(attributes.hasKeyIgnoreCase("Data:test"));
        Assertions.assertFalse(attributes.hasKey("test"));
        Assertions.assertFalse(attributes.hasKeyIgnoreCase("Test"));
    }

    @Test
    public void testRemovingAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("test", "hello");
        Assertions.assertEquals("hello", attributes.get("test"));
        Assertions.assertTrue(attributes.hasKey("test"));
        attributes.remove("test");
        Assertions.assertNull(attributes.get("test"));
        Assertions.assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testRemovingCaseInsensitiveAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("test", "hello");
        Assertions.assertEquals("hello", attributes.getIgnoreCase("test"));
        Assertions.assertEquals("hello", attributes.getIgnoreCase("TeSt"));
        Assertions.assertTrue(attributes.hasKeyIgnoreCase("test"));
        Assertions.assertTrue(attributes.hasKeyIgnoreCase("TeSt"));
        attributes.removeIgnoreCase("test");
        Assertions.assertNull(attributes.get("test"));
        Assertions.assertFalse(attributes.hasKey("test"));
        Assertions.assertFalse(attributes.hasKeyIgnoreCase("test"));
    }

    @Test
    public void testAddingMultipleAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "hello1");
        attributes.put("test2", "hello2");
        List<Map.Entry<String, String>> entries = Arrays.asList(
                new AbstractMap.SimpleEntry<>("test1", "hello1"),
                new AbstractMap.SimpleEntry<>("test2", "hello2")
        );
        Attributes expected = new Attributes();
        expected.addAll(entries);
        Assertions.assertEquals(expected.asList(), attributes.asList());
    }
}