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
        Assert.assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutBooleanMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKey("key") || attributes.size() > 0); // check size for empty set
        attributes.put("key", false);
        Assert.assertFalse(attributes.hasKey("key"));
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

    @Test(expected = NullPointerException.class)
    public void testRemoveNullKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.remove(null);
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKey("key"));
        attributes.put("key", "value");
        Assert.assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
        attributes.put("key", "value");
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        Assert.assertEquals(2, attributes1.size());
        Assert.assertTrue(attributes1.hasKey("key1"));
        Assert.assertTrue(attributes1.hasKey("key2"));
    }

    @Test(expected = NullPointerException.class)
    public void testAddAllNullAttributesMethod() {
        Attributes attributes = new Attributes();
        attributes.addAll(null);
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "value");
        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals("value", dataset.get("key"));
    }

    @Test
    public void testHtmlMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String html = attributes.html();
        Assert.assertEquals("<key>value</key>", html);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlExceptionMethod() {
        Attributes attributes = new Attributes();
        String html = attributes.html();
    }

    @Test
    public void testToStringMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.toString();
        Assert.assertEquals("<attributes><key>value</key></attributes>", result);
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneMethod() throws CloneNotSupportedException {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Attributes clone = attributes.clone();
        Assert.assertNotSame(attributes, clone);
        Assert.assertEquals("value", clone.get("key"));
    }
}