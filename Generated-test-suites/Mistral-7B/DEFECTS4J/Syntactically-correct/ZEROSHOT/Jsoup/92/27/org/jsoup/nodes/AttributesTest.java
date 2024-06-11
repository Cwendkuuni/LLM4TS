package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.jsoup.nodes.Attributes.dataPrefix;
import static org.jsoup.nodes.Attributes.Empty;
import static org.jsoup.nodes.Attributes.InitialCapacity;
import static org.jsoup.nodes.Attributes.NotFound;

public class AttributesTest {

    @Test
    public void testInitialization() {
        Attributes attributes = new Attributes();
        Assert.assertArrayEquals(Empty, attributes.keys);
        Assert.assertArrayEquals(Empty, attributes.vals);
        Assert.assertEquals(0, attributes.size);
    }

    @Test
    public void testCapacity() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        attributes.checkCapacity(5);
        Assert.assertArrayEquals(new String[]{"key1", "key2", "key3", EmptyString, EmptyString}, attributes.keys);
        Assert.assertArrayEquals(new String[]{"value1", "value2", "value3", EmptyString, EmptyString}, attributes.vals);
        Assert.assertEquals(5, attributes.size);
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
        attributes.put("key1", "value1");
        Assert.assertEquals("value1", attributes.getIgnoreCase("key1"));
        Assert.assertEquals(EmptyString, attributes.getIgnoreCase("Key2"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        Assert.assertEquals("value2", attributes.get("key1"));

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");
        attributes.putAll(attributes2);
        Assert.assertEquals(2, attributes.size());
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key1", "value1");
        Assert.assertEquals("value1", attributes.get("key1"));

        Attributes attributes2 = new Attributes();
        attributes2.putIgnoreCase("Key1", "value1");
        attributes2.putIgnoreCase("Key2", "value2");
        attributes.putAll(attributes2);
        Assert.assertEquals(2, attributes.size());
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        Assert.assertFalse(attributes.get("key1").isEmpty());
        Assert.assertEquals(EmptyString, attributes.get("key1"));
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testPutBooleanIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("KEY1", true);
        Assert.assertFalse(attributes.get("key1").isEmpty());
        Assert.assertEquals(EmptyString, attributes.get("key1"));
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testPutAndRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertTrue(attributes.hasKey("key2"));

        attributes.remove("key1");
        Assert.assertEquals(EmptyString, attributes.get("key1"));
        Assert.assertTrue(attributes.hasKey("key2"));

        attributes.remove("key2");
        Assert.assertEquals(EmptyString, attributes.get("key1"));
        Assert.assertEquals(EmptyString, attributes.get("key2"));
        Assert.assertFalse(attributes.hasKey("key1"));
        Assert.assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKey("key1"));

        attributes.put("key1", "value1");
        Assert.assertTrue(attributes.hasKey("key1"));

        attributes.remove("key1");
        Assert.assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("KEY1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key2"));

        attributes.remove("key1");
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("Key1"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        Assert.assertEquals(1, attributes.size());

        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());

        attributes.remove("key1");
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");

        attributes1.addAll(attributes2);
        Assert.assertEquals("value1", attributes1.get("key1"));
        Assert.assertEquals("value2", attributes1.get("key2"));
        Assert.assertEquals("value3", attributes1.get("key3"));
        Assert.assertEquals(3, attributes1.size());

        Assert.assertEquals(EmptyString, attributes2.get("key1"));
        Assert.assertEquals(EmptyString, attributes2.get("key2"));
        Assert.assertEquals("value3", attributes2.get("key3"));
        Assert.assertEquals(1, attributes2.size());
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Iterator<Attribute> iterator = attributes.iterator();
        List<Attribute> attributesList = new ArrayList<>();
        while (iterator.hasNext()) {
            attributesList.add(iterator.next());
        }

        Assert.assertEquals(2, attributesList.size());
        Assert.assertEquals("key1", attributesList.get(0).getKey());
        Assert.assertEquals("value1", attributesList.get(0).getValue());
        Assert.assertEquals("key2", attributesList.get(1).getKey());
        Assert.assertEquals("value2", attributesList.get(1).getValue());
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> attributesList = attributes.asList();
        Assert.assertEquals(2, attributesList.size());
        Assert.assertEquals("key1", attributesList.get(0).getKey());
        Assert.assertEquals("value1", attributesList.get(0).getValue());
        Assert.assertEquals("key2", attributesList.get(1).getKey());
        Assert.assertEquals("value2", attributesList.get(1).getValue());

        attributes.remove("key1");
        attributesList = attributes.asList();
        Assert.assertEquals(1, attributesList.size());
        Assert.assertEquals("key2", attributesList.get(0).getKey());
        Assert.assertEquals("value2", attributesList.get(0).getValue());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals("key1", dataset.keySet().toArray()[0].toString());
        Assert.assertEquals("value1", dataset.get("key1"));
        Assert.assertEquals("key2", dataset.keySet().toArray()[1].toString());
        Assert.assertEquals("value2", dataset.get("key2"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        StringBuilder sb = new StringBuilder();
        attributes.html(sb, new Document.OutputSettings());

        Assert.assertEquals("<key1 value=\"value1\">", sb.toString());
        Assert.assertEquals("<key2 value=\"value2\">", sb.toString() + " key2=\"value2\">");
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertEquals("<key1 value=\"value1\">\n<key2 value=\"value2\">", attributes.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();

        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        attributes2.put("key1", "value1");
        attributes2.put("key3", "value3");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value1");
        attributes3.put("key2", "value2");

        Assert.assertEquals(attributes1, attributes1);
        Assert.assertNotEquals(attributes1, null);
        Assert.assertNotEquals(attributes1, attributes2);
        Assert.assertNotEquals(attributes1, attributes3);
        Assert.assertEquals(attributes1, attributes3);
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();

        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        attributes2.put("key1", "value1");
        attributes2.put("key3", "value3");

        Assert.assertEquals(attributes1.hashCode(), attributes1.hashCode());
        Assert.assertEquals(attributes1.hashCode(), attributes3.hashCode());
        Assert.assertEquals(attributes1.hashCode(), attributes2.hashCode());
        Assert.assertEquals(attributes1.hashCode(), new Attributes().hashCode());
        Assert.assertNotEquals(attributes1.hashCode(), new Object().hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        Attributes clonedAttributes = attributes.clone();

        Assert.assertEquals(attributes.size(), clonedAttributes.size());
        Assert.assertEquals("value1", clonedAttributes.get("key1"));
        Assert.assertEquals("value1", attributes.get("key1"));

        attributes.remove("key1");
        Assert.assertFalse(clonedAttributes.hasKey("key1"));
        Assert.assertTrue(attributes.hasKey("key1"));
    }
}