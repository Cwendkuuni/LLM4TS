package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AttributesTest {

    @Test
    public void testEmptyAttributes() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
        Assert.assertNull(attributes.get("key"));
        Assert.assertNull(attributes.getIgnoreCase("key"));
        Assert.assertFalse(attributes.hasKey("data-key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("data-key"));
        Assert.assertNull(attributes.get("data-key"));
        Assert.assertNull(attributes.getIgnoreCase("data-key"));
        Assert.assertEquals(Attributes.Empty, attributes.asList().toArray());
    }

    @Test
    public void testSettingGettingAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
        Assert.assertEquals("value", attributes.get("key"));
        Assert.assertEquals("value", attributes.getIgnoreCase("key"));
        Assert.assertNull(attributes.get("otherKey"));
        Assert.assertNull(attributes.getIgnoreCase("otherKey"));
        Assert.assertEquals(Arrays.asList(new Attribute("key", "value", attributes)), attributes.asList());
    }

    @Test
    public void testSettingOverwritingAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertEquals("value2", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.getIgnoreCase("key1"));
    }

    @Test
    public void testSettingBooleanAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("booleanKey"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("booleanKey"));
        Assert.assertNull(attributes.get("booleanKey"));
        Assert.assertEquals(EmptyString, attributes.getIgnoreCase("booleanKey"));
        Assert.assertFalse(attributes.getBoolean("booleanKey"));
        Assert.assertTrue(attributes.hasKey("booleanKey") && attributes.getBoolean("booleanKey"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("booleanKey") && attributes.getBoolean("booleanKey"));
        Assert.assertTrue(attributes.getBoolean("booleanKey") != attributes.getBoolean("booleanKey"));
        attributes.put("booleanKey", false);
        Assert.assertFalse(attributes.getBoolean("booleanKey"));
        Assert.assertFalse(attributes.hasKey("booleanKey") && attributes.getBoolean("booleanKey"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("booleanKey") && attributes.getBoolean("booleanKey"));
    }

    @Test
    public void testSettingMultipleAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value1", attributes.getIgnoreCase("key1"));
        Assert.assertTrue(attributes.hasKey("key2"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key2"));
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertEquals("value2", attributes.getIgnoreCase("key2"));
        Assert.assertEquals(Arrays.asList(new Attribute("key1", "value1", attributes),
                new Attribute("key2", "value2", attributes)), attributes.asList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingEmptyKey() {
        Attributes attributes = new Attributes();
        attributes.put(EmptyString, "value");
    }

    @Test
    public void testGettingNonExistentAttributes() {
        Attributes attributes = new Attributes();
        Assert.assertNull(attributes.get("nonExistentKey"));
        Assert.assertEquals(EmptyString, attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testGettingDataAttributes() {
        Attributes attributes = new Attributes();        attributes.put("data-key", "value");
        Assert.assertTrue(attributes.hasKey("data-key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("data-key"));
        Assert.assertEquals("value", attributes.get("data-key"));
        Assert.assertEquals("value", attributes.getIgnoreCase("data-key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("dataKey"));
        Assert.assertEquals("value", attributes.getIgnoreCase("dataKey"));
        Assert.assertEquals(Arrays.asList(new Attribute("data-key", "value", attributes)), attributes.asList());
    }

    @Test
    public void testRemovingAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Assert.assertEquals(1, attributes.size());
        attributes.remove("key1");
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("key1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertNull(attributes.get("key1"));
        Assert.assertEquals(EmptyString, attributes.getIgnoreCase("key1"));
    }

    @Test
    public void testRemovingNonExistentAttributes() {
        Attributes attributes = new Attributes();
        attributes.remove("nonExistentKey");
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("nonExistentKey"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
        Assert.assertNull(attributes.get("nonExistentKey"));
        Assert.assertEquals(EmptyString, attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testIteratingAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Iterator<Attribute> iterator = attributes.iterator();
        List<Attribute> expected = Arrays.asList(new Attribute("key1", "value1", attributes),
                new Attribute("key2", "value2", attributes));
        for (Attribute attribute : expected) {
            Assert.assertEquals(attribute, iterator.next());
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testCloningAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes cloned = attributes.clone();
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals(1, cloned.size());
        Assert.assertEquals(attributes.get("key1"), cloned.get("key1"));
        Assert.assertEquals(attributes.getIgnoreCase("key1"), cloned.getIgnoreCase("key1"));
    }

    @Test
    public void testNormalizingAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("KEY1", "value1");
        attributes.normalize();
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value1", attributes.getIgnoreCase("key1"));
    }
}