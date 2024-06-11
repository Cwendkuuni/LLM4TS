package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RunWith(JUnit4.class)
public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String got = attributes.get("key");
        Assert.assertEquals("value", got);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String got = attributes.getIgnoreCase("Key");
        Assert.assertEquals("value", got);
    }

    @Test
    public void testGetWhenNull() {
        Attributes attributes = new Attributes();
        String got = attributes.get("key");
        Assert.assertEquals("", got);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String got = attributes.get("key");
        Assert.assertEquals("value", got);
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        boolean got = attributes.hasKey("key");
        Assert.assertTrue(got);
        got = attributes.hasKeyIgnoreCase("key");
        Assert.assertTrue(got);
        got = attributes.get("key").equals(BooleanAttribute.TRUE);
        Assert.assertTrue(got);
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        String key = "key";
        String value = "value";
        attributes.put(new Attribute(key, value));
        String got = attributes.get(key);
        Assert.assertEquals(value, got);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        String got = attributes.get("key");
        Assert.assertEquals("", got);
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        attributes.removeIgnoreCase("key");
        String got = attributes.get("Key");
        Assert.assertEquals("value", got);
        got = attributes.get("key");
        Assert.assertEquals("", got);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        boolean got = attributes.hasKey("key");
        Assert.assertFalse(got);
        attributes.put("key", "value");
        got = attributes.hasKey("key");
        Assert.assertTrue(got);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        boolean got = attributes.hasKeyIgnoreCase("key");
        Assert.assertFalse(got);
        attributes.put("Key", "value");
        got = attributes.hasKeyIgnoreCase("key");
        Assert.assertTrue(got);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        int got = attributes.size();
        Assert.assertEquals(0, got);
        attributes.put("key", "value");
        got = attributes.size();
        Assert.assertEquals(1, got);
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes2.put("key3", "value3");

        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);

        List<String> keys = Arrays.asList("key1", "key2", "key3");
        int size = keys.size();
        for (int i = 0; i < size; i++) {
            String key = keys.get(i);
            String got = attributes3.get(key);
            String expected = (i < size / 2) ? "value1" : "value2";
            Assert.assertEquals(expected, got);
        }
    }

    @Test
    public void testIterable() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> expectedList = Arrays.asList(
                new Attribute("key1", "value1"),
                new Attribute("key2", "value2")
        );

        List<Attribute> actualList = new ArrayList<>(attributes);
        Assert.assertEquals(expectedList, actualList);
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

        Map<String, String> expectedMap = new LinkedHashMap<>();
        expectedMap.put("key1", "value1");
        expectedMap.put("key2", "value2");

        Map<String, String> actualMap = attributes.dataset();
        Assert.assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key1=\"value1\"");

        StringBuilder actualHtml = new StringBuilder();
        attributes.html(actualHtml, null);
        Assert.assertEquals(expectedHtml.toString(), actualHtml.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");

        Attributes attributes3 = new Attributes();
        attributes3.put("key2", "value2");

        Attributes attributes4 = attributes1;

        Assert.assertEquals(attributes1, attributes1);
        Assert.assertEquals(attributes1, attributes2);
        Assert.assertNotEquals(attributes1, attributes3);
        Assert.assertNotEquals(attributes1, attributes4);
        Assert.assertNotEquals(attributes1, null);
        Assert.assertNotEquals(attributes1, new Object());

        Attributes attributes5 = new Attributes();
        attributes5.addAll(attributes1);
        Assert.assertEquals(attributes1, attributes5);
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");

        Attributes attributes3 = new Attributes();
        attributes3.put("key2", "value2");

        int hashcode1 = attributes1.hashCode();
        Assert.assertEquals(hashcode1, attributes1.hashCode());
        Assert.assertEquals(hashcode1, attributes2.hashCode());
        Assert.assertNotEquals(hashcode1, attributes3.hashCode());

        Attributes attributes4 = new Attributes();
        attributes4.addAll(attributes1);
        Assert.assertEquals(hashcode1, attributes4.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        Attributes clonedAttributes = attributes.clone();
        Assert.assertEquals(attributes, clonedAttributes);
    }
}