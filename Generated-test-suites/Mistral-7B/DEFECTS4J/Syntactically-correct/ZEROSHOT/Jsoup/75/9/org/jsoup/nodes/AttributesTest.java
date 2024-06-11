package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RunWith(Parameterized.class)
public class AttributesTest {

    private Attributes attributes;

    @Parameterized.Parameters(name = "{index}: test case {0}")
    public static Collection<Attributes> data() {
        return Arrays.asList(new Attributes(), new Attributes().put("key1", "value1"), new Attributes().put("key2", "value2"), new Attributes().put("key1", "value3"));
    }

    public AttributesTest(Attributes attributes) {
        this.attributes = attributes;
    }

    @Test
    public void testGet() {
        Assert.assertEquals("", attributes.get(""));
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertEquals("value3", attributes.get("key1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidKey() {
        attributes.get("invalidKey");
    }

    @Test
    public void testGetIgnoreCase() {
        Assert.assertEquals("value1", attributes.getIgnoreCase("key1"));
        Assert.assertEquals("value2", attributes.getIgnoreCase("key2"));
        Assert.assertEquals("value3", attributes.getIgnoreCase("key1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetIgnoreCaseInvalidKey() {
        attributes.getIgnoreCase("invalidKey");
    }

    @Test
    public void testPut() {
        attributes.put("key3", "value3");
        Assert.assertEquals("value3", attributes.get("key3"));
    }

    @Test
    public void testPutOverwrite() {
        attributes.put("key1", "value4");
        Assert.assertEquals("value4", attributes.get("key1"));
    }

    @Test
    public void testPutIgnoreCase() {
        attributes.putIgnoreCase("Key3", "value3");
        Assert.assertEquals("value3", attributes.getIgnoreCase("key3"));
        Assert.assertEquals("value3", attributes.getIgnoreCase("Key3"));
    }

    @Test
    public void testPutOverwriteIgnoreCase() {
        attributes.putIgnoreCase("key1", "value4");
        Assert.assertEquals("value4", attributes.getIgnoreCase("key1"));
        Assert.assertEquals("value4", attributes.getIgnoreCase("Key1"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key4", false);
        Assert.assertFalse(attributes.get("key4") == null);
        Assert.assertFalse(attributes.hasKey("key4"));
        attributes.put("key4", true);
        Assert.assertTrue(attributes.get("key4") == null);
        Assert.assertTrue(attributes.hasKey("key4"));
    }

    @Test
    public void testPutBooleanOverwrite() {
        attributes.put("key4", false);
        attributes.put("key4", true);
        Assert.assertTrue(attributes.get("key4") == null);
        Assert.assertTrue(attributes.hasKey("key4"));
    }

    @Test
    public void testPutBooleanIgnoreCase() {
        attributes.putIgnoreCase("key4", false);
        Assert.assertFalse(attributes.getIgnoreCase("key4") == null);
        Assert.assertFalse(attributes.hasKey("key4"));
        attributes.putIgnoreCase("key4", true);
        Assert.assertTrue(attributes.getIgnoreCase("key4") == null);
        Assert.assertTrue(attributes.hasKey("key4"));
    }

    @Test
    public void testPutBooleanOverwriteIgnoreCase() {
        attributes.putIgnoreCase("key4", false);
        attributes.putIgnoreCase("key4", true);
        Assert.assertTrue(attributes.getIgnoreCase("key4") == null);
        Assert.assertTrue(attributes.hasKey("key4"));
    }

    @Test
    public void testHasKey() {
        Assert.assertFalse(attributes.hasKey(""));
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertTrue(attributes.hasKey("key2"));
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertFalse(attributes.hasKey("key3"));
        Assert.assertFalse(attributes.hasKey("invalidKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Assert.assertFalse(attributes.hasKeyIgnoreCase(""));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key2"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key3"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("invalidKey"));
    }

    @Test
    public void testSize() {
        Assert.assertEquals(0, attributes.size());
        Assert.assertEquals(2, new Attributes().put("key1", "value1").put("key2", "value2").size());
        Assert.assertEquals(3, new Attributes().put("key1", "value1").put("key2", "value2").put("key1", "value3").size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes2 = new Attributes().put("key3", "value3").put("key4", "value4");
        attributes.addAll(attributes2);
        Assert.assertEquals(4, attributes.size());
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertTrue(attributes.hasKey("key2"));
        Assert.assertTrue(attributes.hasKey("key3"));
        Assert.assertTrue(attributes.hasKey("key4"));
    }

    @Test
    public void testIterator() {
        Map<String, String> expected = new HashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        expected.put("key1", "value3");

        for (Map.Entry<String, String> entry : attributes.asList().iterator()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Assert.assertEquals(expected.get(key), value);
            expected.remove(key);
        }

        Assert.assertEquals(0, expected.size());
    }

    @Test
    public void testAsList() {
        List<Attribute> expectedList = Arrays.asList(new Attribute("key1", "value1"), new Attribute("key2", "value2"), new Attribute("key1", "value3"));
        List<Attribute> actualList = attributes.asList();

        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testDataset() {
        Map<String, String> expectedDataset = new HashMap<>();
        expectedDataset.put("data-key1", "value1");
        expectedDataset.put("data-key2", "value2");
        expectedDataset.put("data-key1", "value3");

        Map<String, String> dataset = attributes.dataset();

        Assert.assertEquals(expectedDataset, dataset);
    }

    @Test
    public void testHtml() {
        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key1=\"value1\" key2=\"value2\" key1=\"value3\"");
        Assert.assertEquals(expectedHtml.toString(), attributes.html());
    }

    @Test
    public void testToString() {
        Assert.assertEquals(attributes.html(), attributes.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes2 = new Attributes().put("key1", "value1").put("key2", "value2").put("key1", "value3");
        Assert.assertEquals(attributes, attributes);
        Assert.assertEquals(attributes, attributes);
        Assert.assertEquals(attributes, attributes2);
        Assert.assertEquals(attributes2, attributes);

        Attributes attributes3 = new Attributes().put("key1", "value1").put("key2", "value4");
        Assert.assertNotEquals(attributes, attributes3);

        Attributes attributes4 = new Attributes();
        Assert.assertNotEquals(attributes, attributes4);
    }

    @Test
    public void testHashCode() {
        Attributes attributes2 = new Attributes().put("key1", "value1").put("key2", "value2").put("key1", "value3");
        Assert.assertEquals(attributes.hashCode(), attributes.hashCode());
        Assert.assertEquals(attributes.hashCode(), attributes2.hashCode());
        Assert.assertEquals(attributes2.hashCode(), attributes.hashCode());

        Attributes attributes3 = new Attributes().put("key1", "value1").put("key2", "value4");
        Assert.assertNotEquals(attributes.hashCode(), attributes3.hashCode());

        Attributes attributes4 = new Attributes();
        Assert.assertNotEquals(attributes.hashCode(), attributes4.hashCode());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attributes attributes2 = new Attributes().put("key1", "value1").put("key2", "value2").put("key1", "value3").clone();
        Assert.assertEquals(attributes, attributes2);
        Assert.assertEquals(attributes2, attributes);
    }
}