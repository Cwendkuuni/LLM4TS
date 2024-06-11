package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.jsoup.nodes.Attributes.dataPrefix;
import static org.jsoup.nodes.Attributes.InitialCapacity;
import static org.jsoup.nodes.Attributes.GrowthFactor;

public class AttributesTest {

    @Test
    public void testInitialization() {
        Attributes attributes = new Attributes();
        Assert.assertArrayEquals(Attributes.EMPTY, attributes.keys);
        Assert.assertArrayEquals(Attributes.EMPTY, attributes.vals);
        Assert.assertEquals(0, attributes.size);
    }

    @Test
    public void testCapacity() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        attributes.checkCapacity(7);
        Assert.assertEquals(6, attributes.size);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCapacity() {
        Attributes attributes = new Attributes();
        attributes.checkCapacity(-1);
    }

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals(EmptyString, attributes.get("key2"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");

        Assert.assertEquals("Value1", attributes.getIgnoreCase("key1"));
        Assert.assertEquals(EmptyString, attributes.getIgnoreCase("Key2"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertEquals(2, attributes.size());
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testPutWithSameKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");

        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testPutWithSameKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");
        attributes.put("key1", "Value2");

        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("Value2", attributes.get("key1"));
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);

        Assert.assertEquals(EmptyString, attributes.get("key1"));
        Assert.assertTrue(attributes.hasKey("key1"));
    }

    @Test
    public void testPutBooleanAttributeWithSameKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        attributes.put("key1", false);

        Assert.assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testPutIgnoringCaseWithSameKey() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", true);
        attributes.put("key1", false);

        Assert.assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testPutAndGetDatatypeAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");

        Assert.assertEquals("value1", attributes.get("data-key1"));
        Assert.assertEquals("value1", attributes.getIgnoreCase("data-key1"));
    }

    @Test
    public void testPutAndGetDatatypeAttributeWithSameKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("data-KEY1", "value1");

        Assert.assertEquals("value1", attributes.get("data-key1"));
        Assert.assertEquals("value1", attributes.getIgnoreCase("data-KEY1"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testRemoveIgnoringCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");
        attributes.put("key2", "value2");

        attributes.remove("Key1");
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");

        Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("Key2"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);

        Assert.assertEquals(2, attributes3.size());
        Assert.assertEquals("value1", attributes3.get("key1"));
        Assert.assertEquals("value2", attributes3.get("key2"));
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clonedAttributes = attributes.clone();

        Assert.assertEquals(2, clonedAttributes.size());
        Assert.assertEquals("value1", clonedAttributes.get("key1"));
        Assert.assertEquals("value2", clonedAttributes.get("key2"));

        Assert.assertNotSame(attributes, clonedAttributes);
    }
}