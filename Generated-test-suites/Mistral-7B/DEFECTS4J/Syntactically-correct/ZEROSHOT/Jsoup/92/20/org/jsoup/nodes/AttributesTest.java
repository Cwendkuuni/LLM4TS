import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String getValue = attributes.get("key");
        Assert.assertEquals("value", getValue);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");
        String getValueIgnoreCase = attributes.getIgnoreCase("key");
        Assert.assertEquals("VALUE", getValueIgnoreCase);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        String getValue = attributes.get("key");
        Assert.assertEquals("value2", getValue);
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE1");
        attributes.put("key", "VALUE2");
        String getValueIgnoreCase = attributes.getIgnoreCase("key");
        Assert.assertEquals("VALUE2", getValueIgnoreCase);
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        boolean value = attributes.get("key").equals("true");
        Assert.assertTrue(value);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean hasKey = attributes.hasKey("key");
        Assert.assertTrue(hasKey);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");
        boolean hasKeyIgnoreCase = attributes.hasKeyIgnoreCase("key");
        Assert.assertTrue(hasKeyIgnoreCase);
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
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        int size = attributes1.size();
        Assert.assertEquals(2, size);
        String getValue1 = attributes1.get("key1");
        Assert.assertEquals("value1", getValue1);
        String getValue2 = attributes1.get("key2");
        Assert.assertEquals("value2", getValue2);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        Map<String, String> dataset = attributes.dataset();
        int size = dataset.size();
        Assert.assertEquals(2, size);
        String getValue1 = dataset.get("data-key1");
        Assert.assertEquals("value1", getValue1);
        String getValue2 = dataset.get("data-key2");
        Assert.assertEquals("value2", getValue2);
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        String toString = attributes.toString();
        String expected = "attributes[key1=\"value1\", key2=\"value2\"]";
        Assert.assertEquals(expected, toString);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        Assert.assertTrue(attributes1.equals(attributes2));

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value2");
        Assert.assertFalse(attributes1.equals(attributes3));
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes clonedAttributes = attributes.clone();
        int size = clonedAttributes.size();
        Assert.assertEquals(1, size);
        String getValue = clonedAttributes.get("key1");
        Assert.assertEquals("value1", getValue);
    }
}