import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;

public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test (expected = NullPointerException.class)
    public void testPutNullKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test
    public void testPutBooleanMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("key")); // case insensitive
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
    }

    @Test (expected = NullPointerException.class)
    public void testRemoveNullKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.remove(null);
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKeyIgnoreCase("Key2"));
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);
        assertEquals(2, attributes3.size());
        assertTrue(attributes3.hasKey("key1"));
        assertTrue(attributes3.hasKey("key2"));
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "value");
        Map<String, String> dataset = attributes.dataset();
        assertEquals("value", dataset.get("key"));
    }

    @Test
    public void testHtmlMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("<key>value</key>", attributes.html());
    }

    @Test (expected = SerializationException.class)
    public void testHtmlMethodWithException() {
        Attributes attributes = new Attributes();
        throw new SerializationException("Test exception");
    }
}