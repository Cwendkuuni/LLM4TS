package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class AttributesTest {

    @Test
    public void testGetAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String gettingValue = attributes.get("key");
        Assert.assertEquals("value", gettingValue);

        String gettingEmptyString = attributes.get("emptyKey");
        Assert.assertEquals("", gettingEmptyString);

        String gettingNull = attributes.get("nullKey");
        Assert.assertEquals("", gettingNull);
    }

    @Test
    public void testGetIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");

        String gettingValueIgnoreCase = attributes.getIgnoreCase("Key");
        Assert.assertEquals("value", gettingValueIgnoreCase);

        String gettingEmptyStringIgnoreCase = attributes.getIgnoreCase("EMPTYKEY");
        Assert.assertEquals("", gettingEmptyStringIgnoreCase);

        String gettingNullIgnoreCase = attributes.getIgnoreCase("nullKey");
        Assert.assertEquals("", gettingNullIgnoreCase);
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String gettingValue1 = attributes.get("key1");
        Assert.assertEquals("value1", gettingValue1);

        String gettingValue2 = attributes.get("key2");
        Assert.assertEquals("value2", gettingValue2);

        String gettingEmptyString = attributes.get("emptyKey");
        Assert.assertEquals("", gettingEmptyString);

        attributes.put("key1", "newValue1");

        String gettingNewValue1 = attributes.get("key1");
        Assert.assertEquals("newValue1", gettingNewValue1);
    }

    @Test
    public void testPutIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();

        attributes.put("KEY1", "value1");
        attributes.put("KEY2", "value2");

        String gettingValue1 = attributes.getIgnoreCase("KEY1");
        Assert.assertEquals("value1", gettingValue1);

        String gettingValue2 = attributes.getIgnoreCase("KEY2");
        Assert.assertEquals("value2", gettingValue2);

        String gettingEmptyString = attributes.getIgnoreCase("EMPTYKEY");
        Assert.assertEquals("", gettingEmptyString);

        attributes.putIgnoreCase("Key1", "newValue1");

        String gettingNewValue1 = attributes.getIgnoreCase("Key1");
        Assert.assertEquals("newValue1", gettingNewValue1);
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();

        attributes.put("booleanKey", true);
        Assert.assertTrue(attributes.hasKeyIgnoreCase("booleanKey"));
        Assert.assertFalse(attributes.get("booleanKey").isEmpty());

        attributes.put("booleanKey", false);
        Assert.assertFalse(attributes.hasKeyIgnoreCase("booleanKey"));
        Assert.assertTrue(attributes.get("booleanKey").isEmpty());
    }

    @Test
    public void testPutAndRemoveAttribute() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        Assert.assertEquals("value1", attributes.get("key1"));

        attributes.remove("key1");
        Assert.assertFalse(attributes.hasKey("key1"));

        attributes.put("key2", "value2");
        Assert.assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int size = attributes.size();
        Assert.assertEquals(2, size);
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();

        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        attributes1.addAll(attributes2);

        int size = attributes1.size();
        Assert.assertEquals(4, size);

        String gettingValue1 = attributes1.get("key1");
        Assert.assertEquals("value1", gettingValue1);

        String gettingValue2 = attributes1.get("key2");
        Assert.assertEquals("value2", gettingValue2);

        String gettingValue3 = attributes1.get("key3");
        Assert.assertEquals("value3", gettingValue3);

        String gettingValue4 = attributes1.get("key4");
        Assert.assertEquals("value4", gettingValue4);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        Assert.assertTrue(attributes.hasKey("key1"));

        attributes.put("key2", "value2");
        Assert.assertTrue(attributes.hasKey("key2"));

        attributes.put("key3", "value3");
        Assert.assertTrue(attributes.hasKey("key3"));

        Assert.assertFalse(attributes.hasKey("emptyKey"));
        Assert.assertFalse(attributes.hasKey("nullKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();

        attributes.put("KEY1", "value1");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("KEY1"));

        attributes.put("KEY2", "value2");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("KEY2"));

        attributes.put("KEY3", "value3");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("KEY3"));

        Assert.assertFalse(attributes.hasKeyIgnoreCase("emptyKey"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("nullKey"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");
        Assert.assertFalse(attributes.hasKey("key1"));

        Assert.assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();

        attributes.put("KEY1", "value1");
        attributes.put("KEY2", "value2");

        attributes.removeIgnoreCase("KEY1");
        Assert.assertFalse(attributes.hasKeyIgnoreCase("KEY1"));

        Assert.assertEquals("value2", attributes.get("KEY2"));
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        Attributes clonedAttributes = attributes.clone();

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertNotSame(attributes, clonedAttributes);
        Assert.assertEquals(attributes.size(), clonedAttributes.size());

        String gettingValue1 = attributes.get("key1");
        Assert.assertEquals("value1", gettingValue1);

        String gettingValue2 = attributes.get("key2");
        Assert.assertEquals("value2", gettingValue2);

        String gettingValue1Cloned = clonedAttributes.get("key1");
        Assert.assertNull(gettingValue1Cloned);

        String gettingValue2Cloned = clonedAttributes.get("key2");
        Assert.assertNull(gettingValue2Cloned);
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("KEY1", "value1");

        attributes.normalize();

        Assert.assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals("value1", dataset.get("data-key1"));
        Assert.assertEquals("value2", dataset.get("data-key2"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String html = attributes.html();

        Assert.assertEquals("<key1>value1</key1> <key2>value2</key2>", html);
    }
}