package org.jsoup.nodes;

import org.jsoup.SerializationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RunWith(Parameterized.class)
public class AttributesTest {

    private Attributes attributes;
    private String key;
    private String value;

    public AttributesTest(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Parameters(name = "{0}={1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"key1", "value1"},
                {"key2", "value2"},
                {"key3", "value3"},
                {"key4", "value4"}
        });
    }

    @Test
    public void testGet() {
        attributes = new Attributes();
        attributes.put(key, value);
        String retrievedValue = attributes.get(key);
        Assert.assertEquals(value, retrievedValue);
    }

    @Test
    public void testGetIgnoreCase() {
        attributes = new Attributes();
        attributes.put(key, value);
        String retrievedValue = attributes.getIgnoreCase(key.toUpperCase());
        Assert.assertEquals(value, retrievedValue);
    }

    @Test
    public void testHasKey() {
        attributes = new Attributes();
        attributes.put(key, value);
        boolean hasKey = attributes.hasKey(key);
        Assert.assertTrue(hasKey);

        hasKey = attributes.hasKeyIgnoreCase(key.toUpperCase());
        Assert.assertTrue(hasKey);
    }

    @Test
    public void testHasKeyIgnoreCaseFalse() {
        attributes = new Attributes();
        boolean hasKey = attributes.hasKeyIgnoreCase("unknownKey");
        Assert.assertFalse(hasKey);
    }

    @Test
    public void testSize() {
        attributes = new Attributes();
        for (String testKey : new String[]{"key1", "key2", "key3", "key4"}) {
            attributes.put(testKey, "value");
        }
        int size = attributes.size();
        Assert.assertEquals(4, size);
    }

    @Test
    public void testPut() {
        attributes = new Attributes();
        attributes.put(key, value);
        String retrievedValue = attributes.get(key);
        Assert.assertEquals(value, retrievedValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKey() {
        attributes = new Attributes();
        attributes.put(null, value);
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullValue() {
        attributes = new Attributes();
        attributes.put(key, null);
    }

    @Test
    public void testPutExistingKey() {
        attributes = new Attributes();
        attributes.put(key, value);
        attributes.put(key, "newValue");
        String retrievedValue = attributes.get(key);
        Assert.assertEquals("newValue", retrievedValue);
    }

    @Test
    public void testPutIgnoreCaseExistingKey() {
        attributes = new Attributes();
        attributes.put(key, value);
        String newKey = key.toUpperCase();
        attributes.put(newKey, "newValue");
        String retrievedValue = attributes.getIgnoreCase(key);
        Assert.assertEquals("newValue", retrievedValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutIgnoredCaseExistingKeyDifferent() {
        attributes = new Attributes();
        attributes.put(key, value);
        String newKey = key + "different";
        attributes.put(newKey, "newValue");
        String retrievedValue = attributes.getIgnoreCase(key);
        Assert.assertNull(retrievedValue);
    }

    @Test
    public void testPutDataAttribute() {
        attributes = new Attributes();
        String dataKey = "data:" + key;
        attributes.put(dataKey, value);
        Map<String, String> dataset = attributes.dataset();
        String retrievedValue = dataset.get(key);
        Assert.assertEquals(value, retrievedValue);
    }

    @Test
    public void testPutDataAttributeExisting() {
        attributes = new Attributes();
        String dataKey1 = "data:" + key;
        attributes.put(dataKey1, value);
        String dataKey2 = "data:" + key + "_new";
        attributes.put(dataKey2, "newValue");
        Map<String, String> dataset = attributes.dataset();
        String retrievedValue = dataset.get(key);
        Assert.assertEquals("newValue", retrievedValue);
    }

    @Test
    public void testRemove() {
        attributes = new Attributes();
        attributes.put(key, value);
        attributes.remove(key);
        boolean hasKey = attributes.hasKey(key);
        Assert.assertFalse(hasKey);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullKey() {
        attributes = new Attributes();
        attributes.remove(null);
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes = new Attributes();
        attributes.put(key, value);
        attributes.removeIgnoreCase(key.toUpperCase());
        boolean hasKey = attributes.hasKey(key);
        Assert.assertFalse(hasKey);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveIgnoreCaseDifferent() {
        attributes = new Attributes();
        attributes.put(key, value);
        attributes.removeIgnoreCase("unknownKey");
        boolean hasKey = attributes.hasKey(key);
        Assert.assertTrue(hasKey);
    }

    @Test
    public void testDataset() {
        attributes = new Attributes();
        for (String testKey : new String[]{"dataKey1", "dataKey2", "dataKey3", "dataKey4"}) {
            attributes.put(testKey, "value");
        }
        Map<String, String> dataset = attributes.dataset();
        for (Map.Entry<String, String> entry : dataset.entrySet()) {
            String testKey = entry.getKey();
            String testValue = entry.getValue();
            Assert.assertEquals("value", testValue);
            Assert.assertTrue(testKey.startsWith("data:"));
        }
    }

    @Test
    public void testHtml() throws IOException {
        attributes = new Attributes();
        for (String testKey : new String[]{"key1", "key2", "key3", "key4"}) {
            attributes.put(testKey, "value");
        }
        StringBuilder accum = new StringBuilder();
        attributes.html(accum, null);
        String html = accum.toString();
        String expectedHtml = " key1=\"value\" key2=\"value\" key3=\"value\" key4=\"value\"";
        Assert.assertEquals(expectedHtml, html);
    }

    @Test
    public void testDatasetEmpty() {
        attributes = new Attributes();
        Map<String, String> dataset = attributes.dataset();
        Assert.assertTrue(dataset.isEmpty());
    }

    @Test
    public void testSizeAfterRemove() {
        attributes = new Attributes();
        for (String testKey : new String[]{"key1", "key2", "key3", "key4"}) {
            attributes.put(testKey, "value");
        }
        attributes.remove(key);
        int size = attributes.size();
        Assert.assertEquals(3, size);
    }

    @Test
    public void testAddAll() {
        Attributes newAttributes = new Attributes();
        for (String testKey : new String[]{"newKey1", "newKey2", "newKey3", "newKey4"}) {
            newAttributes.put(testKey, "value");
        }
        attributes.addAll(newAttributes);
        int size = attributes.size();
        Assert.assertEquals(8, size);
    }

    @Test
    public void testIterator() {
        attributes = new Attributes();
        for (String testKey : new String[]{"key1", "key2", "key3", "key4"}) {
            attributes.put(testKey, "value");
        }
        List<Attribute> asList = attributes.asList();
        int size = asList.size();
        Assert.assertEquals(4, size);
        for (Attribute attribute : asList) {
            Assert.assertNotNull(attribute);
            Assert.assertEquals(testKey, attribute.getKey());
            Assert.assertEquals(value, attribute.getValue());
        }
    }

    @Test
    public void testClone() {
        attributes = new Attributes();
        for (String testKey : new String[]{"key1", "key2", "key3", "key4"}) {
            attributes.put(testKey, "value");
        }
        Attributes clonedAttributes = attributes.clone();
        int size = clonedAttributes.size();
        Assert.assertEquals(4, size);
        for (Attribute attribute : clonedAttributes) {
            Assert.assertNotNull(attribute);
            Assert.assertEquals(testKey, attribute.getKey());
            Assert.assertEquals(value, attribute.getValue());
        }
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        for (String testKey : new String[]{"key1", "key2", "key3", "key4"}) {
            attributes1.put(testKey, "value");
        }
        Attributes attributes2 = new Attributes();
        for (String testKey : new String[]{"key1", "key2", "key3", "key4"}) {
            attributes2.put(testKey, "value");
        }
        boolean equals = attributes1.equals(attributes2);
        Assert.assertTrue(equals);

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value1");
        attributes3.put("key2", "value2");
        boolean notEquals = attributes1.equals(attributes3);
        Assert.assertFalse(notEquals);

        Attributes attributes4 = new Attributes();
        boolean equalsNull = attributes1.equals(null);
        Assert.assertFalse(equalsNull);
    }

    @Test
    public void testHashcode() {
        Attributes attributes1 = new Attributes();
        for (String testKey : new String[]{"key1", "key2", "key3", "key4"}) {
            attributes1.put(testKey, "value");
        }
        Attributes attributes2 = new Attributes();
        for (String testKey : new String[]{"key1", "key2", "key3", "key4"}) {
            attributes2.put(testKey, "value");
        }
        int hashcode1 = attributes1.hashCode();
        int hashcode2 = attributes2.hashCode();
        Assert.assertEquals(hashcode1, hashcode2);

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value1");
        attributes3.put("key2", "value2");
        int notEqualHashcode = attributes1.hashCode();
        Assert.assertNotEquals(notEqualHashcode, attributes3.hashCode());

        Attributes attributes4 = new Attributes();
        int hashcodeNull = attributes1.hashCode();
        Assert.assertNotEquals(hashcodeNull, attributes1.hashCode());
    }
}