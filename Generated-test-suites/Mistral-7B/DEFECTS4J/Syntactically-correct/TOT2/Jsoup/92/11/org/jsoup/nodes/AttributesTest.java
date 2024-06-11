package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import static org.jsoup.nodes.Attributes.dataPrefix;

public class AttributesTest {

    @Test
    public void testGetEmptyAttributes() {
        Attributes attributes = new Attributes();
        Assert.assertEquals("", attributes.get(""));
        Assert.assertNull(attributes.get("key"));
    }

    @Test
    public void testGetAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertEquals("value", attributes.get("key"));
        Assert.assertEquals("", attributes.get(""));
    }

    @Test
    public void testGetIgnoreCaseAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        Assert.assertEquals("Value", attributes.getIgnoreCase("key"));
        Assert.assertEquals("Value", attributes.getIgnoreCase("Key"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeyGet() {
        Attributes attributes = new Attributes();
        attributes.get(null);
    }

    @Test
    public void testPutAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testPutIgnoreCaseAttributes() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key1", "value1");
        attributes.putIgnoreCase("Key2", "value2");
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("Key2"));
    }

    @Test
    public void testPutDuplicateKeyAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        Assert.assertEquals("value2", attributes.get("key"));
    }

    @Test
    public void testPutEmptyValueAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key", "");
        Assert.assertEquals("", attributes.get("key"));
    }

    @Test
    public void testPutBooleanAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key", false);
        Assert.assertFalse(attributes.hasKey("key"));
        attributes.put("key", true);
        Assert.assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testPutAttributeWithNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
        Assert.assertNull(attributes.get("key"));
    }

    @Test
    public void testPutIgnoredAttributeWithNullValue() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key", null);
        Assert.assertNull(attributes.get("key"));
    }

    @Test
    public void testRemoveKeyAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("key1");
        Assert.assertFalse(attributes.hasKey("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveInvalidKeyAttributes() {
        Attributes attributes = new Attributes();
        attributes.remove("invalidKey");
    }

    @Test
    public void testHasKeyAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKey("invalidKey"));
    }

    @Test
    public void testHasKeyIgnoreCaseAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("Key"));
    }

    @Test
    public void testSizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAllAttributes() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        attributes2.put("key3", "value3");
        attributes1.addAll(attributes2);
        Assert.assertEquals(3, attributes1.size());
        Assert.assertTrue(attributes1.hasKey("key1"));
        Assert.assertTrue(attributes1.hasKey("key2"));
        Assert.assertTrue(attributes1.hasKey("key3"));
    }

    @Test
    public void testiteratorAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> list = attributes.asList();
        Iterator<Attribute> iterator = list.iterator();
        Assert.assertEquals("value1", iterator.next().getValue());
        Assert.assertEquals("value2", iterator.next().getValue());
    }

    @Test
    public void testDatasetAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals("value1", dataset.get("key1"));
        Assert.assertEquals("value2", dataset.get("key2"));
    }

    @Test
    public void testToStringAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals("key1=\"value1\" key2=\"value2\"", attributes.toString());
    }

    @Test
    public void testEqualsAttributes() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        attributes2.put("key1", "value1");
        attributes2.put("key3", "value3");
        attributes3.put("key2", "value2");
        attributes3.put("key3", "value3");
        Assert.assertEquals(attributes1, attributes1);
        Assert.assertEquals(attributes1, attributes2);
        Assert.assertNotEquals(attributes1, attributes3);
    }

    @Test
    public void testHashcodeAttributes() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        attributes2.put("key2", "value2");
        attributes2.put("key3", "value3");
        attributes3.put("key3", "value3");
        attributes3.put("key4", "value4");
        Assert.assertEquals(attributes1.hashCode(), attributes1.hashCode());
        Assert.assertEquals(attributes1.hashCode(), attributes2.hashCode());
        Assert.assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
    }

    @Test
    public void testCloneAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes clonedAttributes = attributes.clone();
        Assert.assertEquals(attributes, clonedAttributes);
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.normalize();
        Assert.assertEquals("key1=\"value1\"", attributes.toString());
    }

    @Test
    public void testCapacityAttributes() {
        Attributes attributes = new Attributes();
        for (int i = 0; i < 20; i++) {
            attributes.put("key" + i, "value" + i);
        }
        Assert.assertEquals(4 * 5, attributes.keys.length);
        Assert.assertEquals(4 * 5, attributes.vals.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsExceptionAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove(attributes.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.put("key", "value1");
    }

    @Test(expected = SerializationException.class)
    public void testSerializationExceptionAttributes() {
        Attributes attributes = new Attributes();
        Appendable appendable = StringUtil.borrowBuilder();
        try {
            attributes.html(appendable, null);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }
}