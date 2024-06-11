import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Attributes;

public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        String result = attributes.getIgnoreCase("key");
        Assert.assertEquals("Value", result);
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testPutBooleanMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKey("Key"));
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullValueMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        Assert.assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        attributes.removeIgnoreCase("key");
        Assert.assertFalse(attributes.hasKey("Key"));
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKey("Key"));
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("Key"));
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        Assert.assertEquals(2, attributes1.size());
        Assert.assertTrue(attributes1.hasKey("key1"));
        Assert.assertTrue(attributes1.hasKey("key2"));
    }

    @Test
    public void testIteratorMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Iterator<Attribute> iterator = attributes.iterator();
        Attribute attribute = iterator.next();
        Assert.assertEquals("key1", attribute.getKey());
        Assert.assertEquals("value1", attribute.getValue());
        attribute = iterator.next();
        Assert.assertEquals("key2", attribute.getKey());
        Assert.assertEquals("value2", attribute.getValue());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testAsListMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> list = attributes.asList();
        Attribute attribute = list.get(0);
        Assert.assertEquals("key1", attribute.getKey());
        Assert.assertEquals("value1", attribute.getValue());
        attribute = list.get(1);
        Assert.assertEquals("key2", attribute.getKey());
        Assert.assertEquals("value2", attribute.getValue());
        Assert.assertEquals(2, list.size());
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "data-value");
        Map<String, String> result = attributes.dataset();
        Assert.assertEquals("data-value", result.get("data-key"));
        Assert.assertEquals(1, result.size());
    }

    @Test
    public void testHtmlMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.html();
        Assert.assertEquals(" key=\"value\"", result);
    }

    @Test
    public void testEqualsMethod() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key2", "value2");
        Assert.assertEquals(attributes1, attributes2);
        Assert.assertNotEquals(attributes1, attributes3);
    }

    @Test
    public void testHashCodeMethod() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key2", "value2");
        Assert.assertEquals(attributes1.hashCode(), attributes2.hashCode());
        Assert.assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
    }
}